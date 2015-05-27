package aqua.rpgmod.client.render;

import java.util.Iterator;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import aqua.rpgmod.geometry.AQPoint3D;
import aqua.rpgmod.geometry.AQPoint3Dd;
import aqua.rpgmod.geometry.AQQuad;
import aqua.rpgmod.geometry.AQSide3D;
import aqua.rpgmod.player.AQPlayerWrapper;
import aqua.rpgmod.world.region.AQRegion;
import aqua.rpgmod.world.region.AQRegionProvider;
import aqua.rpgmod.world.region.AQSelection;
import aqua.rpgmod.world.region.AQWorldRegion;
import aqua.rpgmod.world.region.actions.AQRegionActionManager;
import aqua.rpgmod.world.schematics.AQSchematics;

@SideOnly(Side.CLIENT)
public class AQRegionRenderer
{
	public static AQRegionRenderer instance = new AQRegionRenderer();
	public boolean shouldRender = false;
	
	AQSelection points[] = new AQSelection[3];
	static final String pointNames[] = new String[]
	{
		"1st pt", "2st pt", "Buffer pos",
	};
	
	public void setPoint(int index, EntityPlayer player, AQRegionProvider provider, boolean use)
	{
		AQPoint3D point = AQRegionActionManager.instance.getPoint(index);
		
		if(point != null && use)
		{
			if(this.points[index] != null)
			{
				if(point.equals(this.points[index].shape.pos()))
					return;
				
				this.points[index].free();
			}
			
			this.points[index] = new AQSelection(pointNames[index], provider.root, point, point, player, new float[]
			{
				1.0f, 1.0f, 0.0f
			}, index == 1 ? 0.002f : 0.001f);
			
			this.points[index].add();
		}
		else if(this.points[index] != null)
		{
			this.points[index].free();
			this.points[index] = null;
		}
	}
	
	public void render(EntityPlayer player, AQRegionProvider provider, float ticks)
	{
		if(!this.shouldRender)
			return;
		
		AQWorldRegion root = provider.root;
		AQPoint3Dd playerPos = AQPlayerWrapper.getPlayerPos(player, ticks);
		
		AQRegionActionManager manager = AQRegionActionManager.instance;
		manager.checkWorld(player.worldObj);
		
		GL11.glPushMatrix();
		{
			GL11.glDisable(GL11.GL_LIGHTING);
			
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			
			GL11.glTranslated(-playerPos.x, -playerPos.y, -playerPos.z);
			
			GL11.glLineWidth(2.0f);
			
			AQSchematics schx = manager.getBuffer();
			
			if(schx != null)
			{
				if(manager.getPoint(2) == null)
					manager.updatePos(playerPos);
				
				AQPoint3D pos = manager.getPoint(2);
				
				GL11.glDisable(GL11.GL_CULL_FACE);
				GL11.glPushMatrix();
				GL11.glTranslated(pos.x, pos.y, pos.z);
				AQSchematicsRenderer.instance.render(schx, ticks);
				GL11.glPopMatrix();
				GL11.glEnable(GL11.GL_CULL_FACE);
			}
			else
			{
				AQSchematicsRenderer.instance.freeSchematics();
			}
			
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			
			if(schx == null)
			{
				setPoint(0, player, provider, true);
				setPoint(1, player, provider, true);
				setPoint(2, player, provider, false);
			}
			else
			{
				setPoint(0, player, provider, false);
				setPoint(1, player, provider, false);
				setPoint(2, player, provider, true);
			}
			
			renderRegion(player, playerPos, root);
			
			if(manager.getSelection() != null)
				renderRegion(player, playerPos, manager.getSelection());
			
			GL11.glLineWidth(1.0f);
			
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}
		GL11.glPopMatrix();
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	
	public void renderRegion(EntityPlayer player, AQPoint3Dd entityPos, AQRegion region)
	{
		float distance = 0.0f;
		boolean shouldDraw = region.shouldDraw()
			&& (region instanceof AQSelection == false || ((AQSelection) region).player == player || region.name.contains(AQRegionActionManager.selectionName));
		
		if(shouldDraw && !region.permanentView())
		{
			distance = (float) region.shape.distanceTo(entityPos);
			
			if(distance > 64.0f)
				shouldDraw = false;
		}
		
		if(shouldDraw)
		{
			for(AQSide3D side : AQSide3D.values())
				renderSide(region, side, distance);
		}
		
		for(Iterator<AQRegion> i = region.getChildren().iterator(); i.hasNext();)
			renderRegion(player, entityPos, i.next());
	}
	
	public static void renderSide(AQRegion region, AQSide3D side, float distance)
	{
		AQQuad quad = region.getSideQuad(side, 0.01);
		
		float[] color = region.color();
		float alpha = (distance == 0.0f ? 1.0f : 1.0f - Math.abs(distance - 28.0f) / 36.0f) * 0.5f;
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glColor4f(color[0], color[1], color[2], alpha * 0.25f);
		Tessellator.instance.startDrawing(GL11.GL_QUADS);
		renderQuad(quad);
		Tessellator.instance.draw();
		
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glColor4f(color[0] * 0.75f, color[1] * 0.75f, color[2] * 0.75f, alpha);
		Tessellator.instance.startDrawing(GL11.GL_LINE_LOOP);
		renderQuad(quad);
		Tessellator.instance.draw();
	}
	
	protected static void renderQuad(AQQuad quad)
	{
		for(int i = 0; i < 4; ++i)
			Tessellator.instance.addVertex(quad.points[i].x, quad.points[i].y, quad.points[i].z);
	}
}
