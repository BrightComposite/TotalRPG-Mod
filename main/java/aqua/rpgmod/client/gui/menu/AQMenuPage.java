package aqua.rpgmod.client.gui.menu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.opengl.GL11;

import aqua.rpgmod.AQModInfo;
import aqua.rpgmod.AquaMod;
import aqua.rpgmod.client.gui.AQGuiButton;
import aqua.rpgmod.client.gui.AQGuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class AQMenuPage extends GuiScreen
{
	protected String name;
	protected ResourceLocation background = null;
	
	public final int childStart;
	public final int maxChildren;
	protected int currentChild = 0;
	
	protected AQMenuPage parent = null;
	
	public ArrayList<AQGuiButton> buttons = new ArrayList<AQGuiButton>();
	public ArrayList<AQMenuPage> children = new ArrayList<AQMenuPage>();
	
	protected int guiLeft;
	protected int guiTop;
	protected int xSize = 248;
	protected int ySize = 166;
	
	public AQMenuPage(String name)
	{
		this.name = name;
		this.maxChildren = 0;
		this.childStart = 0;
		this.background = new ResourceLocation(AQModInfo.MODID + ":" + "textures/gui/" + this.name + ".png");
	}
	
	public AQMenuPage(String name, int maxChildren)
	{
		this.name = name;
		this.maxChildren = maxChildren;
		this.childStart = AQGuiUtils.reservePages(maxChildren);
		this.background = new ResourceLocation(AQModInfo.MODID + ":" + "textures/gui/" + this.name + ".png");
	}
	
	@Override
	public void initGui()
	{
		if(!this.children.isEmpty())
		{
			openCurrentPage();
			return;
		}
		
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;
		
		setButtons(this.guiLeft, (this.height - 166) / 2, this.buttonList);
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public int getLeft()
	{
		return this.guiLeft;
	}
	
	public int getTop()
	{
		return this.guiTop;
	}
	
	@SuppressWarnings("unused")
	protected void onAddPage(AQMenuPage page, int index)
	{}
	
	protected void setButtons(int x, int y, List buttonList)
	{
		buttonList.addAll(this.buttons);
		
		if(this.parent != null)
			this.parent.setButtons(x, y, buttonList);
	}
	
	public void addPage(AQMenuPage page)
	{
		if(this.children.size() == this.maxChildren)
			return;
		
		page.parent = this;
		this.children.add(page);
		onAddPage(page, this.children.size() - 1);
	}
	
	public void openCurrentPage()
	{
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		player.openGui(AquaMod.mod, this.childStart + this.currentChild, player.worldObj, player.serverPosX, player.serverPosY, player.serverPosZ);
	}
	
	public void selectPage(int index)
	{
		this.currentChild = index;
		
		if(!this.children.isEmpty())
			openCurrentPage();
	}
	
	public AQMenuPage getById(int id)
	{
		if(id >= this.childStart && id < this.childStart + this.maxChildren)
			return this.children.get(id - this.childStart);
		
		for(Iterator<AQMenuPage> i = this.children.iterator(); i.hasNext();)
		{
			AQMenuPage page = i.next().getById(id);
			
			if(page != null)
				return page;
		}
		
		return null;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float ticks)
	{
		drawDefaultBackground();
		drawBackground(mouseX, mouseY, ticks);
		drawForeground(mouseX, mouseY, ticks);
		drawHints(mouseX, mouseY, ticks);
	}
	
	@SuppressWarnings("unused")
	public void drawBackground(int mouseX, int mouseY, float ticks)
	{
		if(this.background != null)
		{
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_BLEND);
			
			this.mc.getTextureManager().bindTexture(this.background);
			this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		}
	}
	
	@SuppressWarnings("unused")
	public void drawForeground(int mouseX, int mouseY, float ticks)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		
		for(int i = 0; i < this.buttonList.size(); ++i)
		{
			((GuiButton) this.buttonList.get(i)).drawButton(this.mc, mouseX, mouseY);
		}
		
		for(int i = 0; i < this.labelList.size(); ++i)
		{
			((GuiLabel) this.labelList.get(i)).func_146159_a(this.mc, mouseX, mouseY);
		}
	}
	
	@SuppressWarnings("unused")
	public void drawHints(int mouseX, int mouseY, float ticks)
	{
		for(int i = 0; i < this.buttonList.size(); ++i)
		{
			Object b = this.buttonList.get(i);
			
			if(b instanceof AQGuiButton)
			{
				AQGuiButton button = (AQGuiButton) b;
				
				if(button.drawHint && button.isMouseOver(mouseX, mouseY))
					button.drawHint(mouseX, mouseY, this.mc.fontRenderer);
			}
		}
	}
	
	@Override
	public void updateScreen()
	{
		super.updateScreen();
		
		if(!this.mc.thePlayer.isEntityAlive() || this.mc.thePlayer.isDead)
			this.mc.thePlayer.closeScreen();
	}
	
	@Override
	public boolean doesGuiPauseGame()
	{
		return true;
	}
	
	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_)
	{
		if(p_73869_2_ == 1 || p_73869_2_ == this.mc.gameSettings.keyBindInventory.getKeyCode())
		{
			this.mc.thePlayer.closeScreen();
		}
	}
	
	@Override
	public void actionPerformed(GuiButton button)
	{
		if(this.parent != null)
			this.parent.actionPerformed(button);
	}
}
