/*******************************************************************************
 *     Copyright (C) 2015 Jordan Dalton
 *
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License along
 *     with this program; if not, write to the Free Software Foundation, Inc.,
 *     51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *******************************************************************************/
package me.TGRHavoc.HarderMobs;

import me.TGRHavoc.HarderMobs.json.wrappers.JsonWrapper;

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
			sendHelpMessage(sender);
			
		}else if (args.length == 1){
			if (args[0].equalsIgnoreCase("on")){
				plugin.setEnabledP(true);
				sender.sendMessage(ChatColor.GREEN + "HarderMobs is now enabled.");
				
			}else if(args[0].equalsIgnoreCase("off")){
				plugin.setEnabledP(false);
				sender.sendMessage(ChatColor.RED + "HarderMobs is now disabled");
				
			}else if(args[0].equalsIgnoreCase("mods") && sender.hasPermission("HarderMobs.Admin")){
				sender.sendMessage(ChatColor.GREEN + "----- Enabled Mods: -----");
				for (JsonWrapper mod : plugin.getMods()){
					if (!mod.isEnabled()) // If the modification isn't enabled then don't show it
						continue;
					sender.sendMessage(ChatColor.GOLD + mod.getName() + ChatColor.GREEN + "\n    " + mod.getDescription());
				}
			}
			
			
			else{ //Haven't got a correct argument
				sendHelpMessage(sender);
			}
		}
		
		return true;
	}
	
	private void sendHelpMessage(CommandSender sendTo){
		sendTo.sendMessage(ChatColor.GOLD + "----- HarderMob Commands -----");
		
		sendTo.sendMessage(ChatColor.GOLD + "/hardermobs on - "+ ChatColor.BLUE + "Turn the plugin on.");
		sendTo.sendMessage(ChatColor.GOLD + "/hardermobs off - "+ ChatColor.BLUE + "Turn the plugin off.");
		sendTo.sendMessage(ChatColor.GOLD + "/hardermobs mods - "+ ChatColor.BLUE + "See the enabled modifications.");
	}
	
}
