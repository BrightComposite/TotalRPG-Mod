package aqua.rpgmod.geometry;

public class AQPoint3Dd extends AQValue3Dd
{
	public AQPoint3Dd(double x, double y, double z)
	{
		super(x, y, z);
	}
	
	public AQPoint3Dd(AQValue3Dd value)
	{
		super(value);
	}
	
	public AQPoint3Dd(AQValue3D value)
	{
		super(value);
	}
	
	public double distanceTo(AQPoint3Dd p)
	{
		double xx = (p.x - this.x) * (p.x - this.x);
		double yy = (p.y - this.y) * (p.y - this.y);
		double zz = (p.z - this.z) * (p.z - this.z);
		
		return Math.sqrt(xx + yy + zz);
	}
}
