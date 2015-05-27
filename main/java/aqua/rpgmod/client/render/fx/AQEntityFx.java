package aqua.rpgmod.client.render.fx;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;
import aqua.rpgmod.fx.AQFxPlacement;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
abstract public class AQEntityFx extends EntityFX
{
	private float baseParticleScale;
	protected AQFxPlacement base;
	protected double time = 0;
	protected double offsetY = 0.4;
	
	abstract protected void rotate();
	
	public AQEntityFx(World world, AQFxPlacement base, float colR, float colG, float colB, int maxAge, float colorRandomness)
	{
		super(world, base.getX(), base.getY(), base.getZ());
		
		this.base = base;

		float f = this.rand.nextFloat() * colorRandomness + (1 - colorRandomness);
		this.baseParticleScale = this.particleScale = this.rand.nextFloat() * 0.5F + 2.0F;
		
		this.particleRed = colR * f;
		this.particleGreen = colG * f;
		this.particleBlue = colB * f;
		this.particleAlpha = 1.0f;
		
		this.noClip = true;
		this.particleMaxAge = maxAge + (int) (this.rand.nextFloat() * maxAge);
		
		this.setParticleTextureIndex(0);
	}
	
	@Override
	public void renderParticle(Tessellator par1Tessellator, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		float f6 = 1.2f + this.rand.nextFloat() * 0.4f;
		this.particleScale = this.baseParticleScale * f6;
		super.renderParticle(par1Tessellator, par2, par3, par4, par5, par6, par7);
	}
	
	@Override
	public int getBrightnessForRender(float par1)
	{
		return 15728880;
	}
	
	@Override
	public float getBrightness(float par1)
	{
        return 1.0F;
	}
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		this.time += 0.25d;
		rotate();
	}
}
