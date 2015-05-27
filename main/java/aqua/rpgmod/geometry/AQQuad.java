package aqua.rpgmod.geometry;

public class AQQuad
{
	public final AQPoint3Dd points[];
	
	public AQQuad(AQPoint3Dd p1, AQPoint3Dd p2, AQSide3D side)
	{
		switch(side)
		{
			case LEFT:
				this.points = new AQPoint3Dd[]
				{
					new AQPoint3Dd(p1.x, p1.y, p2.z), new AQPoint3Dd(p1.x, p2.y, p2.z), new AQPoint3Dd(p1.x, p2.y, p1.z), new AQPoint3Dd(p1.x, p1.y, p1.z)
				};
				break;
			case RIGHT:
				this.points = new AQPoint3Dd[]
				{
					new AQPoint3Dd(p2.x, p1.y, p1.z), new AQPoint3Dd(p2.x, p2.y, p1.z), new AQPoint3Dd(p2.x, p2.y, p2.z), new AQPoint3Dd(p2.x, p1.y, p2.z)
				};
				break;
			case TOP:
				this.points = new AQPoint3Dd[]
				{
					new AQPoint3Dd(p1.x, p2.y, p1.z), new AQPoint3Dd(p1.x, p2.y, p2.z), new AQPoint3Dd(p2.x, p2.y, p2.z), new AQPoint3Dd(p2.x, p2.y, p1.z)
				};
				break;
			case BOTTOM:
				this.points = new AQPoint3Dd[]
				{
					new AQPoint3Dd(p1.x, p1.y, p1.z), new AQPoint3Dd(p2.x, p1.y, p1.z), new AQPoint3Dd(p2.x, p1.y, p2.z), new AQPoint3Dd(p1.x, p1.y, p2.z)
				};
				break;
			case BACK:
				this.points = new AQPoint3Dd[]
				{
					new AQPoint3Dd(p1.x, p1.y, p1.z), new AQPoint3Dd(p1.x, p2.y, p1.z), new AQPoint3Dd(p2.x, p2.y, p1.z), new AQPoint3Dd(p2.x, p1.y, p1.z)
				};
				break;
			default:
				this.points = new AQPoint3Dd[]
				{
					new AQPoint3Dd(p2.x, p1.y, p2.z), new AQPoint3Dd(p2.x, p2.y, p2.z), new AQPoint3Dd(p1.x, p2.y, p2.z), new AQPoint3Dd(p1.x, p1.y, p2.z)
				};
				break;
		}
	}
	
	public AQQuad(AQValue3Dd p1, AQValue3Dd p2, AQSide3D side)
	{
		this(new AQPoint3Dd(p1), new AQPoint3Dd(p2), side);
	}
	
	public AQQuad(AQPoint3Dd p1, AQPoint3Dd p2, AQPoint3Dd p3, AQPoint3Dd p4)
	{
		this.points = new AQPoint3Dd[]
		{
			p1, p2, p3, p4
		};
	}
}
