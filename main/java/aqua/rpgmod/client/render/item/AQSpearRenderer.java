package aqua.rpgmod.client.render.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

import aqua.rpgmod.client.render.AQRenderUtils;
import aqua.rpgmod.item.AQItemSpear;

import cpw.mods.fml.client.FMLClientHandler;

public class AQSpearRenderer implements IItemRenderer
{
	private Minecraft mc;
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		return type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON;
	}
	
	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		return false;
	}
	
	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object ... data)
	{
		if(this.mc == null)
		{
			this.mc = FMLClientHandler.instance().getClient();
			new RenderItem();
		}
		
		this.mc.renderEngine.bindTexture(TextureMap.locationItemsTexture);
		Tessellator tessellator = Tessellator.instance;
		
		IIcon icon = ((AQItemSpear) item.getItem()).fullModel;
		
		GL11.glPushMatrix();
		
		if(type == ItemRenderType.EQUIPPED)
			GL11.glRotatef(-40F, 0F, 0F, 1.0F);
		
		GL11.glTranslatef(-0.5F, -0.5F, 0);
		GL11.glScalef(2, 2, 1);
		
		ItemRenderer.renderItemIn2D(
			tessellator,
			icon.getMaxU(),
			icon.getMinV(),
			icon.getMinU(),
			icon.getMaxV(),
			icon.getIconWidth(),
			icon.getIconHeight(),
			1F / 16F);
		
		if(item.hasEffect(0))
			AQRenderUtils.renderEnchantmentEffects(tessellator);
		
		GL11.glPopMatrix();
	}
}
