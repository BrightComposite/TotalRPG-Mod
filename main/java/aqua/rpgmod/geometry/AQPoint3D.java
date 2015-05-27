package aqua.rpgmod.geometry;

public class AQPoint3D extends AQValue3D
{
	public AQPoint3D()
	{
		super();
	}
	
	public AQPoint3D(AQValue3D value)
	{
		super(value);
	}
	
	public AQPoint3D(AQValue3Dd value)
	{
		super(value);
	}
	
	public AQPoint3D(int x, int y, int z)
	{
		super(x, y, z);
	}
	
	public AQPoint3D(double x, double y, double z)
	{
		super(x, y, z);
	}
	
	public double distanceTo(AQPoint3D p)
	{
		double xx = (p.x - this.x) * (p.x - this.x);
		double yy = (p.y - this.y) * (p.y - this.y);
		double zz = (p.z - this.z) * (p.z - this.z);
		
		return Math.sqrt(xx + yy + zz);
	}
}
