package com.dwidasa.engine.service.impl;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.CustomerDao;
import com.dwidasa.engine.dao.CustomerDeviceDao;
import com.dwidasa.engine.dao.FileBatchDao;
import com.dwidasa.engine.dao.InboxCustomerDao;
import com.dwidasa.engine.dao.InboxDao;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.CustomerDevice;
import com.dwidasa.engine.model.FileBatch;
import com.dwidasa.engine.model.Inbox;
import com.dwidasa.engine.model.InboxCustomer;
import com.dwidasa.engine.model.FileBatch.BatchStatus;
import com.dwidasa.engine.service.FileBatchService;
import com.dwidasa.engine.service.InboxService;
import com.dwidasa.engine.service.notif.PushAdrManager;
import com.dwidasa.engine.service.notif.PushBbManager;
import com.dwidasa.engine.service.notif.PushIosManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA. User: rk Date: 8/1/11 Time: 9:47 AM
 */
@Service("fileBatchService")
public class FileBatchServiceImpl extends GenericServiceImpl<FileBatch, Long>
		implements FileBatchService {

	@Autowired
	private CustomerDao customerDao;

	@Autowired
	private CustomerDeviceDao customerDeviceDao;

	@Autowired
	private InboxDao inboxDao;

	@Autowired
	private InboxService inboxService;

	@Autowired
	private InboxCustomerDao inboxCustomerDao;

	@Autowired
	private FileBatchDao fileBatchDao;

	@Autowired
	private PushAdrManager pushAdrManager;

	@Autowired
	private PushBbManager pushBbManager;

	@Autowired
	private PushIosManager pushIosManager;

	@Autowired
	public FileBatchServiceImpl(FileBatchDao FileBatchDao) {
		super(FileBatchDao);
	}

	@Override
	public void processFileBatchInsertToInboxAndNotify(Long fileBatchId, String uploadDir, Long userId) {
		FileBatch fb = get(fileBatchId);
		File file = new File(uploadDir, fb.getFileName());			
		
		processFileBatchInsertToInboxAndNotify(file.getAbsolutePath(), userId);
		fb.setStatus(BatchStatus.executed.getId());
		save(fb);
	}

	@Override
	public void processFileBatchInsertToInboxAndNotify(String fileName, Long userId) {

		// Read file
		try {
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;

			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				// Insert into inbox where status = 1 then push notification
				// asumsi text file ";" separated: title;message;no_rek
				// norek bisa 1 bisa multi, kalo multi separated dg "&"
				// contoh:
				// Pemberitahuan;Yth. Nasabah...;*
				// Jatuh tempo pembayaran cicilan kredit;Yth. Nasabah...;123456
				// Jatuh tempo pembayaran cicilan kredit;Yth.
				// Nasabah...;123456&89068&129878
				System.out.println("Read line : " + strLine);
				String[] str = strLine.split(";", 3);
				System.out.println("Data to insert : " + str[0] + ", " + str[1] + ", " + str[2]);
				insertInboxAndNotify(str[0], str[1], str[2], userId);
			}

			// Close the input stream
			in.close();

		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}

	}

	private void insertInboxAndNotify(String title, String content, String noRek, Long userId) {
		String regex = ",";

		Inbox inbox = new Inbox();
		inbox.setTitle(title);
		inbox.setContent(content);
		inbox.setCreatedby(userId);
		inbox.setCreated(new Date());
		inbox.setUpdatedby(userId);
		inbox.setUpdated(new Date());
		
		Set<InboxCustomer> ics = new HashSet<InboxCustomer>();
		List<Long> customerIds = new ArrayList<Long>();
		
		if (noRek.equals("*")) {
			inbox.setForAll(true);
		} else if (!noRek.contains(regex)) {
			List<Customer> cs = customerDao.getByAccountNumber(noRek);
			for (Customer c : cs) {
				InboxCustomer ic = new InboxCustomer();
				ic.setInbox(inbox);
				ic.setCustomer(c);
				ics.add(ic);
				
				customerIds.add(c.getId());
			}
			inbox.setInboxCustomers(ics);
			
		} else {
			String[] noReks = noRek.split(regex);
			for (int i = 0; i < noReks.length; i++) {
				List<Customer> cs = customerDao.getByAccountNumber(noReks[i]);
				if (cs == null) continue;				
				for (Customer c : cs) {
					InboxCustomer ic = new InboxCustomer();
					ic.setInbox(inbox);
					ic.setCustomer(c);
					ics.add(ic);
					customerIds.add(c.getId());
				}
			}

			inbox.setInboxCustomers(ics);
		}

		if (customerIds.size() > 0)
			inboxService.sendMessage(customerIds, inbox);
		else
			inboxService.sendMessage(inbox);
			
		pushNotifyCustomer(inbox);
	}

	private void pushNotifyCustomer(Inbox inbox) {
		// get customer device for this inbox
		List<CustomerDevice> cds = null;
		
		if (inbox.isForAll()) 
			cds = customerDeviceDao.getAllActive();
		else 
			cds = customerDeviceDao.getAllActiveByInboxId(inbox.getId());
		// else if (inbox.customerInbox != null )
		// else return;

		if (cds == null) return;
		System.out.print("Customer devise size to notify : " + cds.size());
		
		for (CustomerDevice cd : cds) {
			String pushId = cd.getPushId();
			String title = inbox.getTitle();
			String payload = inbox.getId().toString();

			if (cd.getDeviceId().endsWith(Constants.BLACKBERRY)) {
				pushBbManager.doPush(pushId, payload, title);
			} else if (cd.getDeviceId().endsWith(Constants.ANDROID)) {
				pushAdrManager.doPush(pushId, payload, title);
			} else if (cd.getDeviceId().endsWith(Constants.IPHONE)) {
				pushIosManager.doPush(pushId, payload, title);
			}

		}

	}
}
