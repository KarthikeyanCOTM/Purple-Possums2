/*URL
http://localhost:8081/tbagproj/gameView
*/
package edu.ycp.cs320.tbagproj.servlet;

import java.io.IOException;

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
		
		message = controller.gameRun(command);

		req.setAttribute("message", message);
		// Forward to view to render the result HTML document
		req.getRequestDispatcher("/_view/GameView.jsp").forward(req, resp);
	}


}
