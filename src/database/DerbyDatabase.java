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
	
	public String updateGame(String key, Player player, List<Room> list) {
		return executeTransaction(new Transaction<String>() {
			public String execute(Connection conn) throws SQLException {
				PreparedStatement itemStmt = null;
				PreparedStatement npcStmt = null;
				PreparedStatement inventoryStmt = null;
				PreparedStatement playerStmt = null;
		
				ArrayList<Item> itemList = new ArrayList<Item>();
				ArrayList<NPC> npcList = new ArrayList<NPC>();
				ArrayList<Inventory> inventoryList = new ArrayList<Inventory>();
		
				//gets all items and npcs
				try {
					itemList.addAll(player.getInventory().getItemList());
				
					for (int i = 0; i < list.size(); i++) {
						itemList.addAll(list.get(i).getInventory().getItemList());
						npcList.addAll(list.get(i).getNPCs());
					}
					for (int i = 0; i < npcList.size(); i++) {
						itemList.addAll(npcList.get(i).getInventory().getItemList());
					}
					//gets item's name where hasMoved = true
					ArrayList<Item> itemUpdateList = new ArrayList<Item>();
					for (int i = 0; i < itemList.size(); i++) {
						if (itemList.get(i).getHasMoved() == true) {
							itemUpdateList.add(itemList.get(i));
						}
					}
		
					itemStmt = conn.prepareStatement(
						"update item set inventory_id = ?, equipment_id = ? where item_id = ?"
					);
					for (int i = 0; i < itemUpdateList.size(); i++) {
						itemStmt.setInt(1, itemUpdateList.get(i).getInventory_ID());
						itemStmt.setInt(2, itemUpdateList.get(i).getEquipment_ID());
						itemStmt.setInt(3, itemUpdateList.get(i).getItem_ID());
						itemStmt.addBatch();
					}
					itemStmt.executeBatch();
					System.out.println("item table updated");
					
					for (int i = 0; i < list.size(); i++) {
						inventoryList.add(list.get(i).getInventory());
					}
		
					inventoryStmt = conn.prepareStatement(
						"update inventory set gold = ? where inventory_id = ?"
					);
					for (int i = 0; i < inventoryList.size(); i++) {
						inventoryStmt.setInt(1, inventoryList.get(i).getGold());
						inventoryStmt.setInt(2, inventoryList.get(i).getInventory_ID());
						inventoryStmt.addBatch();
					}
					inventoryStmt.executeBatch();
					System.out.println("inventory table updated");
		
					playerStmt = conn.prepareStatement(
						"update players set health = ?, armor = ?, damage = ?, room_id = ? where player_id = ?"
					);
					playerStmt.setDouble(1, player.getCurHealth());
					playerStmt.setDouble(2, player.getDefence());
					playerStmt.setDouble(3, player.getTotalDamage());
					playerStmt.setInt(4, player.getRoom_ID());
					playerStmt.setInt(5, player.getPlayer_ID());
		
					playerStmt.executeUpdate();
					System.out.println("player table updated");
		
					npcStmt = conn.prepareStatement(
						"update npcs set health = ?, armor = ?, damage = ?, isalive = ? where npc_id = ?"
					);
					for (int i = 0; i < npcList.size(); i++) {
						npcStmt.setDouble(1, npcList.get(i).getCurHealth());
						npcStmt.setDouble(2, npcList.get(i).getDefence());
						npcStmt.setDouble(3, npcList.get(i).getTotalDamage());
						npcStmt.setBoolean(4, npcList.get(i).getIsNPCAlive());
						npcStmt.setInt(5, npcList.get(i).getNPC_ID());
						npcStmt.addBatch();
					}
					npcStmt.executeBatch();
					System.out.println("npc table updated");
				
					return "update complete";
				}finally {
					DBUtil.closeQuietly(itemStmt);
					DBUtil.closeQuietly(npcStmt);
					DBUtil.closeQuietly(inventoryStmt);
					DBUtil.closeQuietly(playerStmt);
				}
			}
		});
	}
	
	public String saveGame(String key, Player player, List<Room> list) {
		return executeTransaction(new Transaction<String>() {
			public String execute(Connection conn) throws SQLException {
				PreparedStatement gameStmt = null;
				PreparedStatement gameStmt2 = null;
				PreparedStatement gameStmt3 = null;
				PreparedStatement playerStmt = null;
				PreparedStatement roomsStmt = null;
				PreparedStatement roomsStmt2 = null;
				PreparedStatement roomsStmt3 = null;
				PreparedStatement roomsStmt4 = null;
				PreparedStatement itemStmt = null;
				PreparedStatement inventoryStmt = null;
				PreparedStatement inventoryStmt2 = null;
				PreparedStatement inventoryStmt3 = null;
				PreparedStatement inventoryStmt4 = null;
				PreparedStatement inventoryStmt5 = null;
				PreparedStatement equipmentStmt = null;
				PreparedStatement equipmentStmt2 = null;
				PreparedStatement equipmentStmt3 = null;
				PreparedStatement npcStmt = null;
				PreparedStatement connectionsStmt = null;
				
				ResultSet resultSet1 = null;
				ResultSet resultSet2 = null;
				ResultSet resultSet3 = null;
				ResultSet resultSet4 = null;
				ResultSet resultSet5 = null;
				ResultSet resultSet6 = null;
				ResultSet resultSet7 = null;
				ResultSet resultSet8 = null;
				ResultSet resultSet9 = null;
				ResultSet resultSet10 = null;
				ResultSet resultSet11 = null;
				
				Integer game_ID = -1;
				Integer playerInventory_ID = 0;
				Integer playerEquipment_ID = 0;
				Integer playerRoom_ID = -1;
				Integer mainHall_ID = -1;
				Integer closet_ID = -1;
				Integer mainHallInventory_ID = 0;
				Integer closetInventory_ID = 0;
				Integer npcInventory_ID = 0;
				Integer npcEquipment_ID = 0;
				ArrayList<Item> itemList = new ArrayList<Item>();
				ArrayList<NPC> npcList = new ArrayList<NPC>();
				ArrayList<Inventory> inventoryList = new ArrayList<Inventory>();
				
				try {
					gameStmt = conn.prepareStatement(
						"select game_id from games " +
						" where game_key = ?"
					);
					gameStmt.setString(1, key);
					
					resultSet1 = gameStmt.executeQuery();
					
					if (resultSet1.next()) {
						
						//gets all items and npcs
						itemList.addAll(player.getInventory().getItemList());
						for (int i = 0; i < list.size(); i++) {
							itemList.addAll(list.get(i).getInventory().getItemList());
							npcList.addAll(list.get(i).getNPCs());
						}
						for (int i = 0; i < npcList.size(); i++) {
							itemList.addAll(npcList.get(i).getInventory().getItemList());
						}
						//gets item's name where hasMoved = true
						ArrayList<Item> itemUpdateList = new ArrayList<Item>();
						for (int i = 0; i < itemList.size(); i++) {
							if (itemList.get(i).getHasMoved() == true) {
								itemUpdateList.add(itemList.get(i));
							}
						}
						
						itemStmt = conn.prepareStatement(
							"update item set inventory_id = ?, equipment_id = ? where item_id = ?"
						);
						for (int i = 0; i < itemUpdateList.size(); i++) {
							itemStmt.setInt(1, itemUpdateList.get(i).getInventory_ID());
							itemStmt.setInt(2, itemUpdateList.get(i).getEquipment_ID());
							itemStmt.setInt(3, itemUpdateList.get(i).getItem_ID());
							itemStmt.addBatch();
						}
						itemStmt.executeBatch();
						System.out.println("item table updated");
						
						for (int i = 0; i < list.size(); i++) {
							inventoryList.add(list.get(i).getInventory());
						}
						
						inventoryStmt = conn.prepareStatement(
							"update inventory set gold = ? where inventory_id = ?"
						);
						for (int i = 0; i < inventoryList.size(); i++) {
							inventoryStmt.setInt(1, inventoryList.get(i).getGold());
							inventoryStmt.setInt(2, inventoryList.get(i).getInventory_ID());
							inventoryStmt.addBatch();
						}
						inventoryStmt.executeBatch();
						System.out.println("inventory table updated");
						
						playerStmt = conn.prepareStatement(
							"update players set health = ?, armor = ?, damage = ?, room_id = ? where player_id = ?"
						);
						playerStmt.setDouble(1, player.getCurHealth());
						playerStmt.setDouble(2, player.getDefence());
						playerStmt.setDouble(3, player.getTotalDamage());
						playerStmt.setInt(4, player.getRoom_ID());
						playerStmt.setInt(5, player.getPlayer_ID());
						
						playerStmt.executeUpdate();
						System.out.println("player table updated");
						
						npcStmt = conn.prepareStatement(
							"update npcs set health = ?, armor = ?, damage = ?, isalive = ? where npc_id = ?"
						);
						for (int i = 0; i < npcList.size(); i++) {
							npcStmt.setDouble(1, npcList.get(i).getCurHealth());
							npcStmt.setDouble(2, npcList.get(i).getDefence());
							npcStmt.setDouble(3, npcList.get(i).getTotalDamage());
							npcStmt.setBoolean(4, npcList.get(i).getIsNPCAlive());
							npcStmt.setInt(5, npcList.get(i).getNPC_ID());
							npcStmt.addBatch();
						}
						npcStmt.executeBatch();
						System.out.println("npc table updated");
						
					}else {
						gameStmt2 = conn.prepareStatement(
							"insert into games (game_key) " +
							" values(?) "
						);
						gameStmt2.setString(1, key);
						System.out.println("games table updated");
						
						gameStmt2.executeUpdate();
						System.out.println("Games table updated");
						
						gameStmt3 = conn.prepareStatement(
							"select game_id from games " +
							" where game_key = ?"
						);
						gameStmt3.setString(1, key);
						
						resultSet2 = gameStmt.executeQuery();
						if (resultSet2.next()) {
							game_ID = resultSet2.getInt(1);
						}
						
						inventoryStmt = conn.prepareStatement(
							"insert into inventory (gold, owner) " +
							" values(?, ?) "
						);
						
						for (int i = 0; i < 4; i++) {
							if (i < 2) {
								inventoryStmt.setInt(1, list.get(i).getInventory().getGold());
								inventoryStmt.setString(2, list.get(i).getName());
								inventoryStmt.addBatch();
							}else if (i == 3){
								inventoryStmt.setInt(1, player.getInventory().getGold());
								inventoryStmt.setString(2, player.getName());
								inventoryStmt.addBatch();
							}else {
								for (int x = 0; x < list.size(); x++) {
									if (list.get(x).getNPCs() != null) {
										ArrayList<NPC> npc = list.get(x).getNPCs();
										for (int y = 0; y < npc.size(); y++) {
											inventoryStmt.setInt(1, npc.get(y).getInventory().getGold());
											inventoryStmt.setString(2, npc.get(y).getName());
											inventoryStmt.addBatch();
										}
									}
								}
							}
						}
						inventoryStmt.executeBatch();
						System.out.println("Inventory table updated");
						
						inventoryStmt2 = conn.prepareStatement(
							"select inventory_id from inventory" +
							" where owner = ?"
						);
						inventoryStmt2.setString(1, list.get(0).getName());
						
						resultSet3 = inventoryStmt2.executeQuery();
						if (resultSet3.next()) {
							mainHallInventory_ID = resultSet3.getInt(1);
						}
						
						inventoryStmt3 = conn.prepareStatement(
							"select inventory_id from inventory" +
							" where owner = ?"
						);
						inventoryStmt3.setString(1, list.get(1).getName());
						
						resultSet4 = inventoryStmt3.executeQuery();
						if (resultSet4.next()) {
							closetInventory_ID = resultSet4.getInt(1);
						}
						
						inventoryStmt4 = conn.prepareStatement(
							"select inventory_id from inventory" +
							" where owner = ?"
						);
						inventoryStmt4.setString(1, list.get(1).getName());
						
						resultSet5 = inventoryStmt4.executeQuery();
						if (resultSet5.next()) {
							playerInventory_ID = resultSet5.getInt(1);
						}
						
						inventoryStmt5 = conn.prepareStatement(
								"select inventory_id from inventory" +
								" where owner = ?"
						);
						inventoryStmt5.setString(1, list.get(1).getName());
							
						resultSet8 = inventoryStmt5.executeQuery();
						if (resultSet8.next()) {
							npcInventory_ID = resultSet8.getInt(1);
						}
						
						equipmentStmt = conn.prepareStatement(
							"insert into equipment (gold, owner) " +
							" values (?, ?) "
						);
						equipmentStmt.setInt(1, player.getInventory().getGold());
						equipmentStmt.setString(2, player.getName());
						
						equipmentStmt.executeUpdate();
						System.out.println("Equipment tabel updated");
						
						equipmentStmt2 = conn.prepareStatement(
							"select equipment_id from equipment" +
							" where owner = ?"
						);
						equipmentStmt2.setString(1, player.getName());
						
						resultSet6 = equipmentStmt2.executeQuery();
						if (resultSet6.next()) {
							playerEquipment_ID = resultSet4.getInt(1);
						}
						
						equipmentStmt3 = conn.prepareStatement(
							"select equipment_id from equipment" +
							" where owner = ?"
						);
						equipmentStmt3.setString(1, player.getName());
							
						resultSet9 = equipmentStmt3.executeQuery();
						if (resultSet9.next()) {
							playerEquipment_ID = resultSet9.getInt(1);
						}
						
						roomsStmt = conn.prepareStatement(
							"insert into rooms (name, inventory_id, description, game_key)" +
							" values (?, ?, ?, ?) "
						);
						roomsStmt.setString(1, list.get(0).getName());
						roomsStmt.setInt(2, mainHallInventory_ID);
						roomsStmt.setString(3, list.get(0).getDescription());
						roomsStmt.setInt(4, list.get(0).getGame_ID());
						roomsStmt.addBatch();
						
						roomsStmt.setString(1, list.get(1).getName());
						roomsStmt.setInt(2, closetInventory_ID);
						roomsStmt.setString(3, list.get(1).getDescription());
						roomsStmt.setInt(4, list.get(1).getGame_ID());
						roomsStmt.addBatch();
						
						roomsStmt.executeBatch();
						System.out.println("Room tabel updated");
						
						roomsStmt2 = conn.prepareStatement(
							"select room_id from rooms" +
							" where name = ?"
						);
						roomsStmt2.setString(1, player.getRoomName());
						
						resultSet7 = roomsStmt2.executeQuery();
						if (resultSet7.next()) {
							playerRoom_ID = resultSet6.getInt(1);
						}
						
						roomsStmt3 = conn.prepareStatement(
							"select room_id from rooms" +
							" where name = ?"
						);
						roomsStmt3.setString(1, list.get(0).getName());
						
						resultSet10 = roomsStmt3.executeQuery();
						if (resultSet10.next()) {
							list.get(0).setRoom_ID(resultSet10.getInt(1));
						}
						
						roomsStmt4 = conn.prepareStatement(
							"select room_id from rooms" +
							" where name = ?"
						);
						roomsStmt4.setString(1, list.get(1).getName());
							
						resultSet11 = roomsStmt4.executeQuery();
						if (resultSet11.next()) {
							list.get(1).setRoom_ID(resultSet11.getInt(1));
						}
						
						playerStmt = conn.prepareStatement(
							"insert into players (name, health, armor, damage, inventory_id, equipment_id, game_id, room_id) " +
							" values (?, ?, ?, ?, ?, ?, ?, ?) "
						);
						playerStmt.setString(1, player.getName());
						playerStmt.setDouble(2, player.getCurHealth());
						playerStmt.setDouble(3, player.getDefence());
						playerStmt.setDouble(4, player.getTotalDamage());
						playerStmt.setInt(5, playerInventory_ID);
						playerStmt.setInt(6, playerEquipment_ID);
						playerStmt.setInt(7, game_ID);
						playerStmt.setInt(8, playerRoom_ID);
						
						playerStmt.executeUpdate();
						System.out.println("Player tabel updated");
						
						npcStmt = conn.prepareStatement(
							"insert into npcs (name, health, armor, damage, inventory_id, equipment_id, isalive, room_id)" +
							"values (?, ?, ?, ?, ?, ?, ?, ?)"
						);
						for (int i = 0; i < list.size(); i++) {
							if (list.get(i).getNPCs() != null) {
								ArrayList<NPC> npcs = list.get(i).getNPCs();
								for (int x = 0; x < npcs.size(); x++) {
									npcStmt.setString(1, npcs.get(x).getName());
									npcStmt.setDouble(2, npcs.get(x).getCurHealth());
									npcStmt.setDouble(3, npcs.get(x).getDefence());
									npcStmt.setDouble(4, npcs.get(x).getTotalDamage());
									npcStmt.setInt(5, npcInventory_ID);
									npcStmt.setInt(6, npcEquipment_ID);
									npcStmt.setBoolean(7, npcs.get(x).getIsNPCAlive());
									npcStmt.setInt(8, list.get(i).getRoom_ID());
									npcStmt.addBatch();
								}
							}
						}
						
						npcStmt.executeBatch();
						System.out.println("NPC table updated");
						
						connectionsStmt = conn.prepareStatement(
							"insert into connections (room_id, room_id2, direction_id)" +
							" values (?, ?, ?)"
						);
						for (int x = 0; x < list.size(); x++) {
							HashMap<String, Room> connections = list.get(x).getConnections();
							if (connections.containsKey("north") == true) {
								connectionsStmt.setInt(1, list.get(x).getRoom_ID());
								connectionsStmt.setInt(2, connections.get("north").getRoom_ID());
								connectionsStmt.setInt(3, 0);
								connectionsStmt.addBatch();
							}
							if (connections.containsKey("east") == true) {
								connectionsStmt.setInt(1, list.get(x).getRoom_ID());
								connectionsStmt.setInt(2, connections.get("east").getRoom_ID());
								connectionsStmt.setInt(3, 1);
								connectionsStmt.addBatch();
							}
							if (connections.containsKey("south") == true) {
								connectionsStmt.setInt(1, list.get(x).getRoom_ID());
								connectionsStmt.setInt(2, connections.get("south").getRoom_ID());
								connectionsStmt.setInt(3, 2);
								connectionsStmt.addBatch();
							}
							if (connections.containsKey("west") == true) {
								connectionsStmt.setInt(1, list.get(x).getRoom_ID());
								connectionsStmt.setInt(2, connections.get("west").getRoom_ID());
								connectionsStmt.setInt(3, 3);
								connectionsStmt.addBatch();
							}
						}
						connectionsStmt.executeBatch();
						System.out.println("Connections table updated");
					}
				}finally {
					DBUtil.closeQuietly(resultSet1);
					DBUtil.closeQuietly(gameStmt);
					DBUtil.closeQuietly(gameStmt2);
					DBUtil.closeQuietly(gameStmt3);
					DBUtil.closeQuietly(playerStmt);
					DBUtil.closeQuietly(roomsStmt);
					DBUtil.closeQuietly(roomsStmt2);
					DBUtil.closeQuietly(itemStmt);
					DBUtil.closeQuietly(inventoryStmt);
					DBUtil.closeQuietly(inventoryStmt2);
					DBUtil.closeQuietly(inventoryStmt3);
					DBUtil.closeQuietly(inventoryStmt4);
					DBUtil.closeQuietly(inventoryStmt5);
					DBUtil.closeQuietly(equipmentStmt);
					DBUtil.closeQuietly(equipmentStmt2);
					DBUtil.closeQuietly(equipmentStmt3);
					DBUtil.closeQuietly(npcStmt);
					DBUtil.closeQuietly(connectionsStmt);
					DBUtil.closeQuietly(resultSet2);
					DBUtil.closeQuietly(resultSet3);
					DBUtil.closeQuietly(resultSet4);
					DBUtil.closeQuietly(resultSet5);
					DBUtil.closeQuietly(resultSet6);
					DBUtil.closeQuietly(resultSet7);
					DBUtil.closeQuietly(resultSet8);
					DBUtil.closeQuietly(resultSet9);
				}
				
				return "Game saved successfully with key: " + key;
			}
		});
	}
	
	public String loadGame(String key, Game game) {
		return "Game loaded successfully";
	}
	
	private void loadRoom(Room room, ResultSet resultSet, int index) throws SQLException {
		room.setName(resultSet.getString(1));
		room.setInventory((Inventory) resultSet.getObject(2));
		room.setDescription(resultSet.getString(3));
		room.setGame_ID(resultSet.getInt(4));
	}
	
	private void loadPlayer(Player player, ResultSet resultSet, int index) throws SQLException{
		player.setName(resultSet.getString(1));
		player.setHealth(resultSet.getDouble(2));
		player.setDefence(resultSet.getDouble(3));
		player.setTotalDamage(resultSet.getDouble(4));
		player.setInventory_ID(resultSet.getInt(5));
		player.setEquipment_ID(resultSet.getInt(6));
		player.setGame_ID(resultSet.getInt(7));
		player.setRoom_ID(resultSet.getInt(8));
	}
	
	private void loadInventory(Inventory inventory, ResultSet resultSet, int index) throws SQLException{
		inventory.addGold(resultSet.getInt(1));
		inventory.setOwner(resultSet.getString(2));
	}
	
	private void loadItem(Item item, ResultSet resultSet, int index) throws SQLException{
		item.setName(resultSet.getString(1));
		item.setDamage(resultSet.getDouble(2));
		item.setArmour(resultSet.getDouble(3));
		item.setHealing(resultSet.getDouble(4));
		item.setIsUsable(resultSet.getBoolean(5));
		item.setInventory_ID(resultSet.getInt(6));
		item.setEquipment_ID(resultSet.getInt(7));
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
									"	gold integer," +
									"	owner varchar(20)" +
									")"
					);
					inventoryStmt.executeUpdate();
					
					System.out.println("Inventory table created");
					
					equipmentStmt = conn.prepareStatement(
							"create table equipment (" +
									"	equipment_id integer primary key " +
									"		generated always as identity (start with 0, increment by 1), " +
									"	gold integer," +
									"	owner varchar(20)" +
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
									"	inventory_id integer," +
									"	equipment_id integer" +
									")"
					);
					itemStmt.executeUpdate();
					
					System.out.println("Item table created");
					
					roomStmt = conn.prepareStatement(
							"create table rooms (" +
									"	room_id integer primary key " +
									"		generated always as identity (start with 0, increment by 1), " +
									"	name varchar(40)," +
									"	inventory_id integer, " +
									"	description varchar(400)," +
									"	game_key integer" +
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
									"	inventory_id integer, " +
									"	equipment_id integer, " +
									"	game_id integer," +
									"	room_id integer" +
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
									"	inventory_id integer, " +
									"	equipment_id integer, " +
									"	isalive boolean," +
									"	room_id integer" +
									")"
					);
					npcStmt.executeUpdate();
					
					System.out.println("NPCs table created");
					
					connectionsStmt = conn.prepareStatement(
							"create table connections (" +
									"	connection_id integer primary key " +
									"		generated always as identity (start with 0, increment by 1), " +
									"	room_id integer, " +
									"	room_id2 integer, " +
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
					insertInventory = conn.prepareStatement("insert into inventory (gold, owner) values (?, ?)");
					for (int i = 1; i < inventoryMap.size(); i++) {
						insertInventory.setInt(1, inventoryMap.get(i).getGold());
						insertInventory.setString(2, inventoryMap.get(i).getOwner());
						insertInventory.addBatch();
					}
					insertInventory.executeBatch();
					
					System.out.println("Inventory table populated");
					
					insertEquipment = conn.prepareStatement("insert into equipment (gold, owner) values(?, ?)");
					for (int i = 1; i < equipmentMap.size(); i++) {
						insertEquipment.setInt(1, equipmentMap.get(i).getGold());
						insertEquipment.setString(2, equipmentMap.get(i).getOwner());
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
						insertItem.setInt(6, item.getInventory_ID());
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
						insertRooms.setInt(4, 0);
						insertRooms.addBatch();
					}
					insertRooms.executeBatch();
					
					System.out.println("Rooms table populated");
					
					insertPlayer = conn.prepareStatement("insert into players (name, health, armor, damage, inventory_id, equipment_id, game_id, room_id) values (?, ?, ?, ?, ?, ?, ?, ?)");
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
					insertPlayer.setInt(7, 0);
					insertPlayer.setInt(8, player.getRoom_ID());
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
					
					insertConnections = conn.prepareStatement("insert into connections (room_id, room_id2, direction_id) values (?, ?, ?)");
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