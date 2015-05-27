package aqua.rpgmod.client.gui;

import java.lang.reflect.Field;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiOptionSlider;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import aqua.rpgmod.AquaMod;

public class AQGuiControls extends GuiControls
{
	private static final GameSettings.Options[] opts = new GameSettings.Options[]
	{
		GameSettings.Options.INVERT_MOUSE, GameSettings.Options.SENSITIVITY
	};
	private GameSettings options;
	private GuiButton reset;
	
	public AQGuiControls(GuiScreen p_i1027_1_, GameSettings p_i1027_2_)
	{
		super(p_i1027_1_, p_i1027_2_);
		
		this.options = p_i1027_2_;
	}
	
	public void patch() throws Exception
	{
		Field f;
		
		try
		{
			f = GuiControls.class.getDeclaredField(AquaMod.inDevelopment() ? "keyBindingList" : "field_146494_r");
			f.set(this, new AQGuiKeyBindingList(this, this.mc));
		}
		catch(NoSuchFieldException e)
		{
			e.printStackTrace();
		}
		
		f = GuiControls.class.getDeclaredField("field_146493_s");
		f.set(this, this.reset);
	}
	
	@Override
	public void initGui()
	{
		this.reset = new GuiButton(201, this.width / 2 - 155 + 160, this.height - 29, 150, 20, I18n.format("controls.resetAll", new Object[0]));
		
		try
		{
			patch();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		this.buttonList.add(new GuiButton(200, this.width / 2 - 155, this.height - 29, 150, 20, I18n.format("gui.done", new Object[0])));
		this.buttonList.add(this.reset);
		
		this.field_146495_a = I18n.format("controls.title", new Object[0]);
		int i = 0;
		GameSettings.Options[] aoptions = opts;
		int j = aoptions.length;
		
		for(int k = 0; k < j; ++k)
		{
			GameSettings.Options options = aoptions[k];
			
			if(options.getEnumFloat())
			{
				this.buttonList.add(new GuiOptionSlider(options.returnEnumOrdinal(), this.width / 2 - 155 + i % 2 * 160, 27 + 24 * (i >> 1), options));
			}
			else
			{
				this.buttonList.add(new GuiOptionButton(
					options.returnEnumOrdinal(),
					this.width / 2 - 155 + i % 2 * 160,
					27 + 24 * (i >> 1),
					options,
					this.options.getKeyBinding(options)));
			}
			
			++i;
		}
	}
}
