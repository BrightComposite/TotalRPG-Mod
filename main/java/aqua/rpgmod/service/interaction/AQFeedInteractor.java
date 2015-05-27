package aqua.rpgmod.service.interaction;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;

public class AQFeedInteractor extends AQInteractor
{
	public AQFeedInteractor()
	{
		super(EntityAnimal.class, "interact.feed");
	}
	
	@Override
	public int getEntityInteractionId(AQRayTraceInfo rayTrace, ItemStack stack)
	{
		return super.getEntityInteractionId(rayTrace, stack) != -1 && stack != null && ((EntityAnimal) rayTrace.getEntity()).isBreedingItem(stack) ? 0 : -1;
	}
}
