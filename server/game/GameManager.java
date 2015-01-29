package com.lux.trump.server.game;

import com.lux.trump.shared.GameAction;

public abstract class GameManager extends Thread {
	abstract void dealWithAction(GameAction action);
}
