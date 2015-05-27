package aqua.rpgmod.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelSpider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;

import aqua.rpgmod.character.AQRace.Arachnoid;
import aqua.rpgmod.client.render.AQTextureManager;
import aqua.rpgmod.client.render.AQTextureMap;
import aqua.rpgmod.client.render.AQTextureMap.TextureType;

public class AQModelArachnoid extends AQModelPlayer
{
	public ModelSpider spiderPart = new ModelSpider();
	
	public AQModelArachnoid()
	{
		super("arachnoid", Arachnoid.instance());
		this.spiderPart.spiderLeg7.rotationPointX = -3.0f;
		this.spiderPart.spiderLeg8.rotationPointX = 3.0f;
		this.spiderPart.spiderLeg7.rotationPointZ = -3.0f;
		this.spiderPart.spiderLeg8.rotationPointZ = -3.0f;
	}
	
	@Override
	public void render(EntityPlayer player, ModelBiped model, float f, AQTextureMap textures, int type)
	{
		if(type == 0)
			AQTextureManager.bindTexture(player, TextureType.SKIN, textures);
		
		model.bipedHead.render(f);
		
		GL11.glPushMatrix();
		GL11.glScaled(1.2, 1.2, 1.0);
		model.bipedBody.render(f);
		GL11.glPopMatrix();
		
		model.bipedRightArm.rotationPointX = -6.0f;
		model.bipedRightArm.render(f);
		
		model.bipedLeftArm.rotationPointX = 6.0f;
		model.bipedLeftArm.render(f);
		
		if(type == 0)
		{
			model.bipedHeadwear.render(f);
			AQTextureManager.bindTexture(player, TextureType.SPIDER, textures);
			GL11.glPushMatrix();
			
			if(player == null)
				GL11.glTranslated(0.0, 3.0, 0.0);
			
			GL11.glPushMatrix();
			
			if(player == null)
			{
				GL11.glTranslated(0.0, 0.0, -7.5);
				GL11.glScaled(1.0, 1.0, 1.8);
			}
			else
			{
				GL11.glTranslated(0.0, 0.0, -0.5);
				GL11.glScaled(1.0, 1.0, 1.5);
			}
			
			this.spiderPart.spiderBody.render(f);
			GL11.glPopMatrix();
			
			this.spiderPart.spiderLeg1.render(f);
			this.spiderPart.spiderLeg2.render(f);
			this.spiderPart.spiderLeg3.render(f);
			this.spiderPart.spiderLeg4.render(f);
			this.spiderPart.spiderLeg5.render(f);
			this.spiderPart.spiderLeg6.render(f);
			this.spiderPart.spiderLeg7.render(f);
			this.spiderPart.spiderLeg8.render(f);
			GL11.glPopMatrix();
		}
	}
	
	@Override
	public void setRotationAngles(ModelBiped model, float p1, float p2, float p3, float p4, float p5, float p6, Entity entity, int type)
	{
		model.bipedHead.rotateAngleY = p4 / (180F / (float) Math.PI);
		model.bipedHead.rotateAngleX = p5 / (180F / (float) Math.PI);
		model.bipedHeadwear.rotateAngleY = model.bipedHead.rotateAngleY;
		model.bipedHeadwear.rotateAngleX = model.bipedHead.rotateAngleX;
		model.bipedRightArm.rotateAngleX = 0.0F;
		model.bipedLeftArm.rotateAngleX = 0.0F;
		model.bipedRightArm.rotateAngleZ = 0.0F;
		model.bipedLeftArm.rotateAngleZ = 0.0F;
		
		model.bipedRightArm.rotationPointY = 2.0f;
		model.bipedLeftArm.rotationPointY = 2.0f;
		
		model.bipedRightArm.offsetZ = 0.0f;
		model.bipedRightArm.offsetY = 0.0f;
		model.bipedRightArm.offsetX = 0.0f;
		
		model.bipedLeftArm.offsetZ = 0.0f;
		model.bipedLeftArm.offsetY = 0.0f;
		model.bipedLeftArm.offsetX = 0.0f;
		
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
		
		if(model.onGround > -9990.0F)
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
		
		if(model.isSneak)
		{
			model.bipedBody.rotateAngleX = 0.0F;
			model.bipedHead.rotationPointY = 1.0F;
			model.bipedHeadwear.rotationPointY = 1.0F;
		}
		else
		{
			model.bipedBody.rotateAngleX = 0.0F;
			model.bipedHead.rotationPointY = 0.0F;
			model.bipedHeadwear.rotationPointY = 0.0F;
		}
		
		model.bipedRightArm.rotateAngleZ += MathHelper.cos(p3 * 0.09F) * 0.05F + 0.05F;
		model.bipedLeftArm.rotateAngleZ -= MathHelper.cos(p3 * 0.09F) * 0.05F + 0.05F;
		model.bipedRightArm.rotateAngleX += MathHelper.sin(p3 * 0.067F) * 0.05F;
		model.bipedLeftArm.rotateAngleX -= MathHelper.sin(p3 * 0.067F) * 0.05F;
		
		if(model.aimedBow)
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
		
		if(type != 0)
			return;
		
		f6 = ((float) Math.PI / 4F);
		this.spiderPart.spiderLeg1.rotateAngleZ = -f6;
		this.spiderPart.spiderLeg2.rotateAngleZ = f6;
		this.spiderPart.spiderLeg3.rotateAngleZ = -f6 * 0.74F;
		this.spiderPart.spiderLeg4.rotateAngleZ = f6 * 0.74F;
		this.spiderPart.spiderLeg5.rotateAngleZ = -f6 * 0.74F;
		this.spiderPart.spiderLeg6.rotateAngleZ = f6 * 0.74F;
		this.spiderPart.spiderLeg7.rotateAngleZ = -f6;
		this.spiderPart.spiderLeg8.rotateAngleZ = f6;
		f7 = -0.0F;
		
		float f8 = 0.3926991F;
		this.spiderPart.spiderLeg1.rotateAngleY = f8 * 2.0F + f7;
		this.spiderPart.spiderLeg2.rotateAngleY = -f8 * 2.0F - f7;
		this.spiderPart.spiderLeg3.rotateAngleY = f8 * 1.0F + f7;
		this.spiderPart.spiderLeg4.rotateAngleY = -f8 * 1.0F - f7;
		this.spiderPart.spiderLeg5.rotateAngleY = -f8 * 1.0F + f7;
		this.spiderPart.spiderLeg6.rotateAngleY = f8 * 1.0F - f7;
		this.spiderPart.spiderLeg7.rotateAngleY = -f8 * 2.0F + f7;
		this.spiderPart.spiderLeg8.rotateAngleY = f8 * 2.0F - f7;
		float f9 = -(MathHelper.cos(p1 * 0.6662F * 2.0F + 0.0F) * 0.4F) * p2;
		float f10 = -(MathHelper.cos(p1 * 0.6662F * 2.0F + (float) Math.PI) * 0.4F) * p2;
		float f11 = -(MathHelper.cos(p1 * 0.6662F * 2.0F + ((float) Math.PI / 2F)) * 0.4F) * p2;
		float f12 = -(MathHelper.cos(p1 * 0.6662F * 2.0F + ((float) Math.PI * 3F / 2F)) * 0.4F) * p2;
		float f13 = Math.abs(MathHelper.sin(p1 * 0.6662F + 0.0F) * 0.4F) * p2;
		float f14 = Math.abs(MathHelper.sin(p1 * 0.6662F + (float) Math.PI) * 0.4F) * p2;
		float f15 = Math.abs(MathHelper.sin(p1 * 0.6662F + ((float) Math.PI / 2F)) * 0.4F) * p2;
		float f16 = Math.abs(MathHelper.sin(p1 * 0.6662F + ((float) Math.PI * 3F / 2F)) * 0.4F) * p2;
		this.spiderPart.spiderLeg1.rotateAngleY += f9;
		this.spiderPart.spiderLeg2.rotateAngleY += -f9;
		this.spiderPart.spiderLeg3.rotateAngleY += f10;
		this.spiderPart.spiderLeg4.rotateAngleY += -f10;
		this.spiderPart.spiderLeg5.rotateAngleY += f11;
		this.spiderPart.spiderLeg6.rotateAngleY += -f11;
		this.spiderPart.spiderLeg7.rotateAngleY += f12;
		this.spiderPart.spiderLeg8.rotateAngleY += -f12;
		this.spiderPart.spiderLeg1.rotateAngleZ += f13;
		this.spiderPart.spiderLeg2.rotateAngleZ += -f13;
		this.spiderPart.spiderLeg3.rotateAngleZ += f14;
		this.spiderPart.spiderLeg4.rotateAngleZ += -f14;
		this.spiderPart.spiderLeg5.rotateAngleZ += f15;
		this.spiderPart.spiderLeg6.rotateAngleZ += -f15;
		this.spiderPart.spiderLeg7.rotateAngleZ += f16;
		this.spiderPart.spiderLeg8.rotateAngleZ += -f16;
	}
}
