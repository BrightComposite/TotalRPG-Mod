package aqua.rpgmod.client.render;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import api.player.render.RenderPlayerAPI;
import api.player.render.RenderPlayerBase;
import aqua.rpgmod.AquaMod;
import aqua.rpgmod.client.render.AQTextureMap.TextureType;

public class AQRenderPlayerBase extends RenderPlayerBase
{
	public AQRenderPlayerBase(RenderPlayerAPI renderPlayerAPI)
	{
		super(renderPlayerAPI);
	}
	
	@Override
	public void renderPlayer(AbstractClientPlayer player, double x, double y, double z, float f1, float f2)
	{
		double newY = (player != Minecraft.getMinecraft().thePlayer) ? y - AquaMod.proxy.getWrapper(player).yOffset : y;
		super.renderPlayer(player, x, newY, z, f1, f2);
	}
	
	@Override
	public void renderFirstPersonArm(EntityPlayer player)
	{
		AQTextureManager.bindTexture(player, TextureType.SKIN, null);
		super.renderFirstPersonArm(player);
		
		ItemStack chestArmor = player.inventory.armorInventory[2];
		
		if(chestArmor != null)
		{
			Minecraft.getMinecraft().renderEngine.bindTexture(RenderBiped.getArmorResource(player, chestArmor, 1, null));
			
			GL11.glPushMatrix();
			GL11.glTranslated(0.15f, -0.35f, 0.0f);
			GL11.glScaled(1.5f, 1.5f, 1.5f);
			super.renderFirstPersonArm(player);
			GL11.glPopMatrix();
		}
		
		GL11.glPopMatrix();
		GL11.glPushMatrix();
	}
}
