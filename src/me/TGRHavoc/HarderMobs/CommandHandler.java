package me.TGRHavoc.HarderMobs;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandHandler implements CommandExecutor{
	//private Plugin instance = Main.getInstance();
	
	public CommandHandler(Main plugin){
		this.plugin = plugin;
	}
	
	Main plugin;
	//FileConfiguration config = plugin.getConfig();
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args.length == 0){
			sender.sendMessage(ChatColor.RED + "Sorry, harder mobs takes arguments. Maybe you ment one of the following:");
			//TODO: Send help message
		}else if (args.length == 1){
			if (args[0].equalsIgnoreCase("on")){
				
			}else if(args[0].equalsIgnoreCase("off")){
				
			}
		}
		
		return false;
	}
	
	
}
