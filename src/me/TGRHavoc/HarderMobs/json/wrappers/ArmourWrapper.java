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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.json.simple.JSONObject;

public class ArmourWrapper {
	
	//All possible armour types
	static List<Material> materials = new ArrayList<Material>();{
		materials.add(Material.LEATHER_BOOTS);
		materials.add(Material.LEATHER_CHESTPLATE);
		materials.add(Material.LEATHER_HELMET);
		materials.add(Material.LEATHER_LEGGINGS);
		
		materials.add(Material.CHAINMAIL_BOOTS);
		materials.add(Material.CHAINMAIL_CHESTPLATE);
		materials.add(Material.CHAINMAIL_HELMET);
		materials.add(Material.CHAINMAIL_LEGGINGS);
		
		materials.add(Material.IRON_BOOTS);
		materials.add(Material.IRON_CHESTPLATE);
		materials.add(Material.IRON_HELMET);
		materials.add(Material.IRON_LEGGINGS);
		
		materials.add(Material.GOLD_BOOTS);
		materials.add(Material.GOLD_CHESTPLATE);
		materials.add(Material.GOLD_HELMET);
		materials.add(Material.GOLD_LEGGINGS);
		
		materials.add(Material.DIAMOND_BOOTS);
		materials.add(Material.DIAMOND_CHESTPLATE);
		materials.add(Material.DIAMOND_HELMET);
		materials.add(Material.DIAMOND_LEGGINGS);
	}
	
	String position;
	String name;
	String item;
	String colorName;
	
	long chance = 100;
	
	public ArmourWrapper(JSONObject obj, String pos){
		
		if (!obj.containsKey("item")){
			Bukkit.getLogger().info("Sorry, no item found for armour... Not applying to mobs");
			return;
		}
		this.item = (String)obj.get("item");
		
		if (!obj.containsKey("name")){
			Bukkit.getLogger().info("Sorry, no name given for armour.. Defaulting to item type");
			//name = item;
		}else{
			this.name = ChatColor.translateAlternateColorCodes('&', (String)obj.get("name"));
		}
		
		this.position = pos;
		
		if (!obj.containsKey("drop_chance")){
			Bukkit.getLogger().info("Couldn't find drop chance for weapon " + ChatColor.translateAlternateColorCodes('&', name) + " defaulting to 100%");
		}else{
			this.chance = (long)obj.get("drop_chance");
		}
		
		if (obj.containsKey("color"))
			this.colorName = (String)obj.get("color");
		else
			this.colorName = "brown";
	}
	
	public ItemStack getItemStack(){
		ItemStack stack = null;
		Material myMat = Material.LEATHER_CHESTPLATE;
		
		if (item.equalsIgnoreCase("random")){
			myMat = getRandomArmourMaterial();
		}else{
			//System.out.println("MyMaterial is " + item);
			myMat = Material.valueOf(item.replace(' ', '_').toUpperCase());
		}
		stack = new ItemStack(myMat);
		
		if ( this.name != null ){
			if (myMat.name().toLowerCase().contains("leather")){
				LeatherArmorMeta m = (LeatherArmorMeta) stack.getItemMeta();
				m.setColor(getColorFromString(colorName));
				stack.setItemMeta(m);
			}
			
			ItemMeta m = stack.getItemMeta();
			m.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
			stack.setItemMeta(m);
		}
		
		return stack;
	}
	
	public void applyToEntity(LivingEntity e){
		if (this.item == null)
			return;
		
		if (position.equalsIgnoreCase("helmet")){
			e.getEquipment().setHelmet(getItemStack());
			e.getEquipment().setHelmetDropChance((float)(chance/100));
			
		}else if(position.equalsIgnoreCase("chestplate")){
			e.getEquipment().setChestplate(getItemStack());
			e.getEquipment().setChestplateDropChance((float)(chance/100));
			
		}else if(position.equalsIgnoreCase("leggins")){
			e.getEquipment().setLeggings(getItemStack());
			e.getEquipment().setLeggingsDropChance((float)(chance/100));
			
		}else if(position.equalsIgnoreCase("boots")){
			e.getEquipment().setBoots(getItemStack());
			e.getEquipment().setBootsDropChance((float)(chance/100));
			
		}else{
			return;
		}
		
		//System.out.println("Set drop chance for " + this.name + " to " + (chance/100));
	}
	
	public Material getRandomArmourMaterial(){
		return materials.get(new Random().nextInt(materials.size()));
	}
	
	public Color getColorFromString(String color){
		if (color.equalsIgnoreCase("aqua")){
			return Color.AQUA;
		}else if(color.equalsIgnoreCase("black")){
			return Color.BLACK;
		}else if(color.equalsIgnoreCase("blue")){
			return Color.BLUE;
		}else if(color.equalsIgnoreCase("fuchsia")){
			return Color.FUCHSIA;
		}else if(color.equalsIgnoreCase("gray") || color.equalsIgnoreCase("grey")){
			return Color.GRAY;
		}else if(color.equalsIgnoreCase("green")){
			return Color.GREEN;
		}else if(color.equalsIgnoreCase("lime")){
			return Color.LIME;
		}else if(color.equalsIgnoreCase("maroon")){
			return Color.MAROON;
		}else if(color.equalsIgnoreCase("navy")){
			return Color.NAVY;
		}else if(color.equalsIgnoreCase("olive")){
			return Color.OLIVE;
		}else if(color.equalsIgnoreCase("orange")){
			return Color.ORANGE;
		}else if(color.equalsIgnoreCase("purple")){
			return Color.PURPLE;
		}else if(color.equalsIgnoreCase("red")){
			return Color.RED;
		}else if(color.equalsIgnoreCase("silver")){
			return Color.SILVER;
		}else if(color.equalsIgnoreCase("teal")){
			return Color.TEAL;
		}else if(color.equalsIgnoreCase("white")){
			return Color.WHITE;
		}else if(color.equalsIgnoreCase("yellow")){
			return Color.YELLOW;
		}
		
		return Color.MAROON; //Need to return something, make maroon the default colour
	}

	@Override
	public String toString() {
		return "ArmourWrapper [position=" + position + ", name=" + name
				+ ", item=" + item + ", colorName=" + colorName + ", chance="
				+ chance + "]";
	}
	
}
