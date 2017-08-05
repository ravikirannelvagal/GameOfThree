package com.GOT.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GamePlayer implements Runnable{

	private static GameManager gameMgr;
	private static HashMap<Integer,List<String>> playerLogs=new HashMap<>();
	private static volatile boolean isGameAlive=true;
	int playerTurn;
	
	public GamePlayer(int playerTurn){
		this.playerTurn=playerTurn;
		playerLogs.put(playerTurn, new ArrayList<String>());
	}
	
	public static GameManager getGameMgr() {
		return gameMgr;
	}

	public static void setGameMgr(GameManager gameMgr) {
		GamePlayer.gameMgr = gameMgr;
	}

	public static boolean isGameAlive() {
		return isGameAlive;
	}

	public static void setGameAlive(boolean isGameAlive) {
		GamePlayer.isGameAlive = isGameAlive;
	}
	
	public void appendLog(int playerTurn,String log){
		List<String> plStringList = playerLogs.get(playerTurn);
		plStringList.add(log);
		playerLogs.put(playerTurn, plStringList);
	}
	
	public List<String> getPlayerLogs(int playerTurn){
		return playerLogs.get(playerTurn);
	}
	
	@Override
	public void run(){
		try {
			System.out.println("started run for "+playerTurn);
			boolean ret=gameMgr.rollAndStore(this);
			System.out.println(ret+" for "+playerTurn);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
