package aqua.rpgmod.geometry;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.MathHelper;

public class AQValue3D
{
	public int x, y, z;
	
	public AQValue3D()
	{
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}
	
	public AQValue3D(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public AQValue3D(double x, double y, double z)
	{
		this.x = MathHelper.floor_double(x);
		this.y = MathHelper.floor_double(y);
		this.z = MathHelper.floor_double(z);
	}
	
	public AQValue3D(AQValue3D value)
	{
		this(value.x, value.y, value.z);
	}
	
	public AQValue3D(AQValue3Dd value)
	{
		this(value.x, value.y, value.z);
	}
	
	@Override
	public AQValue3D clone()
	{
		return new AQValue3D(this.x, this.y, this.z);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof AQValue3D == false)
			return false;
		
		AQValue3D value = (AQValue3D) obj;
		
		return this.x == value.x && this.y == value.y && this.z == value.z;
	}
	
	@Override
	public int hashCode()
	{
		return this.x ^ ~(this.y ^ ~this.z);
	}
	
	public int get(AQAxis3D axis)
	{
		switch(axis)
		{
			case X:
				return this.x;
			case Y:
				return this.y;
				/* Z */default:
				return this.z;
		}
	}
	
	public void set(AQAxis3D axis, int value)
	{
		switch(axis)
		{
			case X:
				this.x = value;
				break;
			case Y:
				this.y = value;
				break;
			/* Z */default:
				this.z = value;
				break;
		}
	}
	
	public ByteBuf serialiaze(ByteBuf buffer)
	{
		buffer.writeInt(this.x);
		buffer.writeInt(this.y);
		buffer.writeInt(this.z);
		
		return buffer;
	}
	
	public ByteBuf deserialiaze(ByteBuf buffer)
	{
		this.x = buffer.readInt();
		this.y = buffer.readInt();
		this.z = buffer.readInt();
		
		return buffer;
	}
	
	public AQValue3D plus(int value)
	{
		return new AQValue3D(this.x + value, this.y + value, this.z + value);
	}
	
	public AQValue3D plus(double value)
	{
		return new AQValue3D(this.x + value, this.y + value, this.z + value);
	}
	
	public AQValue3D plus(AQValue3D value)
	{
		return new AQValue3D(this.x + value.x, this.y + value.y, this.z + value.z);
	}
	
	public AQValue3D mul(int value)
	{
		return new AQValue3D(this.x * value, this.y * value, this.z * value);
	}
	
	public AQValue3D mul(double value)
	{
		return new AQValue3D(this.x * value, this.y * value, this.z * value);
	}
	
	public AQValue3D mul(AQValue3D value)
	{
		return new AQValue3D(this.x * value.x, this.y * value.y, this.z * value.z);
	}
	
	public static AQValue3D generateMin(AQValue3D v1, AQValue3D v2)
	{
		return new AQValue3D(Math.min(v1.x, v2.x), Math.min(v1.y, v2.y), Math.min(v1.z, v2.z));
	}
	
	public static AQValue3D generateMax(AQValue3D v1, AQValue3D v2)
	{
		return new AQValue3D(Math.max(v1.x, v2.x), Math.max(v1.y, v2.y), Math.max(v1.z, v2.z));
	}
}
