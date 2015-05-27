package aqua.rpgmod.client.gui.bars;

import net.minecraft.client.Minecraft;

public class AQJumpBar extends AQBarController
{
	@Override
	public float calculate(Minecraft mc)
	{
		return mc.thePlayer.getHorseJumpPower();
	}
	
}
