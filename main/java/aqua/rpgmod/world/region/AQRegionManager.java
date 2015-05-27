package aqua.rpgmod.world.region;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import aqua.rpgmod.player.AQPlayerWrapper;

abstract public class AQRegionManager
{
	public static AQRegionManager getManagerForPlayer(EntityPlayer player)
	{
		return AQPlayerWrapper.cantBeThis(player) ? AQServerRegionManager.instance : AQClientRegionManager.instance;
	}
	
	public static AQRegionProvider getProviderForPlayer(EntityPlayer player)
	{
		AQRegionManager manager = getManagerForPlayer(player);
		return manager.getProvider(player.worldObj);
	}
	
	abstract public AQRegionProvider getProvider(World world);
	
	abstract public AQRegionProvider getProvider(Integer id);
	
	@SuppressWarnings("unused")
	void addRegion(AQRegion region) {}
	
	@SuppressWarnings("unused")
	void removeRegion(AQRegion region) {}
	
	void updateRegion(AQRegion region)
	{
		removeRegion(region);
		addRegion(region);
	}
}
