package aqua.rpgmod.service.handlers;

import java.util.Random;

import aqua.rpgmod.AquaMod;
import aqua.rpgmod.player.AQPlayerWrapper;
import aqua.rpgmod.player.AQPlayerWrapper.SkillModifier;
import aqua.rpgmod.player.rpg.AQArchery;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class AQBowEventHandler
{
	Random random = new Random();
	
	@SubscribeEvent
	public void onSpawn(EntityJoinWorldEvent event)
	{
		if(event.entity instanceof EntityArrow)
		{
			EntityArrow arrow = (EntityArrow) event.entity;
			
			if(arrow.shootingEntity instanceof EntityPlayer)
			{
				AQPlayerWrapper<EntityPlayer> wrapper = AquaMod.proxy.getWrapper((EntityPlayer) arrow.shootingEntity);
				
				Vec3 vector = wrapper.player.getLookVec();
				
				float scatterMod = wrapper.isMoving() ? 0.24f : 0.08f;
				float scatter = wrapper.getSkillModifier(SkillModifier.ARCHER_ACCURACY) * scatterMod * (float) Math.PI / 9.0f;
				
				if(scatter != 0.0f)
				{
					vector.rotateAroundY(scatter * (this.random.nextFloat() * 2.0f - 1.0f));
					vector.rotateAroundX(scatter * (this.random.nextFloat() * 2.0f - 1.0f));
					vector.rotateAroundZ(scatter * (this.random.nextFloat() * 2.0f - 1.0f));
					
					vector = vector.normalize();
				}
				
				Vec3 current = Vec3.createVectorHelper(arrow.motionX, arrow.motionY, arrow.motionZ);
				double dist = wrapper.getSkillModifier(SkillModifier.ARCHER_TENSION) * current.lengthVector() * 0.5;
				
				arrow.motionX = vector.xCoord * dist;
				arrow.motionY = vector.yCoord * dist;
				arrow.motionZ = vector.zCoord * dist;
			}
		}
	}
	
	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void onBowUse(ArrowNockEvent event)
	{
		if(event.entityPlayer.capabilities.isCreativeMode || event.entityPlayer.inventory.hasItem(Items.arrow))
		{
			AQArchery.updateModifiers();
			AQPlayerWrapper<EntityPlayer> wrapper = AquaMod.proxy.getWrapper(event.entityPlayer);
			float time = wrapper.getSkillModifier(SkillModifier.ARCHER_RELOAD_TIME);
			int duration = event.result.getMaxItemUseDuration();
			event.entityPlayer.setItemInUse(event.result, (int) (duration * time));
		}
		
		event.setCanceled(true);
	}
}
