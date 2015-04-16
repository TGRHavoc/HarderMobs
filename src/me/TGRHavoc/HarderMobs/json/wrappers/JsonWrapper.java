package me.TGRHavoc.HarderMobs.json.wrappers;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bukkit.entity.EntityType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JsonWrapper {
	
	File myFile;
	
	String name;
	String description;
	
	int chance;
	
	boolean enabled;
	
	EntityType[] applyTo;
	ArmourWrapper[] armour;
	EffectsWrapper[] spawnEffects;
	EffectsWrapper[] attackEfects;
	
	public JsonWrapper(File myFile){
		this.myFile = myFile;
		
		loadStuffFromFile(myFile);
	}
	
	private void loadStuffFromFile(File f){
		JSONParser parser = new JSONParser();
		
		try {
			JSONObject obj = (JSONObject) parser.parse(new FileReader(f));
			
			name = (String)obj.get("name");
			description = (String) obj.get("description");
			chance = (int)obj.get("chance");
			enabled = (boolean)obj.get("enabled");
			
			
			//Mobs
			JSONArray mobs = (JSONArray)obj.get("mobs");
			List<EntityType> mobBuffer = new ArrayList<EntityType>();
			
			@SuppressWarnings("unchecked")
			Iterator<String> iterator = mobs.iterator();
			while(iterator.hasNext()){
				String mob = iterator.next();
				if (mob.equalsIgnoreCase("all")){
					mobBuffer.addAll(Arrays.asList(EntityType.values()));
					break;
				}
				
				EntityType t = EntityType.valueOf(mob.replace(' ', '_').toUpperCase());
				mobBuffer.add(t);
			}
			applyTo = (EntityType[])mobBuffer.toArray();
			//End mobs
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
}
