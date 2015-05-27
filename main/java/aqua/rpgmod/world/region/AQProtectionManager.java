package aqua.rpgmod.world.region;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import aqua.rpgmod.geometry.AQPoint3D;
import aqua.rpgmod.world.region.AQProtectionUnit.Flag;

public class AQProtectionManager
{
	static boolean checkProtection(AQPoint3D point, EntityPlayer actor, Flag flag)
	{
		List<AQRegion> list = AQRegionManager.getProviderForPlayer(actor).root.findAllRegionsAt(point);
		
		for(Iterator<AQRegion> i = list.iterator(); i.hasNext();)
		{
			AQRegion r = i.next();
			
			if(r.type == AQRegion.RegionType.PROTECTION)
			{
				AQProtectionRegion region = (AQProtectionRegion) r;
				
				if(region.checkFlags(flag))
				{	
					AQPlayerProtectionUnit unit = region.findPlayer(actor);
					
					if(unit == null || !unit.checkFlags(flag))
						return true;
				}
			}
		}
		
		return false;
	}

	public static boolean checkBlockProtection(AQPoint3D point, EntityPlayer actor)
	{
		return checkProtection(point, actor, Flag.BLOCK);
	}

	public static boolean checkPlaceProtection(AQPoint3D point, EntityPlayer actor)
	{
		return checkProtection(point, actor, Flag.PLACE);
	}

	public static boolean checkChestProtection(AQPoint3D point, EntityPlayer actor)
	{
		return checkProtection(point, actor, Flag.CHEST);
	}

	public static boolean checkUseProtection(AQPoint3D point, EntityPlayer actor)
	{
		return checkProtection(point, actor, Flag.USE);
	}
	
	public static boolean checkPlayerProtection(AQPoint3D point, EntityPlayer actor, EntityPlayer player)
	{
		List<AQRegion> list = AQRegionManager.getProviderForPlayer(actor).root.findAllRegionsAt(point);
		
		for(Iterator<AQRegion> i = list.iterator(); i.hasNext();)
		{
			AQRegion r = i.next();
			
			if(r.type == AQRegion.RegionType.PROTECTION)
			{
				AQProtectionRegion region = (AQProtectionRegion) r;
				
				if(region.checkFlags(Flag.PLAYERS) && region.findPlayer(player) != null)
					return true;
			}
		}
		
		return false;
	}
}
