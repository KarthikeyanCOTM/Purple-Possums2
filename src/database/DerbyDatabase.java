package database;

//working tables: connections, games, equipment, rooms, players, npcs, inventory
//why tables bad: item: won't assign items to correct inventory_id

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
	
	private static final int VOID = 0;
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
				PreparedStatement itemStmt = null;
				PreparedStatement inventoryStmt = null;
				PreparedStatement inventoryStmt2 = null;
				PreparedStatement equipmentStmt = null;
				PreparedStatement equipmentStmt2 = null;
				PreparedStatement npcStmt = null;
				PreparedStatement connectionsStmt = null;
				
				ResultSet resultSet1 = null;
				ResultSet resultSet2 = null;
				ResultSet resultSet3 = null;
				ResultSet resultSet4 = null;
				ResultSet resultSet5 = null;
				
				Integer game_ID = -1;
				Integer totalEquipments = 0;
				Integer totalRooms = 0;
				Integer totalInventorys = 0;
				ArrayList<Item> itemList = new ArrayList<Item>();
				ArrayList<NPC> npcList = new ArrayList<NPC>();
				ArrayList<Inventory> inventoryList = new ArrayList<Inventory>();
				ArrayList<Inventory> equipmentList = new ArrayList<Inventory>();
				
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
							if (list.get(i).getInventory().getItemList() != null) {
								itemList.addAll(list.get(i).getInventory().getItemList());
							}
							if (list.get(i).getNPCs() != null) {
								npcList.addAll(list.get(i).getNPCs());
							}
						}
						for (int i = 0; i < list.size(); i++) {
							ArrayList<NPC> tempList = new ArrayList<NPC>();
							if (list.get(i).getNPCs() != null) {
								tempList = list.get(i).getNPCs();
								for (int x = 0; x < tempList.size(); x++) {
									npcList.add(tempList.get(x));
								}
							}
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
						
						inventoryList.add(player.getInventory());
						for (int i = 0; i < list.size(); i++) {
							inventoryList.add(list.get(i).getInventory());
						}
						for (int i = 0; i < npcList.size(); i++) {
							inventoryList.add(npcList.get(i).getInventory());
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
						
						equipmentList.add(player.getEquipment());
						for (int i = 0; i < npcList.size(); i++) {
							equipmentList.add(npcList.get(i).getEquipment());
						}
						
						equipmentStmt = conn.prepareStatement(
								"update inventory set gold = ? where inventory_id = ?"
							);
							for (int i = 0; i < equipmentList.size(); i++) {
								equipmentStmt.setInt(1, equipmentList.get(i).getGold());
								equipmentStmt.setInt(2, equipmentList.get(i).getEquipment_ID());
								equipmentStmt.addBatch();
							}
							equipmentStmt.executeBatch();
							System.out.println("equipment table updated");
						
						playerStmt = conn.prepareStatement(
							"update players set health = ?, armor = ?, damage = ?, room_id = ? where player_id = ?"
						);
						playerStmt.setDouble(1, player.getCurHealth());
						playerStmt.setDouble(2, player.getDefence());
						playerStmt.setDouble(3, player.getTotalDamage());
						System.out.println(player.getRoom_ID());
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
						
						for (int i = 0; i < list.size(); i++) {
							ArrayList<NPC> tempList = new ArrayList<NPC>();
							if (list.get(i).getNPCs() != null) {
								tempList = list.get(i).getNPCs();
								for (int x = 0; x < tempList.size(); x++) {
									npcList.add(tempList.get(x));
								}
							}
						}
						
						inventoryStmt = conn.prepareStatement(
							"select count(*)" +
							" from inventory"
						);
							
						resultSet3 = inventoryStmt.executeQuery();
						if (resultSet3.next()) {
							totalInventorys = resultSet3.getInt(1);
						}
						
						inventoryStmt2 = conn.prepareStatement(
							"insert into inventory (gold, owner) " +
							" values(?, ?) "
						);
						
						for (int i = 0; i < list.size(); i++) {
							inventoryStmt2.setInt(1, list.get(i).getInventory().getGold());
							inventoryStmt2.setString(2, list.get(i).getName());
							inventoryStmt2.addBatch();
						}
						inventoryStmt2.setInt(1, player.getInventory().getGold());
						inventoryStmt2.setString(2, player.getName());
						inventoryStmt2.addBatch();
						for (int x = 0; x < npcList.size(); x++) {
							inventoryStmt2.setInt(1, npcList.get(x).getInventory().getGold());
							inventoryStmt2.setString(2, npcList.get(x).getName());
							inventoryStmt2.addBatch();
						}
						inventoryStmt2.executeBatch();
						System.out.println("Inventory table updated");
						
						equipmentStmt = conn.prepareStatement(
							"select count(*)" +
							" from equipment"
						);
							
						resultSet4 = equipmentStmt.executeQuery();
						if (resultSet4.next()) {
							totalEquipments = resultSet4.getInt(1);
						}
							
						equipmentStmt2 = conn.prepareStatement(
							"insert into equipment (gold, owner) " +
							" values (?, ?) "
						);
						equipmentStmt2.setInt(1, player.getEquipment().getGold());
						equipmentStmt2.setString(2, player.getName());
						equipmentStmt2.addBatch();
						for (int i = 0; i < npcList.size(); i++) {
							equipmentStmt2.setInt(1, npcList.get(i).getEquipment().getGold());
							equipmentStmt2.setString(2, npcList.get(i).getName());
							equipmentStmt2.addBatch();
						}
						
						equipmentStmt2.executeBatch();
						System.out.println("Equipment tabel updated");
						
						roomsStmt = conn.prepareStatement(
								"select count(*)" +
								" from rooms"
						);
							
						resultSet5 = roomsStmt.executeQuery();
						if (resultSet5.next()) {
							totalRooms = resultSet5.getInt(1);
						}
						
						roomsStmt2 = conn.prepareStatement(
							"insert into rooms (name, inventory_id, description, game_key)" +
							" values (?, ?, ?, ?) "
						);
						for (int i = 0; i < list.size(); i++) {
							roomsStmt2.setString(1, list.get(i).getName());
							roomsStmt2.setInt(2, list.get(i).getInventory_ID() + totalInventorys);
							roomsStmt2.setString(3, list.get(i).getDescription());
							roomsStmt2.setInt(4, game_ID);
							roomsStmt2.addBatch();
						}
						
						roomsStmt2.executeBatch();
						System.out.println("Room tabel updated");
												
						playerStmt = conn.prepareStatement(
							"insert into players (name, health, armor, damage, inventory_id, equipment_id, game_id, room_id) " +
							" values (?, ?, ?, ?, ?, ?, ?, ?) "
						);
						playerStmt.setString(1, player.getName());
						playerStmt.setDouble(2, player.getCurHealth());
						playerStmt.setDouble(3, player.getDefence());
						playerStmt.setDouble(4, player.getTotalDamage());
						playerStmt.setInt(5, player.getInventory_ID() + totalInventorys);
						playerStmt.setInt(6, player.getEquipment_ID() + totalEquipments);
						playerStmt.setInt(7, game_ID);
						playerStmt.setInt(8, player.getRoom_ID() + totalRooms);
						
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
									npcStmt.setInt(5, npcs.get(x).getInventory_ID() + totalInventorys);
									System.out.println(totalEquipments);
									npcStmt.setInt(6, npcs.get(x).getEquipment_ID() + totalEquipments);
									npcStmt.setBoolean(7, npcs.get(x).getIsNPCAlive());
									System.out.println(totalRooms);
									npcStmt.setInt(8, list.get(i).getRoom_ID() + totalRooms);
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
								connectionsStmt.setInt(1, list.get(x).getRoom_ID() + totalRooms);
								connectionsStmt.setInt(2, connections.get("north").getRoom_ID() + totalRooms);
								connectionsStmt.setInt(3, 0);
								connectionsStmt.addBatch();
							}
							if (connections.containsKey("east") == true) {
								connectionsStmt.setInt(1, list.get(x).getRoom_ID() + totalRooms);
								connectionsStmt.setInt(2, connections.get("east").getRoom_ID() + totalRooms);
								connectionsStmt.setInt(3, 1);
								connectionsStmt.addBatch();
							}
							if (connections.containsKey("south") == true) {
								connectionsStmt.setInt(1, list.get(x).getRoom_ID() + totalRooms);
								connectionsStmt.setInt(2, connections.get("south").getRoom_ID() + totalRooms);
								connectionsStmt.setInt(3, 2);
								connectionsStmt.addBatch();
							}
							if (connections.containsKey("west") == true) {
								connectionsStmt.setInt(1, list.get(x).getRoom_ID() + totalRooms);
								connectionsStmt.setInt(2, connections.get("west").getRoom_ID() + totalRooms);
								connectionsStmt.setInt(3, 3);
								connectionsStmt.addBatch();
							}
						}
						connectionsStmt.executeBatch();
						System.out.println("Connections table updated");
						
						if (player.getInventory() != null) {
							itemList.addAll(player.getInventory().getItemList());
						}
						for (int i = 0; i < list.size(); i++) {
							if (list.get(i).getInventory() != null) {
								itemList.addAll(list.get(i).getInventory().getItemList());
							}
						}
						for (int i = 0; i < npcList.size(); i++) {
							if (list.get(i).getInventory() != null) {
								itemList.addAll(npcList.get(i).getInventory().getItemList());
							}
						}
						
						itemStmt = conn.prepareStatement(
							"insert into item (name, damage, armor, healing, isusable, inventory_id, equipment_id)" +
							" values (?, ?, ?, ?, ?, ?, ?) "
						);
						for (int i = 0; i < itemList.size(); i++) {
							itemStmt.setString(1, itemList.get(i).getName());
							itemStmt.setDouble(2, itemList.get(i).getDamage());
							itemStmt.setDouble(3, itemList.get(i).getArmour());
							itemStmt.setDouble(4, itemList.get(i).getHealing());
							itemStmt.setBoolean(5, itemList.get(i).getIsUsable());
							if (itemList.get(i).getInventory_ID() == 0) {
								itemStmt.setInt(6, VOID);
								itemStmt.setInt(7, itemList.get(i).getEquipment_ID() + totalEquipments);
							}else {
								itemStmt.setInt(6, itemList.get(i).getInventory_ID() + totalInventorys);
								itemStmt.setInt(7, VOID);
							}
							itemStmt.addBatch();
						}
						
						itemStmt.executeBatch();
						System.out.println("Item table updated");
					}
				}finally {
					DBUtil.closeQuietly(gameStmt);
					DBUtil.closeQuietly(gameStmt2);
					DBUtil.closeQuietly(gameStmt3);
					DBUtil.closeQuietly(playerStmt);
					DBUtil.closeQuietly(roomsStmt);
					DBUtil.closeQuietly(roomsStmt2);
					DBUtil.closeQuietly(itemStmt);
					DBUtil.closeQuietly(inventoryStmt);
					DBUtil.closeQuietly(inventoryStmt2);
					DBUtil.closeQuietly(equipmentStmt);
					DBUtil.closeQuietly(equipmentStmt2);
					DBUtil.closeQuietly(npcStmt);
					DBUtil.closeQuietly(connectionsStmt);
					DBUtil.closeQuietly(resultSet1);
					DBUtil.closeQuietly(resultSet2);
					DBUtil.closeQuietly(resultSet3);
					DBUtil.closeQuietly(resultSet4);
					DBUtil.closeQuietly(resultSet5);
				}
				
				return "Game saved successfully with key: " + key;
			}
		});
	}
	
	//Load begin

		public String loadGame(String key, Player player, List<Room> roomList) {
			return executeTransaction(new Transaction<String>() {
			public String execute(Connection conn) throws SQLException {
		
			PreparedStatement gameStmt = null;
			PreparedStatement roomStmt = null;
			PreparedStatement inventoryStmt = null;
			PreparedStatement inventoryStmt2 = null;
			PreparedStatement equipStmt = null;
			PreparedStatement equipStmt2 = null;
			PreparedStatement itemStmt = null;
			PreparedStatement connStmt = null;
			
			ResultSet resultSet1 = null;
			ResultSet resultSet2 = null;
			ResultSet resultSet3 = null;
			
			
			try {
				gameStmt = conn.prepareStatement(
					"select game_id from games " +
					" where game_key = ?"
				);
				gameStmt.setString(1, key);
				resultSet1 = gameStmt.executeQuery();
				int gameID = resultSet1.getInt(0);
				
				loadPlayer(player, resultSet1);
				inventoryStmt = conn.prepareStatement("select * from inventory"
						+ "where inventory_id = ?"
						);
				inventoryStmt.setInt(1, player.getInventory_ID());
				resultSet1 = inventoryStmt.executeQuery();
				loadInventory(player.getInventory(), resultSet1);
				equipStmt2 = conn.prepareStatement("select * from equipment where equipment_id = ?");
				equipStmt2.setInt(1, player.getEquipment_ID());
				resultSet1 = equipStmt2.executeQuery();
				loadEquipment(player.getEquipment(), resultSet1);
				
				roomStmt = conn.prepareStatement("select * from rooms"
						);
				resultSet1 = roomStmt.executeQuery();
				for (int i = 0; i < roomList.size(); i++) {
					loadRoom(roomList.get(i), resultSet1);
				}
				
				for (int i = 0; i < roomList.size(); i++) {
					inventoryStmt2 = conn.prepareStatement("select * from inventory"
						+ "where inventory_id = ?");
					inventoryStmt2.setInt(1, roomList.get(i).getInventory_ID());
					resultSet1 = inventoryStmt2.executeQuery();
					loadInventory(roomList.get(i).getInventory(), resultSet1);
				}
				
				for (int i = 0; i < roomList.size(); i++) {
					equipStmt = conn.prepareStatement("select * from inventory"
						+ "where inventory_id = ?");
					equipStmt.setInt(1, roomList.get(i).getInventory_ID());
					resultSet1 = inventoryStmt.executeQuery();
					loadEquipment(roomList.get(i).getInventory(), resultSet1);
				}
				
				Item loadedItem = new Item();
				
				for (int i = 0; i < 4; i++) {
					itemStmt = conn.prepareStatement("select * from items"
							+ "where inventory_id = ?");
					itemStmt.setInt(1, i);
					resultSet2 = itemStmt.executeQuery();
					loadItem(loadedItem, resultSet2);
				}
				
				for (int i = 0; i < roomList.size(); i++) {
					connStmt = conn.prepareStatement("select * from connections"
							+ "where room_id = ?");
					connStmt.setInt(1, roomList.get(i).getRoom_ID());
					resultSet3 = connStmt.executeQuery();
					loadConnections(roomList.get(i), resultSet3);
				}
				
				
			} finally {
				DBUtil.closeQuietly(gameStmt);
				DBUtil.closeQuietly(roomStmt);
				DBUtil.closeQuietly(inventoryStmt);
				DBUtil.closeQuietly(inventoryStmt2);
				DBUtil.closeQuietly(equipStmt);
				DBUtil.closeQuietly(equipStmt2);
				DBUtil.closeQuietly(itemStmt);
				DBUtil.closeQuietly(connStmt);
				DBUtil.closeQuietly(resultSet1);
				DBUtil.closeQuietly(resultSet2);
				DBUtil.closeQuietly(resultSet3);
			}
			return key;
			};
			
			});
			
		}
		
		private void loadRoom(Room room, ResultSet resultSet) throws SQLException {
			room.setName(resultSet.getString(1));
			room.setInventory_ID(resultSet.getInt(2));
			room.setDescription(resultSet.getString(3));
			room.setGame_ID(resultSet.getInt(4));
		}
		
		private void loadPlayer(Player player, ResultSet resultSet) throws SQLException{
			player.setName(resultSet.getString(1));
			player.setHealth(resultSet.getDouble(2));
			player.setDefence(resultSet.getDouble(3));
			player.setTotalDamage(resultSet.getDouble(4));
			player.setInventory_ID(resultSet.getInt(5));
			player.setEquipment_ID(resultSet.getInt(6));
			player.setGame_ID(resultSet.getInt(7));
			player.setRoom_ID(resultSet.getInt(8));
		}
		
		private void loadInventory(Inventory inventory, ResultSet resultSet) throws SQLException{
			inventory.addGold(resultSet.getInt(1));
			inventory.setOwner(resultSet.getString(2));
		}
		
		private void loadEquipment(Inventory equipment, ResultSet resultSet) throws SQLException{
			equipment.addGold(resultSet.getInt(1));
			equipment.setOwner(resultSet.getString(2));
		}
		
		private void loadItem(Item item, ResultSet resultSet) throws SQLException{
			item.setName(resultSet.getString(1));
			item.setDamage(resultSet.getDouble(2));
			item.setArmour(resultSet.getDouble(3));
			item.setHealing(resultSet.getDouble(4));
			item.setIsUsable(resultSet.getBoolean(5));
			item.setInventory_ID(resultSet.getInt(6));
			item.setEquipment_ID(resultSet.getInt(7));
		}
		
		private void loadConnections(Room room, ResultSet resultSet) throws SQLException{
			room.setRoom_ID(resultSet.getInt(1));
			int connectID = resultSet.getInt(2);
			int dir = resultSet.getInt(3);
			if (dir == Room.NORTH)
				room.setConnectionsID("north", connectID);
			else if (dir == Room.EAST)
				room.setConnectionsID("east", connectID);
			else if (dir == Room.SOUTH)
				room.setConnectionsID("south", connectID);
			else if (dir == Room.WEST)
				room.setConnectionsID("west", connectID);
		}
		
		
		//load end
	
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
									"		generated always as identity (start with 1, increment by 1), " +
									"	gold integer," +
									"	owner varchar(20)" +
									")"
					);
					inventoryStmt.executeUpdate();
					
					System.out.println("Inventory table created");
					
					equipmentStmt = conn.prepareStatement(
							"create table equipment (" +
									"	equipment_id integer primary key " +
									"		generated always as identity (start with 1, increment by 1), " +
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
				ArrayList<Item> itemList;
				ArrayList<NPC> npcList;
				
				try {
					roomList = InitialData.getRooms();
					player = InitialData.getPlayer();
					inventoryMap = InitialData.getInventorys();
					equipmentMap = InitialData.getEquipments();
					itemList = InitialData.getAllItems(true);
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
					for (int i = 0; i < inventoryMap.size(); i++) {
						insertInventory.setInt(1, inventoryMap.get(i + 1).getGold());
						insertInventory.setString(2, inventoryMap.get(i + 1).getOwner());
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
					for (Item item : itemList) {
						insertItem.setString(1, item.getName());
						insertItem.setDouble(2, item.getDamage());
						insertItem.setDouble(3, item.getArmour());
						insertItem.setDouble(4, item.getHealing());
						insertItem.setBoolean(5, item.getIsUsable());
						if (item.getInventory_ID() != 0) {
							insertItem.setInt(6, item.getInventory_ID());
							insertItem.setInt(7, VOID);
						}else {
							insertItem.setInt(6, VOID);
							insertItem.setInt(7, item.getInventory_ID());
						}
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
					insertPlayer.setInt(5, player.getInventory_ID());
					insertPlayer.setInt(6, player.getEquipment_ID());
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
						insertNPCs.setInt(5, npc.getInventory_ID());
						insertNPCs.setInt(6, npc.getEquipment_ID());
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