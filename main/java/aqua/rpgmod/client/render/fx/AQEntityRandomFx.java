package aqua.rpgmod.client.render.fx;

import net.minecraft.world.World;
import aqua.rpgmod.fx.AQFxPlacement;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AQEntityRandomFx extends AQEntityFx
{
	protected double speedX = 0;
	protected double speedY = 0;
	protected double speedZ = 0;
	
	protected double angleX = 0;
	protected double angleY = 0;
	protected double angleZ = 0;
	
	protected double radiusX;
	protected double radiusY;
	
	protected void rotate()
	{
		this.posX = this.base.getX() + Math.sin(this.angleX += this.speedX) * this.radiusX;
		this.posY = this.base.getY() + Math.sin(this.angleY += this.speedY) * this.radiusY;
		this.posZ = this.base.getZ() + Math.cos(this.angleZ += this.speedZ) * this.radiusX;
	}
	
	public AQEntityRandomFx(World world, AQFxPlacement base, float colR, float colG, float colB)
	{
		super(world, base, colR, colG, colB, 16, 0.6f);
		
		this.speedX = this.rand.nextDouble() * 0.25d;
		this.speedY = this.rand.nextDouble() * 0.25d;
		this.speedZ = this.rand.nextDouble() * 0.25d;
		
		this.angleX = this.rand.nextDouble() * 2.0d * Math.PI;
		this.angleY = this.rand.nextDouble() * 2.0d * Math.PI;
		this.angleZ = this.rand.nextDouble() * 2.0d * Math.PI;
		
		this.radiusX = this.rand.nextDouble();
		this.radiusY = this.rand.nextDouble();
		
		rotate();
	}
	
	@Override
	public void onUpdate()
	{
		this.radiusX += 0.05d;
		this.radiusY += 0.05d;

		super.onUpdate();
	}
}
