package aqua.rpgmod.fx;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import aqua.rpgmod.client.render.fx.AQEntityRandomFx;

public class AQRandomFxSystem extends AQFxSystem
{
	public static AQFxSystem instance = AQCommonFxSystem.instance;
	private Minecraft mc = Minecraft.getMinecraft();
	
	public static void create()
	{
		if(instance != AQCommonFxSystem.instance)
			return;
		
		instance = new AQRandomFxSystem();
	}
	
	@Override
	public void spawnFx(World world, AQFxPlacement base, float[] color)
	{
		for(int i = 0; i < 27; i++)
			this.mc.effectRenderer.addEffect(new AQEntityRandomFx(world, base, color[0], color[1], color[2]));
	}
	
}
