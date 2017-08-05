package com.GOT.utils;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import javax.ws.rs.core.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.GOT.core.GameRunner;

@RestController
public class StartGOT {

	private String typeOfGame;
	GameRunner runner = new GameRunner();
	
	@RequestMapping(value="/init")
	@Produces(MediaType.TEXT_HTML)
	public String init(){
		String result = "<!DOCTYPE html><html><head><title>Game of Three!</title></head><body><h1>Welcome to the Game of Three!</h1>"
				+ "<h2>Please choose game type below</h2>"
				+ "<form action=\"/gamestart\" method=\"POST\">"
						+ "<input type=\"radio\" name=\"gameType\" value=\"computer\"/>Computer<br /><input type=\"radio\" name=\"gameType\" value=\"user\"/>User"
						+ "<br /><input type=\"submit\" value=\"Submit\"/></form></body></html>";
		return result;
	}
	
	@RequestMapping(value="/gamestart")
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String newGame(@FormParam("gameType") String gameType,@Context HttpServletResponse servletResponse) throws IOException{
		if(null == gameType){
			typeOfGame="user";
		}else{
			typeOfGame=gameType;
		}
		if(typeOfGame.equals("user")){
			return "<!DOCTYPE html><html><head><title>Game of Three!</title></head><body>"
					+ "<h1>Game of Three!</h1><h2>Please enter the details below</h2>"
					+ "<form action=\"/startuserplay\" method=\"POST\">"
					+ "User Name: <input type=\"text\" name=\"uName\"/><br />"
					+ "User Value: <input type=\"text\" name=\"uNum\"/><br />"
					+ "<input type=\"submit\" value=\"Submit\"/></form><br />"
					+ "Logs for Player 1: <a target=\"_blank\" href='/player1'>Player 1</a><br />"
					+ "Logs for Player 2: <a target=\"_blank\" href='/player2'>Player 2</a><br />"
					+ "Start new game <a href='/init'>here</a></body></html>";
		}else{
			return "You can check the game details here once started\n"
						+ "<form action=\"/startAutoPlay\" method=\"POST\">"
						+ "<input type=\"submit\" value=\"Start Game!\"><br />"
						+ "Logs for Player 1: <a target=\"_blank\" href='/player1'>Player 1</a><br />"
						+ "Logs for Player 2: <a target=\"_blank\" href='/player2'>Player 2</a><br />"
					+ "Start new game <a href='/init'>here</a></body></html>";
		}
	}
	
	
	@RequestMapping(value="/startAutoPlay")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String start(){
		return startAutoPlay();
	}
	public String startAutoPlay(){	
		String ret="";
		try{
			runner.runner(2, "", 0);
			System.out.println("r1");
			ret = "<br /> <br />End of game<br />"
					+ "Logs for Player 1: <a target=\"_blank\" href='/player1'>Player 1</a><br />"
					+ "Logs for Player 2: <a target=\"_blank\" href='/player2'>Player 2</a><br />"
					+ "Start game again<a href='/init'> here</a>";
		}catch(InterruptedException ex){
			System.out.println("returned 2");
			ret = "<br /> <br />End of game<br />"
					+ "Logs for Player 1: <a target=\"_blank\" href='/player1'>Player 1</a><br />"
					+ "Logs for Player 2: <a target=\"_blank\" href='/player2'>Player 2</a><br />"
					+ "Start game again<a href='/init'> here</a>";
		}
		return ret;
	}
	
	@RequestMapping(value="/startuserplay")
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String userName(@FormParam("uName")String uName, @FormParam("uNum")String uNum,@Context HttpServletResponse servletResponse) throws IOException{
		String result="";
		try{
			int userVal=Integer.parseInt(uNum);
			if(userVal <2){
				result = "<!DOCTYPE html><html><head><title>Game of Three!</title></head><body>"
						+ "<h1>Number should be more than 1 for the game to start</h1>"
						+ "<h2>Please enter the number again</h2>"
						+ "<br /><a href='/gamestart'>/gamestart</a></body></html>";
				return result;
			}
				
			try{
				runner.runner(1, uName, userVal);
				System.out.println("r2");
				result = "<br /> <br />End of game<br />"
						+ "Logs for Player 1: <a target=\"_blank\" href='/player1'>Player 1</a><br />"
						+ "Logs for Player 2: <a target=\"_blank\" href='/player2'>Player 2</a><br />"
						+ "Start game again<a href='/init'> here</a>";
			}catch(InterruptedException ex){
				System.out.println("Returned 2");
				result = "<br /> <br />End of game<br />"
						+ "Logs for Player 1: <a target=\"_blank\" href='/player1'>Player 1</a><br />"
						+ "Logs for Player 2: <a target=\"_blank\" href='/player2'>Player 2</a><br />"
						+ "Start game again<a href='/init'> here</a>";
			}
		}catch(NumberFormatException e){
			result = "<!DOCTYPE html><html><head><title>Game of Three!</title></head><body>"
					+ "<h1>Incorrect number entered</h1>"
					+ "<h2>Please enter the number again</h2>"
					+ "<br /><a href='/gamestart'>/gamestart</a></body></html>";
		}
		return result;
	}
	
	@RequestMapping(value="/player1")
	@Produces(MediaType.TEXT_HTML)
	public String player1Logs(){
		StringBuffer res= new StringBuffer();
		List<String> resList = runner.getPlayer1Logs();
		res.append("<!DOCTYPE html><html><head><title>Game of Three!</title></head><body>"
					+ "<h1>Game of Three!</h1>"
					+ "<br /><h4>Player 1's game logs can be seen below</h4>");
		for(String s:resList){
			res.append(s);
			res.append("<br />");
		}
		res.append("<br /><br />");
		res.append("Start game again ");
		res.append("<a href='/init'>here</a>");
		res.append("</body></html>");
		return res.toString();
	} 
	
	@RequestMapping(value="/player2")
	@Produces(MediaType.TEXT_HTML)
	public String player2Logs(){
		StringBuffer res= new StringBuffer();
		List<String> resList = runner.getPlayer2Logs();
		res.append("<!DOCTYPE html><html><head><title>Game of Three!</title></head><body>"
				+ "<h1>Game of Three!</h1>"
				+ "<br /><h4>Player 2's game logs can be seen below</h4>");
		for(String s:resList){
			res.append(s);
			res.append("<br />");
		}
		res.append("<br /><br />");
		res.append("Startgame  again ");
		res.append("<a href='/init'>here</a>");
		res.append("</body></html>");
		return res.toString();
	}
}
