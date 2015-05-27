package aqua.rpgmod.client.gui;

import java.net.URI;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Project;

import aqua.rpgmod.character.AQCharacterManager;
import aqua.rpgmod.character.AQCharacterManager.WebResult;
import aqua.rpgmod.client.gui.dialog.AQOkDialog;
import aqua.rpgmod.client.gui.editor.AQGuiCharacter;
import aqua.rpgmod.client.gui.editor.AQGuiCreateCharacter;

import cpw.mods.fml.client.GuiModList;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonLanguage;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;

public class AQGuiMainMenu extends GuiMainMenu
{
	public static boolean initializing = false;
	
	private static final Logger logger = LogManager.getLogger();
	/** The RNG used by the Main Menu Screen. */
	private static final Random rand = new Random();
	/** The splash message. */
	// private String splashText;
	/** Timer used to rotate the panorama, increases every tick. */
	public static int panoramaTimer = 0;
	/**
	 * Texture allocated for the current viewport of the main menu's panorama
	 * background.
	 */
	private DynamicTexture viewportTexture;
	private final Object field_104025_t = new Object();
	private String field_92025_p;
	private String field_146972_A;
	private String field_104024_v;
	// private static final ResourceLocation splashTexts = new
	// ResourceLocation("texts/splashes.txt");
	/** An array of all the paths to the panorama pictures. */
	private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[]
	{
		new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"),
		new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"),
		new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png")
	};
	public static final String field_96138_a = "Please click " + EnumChatFormatting.UNDERLINE + "here" + EnumChatFormatting.RESET + " for more information.";
	private int field_92024_r;
	private int field_92023_s;
	private int field_92022_t;
	private int field_92021_u;
	private int field_92020_v;
	private int field_92019_w;
	private ResourceLocation field_110351_G;
	
	public AQCharacterManager charManager = AQCharacterManager.instance;
	
	public AQGuiMainMenu()
	{
		this.field_146972_A = field_96138_a;
		// this.splashText = "We cracked it!";
		
		/*
		 * BufferedReader bufferedreader = null;
		 * 
		 * try { ArrayList arraylist = new ArrayList(); bufferedreader = new
		 * BufferedReader(new InputStreamReader(
		 * Minecraft.getMinecraft().getResourceManager
		 * ().getResource(splashTexts).getInputStream(), Charsets.UTF_8));
		 * String s;
		 * 
		 * while((s = bufferedreader.readLine()) != null) { s = s.trim();
		 * 
		 * if(!s.isEmpty()) { arraylist.add(s); } }
		 * 
		 * if(!arraylist.isEmpty()) { do { this.splashText = "We cracked it!";//
		 * (String)arraylist.get(rand.nextInt(arraylist.size())); }
		 * while(this.splashText.hashCode() == 125780783); } } catch(IOException
		 * ioexception1) {} finally { if(bufferedreader != null) { try {
		 * bufferedreader.close(); } catch(IOException ioexception) {} } }
		 */
		rand.nextFloat();
		this.field_92025_p = "";
		
		if(!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.func_153193_b())
		{
			this.field_92025_p = I18n.format("title.oldgl1", new Object[0]);
			this.field_146972_A = I18n.format("title.oldgl2", new Object[0]);
			this.field_104024_v = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
		}
	}
	
	static int maxTimer = MathHelper.floor_double(400 * 2 * Math.PI);
	
	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen()
	{
		++panoramaTimer;
		panoramaTimer %= maxTimer;
	}
	
	/**
	 * Returns true if this GUI should pause the game when it is displayed in
	 * single-player
	 */
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}
	
	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_)
	{}
	
	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui()
	{
		this.viewportTexture = new DynamicTexture(256, 256);
		this.field_110351_G = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
		
		int i = 40;
		
		switch(checkCharacter())
		{
			case TRUE:
				this.buttonList.add(new GuiButton(1, 20, i, I18n.format("menu.singleplayer")));
				i += 24;
				this.buttonList.add(new GuiButton(2, 20, i, I18n.format("menu.multiplayer")));
				i += 24;
				this.buttonList.add(new GuiButton(21, 20, i, I18n.format("menu.character")));
				break;
			case FALSE:
				this.buttonList.add(new GuiButton(22, 20, i, I18n.format("menu.createcharacter")));
				break;
			default:
				this.buttonList.add(new GuiButton(23, 20, i, I18n.format("menu.refresh")));
				break;
		}
		
		i += 36;
		this.buttonList.add(new GuiButton(0, 20, i, I18n.format("menu.options")));
		i += 24;
		this.buttonList.add(new GuiButton(4, 20, i, I18n.format("menu.quit")));
		
		this.buttonList.add(new GuiButtonLanguage(5, 230, i));
		
		synchronized(this.field_104025_t)
		{
			this.field_92023_s = this.fontRendererObj.getStringWidth(this.field_92025_p);
			this.field_92024_r = this.fontRendererObj.getStringWidth(this.field_146972_A);
			int j = Math.max(this.field_92023_s, this.field_92024_r);
			this.field_92022_t = (this.width - j) / 2;
			this.field_92021_u = ((GuiButton) this.buttonList.get(0)).yPosition - 24;
			this.field_92020_v = this.field_92022_t + j;
			this.field_92019_w = this.field_92021_u + 24;
		}
	}
	
	protected WebResult checkCharacter()
	{
		WebResult result = AQCharacterManager.instance.checkCharacter();
		
		if(result == WebResult.ERROR)
			new AQOkDialog(this, "menu.servererror").show();
		
		return result;
	}
	
	@Override
	protected void actionPerformed(GuiButton p_146284_1_)
	{
		if(p_146284_1_.id == 0)
		{
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
		}
		
		if(p_146284_1_.id == 5)
		{
			this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
		}
		
		if(p_146284_1_.id == 1)
		{
			this.mc.displayGuiScreen(new GuiSelectWorld(this));
		}
		
		if(p_146284_1_.id == 2)
		{
			this.mc.displayGuiScreen(new GuiMultiplayer(this));
		}
		
		if(p_146284_1_.id == 4)
		{
			this.mc.shutdown();
		}
		
		if(p_146284_1_.id == 6)
		{
			this.mc.displayGuiScreen(new GuiModList(this));
		}
		
		if(p_146284_1_.id == 11)
		{
			this.mc.launchIntegratedServer("Demo_World", "Demo_World", DemoWorldServer.demoWorldSettings);
		}
		
		if(p_146284_1_.id == 12)
		{
			ISaveFormat isaveformat = this.mc.getSaveLoader();
			WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");
			
			if(worldinfo != null)
			{
				GuiYesNo guiyesno = GuiSelectWorld.func_152129_a(this, worldinfo.getWorldName(), 12);
				this.mc.displayGuiScreen(guiyesno);
			}
		}
		
		if(p_146284_1_.id == 14)
		{
			this.func_140005_i();
		}
		
		if(p_146284_1_.id == 21)
		{
			this.mc.displayGuiScreen(new AQGuiCharacter());
		}
		
		if(p_146284_1_.id == 22)
		{
			this.mc.displayGuiScreen(new AQGuiCreateCharacter());
		}
		
		if(p_146284_1_.id == 23)
		{
			this.mc.displayGuiScreen(this);
		}
		
	}
	
	private void func_140005_i()
	{
		RealmsBridge realmsbridge = new RealmsBridge();
		realmsbridge.switchToRealms(this);
	}
	
	@Override
	public void confirmClicked(boolean p_73878_1_, int p_73878_2_)
	{
		if(p_73878_1_ && p_73878_2_ == 12)
		{
			ISaveFormat isaveformat = this.mc.getSaveLoader();
			isaveformat.flushCache();
			isaveformat.deleteWorldDirectory("Demo_World");
			this.mc.displayGuiScreen(this);
		}
		else if(p_73878_2_ == 13)
		{
			if(p_73878_1_)
			{
				try
				{
					Class oclass = Class.forName("java.awt.Desktop");
					Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object) null, new Object[0]);
					oclass.getMethod("browse", new Class[]
					{
						URI.class
					}).invoke(object, new Object[]
					{
						new URI(this.field_104024_v)
					});
				}
				catch(Throwable throwable)
				{
					logger.error("Couldn\'t open link", throwable);
				}
			}
			
			this.mc.displayGuiScreen(this);
		}
	}
	
	/**
	 * Draws the main menu panorama
	 */
	@SuppressWarnings("unused")
	public void drawPanorama(int p_73970_1_, int p_73970_2_, float p_73970_3_)
	{
		Tessellator tessellator = Tessellator.instance;
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDepthMask(false);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		byte b0 = 8;
		
		for(int k = 0; k < b0 * b0; ++k)
		{
			GL11.glPushMatrix();
			float f1 = ((float) (k % b0) / (float) b0 - 0.5F) / 64.0F;
			float f2 = ((float) (k / b0) / (float) b0 - 0.5F) / 64.0F;
			float f3 = 0.0F;
			GL11.glTranslatef(f1, f2, f3);
			GL11.glRotatef(MathHelper.sin((panoramaTimer + p_73970_3_) / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(-(panoramaTimer + p_73970_3_) * 0.1F, 0.0F, 1.0F, 0.0F);
			
			for(int l = 0; l < 6; ++l)
			{
				GL11.glPushMatrix();
				
				if(l == 1)
				{
					GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
				}
				
				if(l == 2)
				{
					GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
				}
				
				if(l == 3)
				{
					GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
				}
				
				if(l == 4)
				{
					GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
				}
				
				if(l == 5)
				{
					GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
				}
				
				this.mc.getTextureManager().bindTexture(titlePanoramaPaths[l]);
				tessellator.startDrawingQuads();
				tessellator.setColorRGBA_I(16777215, 255 / (k + 1));
				float f4 = 0.0F;
				tessellator.addVertexWithUV(-1.0D, -1.0D, 1.0D, 0.0F + f4, 0.0F + f4);
				tessellator.addVertexWithUV(1.0D, -1.0D, 1.0D, 1.0F - f4, 0.0F + f4);
				tessellator.addVertexWithUV(1.0D, 1.0D, 1.0D, 1.0F - f4, 1.0F - f4);
				tessellator.addVertexWithUV(-1.0D, 1.0D, 1.0D, 0.0F + f4, 1.0F - f4);
				tessellator.draw();
				GL11.glPopMatrix();
			}
			
			GL11.glPopMatrix();
			GL11.glColorMask(true, true, true, false);
		}
		
		tessellator.setTranslation(0.0D, 0.0D, 0.0D);
		GL11.glColorMask(true, true, true, true);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPopMatrix();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	
	/**
	 * Rotate and blurs the skybox view in the main menu
	 */
	@SuppressWarnings("unused")
	private void rotateAndBlurSkybox(float p_73968_1_)
	{
		this.mc.getTextureManager().bindTexture(this.field_110351_G);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glColorMask(true, true, true, false);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		byte b0 = 3;
		
		for(int i = 0; i < b0; ++i)
		{
			tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F / (i + 1));
			int j = this.width;
			int k = this.height;
			float f1 = (i - b0 / 2) / 256.0F;
			tessellator.addVertexWithUV(j, k, this.zLevel, 0.0F + f1, 1.0D);
			tessellator.addVertexWithUV(j, 0.0D, this.zLevel, 1.0F + f1, 1.0D);
			tessellator.addVertexWithUV(0.0D, 0.0D, this.zLevel, 1.0F + f1, 0.0D);
			tessellator.addVertexWithUV(0.0D, k, this.zLevel, 0.0F + f1, 0.0D);
		}
		
		tessellator.draw();
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glColorMask(true, true, true, true);
	}
	
	/**
	 * Renders the skybox in the main menu
	 */
	public void renderSkybox(int p_73971_1_, int p_73971_2_, float p_73971_3_)
	{
		this.mc.getFramebuffer().unbindFramebuffer();
		GL11.glViewport(0, 0, 256, 256);
		this.drawPanorama(p_73971_1_, p_73971_2_, p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.mc.getFramebuffer().bindFramebuffer(true);
		GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		float f1 = this.width > this.height ? 120.0F / this.width : 120.0F / this.height;
		float f2 = this.height * f1 / 256.0F;
		float f3 = this.width * f1 / 256.0F;
		tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
		int k = this.width;
		int l = this.height;
		tessellator.addVertexWithUV(0.0D, l, this.zLevel, 0.5F - f2, 0.5F + f3);
		tessellator.addVertexWithUV(k, l, this.zLevel, 0.5F - f2, 0.5F - f3);
		tessellator.addVertexWithUV(k, 0.0D, this.zLevel, 0.5F + f2, 0.5F - f3);
		tessellator.addVertexWithUV(0.0D, 0.0D, this.zLevel, 0.5F + f2, 0.5F + f3);
		tessellator.draw();
	}
	
	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
	{
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		this.renderSkybox(p_73863_1_, p_73863_2_, p_73863_3_);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		Tessellator tessellator = Tessellator.instance;
		this.drawGradientRect(0, 0, this.width, this.height, -2130706433, 16777215);
		this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
		
		tessellator.setColorOpaque_I(-1);
		/*
		 * GL11.glPushMatrix(); GL11.glTranslatef(this.width / 2 + 90, 70.0F,
		 * 0.0F); GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F); float f1 = 1.8F -
		 * MathHelper.abs(MathHelper.sin(Minecraft.getSystemTime() % 1000L /
		 * 1000.0F * (float) Math.PI * 2.0F) * 0.1F); f1 = f1 * 100.0F /
		 * (this.fontRendererObj.getStringWidth(this.splashText) + 32);
		 * GL11.glScalef(f1, f1, f1);
		 * this.drawCenteredString(this.fontRendererObj, this.splashText, 0, -8,
		 * -256); GL11.glPopMatrix();
		 */
		String s1 = "COPYLEFT AQUA & ERJEXY!";
		
		this.drawString(this.fontRendererObj, s1, this.width - this.fontRendererObj.getStringWidth(s1) - 2, this.height - 10, -1);
		
		if(this.field_92025_p != null && this.field_92025_p.length() > 0)
		{
			drawRect(this.field_92022_t - 2, this.field_92021_u - 2, this.field_92020_v + 2, this.field_92019_w - 1, 1428160512);
			this.drawString(this.fontRendererObj, this.field_92025_p, this.field_92022_t, this.field_92021_u, -1);
			this.drawString(
				this.fontRendererObj,
				this.field_146972_A,
				(this.width - this.field_92024_r) / 2,
				((GuiButton) this.buttonList.get(0)).yPosition - 12,
				-1);
		}
		
		for(int i = 0; i < this.buttonList.size(); ++i)
		{
			((GuiButton) this.buttonList.get(i)).drawButton(this.mc, p_73863_1_, p_73863_2_);
		}
		
		for(int i = 0; i < this.labelList.size(); ++i)
		{
			((GuiLabel) this.labelList.get(i)).func_146159_a(this.mc, p_73863_1_, p_73863_2_);
		}
	}
	
	/**
	 * Called when the mouse is clicked.
	 */
	@Override
	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_)
	{
		super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
		synchronized(this.field_104025_t)
		{
			if(this.field_92025_p.length() > 0 && p_73864_1_ >= this.field_92022_t && p_73864_1_ <= this.field_92020_v && p_73864_2_ >= this.field_92021_u
				&& p_73864_2_ <= this.field_92019_w)
			{
				GuiConfirmOpenLink guiconfirmopenlink = new GuiConfirmOpenLink(this, this.field_104024_v, 13, true);
				guiconfirmopenlink.func_146358_g();
				this.mc.displayGuiScreen(guiconfirmopenlink);
			}
		}
	}
}
