package com.dwidasa.engine.service.notif;

public interface PushAdrManager {
	void doPush(String pushId, String payload, String title);
}
