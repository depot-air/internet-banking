package com.dwidasa.engine.service.notif;

public interface PushBbManager {
	void doPush(String pushId, String payload, String title);
}
