package aqua.rpgmod.client.model.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import aqua.rpgmod.block.AQBlockBonfire;

public class AQModelBonfire extends ModelBase
{
	ModelRenderer stick1;
	ModelRenderer stick2;
	ModelRenderer stick3;
	ModelRenderer stick4;
	ModelRenderer stick5;
	ModelRenderer stick6;
	ModelRenderer stone1;
	ModelRenderer stone2;
	ModelRenderer stone3;
	ModelRenderer stone4;
	ModelRenderer stone5;
	ModelRenderer stone6;
	ModelRenderer stone7;
	ModelRenderer stone8;
	
	public AQModelBonfire()
	{
		this.textureWidth = 8;
		this.textureHeight = 8;
		
		this.stick1 = new ModelRenderer(this, 0, 0);
		this.stick1.addBox(-0.5F, 1F, -1F, 1, 5, 1);
		this.stick1.setRotationPoint(0F, -0.5F, 0F);
		this.stick1.setTextureSize(16, 16);
		this.stick1.mirror = true;
		setRotation(this.stick1, -0.7853982F, -3.141593F, 0F);
		this.stick2 = new ModelRenderer(this, 0, 0);
		this.stick2.addBox(-0.5F, 1F, -1F, 1, 5, 1);
		this.stick2.setRotationPoint(0F, -0.5F, 0F);
		this.stick2.setTextureSize(16, 16);
		this.stick2.mirror = true;
		setRotation(this.stick2, -0.7853982F, 2.094395F, 0F);
		this.stick3 = new ModelRenderer(this, 0, 0);
		this.stick3.addBox(-0.5F, 1F, -1F, 1, 5, 1);
		this.stick3.setRotationPoint(0F, -0.5F, 0F);
		this.stick3.setTextureSize(16, 16);
		this.stick3.mirror = true;
		setRotation(this.stick3, -0.7853982F, 1.047198F, 0F);
		this.stick4 = new ModelRenderer(this, 0, 0);
		this.stick4.addBox(-0.5F, 1F, -1F, 1, 5, 1);
		this.stick4.setRotationPoint(0F, -0.5F, 0F);
		this.stick4.setTextureSize(16, 16);
		this.stick4.mirror = true;
		setRotation(this.stick4, -0.7853982F, 0F, 0F);
		this.stick5 = new ModelRenderer(this, 0, 0);
		this.stick5.addBox(-0.5F, 1F, -1F, 1, 5, 1);
		this.stick5.setRotationPoint(0F, -0.5F, 0F);
		this.stick5.setTextureSize(16, 16);
		this.stick5.mirror = true;
		setRotation(this.stick5, -0.7853982F, -1.047198F, 0F);
		this.stick6 = new ModelRenderer(this, 0, 0);
		this.stick6.addBox(-0.5F, 1F, -1F, 1, 5, 1);
		this.stick6.setRotationPoint(0F, -0.5F, 0F);
		this.stick6.setTextureSize(16, 16);
		this.stick6.mirror = true;
		setRotation(this.stick6, -0.7853982F, -2.094395F, 0F);
		this.stone1 = new ModelRenderer(this, 0, 0);
		this.stone1.addBox(0F, 0F, 0F, 2, 2, 4);
		this.stone1.setRotationPoint(2F, 2F, 4.3F);
		this.stone1.setTextureSize(16, 16);
		this.stone1.mirror = true;
		setRotation(this.stone1, 0F, -1.570796F, 0F);
		this.stone2 = new ModelRenderer(this, 4, 0);
		this.stone2.addBox(0F, 0F, 0F, 2, 2, 2);
		this.stone2.setRotationPoint(-2.5F, 2F, 4F);
		this.stone2.setTextureSize(16, 16);
		this.stone2.mirror = true;
		setRotation(this.stone2, 0F, -2.356194F, 0F);
		this.stone3 = new ModelRenderer(this, 0, 0);
		this.stone3.addBox(0F, 0F, 0F, 2, 2, 3);
		this.stone3.setRotationPoint(-4.3F, 2F, 1.5F);
		this.stone3.setTextureSize(16, 16);
		this.stone3.mirror = true;
		setRotation(this.stone3, 0F, 3.141593F, 0F);
		this.stone4 = new ModelRenderer(this, 0, 0);
		this.stone4.addBox(0F, 0F, 0F, 2, 2, 3);
		this.stone4.setRotationPoint(-4F, 2F, -2F);
		this.stone4.setTextureSize(16, 16);
		this.stone4.mirror = true;
		setRotation(this.stone4, 0F, 2.356194F, 0F);
		this.stone5 = new ModelRenderer(this, 0, 0);
		this.stone5.addBox(0F, 0F, 0F, 2, 2, 2);
		this.stone5.setRotationPoint(-1F, 2F, -4.3F);
		this.stone5.setTextureSize(16, 16);
		this.stone5.mirror = true;
		setRotation(this.stone5, 0F, 1.570796F, 0F);
		this.stone6 = new ModelRenderer(this, 0, 0);
		this.stone6.addBox(0F, 0F, 0F, 2, 2, 4);
		this.stone6.setRotationPoint(1.5F, 2F, -4.5F);
		this.stone6.setTextureSize(16, 16);
		this.stone6.mirror = true;
		setRotation(this.stone6, 0F, 0.7853982F, 0F);
		this.stone7 = new ModelRenderer(this, 0, 0);
		this.stone7.addBox(0F, 0F, 0F, 2, 2, 2);
		this.stone7.setRotationPoint(4.3F, 2F, -1F);
		this.stone7.setTextureSize(16, 16);
		this.stone7.mirror = true;
		setRotation(this.stone7, 0F, 0F, 0F);
		this.stone8 = new ModelRenderer(this, 0, 0);
		this.stone8.addBox(0F, 0F, 0F, 2, 2, 2);
		this.stone8.setRotationPoint(4F, 2F, 2.5F);
		this.stone8.setTextureSize(16, 16);
		this.stone8.mirror = true;
		setRotation(this.stone8, 0F, -0.7853982F, 0F);
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		
		Minecraft.getMinecraft().renderEngine.bindTexture(AQBlockBonfire.stoneTex);
		
		this.stone1.render(f5);
		this.stone2.render(f5);
		this.stone3.render(f5);
		this.stone4.render(f5);
		this.stone5.render(f5);
		this.stone6.render(f5);
		this.stone7.render(f5);
		this.stone8.render(f5);
		
		Minecraft.getMinecraft().renderEngine.bindTexture(AQBlockBonfire.stickTex);
		
		this.stick1.render(f5);
		this.stick2.render(f5);
		this.stick3.render(f5);
		this.stick4.render(f5);
		this.stick5.render(f5);
		this.stick6.render(f5);
	}
	
	private static void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
