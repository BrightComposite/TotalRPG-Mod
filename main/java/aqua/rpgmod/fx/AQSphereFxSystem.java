package aqua.rpgmod.fx;

import aqua.rpgmod.client.render.fx.AQEntitySphereFx;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

public class AQSphereFxSystem extends AQFxSystem
{
	public static AQFxSystem instance = AQCommonFxSystem.instance;
	private Minecraft mc = Minecraft.getMinecraft();
	
	public static void create()
	{
		if(instance != AQCommonFxSystem.instance)
			return;
		
		instance = new AQSphereFxSystem();
	}
	
	@Override
	public void spawnFx(World world, AQFxPlacement base, float[] color)
	{
		for(int i = 0; i < 27; i++)
			this.mc.effectRenderer.addEffect(new AQEntitySphereFx(world, base, color[0], color[1], color[2]));
	}
	
}
