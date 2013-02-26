 package me.Wouter0100.StarterIsAGod;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;


public class StarterIsAGodEntityListener implements Listener 
{
	
	private StarterIsAGod plugin;
	
	public StarterIsAGodEntityListener(StarterIsAGod plugin) 
	{
        this.plugin = plugin;
    }

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) 
	{
		if (event.getEntity() instanceof Player && event instanceof EntityDamageByEntityEvent)
		{
			Player player = (Player) event.getEntity();
			EntityDamageByEntityEvent aAttacker = (EntityDamageByEntityEvent) event;
			
			if (aAttacker.getDamager() instanceof Player)
			{
				Player attacker = (Player) aAttacker.getDamager();

				boolean playerEnabled = plugin.enabled(player);
				boolean attackerEnabled = plugin.enabled(attacker);
						
						
				if (playerEnabled || attackerEnabled)
				{
					String message = null;
					if (playerEnabled)
					{
						message = player.getDisplayName() + " is a starter and have disabled PVP'ing for " + plugin.configTime + " hours.";
					}
					if (attackerEnabled)
					{
						message = "You are a starter, you cannot PVP for " + plugin.configTime + " hours.";
					}
					
					attacker.sendMessage(message);
					event.setCancelled(true);
				}
			}
		}
	}
}