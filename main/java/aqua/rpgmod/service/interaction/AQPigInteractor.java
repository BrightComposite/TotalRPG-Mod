package aqua.rpgmod.service.interaction;

import net.minecraft.entity.passive.EntityPig;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class AQPigInteractor extends AQInteractor
{
	public AQPigInteractor()
	{
		super(EntityPig.class, "");
	}
	
	@Override
	public String getDesc(AQRayTraceInfo info, int id)
	{
		if(id == 0)
			return "interact.mount";
		
		return "interact.saddle";
	}
	
	@Override
	public int getEntityInteractionId(AQRayTraceInfo rayTrace, ItemStack stack)
	{
		if(super.getEntityInteractionId(rayTrace, stack) == -1 || (!((EntityPig)rayTrace.getEntity()).getSaddled() && (stack == null || stack.getItem() != Items.saddle)))
			return -1;
		
		return ((EntityPig)rayTrace.getEntity()).getSaddled() ? 0 : 1;
			
	}
}
