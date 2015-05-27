package aqua.rpgmod.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.opengl.GL11;

import aqua.rpgmod.AquaMod;
import aqua.rpgmod.character.AQRace.Elf;
import aqua.rpgmod.client.render.AQTextureManager;
import aqua.rpgmod.client.render.AQTextureMap;
import aqua.rpgmod.client.render.AQTextureMap.TextureType;
import aqua.rpgmod.player.AQPlayerWrapper;

public class AQModelElf extends AQModelPlayer
{
	TexturedQuad ear = null;
	
	public AQModelElf()
	{
		super("Elf", Elf.instance());
		
		PositionTextureVertex positiontexturevertex3 = new PositionTextureVertex(-4.0f, 0.0f, -1.0f, 0.0F, 0.0F);
		PositionTextureVertex positiontexturevertex4 = new PositionTextureVertex(4.0f, 0.0f, -1.0f, 0.0F, 4.0F);
		PositionTextureVertex positiontexturevertex5 = new PositionTextureVertex(4.0f, 8.0f, -1.0f, 4.0F, 4.0F);
		PositionTextureVertex positiontexturevertex6 = new PositionTextureVertex(-4.0f, 8.0f, -1.0f, 4.0F, 0.0F);
		
		this.ear = new TexturedQuad(new PositionTextureVertex[]
		{
			positiontexturevertex3, positiontexturevertex4, positiontexturevertex5, positiontexturevertex6
		}, 0, 0, 8, 8, 8, 8);
	}
	
	@Override
	protected float legsMultiplierY(ModelBiped model)
	{
		return model.isSneak ? 0.6f : 0.7f;
	}
	
	@Override
	protected float legsMultiplierZ(ModelBiped model)
	{
		return model.isSneak ? 0.6f : 0.7f;
	}
	
	@Override
	public void render(EntityPlayer player, ModelBiped model, float f, AQTextureMap textures, int type)
	{
		if(type == 0)
			AQTextureManager.bindTexture(player, TextureType.SKIN, textures);
		
		AQPlayerWrapper wrapper = player == null ? null : AquaMod.proxy.getWrapper(player);
		
		double c = wrapper == null ? 1.0f : Math.abs(Math.cos(wrapper.swimAngle));
		double s = wrapper == null ? 0.0f : Math.abs(Math.sin(wrapper.swimAngle));
		
		GL11.glPushMatrix();
		
		GL11.glPushMatrix();
		model.bipedHead.render(f);
		model.bipedHeadwear.render(f);
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		GL11.glScaled(0.9, 0.9 + c * 0.25, 0.9 + s * 0.25);
		model.bipedBody.render(f);
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		double armSin = wrapper == null ? 1.0f : Math.abs(Math.sin(model.bipedRightArm.rotateAngleZ));
		
		GL11.glScaled(0.9, 0.9 + c * 0.2, 0.9 + s * 0.2);
		
		float armsDX = 0.1f;
		float armsDY = 0.0f;
		
		if(player != null)
		{
			model.bipedRightArm.rotationPointX = -3.5f;
			model.bipedLeftArm.rotationPointX = 3.5f;
		}
		
		model.bipedRightArm.offsetX -= armsDX;
		model.bipedRightArm.offsetY += armsDY;
		model.bipedRightArm.offsetZ -= 0.1f * armSin;
		model.bipedRightArm.render(f);
		
		model.bipedLeftArm.offsetX += armsDX;
		model.bipedLeftArm.offsetY += armsDY;
		model.bipedLeftArm.offsetZ -= 0.1f * armSin;
		model.bipedLeftArm.render(f);
		
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		GL11.glScaled(0.9, 0.9 + c * 0.25, 0.9 + s * 0.25);
		
		model.bipedRightLeg.render(f);
		model.bipedLeftLeg.render(f);
		
		GL11.glPopMatrix();
		
		if(type == 0)
		{
			GL11.glPushMatrix();
			GL11.glRotatef(model.bipedHead.rotateAngleZ * (180F / (float) Math.PI), 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(model.bipedHead.rotateAngleY * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(model.bipedHead.rotateAngleX * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);
			
			GL11.glTranslated(model.bipedHead.rotationPointX, 0.05 * model.bipedHead.rotationPointY - 0.35, model.bipedHead.rotationPointZ);
			GL11.glScaled(0.5, 0.5, -1.0);
			
			AQTextureManager.bindTexture(player, TextureType.EAR, textures);
			
			GL11.glCullFace(GL11.GL_FRONT);
			drawEar(player, f);
			GL11.glCullFace(GL11.GL_BACK);
			
			GL11.glScaled(-1.0, 1.0, 1.0);
			drawEar(player, f);
			GL11.glPopMatrix();
		}
		
		GL11.glPopMatrix();
	}
	
	@Override
	public void setRotationAngles(ModelBiped model, float p1, float p2, float p3, float p4, float p5, float p6, Entity entity, int type)
	{
		super.setRotationAngles(model, p1, p2, p3, p4, p5, p6, entity, type);
		
		if(model.isSneak)
		{
			model.bipedRightLeg.rotationPointY = 11;
			model.bipedLeftLeg.rotationPointY = 11;
		}
	}
	
	protected void drawEar(EntityPlayer player, float f)
	{
		GL11.glPushMatrix();
		
		if(player == null)
			GL11.glTranslated(10.0, -10, 0.0);
		else
			GL11.glTranslated(0.6, 0.0, 0.0);
		
		GL11.glRotatef(45.0f, 0.0F, 1.0F, 0.0F);
		this.ear.draw(Tessellator.instance, f);
		GL11.glPopMatrix();
	}
}
