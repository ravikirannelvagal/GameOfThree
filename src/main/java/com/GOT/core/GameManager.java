package com.GOT.core;

import java.util.LinkedList;

public class GameManager {
		
		private LinkedList<Integer> list = new LinkedList<>();
		private String [] playerNames=new String[GameOptions.LOG_FOR_NO_OF_PLAYERS];
		private int capacity = GameOptions.LIST_CAPACITY;
		private int playerTurn = GameOptions.PLAYER_ONE;
		private int playerNextTurn = GameOptions.PLAYER_TWO;
		private boolean gameStarted=false;
		private volatile boolean gameTerminated=false;
		private int diceVal;
		

		public boolean rollAndStore(GamePlayer player) throws InterruptedException{
			if(gameTerminated)
				return true;
		outer:	while(!gameTerminated){
				synchronized(this){
					if(gameStarted){
						int mod=0;
						while(list.size() == 0){
							System.out.println(player.playerTurn+" waiting for num");
							if(gameTerminated){
								System.out.println("now breaking off");
								break outer;
							}
							wait();
							
						}
							
						
						player.appendLog(playerTurn,playerNames[playerTurn]+":\n\tReading Dice Value");
						diceVal = list.removeFirst();
						
						player.appendLog(playerTurn,"\tDice value was "+diceVal);
						mod=calculateMod(diceVal);
						
						player.appendLog(playerTurn,"\tDice value needs to be modified by "+mod);
						diceVal= diceVal+mod;
						
						player.appendLog(playerTurn,"\tNew Dice value is "+diceVal);
						
						player.appendLog(playerTurn,"\tGenerating new Dice Value from "+diceVal);
						diceVal=diceVal/GameOptions.LOGICAL_DIV;
						if(diceVal == GameOptions.TERMINATE_VALUE){
							
							player.appendLog(playerTurn,playerNames[playerTurn]+" has reached Dice value of "+diceVal);
							player.appendLog(playerNextTurn,playerNames[playerTurn]+" has reached Dice value of "+diceVal);
							
							player.appendLog(playerTurn,"\n");
							player.appendLog(playerNextTurn,"\n");
							
							player.appendLog(playerTurn,"*****************************************************************");
							player.appendLog(playerNextTurn,"*****************************************************************");
							
							player.appendLog(playerTurn,playerNames[playerTurn]+" WINS!!");
							player.appendLog(playerNextTurn,playerNames[playerTurn]+" Wins!! You LOSE");
							player.appendLog(playerTurn,"*****************************************************************");
							player.appendLog(playerNextTurn,"*****************************************************************");
							player.appendLog(playerTurn,"\n");
							player.appendLog(playerNextTurn,"\n");
							gameTerminated=true;
							break;
						}
						
						player.appendLog(playerTurn,"\tSending Dice Value "+diceVal+" to other player.");
					}
					while(list.size() == capacity){
						System.out.println(player.playerTurn+" waiting for reads");
						wait();
					}
						
					
					player.appendLog(playerTurn,playerNames[playerTurn]+":\n\t Rolled "+diceVal+" on the dice");
					player.appendLog(playerTurn,playerNames[playerNextTurn]+" to read the value");
					list.add(diceVal);
					gameStarted=true;
					if(playerTurn == GameOptions.PLAYER_ONE){
						playerTurn=GameOptions.PLAYER_TWO;
						playerNextTurn=GameOptions.PLAYER_ONE;
					}else{
						playerTurn=GameOptions.PLAYER_ONE;
						playerNextTurn=GameOptions.PLAYER_TWO;
					}
					notify();
					Thread.sleep(GameOptions.SLEEP_BETWEEN_DICE_ROLLS);
				}
			}
			GamePlayer.setGameAlive(false);
			//System.exit(0);
			//Thread.currentThread().interrupt();
			return true;
			
			//throw new InterruptedException();
		}
			
		public String[] getPlayerNames() {
			return playerNames;
		}

		public void setPlayerNames(String[] playerNames) {
			this.playerNames = playerNames;
		}

		public int getDiceVal() {
			return diceVal;
		}

		public void setDiceVal(int diceVal) {
			this.diceVal = diceVal;
		}

		
		private int calculateMod(int passVal){
			int switchVal = passVal%GameOptions.LOGICAL_DIV;
			int retVal = GameOptions.DO_NOTHING;
			switch (switchVal) {
			case GameOptions.MOD_ONE:
				retVal=GameOptions.REM_ONE;
				break;
			case GameOptions.MOD_TWO:
				retVal=GameOptions.ADD_ONE;
				break;
			default:
				break;
			}
			
			return retVal;
		}
	}