package aqua.rpgmod.client.gui;

import net.minecraft.client.renderer.Tessellator;

public class AQGuiUtils
{
	static int id = 400;
	
	public static final int reservePages(int count)
	{
		int id0 = id;
		id += count;
		
		return id0;
	}
	
	public static void renderImage(int x, int y, int w, int h, float u1, float v1, float u2, float v2, float zLevel)
	{
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(x + 0, y + h, zLevel, u1, v2);
		tessellator.addVertexWithUV(x + w, y + h, zLevel, u2, v2);
		tessellator.addVertexWithUV(x + w, y + 0, zLevel, u2, v1);
		tessellator.addVertexWithUV(x + 0, y + 0, zLevel, u1, v1);
		tessellator.draw();
	}
	
}
