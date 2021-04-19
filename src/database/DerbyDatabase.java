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
	
	public<ResultType> ResultType executeTransaction(Transaction<ResultType> txn) {
		try {
			return doExecuteTransaction(txn);
		} catch (SQLException e) {
			throw new PersistenceException("Transaction failed", e);
		}
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
				PreparedStatement stmt1 = null;
				PreparedStatement stmt2 = null;
				PreparedStatement stmt3 = null;
				PreparedStatement stmt4 = null;
				PreparedStatement stmt5 = null;
				PreparedStatement stmt6 = null;
				PreparedStatement stmt7 = null;
				PreparedStatement stmt8 = null;
				
				try {
					stmt1 = conn.prepareStatement(
						"create table games (" +
						" 	game_key String prmary key " +
						" 	player_id integer constraint player_id references players," +
						" 	room_id integer constraint room_id references rooms "
					);
					stmt1.executeUpdate();
					
					stmt2 = conn.prepareStatement(
						"create table players (" +
						"	player_id integer primary key " +
						"		generated always as identity (start with 0, increment by 1), " +
						"	name varchar(20)," +
						"	health integer()," +
						"	armor integer()," +
						"	damage integer()," +
						"	inventory_id integer constraint inventory_id references inventory, " +
						"	equipment_id integer constraint equipemnt_id references equipment" +
						")"
					);
					stmt2.executeUpdate();
					
					stmt3 = conn.prepareStatement(
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
					stmt3.executeUpdate();
					
					stmt4 = conn.prepareStatement(
						"create table inventory (" +
						"	inventory_id integer primary key " +
						"		generated always as identity (start with 0, increment by 1), " +
						"	item_id integer constraint item_id references item, " +
						"	gold integer()" +
						")"
					);
					stmt4.executeUpdate();
					
					stmt5 = conn.prepareStatement(
						"create table equipment (" +
						"	equipment_id integer primary key " +
						"		generated always as identity (start with 0, increment by 1), " +
						"	item_id integer constraint item_id references item, " +
						"	gold integer()" +
						")"
					);
					stmt5.executeUpdate();
					
					stmt6 = conn.prepareStatement(
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
					stmt6.executeUpdate();
					
					stmt7 = conn.prepareStatement(
						"create table rooms (" +
						"	room_id integer primary key " +
						"		generated always as identity (start with 0, increment by 1), " +
						"	name varchar(40)," +
						"	inventory_id integer constraint inventory_id references inventory, " +
						"	npc_id integer constraint npc_id references npcs, " +
						"	description varchar(100)" +
						")"
					);
					stmt7.executeUpdate();
					
					stmt8 = conn.prepareStatement(
						"create table connections (" +
						"	connection_id integer primary key " +
						"		generated always as identity (start with 0, increment by 1), " +
						"	room_id integer constraint room_id references rooms, " +
						"	direction_id integer()" +
						")"
					);
					stmt8.executeUpdate();

					return true;
				}finally {
					DBUtil.closeQuietly(stmt1);
					DBUtil.closeQuietly(stmt2);
					DBUtil.closeQuietly(stmt3);
					DBUtil.closeQuietly(stmt4);
					DBUtil.closeQuietly(stmt5);
					DBUtil.closeQuietly(stmt6);
					DBUtil.closeQuietly(stmt7);
					DBUtil.closeQuietly(stmt8);
				}
			}
		});
	}
}
