package aqua.rpgmod.client.gui.dialog;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import aqua.rpgmod.AQModInfo;
import aqua.rpgmod.client.gui.AQGuiUtils;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

abstract public class AQGuiDialog extends GuiScreen
{
	public static int defaultWidth = 166;
	public static int defaultHeight = 114;
	
	public static ResourceLocation defaultBackground = new ResourceLocation(AQModInfo.MODID + ":" + "textures/gui/dialog.png");
	
	protected final GuiScreen owner;
	protected int guiLeft;
	protected int guiTop;
	protected int xSize;
	protected int ySize;
	protected ResourceLocation background;
	
	protected int ownerMouseX;
	protected int ownerMouseY;
	
	public AQGuiDialog(GuiScreen owner)
	{
		this.owner = owner;
		this.xSize = defaultWidth;
		this.ySize = defaultHeight;
		
		this.background = defaultBackground;
	}
	
	public void show()
	{
		this.owner.mc.currentScreen = this;
		this.setWorldAndResolution(this.owner.mc, this.owner.width, this.owner.height);
		
		this.ownerMouseX = Mouse.getX() * this.width / this.mc.displayWidth;
		this.ownerMouseY = (this.mc.displayHeight - Mouse.getY()) * this.height / this.mc.displayHeight;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;
	}
	
	public void resetView()
	{
		ScaledResolution scaledresolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
		
		GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
		
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, scaledresolution.getScaledWidth_double(), scaledresolution.getScaledHeight_double(), 0.0D, 1000.0D, 3000.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
	}
	
	public void drawOwner(float p_73863_3_)
	{
		this.owner.drawScreen(this.ownerMouseX, this.ownerMouseY, p_73863_3_);
		resetView();
		drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
	}
	
	public void drawButtons(int p_73863_1_, int p_73863_2_, float p_73863_3_)
	{
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}
	
	@SuppressWarnings("unused")
	public void drawBackgroundLayer(int p_73863_1_, int p_73863_2_, float p_73863_3_)
	{
		this.mc.getTextureManager().bindTexture(this.background);
		AQGuiUtils.renderImage(this.guiLeft, this.guiTop, this.xSize, this.ySize, 0.0f, 0.0f, 1.0f, 1.0f, this.zLevel);
	}
	
	@SuppressWarnings("unused")
	public void drawForegroundLayer(int p_73863_1_, int p_73863_2_, float p_73863_3_)
	{}
	
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
	{
		drawOwner(p_73863_3_);
		
		GL11.glColor3d(1.0f, 1.0f, 1.0f);
		drawBackgroundLayer(p_73863_1_, p_73863_2_, p_73863_3_);
		drawForegroundLayer(p_73863_1_, p_73863_2_, p_73863_3_);
		drawButtons(p_73863_1_, p_73863_2_, p_73863_3_);
	}
	
	public void close()
	{
		if(this.mc.currentScreen == this)
			this.mc.currentScreen = this.owner;
		
		onClose();
	}
	
	public void onClose()
	{}
	
	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_)
	{
		if(p_73869_2_ == 1)
		{
			close();
		}
	}
	
	@Override
	public void updateScreen()
	{
		this.owner.width = this.width;
		this.owner.height = this.height;
	}
}
