package aqua.rpgmod.service.interaction;

import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class AQTameInteractor extends AQInteractor
{
	public AQTameInteractor()
	{
		super(EntityTameable.class, "interact.tame");
	}
	
	@Override
	public int getEntityInteractionId(AQRayTraceInfo info, ItemStack stack)
	{
		if(super.getEntityInteractionId(info, stack) == -1)
			return -1;
		
		EntityTameable entity = (EntityTameable) info.getEntity();
		
		return !entity.isTamed() && stack != null
			&& ((stack.getItem() == Items.fish && entity instanceof EntityOcelot) || (stack.getItem() == Items.bone && entity instanceof EntityWolf)) ? 0 : -1;
	}
}
