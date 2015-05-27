package aqua.rpgmod.client.render.fx;

import net.minecraft.world.World;
import aqua.rpgmod.fx.AQFxPlacement;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AQEntityRingFx extends AQEntityFx
{
	protected double angle = 0;
	protected double radius;
	
	protected void rotate()
	{
		this.posX = this.base.getX() + Math.sin(this.angle + this.time) * this.radius;
		this.posY = this.base.getY() + (1.0f + Math.sin(this.time)) * this.base.getHeight() * 0.5f;
		this.posZ = this.base.getZ() + Math.cos(this.angle + this.time) * this.radius;
	}
	
	public AQEntityRingFx(World world, AQFxPlacement base, double angle, float colR, float colG, float colB)
	{
		super(world, base, colR, colG, colB, 16, 0.6f);
		
		this.angle = angle;
		this.radius = this.base.getWidth() + 0.2d * (this.rand.nextDouble() - 0.5d) + 0.2d;
		
		rotate();
	}
}
