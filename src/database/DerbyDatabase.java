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
	
	private static final int MAX_ATTEMPTS = 10;
	
	private interface Transaction<ResultType> {
		public ResultType execute(Connection conn) throws SQLException;
	}
	
	public<ResultType> ResultType executeTransaction(Transaction<ResultType> txn) {
		try {
			return doExecuteTransaction(txn);
		} catch (SQLException e) {
			throw new PersistenceException("Transaction failed", e);
		}
	}
	
	public<ResultType> ResultType doExecuteTransaction(Transaction<ResultType> txn) throws SQLException {
		Connection conn = connect();
		
		try {
			int numAttempts = 0;
			boolean success = false;
			ResultType result = null;
			
			while (!success && numAttempts < MAX_ATTEMPTS) {
				try {
					result = txn.execute(conn);
					conn.commit();
					success = true;
				} catch (SQLException e) {
					if (e.getSQLState() != null && e.getSQLState().equals("41000")) {
						// Deadlock: retry (unless max retry count has been reached)
						numAttempts++;
					} else {
						// Some other kind of SQLException
						throw e;
					}
				}
			}
			
			if (!success) {
				throw new SQLException("Transaction failed (too many retries)");
			}
			
			// Success!
			return result;
		} finally {
			DBUtil.closeQuietly(conn);
		}
	}
	
	private Connection connect() throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:derby:C:/CS320-2019-LibraryExample-DB/library.db;create=true");
		
		conn.setAutoCommit(false);
		
		return conn;
	}
	
	public String saveGame(String key, Player player, Map map) {
		return "Game saved successfully with key: " + key;
	}
	
	public String loadGame(String key) {
		return "Game loaded successfully";
	}
	
	public void createTables() {
		executeTransaction(new Transaction<Boolean>() {
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement gameStmt = null;
				PreparedStatement playerStmt = null;
				PreparedStatement itemStmt = null;
				PreparedStatement inventoryStmt = null;
				PreparedStatement equipmentStmt = null;
				PreparedStatement npcStmt = null;
				PreparedStatement roomStmt = null;
				PreparedStatement connectionsStmt = null;
				
				try {
					gameStmt = conn.prepareStatement(
							"create table games (" +
									" 	game_key String prmary key " +
									" 	player_id integer constraint player_id references players," +
									" 	room_id integer constraint room_id references rooms "
					);
					gameStmt.executeUpdate();
					
					playerStmt = conn.prepareStatement(
							"create table players (" +
									"	player_id integer primary key " +
									"		generated always as identity (start with 0, increment by 1), " +
									"	name varchar(20)," +
									"	health integer()," +
									"	armor integer()," +
									"	damage integer()," +
									"	inventory_id integer constraint inventory_id references inventory, " +
									"	equipment_id integer constraint equipment_id references equipment" +
									")"
					);
					playerStmt.executeUpdate();
					
					itemStmt = conn.prepareStatement(
							"create table item (" +
									"	item_id integer primary key " +
									"		generated always as identity (start with 0, increment by 1), " +
									"	name varchar(40)," +
									"	damage integer()," +
									"	armor integer()," +
									"	healing integer()," +
									"	isUsable boolean()," +
									"	inventory_id integer constraint inventory_id references inventory, " +
									"	equipment_id integer constarint equipment_id references equipment" +
									")"
					);
					itemStmt.executeUpdate();
					
					inventoryStmt = conn.prepareStatement(
							"create table inventory (" +
									"	inventory_id integer primary key " +
									"		generated always as identity (start with 0, increment by 1), " +
									"	item_id integer constraint item_id references item, " +
									"	gold integer()" +
									")"
					);
					inventoryStmt.executeUpdate();
					
					equipmentStmt = conn.prepareStatement(
							"create table equipment (" +
									"	equipment_id integer primary key " +
									"		generated always as identity (start with 0, increment by 1), " +
									"	item_id integer constraint item_id references item, " +
									"	gold integer()" +
									")"
					);
					equipmentStmt.executeUpdate();
					
					npcStmt = conn.prepareStatement(
							"create table npcs (" +
									"	npc_id integer primary key " +
									"		generated always as identity (start with 0, increment by 1), " +
									"	name varchar(40)," +
									"	health integer()," +
									"	armor integer()," +
									"	damage integer()," +
									"	invnetory_id integer constraint inventory_id references inventory, " +
									"	equipment_id integer constraint equipment_id references equipment, " +
									"	room_id integer constraint room_id references rooms" +
									")"
					);
					npcStmt.executeUpdate();
					
					roomStmt = conn.prepareStatement(
							"create table rooms (" +
									"	room_id integer primary key " +
									"		generated always as identity (start with 0, increment by 1), " +
									"	name varchar(40)," +
									"	inventory_id integer constraint inventory_id references inventory, " +
									"	npc_id integer constraint npc_id references npcs, " +
									"	description varchar(100)" +
									")"
					);
					roomStmt.executeUpdate();
					
					connectionsStmt = conn.prepareStatement(
							"create table connections (" +
									"	connection_id integer primary key " +
									"		generated always as identity (start with 0, increment by 1), " +
									"	room_id integer constraint room_id references rooms, " +
									"	direction_id integer()" +
									")"
					);
					connectionsStmt.executeUpdate();

					return true;
				}finally {
					DBUtil.closeQuietly(gameStmt);
					DBUtil.closeQuietly(playerStmt);
					DBUtil.closeQuietly(itemStmt);
					DBUtil.closeQuietly(inventoryStmt);
					DBUtil.closeQuietly(equipmentStmt);
					DBUtil.closeQuietly(npcStmt);
					DBUtil.closeQuietly(roomStmt);
					DBUtil.closeQuietly(connectionsStmt);
				}
			}
		});
	}
}
