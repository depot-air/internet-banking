package com.dwidasa.admin.services;

import java.util.ArrayList;
import java.util.List;

import com.dwidasa.engine.model.Menu;

public class MenuManager {
	private final List<Menu> superuserMenu = new ArrayList<Menu>();
	private final List<Menu> adminMenu = new ArrayList<Menu>();
	private final List<Menu> treasuryMenu = new ArrayList<Menu>();
	private final List<Menu> dayAdminMenu = new ArrayList<Menu>();
	private final List<Menu> nightAdminMenu = new ArrayList<Menu>();
    private final List<Menu> kioskMenu = new ArrayList<Menu>();
    
    private final long TOKEN = 9;
    private final long KIOSK = 8;
	private final long MONITOR = 7;
	private final long CUSTOMER = 6;
	private final long TREASURY = 5;
	private final long MASTER = 4;
	private final long PRODUCT = 3;
	private final long SYSTEM = 2;
	private final long USER = 1;
	
	public MenuManager() {
		Menu parent;
		Menu children;
		
		//user menu for treasury and admin
		parent = new Menu(USER, "user", null, 0L);
		parent.setChildrens(new ArrayList<Menu>());
		children = new Menu(100L, "viewProfile", "user/viewProfile", USER);
		children.setParent(parent);
		parent.getChildrens().add(children);
		treasuryMenu.add(parent);
		dayAdminMenu.add(parent);
		adminMenu.add(parent);
		nightAdminMenu.add(parent);
        kioskMenu.add(parent);
		
		//user menu for superuser
		parent = new Menu(USER, "user", null, 0L);
		parent.setChildrens(new ArrayList<Menu>());
		children = new Menu(100L, "viewProfile", "user/viewProfile", USER);
		children.setParent(parent);
		parent.getChildrens().add(children);
		children = new Menu(101L, "maintenanceUserList", "user/maintenanceUserList", USER);
		children.setParent(parent);
		parent.getChildrens().add(children);
		superuserMenu.add(parent);
		
		//admin
		parent = new Menu(MASTER, "master", null, 0L);
		parent.setChildrens(new ArrayList<Menu>());
		children = new Menu(200L, "parameterList", "master/parameterList", MASTER);
		children.setParent(parent);
		parent.getChildrens().add(children);
		children = new Menu(201L, "accountTypeList", "master/accountTypeList", MASTER);
		children.setParent(parent);
		parent.getChildrens().add(children);
		children = new Menu(202L, "transactionTypeList", "master/transactionTypeList", MASTER);
		children.setParent(parent);
		parent.getChildrens().add(children);
		children = new Menu(203L, "currencyList", "master/currencyList", MASTER);
		children.setParent(parent);
		parent.getChildrens().add(children);
		children = new Menu(204L, "responseCodeList", "master/responseCodeList", MASTER);
		children.setParent(parent);
		parent.getChildrens().add(children);
		children = new Menu(205L, "cellularPrefixList", "master/cellularPrefixList", MASTER);
		children.setParent(parent);
		parent.getChildrens().add(children);
		children = new Menu(206L, "appVersionList", "master/appVersionList", MASTER);
		children.setParent(parent);
		children = new Menu(207L, "undianList", "master/undianList", MASTER);
		children.setParent(parent);
		parent.getChildrens().add(children);
		superuserMenu.add(parent);
		adminMenu.add(parent);
		
		parent = new Menu(PRODUCT, "product", null, 0L);
		parent.setChildrens(new ArrayList<Menu>());
		children = new Menu(300L, "billerList", "product/billerList", PRODUCT);
		children.setParent(parent);
		parent.getChildrens().add(children);
		children = new Menu(301L, "providerList", "product/providerList", PRODUCT);
		children.setParent(parent);
		parent.getChildrens().add(children);
		children = new Menu(302L, "billerProductList", "product/billerProductList", PRODUCT);
		children.setParent(parent);
		parent.getChildrens().add(children);
		children = new Menu(303L, "purchaseProductList", "product/purchaseProductList", PRODUCT);
		children.setParent(parent);
		parent.getChildrens().add(children);
		children = new Menu(304L, "maintenancePaymentList", "product/maintenancePaymentList", PRODUCT);
		children.setParent(parent);
		parent.getChildrens().add(children);
		superuserMenu.add(parent);
		adminMenu.add(parent);
		
		parent = new Menu(CUSTOMER, "customer", null, 0L);
		parent.setChildrens(new ArrayList<Menu>());
		children = new Menu(350L, "customerList", "customer/customerList", CUSTOMER);
		children.setParent(parent);
		
		parent.getChildrens().add(children);
		children = new Menu(351L, "ibToken", "customer/IBTokenInput", CUSTOMER);
		children.setParent(parent);
		
		parent.getChildrens().add(children);
		children = new Menu(352L, "merchantRegistration", "customer/merchantRegistrationInput", CUSTOMER);
		children.setParent(parent);
		
		parent.getChildrens().add(children);
		
//		children = new Menu(353L, "FileBactList", "admin/FileBatchList", CUSTOMER);
//		children.setParent(parent);
//		parent.getChildrens().add(children);
		children = new Menu(353L, "inboxList", "customer/inboxList", CUSTOMER);
		children.setParent(parent);
		
		
		parent.getChildrens().add(children);
		superuserMenu.add(parent);
		adminMenu.add(parent);

		parent = new Menu(TOKEN, "token", null, 0L);
		parent.setChildrens(new ArrayList<Menu>());
		children = new Menu(601L, "tokenImport", "token/tokendataimport", TOKEN);
		children.setParent(parent);
		parent.getChildrens().add(children);
		superuserMenu.add(parent);
		
		parent = new Menu(MONITOR, "monitor", null, 0L);
		parent.setChildrens(new ArrayList<Menu>());
		children = new Menu(375L, "sessionList", "monitor/sessionList", MONITOR);
		children.setParent(parent);
		parent.getChildrens().add(children);
		children = new Menu(376L, "transactionList", "monitor/transactionList", MONITOR);
		children.setParent(parent);
		parent.getChildrens().add(children);
		children = new Menu(377L, "auditLogList", "monitor/auditLogList", MONITOR);
		children.setParent(parent);
		parent.getChildrens().add(children);
		superuserMenu.add(parent);
		dayAdminMenu.add(parent);
		adminMenu.add(parent);

		//system menu for night admin
		parent = new Menu(SYSTEM, "system", null, 0L);
		parent.setChildrens(new ArrayList<Menu>());
		children = new Menu(400L, "bodList", "system/bodList", SYSTEM);
		children.setParent(parent);
		parent.getChildrens().add(children);
		nightAdminMenu.add(parent);
		adminMenu.add(parent);
		
		//system menu for superuser
		parent = new Menu(SYSTEM, "system", null, 0L);
		parent.setChildrens(new ArrayList<Menu>());
		children = new Menu(400L, "bodList", "system/bodList", SYSTEM);
		children.setParent(parent);
		parent.getChildrens().add(children);
		children = new Menu(410L, "reloadCache", "system/reloadCache", SYSTEM);
		children.setParent(parent);
		parent.getChildrens().add(children);
		superuserMenu.add(parent);
		adminMenu.add(parent);

        parent = new Menu(KIOSK, "kiosk", null, 0L);
        parent.setChildrens(new ArrayList<Menu>());
        children = new Menu(500L, "kioskTerminalList", "kiosk/kioskTerminalList", KIOSK);
        children.setParent(parent);
        parent.getChildrens().add(children);
        children = new Menu(501L, "kioskPrinterList", "kiosk/kioskPrinterList", KIOSK);
        children.setParent(parent);
        parent.getChildrens().add(children);
        children = new Menu(502L, "kioskPaperList", "kiosk/kioskPaperList", KIOSK);
        children.setParent(parent);
        parent.getChildrens().add(children);
        superuserMenu.add(parent);
        dayAdminMenu.add(parent);
        adminMenu.add(parent);

		//treasury
		parent = new Menu(TREASURY, "treasury", null, 0L);
		parent.setChildrens(new ArrayList<Menu>());
		children = new Menu(700L, "treasuryPenugasan", "treasury/treasuryStagePenugasan", TREASURY);
		children.setParent(parent);
		parent.getChildrens().add(children);
		
//		children = new Menu(701L, "treasuryListTransaction", "treasury/treasuryStageList", TREASURY);
//		children.setParent(parent);
//		parent.getChildrens().add(children);
//		children = new Menu(702L, "treasuryListHang", "treasury/treasuryList", TREASURY);
//		children.setParent(parent);
//		parent.getChildrens().add(children);
//		children = new Menu(703L, "treasuryListFail", "treasury/treasuryList", TREASURY);
//		children.setParent(parent);
//		parent.getChildrens().add(children);
//		children = new Menu(704L, "treasuryListPerOfficer", "treasury/treasuryList", TREASURY);
//		children.setParent(parent);
//		parent.getChildrens().add(children);
//		children = new Menu(705L, "treasuryListDaily", "treasury/treasuryList", TREASURY);
//		children.setParent(parent);
//		parent.getChildrens().add(children);
		superuserMenu.add(parent);
		treasuryMenu.add(parent);

	}

	public List<Menu> getSuperuserMenu() {
		return superuserMenu;
	}
	
	public List<Menu> getDayAdminMenu() {
		return dayAdminMenu;
	}
	
	public List<Menu> getNightAdminMenu() {
		return nightAdminMenu;
	}

	public List<Menu> getTreasuryMenu() {
		return treasuryMenu;
	}

	public List<Menu> getAdminMenu() {
		return adminMenu;
	}
}
