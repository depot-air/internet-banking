package com.dwidasa.engine.service.impl.facade;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.enums.LetterCodedLabeledEnum;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.ExtendedProperty;
import com.dwidasa.engine.dao.AccountTypeDao;
import com.dwidasa.engine.dao.BalanceDao;
import com.dwidasa.engine.dao.CustomerAccountDao;
import com.dwidasa.engine.dao.CustomerDao;
import com.dwidasa.engine.dao.CustomerDeviceDao;
import com.dwidasa.engine.dao.ProductDao;
import com.dwidasa.engine.dao.TransactionDao;
import com.dwidasa.engine.dao.TransactionDataDao;
import com.dwidasa.engine.dao.TransactionHistoryDao;
import com.dwidasa.engine.dao.TransactionTypeDao;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.Balance;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.CustomerAccount;
import com.dwidasa.engine.model.CustomerDevice;
import com.dwidasa.engine.model.Product;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.TransactionData;
import com.dwidasa.engine.model.TransactionHistory;
import com.dwidasa.engine.model.view.AccountSsppView;
import com.dwidasa.engine.model.view.AccountStatementView;
import com.dwidasa.engine.model.view.AccountView;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.CashInDelimaView;
import com.dwidasa.engine.model.view.LotteryTransactionView;
import com.dwidasa.engine.model.view.LotteryView;
import com.dwidasa.engine.model.view.PortfolioView;
import com.dwidasa.engine.model.view.ResultView;
import com.dwidasa.engine.model.view.TransferView;
import com.dwidasa.engine.model.view.VoucherPurchaseView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.IsoBitmapService;
import com.dwidasa.engine.service.ServiceLocator;
import com.dwidasa.engine.service.facade.AccountService;
import com.dwidasa.engine.service.facade.KioskCheckStatusService;
import com.dwidasa.engine.service.facade.NativeAdministrationService;
import com.dwidasa.engine.service.impl.view.KioskReprintMessageCustomizer;
import com.dwidasa.engine.service.impl.view.VoucherPurchaseViewServiceImpl;
import com.dwidasa.engine.service.link.CommLink;
import com.dwidasa.engine.service.link.MxCommLink;
import com.dwidasa.engine.service.transform.TransformerFactory;
import com.dwidasa.engine.service.view.BaseViewService;
import com.dwidasa.engine.service.view.MessageCustomizer;
import com.dwidasa.engine.util.DateUtils;
import com.dwidasa.engine.util.EngineUtils;
import com.dwidasa.engine.util.ReferenceGenerator;
import com.dwidasa.interlink.Listener;
import com.dwidasa.interlink.medium.Transporter;

/**
 * Implementation class of AccountService interface.
 * 
 * @author rk
 */
@Service("accountService")
public class AccountServiceImpl implements AccountService, Listener {
	private static Logger logger = Logger.getLogger(AccountServiceImpl.class);
	@Autowired
	private CustomerDao customerDao;

	@Autowired
	private CustomerAccountDao customerAccountDao;

	@Autowired
	private CustomerDeviceDao customerDeviceDao;

	@Autowired
	private TransactionHistoryDao transactionHistoryDao;

	@Autowired
	private TransactionDao transactionDao;

	@Autowired
	private TransactionDataDao transactionDataDao;

	@Autowired
	private AccountTypeDao accountTypeDao;

	@Autowired
	private TransactionTypeDao transactionTypeDao;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private BalanceDao balanceDao;

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private NativeAdministrationService nativeAdministrationService;

	@Autowired
	private ExtendedProperty extendedProperty;

	@Autowired
	private IsoBitmapService isoBitmapService;

	@Autowired
	private KioskCheckStatusService kioskCheckStatusService;

	private Transporter transporter;
	private String out = "";
	List<AccountSsppView> asvs = new ArrayList<AccountSsppView>();

	@SuppressWarnings("restriction")
	public AccountServiceImpl() {
		Security.addProvider(new com.sun.crypto.provider.SunJCE());
	}

	private String decryptTin(String tin) {
		String result;
		String key = cacheManager.getParameter("PRIVATE_KEY")
				.getParameterValue();
		tin += cacheManager.getParameter("PADDING_TIN").getParameterValue();

		try {
			Cipher cipher = Cipher.getInstance("DES");
			byte[] data = Hex.decodeHex(tin.toCharArray());
			SecretKeySpec k = new SecretKeySpec(
					Hex.decodeHex(key.toCharArray()), "DES");
			cipher.init(Cipher.DECRYPT_MODE, k);
			result = new String(Hex.encodeHex(cipher.doFinal(data)));
		} catch (NoSuchAlgorithmException e) {
			throw new BusinessException("IB-0500", "DECRY");
		} catch (NoSuchPaddingException e) {
			throw new BusinessException("IB-0500", "DECRY");
		} catch (InvalidKeyException e) {
			throw new BusinessException("IB-0500", "DECRY");
		} catch (IllegalBlockSizeException e) {
			throw new BusinessException("IB-0500", "DECRY");
		} catch (BadPaddingException e) {
			throw new BusinessException("IB-0500", "DECRY");
		} catch (DecoderException e) {
			throw new BusinessException("IB-0500", "DECRY");
		}

		return result.substring(0, 6);
	}

	/**
	 * Decrypt tin using symmetric private key.
	 * 
	 * @param tin
	 *            encrypted tin
	 * @return tin
	 */
	private String decryptTinFromEdc(String tin) {
		String result;
		String key = cacheManager.getParameter("PRIVATE_KEY")
				.getParameterValue();
		tin += cacheManager.getParameter("PADDING_TIN").getParameterValue();

		try {
			Cipher cipher = Cipher.getInstance("DES");
			byte[] data = Hex.decodeHex(tin.toCharArray());
			SecretKeySpec k = new SecretKeySpec(
					Hex.decodeHex(key.toCharArray()), "DES");
			cipher.init(Cipher.DECRYPT_MODE, k);
			result = new String(Hex.encodeHex(cipher.doFinal(data)));
		} catch (NoSuchAlgorithmException e) {
			throw new BusinessException("IB-0500", "DECRY");
		} catch (NoSuchPaddingException e) {
			throw new BusinessException("IB-0500", "DECRY");
		} catch (InvalidKeyException e) {
			throw new BusinessException("IB-0500", "DECRY");
		} catch (IllegalBlockSizeException e) {
			throw new BusinessException("IB-0500", "DECRY");
		} catch (BadPaddingException e) {
			throw new BusinessException("IB-0500", "DECRY");
		} catch (DecoderException e) {
			throw new BusinessException("IB-0500", "DECRY");
		}

		return result.substring(0, 6);
	}

	/**
	 * Generate username.
	 * 
	 * @param customerName
	 *            customer name
	 * @param cardNumber
	 *            card number
	 * @return generated user name
	 */
	private String generateUsername(String customerName, String cardNumber) {
		String nameNoSpace = customerName.replaceAll(" ", "").toUpperCase();
		int leftCount = 4;
		int count = 8;
		if (nameNoSpace.length() < 8) {
			count = nameNoSpace.length();
			leftCount = 12 - count;
		}

		return nameNoSpace.substring(0, count)
				+ cardNumber.substring(cardNumber.length() - leftCount);
	}

	/**
	 * Reactivation means re-register pre-existing user with same username and
	 * new activation code. 1. Find device that has not been activated 2. If
	 * found then reactivate that device 3. If not found then create new device
	 * 4. Old device will not be deleted To delete device(s) use deactivation
	 * 
	 * @param customerAccount
	 *            customer account object
	 * @param tin
	 *            string tin
	 * @param phoneNumber
	 *            string phone number
	 * @param cif
	 *            string cif number
	 * @param t
	 *            transaction object
	 */
	private void accountReactivation(CustomerAccount customerAccount,
			String tin, String phoneNumber, String cif, Transaction t) {
		String bit48 = t.getFreeData1();
		Customer c = customerDao.get(customerAccount.getCustomerId());
		updateCustomer(c, tin, phoneNumber, cif);

		customerDeviceDao.removeInactiveDevices(c.getId());

		CustomerDevice cd = registerDevices(customerAccount.getCardNumber(),
				customerAccount.getAccountNumber(), c);

		String tail = bit48.substring(105);
		bit48 = bit48.substring(0, 65);
		String feedBack = String.format("%1$-20s", c.getCustomerUsername());
		feedBack += String.format("%1$-20s", cd.getActivatePin());

		t.setFreeData1(bit48 + feedBack + tail);
	}

	private void accountReactivationIB(CustomerAccount customerAccount,
			String tin, String cif, Transaction t) {
		String bit48 = t.getFreeData1();
		Customer c = customerDao.get(customerAccount.getCustomerId());
		updateCustomerIB(c, tin, cif);

		customerDeviceDao.removeInactiveDevices(c.getId());

		CustomerDevice cd = registerDevices(customerAccount.getCardNumber(),
				customerAccount.getAccountNumber(), c);

		String tail = bit48.substring(105);
		bit48 = bit48.substring(0, 65);
		String feedBack = String.format("%1$-20s", c.getCustomerUsername());
		feedBack += String.format("%1$-20s", cd.getActivatePin());

		t.setFreeData1(bit48 + feedBack + tail);
	}

	/**
	 * Register customer devices. If CustomerDevice is provided, then just
	 * re-register that device -- This method will register only one device for
	 * NOW, but when IB is alive we MUST modify this code to add another device
	 * for soft token. For soft token, value for device id is customer's card
	 * number. --
	 * 
	 * @param cardNumber
	 *            card number
	 * @param accountNumber
	 *            account number
	 * @param customer
	 *            customer object
	 * @return customer device object
	 */
	private CustomerDevice registerDevices(String cardNumber,
			String accountNumber, Customer customer) {

		int expiredDays = Integer.parseInt(cacheManager.getParameter(
				"REGISTRATION_EXPIRED_DAYS").getParameterValue());

		CustomerDevice cd = new CustomerDevice();
		logger.info("customer=" + customer);
		logger.info("customer.getId()=" + customer.getId());
		cd.setCustomerId(customer.getId());

		if (EngineUtils.isMerchant(accountNumber)) {

			String terminalId = EngineUtils
					.generateTerminalIdByAccountNumber(accountNumber);
			cd.setTerminalId(terminalId);
		}

		cd.setActivatePin(RandomStringUtils.randomNumeric(6));
		cd.setStatus(Constants.INACTIVE_STATUS);
		cd.setExpiredDate(DateUtils.after(new Date(), expiredDays));

		cd.setCreated(new Date());
		cd.setCreatedby(0L);
		cd.setUpdated(new Date());
		cd.setUpdatedby(0L);
		cd = customerDeviceDao.save(cd);
		logger.info("cd=" + cd);

		registerSoftToken(cardNumber, accountNumber, cd);
		return cd;
	}
	
	

	private CustomerDevice registerDevicesIB(String cardNumber,
			String accountNumber, Customer customer) {

		int expiredDays = Integer.parseInt(cacheManager.getParameter(
				"REGISTRATION_EXPIRED_DAYS").getParameterValue());

		CustomerDevice cd = new CustomerDevice();
		cd.setCustomerId(customer.getId());

		cd.setTerminalId(Constants.IBS_SERVER);
		// cd.setActivatePin(RandomStringUtils.randomNumeric(6));
		cd.setStatus(Constants.INACTIVE_STATUS);
		cd.setExpiredDate(DateUtils.after(new Date(), expiredDays));

		cd.setCreated(new Date());
		cd.setCreatedby(0L);
		cd.setUpdated(new Date());
		cd.setUpdatedby(0L);
		customerDeviceDao.save(cd);

		return cd;
	}

	/**
	 * Register soft token device 1a if soft token device is not yet registered
	 * then register new one 1b then also automatically activate it (register it
	 * to OTP) 2 if soft token device already registered then just update the
	 * activationCode to be used for resetting device
	 * 
	 * @param cardNumber
	 * @param accountNumber
	 * @param device
	 */
	private void registerSoftToken(String cardNumber, String accountNumber,
			CustomerDevice device) {

		CustomerDevice tokenDevice = customerDeviceDao.get(cardNumber);
		boolean needActivation = false;
		if (tokenDevice == null) {
			tokenDevice = new CustomerDevice();
			tokenDevice.setCustomerId(device.getCustomerId());
			tokenDevice.setDeviceId(cardNumber);
			tokenDevice.setIme(cardNumber);

			if (EngineUtils.isMerchant(accountNumber)) {

				String terminalId = EngineUtils
						.generateTerminalIdByAccountNumber(accountNumber);
				tokenDevice.setTerminalId(terminalId);
			}

			tokenDevice.setCreated(new Date());
			tokenDevice.setCreatedby(0L);

			needActivation = true;
		}

		tokenDevice.setActivatePin(device.getActivatePin());
		tokenDevice.setExpiredDate(new Date());
		tokenDevice.setStatus(Constants.ACTIVE_STATUS);
		tokenDevice.setUpdated(new Date());
		tokenDevice.setUpdatedby(0L);
		tokenDevice = customerDeviceDao.save(tokenDevice);
		logger.info("tokenDevice=" + tokenDevice);

		if (needActivation) {
			nativeAdministrationService
					.activateSoftToken(device.getCustomerId(), cardNumber,
							device.getActivatePin());
		}
	}

	private void createCustomer(String customerName, String cardNumber,
			String tin, String phoneNumber, String cif, Customer customer) {

		String username = generateUsername(customerName, cardNumber);
		logger.info("username=" + username);

		// check for customer_username
		while (customerDao.get(username) != null) {
			username = username.substring(0, 10)
					+ RandomStringUtils.randomNumeric(2);
		}

		customer.setCustomerName(customerName);
		customer.setCustomerUsername(username);
		customer.setCustomerPin(tin);
		customer.setCustomerPhone(phoneNumber);
		customer.setCifNumber(cif);
		customer.setFailedAuthAttempts(0);
		customer.setFirstLogin("Y");
		customer.setTokenActivated("N");
		customer.setMobileWebTokenId(cardNumber);
		customer.setStatus(Constants.ACTIVE_STATUS);
		customer.setCreated(new Date());
		customer.setCreatedby(0L);
		customer.setUpdated(new Date());
		customer.setUpdatedby(0L);
		Customer cust = customerDao.save(customer);
		logger.info("cust=" + cust.getCustomerUsername());
	}

	private Customer createCustomerSilentRegistration(String customerName,
			String cardNumber, String tin, String phoneNumber, String cif,
			Customer customer) {

		String username = generateUsername(customerName, cardNumber);

		// check for customer_username
		while (customerDao.get(username) != null) {
			username = username.substring(0, 10)
					+ RandomStringUtils.randomNumeric(2);
		}

		customer.setCustomerName(customerName);
		customer.setCustomerUsername(username);
		customer.setCustomerPin(tin);
		customer.setCustomerPhone(phoneNumber);
		customer.setCifNumber(cif);
		customer.setFailedAuthAttempts(0);
		customer.setFirstLogin("Y");
		customer.setTokenActivated("N");
		customer.setMobileWebTokenId(cardNumber);
		customer.setStatus(Constants.INACTIVE_STATUS); // set inactive status,
														// harus registrasi
														// lewat EDC /ATM
		customer.setCreated(new Date());
		customer.setCreatedby(0L);
		customer.setUpdated(new Date());
		customer.setUpdatedby(0L);
		customerDao.insertCustomerRegisterSilent(customer);
		return customer;
	}

	private void updateCustomer(Customer customer, String tin,
			String phoneNumber, String cif) {
		customer.setCustomerPin(tin);
		customer.setCustomerPhone(phoneNumber);
		customer.setCifNumber(cif);
		customer.setFailedAuthAttempts(0);
		customer.setStatus(Constants.ACTIVE_STATUS);
		customer.setUpdated(new Date());
		customer.setUpdatedby(0L);
		customer = customerDao.save(customer);
	}

	private void updateCustomerIB(Customer customer, String tin, String cif) {
		customer.setCustomerPin(tin);
		customer.setCifNumber(cif);
		customer.setFailedAuthAttempts(0);
		customer.setStatus(Constants.ACTIVE_STATUS);
		customer.setUpdated(new Date());
		customer.setUpdatedby(0L);
		customerDao.save(customer);
	}

	private void createAccount(Long customerId, String accountType,
			String defaultAccount, String accountNumber, String cardNumber,
			String productType) {

		CustomerAccount ca = new CustomerAccount();
		if (accountType.equals("1")) {
			ca.setAccountTypeId(accountTypeDao.get(Constants.TABUNGAN_CODE)
					.getId());
		} else {
			ca.setAccountTypeId(accountTypeDao.get(Constants.GIRO_CODE).getId());
		}
		ca.setCurrencyId(cacheManager.getCurrency("360").getId());
		ca.setCustomerId(customerId);
		ca.setProductId(productDao.get(productType).getId());
		ca.setAccountNumber(accountNumber);
		ca.setCardNumber(cardNumber);
		if (accountNumber.equals(defaultAccount)) {
			ca.setIsDefault("Y");
		} else {
			ca.setIsDefault("N");
		}
		ca.setStatus(Constants.ACTIVE_STATUS);
		ca.setCreated(new Date());
		ca.setCreatedby(0L);
		ca.setUpdated(new Date());
		ca.setUpdatedby(0L);
		ca = customerAccountDao.save(ca);
		logger.info("customerId=" + customerId);
		logger.info("ca=" + ca.getAccountNumber());
	}

	/**
	 * {@inheritDoc}
	 */
	public void registerEBanking(final Transaction t) {
		String bit48 = t.getFreeData1();

		String cif = bit48.substring(0, 8);
		String encryptedPin = bit48.substring(8, 24);
		String tin = decryptTin(encryptedPin);
		String cardNumber = bit48.substring(24, 40);
		String accNumber = bit48.substring(40, 50);
		String phoneNumber = bit48.substring(50, 65).trim();
		logger.info("cardNumber=" + cardNumber + " accNumber=" + accNumber
				+ " phoneNumber=" + phoneNumber);

		// validate phone number for SMS Token
		phoneNumber = cacheManager.validateSmsTokenPhoneNumber(phoneNumber);
		if (phoneNumber == null) {
			t.setResponseCode("56");
			return;
		}

		Customer customer = new Customer();
		List<CustomerAccount> cas = customerAccountDao.get(cardNumber);
		if (cas != null && cas.size() != 0) {
			accountReactivation(customerAccountDao.getDefaultWithType(cas
					.get(0).getCustomerId()), tin, phoneNumber, cif, t);
		} else {
			int numOfCard = Integer.valueOf(bit48.substring(105, 107));
			int base = 107;
			int len = 71;

			for (int i = 0; i < numOfCard; i++) {
				String accountNumber = bit48.substring(base + i * len,
						base + i * len + 10).trim();
				String customerName = bit48.substring(base + i * len + 10,
						base + i * len + 40).trim();
				String accountType = bit48.substring(base + i * len + 40, base
						+ i * len + 41);
				String productType = bit48.substring(base + i * len + 41,
						base + i * len + 71).trim();

				if (i == 0) {
					logger.info("cardNumber=" + cardNumber + " accNumber="
							+ accNumber);
					createCustomer(customerName, cardNumber, tin, phoneNumber,
							cif, customer);
				}
				createAccount(customer.getId(), accountType, accNumber,
						accountNumber, cardNumber, productType);
			}

			CustomerDevice cd = registerDevices(cardNumber, accNumber, customer);

			String tail = bit48.substring(105);
			bit48 = bit48.substring(0, 65);
			String feedBack = String.format("%1$-20s",
					customer.getCustomerUsername());
			feedBack += String.format("%1$-20s", cd.getActivatePin());

			t.setFreeData1(bit48 + feedBack + tail);
		}
	}

	public void registerEBankingIGate(final Transaction t) {
		String bit48 = t.getFreeData1();

		String cif = bit48.substring(0, 8);
		String encryptedPin = bit48.substring(8, 24);
		String tin = EngineUtils.getEdcPin(encryptedPin, t.getCardNumber());
		logger.info("encryptedTin=" + encryptedPin + " tin=" + tin);
		String cardNumber = bit48.substring(24, 40);
		String accNumber = bit48.substring(40, 50);
		String phoneNumber = bit48.substring(50, 65);

		Customer customer = new Customer();
		List<CustomerAccount> cas = customerAccountDao.get(cardNumber);
		if (cas != null && cas.size() != 0) {
			accountReactivation(customerAccountDao.getDefaultWithType(cas
					.get(0).getCustomerId()), tin, phoneNumber, cif, t);
		} else {
			int numOfCard = Integer.valueOf(bit48.substring(105, 107));
			int base = 107;
			int len = 71;

			for (int i = 0; i < numOfCard; i++) {
				String accountNumber = bit48.substring(base + i * len,
						base + i * len + 10).trim();
				String customerName = bit48.substring(base + i * len + 10,
						base + i * len + 40).trim();
				String accountType = bit48.substring(base + i * len + 40, base
						+ i * len + 41);
				String productType = bit48.substring(base + i * len + 41,
						base + i * len + 71).trim();

				if (i == 0) {
					createCustomer(customerName, cardNumber, tin, phoneNumber,
							cif, customer);

				}
				createAccount(customer.getId(), accountType, accNumber,
						accountNumber, cardNumber, productType);
				logger.info("after create Account, i = " + i);
			}
			logger.info("cardNumber=" + cardNumber + " accNumber=" + accNumber
					+ " customer=" + customer);
			CustomerDevice cd = registerDevices(cardNumber, accNumber, customer);
			logger.info("cd.getTerminalId()=" + cd.getTerminalId());
			String tail = bit48.substring(105);

			bit48 = bit48.substring(0, 65);
			logger.info("bit48=" + bit48);
			String feedBack = String.format("%1$-20s",
					customer.getCustomerUsername());
			feedBack += String.format("%1$-20s", cd.getActivatePin());

			t.setFreeData1(bit48 + feedBack + tail);
			logger.info("t.getFreeData1()=" + t.getFreeData1());
		}
	}

	public Customer registerCustomerSilent(Customer customer,
			AccountView accountView) {
		return createCustomerSilentRegistration(customer.getCustomerName(),
				accountView.getCardNumber(), customer.getCustomerPin(),
				customer.getCustomerPhone(), customer.getCifNumber(), customer);
		// createAccount(insertedCust.getId(), accountView.getAccountType(),
		// accountView.getAccountNumber(), accountView.getAccountNumber(),
		// accountView.getCardNumber(), accountView.getProductCode());
	}

	public void registerAccountSilent(Customer customer,
			AccountView accountView, String defaultAccount) {
		createAccount(customer.getId(), accountView.getAccountType(),
				defaultAccount, accountView.getAccountNumber(),
				accountView.getCardNumber(), accountView.getProductCode());
	}

	/**
	 * {@inheritDoc}
	 */
	public List<AccountView> getAccounts(Long customerId) {
		List<CustomerAccount> cas = customerAccountDao
				.getAllWithTypeAndProduct(customerId);
		List<AccountView> avs = new ArrayList<AccountView>();
		for (CustomerAccount ca : cas) {
			avs.add(transform(ca));
		}

		return avs;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<CustomerAccount> getRegisteredAccounts(Long customerId) {
		return customerAccountDao.getAllRegistered(customerId);
	}

	/**
	 * {@inheritDoc}
	 */
	public AccountView getDefaultAccount(Long customerId) {
		return transform(customerAccountDao.getDefaultWithType(customerId));
	}

	/**
	 * {@inheritDoc}
	 */
	public AccountView getAccountBalance(AccountView view) {
		CustomerAccount ca = customerAccountDao.getWithTypeAndProduct(
				view.getCustomerId(), view.getAccountNumber());

		AccountView result = transform(ca);
		result.setTransactionType(Constants.ACCOUNT_BALANCE_CODE);
		result.setCurrencyCode(view.getCurrencyCode());
		result.setTerminalId(view.getTerminalId());
		result.setMerchantType(view.getMerchantType());

		Transaction t = TransformerFactory.getTransformer(view).transformTo(
				result, new Transaction());
		MessageCustomizer customizer = new AccountBalanceMessageCustomizer();
		customizer.compose(view, t);

		CommLink link = new MxCommLink(t);
		link.sendMessage();

		if (t.getResponseCode().equals(Constants.SUCCESS_CODE)) {
			customizer.decompose(view, t);
		} else {
			throw new BusinessException("1009", t.getResponseCode());
		}
		view.setResponseCode(t.getResponseCode());
		view.setReferenceNumber(t.getReferenceNumber());
		view.setResponseCode(Constants.SUCCESS_CODE);

		return view;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<AccountView> getAccountBalances(AccountView view) {
		List<CustomerAccount> accounts = customerAccountDao
				.getAllWithTypeAndProduct(view.getCustomerId());
		List<AccountView> result = new ArrayList<AccountView>();
		List<CustomerAccount> temp = new ArrayList<CustomerAccount>();

		for (CustomerAccount account : accounts) {
			temp.add(account);
			if (temp.size() == 6
					|| accounts.get(accounts.size() - 1).equals(account)) {
				result.addAll(processAccountBalances(view, temp));
				temp.clear();
			}
		}

		return result;
	}

	/**
	 * Factor out process of getting account balances to handle restriction of
	 * ISO message which is maximum six account per inquiry.
	 * 
	 * @param view
	 *            view object
	 * @param accounts
	 *            source account to be checked
	 * @return list of account view complete with it's balance and status
	 */
	private List<AccountView> processAccountBalances(AccountView view,
			List<CustomerAccount> accounts) {
		List<AccountView> result = new ArrayList<AccountView>();
		String accountNumbers = "";
		String acc = "";

		for (CustomerAccount account : accounts) {
			AccountView av = transform(account);
			av.setTransactionType(Constants.ACCOUNT_BALANCE_CODE);
			av.setCurrencyCode(view.getCurrencyCode());
			av.setTerminalId(view.getTerminalId());
			av.setMerchantType(view.getMerchantType());
			result.add(av);

			if (account.getIsDefault().equals("Y")) {
				acc = av.getAccountNumber();
			}

			accountNumbers += av.getAccountNumber() + ",";
		}

		AccountView firstAccount = result.get(0);
		firstAccount.setAccountNumber(accountNumbers);

		Transaction t = TransformerFactory.getTransformer(firstAccount)
				.transformTo(firstAccount, new Transaction());
		MessageCustomizer customizer = new AccountBalanceMessageCustomizer();
		customizer.compose(firstAccount, t);
		t.setToAccountType("00");
		t.setFromAccountNumber(acc);
		CommLink link = new MxCommLink(t);
		link.sendMessage();

		if (t.getResponseCode().equals(Constants.SUCCESS_CODE)) {
			customizer.decompose(result, t);
		} else {
			throw new BusinessException("IB-1009", t.getResponseCode());
		}

		String accNumbers[] = firstAccount.getAccountNumber().split(",");
		firstAccount.setAccountNumber(accNumbers[0]);
		return result;
	}
	
	/**
	 * Tes Untuk Esatement
	 * @param view
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<AccountStatementView> getStatementNoSession(AccountView view, Date startDate, Date endDate) {

//      Date now = DateUtils.truncate(new Date());
      Date end = DateUtils.truncate(endDate);
      Date start = DateUtils.truncate(startDate);

//      AccountView view = new AccountView();
      view.setStatementDateFrom(start);
      view.setStatementDateTo(end);
      view.setAccountNumber(StringUtils.leftPad(view.getAccountNumber(), 10, "0"));

      List<AccountStatementView> stat = new LinkedList<AccountStatementView>();
      List<AccountStatementView> statDb;

      statDb = getAccountStatementFromDB(view, start, end);

      if (statDb != null && statDb.size() != 0) {
          stat.addAll(statDb);
      }
      return stat;
	}


	/**
	 * {@inheritDoc}
	 */
	public List<AccountStatementView> getAccountStatement(AccountView view,
			Date startDate, Date endDate) {

		Date now = DateUtils.truncate(new Date());
		//Date yesterday = DateUtils.truncate(DateUtils.before(new Date(), 2));
		Date end = DateUtils.truncate(endDate);
		Date start = DateUtils.truncate(startDate);

		view.setStatementDateFrom(startDate);
		view.setStatementDateTo(endDate);
		view.setAccountNumber(StringUtils.leftPad(view.getAccountNumber(), 10, "0"));

		List<AccountStatementView> stat = new LinkedList<AccountStatementView>();
		List<AccountStatementView> statCore = new ArrayList<AccountStatementView>();
		List<AccountStatementView> statDb;

		//if (end.compareTo(now) == 0 || end.compareTo(yesterday) == 0) {
		if (end.compareTo(now) == 0) {

			// getting account statement from core
			try {

				statCore = getAccountStatementFromCore(view);
				logger.info("statCore=" + statCore);
			} catch (Exception e) {
				logger.info("error getting account statement from core");
				e.printStackTrace();
			}
		}

		////end maksimal = yesterday, karena yesterday tidak ambil dari DB tp dari core
		//if (end.compareTo(now) == 0 || end.compareTo(yesterday) == 0) {
		//	end = DateUtils.truncate(DateUtils.before(new Date(), 2));
		//}
		statDb = getAccountStatementFromDB(view, start, end);

		if (statDb != null && statDb.size() != 0) {
			stat.addAll(statDb);

		}

		if (statCore != null && statCore.size() != 0) {
			stat.addAll(statCore);
		}

		if (stat.size() == 0) {
			throw new BusinessException("IB-2001");
		}
		
//		Remove Symbol ?
			for(int i=0;i<stat.size();i++){
				String s = stat.get(i).getDescription().replace('�', '-');
				String f = s.replace('?', '-');
				stat.get(i).setDescription(f);
			}
				
			//	Custom Description Bunga dan Deposito
			for(int i=0;i<stat.size();i++){
				if(stat.get(i).getDescription().equalsIgnoreCase("bunga simpanan")){
					if(stat.get(i).getReferenceNumber().substring(0, 2).equals("AZ")){
						String refrensi = "";
						if(stat.get(i).getReferenceNumber().contains("\\")){
					
							refrensi = stat.get(i).getReferenceNumber().substring(3, stat.get(i).getReferenceNumber().lastIndexOf('\\'));
							 stat.get(i).setTransactionName("Bunga deposito"+" "+" Dep.Nr. "+refrensi);
						}else if(!stat.get(i).getReferenceNumber().contains("\\")){
							
							refrensi = stat.get(i).getReferenceNumber().substring(3, stat.get(i).getReferenceNumber().length());
							stat.get(i).setTransactionName("Bunga deposito"+" "+" Dep.Nr. "+refrensi);
						}
						
					}else{
						stat.get(i).setTransactionName("Bunga tabungan");
					}
						
				}
			}

		return stat;

	}

	/**
	 * {@inheritDoc}
	 */
	public List<LotteryTransactionView> getAccountJenisUndian(
			LotteryTransactionView view) {

		// view.setStatementDateFrom(startDate);
		// view.setStatementDateTo(endDate);
		// view.setAccountNumber(StringUtils.leftPad(view.getAccountNumber(),10,"0"));

		List<LotteryTransactionView> stat = new LinkedList<LotteryTransactionView>();
		List<LotteryTransactionView> statCore = new ArrayList<LotteryTransactionView>();
		// List<LotteryTransactionView> statDb;

		// if (end.compareTo(now) == 0){

		// getting account statement from core
		try {

			statCore = getPortfolioFromIGateUndian(view,
					com.dwidasa.engine.Constants.TRANSLATION_LOTTERY);
		} catch (Exception e) {
			logger.info("error getting account statement from core");
			e.printStackTrace();
		}
		// }

		// statDb = getAccountStatementFromDB(view, start, end);

		// if (statDb != null && statDb.size() != 0){
		// stat.addAll(statDb);
		//
		// }

		if (statCore != null && statCore.size() != 0) {
			stat.addAll(statCore);
		}

		return stat;

	}

	/**
	 * {@inheritDoc}
	 */
	public List<AccountStatementView> getAccountStatementFromCore(
			AccountView view) {
		List<AccountStatementView> asvs = Collections.synchronizedList(new ArrayList<AccountStatementView>());

		view.setTransactionType(Constants.MINI_STATMENT_CODE);
		Transaction t = TransformerFactory.getTransformer(view).transformTo(view, new Transaction());
		t.setTranslationCode(null);
		t.setFreeData1(new String());

		boolean nextAvailable = true;

		String bit48 = null;

		BigDecimal endingBalance = BigDecimal.ZERO;
		while (nextAvailable) {

			CommLink link = new MxCommLink(t);
			MessageCustomizer customizer = new AccountStatementMessageCustomizer();
			customizer.compose(view, t);
			if (bit48 != null) {
				t.setFreeData1(bit48);
			}

			link.sendMessage();
			if (t.getResponseCode().equals(Constants.SUCCESS_CODE)) {
				List<AccountStatementView> asvsl = Collections
						.synchronizedList(new ArrayList<AccountStatementView>());
				customizer.decompose(asvsl, t);

				bit48 = t.getFreeData1();
				nextAvailable = "1".equals(bit48.substring(39, 40));

				if (!nextAvailable) {
					endingBalance = new BigDecimal(bit48.substring(40, 55));
					if ("-".equals(bit48.substring(55, 56)))
						endingBalance = endingBalance.multiply(new BigDecimal(
								"-1"));
				}
				asvs.addAll(asvsl);
			} else {
				throw new BusinessException("IB-1009", t.getResponseCode());
			}

		}
		calculateRunningBalance(asvs, endingBalance);

		return asvs;
	}

	private void calculateRunningBalance(List<AccountStatementView> asvs,
			BigDecimal currentBalance) {

		for (int i = asvs.size() - 1; i > -1; i--) {

			AccountStatementView asv = asvs.get(i);
			asv.setRunningBalance(currentBalance);
			if ("D".equals(asv.getTransactionIndicator())) {
				currentBalance = currentBalance.add(asv.getAmount().abs());
			} else {
				currentBalance = currentBalance.subtract(asv.getAmount().abs());
			}

		}

	}

	public List<AccountStatementView> getAccountStatementFromDB(
			AccountView view, Date startDate, Date endDate) {
		Date end = DateUtils.truncate(endDate);
		Date start = DateUtils.truncate(startDate);

		Balance endingBalance = balanceDao.getEndingBalance(
				view.getAccountNumber(), start, end);
		if (endingBalance == null)
			return new ArrayList<AccountStatementView>();

		List<TransactionHistory> ths = transactionHistoryDao.getAll(
				view.getAccountNumber(), start, end);
		
		//	Remove Symbol ?
		for(int i=0;i<ths.size();i++){
			String s = ths.get(i).getDescription().replace('�', '-');
			String f = s.replace('?', '-');
			ths.get(i).setDescription(f);
		}
		
		//	Custom Description Bunga dan Deposito
		for(int i=0;i<ths.size();i++){
			if(ths.get(i).getDescription().equalsIgnoreCase("bunga simpanan")){
				if(ths.get(i).getReferenceNumber().substring(0, 2).equals("AZ")){
					String refrensi = ths.get(i).getReferenceNumber().substring(3, ths.get(i).getReferenceNumber().lastIndexOf('\\'));
					ths.get(i).setDescription("Bunga deposito"+" "+" Dep.Nr. "+refrensi);
				}else{
					String refrensi = ths.get(i).getReferenceNumber().substring(0, ths.get(i).getReferenceNumber().lastIndexOf('-'));
					ths.get(i).setDescription("Bunga tabungan");
				}
				
			}
		}
		

		List<AccountStatementView> asvs = new LinkedList<AccountStatementView>();

		// mapping TH -> ASV
		for (TransactionHistory th : ths) {
			AccountStatementView asv = new AccountStatementView();
			asv.setTransactionDate(th.getPostingDate());
			asv.setValueDate(th.getPostingDate());
			asv.setAccountNumber(th.getAccountNumber());
			asv.setAmount(th.getTransactionAmount());
			asv.setReferenceNumber(th.getReferenceNumber());
			asv.setDescription(th.getDescription());
			asv.setCurrencyCode(th.getCurrency());
			asv.setTransactionIndicator(th.getTransactionIndicator());
			asv.setTransactionName(th.getDescription());
			asv.setCustomerId(view.getCustomerId());
			asvs.add(asv);
		}

		// calculate running balance
		calculateRunningBalance(asvs, endingBalance.getAmount());

		return asvs;
	}
	


	/**
	 * {@inheritDoc}
	 */
	public List<AccountStatementView> getTransactionStatus(AccountView view,
			String status, Date startDate, Date endDate) {
		List<AccountStatementView> views = new ArrayList<AccountStatementView>();
		List<Transaction> ts = transactionDao.getAll(view.getAccountNumber(), view.getTransactionType(), status, startDate, endDate);
		if (ts != null) {
			for (Transaction t : ts) {
				logger.info("t.getTransactionType()=" + t.getTransactionType() + " t.getStatus()=" + t.getStatus());
				SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
				boolean sameDay = fmt.format(t.getTransactionDate()).equals( fmt.format(new Date()));
				if (t.getTransactionType().equals(
						Constants.VOUCHER_PURCHASE_CODE)
						&& t.getStatus().equals(Constants.PENDING_STATUS)
						&& sameDay) {
					TransactionData transactionData = transactionDataDao.getByTransactionFk(t.getId());
					BaseView oldView = (BaseView) EngineUtils.deserialize(transactionData);

					// -- change transaction status just for inquiry
					t.setTransactionType(Constants.VOUCHER_PURCHASE_CHK_CODE);
					t.setTerminalId(view.getTerminalId());
					t.setMerchantType(view.getMerchantType());
					MessageCustomizer customizer = new TransactionStatusMessageCustomizer(transactionData, oldView);
					logger.info("customizer=" + customizer.getClass());
					// customizer.compose(oldView, t);
					// CommLink link = new MxCommLink(t);
					// link.sendMessage();

					// //-- restore transaction status back
					// t.setTransactionType(Constants.VOUCHER_PURCHASE_CODE);
					// AccountStatementView asv = transform(t);
					// if (t.getResponseCode().equals(Constants.SUCCESS_CODE)) {
					// customizer.decompose(asv, t);
					// t.setStatus(Constants.SUCCEED_STATUS);
					// transactionDao.save(t);
					// }
					// else if
					// (t.getResponseCode().equals(Constants.TIMEOUT_CODE)) {
					// // do nothing, still a pending transaction
					// }
					// else {
					// //throw new BusinessException("IB-1009",
					// t.getResponseCode());
					// customizer.decompose(asv,t);
					// t.setStatus(Constants.FAILED_STATUS);
					// transactionDao.save(t);
					// }

					VoucherPurchaseView vpv = (VoucherPurchaseView) oldView;
					vpv.setTransactionType(t.getTransactionType());
					vpv = (VoucherPurchaseView) kioskCheckStatusService.checkStatus(vpv);

					// -- restore transaction status back
					vpv.setTransactionType(Constants.VOUCHER_PURCHASE_CODE);
					Transaction transaction = TransformerFactory.getTransformer(view).transformTo(view, new Transaction());
					if (transaction.getResponseCode().equals(Constants.SUCCESS_CODE)) {
						transaction.setStatus(Constants.SUCCEED_STATUS);
						transactionDao.save(transaction);
					} else if (transaction.getResponseCode().equals(Constants.TIMEOUT_CODE)) {
						// do nothing, still a pending transaction
					} else {
						// throw new BusinessException("IB-1009",
						// t.getResponseCode());
						transaction.setStatus(Constants.FAILED_STATUS);
						transactionDao.save(transaction);
					}
				}// gandos added some lines of code below, adding a filter that
					// catch
					// cash in transaction, cek it's status to mx
					// TODO: trim redundant code, wrap it with strategy pattern
					// if necessary
				else if (t.getTransactionType().equals(Constants.CASHIN_DELIMA_CODE)) {
//					TransactionData transactionData = transactionDataDao.getByTransactionFk(t.getId());
//					BaseView oldView = (BaseView) EngineUtils.deserialize(transactionData);
//
//					// -- change transaction status just for inquiry
//					t.setTransactionType(Constants.CASHIN_DELIMA_CHK_CODE);
//					t.setTerminalId(view.getTerminalId());
//					t.setMerchantType(view.getMerchantType());
//					MessageCustomizer customizer = new CashInCheckStatusMessageCustomizer(
//							transactionData, oldView);
//					customizer.compose(oldView, t);
//
//					CommLink link = new MxCommLink(t);
//					link.sendMessage();
//
//					// -- restore transaction status back
//					t.setTransactionType(Constants.CASHIN_DELIMA_CODE);
//					AccountStatementView asv = transform(t);
//					if (t.getResponseCode().equals(Constants.SUCCESS_CODE)) {
//						customizer.decompose(asv, t);
//					} else {
//						throw new BusinessException("IB-1009", t.getResponseCode());
//					}
				}

				AccountStatementView asv = transform(t);
				asv.setTransactionName(transactionTypeDao.get(
						t.getTransactionType()).getDescription());
				asv.setTransactionId(t.getId());
				views.add(asv);
			}
		}

		return views;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<AccountStatementView> getTransactionHistory(AccountView view,
			Date startDate, Date endDate) {
		return transactionDao.getAll(view.getAccountNumber(),
				view.getTransactionType(), startDate, endDate);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<AccountStatementView> getLastNTransaction(AccountView view,
			Integer n) {

		// throw new BusinessException("IB-ASXX");

		List<AccountStatementView> v1 = new ArrayList<AccountStatementView>();

		view.setTransactionType(Constants.MINI_STATMENT_CODE); // LAST_10_TRANSACTION_CODE
		Transaction t = TransformerFactory.getTransformer(view).transformTo(
				view, new Transaction());

		CommLink link = new MxCommLink(t);
		MessageCustomizer customizer = new AccountStatement10TrxMessageCustomizer(
				Constants.ACC_10_TRANSAKSI_AKHIR);
		customizer.compose(view, t);

		link.sendMessage();
		if (t.getResponseCode().equals(Constants.SUCCESS_CODE)) {
			customizer.decompose(v1, t);
		} else {
			throw new BusinessException("IB-1009", t.getResponseCode());
		}

		return v1;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<CustomerDevice> getDevices(Long customerId) {
		return customerDeviceDao.getAll(customerId);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<AccountView> getCards(AccountView view) {
		view.setTransactionType(Constants.ATM_BLOCKING_INQ_CODE);
		Transaction t = TransformerFactory.getTransformer(view).transformTo(
				view, new Transaction());

		MessageCustomizer customizer = new DeactivateCardInquiryMessageCustomizer();
		customizer.compose(view, t);

		CommLink link = new MxCommLink(t);
		link.sendMessage();

		List<AccountView> result = new ArrayList<AccountView>();
		if (t.getResponseCode().equals(Constants.SUCCESS_CODE)) {
			try {
				customizer.decompose(result, t);
			} catch (Exception e) {
				throw new BusinessException("IB-1009", t.getResponseCode());
			}
		} else {
			throw new BusinessException("IB-1009", t.getResponseCode());
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	public AccountView deactivateCard(AccountView view) {
		view.setTransactionType(Constants.ATM_BLOCKING_CODE);
		logger.info("deactivateCard view.getTransactionType()="
				+ view.getTransactionType());
		Transaction t = TransformerFactory.getTransformer(view).transformTo(
				view, new Transaction());

		logger.info("deactivateCard t.getTransactionType()"
				+ t.getTransactionType());

		MessageCustomizer customizer = new DeactivateCardMessageCustomizer();
		logger.info("deactivateCard t.getTransactionType()"
				+ t.getTransactionType());
		customizer.compose(view, t);

		CommLink link = new MxCommLink(t);
		link.sendMessage();

		if (t.getResponseCode().equals(Constants.SUCCESS_CODE)) {
			customizer.decompose(view, t);
			List<CustomerAccount> accs = customerAccountDao.getAll(
					view.getCustomerId(), view.getCardNumber());
			if (accs != null) {
				for (CustomerAccount acc : accs) {
					customerAccountDao
							.remove(acc.getId(), view.getCustomerId());
				}
			}
		} else {
			throw new BusinessException("IB-1009", t.getResponseCode());
		}

		return view;
	}

	/**
	 * {@inheritDoc}
	 */
	public AccountView registerCard(AccountView view) {
		// -- TODO waiting for messages

		view.setReferenceNumber(ReferenceGenerator.generate());
		view.setResponseCode(Constants.SUCCESS_CODE);
		return view;
	}

	/**
	 * {@inheritDoc}
	 */
	public AccountView registerAccount(AccountView view) {
		// -- TODO waiting for messages

		view.setReferenceNumber(ReferenceGenerator.generate());
		view.setResponseCode(Constants.SUCCESS_CODE);
		return view;
	}

	/**
	 * {@inheritDoc}
	 */
	public ResultView changeAccountStatus(Long customerId,
			Map<String, Integer> accountNumbers) {
		for (String s : accountNumbers.keySet()) {
			CustomerAccount ca = customerAccountDao.get(customerId, s);
			ca.setStatus(accountNumbers.get(s));
			customerAccountDao.save(ca);
		}

		ResultView view = new ResultView();
		view.setReferenceNumber(ReferenceGenerator.generate());
		view.setResponseCode(Constants.SUCCESS_CODE);
		return view;
	}

	/**
	 * Transform from customerAccount object to accountView object.
	 * 
	 * @param src
	 *            customer account source object
	 * @return customer view object
	 */
	private AccountView transform(CustomerAccount src) {
		AccountView result = new AccountView();
		result.setCustomerId(src.getCustomerId());
		result.setAccountNumber(src.getAccountNumber());
		result.setCardNumber(src.getCardNumber());

		if (src.getAccountType() != null) {
			result.setAccountType(src.getAccountType().getAccountType());
			result.setAccountName(src.getAccountType().getAccountName());
		}

		if (src.getProduct() != null) {
			result.setProductCode(src.getProduct().getProductCode());
			result.setProductName(src.getProduct().getProductName());
		}

		if (src.getIsDefault() != null) {
			result.setIsDefault(src.getIsDefault());
		}
		return result;
	}

	/**
	 * Transform from transactionHistory object to accountStatementView object.
	 * 
	 * @param src
	 *            transaction history source object
	 * @return account statement view object
	 */
	private AccountStatementView transform(TransactionHistory src) {
		AccountStatementView asv = new AccountStatementView();

		asv.setAccountNumber(src.getAccountNumber());
		asv.setReferenceNumber(src.getReferenceNumber());
		asv.setCurrencyCode(src.getCurrency());
		asv.setAmount(src.getTransactionAmount());
		asv.setDescription(src.getDescription());
		asv.setTransactionDate(src.getEntryDate());
		asv.setValueDate(src.getPostingDate());
		asv.setTransactionIndicator(src.getTransactionIndicator());

		return asv;
	}

	/**
	 * Transform from transaction object to accountStatementView object.
	 * 
	 * @param src
	 *            transaction source object
	 * @return account statement view object
	 */
	private AccountStatementView transform(Transaction src) {
		AccountStatementView asv = new AccountStatementView();

		asv.setAccountNumber(src.getFromAccountNumber());
		asv.setTransactionType(src.getTransactionType());
		asv.setReferenceNumber(src.getReferenceNumber());
		asv.setToAccountNumber(src.getToAccountNumber());
		asv.setCurrencyCode(src.getCurrencyCode());
		asv.setAmount(src.getTransactionAmount());
		asv.setDescription(src.getDescription());
		asv.setTransactionDate(src.getTransactionDate());
		asv.setValueDate(src.getValueDate());
		asv.setResponseCode(src.getResponseCode());
		asv.setCustomerReference(src.getCustomerReference());

		if (src.getStatus().equals(Constants.FAILED_STATUS)) {
			asv.setStatus(0);
		} else if (src.getStatus().equals(Constants.SUCCEED_STATUS)) {
			asv.setStatus(1);
		} else if (src.getStatus().equals(Constants.PENDING_STATUS)) {
			asv.setStatus(2);
		} else if (src.getStatus().equals(Constants.CANCELED_STATUS)) {
			asv.setStatus(3);
		}

		return asv;
	}

	/**
	 * Class to compose and decompose custom data for account balance feature.
	 */
	private class AccountBalanceMessageCustomizer implements MessageCustomizer {
		private AccountBalanceMessageCustomizer() {
		}

		public void compose(BaseView view, Transaction transaction) {
			AccountView av = (AccountView) view;
			String accountNumbers[] = av.getAccountNumber().split(",");

			String customData = "";
			// -- number of array(n2) right justified zero padding
			customData += String.format("%02d", accountNumbers.length);
			// -- array length(158)
			customData += String.valueOf(158 * accountNumbers.length);

			for (String accountNumber : accountNumbers) {
				// -- account number - request an(10)
				customData += String.format("%1$-10s", accountNumber);
				// -- account status - blank for request(an1)
				customData += " ";
				// -- account name holder - blank for request(an30)
				customData += String.format("%1$-30s", " ");
				// -- account type - blank for request(an1)
				customData += " ";
				// -- product type - blank for request(an30)
				customData += String.format("%1$-30s", " ");
				// -- account currency - blank for request(an3)
				customData += String.format("%1$-3s", " ");
				// -- branch - blank for request(an3)
				customData += String.format("%1$-3s", " ");
				// -- plafond - blank for request(n15+sign)
				customData += String.format("%015d", 0) + "+";
				// -- blocked balance(n15+sign)
				customData += String.format("%015d", 0) + "+";
				// -- minimum balance(n15+sign)
				customData += String.format("%015d", 0) + "+";
				// -- clearing settlement(n15+sign)
				customData += String.format("%015d", 0) + "+";
				// -- available balance(n15+sign)
				customData += String.format("%015d", 0) + "+";
			}

			transaction.setFreeData1(customData);
			transaction.setTranslationCode("01608");
		}

		@SuppressWarnings("unchecked")
		public Boolean decompose(Object view, Transaction transaction) {
			if (view instanceof List) {
				List<AccountView> avs = (List<AccountView>) view;
				int len = 158;
				for (int i = 0; i < avs.size(); i++) {
					AccountView av = avs.get(i);
					av.setCustomerName(transaction.getFreeData1()
							.substring(5 + len * i + 11, 5 + len * i + 41)
							.trim());
					if (transaction.getFreeData1()
							.substring(5 + len * i + 41, 5 + len * i + 42)
							.equals("1")) {
						av.setAccountType(Constants.TABUNGAN_CODE);
					} else {
						av.setAccountType(Constants.GIRO_CODE);
					}
					av.setAvailableBalance(new BigDecimal(transaction
							.getFreeData1().substring(5 + len * i + 157,
									5 + len * i + 158)
							+ transaction.getFreeData1().substring(
									5 + len * i + 142, 5 + len * i + 157)));
					av.setBlockedBalance(new BigDecimal(transaction
							.getFreeData1().substring(5 + len * i + 109,
									5 + len * i + 110)
							+ transaction.getFreeData1().substring(
									5 + len * i + 94, 5 + len * i + 109)));
					av.setMinimumBalance(new BigDecimal(transaction
							.getFreeData1().substring(5 + len * i + 125,
									5 + len * i + 126)
							+ transaction.getFreeData1().substring(
									5 + len * i + 110, 5 + len * i + 125)));
					av.setAccountStatus(Integer.valueOf(transaction
							.getFreeData1().substring(5 + len * i + 10,
									5 + len * i + 11)));
				}
			} else {
				AccountView av = (AccountView) view;
				av.setCustomerName(transaction.getFreeData1()
						.substring(5 + 11, 5 + 41).trim());
				if (transaction.getFreeData1().substring(5 + 46, 5 + 47)
						.equals("1")) {
					av.setAccountType(Constants.TABUNGAN_CODE);
				} else {
					av.setAccountType(Constants.GIRO_CODE);
				}
				av.setAvailableBalance(new BigDecimal(transaction
						.getFreeData1().substring(5 + 157, 5 + 158)
						+ transaction.getFreeData1()
								.substring(5 + 142, 5 + 157)));
				av.setBlockedBalance(new BigDecimal(transaction.getFreeData1()
						.substring(5 + 109, 5 + 110)
						+ transaction.getFreeData1().substring(5 + 94, 5 + 109)));
				av.setMinimumBalance(new BigDecimal(transaction.getFreeData1()
						.substring(5 + 125, 5 + 126)
						+ transaction.getFreeData1()
								.substring(5 + 110, 5 + 125)));
				av.setAccountStatus(Integer.valueOf(transaction.getFreeData1()
						.substring(5 + 10, 5 + 11)));
			}

			return Boolean.TRUE;
		}
	}

	/**
	 * Class to compose and decompose custom data for lottery view feature.
	 */
	private class LotteryViewMessageCustomizer implements MessageCustomizer {
		private LotteryViewMessageCustomizer() {
		}

		public void compose(BaseView view, Transaction transaction) {
			LotteryView lv = (LotteryView) view;
			// MMyyyy+n14(startPeriod)+n14(endPeriod)
			transaction.setFreeData1(lv.getPeriodMonth() + lv.getPeriodYear()
					+ "00000000000000" + "00000000000000");
			transaction.setTranslationCode(Constants.TRANSLATION_LOTTERY);
			// bit 61, 62, 103 dimatikan, tidak bisa diubah dr DB karena juga
			// dipake di Mutasi Rek dan 10 trx akhir
			transaction.setFreeData2(null);
			transaction.setFreeData3(null);
			transaction.setToAccountNumber(null);
		}

		public Boolean decompose(Object view, Transaction transaction) {
			if (view instanceof List) {
				//
			} else {
				LotteryView lv = (LotteryView) view;
				transaction.getFreeData1();// bit48
				//
				lv.setStartPointNumber(Long.valueOf(transaction.getFreeData1()
						.substring(6, 6 + 14)));
				lv.setEndPointNumber(Long.valueOf(transaction.getFreeData1()
						.substring(6 + 14)));
			}
			return Boolean.TRUE;
		}
	}

	/**
	 * Class to compose and decompose custom data for account statement feature.
	 */
	private class AccountStatementMessageCustomizer implements
			MessageCustomizer {
		private AccountStatementMessageCustomizer() {
		}

		private AccountStatementMessageCustomizer(String translationCode) {
			this.translationCode = translationCode;
		}

		private String translationCode;

		public void compose(BaseView view, Transaction transaction) {
			AccountView accView = (AccountView) view;

			String customData = "";
			// -- last entry date - blank for request(n8) format yyyymmdd
			customData += String.format("%1$-8s", " ");
			// -- last entry time - blank for request(n6) format hhmmss
			customData += String.format("%1$-6s", " ");
			// -- last transaction code - blank for request(an4)
			customData += String.format("%1$-4s", " ");
			// -- last reference number - blank for request(n15)
			customData += String.format("%1$-15s", " ");
			// -- record number(n6)
			customData += String.format("%06d", 0);
			// -- next available flag(n1)
			customData += "0";
			// -- current balance
			customData += String.format("%015d", 0) + "+";
			// tambah date awal & date akhir
			// customData += accView.getProductCode();
			// customData += accView.getProductName();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			customData += sdf.format(accView.getStatementDateFrom());
			customData += sdf.format(accView.getStatementDateTo());

			transaction.setFreeData1(customData);
			transaction.setFreeData4("00");
			transaction.setFreeData5("00");
			if (translationCode != null) { // utk 10 message terakhir
				transaction.setTranslationCode("02604");
			} else {
				transaction.setTranslationCode(Constants.TRANSLATION_MUTASIREK);
			}
		}

		@SuppressWarnings("unchecked")
		public Boolean decompose(Object view, Transaction t) {
			List<AccountStatementView> views = (List<AccountStatementView>) view;
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");

			if (t.getResponseCode().equals("00")) {
				logger.info("t.getFreeData4()=" + t.getFreeData4());
				parseFreeData45(views, t.getFreeData4(), sdf1, sdf2);
				//parseFreeData45(views, t.getFreeData5(), sdf1, sdf2);
			} else {
				return Boolean.FALSE;
			}

			t.setFreeData2(t.getFreeData1().substring(40, 55));
			return t.getFreeData1().substring(39, 40).equals("1");
		}

		private void parseFreeData45(List<AccountStatementView> views, String freeData, SimpleDateFormat sdf1, SimpleDateFormat sdf2) {
			if (freeData == null || freeData.trim().length() == 0) {
				return;
			}

			int loop = Integer.parseInt(freeData.substring(0, 2));
			logger.info("loop=" + loop);
			int len = 96;
			for (int j = 0; j < loop; j++) {
				AccountStatementView asv = new AccountStatementView();

				String valueDate = freeData.substring(5 + j * len + 8, 5 + j * len + 8 + 8);
				logger.info("valueDate=" + valueDate);
				try {
					asv.setValueDate(sdf1.parse(valueDate));
				} catch (ParseException e) {
					asv.setValueDate(new Date());
				}

				String transactionDate = freeData.substring(5 + j * len + 16, 5 + j * len + 16 + 14);
				logger.info("transactionDate=" + transactionDate);
				try {
					asv.setTransactionDate(sdf2.parse(transactionDate));
				} catch (ParseException e) {
					asv.setTransactionDate(new Date());
				}

				asv.setTransactionName(freeData.substring(5 + j * len + 30, 5 + j * len + 30 + 30).trim());
				logger.info("asv.getTransactionName()=" + asv.getTransactionName());
				asv.setDescription(asv.getTransactionName());
				asv.setReferenceNumber(freeData.substring(5 + j * len + 64, 5 + j * len + 64 + 15).trim());
				BigDecimal bdAmount = new BigDecimal(freeData.substring(5 + j * len + 79, 5 + j * len + 79 + 15));
				logger.info("bdAmount=" + bdAmount);
				DecimalFormat myFormatter = new DecimalFormat("###.00000", DecimalFormatSymbols.getInstance());
				String output = myFormatter.format(bdAmount.doubleValue());
				logger.info("output=" + output);
				output = output.replaceAll(",","\\.");
				logger.info("after replace output=" + output);
				asv.setAmount(new BigDecimal(output));
				logger.info("asv.getAmount()=" + asv.getAmount());
				asv.setTransactionIndicator(freeData.substring( 5 + j * len + 94, 5 + j * len + 95));				
				logger.info("asv=" + asv);
				views.add(asv);
			}
		}
	}

	/**
	 * Class to compose and decompose custom data for account statement feature.
	 */
	private class AccountStatementFromCoreMessageCustomizer implements
			MessageCustomizer {
		private AccountStatementFromCoreMessageCustomizer() {
		}

		private AccountStatementFromCoreMessageCustomizer(String translationCode) {
			this.translationCode = translationCode;
		}

		private String translationCode;

		public void compose(BaseView view, Transaction transaction) {
			logger.info("transaction.getFreeData1()="
					+ transaction.getFreeData1());
			if (transaction.getFreeData1() == null
					|| transaction.getFreeData1().equals("")) {
				String customData = "";
				// -- last entry date - blank for request(n8) format yyyymmdd
				customData += String.format("%1$-8s", " ");
				// -- last entry time - blank for request(n6) format hhmmss
				customData += String.format("%1$-6s", " ");
				// -- last transaction code - blank for request(an4)
				customData += String.format("%1$-4s", " ");
				// -- last reference number - blank for request(n15)
				customData += String.format("%1$-15s", " ");
				// -- record number(n6)
				customData += String.format("%06d", 0);
				// -- next available flag(n1)
				customData += "0";
				// -- current balance
				customData += String.format("%015d", 0) + "+";
				transaction.setFreeData1(customData);
			}
			transaction.setFreeData4("00000");
			transaction.setFreeData5("00000");
			if (translationCode != null) { // utk 10 message terakhir
				transaction.setTranslationCode(translationCode);
			} else {
				transaction.setTranslationCode(Constants.TRANSLATION_MUTASIREK);
			}
		}

		@SuppressWarnings("unchecked")
		public Boolean decompose(Object view, Transaction t) {
			List<AccountStatementView> views = (List<AccountStatementView>) view;
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");

			if (t.getResponseCode().equals("00")) {
				parseFreeData45(views, t.getFreeData4(), sdf1, sdf2);
				parseFreeData45(views, t.getFreeData5(), sdf1, sdf2);
			} else {
				return Boolean.FALSE;
			}

			t.setFreeData2(t.getFreeData1().substring(40, 55)); // saldo akhir
			BigDecimal runningBalance = BigDecimal.valueOf(Double.parseDouble(t
					.getFreeData2()));
			for (int i = views.size() - 1; i >= 0; i--) {
				AccountStatementView av = views.get(i);
				av.setRunningBalance(runningBalance);
				// Trx Debit akan mengurangi saldo
				if (av.getStrTransactionIndicator().equals("D")) {
					runningBalance = runningBalance.add(av.getAmount());
				} else {
					runningBalance = runningBalance.subtract(av.getAmount());
				}
				if (i == 0) { // disimpan di Item ke-0, Next Available Flag
					av.setStatus(Integer.parseInt(t.getFreeData1().substring(
							39, 40)));
					av.setCustomerReference(t.getFreeData1());
				}
			}
			return t.getFreeData1().substring(39, 40).equals("1");
		}

		private void parseFreeData45(List<AccountStatementView> views,
				String freeData, SimpleDateFormat sdf1, SimpleDateFormat sdf2) {
			if (freeData == null || freeData.trim().length() == 0) {
				return;
			}

			int loop = Integer.parseInt(freeData.substring(0, 2));
			int len = 96;
			for (int j = 0; j < loop; j++) {
				AccountStatementView asv = new AccountStatementView();

				String valueDate = freeData.substring(5 + j * len + 8, 5 + j
						* len + 8 + 8);
				try {
					asv.setValueDate(sdf1.parse(valueDate));
				} catch (ParseException e) {
					asv.setValueDate(new Date());
				}

				String transactionDate = freeData.substring(5 + j * len + 16, 5
						+ j * len + 16 + 14);
				try {
					asv.setTransactionDate(sdf2.parse(transactionDate));
				} catch (ParseException e) {
					asv.setTransactionDate(new Date());
				}

				asv.setTransactionName(freeData.substring(5 + j * len + 30,
						5 + j * len + 30 + 30).trim());
				asv.setDescription(asv.getTransactionName());
				asv.setReferenceNumber(freeData.substring(5 + j * len + 64,
						5 + j * len + 64 + 15).trim());
				asv.setAmount(new BigDecimal(freeData.substring(5 + j * len
						+ 79, 5 + j * len + 79 + 15)));
				asv.setTransactionIndicator(freeData.substring(
						5 + j * len + 94, 5 + j * len + 95));
				views.add(asv);
			}
		}
	}

	private class AccountStatement10TrxMessageCustomizer implements
			MessageCustomizer {
		private AccountStatement10TrxMessageCustomizer() {
		}

		private AccountStatement10TrxMessageCustomizer(String translationCode) {
			this.translationCode = translationCode;
		}

		private String translationCode;

		public void compose(BaseView view, Transaction transaction) {
			String customData = "";
			// -- last entry date - blank for request(n8) format yyyymmdd
			customData += String.format("%1$-8s", " ");
			// -- last entry time - blank for request(n6) format hhmmss
			customData += String.format("%1$-6s", " ");
			// -- last transaction code - blank for request(an4)
			customData += String.format("%1$-4s", " ");
			// -- last reference number - blank for request(n15)
			customData += String.format("%1$-15s", " ");
			// -- record number(n6)
			customData += String.format("%06d", 0);
			// -- next available flag(n1)
			customData += "0";
			// -- current balance
			customData += String.format("%015d", 0) + "+";

			transaction.setFreeData1(customData);
			transaction.setFreeData4("00000");
			transaction.setFreeData5("00000");
			if (translationCode != null) { // utk 10 message terakhir
				transaction.setTranslationCode("02604");
			} else {
				transaction.setTranslationCode(Constants.TRANSLATION_MUTASIREK);
			}
		}

		@SuppressWarnings("unchecked")
		public Boolean decompose(Object view, Transaction t) {
			if (!t.getResponseCode().equals("00")) {
				return Boolean.FALSE;
			} else {
				List<AccountStatementView> views = (List<AccountStatementView>) view;

				String nomerRekening = t.getFreeData1().substring(0, 10);
				String counterTotTrx = t.getFreeData1().substring(54, 56);

				String trxs = t.getFreeData1().substring(56,
						t.getFreeData1().length());
				int n = Integer.parseInt(counterTotTrx);
				BigDecimal balance = t.getTransactionAmount(); // ditaruh di bit
																// 4 utk IGATE
																// CORE
				for (int i = 0; i < n; i++) {
					AccountStatementView ltv = new AccountStatementView();
					String tglTransaksi = trxs.substring(0 + (i * 79),
							8 + (i * 79));// +8
					String jamTransaksi = trxs.substring(8 + (i * 79),
							14 + (i * 79));// +6
					String kodeTransaksi = trxs.substring(14 + (i * 79),
							18 + (i * 79));// +4
					String refTransaksi = trxs.substring(18 + (i * 79),
							33 + (i * 79));// +15
					String descTransaksi = trxs.substring(33 + (i * 79),
							63 + (i * 79));// +30
					String nilaiTransaksi = trxs.substring(63 + (i * 79),
							78 + (i * 79));// +15
					String cd = trxs.substring(78 + (i * 79), 79 + (i * 79));// +1

					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					// YYYYMMDD HHMMSS
					Date d;
					try {
						d = sdf.parse(tglTransaksi.substring(0, 4) + "-"
								+ tglTransaksi.substring(4, 6) + "-"
								+ tglTransaksi.substring(6, 8) + " "
								+ jamTransaksi.substring(0, 2) + ":"
								+ jamTransaksi.substring(2, 4) + ":"
								+ jamTransaksi.substring(4, 6));
					} catch (ParseException e) {
						d = new Date();
					}
					ltv.setAccountNumber(nomerRekening);
					// ltv.setCustomerName(namaNasabah);
					ltv.setTransactionDate(d);
					ltv.setValueDate(d);
					// ltv.setTransactionCode(kodeTransaksi);
					ltv.setReferenceNumber(refTransaksi);
					ltv.setDescription(descTransaksi);
					ltv.setAmount(BigDecimal.valueOf(Double
							.parseDouble(nilaiTransaksi)));
					ltv.setTransactionIndicator(cd);
					// ltv.setRunningBalance(cd.equals("D") ?
					// balance.subtract(ltv.getAmount()) :
					// balance.add(ltv.getAmount())); //saldo terakhir ditaruh
					// di running balance

					views.add(ltv);
				}

				for (int i = views.size() - 1; i >= 0; i--) {
					AccountStatementView ltv = views.get(i);
					ltv.setRunningBalance(balance);
					// D mengurangi saldo, reversenya, akan menambah
					balance = ltv.getTransactionIndicator().equals("D") ? balance
							.add(ltv.getAmount()) : balance.subtract(ltv
							.getAmount());
					logger.info("balance=" + balance);
				}
				return true;
			}
		}

		private void parseFreeData45(List<AccountStatementView> views,
				String freeData, SimpleDateFormat sdf1, SimpleDateFormat sdf2) {
			if (freeData == null || freeData.trim().length() == 0) {
				return;
			}

			int loop = Integer.parseInt(freeData.substring(0, 2));
			int len = 96;
			for (int j = 0; j < loop; j++) {
				AccountStatementView asv = new AccountStatementView();

				String valueDate = freeData.substring(5 + j * len + 8, 5 + j
						* len + 8 + 8);
				try {
					asv.setValueDate(sdf1.parse(valueDate));
				} catch (ParseException e) {
					asv.setValueDate(new Date());
				}

				String transactionDate = freeData.substring(5 + j * len + 16, 5
						+ j * len + 16 + 14);
				try {
					asv.setTransactionDate(sdf2.parse(transactionDate));
				} catch (ParseException e) {
					asv.setTransactionDate(new Date());
				}

				asv.setTransactionName(freeData.substring(5 + j * len + 30,
						5 + j * len + 30 + 30).trim());
				asv.setDescription(asv.getTransactionName());
				asv.setReferenceNumber(freeData.substring(5 + j * len + 64,
						5 + j * len + 64 + 15).trim());
				asv.setAmount(new BigDecimal(freeData.substring(5 + j * len
						+ 79, 5 + j * len + 79 + 15)));
				asv.setTransactionIndicator(freeData.substring(
						5 + j * len + 94, 5 + j * len + 95));
				views.add(asv);
			}
		}
	}

	/**
	 * Class to compose and decompose custom data for checking transaction
	 * status feature. Don't used this class as singleton !
	 */
	private class TransactionStatusMessageCustomizer implements
			MessageCustomizer {
		private TransactionData transactionData;
		private BaseView oldView;

		private TransactionStatusMessageCustomizer(
				TransactionData transactionData, BaseView oldView) {
			this.transactionData = transactionData;
			this.oldView = oldView;
		}

		private String setTranslationCode(String providerCode) {
			String translationCode = "";

			if (providerCode.equals(Constants.INDOSMART_CODE)) {
				translationCode = "00102";
			} else if (providerCode.equals(Constants.MKN_CODE)) {
				translationCode = "00402";
			} else if (providerCode.equals(Constants.AKSES_NUSANTARA_CODE)) {
				translationCode = "01703";
			} else if (providerCode.equals(Constants.AKSES_CIPTA_CODE)) {
				translationCode = "01801";
			}

			return translationCode;
		}

		public void compose(BaseView view, Transaction transaction) {
			VoucherPurchaseView vpv = (VoucherPurchaseView) view;
			String[] phone = EngineUtils.splitPhoneNumber(vpv
					.getCustomerReference());

			transaction.setTransactionAmount(new BigDecimal(vpv
					.getDenomination()));

			String customData = "";
			// -- country code(an2)
			customData += "62";
			// -- area / operator code(an4)
			customData += phone[0];
			// -- phone number(an10)
			customData += String.format("%1$-10s", phone[1]);
			// -- voucher nominal(n12) right justified zero padding
			customData += StringUtils.leftPad(vpv.getDenomination(), 12, "0");

			if (vpv.getFeeIndicator() == null) {
				vpv.setFeeIndicator("C");
			}
			// -- fee indicator(a1)
			customData += vpv.getFeeIndicator();
			// -- fee amount(n12)
			customData += String.format("%012d", vpv.getFee().longValue());

			transaction.setFreeData1(customData);
			transaction.setProviderCode(vpv.getProviderCode());
			transaction.setProductCode(vpv.getProductCode());
			String prodId = String
					.valueOf(Long.valueOf(vpv.getDenomination()) / 1000);
			transaction.setTranslationCode(setTranslationCode(vpv
					.getProviderCode())
					+ vpv.getProviderCode()
					+ "02"
					+ vpv.getProductCode()
					+ StringUtils.leftPad(prodId, 4, "0"));
			logger.info("vpv.getProviderCode()=" + vpv.getProviderCode()
					+ " vpv.getTransactionType()=" + vpv.getTransactionType());
			if (vpv.getProviderCode().equals(Constants.PAYEE_ID.SERA)) {
				// transaction.setFreeData2("02" +
				// VoucherPurchaseViewServiceImpl.getSeraPurchaseBillerId(vpv.getProductCode())
				// + Constants.PAYEE_ID.SERA_BIT61 +
				// VoucherPurchaseViewServiceImpl.getSeraPurchaseProductId(vpv.getProductCode(),
				// vpv.getDenomination()));
				// payee ID + industry code + biller code + product ID
				// biller code di URS = product code di DB
				// 023 + process code + product code (payee ID + industry code +
				// biller code + product ID)
				transaction
						.setTranslationCode("023"
								+ Constants.TRANSLATION_PROCESS_CODE.SERA.PREPAID_CEK_STATUS
								+ Constants.PAYEE_ID.SERA_FOR_ALL
								+ Constants.INDUSTRY_CODE.TELCO_PREPAID_POSTPAID
								+ VoucherPurchaseViewServiceImpl
										.getSeraPurchaseBillerId(vpv
												.getProductCode())
								+ VoucherPurchaseViewServiceImpl
										.getSeraPurchaseProductId(
												vpv.getProductCode(),
												vpv.getDenomination()));

				if (vpv.getCardData1() != null && vpv.getCardData2() != null) {
					transaction
							.setBit22(Constants.PO_SERVICE_ENTRY_MODE.PAN_EXPIRATION_DATE.MAGNETIC_STRIPE
									+ Constants.PO_SERVICE_ENTRY_MODE.PIN_ENTRY_CAPABILITY.PIN_ENTRY_CAPABILITY);
				} else {
					transaction
							.setBit22(Constants.PO_SERVICE_ENTRY_MODE.PAN_EXPIRATION_DATE.UNKNOWN_OR_NOT_APPLICABLE
									+ Constants.PO_SERVICE_ENTRY_MODE.PIN_ENTRY_CAPABILITY.UNKNOWN);
				}
			} else {
				transaction.setFreeData2(customData);
			}

		}

		public Boolean decompose(Object view, Transaction transaction) {
			AccountStatementView asv = (AccountStatementView) view;
			asv.setResponseCode(transaction.getResponseCode());
			asv.setReferenceNumber(transaction.getReferenceNumber());

			VoucherPurchaseView vpv = (VoucherPurchaseView) oldView;
			vpv.setResponseCode(transaction.getResponseCode());
			vpv.setReferenceNumber(transaction.getReferenceNumber());
			if (vpv.getResponseCode().equals(Constants.SUCCESS_CODE)) {
				// -- 0 = failed, 1 = succeed, 2 = pending
				String status = transaction.getFreeData1().substring(52, 53);

				if (status.equals("0") || status.equals("1")) {
					asv.setStatus(Integer.valueOf(status));

					SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
					try {
						vpv.setWindowPeriod(sdf.parse(transaction
								.getFreeData1().substring(28, 36)));
					} catch (ParseException e) {
						vpv.setWindowPeriod(new Date());
					}
					if (vpv.getProviderCode().equals(
							Constants.PROVIDER_FINNET_CODE)) { // FINNET
																// Telkomsel
						vpv.setProductCode(Constants.PREPAID_PRODCODE.FINNET_TELKOMSEL);
						transaction
								.setProductCode(Constants.PREPAID_PRODCODE.FINNET_TELKOMSEL);
						vpv.setSerialNumber(transaction.getFreeData1()
								.substring(36));
					} else {
						vpv.setSerialNumber(transaction.getFreeData1()
								.substring(36, 52));
					}
					if (status.equals("1")) {
						transaction.setStatus(Constants.SUCCEED_STATUS);
					}
					if (status.equals("2")) {
						transaction.setStatus(Constants.PENDING_STATUS);
					} else {
						transaction.setStatus(Constants.FAILED_STATUS);
					}

					transaction.setTransactionAmount(vpv.getAmount());
					transaction
							.setTransactionType(Constants.VOUCHER_PURCHASE_CODE);
					transaction.setUpdated(new Date());
					transactionDao.save(transaction);

					transactionData.setTransactionData(PojoJsonMapper
							.toJson(vpv));
					transactionData.setUpdated(new Date());
					transactionDataDao.save(transactionData);
				}
			}

			return Boolean.TRUE;
		}
	}

	private class CashInCheckStatusMessageCustomizer implements
			MessageCustomizer {
		@SuppressWarnings("unused")
		private TransactionData transactionData;
		@SuppressWarnings("unused")
		private BaseView oldView;

		private CashInCheckStatusMessageCustomizer(
				TransactionData transactionData, BaseView oldView) {
			this.transactionData = transactionData;
			this.oldView = oldView;
		}

		public void compose(BaseView view, Transaction transaction) {
			CashInDelimaView cv = (CashInDelimaView) view;

			String customData = "";
			// -- receiver id number an(25) left justfied padding with space
			customData += String.format("%-25s", cv.getReceiverIdNumber());
			// -- transfer code n(16) left justfied padding with space
			customData += String.format("%1$-16s", cv.getCustomerReference());
			// -- amount
			customData += String.format("%012d", 0);
			// -- provider fee
			customData += String.format("%012d", 0);
			// -- reference
			customData += String.format("%-12s", " ");
			// -- bank admin fee
			customData += String.format("%012d", 0);

			transaction.setFreeData1(customData);
			transaction.setTranslationCode("00207002013");
		}

		public Boolean decompose(Object view, Transaction transaction) {
			CashInDelimaView cv = (CashInDelimaView) view;
			cv.setBillerReference(transaction.getFreeData1().substring(65, 77)
					.trim());
			cv.setResponseCode(transaction.getResponseCode());
			cv.setReferenceNumber(transaction.getReferenceNumber());

			return Boolean.TRUE;
		}
	}

	/**
	 * Class to compose and decompose custom data for deactivate card feature.
	 */
	private class DeactivateCardMessageCustomizer implements MessageCustomizer {
		private DeactivateCardMessageCustomizer() {
		}

		public void compose(BaseView view, Transaction transaction) {
			AccountView av = (AccountView) view;

			String customData = av.getGenerated();

			transaction.setFreeData1(customData);
			transaction.setTranslationCode("01613");
		}

		public Boolean decompose(Object view, Transaction transaction) {
			AccountView av = (AccountView) view;
			av.setResponseCode(transaction.getResponseCode());
			av.setReferenceNumber(transaction.getReferenceNumber());

			return Boolean.TRUE;
		}
	}

	/**
	 * Class to compose and decompose custom data from inquiry of deactivating
	 * card feature.
	 */
	private class DeactivateCardInquiryMessageCustomizer implements
			MessageCustomizer {
		private DeactivateCardInquiryMessageCustomizer() {
		}

		public void compose(BaseView view, Transaction transaction) {

			Customer c = customerDao.get(view.getCustomerId());
			String customData = c.getCifNumber();
			transaction.setFreeData1(customData);
			transaction.setTranslationCode("01612");
		}

		@SuppressWarnings("unchecked")
		public Boolean decompose(Object view, Transaction transaction) {
			List<AccountView> vs = (List<AccountView>) view;
			String bit48 = transaction.getFreeData1();

			int len = 86;
			int loop = Integer.valueOf(bit48.substring(8, 10).trim());
			int base = 10;

			for (int i = 0; i < loop; i++) {
				AccountView av = new AccountView();
				av.setCardNumber(bit48.substring(base + len * i,
						base + len * i + 16).trim());
				av.setAccountNumber(bit48.substring(base + len * i + 16, base
						+ len * i + 26));
				av.setCustomerName(bit48.substring(base + len * i + 26,
						base + len * i + 56).trim());
				av.setProductCode(bit48.substring(base + len * i + 56,
						base + len * i + 86).trim());
				Product p = productDao.get(av.getProductCode());
				av.setProductName(p.getProductName());
				av.setProductId(p.getId());

				vs.add(av);
			}

			return Boolean.TRUE;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public LotteryView getLotteryViewPoint(LotteryView view) {
		view.setTransactionType(Constants.INQUIRY_LOTTERY);

		Transaction t = TransformerFactory.getTransformer(view).transformTo(
				view, new Transaction());
		MessageCustomizer customizer = new LotteryViewMessageCustomizer();
		customizer.compose(view, t);

		CommLink link = new MxCommLink(t);
		link.sendMessage();

		if (t.getResponseCode() == null) {
			throw new BusinessException("Data Undian Tidak Ditemukan");
		} else if (t.getResponseCode().equals(Constants.SUCCESS_CODE)
				|| !t.getResponseCode().equals(Constants.TIMEOUT_CODE)) {
			try {
				customizer.decompose(view, t);
			} catch (Exception e) {
				throw new BusinessException("Data Undian Tidak Ditemukan");
			}
		} else {
			throw new BusinessException("Data Undian Tidak Ditemukan");
		}
		view.setResponseCode(t.getResponseCode());
		view.setReferenceNumber(t.getReferenceNumber());
		view.setResponseCode(Constants.SUCCESS_CODE);

		return view;
	}

	public List<AccountSsppView> getAccountSsppPerPage(AccountView view) {
		asvs = new ArrayList<AccountSsppView>();
		String messageLength = "0048";
		String typeIdentifier = "0200";
		// view.getAccountNumber();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = sdf1.format(new Date());
		String stan = ReferenceGenerator.generate();
		// view.getTerminalId();
		String rcInput = "00";
		String message = messageLength + typeIdentifier
				+ view.getAccountNumber() + time
				+ stan.substring(stan.length() - 6) + view.getTerminalId()
				+ rcInput;
		logger.info("message=" + message);
		/*
		 * if (transporter.transmit(message)) { synchronized (this) {
		 * logger.info("out=" + out); while ( !bundlingMessage() ) { try {
		 * logger.info("wait out=" + out); wait(); } catch (InterruptedException
		 * e) { //-- do nothing } }
		 * 
		 * logger.info("perPage asvs.size()=" + asvs.size()); if (asvs.size() >
		 * 0) { String rc = out.substring(46, 48); if (rc.equals("00")) { String
		 * endFlag = out.substring(out.length() - 1); AccountSsppView lastAsv =
		 * asvs.get(asvs.size() - 1); if (endFlag.equals("1")) {
		 * lastAsv.setAgainFlag(false); } else { lastAsv.setAgainFlag(true); }
		 * logger.info("endFlag=" + endFlag + " lastAsv.getAgainFlag()=" +
		 * lastAsv.getAgainFlag()); }
		 * 
		 * } out = null; } }
		 */
		/*
		 * bundlingMessage();
		 * 
		 * logger.info("perPage getAccountSspp 7 asvs.size() = " + asvs.size());
		 * if (asvs.size() > 0) { //looping selama flag = 1 String endFlag =
		 * out.substring(out.length() - 1); AccountSsppView lastAsv =
		 * asvs.get(asvs.size() - 1); if (endFlag.equals("1")) {
		 * lastAsv.setAgainFlag(false); } else { lastAsv.setAgainFlag(true); }
		 * logger.info("endFlag=" + endFlag + " lastAsv.getAgainFlag()=" +
		 * lastAsv.getAgainFlag()); }
		 */

		if (transporter.transmit(message)) {
			int x = 0;
			while (true) {
				x += 1;
				logger.info("x=" + x);
				if (bundlingMessage()) {
					logger.info("perPage asvs.size()=" + asvs.size());
					if (asvs.size() > 0) {
						String rc = out.substring(46, 48);
						if (rc.equals("00")) {
							String endFlag = out.substring(out.length() - 1);
							AccountSsppView lastAsv = asvs.get(asvs.size() - 1);
							if (endFlag.equals("1")) {
								lastAsv.setAgainFlag(false);
							} else {
								lastAsv.setAgainFlag(true);
							}
							logger.info("endFlag=" + endFlag
									+ " lastAsv.getAgainFlag()="
									+ lastAsv.getAgainFlag());
						}
					}
					out = null;
					break;
				}
				if (x == 3)
					break;
			}
		}

		return asvs;
	}

	private boolean bundlingMessage() {
		try {
			Thread.currentThread().sleep(9000); // response terlama yg ditemukan
												// 4000
		} catch (Exception e) {

		} finally {
			logger.info("out bundlingMessage=" + out);
			if (out != null && out.length() > 46) {
				String rc = out.substring(46, 48);
				logger.info("rc=" + rc + " out=" + out);
				AccountSsppView asvRCNotSuccess = new AccountSsppView();
				if (rc.equals("00")) {
					int n = Integer.parseInt(out.substring(48, 50));
					logger.info("n=" + n);
					String trxs = out.substring(50);
					logger.info("trxs=" + trxs);
					for (int i = 0; i < n; i++) {
						AccountSsppView asv = new AccountSsppView();
						String oneTrx = trxs.substring(0 + (70 * i),
								70 + (70 * i));
						int page = Integer.parseInt(oneTrx.substring(65, 68)); // 8
																				// +
																				// 4
																				// +
																				// 1
																				// +
																				// 21
																				// +
																				// 21
																				// +
																				// 10
																				// +
																				// 3
																				// +
																				// 2
						int row = Integer.parseInt(oneTrx.substring(68, 70));
						asv.setPage(page);
						asv.setRow(row);
						asv.setSsppRow(row);
						/*
						 * 20111222MXAGD 30,000 9,232,643,495-ATMEDC 00704
						 * 20111222MXAGD 50,000- 9,232,693,495-ATMEDC 00705
						 * menjadi _DataDummy[0] =
						 * "01   11/01/11    11            10,000,000.00                                    23,649,162.66    9999\n"
						 * ; _DataDummy[1] =
						 * "02   11/01/11    99                                     5,000,000.00            18,649,162.66    9999\n"
						 * ;
						 */

						String content = "";
						content += "   "; // (row < 10) ? "0" + row : "" + row;
						content += "  ";
						content += oneTrx.substring(6, 8) + "-"
								+ oneTrx.substring(4, 6) + "-"
								+ oneTrx.substring(2, 4);
						content += "   ";
						content += oneTrx.substring(8, 12);
						content += "   ";
						String debitCredit = oneTrx.substring(13, 34);
						boolean isMinus = debitCredit.contains("-");
						// content += oneTrx.substring(12, 13).equals("D") ?
						// debitCredit + "                     "
						// :"                      " + debitCredit ;
						content += oneTrx.substring(12, 13).equals("D") ? debitCredit
								+ "                        "
								: "                        " + debitCredit;
						content += "    ";

						content += oneTrx.substring(34, 54) + " CR";
						content += "  ";
						content += oneTrx.substring(59, 65).trim();
						asv.setContent(content);
						asvs.add(asv);
					}
					logger.info("asvs.size()=" + asvs.size());
				} else if (rc.equals("01") || rc.equals("02")
						|| rc.equals("03") || rc.equals("04")
						|| rc.equals("05") || rc.equals("20")
						|| rc.equals("21") || rc.equals("S1")) {
					logger.info("rc=" + rc);
					asvRCNotSuccess.setPage(0);
					asvRCNotSuccess.setRow(0);
					asvRCNotSuccess.setSsppRow(0);
					if (rc.equals("01")) { // 01 = Bukan Rekening Tabungan
						asvRCNotSuccess.setContent(Constants.SSPP.RC01);
					} else if (rc.equals("02")) {
						asvRCNotSuccess.setContent(Constants.SSPP.RC02); // Rekening
																			// Tidak
																			// Terdaftar
					} else if (rc.equals("03")) {
						asvRCNotSuccess.setContent(Constants.SSPP.RC03); // Currency
																			// Rekening
																			// Tidak
																			// Terdaftar
					} else if (rc.equals("04")) {
						asvRCNotSuccess.setContent(Constants.SSPP.RC04); // Kode
																			// Format
																			// Printer
																			// tidak
																			// terdaftar
																			// (Internal
																			// Code)
					} else if (rc.equals("05")) {
						asvRCNotSuccess.setContent(Constants.SSPP.RC05); // Record
																			// gagal
																			// diupdate
					} else if (rc.equals("20")) {
						asvRCNotSuccess.setContent(Constants.SSPP.RC20); // Message
																			// Length
																			// Not
																			// Valid
					} else if (rc.equals("21")) {
						asvRCNotSuccess.setContent(Constants.SSPP.RC21); // Message
																			// ID
																			// not
																			// valid
																			// for
																			// this
																			// Transaction
					} else if (rc.equals("S1")) {
						asvRCNotSuccess.setContent(Constants.SSPP.RCS1); // Silakan
																			// ke
																			// Kantor
																			// Cabang
																			// Untuk
																			// Mendapatkan
																			// Buku
																			// yang
																			// Baru
					}
					asvs.add(asvRCNotSuccess);
				}

				else {
					throw new BusinessException("1009", rc);
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public void onMessage(String argMsg) {
		logger.info("Main.onMessage() = " + argMsg);
		out = argMsg;

		// transporter.shutdown();
	}

	@Override
	public List<AccountStatementView> checkTransactionStatus(AccountView view) {
		Date today = new Date();

		List<AccountStatementView> views = new ArrayList<AccountStatementView>();

		List<Transaction> ts = transactionDao.getAllForAccount(
				view.getAccountNumber(), Constants.VOUCHER_PURCHASE_CODE,
				today, today);

		if (ts != null) {
			for (Transaction t : ts) {

				TransactionData transactionData = transactionDataDao
						.getByTransactionFk(t.getId());
				VoucherPurchaseView vpv = (VoucherPurchaseView) EngineUtils
						.deserialize(transactionData);

				if (t.getStatus().equals(Constants.PENDING_STATUS)
						|| t.getResponseCode().equals(Constants.TIMEOUT_CODE)) {

					// -- change transaction status just for inquiry
					t.setTransactionType(Constants.VOUCHER_PURCHASE_CHK_CODE);
					t.setTerminalId(view.getTerminalId());
					t.setMerchantType(view.getMerchantType());
					MessageCustomizer customizer = new TransactionStatusMessageCustomizer(
							transactionData, vpv);
					customizer.compose(vpv, t);

					CommLink link = new MxCommLink(t);
					link.sendMessage();

					// -- restore transaction status back
					t.setTransactionType(Constants.VOUCHER_PURCHASE_CODE);
					AccountStatementView asv = transform(t);
					if (t.getResponseCode().equals(Constants.SUCCESS_CODE)) {
						customizer.decompose(asv, t);
						t.setStatus(Constants.SUCCEED_STATUS);
						transactionDao.save(t);
					} else if (t.getResponseCode().equals(
							Constants.TIMEOUT_CODE)) {
						// do nothing, still a pending transaction
					} else {
						// throw new BusinessException("IB-1009",
						// t.getResponseCode());
						customizer.decompose(asv, t);
						t.setStatus(Constants.FAILED_STATUS);
						transactionDao.save(t);
					}
				} else {
					// do nothing just add to the list
				}
				AccountStatementView asv = transform(t);
				asv.setTransactionName(transactionTypeDao.get(
						t.getTransactionType()).getDescription());
				asv.setAmount(new BigDecimal(vpv.getDenomination()));
				asv.setTransactionId(t.getId());
				views.add(asv);
			}
		}

		return views;
	}

	// bsueb cek transfer atmb status
	public Transaction getTransferViewForTransferAMTBStatus(TransferView tv) {
		return transactionDao.getTransactionForTransferAMTBStatus(
				tv.getAccountNumber(), tv.getCustomerReference(),
				tv.getBillerCode(), tv.getAmount());
	}

	// bsueb check provider denom di hari yg sama
	public Boolean checkProviderDenomAtSameDay(VoucherPurchaseView vv) {
		try {
			NumberFormat formatter = NumberFormat.getInstance(Locale.GERMAN);
			return transactionDao.checkProviderDenomAtSameDay(vv
					.getAccountNumber(), vv.getCustomerReference(),
					new BigDecimal(formatter.parse(vv.getDenomination())
							.doubleValue()), vv.getTransactionDate());
		} catch (Exception ex) {
			throw new BusinessException(ex.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public List<PortfolioView> getPortfolioFromIGate(AccountView view,
			String portfolioType) {
		List<PortfolioView> pvs = new ArrayList<PortfolioView>();

		view.setTransactionType(Constants.PORTFOLIO.TRANS_TYPE);
		Transaction t = TransformerFactory.getTransformer(view).transformTo(
				view, new Transaction());
		t.setProviderCode(portfolioType); // disimpan di table m_feature sbg
											// providerCode utk bit 56
		int index = 0;
		boolean nextData = true;

		while (nextData) {

			List<PortfolioView> portos = new ArrayList<PortfolioView>();

			// ntuk sementara index ditaruh di freeData2
			String strIndex = StringUtils.leftPad(Integer.toString(index), 4,
					"0");
			t.setFreeData2(strIndex);

			CommLink link = new MxCommLink(t);
			MessageCustomizer customizer = new PortfolioViewMessageCustomizer();
			customizer.compose(view, t);

			link.sendMessage();
			if (t.getResponseCode().equals(Constants.SUCCESS_CODE)) {
				customizer.decompose(portos, t);
			} else {
				throw new BusinessException("IB-1009", t.getResponseCode());
			}

			if (portos != null && portos.size() > 0) {
				pvs.addAll(portos);
				index = index + portos.size();
				PortfolioView last = portos.get(portos.size() - 1);
				nextData = "Y".equals(last.getFlagAgain());
			} else {
				nextData = false;
			}

		}

		return pvs;
	}

	private class PortfolioViewMessageCustomizer implements MessageCustomizer {
		private PortfolioViewMessageCustomizer() {
		}

		public void compose(BaseView view, Transaction transaction) {
			transaction.setFreeData1(transaction.getFreeData2() + "N");
			transaction.setFreeData2(null);
			transaction.setFreeData3(null);
			transaction.setToAccountNumber(null);
		}

		public Boolean decompose(Object view, Transaction trx) {
			List<PortfolioView> pvs = (List<PortfolioView>) view;
			String bit48 = trx.getFreeData1();

			if (trx.getResponseCode().equals("00")) {
				// format 48 = XXXXY + content
				String content = bit48.substring(5);
				int n = Integer.parseInt(bit48.substring(0, 4));
				if (n > 0) {
					if (trx.getProviderCode().equals(Constants.PORTFOLIO.CASA)) {
						/*
						 * reply structure 01. account number. n10 02. account
						 * status. an1 03. account name. an30 04. account type.
						 * an1 05. product type. an30 06. account currency. an3
						 * 07. branch. an3 08. plafond. n15 + sign 80+78 09.
						 * blocked balance. n15 + sign 10. minimum balance. n15
						 * + sign 11. clearing settlement. n15 + sign 12.
						 * available balance. n15 + sign
						 */
						// 10 + 1 + 30 + 1 + 30 + 3 + 3 + 15 * 5 + 1 * 5 = 158
						int LENGTH = 158;
						for (int i = 0; i < n; i++) {
							int vLength = i * LENGTH;
							PortfolioView pv = new PortfolioView();
							pv.setAccountNumber(content.substring(0 + vLength,
									10 + vLength).trim());
							pv.setAccountStatus(Integer.parseInt(content
									.substring(10 + vLength, 11 + vLength)));
							pv.setAccountName(content.substring(11 + vLength,
									41 + vLength).trim());
							pv.setAccountType(content.substring(41 + vLength,
									42 + vLength));
							pv.setProductName(content.substring(42 + vLength,
									72 + vLength).trim());
							pv.setCurrencyCode(content.substring(72 + vLength,
									75 + vLength));
							pv.setBranchCode(content.substring(75 + vLength,
									78 + vLength));
							pv.setPlafond(EngineUtils.parseIsoAmount(content
									.substring(78 + vLength, 94 + vLength)));
							pv.setBlockedBalance(EngineUtils
									.parseIsoAmount(content.substring(
											94 + vLength, 110 + vLength)));
							pv.setMinimumBalance(EngineUtils
									.parseIsoAmount(content.substring(
											110 + vLength, 126 + vLength)));
							pv.setAvailableBalance(EngineUtils
									.parseIsoAmount(content.substring(
											142 + vLength, 158 + vLength)));
							pv.setTransactionStatus(Constants.PORTFOLIO.CASA);
							pvs.add(pv);
						}
					} else if (trx.getProviderCode().equals(
							Constants.PORTFOLIO.DEPOSITO)) {
						/*
						 * reply structure 01. nomor deposito. an10 02. mata
						 * uang. an3 03. nominal. n15 04. jangka waktu. n3 05.
						 * bunga per-tahun %. n3 06. tanggal efektif. n8
						 * yyyymmdd 07. tanggal jatuh tempo. n8 yyyymmdd 08.
						 * rekening kredit. n10 09. instruksi saat jatuh tempo.
						 * a1
						 */
						// 10 + 3 + 15 + 2 + 5 + 8 + 8 + 10 + 1 = 62
						int LENGTH = 62;
						for (int i = 0; i < n; i++) {
							int vLength = i * LENGTH;
							PortfolioView pv = new PortfolioView();
							pv.setDepositoNumber(content.substring(0 + vLength,
									10 + vLength).trim());
							pv.setCurrencyCode(content.substring(10 + vLength,
									13 + vLength));
							pv.setNominalDeposito(EngineUtils
									.getBigDecimalValue(content.substring(
											13 + vLength, 28 + vLength), 0));
							try {
								pv.setJangkaWaktu(Integer.toString(Integer
										.parseInt(content.substring(28 + vLength,
												30 + vLength))));
							} catch (Exception e) {
								pv.setJangkaWaktu(Integer.toString(Integer.parseInt("0")));// TODO: handle exception
							}
							
							pv.setSatuanWaktu(content.substring(30 + vLength, 31 + vLength));
							pv.setAnnualRate(content.substring(31 + vLength,
									35 + vLength) + "00");
							pv.setEffectiveDate(content.substring(35 + vLength,
									43 + vLength));
							pv.setDueDate(content.substring(43 + vLength,
									51 + vLength));
							pv.setCreditAccount(content.substring(51 + vLength,
									61 + vLength));
							pv.setInstruction(content.substring(61 + vLength,
									62 + vLength));
							pv.setTransactionStatus(Constants.PORTFOLIO.DEPOSITO);
							pvs.add(pv);
						}
					} else {
						/*
						 * reply structure 01. nomor pinjaman. an10 02. mata
						 * uang. an3 03. pokok pinjaman. n15 04. bunga pinjaman.
						 * an7 05. tenor/lama pinjaman. n3 06. jumlah angsuran.
						 * n15 07. sisa pinjaman. n15 08. tanggal efektif. n8
						 * yyyymmdd 09. tanggal jatuh tempo. n8 yyyymmdd 10.
						 * tunggakan pokok. an15 11. tunggakan bunga. an15 12.
						 * tanggal bayar pokok berikut. n8 yyyymmdd 13. tanggal
						 * bayar bunga berikut. n8 yyyymmdd
						 */
						// 10 + 3 + 15 + 7 + 3 + 15 + 15 + 8 + 8 + 15 + 15 + 8 +
						// 8 = 130 + 1 + 2 + 3 + 15 * 4 = 196
						int LENGTH = 130;
						for (int i = 0; i < n; i++) {
							int vLength = i * LENGTH;
							PortfolioView pv = new PortfolioView();
							pv.setLoanNumber(content.substring(0 + vLength,
									10 + vLength));
							pv.setCurrencyCode(content.substring(10 + vLength,
									13 + vLength));
							pv.setPokokPinjaman(EngineUtils.getBigDecimalValue(
									content.substring(13 + vLength,
											28 + vLength), 0));
							pv.setBungaPinjaman(EngineUtils.getBigDecimalValue(
									content.substring(28 + vLength,
											35 + vLength), 0));
							pv.setTenor(Integer.parseInt(content.substring(
									35 + vLength, 38 + vLength)));
							pv.setJumlahAngsuran(EngineUtils
									.getBigDecimalValue(content.substring(
											38 + vLength, 53 + vLength), 0));
							pv.setSisaPinjaman(EngineUtils.getBigDecimalValue(
									content.substring(53 + vLength,
											68 + vLength), 0));
							pv.setEffectiveDate(content.substring(68 + vLength,
									76 + vLength));
							pv.setDueDate(content.substring(76 + vLength,
									84 + vLength));
							pv.setTunggakanPokok(EngineUtils
									.getBigDecimalValue(content.substring(
											84 + vLength, 99 + vLength), 0));
							pv.setTunggakanBunga(EngineUtils
									.getBigDecimalValue(content.substring(
											99 + vLength, 114 + vLength), 0));
							pv.setTanggalBayarPokok(content.substring(
									114 + vLength, 122 + vLength));
							pv.setTanggalBayarBunga(content.substring(
									122 + vLength, 130 + vLength));

							// pv.setSatuanWaktu(content.substring(130 +
							// vLength, 131 + vLength));
							// pv.setFrekuensiBayar(content.substring(131 +
							// vLength, 133 + vLength));
							// pv.setBayarKe(
							// Integer.parseInt(content.substring(133 + vLength,
							// 136 + vLength)));
							// pv.setNilaiBunga(EngineUtils.getBigDecimalValue(content.substring(136
							// + vLength, 151 + vLength), 2));
							// pv.setPrinciplePaidAmount(EngineUtils.getBigDecimalValue(content.substring(151
							// + vLength, 166 + vLength), 0));
							// pv.setDuePrincipleAmount(EngineUtils.getBigDecimalValue(content.substring(166
							// + vLength, 181 + vLength), 0));
							// pv.setPenaltyPaidAmount(EngineUtils.getBigDecimalValue(content.substring(181
							// + vLength, 196 + vLength), 0));
							pv.setTransactionStatus(Constants.PORTFOLIO.LOAN);
							pvs.add(pv);
						}
					}
					PortfolioView lastPv = pvs.get(pvs.size() - 1);
					lastPv.setFlagAgain(bit48.substring(4, 5));
				}

			} else {
				return Boolean.FALSE;
			}

			return Boolean.TRUE;
		}
	}

	public BaseView ssppTemenos(BaseView view) {
		// TODO Auto-generated method stub
		// BaseViewService viewService = getServiceObject(view);
		// viewService.preProcess(view);

		Transaction transaction = TransformerFactory.getTransformer(view)
				.transformTo(view, new Transaction());
		MessageCustomizer msgCustomizer = KioskReprintMessageCustomizer
				.instance().getReprintMessageCustomizer(view);

		transaction.setReferenceNumber(view.getReferenceNumber());

		msgCustomizer.compose(view, transaction);

		CommLink link = new MxCommLink(transaction);
		link.sendMessage();

		if (transaction.getResponseCode().equals(Constants.SUCCESS_CODE)) {
			msgCustomizer.decompose(view, transaction);
		} else {
			throw new BusinessException("IB-1009",
					transaction.getResponseCode());
		}

		EngineUtils.setTransactionStatus(transaction);
		transaction.setExecutionType(Constants.NOW_ET);
		transaction.setCreated(new Date());
		transaction.setCreatedby(0L);
		transaction.setUpdated(new Date());
		transaction.setUpdatedby(0L);

		transactionDao.save(transaction);

		return view;
	}

	protected BaseViewService getServiceObject(BaseView view) {
		String viewServiceName = view.getClass().getSimpleName() + "Service";
		viewServiceName = viewServiceName.substring(0, 1).toLowerCase()
				+ viewServiceName.substring(1);
		return (BaseViewService) ServiceLocator.getService(viewServiceName);
	}

	@Override
	public InputStream getAccountStatementCsvStream(AccountView view,
			List<AccountStatementView> asvList, Date startDate, Date endDate,
			BigDecimal endingBalance) {
		System.out.println("start downloading");
		BigDecimal saldoAwal = endingBalance;
		// max. byte array size (5 MB)
		final int SIZE_LIMIT = 500000000;

		ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
		PrintWriter pw = new PrintWriter(byteArray);
		DateFormat dfIso = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		DateFormat dfYmd = new SimpleDateFormat("yyyy-MM-dd");
		// write header
		StringBuilder sb = new StringBuilder();
		sb.append("\"Tgl Download: \",\"").append(dfIso.format(new Date()))
				.append("\"\n");
		sb.append("\"No. Rekening:\",=\"").append(view.getAccountNumber())
				.append("\"\n");
		sb.append("\"Nama:\",\"").append(view.getAccountName()).append("\"\n");
		sb.append("\"Mata Uang:\",\"").append(view.getCurrencyCode())
				.append("\"\n\n");
		sb.append("\"Tanggal\",\"Keterangan\",\"Nominal\",,\"Saldo\"");
		pw.println(sb.toString());
		System.out.println("string builder : " + sb);
		int i = 0;
		BigDecimal kredit = BigDecimal.ZERO;
		BigDecimal debet = BigDecimal.ZERO;
		for (AccountStatementView asv : asvList) {
			System.out.println("asv ke i :" + i);
			if (i == 0) {
				if (asv.getTransactionIndicator().equals("D")) {
					saldoAwal = asv.getRunningBalance().add(asv.getAmount());
				} else {
					saldoAwal = asv.getRunningBalance().subtract(
							asv.getAmount());
				}
			}
			if (asv.getTransactionIndicator().equals("D")) {
				debet = debet.add(asv.getAmount());
			} else {
				kredit = kredit.add(asv.getAmount());
			}
			sb.delete(0, sb.length());
			sb.append("\"").append(dfYmd.format(asv.getValueDate()))
					.append("\"").append(",");
			sb.append("=\"").append(asv.getTransactionName()).append("\"")
					.append(",");
			sb.append("\"").append(asv.getAmount()).append("\"").append(",");
			sb.append("\"").append(asv.getStrTransactionIndicator())
					.append("\"").append(",");
			sb.append("\"").append(asv.getRunningBalance()).append("\"")
					.append(",");
			pw.println(sb.toString());
			endingBalance = asv.getRunningBalance();
			i++;
			if (byteArray.size() > SIZE_LIMIT)
				break;
		}
		sb.delete(0, sb.length());
		sb.append("\n\"Saldo Awal: \",\"").append(saldoAwal).append("\"\n");
		sb.append("\"Kredit:\",\"").append(kredit).append("\"\n");
		sb.append("\"Debet\",\"").append(debet).append("\"\n");
		sb.append("\"Saldo Akhir:\",\"").append(endingBalance).append("\"\n");
		pw.println(sb.toString());
		System.out.println("end flush");
		pw.flush();
		pw.close();
		return new ByteArrayInputStream(byteArray.toByteArray());
	}

	public List<LotteryTransactionView> getPortfolioFromIGateUndian(
			LotteryTransactionView view, String portfolioType) {
		List<LotteryTransactionView> pvs = new ArrayList<LotteryTransactionView>();

		// view.setTransactionType(Constants.INQUIRY_LOTTERY);
		Transaction t = TransformerFactory.getTransformer(view).transformTo(
				view, new Transaction());
		t.setProviderCode(portfolioType); // disimpan di table m_feature sbg
											// providerCode utk bit 56
		int index = 0;
		boolean nextData = true;
		BigDecimal endingBalance = BigDecimal.ZERO;
		while (nextData) {

			List<LotteryTransactionView> portos = new ArrayList<LotteryTransactionView>();

			CommLink link = new MxCommLink(t);
			MessageCustomizer customizer = new LotteryUndianViewMessageCustomizer();
			customizer.compose(view, t);

			link.sendMessage();
			if (t.getResponseCode().equals(Constants.SUCCESS_CODE)) {
				customizer.decompose(portos, t);
			} else {
				throw new BusinessException("IB-1009", t.getResponseCode());
			}

			if (portos != null && portos.size() > 0) {
				pvs.addAll(portos);
				index = index + portos.size();
				LotteryTransactionView last = portos.get(portos.size() - 1);
				nextData = "Y".equals(last.getNextAvailableFlag());

				last.setPosisiAwal(last.getPosisiAwal()
						.add(last.getTotalData()));

			} else {
				nextData = false;
			}

		}

		return pvs;
	}

	/**
	 * Class to compose and decompose custom data for lottery view feature.
	 */
	private class LotteryUndianViewMessageCustomizer implements
			MessageCustomizer {
		private LotteryUndianViewMessageCustomizer() {
		}

		public void compose(BaseView view, Transaction transaction) {
			LotteryTransactionView lv = (LotteryTransactionView) view;

			// an 4 PosisiAwal
			String customData = StringUtils.rightPad("0000", 4, "");
			// n 4 TotalData
			customData += StringUtils.rightPad("0000", 4, "");
			// an 1 next
			customData += StringUtils
					.rightPad(lv.getNextAvailableFlag(), 1, "");
			// an 990 nomorRekening
			customData += StringUtils.rightPad(lv.getAccountNumber(), 0, "");

			transaction.setFreeData1(customData); // Bit 48
			transaction.setTranslationCode(Constants.TRANSLATION_LOTTERY);

		}

		public Boolean decompose(Object view, Transaction transaction) {

			List<LotteryTransactionView> views = (List<LotteryTransactionView>) view;

			String bit48 = transaction.getFreeData1();
			String bit120 = transaction.getFreeData4();
			String bit121 = transaction.getBit121();
			String bit122 = transaction.getFreeData5();
			String bit123 = transaction.getBit123();

			int LENGTH = 74;

			int lenghtBit120 = 0;
			int lenghtBit121 = 0;
			int lenghtBit122 = 0;
			int lenghtBit123 = 0;

			try {
				if (transaction.getFreeData4().length() % LENGTH == 1) {
					lenghtBit120 = (transaction.getFreeData4().length() / LENGTH) + 1;
				} else {
					lenghtBit120 = transaction.getFreeData4().length() / LENGTH;
				}

				// bit120

				for (int i = 0; i < lenghtBit120; i++) {
					int vLength = i * LENGTH;
					LotteryTransactionView lv = new LotteryTransactionView();
					try {
						lv.setAccountNumber(bit120.substring(0 + vLength,
								20 + vLength));
						lv.setKodeUndian(bit120.substring(20 + vLength,
								24 + vLength));
						lv.setJenisUndian(bit120.substring(24 + vLength,
								74 + vLength));
					} catch (Exception e) {
						lv.setAccountNumber("");
						lv.setKodeUndian("");
						lv.setJenisUndian("");
					}
					System.out.println("Account Number "
							+ lv.getAccountNumber());
					System.out.println("Kode Undian Number "
							+ lv.getKodeUndian());
					System.out.println("Jenis Undian " + lv.getJenisUndian());
					views.add(lv);

				}

			} catch (Exception e) {
				// TODO: handle exception
			}

			try {
				if (transaction.getBit121().length() % LENGTH == 1) {
					lenghtBit121 = (transaction.getBit121().length() / LENGTH) + 1;
				} else {
					lenghtBit121 = transaction.getBit121().length() / LENGTH;
				}

				// bit121
				for (int i = 0; i < lenghtBit121; i++) {
					int vLength = i * LENGTH;
					LotteryTransactionView lv = new LotteryTransactionView();
					try {
						lv.setAccountNumber(bit121.substring(0 + vLength,
								20 + vLength));
						lv.setKodeUndian(bit121.substring(20 + vLength,
								24 + vLength));
						lv.setJenisUndian(bit121.substring(24 + vLength,
								74 + vLength));
					} catch (Exception e) {
						lv.setAccountNumber("");
						lv.setKodeUndian("");
						lv.setJenisUndian("");
					}
					System.out.println("Account Number "
							+ lv.getAccountNumber());
					System.out.println("Kode Undian Number "
							+ lv.getKodeUndian());
					System.out.println("Jenis Undian " + lv.getJenisUndian());
					views.add(lv);
				}

			} catch (Exception e) {
				// TODO: handle exception
			}

			try {
				if (transaction.getFreeData5().length() % LENGTH == 1) {
					lenghtBit122 = (transaction.getFreeData5().length() / LENGTH) + 1;
				} else {
					lenghtBit122 = transaction.getFreeData5().length() / LENGTH;
				}

				// bit122
				for (int i = 0; i < lenghtBit122; i++) {
					int vLength = i * LENGTH;
					LotteryTransactionView lv = new LotteryTransactionView();
					try {
						lv.setAccountNumber(bit122.substring(0 + vLength,
								20 + vLength));
						lv.setKodeUndian(bit122.substring(20 + vLength,
								24 + vLength));
						lv.setJenisUndian(bit122.substring(24 + vLength,
								74 + vLength));
					} catch (Exception e) {
						lv.setAccountNumber("");
						lv.setKodeUndian("");
						lv.setJenisUndian("");
					}
					System.out.println("Account Number "
							+ lv.getAccountNumber());
					System.out.println("Kode Undian Number "
							+ lv.getKodeUndian());
					System.out.println("Jenis Undian " + lv.getJenisUndian());
					views.add(lv);
				}

			} catch (Exception e) {
				// TODO: handle exception
			}

			try {
				if (transaction.getBit123().length() % LENGTH == 1) {
					lenghtBit123 = (transaction.getBit123().length() / LENGTH) + 1;
				} else {
					lenghtBit123 = transaction.getBit123().length() / LENGTH;
				}

				// bit123
				for (int i = 0; i < lenghtBit123; i++) {
					int vLength = i * LENGTH;
					LotteryTransactionView lv = new LotteryTransactionView();
					try {
						lv.setAccountNumber(bit123.substring(0 + vLength,
								20 + vLength));
						lv.setKodeUndian(bit123.substring(20 + vLength,
								24 + vLength));
						lv.setJenisUndian(bit123.substring(24 + vLength,
								74 + vLength));
					} catch (Exception e) {
						lv.setAccountNumber("");
						lv.setKodeUndian("");
						lv.setJenisUndian("");
					}
					System.out.println("Account Number "
							+ lv.getAccountNumber());
					System.out.println("Kode Undian Number "
							+ lv.getKodeUndian());
					System.out.println("Jenis Undian " + lv.getJenisUndian());
					views.add(lv);
				}

			} catch (Exception e) {
				// TODO: handle exception
			}

			LotteryTransactionView lastPv = views.get(views.size() - 1);
			lastPv.setPosisiAwal(new BigDecimal(bit48.substring(0, 4)));
			lastPv.setTotalData(new BigDecimal(bit48.substring(4, 8)));
			lastPv.setNextAvailableFlag(bit48.substring(8, 9));
			// lastPv.setAccountNumber(bit48.substring(9));

			return Boolean.TRUE;
		}
	}

	@Override
	public List<LotteryTransactionView> getPortfolioFromIGateNomorUndian(
			LotteryTransactionView view, String portfolioType) {
		List<LotteryTransactionView> pvs = new ArrayList<LotteryTransactionView>();

		// view.setTransactionType(Constants.INQUIRY_LOTTERY);
		Transaction t = TransformerFactory.getTransformer(view).transformTo(
				view, new Transaction());
		t.setProviderCode(portfolioType); // disimpan di table m_feature sbg
											// providerCode utk bit 56
		int index = 0;
		boolean nextData = true;
		BigDecimal endingBalance = BigDecimal.ZERO;
		while (nextData) {

			List<LotteryTransactionView> portos = new ArrayList<LotteryTransactionView>();

			CommLink link = new MxCommLink(t);
			MessageCustomizer customizer = new LotteryNomorUndianViewMessageCustomizer();
			customizer.compose(view, t);

			link.sendMessage();
			if (t.getResponseCode().equals(Constants.SUCCESS_CODE)) {
				customizer.decompose(portos, t);
			} else {
				throw new BusinessException("IB-1009", t.getResponseCode());
			}

			if (portos != null && portos.size() > 0) {
				pvs.addAll(portos);
				index = index + portos.size();
				LotteryTransactionView last = portos.get(portos.size() - 1);
				nextData = "Y".equals(last.getNextAvailableFlag());
				last.setPosisiAwal(last.getPosisiAwal()
						.add(last.getTotalData()));
				// if (!nextData){
				// endingBalance = last.getTotalData();
				// if ("-".equals(bit48.substring(55,56))) endingBalance =
				// endingBalance.multiply(new BigDecimal("-1"));
				// }
				// asvs.addAll(asvsl);

			} else {
				nextData = false;
			}

		}

		return pvs;
	}

	/**
	 * Class to compose and decompose custom data for lottery view feature.
	 */
	private class LotteryNomorUndianViewMessageCustomizer implements
			MessageCustomizer {
		private LotteryNomorUndianViewMessageCustomizer() {
		}

		public void compose(BaseView view, Transaction transaction) {

			LotteryTransactionView lv = (LotteryTransactionView) view;
			// an 4 PosisiAwal
			String customData = StringUtils.leftPad(lv.getAccountNumber(), 20,
					"");
			// n 4 TotalData
			customData += StringUtils.rightPad(lv.getKodeUndian(), 4, "");
			// an 1 next
			customData += StringUtils.rightPad("0000", 4, "");

			transaction.setFreeData1(customData); // Bit 48
			transaction.setTranslationCode(Constants.TRANSLATION_LOTTERY);

		}

		public Boolean decompose(Object view, Transaction transaction) {

			List<LotteryTransactionView> lotteryTransactionViews = (List<LotteryTransactionView>) view;

			String bit48 = transaction.getFreeData1();
			String bit120 = transaction.getFreeData4();
			String bit121 = transaction.getBit121();
			String bit122 = transaction.getFreeData5();
			String bit123 = transaction.getBit123();

			try {

				String bit120Length[] = bit120.split(",");

				for (int i = 0; i < bit120Length.length; i++) {

					LotteryTransactionView lv = new LotteryTransactionView();
					lv.setNomorUndian(bit120Length[i]);
					lotteryTransactionViews.add(lv);

				}

			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				String bit121Length[] = bit121.split(",");

				for (int i = 0; i < bit121Length.length; i++) {

					LotteryTransactionView lv = new LotteryTransactionView();
					lv.setNomorUndian(bit121Length[i]);
					lotteryTransactionViews.add(lv);

				}

			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				String bit122Length[] = bit122.split(",");

				for (int i = 0; i < bit122Length.length; i++) {

					LotteryTransactionView lv = new LotteryTransactionView();
					lv.setNomorUndian(bit122Length[i]);
					lotteryTransactionViews.add(lv);

				}

			} catch (Exception e) {
				// TODO: handle exception
			}
			try {

				String bit123Length[] = bit123.split(",");

				for (int i = 0; i < bit123Length.length; i++) {

					LotteryTransactionView lv = new LotteryTransactionView();
					lv.setNomorUndian(bit123Length[i]);
					lotteryTransactionViews.add(lv);

				}

			} catch (Exception e) {
				// TODO: handle exception
			}

			LotteryTransactionView lotteryView = lotteryTransactionViews
					.get(lotteryTransactionViews.size() - 1);
			try {
				lotteryView.setAccountNumber(bit48.substring(0, 20));
			} catch (Exception e) {
				lotteryView.setAccountNumber("");
			}
			try {
				lotteryView.setNamaUndian(bit48.substring(20, 70));
			} catch (Exception e) {
				lotteryView.setNamaUndian("");// TODO: handle exception
			}
			
			try {
				lotteryView.setPosisiAwal(new BigDecimal(bit48.substring(70, 74)));
			} catch (Exception e) {
				lotteryView.setPosisiAwal(BigDecimal.ZERO);// TODO: handle exception
			}			
			
			try {
				lotteryView.setTotalData(new BigDecimal(bit48.substring(74, 78)));
			} catch (Exception e) {
				lotteryView.setTotalData(BigDecimal.ZERO);
			}
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

			try {
				lotteryView.setAwalPeriod(dateFormat.parse(bit48.substring(78,
						86)));
			} catch (Exception e) {
				lotteryView.setAwalPeriod(new Date());// TODO: handle exception
			}

			try {
				lotteryView.setAkhirPeriod(dateFormat.parse(bit48.substring(86,
						94)));
			} catch (Exception e) {
				lotteryView.setAkhirPeriod(new Date());// TODO: handle exception
			}

			try {
				lotteryView.setNextAvailableFlag(bit48.substring(94, 95));
			} catch (Exception e) {
				lotteryView.setNextAvailableFlag("N");// TODO: handle exception
			}
			

			return Boolean.TRUE;
		}
	}

	public static void main(String[] args) {
		String str = "98,0000";
		//str +
	}
}
