package aqua.rpgmod.client.gui.dialog;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class AQInfoDialog extends AQGuiDialog
{
	protected String text;
	
	public AQInfoDialog(GuiScreen owner, String text, Object ... objects)
	{
		super(owner);
		
		this.text = I18n.format(text, objects);
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		this.buttonList.add(new GuiButton(0, this.guiLeft + this.xSize - 30, this.guiTop + 10, 20, 20, "X"));
	}
	
	public int getTextY()
	{
		return this.guiTop;
	}
	
	public int getTextHeight()
	{
		return this.ySize;
	}
	
	@Override
	public void drawForegroundLayer(int p_73863_1_, int p_73863_2_, float p_73863_3_)
	{
		String strings[] = this.text.split("\n");
		List<String> lines = new ArrayList<String>();
		
		for(String s : strings)
		{
			lines.addAll(this.fontRendererObj.listFormattedStringToWidth(s, this.xSize - 60));
		}
		
		int height = this.fontRendererObj.FONT_HEIGHT;
		int yStart = getTextY() + (getTextHeight() - (lines.size() - 1) * (height + 2)) / 2;
		
		for(int i = 0; i < lines.size(); ++i)
		{
			drawCenteredString(this.mc.fontRenderer, lines.get(i), this.guiLeft + this.xSize / 2, yStart + i * (height + 2), 0xFFFFFFFF);
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button)
	{
		close();
	}
}
