package aqua.rpgmod.geometry;

public enum AQDirection3D
{
	NORTH(0, -1),
	EAST(1, 0),
	SOUTH(0, 1),
	WEST(-1, 0);
	
	public final int x, z;
	
	AQDirection3D(int x, int z)
	{
		this.x = x;
		this.z = z;
	}
}
