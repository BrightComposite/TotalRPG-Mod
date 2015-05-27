package aqua.rpgmod.client.render.fx;

import net.minecraft.world.World;
import aqua.rpgmod.fx.AQFxPlacement;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AQEntitySphereFx extends AQEntityFx
{
	protected double speedX = 0;
	protected double speedY = 0;
	protected double speedZ = 0;

	protected double radiusX;
	protected double radiusY;
	
	protected void rotate()
	{
		this.posX = this.base.getX() + Math.sin(this.time * this.speedX) * this.radiusX;
		this.posY = this.base.getY() + Math.sin(this.time * this.speedY) * this.radiusY;
		this.posZ = this.base.getZ() + Math.cos(this.time * this.speedZ) * this.radiusX;
	}
	
	public AQEntitySphereFx(World world, AQFxPlacement base, float colR, float colG, float colB)
	{
		super(world, base, colR, colG, colB, 27, 0.6f);
		
		this.speedX = this.rand.nextDouble();
		this.speedY = this.rand.nextDouble();
		this.speedZ = this.rand.nextDouble();
		
		this.radiusX = this.base.getWidth() + 0.2d * (this.rand.nextDouble() - 0.5d) + 0.2d;
		this.radiusY = this.base.getHeight() + 0.2d * (this.rand.nextDouble() - 0.5d) + 0.2d;
		
		rotate();
	}
}
