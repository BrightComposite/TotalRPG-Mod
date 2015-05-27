package aqua.rpgmod.client.gui.bars;

import net.minecraft.client.Minecraft;
import aqua.rpgmod.player.AQThisPlayerWrapper;

public class AQAirBar extends AQBarController
{
	@Override
	public void checkVisibility(float value)
	{
		this.visible = value < 1.0f;
	}
	
	@Override
	public float calculate(Minecraft mc)
	{
		return AQThisPlayerWrapper.getWrapper().airLevel.getPercent();
	}
}
