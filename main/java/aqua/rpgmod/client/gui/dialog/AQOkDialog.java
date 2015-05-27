package aqua.rpgmod.client.gui.dialog;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class AQOkDialog extends AQInfoDialog
{
	public static enum Result
	{
		OK,
		CLOSE;
	}
	
	protected Result result = Result.CLOSE;
	
	public AQOkDialog(GuiScreen owner, String text, Object ... objects)
	{
		super(owner, text, objects);
	}
	
	public int getTextHeight()
	{
		return this.ySize - 35;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		this.buttonList.add(new GuiButton(1, this.guiLeft + (this.xSize - 50) / 2, this.guiTop + this.ySize - 40, 50, 20, I18n.format("menu.ok")));
	}
	
	@Override
	protected void actionPerformed(GuiButton button)
	{
		if(button.id == 0)
		{
			this.result = Result.CLOSE;
			close();
		}
		else if(button.id == 1)
		{
			this.result = Result.OK;
			close();
		}
	}
	
	public Result getResult()
	{
		return this.result;
	}
}
