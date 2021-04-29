/*URL
http://localhost:8081/tbagproj/gameView
*/
package edu.ycp.cs320.tbagproj.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ycp.cs320.tbagproj.model.Game;
import edu.ycp.cs320.tbagproj.controller.*;

public class GameServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private String command = null;
	private String message = null;
	private String previous = "";
	private double health = 0;
	private Game model = new Game();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		System.out.println("Game Servlet: doGet");	
		
		// call JSP to generate empty form
		req.getRequestDispatcher("/_view/GameView.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		System.out.println("Game Servlet: doPost");
		
		GameController controller = new GameController();
		
		controller.setModel(model);
		
		command = req.getParameter("command");
		
		if (message != null) {
			previous += message + "  \n";
			previous = nl2br(previous);
			
		}
		message = controller.gameRun(command);
		
		health = controller.getModel().getPlayer().getCurHealth();

		req.setAttribute("message", message);
		req.setAttribute("health", health);
		req.setAttribute("previous", previous);
		// Forward to view to render the result HTML document
		req.getRequestDispatcher("/_view/GameView.jsp").forward(req, resp);
	}
	
	//https://codingexplained.com/coding/java/replace-newline-characters-java-jstl
	public static String nl2br(String input) {
        Scanner scanner = new Scanner(input);
        List lines = new ArrayList<>();

        do {
            lines.add(scanner.nextLine());
        } while (scanner.hasNextLine());

        return String.join("<br />", lines);
    }


}
