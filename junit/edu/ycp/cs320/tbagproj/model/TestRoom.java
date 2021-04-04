package edu.ycp.cs320.tbagproj.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;

import edu.ycp.cs320.tbagproj.model.Room;

public class TestRoom {
	private Room model;
	private Room secondRoom;
	private HashMap<String, Room> tempMap;
	private ArrayList<NPC> NPCList;
	private Inventory items;
	
	@Before
	public void setup() {
		model = new Room();
		items = new Inventory();
		NPCList = new ArrayList<NPC>();
		tempMap = new HashMap<String, Room>();
		secondRoom = new Room(items, "secondRoom", NPCList, tempMap);
		tempMap = new HashMap<String, Room>();
		tempMap.put("room", secondRoom);
	}
	
	@Test
	public void testGetInventory() {
		model.setInventory(items);
		assertTrue(model.getInventory() == items);
	}
	
	@Test
	public void testGetConnections() {
		model.setConnections("room", secondRoom);
		HashMap<String, Room> temp = model.getConnections();
		assertTrue(temp.get("room") == tempMap.get("room"));
	}

}
