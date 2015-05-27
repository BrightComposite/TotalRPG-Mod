package aqua.rpgmod.geometry;

public enum AQSide3D
{
	LEFT(AQAxis3D.X, -1),
	RIGHT(AQAxis3D.X, 1),
	BOTTOM(AQAxis3D.Y, -1),
	TOP(AQAxis3D.Y, 1),
	BACK(AQAxis3D.Z, -1),
	FRONT(AQAxis3D.Z, 1);
	
	public final AQAxis3D axis;
	public final int sign;
	
	AQSide3D(AQAxis3D axis, int sign)
	{
		this.axis = axis;
		this.sign = sign;
	}
	
	public static AQSide3D opposite(AQSide3D side)
	{
		switch(side)
		{
			case LEFT:
				return RIGHT;
			case RIGHT:
				return LEFT;
			case BOTTOM:
				return TOP;
			case TOP:
				return BOTTOM;
			case BACK:
				return FRONT;
			default:
				return BACK;
		}
	}
	
	public static AQSide3D side(int i)
	{
		return AQSide3D.values()[i];
	}
	
	public static int getMCside(AQSide3D side)
	{
		switch(side)
		{
			case LEFT:
				return 4;
			case RIGHT:
				return 5;
			case BOTTOM:
				return 0;
			case TOP:
				return 1;
			case BACK:
				return 2;
			default:
				return 3;
		}
	}
	
	public int compare(int i1, int i2)
	{
		return (i1 > i2) ? this.sign : (i1 < i2) ? -this.sign : 0;
	}
}
