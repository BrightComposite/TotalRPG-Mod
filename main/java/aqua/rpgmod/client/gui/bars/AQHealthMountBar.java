package aqua.rpgmod.client.gui.bars;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class AQHealthMountBar extends AQBarController
{
	public static class MetaController extends AQBarController
	{
		@Override
		public float calculate(Minecraft mc)
		{
			Entity ridingEntity = mc.thePlayer.ridingEntity;
			
			if(ridingEntity == null || ridingEntity instanceof EntityLivingBase == false)
				return 0.0f;
			
			EntityLivingBase mount = (EntityLivingBase) ridingEntity;
			
			float healthMax = mount.getMaxHealth();
			int hearts = (int) (healthMax + 0.5F) / 2;
			if(hearts > 30)
				hearts = 30;
			
			return hearts / 30.0f;
		}
	}
	
	public AQHealthMountBar()
	{
		this.metaController = new MetaController();
	}
	
	@Override
	public float calculate(Minecraft mc)
	{
		Entity ridingEntity = mc.thePlayer.ridingEntity;
		
		if(ridingEntity == null || ridingEntity instanceof EntityLivingBase == false)
			return 0.0f;
		
		EntityLivingBase mount = (EntityLivingBase) ridingEntity;
		return mount.getHealth() / mount.getMaxHealth();
	}
	
}
