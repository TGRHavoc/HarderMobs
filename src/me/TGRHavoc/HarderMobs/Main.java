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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import me.TGRHavoc.HarderMobs.json.wrappers.JsonWrapper;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener{
	
	private static Plugin instance;
	
	YamlConfiguration yamlConfig;
	File[] modFiles;
	List<JsonWrapper> mods = new ArrayList<JsonWrapper>();

	boolean enabled;
	Metrics metrics;
		
	@Override
	public void onEnable(){		
		instance = this;
		initConfig();
		
		getCommand("HarderMobs").setExecutor(new CommandHandler(this));
		
		if (yamlConfig.getBoolean("auto-update")){
			new Updater(this, 77840, this.getDataFolder(), Updater.UpdateType.DEFAULT,true);
		}
		if (yamlConfig.getBoolean("allow-stats")){
			try {
				metrics = new Metrics(this);
				metrics.start();
			} catch (IOException e) {
				getServer().getLogger().info("Error enabling metrics... Maybe server is down?");
				e.printStackTrace();
			}
		}
		
		
		Bukkit.getServer().getPluginManager().registerEvents(new MyEventHandler(this), this);
	}
	
	private void initConfig(){
		File configFile = new File(this.getDataFolder(), "config.yml");
		try {			
			writeToFile(getResource("config.yml"), configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		yamlConfig = YamlConfiguration.loadConfiguration(configFile);
		
		File mod_folder = new File(getDataFolder() + File.separator + "mods");
		if(!mod_folder.exists()){
			try {
				writeToFile(getResource("mob_examples/Harder_Mobs.json"), new File(getDataFolder() + File.separator + "mods", "harder mobs.json"));
				writeToFile(getResource("mob_examples/Harder_Skeletons.json"), new File(getDataFolder() + File.separator + "mods", "harder skeletons.json"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		modFiles = mod_folder.listFiles();
		
		for (File f: mod_folder.listFiles()){
			mods.add( new JsonWrapper(f) );
		}
	}
	
	private void writeToFile(InputStream in, File file) throws IOException{
		if (!file.exists()){
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
		
		OutputStream out = new FileOutputStream(file);
		byte[] buffer = new byte[1024];
		int bytesRead;
		while((bytesRead = in.read(buffer)) !=-1){
			out.write(buffer, 0, bytesRead);
		}
		in.close();
		out.flush();
		out.close();
	}
	
	public void onDisable(){
		instance = null;
	}
	public Plugin getInstance() {
		return instance;
	}
	
	public boolean isEnabledP() {
		return enabled;
	}
	public void setEnabledP(boolean enabled) {
		this.enabled = enabled;
	}

	public List<JsonWrapper> getMods() {
		return mods;
	}
	
}
