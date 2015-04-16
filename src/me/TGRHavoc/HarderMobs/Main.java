package me.TGRHavoc.HarderMobs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener{
	
	private static Plugin instance;
	
	YamlConfiguration yamlConfig;
	File[] modFiles;

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
	}
	
	private void initConfig(){
		File configFile = new File(this.getDataFolder(), "config.yml");
		try {			
			writeToFile(getResource("config.yml"), configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		yamlConfig = YamlConfiguration.loadConfiguration(configFile);
		
		File mod_folder = new File(getDataFolder() + File.pathSeparator + "mods");
		if(!mod_folder.exists()){
			try {
				writeToFile(getResource("mob_examples/Harder_Mobs.json"), new File(getDataFolder() + File.separator + "mods", "harder_mobs.json"));
				writeToFile(getResource("mob_examples/Harder_Skeletons.json"), new File(getDataFolder() + File.separator + "mods", "harder_skeletons.json"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		modFiles = mod_folder.listFiles();
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
	
}
