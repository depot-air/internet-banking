package com.dwidasa.engine;

/**
 * Created by IntelliJ IDEA.
 * User: Ibaihaqi
 * Date: 6/16/12
 */
public class ExtendedProperty {
    private String serverType;
    private String defaultTerminalId;
    private String defaultMerchantType;
    private boolean migration;
    
    public ExtendedProperty() {
		super();
	}

	public ExtendedProperty(String serverType, String defaultTerminalId, String defaultMerchantType, boolean migration) {
		super();
		this.serverType = serverType;
		this.defaultTerminalId = defaultTerminalId;
		this.defaultMerchantType = defaultMerchantType;
		this.migration = migration;
	}

	public String getServerType() {
        return serverType;
    }

    public void setServerType(String serverType) {
        this.serverType = serverType;
    }

	public String getDefaultTerminalId() {
		return defaultTerminalId;
	}

	public void setDefaultTerminalId(String terminalIdDefault) {
		this.defaultTerminalId = terminalIdDefault;
	}

	public String getDefaultMerchantType() {
		return defaultMerchantType;
	}

	public void setDefaultMerchantType(String defaultMerchantType) {
		this.defaultMerchantType = defaultMerchantType;
	}

	public boolean isMigration() {
		return migration;
	}

	public void setMigration(boolean migration) {
		this.migration = migration;
	}

	
}
