package database;

import java.util.List;

import edu.ycp.cs320.tbagproj.model.*;

public interface IDatabase {
	public String saveGame(String key, Player player, List<Room> list);
	public String loadGame(String key, Game game);
}
