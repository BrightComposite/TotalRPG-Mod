package aqua.rpgmod.world.region;

import net.minecraft.world.World;
import aqua.rpgmod.AquaMod;
import aqua.rpgmod.service.AQStringTranslator;
import aqua.rpgmod.service.network.AQMessage;
import aqua.rpgmod.service.network.AQMessage.RegionMessage;

public class AQClientRegionManager extends AQRegionManager
{
	public static AQClientRegionManager instance = new AQClientRegionManager();
	AQRegionProvider provider;
	
	public void createProvider(World world)
	{
		this.provider = new AQRegionProvider(world, this);
	}
	
	@Override
	public AQRegionProvider getProvider(World world)
	{
		return this.provider;
	}
	
	@Override
	public AQRegionProvider getProvider(Integer id)
	{
		return this.provider;
	}
	
	@Override
	void addRegion(AQRegion region)
	{
		AquaMod.regionNetwork.sendToServer(new AQMessage(RegionMessage.ADD, new AQRegion.Translator(region)));
	}
	
	@Override
	void removeRegion(AQRegion region)
	{
		AquaMod.regionNetwork.sendToServer(new AQMessage(RegionMessage.REMOVE, new AQStringTranslator(region.name)));
	}
}
