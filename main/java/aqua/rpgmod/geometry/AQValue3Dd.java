package aqua.rpgmod.geometry;

public class AQValue3Dd
{
	public double x, y, z;
	
	public AQValue3Dd(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public AQValue3Dd(AQValue3Dd value)
	{
		this(value.x, value.y, value.z);
	}
	
	public AQValue3Dd(AQValue3D value)
	{
		this(value.x, value.y, value.z);
	}
	
	@Override
	public AQValue3D clone()
	{
		return new AQValue3D(this.x, this.y, this.z);
	}
	
	public double get(AQAxis3D axis)
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
	
	public void set(AQAxis3D axis, double value)
	{
		switch(axis)
		{
			case X:
				this.x = value;
			case Y:
				this.y = value;
				/* Z */default:
				this.z = value;
		}
	}
	
	public AQValue3Dd plus(double value)
	{
		return new AQValue3Dd(this.x + value, this.y + value, this.z + value);
	}
	
	public AQValue3Dd plus(AQValue3Dd value)
	{
		return new AQValue3Dd(this.x + value.x, this.y + value.y, this.z + value.z);
	}
	
	public AQValue3Dd plus(AQValue3D value)
	{
		return new AQValue3Dd(this.x + value.x, this.y + value.y, this.z + value.z);
	}
	
	public AQValue3Dd mul(double value)
	{
		return new AQValue3Dd(this.x * value, this.y * value, this.z * value);
	}
	
	public AQValue3Dd mul(AQValue3Dd value)
	{
		return new AQValue3Dd(this.x * value.x, this.y * value.y, this.z * value.z);
	}
	
	public AQValue3Dd mul(AQValue3D value)
	{
		return new AQValue3Dd(this.x * value.x, this.y * value.y, this.z * value.z);
	}
	
	public static AQValue3Dd generateMin(AQValue3Dd v1, AQValue3Dd v2)
	{
		return new AQValue3Dd(Math.min(v1.x, v2.x), Math.min(v1.y, v2.y), Math.min(v1.z, v2.z));
	}
	
	public static AQValue3Dd generateMax(AQValue3Dd v1, AQValue3Dd v2)
	{
		return new AQValue3Dd(Math.max(v1.x, v2.x), Math.max(v1.y, v2.y), Math.max(v1.z, v2.z));
	}
}
