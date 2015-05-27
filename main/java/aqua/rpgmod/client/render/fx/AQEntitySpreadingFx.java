package aqua.rpgmod.client.render.fx;

import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import aqua.rpgmod.fx.AQFxPlacement;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AQEntitySpreadingFx extends AQEntityFx
{
	protected Vec3 vec;
	
	protected double radiusX;
	protected double radiusY;
	
	protected void rotate() {}
	
	public AQEntitySpreadingFx(World world, AQFxPlacement base, float colR, float colG, float colB)
	{
		super(world, base, colR, colG, colB, 27, 0.0f);
		
		this.vec = Vec3.createVectorHelper(this.rand.nextDouble() * 2 - 1, this.rand.nextDouble() * 2 - 1, this.rand.nextDouble() * 2 - 1);
		this.vec.normalize();
		
		this.posX = this.base.getX() + this.vec.xCoord * this.radiusX;
		this.posY = this.base.getY() + this.vec.yCoord * this.radiusY;
		this.posZ = this.base.getZ() + this.vec.zCoord * this.radiusX;
		
		this.motionX = this.vec.xCoord * 0.2;
		this.motionY = this.vec.yCoord * 0.2;
		this.motionZ = this.vec.zCoord * 0.2;
		
		this.particleGravity = 0.2f;
	}
	
	@Override
	public void onUpdate()
	{
		this.particleAlpha = (float)(this.particleMaxAge - this.particleAge) / (float)this.particleMaxAge;
		super.onUpdate();
	}
}
