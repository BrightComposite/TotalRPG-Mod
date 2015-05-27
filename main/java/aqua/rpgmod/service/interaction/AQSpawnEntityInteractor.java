package aqua.rpgmod.service.interaction;

import net.minecraft.entity.EntityList;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class AQSpawnEntityInteractor extends AQInteractor
{
	public AQSpawnEntityInteractor()
	{
		super(null, "interact.clone");
	}
	
	@Override
	public int getEntityInteractionId(AQRayTraceInfo rayTrace, ItemStack stack)
	{
		if(stack != null && stack.getItem() == Items.spawn_egg)
		{
			Class cl = EntityList.getClassFromID(stack.getItemDamage());
			
			if(cl != null && cl.isInstance(rayTrace.getEntity()))
				return 0;
		}
		
		return -1;
	}
}
