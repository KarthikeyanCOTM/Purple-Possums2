package database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

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
		Connection conn = DriverManager.getConnection("jdbc:derby:C:/Purple-Possums-Library-DB/library.db;create=true");
		
		conn.setAutoCommit(false);
		
		return conn;
	}
	
	public String saveGame(String key, Player player, Map map) {
		return "Game saved successfully with key: " + key;
	}
	
	public String loadGame(String key) {
		/*executeTransaction(new Transaction<String>() {
		public String execute(Connection conn) throws SQLException {
	
		ResultSet resultSet1 = null;
		PreparedStatement gameStmt = null;
		try {
			gameStmt = conn.prepareStatement(
				"select game_id from games " +
				" where game_key = ?"
			);
			gameStmt.setString(1, key);
			
			resultSet1 = gameStmt.executeQuery();
		
		} finally {
			DBUtil.closeQuietly(gameStmt);
			
		}
		return key;
		};
		});*/
		return "Game loaded successfully";
	}
	
	private void loadRoom(Room room, ResultSet resultSet, int index) throws SQLException {
		room.setNPCs((ArrayList<NPC>) resultSet.getArray(3));
		room.setName(resultSet.getString(1));
		room.setInventory((Inventory) resultSet.getObject(2));
		room.setDescription(resultSet.getString(4));
		//room.setConnections(resultSet.getString(5), null);
	}
	
	public void createTables() {
		executeTransaction(new Transaction<Boolean>() {
			@Override
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
						"	game_id integer primary key " +
						"		generated always as identity (start with 0, increment by 1), " +
						"	game_key varchar(40)" +
						")"
					);
					gameStmt.executeUpdate();
					
					System.out.println("Games table created");
					
					inventoryStmt = conn.prepareStatement(
							"create table inventory (" +
									"	inventory_id integer primary key " +
									"		generated always as identity (start with 0, increment by 1), " +
									"	gold integer" +
									")"
					);
					inventoryStmt.executeUpdate();
					
					System.out.println("Inventory table created");
					
					equipmentStmt = conn.prepareStatement(
							"create table equipment (" +
									"	equipment_id integer primary key " +
									"		generated always as identity (start with 0, increment by 1), " +
									"	gold integer" +
									")"
					);
					equipmentStmt.executeUpdate();
					
					System.out.println("Equipment table created");
					
					itemStmt = conn.prepareStatement(
							"create table item (" +
									"	item_id integer primary key " +
									"		generated always as identity (start with 0, increment by 1), " +
									"	name varchar(40)," +
									"	damage double," +
									"	armor double," +
									"	healing integer," +
									"	isUsable boolean," +
									"	inventory_id integer constraint inventory_id references inventory, " +
									"	equipment_id integer constraint equipment_id references equipment" +
									")"
					);
					itemStmt.executeUpdate();
					
					System.out.println("Item table created");
					
					roomStmt = conn.prepareStatement(
							"create table rooms (" +
									"	room_id integer primary key " +
									"		generated always as identity (start with 0, increment by 1), " +
									"	name varchar(40)," +
									"	inventory_id integer references inventory, " +
									"	description varchar(400)," +
									"	game_key varchar(40)" +
									")"
					);
					roomStmt.executeUpdate();
					
					System.out.println("Rooms table created");
					
					playerStmt = conn.prepareStatement(
							"create table players (" +
									"	player_id integer primary key " +
									"		generated always as identity (start with 1, increment by 1), " +
									"	name varchar(20)," +
									"	health double," +
									"	armor double," +
									"	damage double," +
									"	inventory_id integer references inventory, " +
									"	equipment_id integer references equipment, " +
									"	game_id integer references games" +
									")"
					);
					playerStmt.executeUpdate();
					
					System.out.println("Players table created");
					
					npcStmt = conn.prepareStatement(
							"create table npcs (" +
									"	npc_id integer primary key " +
									"		generated always as identity (start with 0, increment by 1), " +
									"	name varchar(40)," +
									"	health double," +
									"	armor double," +
									"	damage double," +
									"	inventory_id integer references inventory, " +
									"	equipment_id integer references equipment, " +
									"	isalive boolean," +
									"	room_id integer references rooms" +
									")"
					);
					npcStmt.executeUpdate();
					
					System.out.println("NPCs table created");
					
					connectionsStmt = conn.prepareStatement(
							"create table connections (" +
									"	connection_id integer primary key " +
									"		generated always as identity (start with 0, increment by 1), " +
									"	room_id integer references rooms, " +
									"	room_id2 integer constraint room_id references rooms, " +
									"	direction_id integer" +
									")"
					);
					connectionsStmt.executeUpdate();
					
					System.out.println("Connections table created");

					return true;
				}finally {
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
	
	public void loadInitialData() {
		executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				List<Room> roomList;
				Player player;
				HashMap<Integer, Inventory> inventoryMap;
				HashMap<Integer, Inventory> equipmentMap;
				ArrayList<String> games;
				ArrayList<Item> itemInventoryList;
				ArrayList<Item> itemEquipmentList;
				ArrayList<NPC> npcList;
				
				try {
					roomList = InitialData.getRooms();
					player = InitialData.getPlayer();
					inventoryMap = InitialData.getInventorys();
					equipmentMap = InitialData.getEquipments();
					itemInventoryList = InitialData.getAllItems(true);
					itemEquipmentList = InitialData.getAllItems(false);
					npcList = InitialData.getAllNPCs();
					games = InitialData.getGames();
				}catch (IOException e) {
					throw new SQLException("Couldn't read initial data", e);
				}
				
				PreparedStatement insertRooms = null;
				PreparedStatement insertPlayer = null;
				PreparedStatement insertInventory = null;
				PreparedStatement insertItem = null;
				PreparedStatement insertEquipment = null;
				PreparedStatement insertNPCs = null;
				PreparedStatement insertConnections = null;
				PreparedStatement insertGames = null;
				
				try {
					insertInventory = conn.prepareStatement("insert into inventory (gold) values (?)");
					for (int i = 1; i < inventoryMap.size(); i++) {
						insertInventory.setInt(1, inventoryMap.get(i).getGold());
						insertInventory.addBatch();
					}
					insertInventory.executeBatch();
					
					System.out.println("Inventory table populated");
					
					insertEquipment = conn.prepareStatement("insert into equipment (gold) values(?)");
					for (int i = 1; i < equipmentMap.size(); i++) {
						insertEquipment.setInt(1, equipmentMap.get(i).getGold());
						insertEquipment.addBatch();
					}
					insertEquipment.executeBatch();
					
					System.out.println("Equipment table populated");
					
					insertItem = conn.prepareStatement("insert into item (name, damage, armor, healing, isUsable, inventory_id, equipment_id) values (?, ?, ?, ?, ?, ?, ?)");
					for (Item item : itemInventoryList) {
						insertItem.setString(1, item.getName());
						insertItem.setDouble(2, item.getDamage());
						insertItem.setDouble(3, item.getArmour());
						insertItem.setDouble(4, item.getHealing());
						insertItem.setBoolean(5, item.getIsUsable());
						//insertItem.setInt(6, item.getInventory_ID());
						insertItem.setInt(7, 0);
						insertItem.addBatch();
					}
					for (Item item : itemEquipmentList) {
						insertItem.setString(1, item.getName());
						insertItem.setDouble(2, item.getDamage());
						insertItem.setDouble(3, item.getArmour());
						insertItem.setDouble(4, item.getHealing());
						insertItem.setBoolean(5, item.getIsUsable());
						insertItem.setInt(6, 0);
						insertItem.setInt(7, item.getEquipment_ID());
						insertItem.addBatch();
					}
					insertItem.executeBatch();
					
					System.out.println("Item table populated");
					
					insertRooms = conn.prepareStatement("insert into rooms (name, inventory_id, description, game_key) values (?, ?, ?, ?)");
					for (Room room : roomList) {
						insertRooms.setString(1, room.getName());
						insertRooms.setInt(2, room.getInventory_ID());
						insertRooms.setString(3, room.getDescription());
						insertRooms.setString(4, "initial");
						insertRooms.addBatch();
					}
					insertRooms.executeBatch();
					
					System.out.println("Rooms table populated");
					
					insertPlayer = conn.prepareStatement("insert into players (name, health, armor, damage, inventory_id, equipment_id, game_id) values (?, ?, ?, ?, ?, ?, ?)");
					insertPlayer.setString(1, player.getName());
					insertPlayer.setDouble(2, player.getCurHealth());
					insertPlayer.setDouble(3, player.getDefence());
					insertPlayer.setDouble(4, player.getTotalDamage());
					if (player.getInventory_ID() != 0) {
						insertPlayer.setInt(5, player.getInventory_ID());
					}else {
						insertPlayer.setInt(5, 0);
					}
					if (player.getEquipment_ID() != 0) {
						insertPlayer.setInt(6, player.getEquipment_ID());
					}else {
						insertPlayer.setInt(6, 0);
					}
					//insertPlayer.setInt(7, 0);
					insertPlayer.addBatch();
					insertPlayer.executeBatch();
					
					System.out.println("Players table populated");
					
					insertNPCs = conn.prepareStatement("insert into npcs (name, health, armor, damage, inventory_id, equipment_id, isalive, room_id) values (?, ?, ?, ?, ?, ?, ?, ?)");
					for (NPC npc : npcList) {
						insertNPCs.setString(1, npc.getName());
						insertNPCs.setDouble(2, npc.getCurHealth());
						insertNPCs.setDouble(3, npc.getDefence());
						insertNPCs.setDouble(4, npc.getTotalDamage());
						if (npc.getInventory_ID() != 0) {
							insertNPCs.setInt(5, npc.getInventory_ID());
						}else {
							insertNPCs.setInt(5, 0);
						}
						if (npc.getEquipment_ID() != 0) {
							insertNPCs.setInt(6, npc.getEquipment_ID());
						}else {
							insertNPCs.setInt(6, 0);
						}
						insertNPCs.setBoolean(7, npc.getIsNPCAlive());
						insertNPCs.setInt(8, npc.getRoom_ID());
						insertNPCs.addBatch();
					}
					insertNPCs.executeBatch();
					
					System.out.println("NPCs table populated");
					
					insertConnections = conn.prepareStatement("insert into connections (room_id, room_id, direction_id) values (?, ?, ?)");
					int x = 0;
					while (x < roomList.size()) {
						HashMap<String, Room> connections = roomList.get(x).getConnections();
						Room room = roomList.get(x);
						if (connections.containsKey("north") == true) {
							insertConnections.setInt(1, room.getRoom_ID());
							insertConnections.setInt(2, connections.get("north").getRoom_ID());
							insertConnections.setInt(3, 0);
							insertConnections.addBatch();
						}
						if (connections.containsKey("east") == true) {
							insertConnections.setInt(1, room.getRoom_ID());
							insertConnections.setInt(2, connections.get("east").getRoom_ID());
							insertConnections.setInt(3, 1);
							insertConnections.addBatch();
						}
						if (connections.containsKey("south") == true) {
							insertConnections.setInt(1, room.getRoom_ID());
							insertConnections.setInt(2, connections.get("south").getRoom_ID());
							insertConnections.setInt(3, 2);
							insertConnections.addBatch();
						}
						if (connections.containsKey("west") == true) {
							insertConnections.setInt(1, room.getRoom_ID());
							insertConnections.setInt(2, connections.get("west").getRoom_ID());
							insertConnections.setInt(3, 3);
							insertConnections.addBatch();
						}
						x++;
					}
					insertConnections.executeBatch();
					
					System.out.println("Connections table populated");
					
					insertGames = conn.prepareStatement("insert into games (game_key) values (?)");
					for (int i = 0; i < games.size(); i++) {
						insertGames.setString(1, games.get(i));
						insertGames.addBatch();
					}
					insertGames.executeBatch();
					
					System.out.println("Games table populated");
					
					return true;
				}finally {
					DBUtil.closeQuietly(insertRooms);
					DBUtil.closeQuietly(insertPlayer);
					DBUtil.closeQuietly(insertInventory);
					DBUtil.closeQuietly(insertItem);
					DBUtil.closeQuietly(insertEquipment);
					DBUtil.closeQuietly(insertNPCs);
					DBUtil.closeQuietly(insertConnections);
				}
			}
		});
	}
	
	public static void main(String[] args) throws IOException {
		System.out.println("Creating tables...");
		DerbyDatabase db = new DerbyDatabase();
		
		db.createTables();
		
		System.out.println("Loading initial data...");
		db.loadInitialData();
		
		System.out.println("Library DB successfully initialized!");
	}
}
