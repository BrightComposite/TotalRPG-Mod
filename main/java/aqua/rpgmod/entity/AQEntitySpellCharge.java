package aqua.rpgmod.entity;

import aqua.rpgmod.fx.AQFxPlacement;
import aqua.rpgmod.fx.AQSpreadingFxSystem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class AQEntitySpellCharge extends EntityThrowable
{
	public AQEntitySpellCharge(World world)
	{
		super(world);
	}
	
	public AQEntitySpellCharge(World world, EntityLivingBase entity)
	{
		super(world, entity);
	}
	
	public AQEntitySpellCharge(World world, double x, double y, double z)
	{
      	super(world, x, y, z);
	}
	
	@Override
	protected void onImpact(MovingObjectPosition pos)
	{
		
		
		this.setDead();
	}
	
	@Override
	protected float getGravityVelocity()
	{
		return 0.0f;
	}
	
	@Override
	public void onEntityUpdate()
	{
		if(this.worldObj.isRemote)
			AQSpreadingFxSystem.instance.spawnFx(this.worldObj, new AQFxPlacement.StaticPlacement(this.posX, this.posY, this.posZ, 1.0f, 1.0f), new float[]{1.0f, 1.0f, 0.0f});
		
		super.onEntityUpdate();
		
		if(this.ticksExisted > 120)
			setDead();
	}
}
