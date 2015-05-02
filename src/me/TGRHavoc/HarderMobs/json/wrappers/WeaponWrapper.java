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

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONObject;

public class WeaponWrapper {
	
	String name;
	String item;
	int chance = 100;
	
	public WeaponWrapper(JSONObject weaponObject){
		if (!weaponObject.containsKey("item")){
			Bukkit.getLogger().info("Sorry, no item found... Not applying to mob");
			return;
		}
		
		this.item = (String)weaponObject.get("item");
		
		if(!weaponObject.containsKey("name")){
			Bukkit.getLogger().info("Sorry, found no name for the weapon...");
		}else{
			this.name = (String) weaponObject.get("name");
		}
		
		if (!weaponObject.containsKey("chance")){
			Bukkit.getLogger().info("Sorry, found no chance for weapon drop.. Defaulting to 100%");
		}else{
			this.chance = (int)weaponObject.get("chance");
		}
	}
	
	public ItemStack getItemStack(){
		ItemStack stack = null;
		Material myMat = null;
		
		if (item.equalsIgnoreCase("random")){
			myMat = getRandomMaterial();
		}else{
			myMat = Material.getMaterial(item.toUpperCase().replace(' ', '_'));
		}
		
		stack = new ItemStack(myMat);
		
		if (name != null){
			ItemMeta m = stack.getItemMeta();
			m.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
			stack.setItemMeta(m);
		}		
		return stack;
	}
	
	public void applyToEntity(LivingEntity ent){
		if (item == null)
			return;
		ent.getEquipment().setItemInHand(getItemStack());
		ent.getEquipment().setItemInHandDropChance(chance/100);
	}
	
	public Material getRandomMaterial(){
		return Material.values()[new Random().nextInt(Material.values().length)];
	}
	
}
