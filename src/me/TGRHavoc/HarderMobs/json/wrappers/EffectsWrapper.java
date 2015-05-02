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

import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.json.simple.JSONObject;

public class EffectsWrapper {
	
	String type;
	long duration = Integer.MAX_VALUE;
	long amplifier = 1;
	
	//For attack effects
	long chance = 100;
	
	public EffectsWrapper(JSONObject effectObject) {
		if (!effectObject.containsKey("type")){
			Bukkit.getLogger().info("Sorry, effect doesn't have a type.. Not applying");
			return;
		}
		
		this.type = (String)effectObject.get("type");
		
		if (effectObject.containsKey("duration")){
			this.duration = (long)effectObject.get("duration");
		}
		
		if (effectObject.containsKey("amplifier")){
			this.amplifier = (long) effectObject.get("amplifier");
		}
		
		if (effectObject.containsKey("chance")){
			this.chance = (long) effectObject.get("chance");
		}
	}
	
	public PotionEffect getEffect(){
		if (type == null)
			return new PotionEffect(PotionEffectType.ABSORPTION, 10, 1, false);
		
		PotionEffect effect = new PotionEffect(effectFromString(type), (int)(duration * 20), (int)amplifier, true);
		return effect;
	}
	
	public PotionEffectType effectFromString(String type){
		if (type == null)
			return PotionEffectType.CONFUSION;
		
		for (PotionEffectType effectType : PotionEffectType.values()){
			if (effectType == null || effectType.getName() == null){
				//System.out.println("Something is null.. " + (effectType == null ? "effectType" : "getName()"));
				continue;
			}
			String effectName = effectType.getName().toLowerCase();
			
			if (effectName.equalsIgnoreCase(type.replace(' ', '_'))){
				return effectType;
			}
		}
		
		return PotionEffectType.CONFUSION;
	}

	public String getType() {
		return type;
	}

	public long getDuration() {
		return duration;
	}

	public long getAmplifier() {
		return amplifier;
	}

	public long getChance() {
		return chance;
	}
	
}
