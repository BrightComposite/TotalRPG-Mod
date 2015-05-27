package aqua.rpgmod.client.gui.bars;

import net.minecraft.client.Minecraft;

public class AQHealthBar extends AQBarController
{
	@Override
	public float calculate(Minecraft mc)
	{
		return mc.thePlayer.getHealth() / mc.thePlayer.getMaxHealth();
	}
}
