package aqua.rpgmod.service.interaction;

import net.minecraft.item.ItemStack;

public class AQTransportInteractor extends AQInteractor
{
	public AQTransportInteractor(Class cl)
	{
		super(cl, "interact.getinto");
	}	
	
	@Override
	public int getEntityInteractionId(AQRayTraceInfo info, ItemStack stack)
	{
		return super.getEntityInteractionId(info, stack) != -1 && info.getPlayerWrapper().getRace().canMountEntity() ? 0 : -1;
	}
}
