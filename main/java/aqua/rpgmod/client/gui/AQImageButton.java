package aqua.rpgmod.client.gui;

import aqua.rpgmod.client.gui.menu.AQMenuPage;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class AQImageButton extends AQGuiButton
{
	public boolean drawBackground = true;
	public ResourceLocation image = null;
	public int imageWidth;
	public int imageHeight;
	
	public float u1 = 0.0f;
	public float v1 = 0.0f;
	public float u2 = 1.0f;
	public float v2 = 1.0f;
	
	public AQImageButton(AQMenuPage owner, int id, int x, int y, int width, int height)
	{
		super(owner, id, x, y, width, height, "");
		
		this.drawHint = true;
		this.imageWidth = width;
		this.imageHeight = height;
	}
	
	@Override
	protected void drawForeground(Minecraft mc)
	{
		drawImage(
			mc,
			this.image,
			(this.width - this.imageWidth) / 2,
			(this.height - this.imageHeight) / 2,
			this.imageWidth,
			this.imageHeight,
			this.u1,
			this.v1,
			this.u2,
			this.v2);
	}
}
