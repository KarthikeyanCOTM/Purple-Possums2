package edu.ycp.cs320.tbagproj.model;

public class Attack {
	
	public void attack(Player player, NPC npc, boolean playerAttack) {
		if (playerAttack == true) {
			npc.setHealth((player.getTotalDamage() - npc.getDefence()) * -1);
		}else {
			player.setHealth((npc.getTotalDamage() - player.getDefence()) * -1);
		}
	}
}
