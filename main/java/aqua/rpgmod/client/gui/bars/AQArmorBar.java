package aqua.rpgmod.client.gui.bars;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;

public class AQArmorBar extends AQBarController
{
	public static class MetaController extends AQBarController
	{
		@Override
		public float calculate(Minecraft mc)
		{
			return ForgeHooks.getTotalArmorValue(mc.thePlayer) / 20.0f;
		}
	}
	
	public AQArmorBar()
	{
		this.metaController = new MetaController();
	}
	
	public static float getTotalArmorState(EntityPlayer player)
	{
		float ret = -1.0f;
		
		for(int i = 0; i < player.inventory.armorInventory.length; i++)
		{
			ItemStack stack = player.inventory.armorInventory[i];
			
			if(stack != null)
			{
				float state = 1.0f - (float) stack.getItemDamage() / (float) stack.getMaxDamage();
				
				if(ret < 0.0f)
					ret = state;
				else
					ret = (ret + state) / 2.0f;
			}
		}
		
		return Math.max(ret, 0.0f);
	}
	
	@Override
	public float calculate(Minecraft mc)
	{
		return getTotalArmorState(mc.thePlayer);
	}
	
}
