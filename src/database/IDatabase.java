package database;

import java.util.List;
import java.util.ArrayList;

import edu.ycp.cs320.tbagproj.model.*;

public interface IDatabase {
	public String saveGame(String key, Player player, List<Room> list);
	public String loadGame(String key, Player player, ArrayList<Room> roomList);
	public void deleteGame();

}
