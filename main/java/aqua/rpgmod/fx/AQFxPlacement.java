package aqua.rpgmod.fx;

import net.minecraft.entity.Entity;

public interface AQFxPlacement
{
	abstract double getX();
	
	abstract double getY();
	
	abstract double getZ();
	
	abstract double getWidth();
	
	abstract double getHeight();
	
	public static class EntityPlacement implements AQFxPlacement
	{
		protected Entity base;
		
		public EntityPlacement(Entity base)
		{
			this.base = base;
		}
		
		@Override
		public double getX()
		{
			return this.base.posX;
		}
		
		@Override
		public double getY()
		{
			return this.base.posY;
		}
		
		@Override
		public double getZ()
		{
			return this.base.posZ;
		}
		
		@Override
		public double getWidth()
		{
			return this.base.width;
		}
		
		@Override
		public double getHeight()
		{
			return this.base.height;
		}
		
	}
	
	public class StaticPlacement implements AQFxPlacement
	{
		protected double x;
		protected double y;
		protected double z;
		protected double width;
		protected double height;
		
		public StaticPlacement(double x, double y, double z, double width, double height)
		{
			this.x = x;
			this.y = y;
			this.z = z;
			this.width = width;
			this.height = height;
		}
		
		public StaticPlacement(Entity base)
		{
			this.x = base.posX;
			this.y = base.posY;
			this.z = base.posZ;
			this.width = base.width;
			this.height = base.height;
		}
		
		@Override
		public double getX()
		{
			return this.x;
		}
		
		@Override
		public double getY()
		{
			return this.y;
		}
		
		@Override
		public double getZ()
		{
			return this.z;
		}
		
		@Override
		public double getWidth()
		{
			return this.width;
		}
		
		@Override
		public double getHeight()
		{
			return this.height;
		}
	}
}
