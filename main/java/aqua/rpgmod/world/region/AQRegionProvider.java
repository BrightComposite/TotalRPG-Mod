package aqua.rpgmod.world.region;

import java.util.HashMap;

import net.minecraft.world.World;

public class AQRegionProvider
{
	public final AQWorldRegion root;
	public final World world;
	public final AQRegionManager manager;
	final HashMap<String, AQRegion> registry = new HashMap<String, AQRegion>();

	@SuppressWarnings("static-method")
	synchronized public void synchronize(Runnable function)
	{
		function.run();
	}
	
	protected AQRegionProvider(World world, AQRegionManager manager)
	{
		this.world = world;
		this.manager = manager;
		this.root = new AQWorldRegion(this);
	}
	
	synchronized void registerRegion(AQRegion region)
	{
		this.registry.put(region.name, region);
	}
	
	synchronized void removeRegion(AQRegion region)
	{
		this.registry.remove(region.name);
	}
	
	synchronized public AQRegion findRegion(String name)
	{
		return this.registry.get(name);
	}

	synchronized public AQRegion[] getRegions()
	{
		return this.registry.values().toArray(new AQRegion[0]);
	}
}
