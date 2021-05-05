package edu.ycp.cs320.tbagproj.model;

public class Attack {
	
	public double attack(Player player, NPC npc, boolean playerAttack) {
		double damageDelt;
		if (playerAttack == true) {
			damageDelt = player.getTotalDamage() - npc.getDefence();
			if (damageDelt < 0) {
				return 0.0;
			}else {
				if (npc.getCurHealth() - damageDelt < 0) {
					damageDelt += npc.getCurHealth() - damageDelt;
					return damageDelt;
				}else {
					return damageDelt;
				}
			}
		}else {
			return npc.getTotalDamage() - player.getDefence();
		}
	}
}
