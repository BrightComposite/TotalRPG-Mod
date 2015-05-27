package aqua.rpgmod.world.region;

import aqua.rpgmod.geometry.AQBox;
import aqua.rpgmod.geometry.AQPoint3D;

public class AQWorldRegion extends AQRegion
{
	AQWorldRegion(AQRegionProvider provider)
	{
		super(provider.world.provider.getDimensionName(), RegionType.ROOT);
		
		this.provider = provider;
		this.shape = new AQBox(new AQPoint3D(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE), new AQPoint3D(
			Double.MAX_VALUE,
			Double.MAX_VALUE,
			Double.MAX_VALUE));
	}
	
	@Override
	public boolean shouldDraw()
	{
		return false;
	}
	
	@Override
	public boolean permanentView()
	{
		return false;
	}
	
	@Override
	public float[] color()
	{
		return new float[]
		{
			1.0f, 1.0f, 1.0f
		};
	}
}
