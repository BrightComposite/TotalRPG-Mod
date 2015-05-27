package aqua.rpgmod.world.schematics;

import aqua.rpgmod.geometry.AQDimensions;

public class AQSchematicsBase
{
	protected AQDimensions dim = new AQDimensions(0, 0, 0);
	protected int rotation;
	
	public AQDimensions dimensions()
	{
		return this.dim;
	}
}
