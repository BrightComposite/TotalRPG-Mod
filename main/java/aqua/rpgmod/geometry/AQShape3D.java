package aqua.rpgmod.geometry;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;

abstract public class AQShape3D
{
	public static enum Type
	{
		BOX
	}
	
	public final Type type;
	
	public AQShape3D(Type type)
	{
		this.type = type;
	}
	
	public static AQShape3D create(Type type, AQPoint3D pos, AQDimensions dim)
	{
		switch(type)
		{
			case BOX:
				return new AQBox(pos, dim);
				
			default:
				return null;
		}
	}
	
	@Override
	abstract public AQShape3D clone();
	
	abstract public void set(AQPoint3D pos, AQDimensions dim);
	
	abstract public void set(AQPoint3D first, AQPoint3D second);
	
	abstract public AQInterval<Integer> getInterval(AQAxis3D axis);
	
	abstract public void setInterval(AQAxis3D axis, AQInterval<Integer> value);
	
	abstract public AQPoint3D pos();
	
	abstract public AQDimensions dimensions();
	
	abstract public int getSide(AQSide3D side);
	
	public AQQuad getSideQuad(AQSide3D side, double offset)
	{
		return new AQQuad(new AQPoint3Dd(pos().plus(-offset)), new AQPoint3Dd(pos().plus(dimensions()).plus(offset)), side);
	}
	
	public AQPoint3D getCenter()
	{
		return new AQPoint3D(pos().plus(dimensions().plus(-1).mul(0.5)));
	}
	
	abstract public int dimension(AQAxis3D axis);
	
	abstract public void extendBy(AQShape3D shape);
	
	public int getMaxDimension()
	{
		int max = 0;
		
		for(AQAxis3D axis : AQAxis3D.values())
		{
			int value = dimension(axis);
			
			if(value > max)
				max = value;
		}
		
		return max;
	}
	
	public int getMinDimension()
	{
		int min = 0;
		
		for(AQAxis3D axis : AQAxis3D.values())
		{
			int value = dimension(axis);
			
			if(value < min)
				min = value;
		}
		
		return min;
	}
	
	abstract public double radius();
	
	public double approximateDistanceTo(AQPoint3D p)
	{
		if(contains(p))
			return 0.0f;
		
		return Math.max(0.0f, getCenter().distanceTo(p) - radius());
	}
	
	abstract public double distanceTo(AQPoint3D p);
	
	public double distanceTo(AQPoint3Dd p)
	{
		return distanceTo(new AQPoint3D(p));
	}
	
	public boolean contains(AQPoint3D p)
	{
		return contains(p.x, p.y, p.z);
	}
	
	public boolean contains(AQPoint3Dd p)
	{
		return contains(p.x, p.y, p.z);
	}
	
	public boolean contains(Entity entity)
	{
		return contains(entity.posX, entity.posY, entity.posZ);
	}
	
	abstract public boolean contains(double x, double y, double z);
	
	public static ByteBuf serialiaze(ByteBuf buffer, AQShape3D shape)
	{
		buffer.writeInt(shape.type.ordinal());
		shape.pos().serialiaze(buffer);
		shape.dimensions().serialiaze(buffer);
		
		return buffer;
	}
	
	public static AQShape3D deserialiaze(ByteBuf buffer)
	{
		Type type = Type.values()[buffer.readInt()];
		
		AQPoint3D pos = new AQPoint3D();
		pos.deserialiaze(buffer);
		
		AQDimensions dim = new AQDimensions();
		dim.deserialiaze(buffer);
		
		return create(type, pos, dim);
	}
}
