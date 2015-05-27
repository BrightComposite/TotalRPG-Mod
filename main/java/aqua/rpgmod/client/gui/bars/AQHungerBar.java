package aqua.rpgmod.client.gui.bars;

import net.minecraft.client.Minecraft;
import aqua.rpgmod.player.AQThisPlayerWrapper;

public class AQHungerBar extends AQBarController
{
	@Override
	public float calculate(Minecraft mc)
	{
		return AQThisPlayerWrapper.getWrapper().hunger.getPercent();
	}
}
