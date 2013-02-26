package me.Wouter0100.StarterIsAGod;
     
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
    
import java.text.DateFormat;
    
public class StarterIsAGodPlayerListener implements Listener 
{
  	private static StarterIsAGod plugin;
  	public DateFormat dateFormat;
             
	public StarterIsAGodPlayerListener(StarterIsAGod instance) 
 	{
		plugin = instance;
 
	}
             
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) 
	{
		Player p = event.getPlayer();
		if(!p.hasPlayedBefore() && plugin.checkPermission(p, "starterisagod.get")) 
		{
        	p.sendMessage("Hello starter, you've got "+plugin.configTime+" hours god.");
        	p.sendMessage("Commands /sg help");
            plugin.addToPlayersList(p.getName(),getDateTimenew());
        }
        else if(plugin.checkDate(p))
        {
        	plugin.gods.put(p.getName(), true); 
        	p.sendMessage("God mode is set for "+plugin.configTime+" hours, you still have god enabled.");
        	p.sendMessage("Commands /sg help");
        }
          
     }
     
     public void onPlayerQuit(PlayerQuitEvent event)
     {
       	 Player p = event.getPlayer();
    	 plugin.gods.put(p.getName(), false);
     }
     
       
     private long getDateTimenew() 
     {
    	 long output = (System.currentTimeMillis() + (1000 * 60 * 60 * plugin.configTime));
    	 return output;
     }
}