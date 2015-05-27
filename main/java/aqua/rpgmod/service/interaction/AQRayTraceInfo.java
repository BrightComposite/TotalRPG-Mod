package aqua.rpgmod.service.interaction;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import aqua.rpgmod.AquaMod;
import aqua.rpgmod.item.AQItemRegionWand;
import aqua.rpgmod.player.AQPlayerWrapper;

public class AQRayTraceInfo
{
	public int x, y, z, sideHit;
	public float xh, yh, zh;
	public EntityPlayer player;
	public MovingObjectPosition.MovingObjectType type;
	public AQInteractionInfo interactionInfo = null;
	protected final Minecraft mc;
	
	public AQRayTraceInfo(Minecraft mc)
	{
		this.mc = mc;
		update();
	}
	
	public void update()
	{
		if(this.mc == null || this.mc.thePlayer == null || this.mc.objectMouseOver == null)
			return;
		
		this.x = this.mc.objectMouseOver.blockX;
		this.y = this.mc.objectMouseOver.blockY;
		this.z = this.mc.objectMouseOver.blockZ;
		this.sideHit = this.mc.objectMouseOver.sideHit;
		
		this.xh = (float) (this.mc.objectMouseOver.hitVec.xCoord - this.x);
		this.yh = (float) (this.mc.objectMouseOver.hitVec.yCoord - this.y);
		this.zh = (float) (this.mc.objectMouseOver.hitVec.zCoord - this.z);
		
		this.player = this.mc.thePlayer;
		this.type = this.mc.objectMouseOver.typeOfHit;
		
		ItemStack stack = this.getEquippedItem();
		
		if(stack != null && stack.getItem() instanceof AQItemRegionWand)
		{
			this.interactionInfo = null;
			return;
		}
		
		this.interactionInfo = AQInteractManager.getInteractionInfo(this);
	}
	
	public boolean interact()
	{
		return getBlock().onBlockActivated(this.mc.theWorld, this.x, this.y, this.z, this.player, this.sideHit, this.xh, this.yh, this.zh);
	}
	
	public Block getBlock()
	{
		return this.mc.theWorld.getBlock(this.x, this.y, this.z);
	}
	
	public Entity getEntity()
	{
		return this.mc.objectMouseOver.entityHit;
	}

	public AQPlayerWrapper getPlayerWrapper()
	{
		return AquaMod.proxy.getWrapper(this.player);
	}
	
	public ItemStack getEquippedItem()
	{
		return this.mc.thePlayer.getCurrentEquippedItem();
	}
}
