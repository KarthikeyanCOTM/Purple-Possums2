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
		secondRoom = new Room(items, "secondRoom", NPCList, tempMap);
		tempMap.put("room", secondRoom);
	}
	
	@Test
	public void testSetConnections() {
		model.setConnections("room", secondRoom);
		assertTrue(model.isConnected(secondRoom) == true);
	}

}
