package aqua.rpgmod.client.gui.bars;

import net.minecraft.client.Minecraft;
import aqua.rpgmod.player.AQPlayerWrapper;
import aqua.rpgmod.player.AQThisPlayerWrapper;

public class AQManaBar extends AQBarController
{
	@Override
	public float calculate(Minecraft mc)
	{
		AQPlayerWrapper info = AQThisPlayerWrapper.getWrapper();
		return info.mana.getPercent();
	}
	
}
