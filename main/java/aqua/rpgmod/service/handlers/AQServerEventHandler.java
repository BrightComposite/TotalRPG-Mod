package aqua.rpgmod.service.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.world.WorldEvent;
import aqua.rpgmod.AquaMod;
import aqua.rpgmod.geometry.AQPoint3D;
import aqua.rpgmod.item.AQItemRegionWand;
import aqua.rpgmod.player.AQPlayerWrapper;
import aqua.rpgmod.player.AQPlayerWrapper.SkillModifier;
import aqua.rpgmod.service.network.AQMessage;
import aqua.rpgmod.service.network.AQMessage.PlayerMessage;
import aqua.rpgmod.world.region.AQProtectionManager;
import aqua.rpgmod.world.region.AQServerRegionManager;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class AQServerEventHandler
{
	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void onPlayerJoin(EntityJoinWorldEvent event)
	{
		if(event.entity instanceof EntityPlayer == false)
			return;
		
		AQPlayerWrapper wrapper = AquaMod.proxy.getWrapper((EntityPlayer) event.entity);
		
		wrapper.initialize();
		wrapper.regionSyncronizer.start(true);
	}
	
	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void onPlayerConstructing(EntityConstructing event)
	{
		if(event.entity instanceof EntityPlayer == false)
			return;
		
		AquaMod.proxy.createWrapper((EntityPlayer) event.entity);
	}
	
	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void onDeath(LivingDeathEvent event)
	{
		if(event.entity instanceof EntityPlayerMP)
		{
			AQPlayerWrapper wrapper = AquaMod.proxy.getWrapper((EntityPlayer) event.entity);
			wrapper.reset();
			AquaMod.playerNetwork.sendTo(new AQMessage(PlayerMessage.DEATH), (EntityPlayerMP) event.entity);
		}
	}
	
	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void onLoad(WorldEvent.Load event)
	{
		AQServerRegionManager.instance.loadRegions(event.world);
	}
	
	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void onSave(WorldEvent.Save event)
	{
		AQServerRegionManager.instance.saveRegions(event.world);
	}
	
	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void onInteract(PlayerInteractEvent event)
	{
		if(event.action == Action.LEFT_CLICK_BLOCK)
		{
			ItemStack stack = event.entityPlayer.getCurrentEquippedItem();
			
			if(stack != null && stack.getItem() instanceof AQItemRegionWand)
			{
				event.setCanceled(true);
			}
		}
	}
	
	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void onBreakSpeed(PlayerEvent.BreakSpeed event)
	{
		if(AQProtectionManager.checkBlockProtection(new AQPoint3D(event.x, event.y, event.z), event.entityPlayer))
		{
			event.setCanceled(true);
			return;
		}
		
		EntityPlayer player = event.entityPlayer;
		AQPlayerWrapper wrapper = AquaMod.proxy.getWrapper(player);
		event.newSpeed = event.originalSpeed * wrapper.getSkillModifier(SkillModifier.BREAK);
		
		if(wrapper.stamina.fatigueLevel >= 2)
		{
			event.newSpeed *= wrapper.stamina.fatigueModifier(1, 0.5f);
		}
	}
	
	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void onFall(LivingFallEvent event)
	{
		if(event.entity instanceof EntityPlayer == false)
			return;
		
		AQPlayerWrapper wrapper = AquaMod.proxy.getWrapper((EntityPlayer) event.entity);
		
		if(wrapper.getRace() != null && (wrapper.getRace().slowFalling() || wrapper.getRace().canClimpUpTheWall()))
		{
			double dist = wrapper.player.isOnLadder() ? 0 : Math.max(wrapper.safeY - wrapper.getPlayerMinY(), 0);
			
			if(dist < event.distance)
				event.distance = (float) dist;
		}
	}
	
}
