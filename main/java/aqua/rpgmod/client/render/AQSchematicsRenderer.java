package aqua.rpgmod.client.render;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import aqua.rpgmod.geometry.AQBox;
import aqua.rpgmod.geometry.AQDimensions;
import aqua.rpgmod.geometry.AQPoint3D;
import aqua.rpgmod.geometry.AQQuad;
import aqua.rpgmod.geometry.AQSide3D;
import aqua.rpgmod.geometry.AQValue3D;
import aqua.rpgmod.world.schematics.AQSchematics;
import aqua.rpgmod.world.schematics.AQSchematics.AQTileInfo;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

public class AQSchematicsRenderer
{
	int renderList;
	AQSchematics schx = null;
	public static final AQSchematicsRenderer instance = new AQSchematicsRenderer();
	
	public AQSchematicsRenderer()
	{
		this.renderList = GLAllocation.generateDisplayLists(1);
	}
	
	public void freeSchematics()
	{
		if(AQSchematicsRenderer.instance.schx != null)
		{
			AQSchematicsRenderer.instance.schx = null;
			GL11.glDeleteLists(this.renderList, 1);
		}
	}
	
	private static void preRenderBlocks()
	{
		GL11.glNewList(instance.renderList, GL11.GL_COMPILE_AND_EXECUTE);
		GL11.glPushMatrix();
		float f = 1.000001F;
		GL11.glTranslatef(-8.0F, -8.0F, -8.0F);
		GL11.glScalef(f, f, f);
		GL11.glTranslatef(8.0F, 8.0F, 8.0F);
		Tessellator.instance.startDrawingQuads();
	}
	
	private static void postRenderBlocks()
	{
		Tessellator.instance.draw();
		GL11.glPopMatrix();
		GL11.glEndList();
	}
	
	public void render(AQSchematics schx, float ticks)
	{
		if(this.schx == schx)
		{
			GL11.glCallList(this.renderList);
			return;
		}
		
		this.schx = schx;
		AQDimensions dims = schx.dimensions();
		
		List<AQSide3D> sides = new ArrayList<AQSide3D>();
		
		preRenderBlocks();
		RenderBlocks specialRenderer = new RenderBlocks(schx);
		
		for(int z = 0; z < dims.z; ++z)
		{
			for(int y = 0; y < dims.y; ++y)
			{
				for(int x = 0; x < dims.x; ++x)
				{
					Block block = schx.getBlock(x, y, z);
					
					if(block.getMaterial() == Material.air)
						continue;
					
					AQPoint3D coords = new AQPoint3D(x, y, z);
					sides.clear();
					
					for(AQSide3D side : AQSide3D.values())
					{
						int value = coords.get(side.axis);
						int nvalue = value + side.sign;
						int max = dims.get(side.axis);
						
						if(nvalue >= 0 && nvalue < max)
						{
							AQValue3D ncoords = new AQValue3D(coords);
							ncoords.set(side.axis, nvalue);
							
							Block neightbour = schx.getBlock(ncoords.x, ncoords.y, ncoords.z);
							
							if(neightbour.getMaterial() != Material.air && neightbour.renderAsNormalBlock())
								continue;
						}
						
						sides.add(side);
					}
					
					if(!block.renderAsNormalBlock())
					{
						if(!sides.isEmpty())
						{
							GL11.glEnable(GL11.GL_TEXTURE_2D);
							
							GL11.glPushMatrix();
							GL11.glScalef(0.8f, 0.8f, 0.8f);
							specialRenderer.renderBlockByRenderType(block, x, y, z);
							GL11.glPopMatrix();
						}
						
						continue;
					}
					
					GL11.glPushMatrix();
					GL11.glScalef(0.8f, 0.8f, 0.8f);
					renderBlock(block, coords, schx.getBlockMetadata(x, y, z), sides, true);
					GL11.glPopMatrix();
				}
			}
		}
		
		for(int i = 0; i < schx.getTileInfoSize(); ++i)
		{
			AQTileInfo info = schx.getTileInfo(i);
			
			if(info == null)
				continue;
			
			TileEntity tile = info.tile;
			TileEntityRendererDispatcher.instance.renderTileEntity(tile, ticks);
		}
		
		postRenderBlocks();
	}
	
	protected static void renderBlock(Block block, AQPoint3D coords, int meta, List<AQSide3D> sides, boolean useTexture)
	{
		IIcon icon = null;
		
		for(AQSide3D side : sides)
		{
			if(useTexture)
				icon = block.getIcon(AQSide3D.getMCside(side), meta);
			
			renderBlockSide(icon, coords, side);
		}
	}
	
	protected static void renderBlockSide(IIcon icon, AQPoint3D pos, AQSide3D side)
	{
		AQBox box = new AQBox(pos, pos);
		AQQuad quad = box.getSideQuad(side, -0.001);
		Tessellator.instance.setColorRGBA_F(0.5f, 0.5f, 0.5f, 0.75f);
		
		if(icon != null)
		{
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			
			Tessellator.instance.addVertexWithUV(quad.points[0].x, quad.points[0].y, quad.points[0].z, icon.getMinU(), icon.getMaxV());
			Tessellator.instance.addVertexWithUV(quad.points[1].x, quad.points[1].y, quad.points[1].z, icon.getMinU(), icon.getMinV());
			Tessellator.instance.addVertexWithUV(quad.points[2].x, quad.points[2].y, quad.points[2].z, icon.getMaxU(), icon.getMinV());
			Tessellator.instance.addVertexWithUV(quad.points[3].x, quad.points[3].y, quad.points[3].z, icon.getMaxU(), icon.getMaxV());
		}
		else
		{
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			
			Tessellator.instance.addVertex(quad.points[0].x, quad.points[0].y, quad.points[0].z);
			Tessellator.instance.addVertex(quad.points[1].x, quad.points[1].y, quad.points[1].z);
			Tessellator.instance.addVertex(quad.points[2].x, quad.points[2].y, quad.points[2].z);
			Tessellator.instance.addVertex(quad.points[3].x, quad.points[3].y, quad.points[3].z);
		}
	}
}
