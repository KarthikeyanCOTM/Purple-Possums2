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
	
	@Test
	public void testContents() {
		items.addGold(5);
		model.setInventory(items);
		model.updateContents();
		String temp = model.getContents();
		assertEquals(temp, "The room has 5 gold.");
		model.getInventory().addGold(-5);
		model.updateContents();
		temp = model.getContents();
		assertEquals(temp, "The room has nothing.");
		NPC tempNPC = new NPC("human");
		NPCList.add(tempNPC);
		model.setNPCs(NPCList);
		model.updateContents();
		temp = model.getContents();
		assertEquals(temp, "The room has nothing.\nThere is a human");
	}

	@Test
	public void testGetNPC() {
		NPC tempNPC = new NPC("human");
		NPCList.add(tempNPC);
		model.setNPCs(NPCList);
		assertEquals(model.getNPC("human"), tempNPC);
	}
	
	@Test
	public void testContainsNPC() {
		NPC tempNPC = new NPC("human");
		NPCList.add(tempNPC);
		model.setNPCs(NPCList);
		assertEquals (model.containsNPC("human"), true);
	}
}
