package aqua.rpgmod.service.interaction;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public class AQInteractor
{
	protected Class cl;
	protected final String desc;
	
	public AQInteractor(Class cl, String desc)
	{
		this.cl = cl;
		this.desc = desc;
	}
	
	@SuppressWarnings("unused")
	public String getDesc(AQRayTraceInfo rayTrace, int id)
	{
		return this.desc;
	}
	
	@SuppressWarnings("unused")
	public int getEntityInteractionId(AQRayTraceInfo rayTrace, ItemStack stack)
	{
		Entity entity = rayTrace.getEntity();
		return (entity != null && this.cl.isInstance(entity)) ? 0 : -1;
	}
	
	@SuppressWarnings("unused")
	public int getBlockInteractionId(AQRayTraceInfo rayTrace, ItemStack stack)
	{
		Block block = rayTrace.getBlock();
		return (block != null && this.cl.isInstance(block)) ? 0 : -1;
	}
}
