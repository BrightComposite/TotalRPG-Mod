package aqua.rpgmod.player;

import io.netty.buffer.ByteBuf;
import aqua.rpgmod.AquaMod;
import aqua.rpgmod.item.AQItemRegionWand;
import aqua.rpgmod.service.interaction.AQRayTraceInfo;
import aqua.rpgmod.service.network.AQMessage;
import aqua.rpgmod.service.network.AQTranslator;
import aqua.rpgmod.service.network.AQMessage.PlayerMessage;
import aqua.rpgmod.world.region.actions.AQRegionActionManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.network.play.client.C11PacketEnchantItem;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;

@SideOnly(Side.CLIENT)
public class AQPlayerControllerMP extends PlayerControllerMP
{
	private final Minecraft mc;
	private final NetHandlerPlayClient netClientHandler;
	/** PosX of the current block being destroyed */
	private int currentBlockX = -1;
	/** PosY of the current block being destroyed */
	private int currentBlockY = -1;
	/** PosZ of the current block being destroyed */
	private int currentblockZ = -1;
	/** The Item currently being used to destroy a block */
	private ItemStack currentItemHittingBlock;
	/** Current block damage (MP) */
	private float curBlockDamageMP;
	/**
	 * Tick counter, when it hits 4 it resets back to 0 and plays the step sound
	 */
	private float stepSoundTickCounter;
	/** Delays the first damage on the block after the first click on the block */
	private int blockHitDelay;
	/** Tells if the player is hitting a block */
	private boolean isHittingBlock;
	/** Current game type for the player */
	private WorldSettings.GameType currentGameType;
	/** Index of the current item held by the player in the inventory hotbar */
	private int currentPlayerItem;
	public final AQRayTraceInfo rayTrace;
	
	protected boolean selectingBlock = false;
	
	public AQPlayerControllerMP(Minecraft p_i45062_1_, NetHandlerPlayClient p_i45062_2_)
	{
		super(p_i45062_1_, p_i45062_2_);
		
		this.currentGameType = WorldSettings.GameType.SURVIVAL;
		this.mc = p_i45062_1_;
		this.rayTrace = new AQRayTraceInfo(this.mc);
		this.netClientHandler = p_i45062_2_;
	}
	
	/**
	 * Block dig operation in creative mode (instantly digs the block).
	 */
	public static void clickBlockCreative(Minecraft p_78744_0_, PlayerControllerMP p_78744_1_, int p_78744_2_, int p_78744_3_, int p_78744_4_, int p_78744_5_)
	{
		if(!p_78744_0_.theWorld.extinguishFire(p_78744_0_.thePlayer, p_78744_2_, p_78744_3_, p_78744_4_, p_78744_5_))
		{
			p_78744_1_.onPlayerDestroyBlock(p_78744_2_, p_78744_3_, p_78744_4_, p_78744_5_);
		}
	}
	
	public void updateRayTrace()
	{
		this.rayTrace.update();
	}
	
	class AQInteractTranslator implements AQTranslator
	{
		@Override
		public ByteBuf write(ByteBuf buf)
		{
			buf.writeInt(AQPlayerControllerMP.this.rayTrace.x);
			buf.writeInt(AQPlayerControllerMP.this.rayTrace.y);
			buf.writeInt(AQPlayerControllerMP.this.rayTrace.z);
			buf.writeInt(AQPlayerControllerMP.this.rayTrace.sideHit);
			buf.writeFloat(AQPlayerControllerMP.this.rayTrace.xh);
			buf.writeFloat(AQPlayerControllerMP.this.rayTrace.yh);
			buf.writeFloat(AQPlayerControllerMP.this.rayTrace.zh);
			
			return buf;
		}
	}
	
	public void interact()
	{
		switch(this.mc.objectMouseOver.typeOfHit)
		{
			case BLOCK:
				ItemStack stack = this.rayTrace.getEquippedItem();
				
				if(stack != null && stack.getItem() instanceof AQItemRegionWand)
				{
					AQRegionActionManager.instance.execute(
						"point",
						this.rayTrace.player,
						String.valueOf(1),
						String.valueOf(this.rayTrace.x),
						String.valueOf(this.rayTrace.y),
						String.valueOf(this.rayTrace.z));
					
					return;
				}
				
				AquaMod.playerNetwork.sendToServer(new AQMessage(PlayerMessage.INTERACT, new AQInteractTranslator()));
				this.rayTrace.interact();
				break;
			
			case ENTITY:
				interactWithEntitySendOwnPacket(this.mc.thePlayer, this.mc.objectMouseOver.entityHit);
				break;
			default:
		}
	}
	
	/**
	 * Sets player capabilities depending on current gametype. params: player
	 */
	@Override
	public void setPlayerCapabilities(EntityPlayer p_78748_1_)
	{
		this.currentGameType.configurePlayerCapabilities(p_78748_1_.capabilities);
	}
	
	/**
	 * If modified to return true, the player spins around slowly around (0,
	 * 68.5, 0). The GUI is disabled, the view is set to first person, and both
	 * chat and menu are disabled. Unless the server is modified to ignore
	 * illegal stances, attempting to enter a world at all will result in an
	 * immediate kick due to an illegal stance. Appears to be left-over debug,
	 * or demo code.
	 */
	@Override
	public boolean enableEverythingIsScrewedUpMode()
	{
		return false;
	}
	
	/**
	 * Sets the game type for the player.
	 */
	@Override
	public void setGameType(WorldSettings.GameType p_78746_1_)
	{
		this.currentGameType = p_78746_1_;
		this.currentGameType.configurePlayerCapabilities(this.mc.thePlayer.capabilities);
	}
	
	/**
	 * Flips the player around. Args: player
	 */
	@Override
	public void flipPlayer(EntityPlayer p_78745_1_)
	{
		p_78745_1_.rotationYaw = -180.0F;
	}
	
	@Override
	public boolean shouldDrawHUD()
	{
		return this.currentGameType.isSurvivalOrAdventure();
	}
	
	/**
	 * Called when a player completes the destruction of a block
	 */
	@Override
	public boolean onPlayerDestroyBlock(int p_78751_1_, int p_78751_2_, int p_78751_3_, int p_78751_4_)
	{
		ItemStack stack = this.mc.thePlayer.getCurrentEquippedItem();
		if(stack != null && stack.getItem() != null && stack.getItem().onBlockStartBreak(stack, p_78751_1_, p_78751_2_, p_78751_3_, this.mc.thePlayer))
		{
			return false;
		}
		
		if(this.currentGameType.isAdventure() && !this.mc.thePlayer.isCurrentToolAdventureModeExempt(p_78751_1_, p_78751_2_, p_78751_3_))
		{
			return false;
		}
		else if(this.currentGameType.isCreative() && this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)
		{
			return false;
		}
		else
		{
			WorldClient worldclient = this.mc.theWorld;
			Block block = worldclient.getBlock(p_78751_1_, p_78751_2_, p_78751_3_);
			
			if(block.getMaterial() == Material.air)
			{
				return false;
			}
			
			worldclient.playAuxSFX(
				2001,
				p_78751_1_,
				p_78751_2_,
				p_78751_3_,
				Block.getIdFromBlock(block) + (worldclient.getBlockMetadata(p_78751_1_, p_78751_2_, p_78751_3_) << 12));
			int i1 = worldclient.getBlockMetadata(p_78751_1_, p_78751_2_, p_78751_3_);
			boolean flag = block.removedByPlayer(worldclient, this.mc.thePlayer, p_78751_1_, p_78751_2_, p_78751_3_);
			
			if(flag)
			{
				block.onBlockDestroyedByPlayer(worldclient, p_78751_1_, p_78751_2_, p_78751_3_, i1);
			}
			
			this.currentBlockY = -1;
			
			if(!this.currentGameType.isCreative())
			{
				ItemStack itemstack = this.mc.thePlayer.getCurrentEquippedItem();
				
				if(itemstack != null)
				{
					itemstack.func_150999_a(worldclient, block, p_78751_1_, p_78751_2_, p_78751_3_, this.mc.thePlayer);
					
					if(itemstack.stackSize == 0)
					{
						this.mc.thePlayer.destroyCurrentEquippedItem();
					}
				}
			}
			
			return flag;
		}
	}
	
	/**
	 * Called by Minecraft class when the player is hitting a block with an
	 * item. Args: x, y, z, side
	 */
	@Override
	public void clickBlock(int x, int y, int z, int p_78743_4_)
	{
		ItemStack currentItem = this.mc.thePlayer.getCurrentEquippedItem();
		
		if(currentItem != null && currentItem.getItem() instanceof AQItemRegionWand)
		{
			if(!this.selectingBlock)
			{
				AQRegionActionManager.instance.execute("point", this.mc.thePlayer, String.valueOf(0), String.valueOf(x), String.valueOf(y), String.valueOf(z));
				
				this.selectingBlock = true;
			}
			
			this.isHittingBlock = false;
			return;
		}
		
		this.selectingBlock = false;
		
		if(!this.currentGameType.isAdventure() || this.mc.thePlayer.isCurrentToolAdventureModeExempt(x, y, z))
		{
			if(this.currentGameType.isCreative())
			{
				this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(0, x, y, z, p_78743_4_));
				clickBlockCreative(this.mc, this, x, y, z, p_78743_4_);
				this.blockHitDelay = 5;
			}
			else if(!this.isHittingBlock || !this.sameToolAndBlock(x, y, z))
			{
				if(this.isHittingBlock)
				{
					this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(1, this.currentBlockX, this.currentBlockY, this.currentblockZ, p_78743_4_));
				}
				
				this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(0, x, y, z, p_78743_4_));
				Block block = this.mc.theWorld.getBlock(x, y, z);
				boolean flag = block.getMaterial() != Material.air;
				
				if(flag && this.curBlockDamageMP == 0.0F)
				{
					block.onBlockClicked(this.mc.theWorld, x, y, z, this.mc.thePlayer);
				}
				
				if(flag && block.getPlayerRelativeBlockHardness(this.mc.thePlayer, this.mc.thePlayer.worldObj, x, y, z) >= 1.0F)
				{
					this.onPlayerDestroyBlock(x, y, z, p_78743_4_);
				}
				else
				{
					this.isHittingBlock = true;
					this.currentBlockX = x;
					this.currentBlockY = y;
					this.currentblockZ = z;
					this.currentItemHittingBlock = this.mc.thePlayer.getHeldItem();
					this.curBlockDamageMP = 0.0F;
					this.stepSoundTickCounter = 0.0F;
					this.mc.theWorld.destroyBlockInWorldPartially(
						this.mc.thePlayer.getEntityId(),
						this.currentBlockX,
						this.currentBlockY,
						this.currentblockZ,
						(int) (this.curBlockDamageMP * 10.0F) - 1);
				}
			}
		}
	}
	
	/**
	 * Resets current block damage and field_78778_j
	 */
	@Override
	public void resetBlockRemoving()
	{
		if(this.isHittingBlock)
		{
			this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(1, this.currentBlockX, this.currentBlockY, this.currentblockZ, -1));
		}
		
		this.selectingBlock = false;
		this.isHittingBlock = false;
		this.curBlockDamageMP = 0.0F;
		this.mc.theWorld.destroyBlockInWorldPartially(this.mc.thePlayer.getEntityId(), this.currentBlockX, this.currentBlockY, this.currentblockZ, -1);
	}
	
	/**
	 * Called when a player damages a block and updates damage counters
	 */
	@Override
	public void onPlayerDamageBlock(int x, int y, int z, int p_78759_4_)
	{
		this.syncCurrentPlayItem();
		
		ItemStack currentItem = this.mc.thePlayer.getCurrentEquippedItem();
		
		if(currentItem != null && currentItem.getItem() instanceof AQItemRegionWand)
		{
			if(!this.selectingBlock)
			{
				AQRegionActionManager.instance.execute("point", this.mc.thePlayer, String.valueOf(0), String.valueOf(x), String.valueOf(y), String.valueOf(z));
				
				this.selectingBlock = true;
			}
			
			this.isHittingBlock = false;
			return;
		}
		
		this.selectingBlock = false;
		
		if(this.blockHitDelay > 0)
		{
			--this.blockHitDelay;
		}
		else if(this.currentGameType.isCreative())
		{
			this.blockHitDelay = 5;
			this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(0, x, y, z, p_78759_4_));
			clickBlockCreative(this.mc, this, x, y, z, p_78759_4_);
		}
		else
		{
			if(this.sameToolAndBlock(x, y, z))
			{
				Block block = this.mc.theWorld.getBlock(x, y, z);
				
				if(block.getMaterial() == Material.air)
				{
					this.isHittingBlock = false;
					return;
				}
				
				this.curBlockDamageMP += block.getPlayerRelativeBlockHardness(this.mc.thePlayer, this.mc.thePlayer.worldObj, x, y, z);
				
				if(this.stepSoundTickCounter % 4.0F == 0.0F)
				{
					this.mc.getSoundHandler().playSound(
						new PositionedSoundRecord(
							new ResourceLocation(block.stepSound.getStepResourcePath()),
							(block.stepSound.getVolume() + 1.0F) / 8.0F,
							block.stepSound.getPitch() * 0.5F,
							x + 0.5F,
							y + 0.5F,
							z + 0.5F));
				}
				
				++this.stepSoundTickCounter;
				
				if(this.curBlockDamageMP >= 1.0F)
				{
					this.isHittingBlock = false;
					this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(2, x, y, z, p_78759_4_));
					this.onPlayerDestroyBlock(x, y, z, p_78759_4_);
					this.curBlockDamageMP = 0.0F;
					this.stepSoundTickCounter = 0.0F;
					this.blockHitDelay = 5;
				}
				
				this.mc.theWorld.destroyBlockInWorldPartially(
					this.mc.thePlayer.getEntityId(),
					this.currentBlockX,
					this.currentBlockY,
					this.currentblockZ,
					(int) (this.curBlockDamageMP * 10.0F) - 1);
			}
			else
			{
				this.clickBlock(x, y, z, p_78759_4_);
			}
		}
	}
	
	/**
	 * player reach distance = 4F
	 */
	@Override
	public float getBlockReachDistance()
	{
		return this.currentGameType.isCreative() ? 5.0F : 4.5F;
	}
	
	@Override
	public void updateController()
	{
		this.syncCurrentPlayItem();
		
		if(this.netClientHandler.getNetworkManager().isChannelOpen())
		{
			this.netClientHandler.getNetworkManager().processReceivedPackets();
		}
		else if(this.netClientHandler.getNetworkManager().getExitMessage() != null)
		{
			this.netClientHandler.getNetworkManager().getNetHandler().onDisconnect(this.netClientHandler.getNetworkManager().getExitMessage());
		}
		else
		{
			this.netClientHandler.getNetworkManager().getNetHandler().onDisconnect(new ChatComponentText("Disconnected from server"));
		}
	}
	
	private boolean sameToolAndBlock(int p_85182_1_, int p_85182_2_, int p_85182_3_)
	{
		ItemStack itemstack = this.mc.thePlayer.getHeldItem();
		boolean flag = this.currentItemHittingBlock == null && itemstack == null;
		
		if(this.currentItemHittingBlock != null && itemstack != null)
		{
			flag = itemstack.getItem() == this.currentItemHittingBlock.getItem() && ItemStack.areItemStackTagsEqual(itemstack, this.currentItemHittingBlock)
				&& (itemstack.isItemStackDamageable() || itemstack.getItemDamage() == this.currentItemHittingBlock.getItemDamage());
		}
		
		return p_85182_1_ == this.currentBlockX && p_85182_2_ == this.currentBlockY && p_85182_3_ == this.currentblockZ && flag;
	}
	
	/**
	 * Syncs the current player item with the server
	 */
	private void syncCurrentPlayItem()
	{
		int i = this.mc.thePlayer.inventory.currentItem;
		
		if(i != this.currentPlayerItem)
		{
			this.currentPlayerItem = i;
			this.netClientHandler.addToSendQueue(new C09PacketHeldItemChange(this.currentPlayerItem));
		}
	}
	
	/**
	 * Handles a players right click. Args: player, world, x, y, z, side, hitVec
	 */
	@Override
	public boolean onPlayerRightClick(EntityPlayer p_78760_1_, World p_78760_2_, ItemStack p_78760_3_, int p_78760_4_, int p_78760_5_, int p_78760_6_, int p_78760_7_, Vec3 p_78760_8_)
	{
		this.syncCurrentPlayItem();
		float f = (float) p_78760_8_.xCoord - p_78760_4_;
		float f1 = (float) p_78760_8_.yCoord - p_78760_5_;
		float f2 = (float) p_78760_8_.zCoord - p_78760_6_;
		boolean flag = false;
		
		if(p_78760_3_ != null && p_78760_3_.getItem() != null
			&& p_78760_3_.getItem().onItemUseFirst(p_78760_3_, p_78760_1_, p_78760_2_, p_78760_4_, p_78760_5_, p_78760_6_, p_78760_7_, f, f1, f2))
		{
			return true;
		}
		/*
		 * if (!p_78760_1_.isSneaking() || p_78760_1_.getHeldItem() == null ||
		 * p_78760_1_.getHeldItem().getItem().doesSneakBypassUse(p_78760_2_,
		 * p_78760_4_, p_78760_5_, p_78760_6_, p_78760_1_)) { flag =
		 * p_78760_2_.getBlock(p_78760_4_, p_78760_5_,
		 * p_78760_6_).onBlockActivated(p_78760_2_, p_78760_4_, p_78760_5_,
		 * p_78760_6_, p_78760_1_, p_78760_7_, f, f1, f2); }
		 */
		if(!flag && p_78760_3_ != null && p_78760_3_.getItem() instanceof ItemBlock)
		{
			ItemBlock itemblock = (ItemBlock) p_78760_3_.getItem();
			
			if(!itemblock.func_150936_a(p_78760_2_, p_78760_4_, p_78760_5_, p_78760_6_, p_78760_7_, p_78760_1_, p_78760_3_))
			{
				return false;
			}
		}
		
		this.netClientHandler.addToSendQueue(new C08PacketPlayerBlockPlacement(
			p_78760_4_,
			p_78760_5_,
			p_78760_6_,
			p_78760_7_,
			p_78760_1_.inventory.getCurrentItem(),
			f,
			f1,
			f2));
		
		if(flag)
		{
			return true;
		}
		else if(p_78760_3_ == null)
		{
			return false;
		}
		else if(this.currentGameType.isCreative())
		{
			int j1 = p_78760_3_.getItemDamage();
			int i1 = p_78760_3_.stackSize;
			boolean flag1 = p_78760_3_.tryPlaceItemIntoWorld(p_78760_1_, p_78760_2_, p_78760_4_, p_78760_5_, p_78760_6_, p_78760_7_, f, f1, f2);
			p_78760_3_.setItemDamage(j1);
			p_78760_3_.stackSize = i1;
			return flag1;
		}
		else
		{
			if(!p_78760_3_.tryPlaceItemIntoWorld(p_78760_1_, p_78760_2_, p_78760_4_, p_78760_5_, p_78760_6_, p_78760_7_, f, f1, f2))
			{
				return false;
			}
			if(p_78760_3_.stackSize <= 0)
			{
				MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(p_78760_1_, p_78760_3_));
			}
			return true;
		}
	}
	
	/**
	 * Notifies the server of things like consuming food, etc...
	 */
	@Override
	public boolean sendUseItem(EntityPlayer p_78769_1_, World p_78769_2_, ItemStack p_78769_3_)
	{
		this.syncCurrentPlayItem();
		this.netClientHandler.addToSendQueue(new C08PacketPlayerBlockPlacement(-1, -1, -1, 255, p_78769_1_.inventory.getCurrentItem(), 0.0F, 0.0F, 0.0F));
		int i = p_78769_3_.stackSize;
		ItemStack itemstack1 = p_78769_3_.useItemRightClick(p_78769_2_, p_78769_1_);
		
		if(itemstack1 == p_78769_3_ && itemstack1.stackSize == i)
		{
			return false;
		}
		
		p_78769_1_.inventory.mainInventory[p_78769_1_.inventory.currentItem] = itemstack1;
		
		if(itemstack1.stackSize <= 0)
		{
			p_78769_1_.inventory.mainInventory[p_78769_1_.inventory.currentItem] = null;
			MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(p_78769_1_, itemstack1));
		}
		
		return true;
	}
	
	@Override
	public EntityClientPlayerMP func_147493_a(World p_147493_1_, StatFileWriter p_147493_2_)
	{
		return new EntityClientPlayerMP(this.mc, p_147493_1_, this.mc.getSession(), this.netClientHandler, p_147493_2_);
	}
	
	/**
	 * Attacks an entity
	 */
	@Override
	public void attackEntity(EntityPlayer p_78764_1_, Entity p_78764_2_)
	{
		this.syncCurrentPlayItem();
		this.netClientHandler.addToSendQueue(new C02PacketUseEntity(p_78764_2_, C02PacketUseEntity.Action.ATTACK));
		p_78764_1_.attackTargetEntityWithCurrentItem(p_78764_2_);
	}
	
	/**
	 * Send packet to server - player is interacting with another entity (left
	 * click)
	 */
	public boolean interactWithEntitySendOwnPacket(EntityPlayer p_78768_1_, Entity p_78768_2_)
	{
		this.syncCurrentPlayItem();
		this.netClientHandler.addToSendQueue(new C02PacketUseEntity(p_78768_2_, C02PacketUseEntity.Action.INTERACT));
		return p_78768_1_.interactWith(p_78768_2_);
	}
	
	@Override
	public boolean interactWithEntitySendPacket(EntityPlayer p_78768_1_, Entity p_78768_2_)
	{
		return false;
	}
	
	@Override
	public ItemStack windowClick(int p_78753_1_, int p_78753_2_, int p_78753_3_, int p_78753_4_, EntityPlayer p_78753_5_)
	{
		short short1 = p_78753_5_.openContainer.getNextTransactionID(p_78753_5_.inventory);
		ItemStack itemstack = p_78753_5_.openContainer.slotClick(p_78753_2_, p_78753_3_, p_78753_4_, p_78753_5_);
		this.netClientHandler.addToSendQueue(new C0EPacketClickWindow(p_78753_1_, p_78753_2_, p_78753_3_, p_78753_4_, itemstack, short1));
		return itemstack;
	}
	
	/**
	 * GuiEnchantment uses this during multiplayer to tell PlayerControllerMP to
	 * send a packet indicating the enchantment action the player has taken.
	 */
	@Override
	public void sendEnchantPacket(int p_78756_1_, int p_78756_2_)
	{
		this.netClientHandler.addToSendQueue(new C11PacketEnchantItem(p_78756_1_, p_78756_2_));
	}
	
	/**
	 * Used in PlayerControllerMP to update the server with an ItemStack in a
	 * slot.
	 */
	@Override
	public void sendSlotPacket(ItemStack p_78761_1_, int p_78761_2_)
	{
		if(this.currentGameType.isCreative())
		{
			this.netClientHandler.addToSendQueue(new C10PacketCreativeInventoryAction(p_78761_2_, p_78761_1_));
		}
	}
	
	/**
	 * Sends a Packet107 to the server to drop the item on the ground
	 */
	@Override
	public void sendPacketDropItem(ItemStack p_78752_1_)
	{
		if(this.currentGameType.isCreative() && p_78752_1_ != null)
		{
			this.netClientHandler.addToSendQueue(new C10PacketCreativeInventoryAction(-1, p_78752_1_));
		}
	}
	
	@Override
	public void onStoppedUsingItem(EntityPlayer p_78766_1_)
	{
		this.syncCurrentPlayItem();
		this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(5, 0, 0, 0, 255));
		p_78766_1_.stopUsingItem();
	}
	
	@Override
	public boolean gameIsSurvivalOrAdventure()
	{
		return this.currentGameType.isSurvivalOrAdventure();
	}
	
	/**
	 * Checks if the player is not creative, used for checking if it should
	 * break a block instantly
	 */
	@Override
	public boolean isNotCreative()
	{
		return !this.currentGameType.isCreative();
	}
	
	/**
	 * returns true if player is in creative mode
	 */
	@Override
	public boolean isInCreativeMode()
	{
		return this.currentGameType.isCreative();
	}
	
	/**
	 * true for hitting entities far away.
	 */
	@Override
	public boolean extendedReach()
	{
		return this.currentGameType.isCreative();
	}
	
	@Override
	public boolean func_110738_j()
	{
		return this.mc.thePlayer.isRiding() && this.mc.thePlayer.ridingEntity instanceof EntityHorse;
	}
}
