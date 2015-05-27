package aqua.rpgmod.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class AQHorsePart extends ModelBase
{
	public ModelRenderer body;
	public ModelRenderer tailBase;
	public ModelRenderer tailMiddle;
	public ModelRenderer tailTip;
	public ModelRenderer backLeftLeg;
	public ModelRenderer backLeftShin;
	public ModelRenderer backLeftHoof;
	public ModelRenderer backRightLeg;
	public ModelRenderer backRightShin;
	public ModelRenderer backRightHoof;
	public ModelRenderer frontLeftLeg;
	public ModelRenderer frontLeftShin;
	public ModelRenderer frontLeftHoof;
	public ModelRenderer frontRightLeg;
	public ModelRenderer frontRightShin;
	public ModelRenderer frontRightHoof;
	
	public AQHorsePart()
	{
		this.textureWidth = 128;
		this.textureHeight = 128;
		this.body = new ModelRenderer(this, 0, 34);
		this.body.addBox(-5.0F, -8.0F, -19.0F, 10, 10, 24);
		this.body.setRotationPoint(0.0F, 11.0F, 9.0F);
		this.tailBase = new ModelRenderer(this, 44, 0);
		this.tailBase.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 3);
		this.tailBase.setRotationPoint(0.0F, 3.0F, 14.0F);
		AQHorsePart.setBoxRotation(this.tailBase, -1.134464F, 0.0F, 0.0F);
		this.tailMiddle = new ModelRenderer(this, 38, 7);
		this.tailMiddle.addBox(-1.5F, -2.0F, 3.0F, 3, 4, 7);
		this.tailMiddle.setRotationPoint(0.0F, 3.0F, 14.0F);
		AQHorsePart.setBoxRotation(this.tailMiddle, -1.134464F, 0.0F, 0.0F);
		this.tailTip = new ModelRenderer(this, 24, 3);
		this.tailTip.addBox(-1.5F, -4.5F, 9.0F, 3, 4, 7);
		this.tailTip.setRotationPoint(0.0F, 3.0F, 14.0F);
		AQHorsePart.setBoxRotation(this.tailTip, -1.40215F, 0.0F, 0.0F);
		this.backLeftLeg = new ModelRenderer(this, 78, 29);
		this.backLeftLeg.addBox(-2.5F, -2.0F, -2.5F, 4, 9, 5);
		this.backLeftLeg.setRotationPoint(4.0F, 9.0F, 11.0F);
		this.backLeftShin = new ModelRenderer(this, 78, 43);
		this.backLeftShin.addBox(-2.0F, 0.0F, -1.5F, 3, 5, 3);
		this.backLeftShin.setRotationPoint(4.0F, 16.0F, 11.0F);
		this.backLeftHoof = new ModelRenderer(this, 78, 51);
		this.backLeftHoof.addBox(-2.5F, 5.1F, -2.0F, 4, 3, 4);
		this.backLeftHoof.setRotationPoint(4.0F, 16.0F, 11.0F);
		this.backRightLeg = new ModelRenderer(this, 96, 29);
		this.backRightLeg.addBox(-1.5F, -2.0F, -2.5F, 4, 9, 5);
		this.backRightLeg.setRotationPoint(-4.0F, 9.0F, 11.0F);
		this.backRightShin = new ModelRenderer(this, 96, 43);
		this.backRightShin.addBox(-1.0F, 0.0F, -1.5F, 3, 5, 3);
		this.backRightShin.setRotationPoint(-4.0F, 16.0F, 11.0F);
		this.backRightHoof = new ModelRenderer(this, 96, 51);
		this.backRightHoof.addBox(-1.5F, 5.1F, -2.0F, 4, 3, 4);
		this.backRightHoof.setRotationPoint(-4.0F, 16.0F, 11.0F);
		this.frontLeftLeg = new ModelRenderer(this, 44, 29);
		this.frontLeftLeg.addBox(-1.9F, -1.0F, -2.1F, 3, 8, 4);
		this.frontLeftLeg.setRotationPoint(4.0F, 9.0F, -8.0F);
		this.frontLeftShin = new ModelRenderer(this, 44, 41);
		this.frontLeftShin.addBox(-1.9F, 0.0F, -1.6F, 3, 5, 3);
		this.frontLeftShin.setRotationPoint(4.0F, 16.0F, -8.0F);
		this.frontLeftHoof = new ModelRenderer(this, 44, 51);
		this.frontLeftHoof.addBox(-2.4F, 5.1F, -2.1F, 4, 3, 4);
		this.frontLeftHoof.setRotationPoint(4.0F, 16.0F, -8.0F);
		this.frontRightLeg = new ModelRenderer(this, 60, 29);
		this.frontRightLeg.addBox(-1.1F, -1.0F, -2.1F, 3, 8, 4);
		this.frontRightLeg.setRotationPoint(-4.0F, 9.0F, -8.0F);
		this.frontRightShin = new ModelRenderer(this, 60, 41);
		this.frontRightShin.addBox(-1.1F, 0.0F, -1.6F, 3, 5, 3);
		this.frontRightShin.setRotationPoint(-4.0F, 16.0F, -8.0F);
		this.frontRightHoof = new ModelRenderer(this, 60, 51);
		this.frontRightHoof.addBox(-1.6F, 5.1F, -2.1F, 4, 3, 4);
		this.frontRightHoof.setRotationPoint(-4.0F, 16.0F, -8.0F);
	}
	
	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	public void render(float p_78088_7_)
	{
		this.backLeftLeg.render(p_78088_7_);
		this.backLeftShin.render(p_78088_7_);
		this.backLeftHoof.render(p_78088_7_);
		this.backRightLeg.render(p_78088_7_);
		this.backRightShin.render(p_78088_7_);
		this.backRightHoof.render(p_78088_7_);
		this.frontLeftLeg.render(p_78088_7_);
		this.frontLeftShin.render(p_78088_7_);
		this.frontLeftHoof.render(p_78088_7_);
		this.frontRightLeg.render(p_78088_7_);
		this.frontRightShin.render(p_78088_7_);
		this.frontRightHoof.render(p_78088_7_);
		
		this.body.render(p_78088_7_);
		this.tailBase.render(p_78088_7_);
		this.tailMiddle.render(p_78088_7_);
		this.tailTip.render(p_78088_7_);
	}
	
	/**
	 * Sets the rotations for a ModelRenderer in the ModelHorse class.
	 */
	private static void setBoxRotation(ModelRenderer p_110682_1_, float p_110682_2_, float p_110682_3_, float p_110682_4_)
	{
		p_110682_1_.rotateAngleX = p_110682_2_;
		p_110682_1_.rotateAngleY = p_110682_3_;
		p_110682_1_.rotateAngleZ = p_110682_4_;
	}
	
	/**
	 * Fixes and offsets a rotation in the ModelHorse class.
	 */
	private static float updateHorseRotation(float p_110683_1_, float p_110683_2_, float p_110683_3_)
	{
		float f3;
		
		for(f3 = p_110683_2_ - p_110683_1_; f3 < -180.0F; f3 += 360.0F)
		{}
		for(; f3 >= 180.0F; f3 -= 360.0F)
		{}
		
		return p_110683_1_ + p_110683_3_ * f3;
	}
	
	/**
	 * Used for easily adding entity-dependent animations. The second and third
	 * float params here are the same second and third as in the
	 * setRotationAngles method.
	 */
	@Override
	public void setLivingAnimations(EntityLivingBase p_78086_1_, float p_78086_2_, float p_78086_3_, float p_78086_4_)
	{
		float f3 = p_78086_1_ == null ? 0 : AQHorsePart.updateHorseRotation(p_78086_1_.prevRenderYawOffset, p_78086_1_.renderYawOffset, p_78086_4_);
		float f6 = -f3;
		if(f6 > 20.0F)
		{
			f6 = 20.0F;
		}
		
		if(f6 < -20.0F)
		{
			f6 = -20.0F;
		}
		
		if(p_78086_3_ > 0.2F)
		{}
		
		float f9 = 0.0f;
		float f10 = 1.0F - f9;
		float f12 = p_78086_1_ == null ? 0 : p_78086_1_.ticksExisted + p_78086_4_;
		float f13 = MathHelper.cos(p_78086_2_ * 0.6662F + (float) Math.PI);
		float f14 = f13 * 0.8F * p_78086_3_;
		this.tailBase.rotationPointY = 3.0F;
		this.tailMiddle.rotationPointZ = 14.0F;
		this.body.rotateAngleX = 0.0F;
		this.tailBase.rotationPointY = f9 * 9.0F + f10 * this.tailBase.rotationPointY;
		this.tailMiddle.rotationPointZ = f9 * 18.0F + f10 * this.tailMiddle.rotationPointZ;
		this.body.rotateAngleX = f9 * -((float) Math.PI / 4F) + f10 * this.body.rotateAngleX;
		float f15 = ((float) Math.PI / 2F);
		float f18 = 0.2617994F * f9;
		float f19 = MathHelper.cos(f12 * 0.6F + (float) Math.PI);
		this.frontLeftLeg.rotationPointY = -2.0F * f9 + 9.0F * f10;
		this.frontLeftLeg.rotationPointZ = -2.0F * f9 + -8.0F * f10;
		this.frontRightLeg.rotationPointY = this.frontLeftLeg.rotationPointY;
		this.frontRightLeg.rotationPointZ = this.frontLeftLeg.rotationPointZ;
		this.backLeftShin.rotationPointY = this.backLeftLeg.rotationPointY + MathHelper.sin(((float) Math.PI / 2F) + f18 + f10 * -f13 * 0.5F * p_78086_3_)
			* 7.0F;
		this.backLeftShin.rotationPointZ = this.backLeftLeg.rotationPointZ + MathHelper.cos(((float) Math.PI * 3F / 2F) + f18 + f10 * -f13 * 0.5F * p_78086_3_)
			* 7.0F;
		this.backRightShin.rotationPointY = this.backRightLeg.rotationPointY + MathHelper.sin(((float) Math.PI / 2F) + f18 + f10 * f13 * 0.5F * p_78086_3_)
			* 7.0F;
		this.backRightShin.rotationPointZ = this.backRightLeg.rotationPointZ
			+ MathHelper.cos(((float) Math.PI * 3F / 2F) + f18 + f10 * f13 * 0.5F * p_78086_3_) * 7.0F;
		float f20 = (-1.0471976F + f19) * f9 + f14 * f10;
		float f21 = (-1.0471976F + -f19) * f9 + -f14 * f10;
		this.frontLeftShin.rotationPointY = this.frontLeftLeg.rotationPointY + MathHelper.sin(((float) Math.PI / 2F) + f20) * 7.0F;
		this.frontLeftShin.rotationPointZ = this.frontLeftLeg.rotationPointZ + MathHelper.cos(((float) Math.PI * 3F / 2F) + f20) * 7.0F;
		this.frontRightShin.rotationPointY = this.frontRightLeg.rotationPointY + MathHelper.sin(((float) Math.PI / 2F) + f21) * 7.0F;
		this.frontRightShin.rotationPointZ = this.frontRightLeg.rotationPointZ + MathHelper.cos(((float) Math.PI * 3F / 2F) + f21) * 7.0F;
		this.backLeftLeg.rotateAngleX = f18 + -f13 * 0.5F * p_78086_3_ * f10;
		this.backLeftShin.rotateAngleX = -0.08726646F * f9 + (-f13 * 0.5F * p_78086_3_ - Math.max(0.0F, f13 * 0.5F * p_78086_3_)) * f10;
		this.backLeftHoof.rotateAngleX = this.backLeftShin.rotateAngleX;
		this.backRightLeg.rotateAngleX = f18 + f13 * 0.5F * p_78086_3_ * f10;
		this.backRightShin.rotateAngleX = -0.08726646F * f9 + (f13 * 0.5F * p_78086_3_ - Math.max(0.0F, -f13 * 0.5F * p_78086_3_)) * f10;
		this.backRightHoof.rotateAngleX = this.backRightShin.rotateAngleX;
		this.frontLeftLeg.rotateAngleX = f20;
		this.frontLeftShin.rotateAngleX = (this.frontLeftLeg.rotateAngleX + (float) Math.PI * Math.max(0.0F, 0.2F + f19 * 0.2F)) * f9
			+ (f14 + Math.max(0.0F, f13 * 0.5F * p_78086_3_)) * f10;
		this.frontLeftHoof.rotateAngleX = this.frontLeftShin.rotateAngleX;
		this.frontRightLeg.rotateAngleX = f21;
		this.frontRightShin.rotateAngleX = (this.frontRightLeg.rotateAngleX + (float) Math.PI * Math.max(0.0F, 0.2F - f19 * 0.2F)) * f9
			+ (-f14 + Math.max(0.0F, -f13 * 0.5F * p_78086_3_)) * f10;
		this.frontRightHoof.rotateAngleX = this.frontRightShin.rotateAngleX;
		this.backLeftHoof.rotationPointY = this.backLeftShin.rotationPointY;
		this.backLeftHoof.rotationPointZ = this.backLeftShin.rotationPointZ;
		this.backRightHoof.rotationPointY = this.backRightShin.rotationPointY;
		this.backRightHoof.rotationPointZ = this.backRightShin.rotationPointZ;
		this.frontLeftHoof.rotationPointY = this.frontLeftShin.rotationPointY;
		this.frontLeftHoof.rotationPointZ = this.frontLeftShin.rotationPointZ;
		this.frontRightHoof.rotationPointY = this.frontRightShin.rotationPointY;
		this.frontRightHoof.rotationPointZ = this.frontRightShin.rotationPointZ;
		
		f15 = -1.3089F + p_78086_3_ * 1.5F;
		
		if(f15 > 0.0F)
		{
			f15 = 0.0F;
		}
		
		// if (flag)
		// {
		// this.tailBase.rotateAngleY = MathHelper.cos(f12 * 0.7F);
		// f15 = 0.0F;
		// }
		// else
		// {
		this.tailBase.rotateAngleY = 0.0F;
		// }
		
		this.tailMiddle.rotateAngleY = this.tailBase.rotateAngleY;
		this.tailTip.rotateAngleY = this.tailBase.rotateAngleY;
		this.tailMiddle.rotationPointY = this.tailBase.rotationPointY;
		this.tailTip.rotationPointY = this.tailBase.rotationPointY;
		this.tailMiddle.rotationPointZ = this.tailBase.rotationPointZ;
		this.tailTip.rotationPointZ = this.tailBase.rotationPointZ;
		this.tailBase.rotateAngleX = f15;
		this.tailMiddle.rotateAngleX = f15;
		this.tailTip.rotateAngleX = -0.2618F + f15;
	}
}
