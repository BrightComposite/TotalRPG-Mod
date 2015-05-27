package aqua.rpgmod.client.gui.bars;

import net.minecraft.client.Minecraft;
import aqua.rpgmod.player.AQThisPlayerWrapper;

public class AQStaminaBar extends AQBarController
{
	public static class MetaController extends AQBarController
	{
		@Override
		public float calculate(Minecraft mc)
		{
			return AQThisPlayerWrapper.getWrapper().hunger.getPercent();
		}
	}
	
	public AQStaminaBar()
	{
		this.alwaysVisible = true;
		this.metaController = new MetaController();
	}
	
	@Override
	public float calculate(Minecraft mc)
	{
		return AQThisPlayerWrapper.getWrapper().stamina.getPercent();
	}
	
}
