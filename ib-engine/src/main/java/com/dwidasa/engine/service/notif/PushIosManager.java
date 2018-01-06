package com.dwidasa.engine.service.notif;

public interface PushIosManager {
	void doPush(String pushId, String payload, String title);
}
