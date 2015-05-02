package me.TGRHavoc.HarderMobs;

import java.util.Random;

import me.TGRHavoc.HarderMobs.json.wrappers.ArmourWrapper;
import me.TGRHavoc.HarderMobs.json.wrappers.EffectsWrapper;
import me.TGRHavoc.HarderMobs.json.wrappers.JsonWrapper;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class MyEventHandler implements Listener{
	
	Main plugin;
	
	public MyEventHandler(Main plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onEntitySpawnEvent(CreatureSpawnEvent e){
		if (!plugin.isEnabledP())
			return;
		if (e.getEntity() instanceof Player)
			return; // Entity is player, no need to apply effects?
		LivingEntity ent = e.getEntity();
		
		//System.out.println("Creature spanwed...");
		
		for (JsonWrapper mod: plugin.getMods()){
			int randChance = new Random().nextInt(100);
			
			if(randChance >= mod.getChance()){
				continue;
			}
			
			//System.out.println("randChance = " + randChance + ", modChance = " + mod.getChance());
			//System.out.println("Appplying mod: " + mod.getName() + " to " + ent.getType());
			if (!mod.isEnabled())
				continue;
			if (!mod.getApplyTo().contains(ent.getType()))
				continue;
			
			//Apply armour to mob
			for (ArmourWrapper armour : mod.getArmour()){
				armour.applyToEntity(ent);
			}
			
			//If they have a weapon they want to apply, apply it
			if (mod.getWeaponWrapper() != null){
				mod.getWeaponWrapper().applyToEntity(ent);
			}
			
			//Apply spawn effects
			for (EffectsWrapper spawnEffect : mod.getSpawnEffects()){
				if (new Random().nextInt(101) <= spawnEffect.getChance()){
					ent.addPotionEffect(spawnEffect.getEffect());
				}
			}
		}
	}
	
	@EventHandler
	public void onEntityAttackPlayer(EntityDamageByEntityEvent e){
		if (!plugin.isEnabledP())
			return;
		if (!(e.getEntity() instanceof Player))
			return; //I don't need to do anything. Just leave this event
		
		Player attacked = (Player)e.getEntity();
		Entity attacker = e.getDamager();
		
		for (JsonWrapper mod: plugin.getMods()){
			if (!mod.isEnabled())
				continue;
			if (mod.getApplyTo().contains(attacker.getType())){ //If they want to apply this effect from the mob
				for (EffectsWrapper attackEffect : mod.getAttackEfects()){ //Get the effects
					if( (new Random().nextInt(101)) <= attackEffect.getChance()){
						attacked.addPotionEffect(attackEffect.getEffect());
					}
				}
			}
		}
		
	}
	
}
