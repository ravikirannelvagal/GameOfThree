package com.GOT.core;

import java.util.List;
import java.util.Random;

public class GameRunner{
	
	GamePlayer player1 = new GamePlayer(GameOptions.PLAYER_ONE);
	GamePlayer player2 = new GamePlayer(GameOptions.PLAYER_TWO);
	
	public List<String> getPlayer1Logs(){
		return player1.getPlayerLogs(GameOptions.PLAYER_ONE);
	}
	
	public List<String> getPlayer2Logs(){
		return player2.getPlayerLogs(GameOptions.PLAYER_TWO);
	}
	
	public void runner(int option,String playerName,int userVal) throws InterruptedException{
		
		final GameManager gameMgr = new GameManager();
		String [] playerNames = new String[GameOptions.LOG_FOR_NO_OF_PLAYERS];
		if(option == GameOptions.COMPUTER_PLAY){
			Random random = new Random();
			int randomNum = random.nextInt(GameOptions.MAX_NO_GEN);
			playerNames[GameOptions.PLAYER_ONE] = GameOptions.PLAYER_ONE_NAME;
			playerNames[GameOptions.PLAYER_TWO] = GameOptions.PLAYER_TWO_NAME;
			while(randomNum<GameOptions.MIN_NO_REQD)
				randomNum = random.nextInt(GameOptions.MAX_NO_GEN);
			gameMgr.setDiceVal(randomNum); 
		}else{
			String userName = playerName;
			playerNames[GameOptions.PLAYER_ONE] = userName;
			playerNames[GameOptions.PLAYER_TWO] = GameOptions.COMPUTER_NAME;
			gameMgr.setDiceVal(userVal);
		}
		
		gameMgr.setPlayerNames(playerNames);
		// Create player threads
		GamePlayer.setGameMgr(gameMgr);
		GamePlayer.setGameAlive(true);
		
		Thread t1= new Thread(player1);
		
		Thread t2= new Thread(player2);
		
		// Start both threads
		t1.start();
		t2.start();
		
		// t1 finishes before t2
		
		t1.join();
		t2.join();
		
		System.out.println("try to leave");
		Runtime.getRuntime().addShutdownHook(t1);
		Runtime.getRuntime().addShutdownHook(t2);
		
	}
}