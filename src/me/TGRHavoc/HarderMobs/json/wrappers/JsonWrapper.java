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
package me.TGRHavoc.HarderMobs.json.wrappers;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JsonWrapper {
	
	File myFile;
	
	String name;
	String description;
	
	long chance;
	
	boolean enabled;
	
	List<EntityType> applyTo = new ArrayList<EntityType>();
	
	//List of armour to add to entity when they spawn
	List<ArmourWrapper> armour = new ArrayList<ArmourWrapper>();
	//Spawn effects. Applied when the entity spawns
	List<EffectsWrapper> spawnEffects = new ArrayList<EffectsWrapper>();
	//Attack effects. Applied when the entity attacks a player
	List<EffectsWrapper> attackEfects = new ArrayList<EffectsWrapper>();
	//Weapon used by Entity.
	WeaponWrapper weaponWrapper = null;
	
	public JsonWrapper(File myFile){
		this.myFile = myFile;
		
		loadStuffFromFile(myFile);
		
		System.out.println("Successfully created JSONWrapper for mod " + name);
	}
	
	private void loadStuffFromFile(File f){
		JSONParser parser = new JSONParser();
		
		try {
			JSONObject obj = (JSONObject) parser.parse(new FileReader(f));
			
			if (!obj.containsKey("name")){
				Bukkit.getLogger().info("Sorry, couldn't find a name for this mod... Disabling it");
				enabled = false;
				return;
			}else{
				name = (String)obj.get("name");
			}
			
			if (!obj.containsKey("description")){
				Bukkit.getLogger().info("Sorry, found no description.. Assuming this isn't a mistake..");
				description = "No description given";
			}else{
				description = (String) obj.get("description");
			}
			
			if (!obj.containsKey("chance")){
				Bukkit.getLogger().info("Sorry, found no chance.. Defaulting to 100%");
				chance = 100;
			}else{
				chance = (long)obj.get("chance");
			}
			
			if (!obj.containsKey("enabled")){
				Bukkit.getLogger().info("Sorry, found no enabled attribute.. Defaulting to true");
				enabled = true;
			}else{
				enabled = (boolean)obj.get("enabled");
			}
			
			populateMobs(obj);
			populateArmour(obj);
			populateSpawnEffect(obj);
			populateAttackEffect(obj);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//System.out.println("Loaded all stuff for " + this.name + " and is it enabled? " + this.enabled);
	}
	
	@SuppressWarnings("unchecked")
	private void populateArmour(JSONObject obj){
		
		if (!obj.containsKey("armour")){
			Bukkit.getLogger().info("Sorry but, I found no armour list for the mob.");
			return;
		}
		
		JSONObject armourObj = (JSONObject)obj.get("armour");
		
		for (Iterator<String> i = armourObj.keySet().iterator(); i.hasNext();){
			String key = i.next();
			JSONObject o = (JSONObject) armourObj.get(key);
			
			ArmourWrapper aw = new ArmourWrapper(o, key);
			armour.add(aw);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void populateSpawnEffect(JSONObject obj){
		if (!obj.containsKey("spawn_effects")){
			Bukkit.getLogger().info("Sorry but, I found no spawn effect list for the mob.");
			return;
		}
		
		JSONArray spawnObj = (JSONArray)obj.get("spawn_effects");
		
		for (Iterator<JSONObject> i = spawnObj.iterator(); i.hasNext();){
			
			JSONObject effectObject = i.next();
			
			EffectsWrapper ew = new EffectsWrapper(effectObject);
			spawnEffects.add(ew);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private void populateAttackEffect(JSONObject obj){
		if (!obj.containsKey("attack_effects")){
			Bukkit.getLogger().info("Sorry but, I found no attack effects list for the mob.");
			return;
		}
		
		JSONArray attackObj = (JSONArray)obj.get("attack_effects");
		
		for (Iterator<JSONObject> i = attackObj.iterator(); i.hasNext();){
			JSONObject effectObject = (JSONObject) i.next();
			
			EffectsWrapper ew = new EffectsWrapper(effectObject);
			attackEfects.add(ew);
		}
		
	}
	
	private void populateMobs(JSONObject obj){
		
		if (!obj.containsKey("mobs")){
			Bukkit.getLogger().info("Sorry but, I found no mobs to apply the modifications to.. Defaulting to all");
			applyTo.addAll(Arrays.asList(EntityType.values()));
			return;
		}
		
		JSONArray mobs = (JSONArray)obj.get("mobs");
		
		@SuppressWarnings("unchecked")
		Iterator<String> iterator = mobs.iterator();
		while(iterator.hasNext()){
			String mob = iterator.next();
			if (mob.equalsIgnoreCase("all")){
				applyTo.addAll(Arrays.asList(EntityType.values()));
				break;
			}
			
			EntityType t = EntityType.valueOf(mob.replace(' ', '_').toUpperCase());
			applyTo.add(t);
		}
		
	}

	public File getMyFile() {
		return myFile;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public long getChance() {
		return chance;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public List<EntityType> getApplyTo() {
		return applyTo;
	}

	public List<ArmourWrapper> getArmour() {
		return armour;
	}

	public List<EffectsWrapper> getSpawnEffects() {
		return spawnEffects;
	}

	public List<EffectsWrapper> getAttackEfects() {
		return attackEfects;
	}

	public WeaponWrapper getWeaponWrapper() {
		return weaponWrapper;
	}
	
}
