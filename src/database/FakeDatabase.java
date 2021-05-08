package database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
 
import edu.ycp.cs320.tbagproj.model.*;

public class FakeDatabase implements IDatabase {
	private List<Game> gameList;
	private HashMap<Integer, Inventory> inventoryList;
	private List<Item> itemList;
	private HashMap<Integer, Inventory> equipmentList;
	private List<NPC> npcList;
	private List<Player> playerList;
	private List<Room> roomList;
	private List<String> connectionList;
	private String storedKey;
	
	public FakeDatabase() {
		gameList = new ArrayList<Game>();
		inventoryList = new HashMap<Integer, Inventory>();
		itemList = new ArrayList<Item>();
		equipmentList = new HashMap<Integer, Inventory>();
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
			inventoryList.putAll(InitialData.getInventorys());
			equipmentList.putAll(InitialData.getEquipments());
		} catch (IOException e) {
			throw new IllegalStateException("Couldn't read initial data", e);
		}
	}
	
	public String saveGame(String key, Player player, List list) {
		
		return null;
	}

	

	public String loadGame(String key, Player player, List<Room> roomList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteGame() {
		// TODO Auto-generated method stub
		
	}

}
