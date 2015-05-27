package aqua.rpgmod.client.gui;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import aqua.rpgmod.client.gui.menu.AQMenuPage;

public class AQGuiButton extends GuiButton
{
	public static final int MAX_HINT_WIDTH = 128;
	
	protected ArrayList<String> hint = new ArrayList<String>();
	public boolean drawHint = false;
	public AQMenuPage owner;
	
	public AQGuiButton(AQMenuPage owner, int id, int x, int y, int width, int height, String text)
	{
		super(id, x, y, width, height, text);
		this.owner = owner;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY)
	{
		if(this.visible)
		{
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			
			drawSurface(mc, mouseX, mouseY);
			drawForeground(mc);
		}
	}
	
	public void setHint(String text)
	{
		this.hint.clear();
		String strings[] = text.split("\n");
		
		if(this.owner.mc == null)
		{
			for(String s : strings)
				this.hint.add(s);
			
			return;
		}
		
		for(String s : strings)
			this.hint.addAll(this.owner.mc.fontRenderer.listFormattedStringToWidth(s, MAX_HINT_WIDTH));
	}
	
	public void addHintLine(String line)
	{
		this.hint.add(line);
	}
	
	public boolean isMouseOver(int x, int y)
	{
		return x >= this.xPosition && y >= this.yPosition && x < this.xPosition + this.width && y < this.yPosition + this.height;
	}
	
	protected void drawSurface(Minecraft mc, int mouseX, int mouseY)
	{
		mc.getTextureManager().bindTexture(buttonTextures);
		int k = !this.enabled ? 0 : isMouseOver(mouseX, mouseY) ? 2 : 1;
		this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + k * 20, this.width / 2, this.height);
		this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + k * 20, this.width / 2, this.height);
		this.mouseDragged(mc, mouseX, mouseY);
	}
	
	protected void drawForeground(Minecraft mc)
	{
		drawCenteredText(mc, this.width / 2, (this.height - 8) / 2, this.displayString);
	}
	
	protected void drawCenteredText(Minecraft mc, int x, int y, String text)
	{
		if(text.isEmpty())
			return;
		
		int l = 14737632;
		
		if(this.packedFGColour != 0)
		{
			l = this.packedFGColour;
		}
		else if(!this.enabled)
		{
			l = 10526880;
		}
		else if(this.field_146123_n)
		{
			l = 16777120;
		}
		
		this.drawCenteredString(mc.fontRenderer, text, this.xPosition + x, this.yPosition + y, l);
	}
	
	protected void drawImage(Minecraft mc, ResourceLocation image, int x, int y, int width, int height, float u1, float v1, float u2, float v2)
	{
		if(image == null)
			return;
		
		mc.getTextureManager().bindTexture(image);
		AQGuiUtils.renderImage(this.xPosition + x, this.yPosition + y, width, height, u1, v1, u2, v2, this.zLevel);
	}
	
	protected void drawImage(Minecraft mc, ResourceLocation image, int x, int y, int width, int height)
	{
		if(image == null)
			return;
		
		mc.getTextureManager().bindTexture(image);
		AQGuiUtils.renderImage(this.xPosition + x, this.yPosition + y, width, height, 0.0f, 0.0f, 1.0f, 1.0f, this.zLevel);
	}
	
	public void drawHint(int x, int y, FontRenderer font)
	{
		if(this.hint.isEmpty())
			return;
		
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		int k = 0;
		
		for(Iterator<String> i = this.hint.iterator(); i.hasNext();)
		{
			int l = font.getStringWidth(i.next());
			
			if(l > k)
			{
				k = l;
			}
		}
		
		int j2 = x + 12;
		int k2 = y - 12;
		int i1 = 8;
		
		if(this.hint.size() > 1)
		{
			i1 += 2 + (this.hint.size() - 1) * 10;
		}
		
		int j1 = -267386864;
		this.drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
		this.drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
		this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
		this.drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
		this.drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
		int k1 = 1347420415;
		int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
		this.drawGradientRect(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
		this.drawGradientRect(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
		this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
		this.drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);
		
		boolean started = false;
		for(Iterator<String> i = this.hint.iterator(); i.hasNext();)
		{
			String s1 = i.next();
			font.drawStringWithShadow(s1, j2, k2, -1);
			
			if(!started)
			{
				k2 += 2;
				started = true;
			}
			
			k2 += 10;
		}
	}
}
