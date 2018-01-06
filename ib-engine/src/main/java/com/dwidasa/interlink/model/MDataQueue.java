package com.dwidasa.interlink.model;

/**
 * @author prayugo
 */
public class MDataQueue {

	private Long id;
	private String code;
	private String library;
	private String system;
	private String username;
	private String password;
	private String queueReceiver;
	private String queueSender;
	private String description;
	
	public void dump() {

		System.out.println( "id : " + id );
		System.out.println( "code : " + code );
		System.out.println( "library : " + library );
		System.out.println( "system : " + system );
		System.out.println( "username : " + username );
		System.out.println( "password : " + password );
		System.out.println( "queueReceiver : " + queueReceiver );
		System.out.println( "queueSender : " + queueSender );
		System.out.println( "description : " + description );
		System.out.println();
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLibrary() {
		return library;
	}
	
	public void setLibrary(String library) {
		this.library = library;
	}
	
	public String getSystem() {
		return system;
	}
	
	public void setSystem(String system) {
		this.system = system;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getQueueReceiver() {
		return queueReceiver;
	}
	
	public void setQueueReceiver(String queueReceiver) {
		this.queueReceiver = queueReceiver;
	}
	
	public String getQueueSender() {
		return queueSender;
	}
	
	public void setQueueSender(String queueSender) {
		this.queueSender = queueSender;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
