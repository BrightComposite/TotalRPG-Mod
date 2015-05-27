package aqua.rpgmod.service.handlers;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.event.world.WorldEvent;

import org.lwjgl.opengl.GL11;

import aqua.rpgmod.AquaMod;
import aqua.rpgmod.client.render.AQGuiOverlayRenderer;
import aqua.rpgmod.client.render.AQRegionRenderer;
import aqua.rpgmod.geometry.AQPoint3D;
import aqua.rpgmod.item.AQItemRegionWand;
import aqua.rpgmod.player.AQPlayerWrapper;
import aqua.rpgmod.player.AQPlayerWrapper.SkillModifier;
import aqua.rpgmod.player.AQThisPlayerWrapper;
import aqua.rpgmod.player.rpg.AQHarvesting;
import aqua.rpgmod.player.update.AQUpdateList;
import aqua.rpgmod.player.update.AQUpdateTimer;
import aqua.rpgmod.world.region.AQProtectionManager;
import aqua.rpgmod.world.region.AQRegionManager;
import aqua.rpgmod.world.region.AQRegionProvider;
import aqua.rpgmod.world.region.AQServerRegionManager;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class AQClientEventHandler
{
	AQUpdateList updaters = new AQUpdateList();
	AQUpdateTimer timer = new AQUpdateTimer(this.updaters, 4);
	AQUpdateTimer render = new AQUpdateTimer(2);
	Random random = new Random();
	
	AQThisPlayerWrapper wrapper = null;
	
	public AQClientEventHandler()
	{
		this.updaters.add(this.render);
	}
	
	@SubscribeEvent
	public void onPlayerJoin(EntityJoinWorldEvent event)
	{
		if(event.entity instanceof EntityPlayer == false)
			return;
		
		AQPlayerWrapper wrapper = AquaMod.proxy.getWrapper((EntityPlayer) event.entity);
		wrapper.initialize();
		
		if(wrapper.isThis())
		{
			Minecraft.getMinecraft().gameSettings.showInventoryAchievementHint = false;
			this.wrapper = (AQThisPlayerWrapper) wrapper;
			this.wrapper.regionSyncronizer.start(false);
			return;
		}
		
		if(event.entity instanceof EntityPlayerMP)
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
		if(event.entity instanceof EntityPlayer)
		{
			AQPlayerWrapper wrapper = AquaMod.proxy.getWrapper((EntityPlayer) event.entity);
			wrapper.reset();
		}
	}
	
	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void onLoad(WorldEvent.Load event)
	{
		if(event.world.isRemote)
			return;

		AQServerRegionManager.instance.loadRegions(event.world);
	}
	
	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void onSave(WorldEvent.Save event)
	{
		if(event.world.isRemote)
			return;
		
		AQServerRegionManager.instance.saveRegions(event.world);
	}
	
	/*
	 * @SubscribeEvent public void onItemInfo(ItemTooltipEvent event) {
	 * event.toolTip.add("\u00a74" + ":("); }
	 */
	
	@SubscribeEvent
	public void onBreakSpeed(PlayerEvent.BreakSpeed event)
	{
		if(event.entityPlayer != this.wrapper.player)
			return;
		
		if(AQProtectionManager.checkBlockProtection(new AQPoint3D(event.x, event.y, event.z), event.entityPlayer))
		{
			event.setCanceled(true);
			return;
		}
		
		ItemStack stack = this.wrapper.player.inventory.getCurrentItem();
		
		if(this.timer.getTick() == 0)
		{
			this.wrapper.stamina.disableRestoring(4);
			this.wrapper.stamina.changeBy(-0.025f);
		}
		
		AQHarvesting.updateToolModifier(stack, event.block, event.metadata);
		event.newSpeed = event.originalSpeed * this.wrapper.getSkillModifier(SkillModifier.BREAK);
		
		if(this.wrapper.stamina.fatigueLevel >= 2)
		{
			event.newSpeed *= this.wrapper.stamina.fatigueModifier(1, 0.5f);
		}
	}
	
	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event)
	{
		if(this.wrapper == null || event.entity != this.wrapper.player)
			return;
		
		if(this.wrapper.player.isDead)
			return;
		
		if(this.wrapper.isFlying && !this.wrapper.player.capabilities.isCreativeMode)
			this.wrapper.isFlying = false;
		
		this.wrapper.player.capabilities.isFlying = this.wrapper.isFlying;
		this.wrapper.player.capabilities.allowFlying = false;
		
		if(this.wrapper.isFlying)
			this.wrapper.player.fallDistance = 0.0f;
		
		this.timer.update();
		this.wrapper.update();
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
	
	@SubscribeEvent
	public void onItemStartUse(PlayerUseItemEvent.Start event)
	{
		if(event.item == null)
			return;
		
		if(event.item.getItem() == Items.potionitem)
		{
			if(!event.entityPlayer.capabilities.isCreativeMode && event.item.getItemDamage() == 0)
			{
				if(!this.wrapper.thirst.needWater())
					event.setCanceled(true);
			}
		}
	}
	
	static ForgeDirection directions[] = new ForgeDirection[]
	{
		ForgeDirection.DOWN, ForgeDirection.UP, ForgeDirection.EAST, ForgeDirection.WEST, ForgeDirection.NORTH, ForgeDirection.SOUTH,
	};
	
	public void tryToDrink()
	{
		if(this.wrapper.player.getCurrentEquippedItem() == null)
		{
			int x = MathHelper.floor_double(this.wrapper.player.posX);
			int y = MathHelper.floor_double(this.wrapper.player.posY - 1);
			int z = MathHelper.floor_double(this.wrapper.player.posZ);
			
			if(this.wrapper.player.worldObj.getBlock(x, y, z) == Blocks.water)
			{
				if(!this.wrapper.player.capabilities.isCreativeMode)
				{
					if(this.wrapper.thirst.needWater())
					{
						this.wrapper.thirst.changeBy(800);
						this.wrapper.player.playSound("random.drink", 0.5F, 1.0f - 0.1f * (this.random.nextFloat()));
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onInteract(PlayerInteractEvent event)
	{
		switch(event.action)
		{
			case LEFT_CLICK_BLOCK:
				ItemStack stack = event.entityPlayer.getCurrentEquippedItem();
				
				if(stack != null && stack.getItem() instanceof AQItemRegionWand)
					event.setCanceled(true);
				
				break;
			case RIGHT_CLICK_AIR:
				tryToDrink();
				break;
			case RIGHT_CLICK_BLOCK:
				if(event.face == -1)
					break;
				
				tryToDrink();
				
				ForgeDirection dir = directions[event.face];
				AQPoint3D offset = new AQPoint3D(dir.offsetX, dir.offsetY, dir.offsetZ);
				
				if(AQProtectionManager.checkBlockProtection(new AQPoint3D(new AQPoint3D(event.x, event.y, event.z).plus(offset)), event.entityPlayer))
					event.setCanceled(true);
				
				break;
			default:
				break;
		}
	}
	
	@SubscribeEvent
	public void onItemFinishUse(PlayerUseItemEvent.Finish event)
	{
		if(event.item == null)
			return;
		
		if(event.item.getItem() == Items.potionitem)
		{
			if(!event.entityPlayer.capabilities.isCreativeMode && event.item.getItemDamage() == 0)
			{
				this.wrapper.thirst.changeBy(8000);
			}
		}
	}
	
	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void onGuiOverlayRender(RenderGameOverlayEvent event)
	{
		int width = event.resolution.getScaledWidth();
		int height = event.resolution.getScaledHeight();
		
		switch(event.type)
		{
			case ARMOR:
			case FOOD:
			case AIR:
			case HEALTH:
			case HEALTHMOUNT:
			case JUMPBAR:
				break;
				
			case HOTBAR:
				AQGuiOverlayRenderer.instance.render(width, height);
			default:
				return;
		}
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		event.setCanceled(true);
	}

	@SubscribeEvent
	public void onTextRender(RenderGameOverlayEvent.Text event)
	{
		event.left.add("");
		
		if(Minecraft.getMinecraft().gameSettings.showDebugInfo)
		{
			for(SkillModifier mod : SkillModifier.values())
				event.left.add(mod.toString().toLowerCase() + ": " + this.wrapper.getSkillModifier(mod));
		}
	}
	
	@SubscribeEvent
	public void onWorldRender(RenderWorldLastEvent event)
	{
		final AQRegionProvider provider = AQRegionManager.getProviderForPlayer(this.wrapper.player);
		final float ticks = event.partialTicks;
		
		AQRegionRenderer.instance.render(this.wrapper.player, provider, ticks);
	}
}
