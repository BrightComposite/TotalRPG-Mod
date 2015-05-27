package aqua.rpgmod.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import api.player.model.ModelPlayer;
import aqua.rpgmod.AquaMod;
import aqua.rpgmod.character.AQRace;
import aqua.rpgmod.client.render.AQTextureManager;
import aqua.rpgmod.client.render.AQTextureMap;
import aqua.rpgmod.client.render.AQTextureMap.TextureType;
import aqua.rpgmod.player.AQPlayerWrapper;

public class AQModelPlayer
{
	public final String name;
	public final AQRace race;
	public ModelBiped model = new ModelBiped();
	public static String skinType = "skin";
	
	public AQModelPlayer(String name, AQRace race)
	{
		this.name = name;
		this.race = race;
	}
	
	public void setModel(ModelPlayer model)
	{
		model.bipedHead = this.model.bipedHead;
		model.bipedBody = this.model.bipedBody;
		model.bipedRightArm = this.model.bipedRightArm;
		model.bipedLeftArm = this.model.bipedLeftArm;
		model.bipedRightLeg = this.model.bipedRightLeg;
		model.bipedLeftLeg = this.model.bipedLeftLeg;
		model.bipedHeadwear = this.model.bipedHeadwear;
	}
	
	@SuppressWarnings("static-method")
	public void render(EntityPlayer player, ModelBiped model, float f, AQTextureMap textures, int type)
	{
		if(type == 0)
			AQTextureManager.bindTexture(player, TextureType.SKIN, textures);
		
		model.bipedHead.render(f);
		model.bipedBody.render(f);
		model.bipedRightArm.render(f);
		model.bipedLeftArm.render(f);
		model.bipedRightLeg.render(f);
		model.bipedLeftLeg.render(f);
		
		if(type == 0)
			model.bipedHeadwear.render(f);
	}
	
	public static void setSwimAnimation(AQPlayerWrapper wrapper, float delta)
	{
		if(wrapper.isSwimming)
		{
			float x = Minecraft.getMinecraft().thePlayer == wrapper.player ? (float) (4.0 * (Math.PI * (wrapper.player.motionY + 0.02))) : 0.0f;
			float angle = (float) (Math.min(Math.max(Math.PI / 2.0 - x, 0.0), Math.PI));
			
			if(wrapper.swimAngle > angle + delta)
			{
				wrapper.swimAngle -= delta * (wrapper.swimAngle - angle);
			}
			else if(wrapper.swimAngle < angle - delta)
			{
				wrapper.swimAngle += delta * (angle - wrapper.swimAngle);
			}
			else
			{
				wrapper.swimAngle = angle;
			}
			
			return;
		}
		
		wrapper.swimAngle = Math.max(wrapper.swimAngle - delta * 5, 0.0f);
	}
	
	@SuppressWarnings(
	{
		"static-method", "unused"
	})
	protected float legsMultiplierZ(ModelBiped model)
	{
		return 0.7f;
	}
	
	@SuppressWarnings(
	{
		"static-method", "unused"
	})
	protected float legsMultiplierY(ModelBiped model)
	{
		return 0.75f;
	}
	
	@SuppressWarnings(
	{
		"unused"
	})
	public void setRotationAngles(ModelBiped model, float p1, float p2, float p3, float p4, float p5, float p6, Entity entity, int type)
	{
		model.bipedHead.rotateAngleY = p4 / (180F / (float) Math.PI);
		model.bipedHead.rotateAngleX = p5 / (180F / (float) Math.PI);
		model.bipedHeadwear.rotateAngleY = model.bipedHead.rotateAngleY;
		model.bipedHeadwear.rotateAngleX = model.bipedHead.rotateAngleX;
		model.bipedBody.rotateAngleX = 0.0F;
		
		model.bipedRightArm.rotateAngleX = MathHelper.cos(p1 * 0.6662F + (float) Math.PI) * 2.0F * p2 * 0.5F;
		model.bipedLeftArm.rotateAngleX = MathHelper.cos(p1 * 0.6662F) * 2.0F * p2 * 0.5F;
		model.bipedRightArm.rotateAngleZ = 0.0F;
		model.bipedLeftArm.rotateAngleZ = 0.0F;
		model.bipedRightLeg.rotateAngleX = MathHelper.cos(p1 * 0.6662F) * 1.4F * p2;
		model.bipedLeftLeg.rotateAngleX = MathHelper.cos(p1 * 0.6662F + (float) Math.PI) * 1.4F * p2;
		model.bipedRightLeg.rotateAngleY = 0.0F;
		model.bipedLeftLeg.rotateAngleY = 0.0F;
		model.bipedRightArm.rotationPointY = 2.0F;
		model.bipedLeftArm.rotationPointY = 2.0F;
		
		model.bipedRightArm.offsetZ = 0.0f;
		model.bipedRightArm.offsetY = 0.0f;
		model.bipedRightArm.offsetX = 0.0f;
		
		model.bipedLeftArm.offsetZ = 0.0f;
		model.bipedLeftArm.offsetY = 0.0f;
		model.bipedLeftArm.offsetX = 0.0f;
		
		AQPlayerWrapper wrapper = entity == null ? null : AquaMod.proxy.getWrapper((EntityPlayer) entity);
		
		boolean flag = wrapper != null && !wrapper.renderInMenu();
		
		if(flag && !model.isRiding && wrapper != null && (wrapper.isSwimming || wrapper.swimAngle > 0.0f))
		{
			float f = p3 * 0.32f;
			
			model.isSneak = false;
			setSwimAnimation(wrapper, wrapper.lastAngle > p3 ? 0 : (p3 - wrapper.lastAngle) * 0.1f);
			wrapper.lastAngle = p3;
			
			model.bipedHead.rotateAngleX = (float) Math.min(Math.max(model.bipedHead.rotateAngleX, wrapper.swimAngle - Math.PI / 2), wrapper.swimAngle
				+ Math.PI / 3);
			
			model.bipedHeadwear.rotateAngleX = model.bipedHead.rotateAngleX;
			
			model.bipedRightArm.offsetY = -0.15f * MathHelper.sin(wrapper.swimAngle / 2.0f);
			model.bipedLeftArm.offsetY = model.bipedRightArm.offsetY;
			
			model.bipedRightLeg.offsetZ = legsMultiplierZ(model) * MathHelper.sin(wrapper.swimAngle);
			model.bipedLeftLeg.offsetZ = model.bipedRightLeg.offsetZ;
			
			model.bipedRightLeg.offsetY = legsMultiplierY(model) * (MathHelper.cos(wrapper.swimAngle) - 1);
			model.bipedLeftLeg.offsetY = model.bipedRightLeg.offsetY;
			
			if(wrapper.isSwimming)
			{
				model.bipedBody.rotateAngleX = wrapper.swimAngle;
				
				model.bipedRightArm.rotateAngleX = (float) Math.PI / 2.0f;
				model.bipedLeftArm.rotateAngleX = model.bipedRightArm.rotateAngleX;
				
				model.bipedRightArm.rotateAngleY = -MathHelper.sin(f * 0.6662F + (float) Math.PI) * 8.0F * 0.25f * 0.5F - (float) (3.0f * Math.PI / 4.0f);
				model.bipedLeftArm.rotateAngleY = -model.bipedRightArm.rotateAngleY;
				
				model.bipedRightArm.rotateAngleZ = (-1.5f - MathHelper.sin(f * 0.6662F - (float) Math.PI)) * (float) Math.PI / 6;
				model.bipedLeftArm.rotateAngleZ = -model.bipedRightArm.rotateAngleZ;
				
				model.bipedRightLeg.rotateAngleX = wrapper.swimAngle;
				model.bipedLeftLeg.rotateAngleX = wrapper.swimAngle;
				
				model.bipedRightLeg.rotateAngleY = MathHelper.abs(MathHelper.cos((f + p6) * 0.6662F) + 1.0f) * 0.7F * 0.25f - (float) (Math.PI / 8.0);
				model.bipedLeftLeg.rotateAngleY = -model.bipedRightLeg.rotateAngleY;
				
				return;
			}
			
			model.bipedBody.rotateAngleX += wrapper.swimAngle;
			model.bipedRightArm.rotateAngleX += wrapper.swimAngle;
			model.bipedLeftArm.rotateAngleX += wrapper.swimAngle;
			model.bipedRightLeg.rotateAngleX += wrapper.swimAngle;
			model.bipedLeftLeg.rotateAngleX += wrapper.swimAngle;
		}
		else
		{
			if(wrapper != null)
				wrapper.lastAngle = Float.MAX_VALUE;
			
			model.bipedRightArm.offsetY = 0.0f;
			model.bipedLeftArm.offsetY = 0.0f;
			
			model.bipedRightLeg.offsetY = 0.0f;
			model.bipedLeftLeg.offsetY = 0.0f;
			
			model.bipedRightLeg.offsetZ = 0.0f;
			model.bipedLeftLeg.offsetZ = 0.0f;
		}
		
		if(flag && model.isRiding)
		{
			model.bipedRightArm.rotateAngleX += -((float) Math.PI / 5F);
			model.bipedLeftArm.rotateAngleX += -((float) Math.PI / 5F);
			model.bipedRightLeg.rotateAngleX = -((float) Math.PI * 2F / 5F);
			model.bipedLeftLeg.rotateAngleX = -((float) Math.PI * 2F / 5F);
			model.bipedRightLeg.rotateAngleY = ((float) Math.PI / 10F);
			model.bipedLeftLeg.rotateAngleY = -((float) Math.PI / 10F);
		}
		
		if(model.heldItemLeft != 0)
		{
			model.bipedLeftArm.rotateAngleX = model.bipedLeftArm.rotateAngleX * 0.5F - ((float) Math.PI / 10F) * model.heldItemLeft;
		}
		
		if(model.heldItemRight != 0)
		{
			model.bipedRightArm.rotateAngleX = model.bipedRightArm.rotateAngleX * 0.5F - ((float) Math.PI / 10F) * model.heldItemRight;
		}
		
		model.bipedRightArm.rotateAngleY = 0.0F;
		model.bipedLeftArm.rotateAngleY = 0.0F;
		float f6;
		float f7;
		
		if(wrapper == null || model.onGround > -9990.0F)
		{
			f6 = model.onGround;
			model.bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(f6) * (float) Math.PI * 2.0F) * 0.2F;
			model.bipedRightArm.rotationPointZ = MathHelper.sin(model.bipedBody.rotateAngleY) * 5.0F;
			model.bipedRightArm.rotationPointX = -MathHelper.cos(model.bipedBody.rotateAngleY) * 5.0F;
			model.bipedLeftArm.rotationPointZ = -MathHelper.sin(model.bipedBody.rotateAngleY) * 5.0F;
			model.bipedLeftArm.rotationPointX = MathHelper.cos(model.bipedBody.rotateAngleY) * 5.0F;
			model.bipedRightArm.rotateAngleY += model.bipedBody.rotateAngleY;
			model.bipedLeftArm.rotateAngleY += model.bipedBody.rotateAngleY;
			model.bipedLeftArm.rotateAngleX += model.bipedBody.rotateAngleY;
			f6 = 1.0F - model.onGround;
			f6 *= f6;
			f6 *= f6;
			f6 = 1.0F - f6;
			f7 = MathHelper.sin(f6 * (float) Math.PI);
			float f8 = MathHelper.sin(model.onGround * (float) Math.PI) * -(model.bipedHead.rotateAngleX - 0.7F) * 0.75F;
			model.bipedRightArm.rotateAngleX = (float) (model.bipedRightArm.rotateAngleX - (f7 * 1.2D + f8));
			model.bipedRightArm.rotateAngleY += model.bipedBody.rotateAngleY * 2.0F;
			model.bipedRightArm.rotateAngleZ = MathHelper.sin(model.onGround * (float) Math.PI) * -0.4F;
		}
		
		if(flag && model.isSneak)
		{
			model.bipedBody.rotateAngleX += 0.5F;
			model.bipedRightArm.rotateAngleX += 0.4F;
			model.bipedLeftArm.rotateAngleX += 0.4F;
			model.bipedRightLeg.rotationPointZ = 4.0F;
			model.bipedLeftLeg.rotationPointZ = 4.0F;
			model.bipedRightLeg.rotationPointY = 9.0F;
			model.bipedLeftLeg.rotationPointY = 9.0F;
			model.bipedHead.rotationPointY = 1.0F;
			model.bipedHeadwear.rotationPointY = 1.0F;
		}
		else
		{
			model.bipedRightLeg.rotationPointZ = 0.1F;
			model.bipedLeftLeg.rotationPointZ = 0.1F;
			model.bipedRightLeg.rotationPointY = 12.0F;
			model.bipedLeftLeg.rotationPointY = 12.0F;
			model.bipedHead.rotationPointY = 0.0F;
			model.bipedHeadwear.rotationPointY = 0.0F;
		}
		
		model.bipedRightArm.rotateAngleZ += MathHelper.cos(p3 * 0.09F) * 0.05F + 0.05F;
		model.bipedLeftArm.rotateAngleZ -= MathHelper.cos(p3 * 0.09F) * 0.05F + 0.05F;
		model.bipedRightArm.rotateAngleX += MathHelper.sin(p3 * 0.067F) * 0.05F;
		model.bipedLeftArm.rotateAngleX -= MathHelper.sin(p3 * 0.067F) * 0.05F;
		
		if(flag && model.aimedBow)
		{
			f6 = 0.0F;
			f7 = 0.0F;
			model.bipedRightArm.rotateAngleZ = 0.0F;
			model.bipedLeftArm.rotateAngleZ = 0.0F;
			model.bipedRightArm.rotateAngleY = -(0.1F - f6 * 0.6F) + model.bipedHead.rotateAngleY;
			model.bipedLeftArm.rotateAngleY = 0.1F - f6 * 0.6F + model.bipedHead.rotateAngleY + 0.4F;
			model.bipedRightArm.rotateAngleX = -((float) Math.PI / 2F) + model.bipedHead.rotateAngleX;
			model.bipedLeftArm.rotateAngleX = -((float) Math.PI / 2F) + model.bipedHead.rotateAngleX;
			model.bipedRightArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
			model.bipedLeftArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
			model.bipedRightArm.rotateAngleZ += MathHelper.cos(p3 * 0.09F) * 0.05F + 0.05F;
			model.bipedLeftArm.rotateAngleZ -= MathHelper.cos(p3 * 0.09F) * 0.05F + 0.05F;
			model.bipedRightArm.rotateAngleX += MathHelper.sin(p3 * 0.067F) * 0.05F;
			model.bipedLeftArm.rotateAngleX -= MathHelper.sin(p3 * 0.067F) * 0.05F;
		}
	}
}
