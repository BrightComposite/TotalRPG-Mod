package aqua.rpgmod.service;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.client.settings.KeyBinding;

public class AQKeyBinding extends KeyBinding
{
	
	public AQKeyBinding(String desc, int code, String category)
	{
		super(desc, code, category);
		ClientRegistry.registerKeyBinding(this);
	}
	
	public void onPress()
	{}
}
