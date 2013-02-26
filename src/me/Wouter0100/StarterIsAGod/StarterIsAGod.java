package me.Wouter0100.StarterIsAGod;
     
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;
     
     
public class StarterIsAGod extends JavaPlugin {
	
	public Integer configTime;
	public Boolean configPermission;
	
	private File pFolder = new File("plugins" + File.separator + "StarterIsAGod" + File.separator + "playerGod.save");
	
	private HashMap<String, Long> saveMap = new HashMap<String, Long>();
	
    private final Logger log = Logger.getLogger("Minecraft");
    private PluginDescriptionFile pdFile;
    
    private final Listener playerListener = new StarterIsAGodPlayerListener(this);
    private final Listener entityListener = new StarterIsAGodEntityListener(this);
    
    
    public final HashMap<String, Boolean> gods = new HashMap<String, Boolean>();


    public void onDisable() 
    {
    	saveHashmap();
        log.info("[StarterIsAGod] v"+pdFile.getVersion()+" has been disabled.");   
    }

    public void onEnable() 
    {
        pdFile = getDescription();
        log.info("[StarterIsAGod] Starting up.");
        
        loadConfiguration();
        
        loadHashmap();
        
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(playerListener, this);
        pm.registerEvents(entityListener, this);
    }

    
    public void loadConfiguration()
    {
    	getConfig().addDefault("StarterIsAGod.Time.Hours", 24);
    	getConfig().addDefault("StarterIsAGod.permissionsEnabled", true);
        getConfig().options().copyDefaults(true);
        saveConfig();
        configPermission = getConfig().getBoolean("StarterIsAGod.permissionsEnabled");
        configTime = getConfig().getInt("StarterIsAGod.Time.Hours");
    }
    
    private void loadHashmap()
    {
		try {
			
			if(!pFolder.exists())
			{
				return;
			}
			
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(pFolder)));

		    String l;
			while((l = br.readLine()) != null)
			{
				String[] args = l.split("[,]", 2);
				if(args.length != 2)continue;
				String p = args[0].replaceAll(" ", "");
				String b = args[1].replaceAll(" ", "");
				
				saveMap.put(p, Long.parseLong(b));
				
			}
			
			br.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void saveHashmap()
    {
    	try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(pFolder));
			bw.write(new String());
			
			for(String p : saveMap.keySet())
			{
				bw.write(p + "," + saveMap.get(p));
				bw.newLine();
			}
			bw.flush();
			bw.close();
			   
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public boolean checkPermission(Player player, String perm)
    {
    	if(configPermission)
    	{
    		return player.hasPermission(perm);
    	}else{
    		return true;
    	}
    }
        
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) 
    {
    	if(!(sender instanceof Player)) {
    		sender.sendMessage("You need to be a player!");
    	}else{
    		Player player = (Player) sender;
    		if (command.getName().equalsIgnoreCase("sg")) {
    			if (args.length == 0) {
    				if (checkPermission(player, "starterisagod.on")) {
    					sender.sendMessage("/sg on - Turns Starter God on.");
    				}
    				if (checkPermission(player, "starterisagod.off")) {
    					sender.sendMessage("/sg off - Turns Starter God Off.");
    				}
    				if (checkPermission(player, "starterisagod.stop")) {
    					sender.sendMessage("/sg stop - Stops your time.");
    				}
    				if (checkPermission(player, "starterisagod.time")) {
    					sender.sendMessage("/sg time - Shows you when your time is expired.");
    				}
    			} else {
    				if (args[0].equals("off")) {
    					if (checkPermission(player, "starterisagod.off")) 
    					{
    						if (checkDate(player)) {
    							gods.put(player.getName(), false);
    							sender.sendMessage("Your StarterGod is now off.");
    							sender.sendMessage("To stop your StarterGod type: /sg stop");
    						} else {
    							sender.sendMessage("Your time is already done.");
    						}
    					}else{
    						sender.sendMessage(ChatColor.RED + "You don't have permission for this command.");
    					}
    				} else if (args[0].equals("on")) {
    					if (checkPermission(player, "starterisagod.on")) {
    						if (checkDate(player)) {
    							gods.put(player.getName(), true);
    							sender.sendMessage("Your StarterGod is now on.");
    						} else {
    							sender.sendMessage("Your time is already done.");
    						}
    					}else{
    						sender.sendMessage(ChatColor.RED + "You don't have permission for this command.");
    					}
    				} else if (args[0].equals("time")) {
    					if (checkPermission(player, "starterisagod.time")) {
    						if (checkDate(player)) {
    							sender.sendMessage(display(player));
    						} else {
    							sender.sendMessage("Your time is already done.");
    						}
    					}else{
    						sender.sendMessage(ChatColor.RED + "You don't have permission for this command.");
    					}
    				} else if (args[0].equals("stop")) {
    					if (checkPermission(player, "starterisagod.stop")) {
    						if (checkDate(player)) {
    							sender.sendMessage(ChatColor.RED +"You're on your way to turn off your StarterGod time, There's no way back!");
    							sender.sendMessage("To accept: /sg stopaccept");
    						} else {
    							sender.sendMessage("Your time is already done.");
    						}
    					}else{
    						sender.sendMessage(ChatColor.RED + "You don't have permission for this command.");
    					}
    				} else if (args[0].equals("stopaccept")) {
    					if (checkPermission(player, "starterisagod.stop")) {
    						if (checkDate(player)) {
    							gods.remove(player.getName());
    							saveMap.remove(player.getName());
    							sender.sendMessage("You're time is now off.");
    						}else{
    							sender.sendMessage("Your time is already done.");
    						}
    					}else{
    						sender.sendMessage(ChatColor.RED + "You don't have permission for this command.");
    					}
    				} else {
    					sender.sendMessage("Use: /sg [off/on/stop/time]");
    					return true;
    				}
    			}
    			return true;
    		}
    		return true;
    	}
		return true;
    }
        
 	public boolean enabled(Player player)
	{
 		return gods.containsKey(player.getName());
	}
   
	public void addToPlayersList(String paramPlayer, long i) 
	{
		log.info("[StarterIsAGod] New player " + paramPlayer + " Added with time " + i + ".");
		gods.put(paramPlayer, true);
		
		saveMap.put(paramPlayer, i);
	}
        
    public boolean checkDate(Player p) 
    {
    	Object date = saveMap.get(p.getName());
    	
    	if(date != null)
    	{
	    	if((Long) date >= System.currentTimeMillis()) 
	    	{
	 	    	return true;
	 	    }
    	}
    	
    	return false;
    }
    
	public String display(Player player) 
	{
		Object checkDate = saveMap.get(player.getName());
    	
    	if(checkDate != null)
    	{
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm");
	    	Date date = new Date((Long) checkDate);
	    	
	    	if (date != null)
	    	{
	    		return dateFormat.format(date) + " then your time will expire.";
	        }   
    	}
    	
    	return "Can't get expire time.";
	}
}