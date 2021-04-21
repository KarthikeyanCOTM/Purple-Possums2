package database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
 
import edu.ycp.cs320.tbagproj.model.*;

public class FakeDatabase implements IDatabase {
	private List<Game> gameList;
	private List<Inventory> inventoryList;
	private List<Item> itemList;
	private List<Inventory> equipmentList;
	private List<NPC> npcList;
	private List<Player> playerList;
	private List<Room> roomList;
	private List<String> connectionList;
	
	public FakeDatabase() {
		gameList = new ArrayList<Game>();
		inventoryList = new ArrayList<Inventory>();
		itemList = new ArrayList<Item>();
		equipmentList = new ArrayList<Inventory>();
		npcList = new ArrayList<NPC>();
		playerList = new ArrayList<Player>();
		roomList = new ArrayList<Room>();
		connectionList = new ArrayList<String>();
		
		readInitialData();
		
		System.out.println(itemList.size() + " items");
		System.out.println(inventoryList.size() + " inventory");
	}
	
	public void readInitialData() {
		try {
			itemList.addAll(InitialData.getItems(false, 0));
			//inventoryList.addAll(InitialData.getInventorys());
		} catch (IOException e) {
			throw new IllegalStateException("Couldn't read initial data", e);
		}
	}
	
	public String saveGame(String key, Player player, Map map) {
		
		return null;
	}

	
	public String loadGame(String key) {
		
		return null;
	}

}
