package edu.ycp.cs320.tbagproj.model;

public class Attack {
	
	public double attack(Player player, NPC npc, boolean playerAttack) {
		if (playerAttack == true) {
			return player.getTotalDamage() - npc.getDefence();
		}else {
			return npc.getTotalDamage() - player.getDefence();
		}
	}
}
