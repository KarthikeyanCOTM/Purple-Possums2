package database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ycp.cs320.tbagproj.model.*;

public class DerbyDatabase implements IDatabase {
	
	private interface Transaction<ResultType> {
		public ResultType execute(Connection conn) throws SQLException;
	}
	
	public String saveGame(String key, Player player, Map map) {
		return key;
	}
	
	public String loadGame(String key) {
		return "Game loaded successfully";
	}
	
	public void createTables() {
		executeTransaction(new Transaction<Boolean>() {
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				
				try {
					stmt1 = conn.prepareStatment(
						"create table games (" +
						" 	game_key String prmary key " +
						" 	player int(1)," +
						" 	room "
					);
				}finally {
					
				}
			}
		}
	}
}
