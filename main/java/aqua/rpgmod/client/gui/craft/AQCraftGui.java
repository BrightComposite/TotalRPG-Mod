package aqua.rpgmod.client.gui.craft;

import java.util.ArrayList;
import java.util.List;

import aqua.rpgmod.inventory.AQCraftContainer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class AQCraftGui extends GuiContainer
{
	protected ResourceLocation background = null;
	public final AQCraftContainer container;
	protected List<GuiButton> buttons = new ArrayList<GuiButton>();
	public static AQCraftGui instance;
	public final String name;
	
	public AQCraftGui(String name, AQCraftContainer container)
	{
		super(container);
		this.name = name;
		this.container = container;
		AQCraftGui.instance = this;
	}
	
	public void setBackground(ResourceLocation background)
	{
		this.background = background;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
	{
		if(this.background == null)
			return;
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(this.background);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
	{
		super.drawGuiContainerForegroundLayer(p_146979_1_, p_146979_2_);
		this.fontRendererObj.drawString(I18n.format(this.name, new Object[0]), 28, 6, 4210752);
		this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
		
		if(this.container.result != null && this.container.result.text != null)
		{
			this.fontRendererObj.drawString(I18n.format(this.container.result.text, new Object[0]), 100, this.ySize - 96 + 2, 4210752);
		}
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		this.mc.thePlayer.openContainer = this.inventorySlots;
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;
	}
	
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}
	
	@Override
	public void drawScreen(int i, int j, float f)
	{
		super.drawScreen(i, j, f);
	}
	
	@Override
	public void actionPerformed(GuiButton button)
	{}
}
