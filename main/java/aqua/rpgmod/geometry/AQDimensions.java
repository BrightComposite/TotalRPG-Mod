package aqua.rpgmod.geometry;

public class AQDimensions extends AQValue3D
{
	public AQDimensions()
	{
		super();
	}
	
	public AQDimensions(int x, int y, int z)
	{
		super(x, y, z);
	}
	
	public AQDimensions(double x, double y, double z)
	{
		super(x, y, z);
	}
	
	public AQDimensions(AQValue3D value)
	{
		super(value.x, value.y, value.z);
	}
	
	public AQDimensions(AQValue3Dd value)
	{
		super(value.x, value.y, value.z);
	}
}
