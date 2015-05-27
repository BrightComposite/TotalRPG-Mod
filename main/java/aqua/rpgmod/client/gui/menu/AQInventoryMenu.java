package aqua.rpgmod.client.gui.menu;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import aqua.rpgmod.inventory.AQContainerPlayer;

public class AQInventoryMenu extends AQMenuPage
{
	public Container inventorySlots;
	private Slot theSlot;
	protected final Set field_147008_s = new HashSet();
	protected boolean field_147007_t;
	private int field_146987_F;
	private int field_146988_G;
	private boolean field_146995_H;
	private int field_146996_I;
	private long field_146997_J;
	private Slot field_146998_K;
	private int field_146992_L;
	private boolean field_146993_M;
	private ItemStack field_146994_N;
	
	public AQInventoryMenu()
	{
		super("inventory");
		this.inventorySlots = null;
		this.field_146995_H = true;
		
		this.xSize = 176;
		this.ySize = 166;
	}
	
	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui()
	{
		super.initGui();
		
		this.mc.thePlayer.openContainer = this.inventorySlots = this.mc.thePlayer.inventoryContainer;
	}
	
	/**
	 * Draws the screen and all the components in it.
	 */
	
	protected static boolean isValid(Slot slot)
	{
		return slot instanceof AQContainerPlayer.DisabledArmorSlot == false;
	}
	
	@SuppressWarnings("unused")
	protected void drawGuiContainerBackgroundLayer(float p_73863_3_, int p_146979_1_, int p_146979_2_)
	{}
	
	@SuppressWarnings("unused")
	protected void drawGuiContainerElements(float p_73863_3_, int p_146979_1_, int p_146979_2_)
	{}
	
	@SuppressWarnings("unused")
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
	{}
	
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
	{
		drawDefaultBackground();
		drawGuiContainerBackgroundLayer(p_73863_3_, p_73863_1_, p_73863_2_);
		drawBackground(p_73863_1_, p_73863_2_, p_73863_3_);
		drawGuiContainerElements(p_73863_3_, p_73863_1_, p_73863_2_);
		
		int k = this.guiLeft;
		int l = this.guiTop;
		
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glPushMatrix();
		GL11.glTranslatef(k, l, 0.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		this.theSlot = null;
		short short1 = 240;
		short short2 = 240;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, short1 / 1.0F, short2 / 1.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int k1;
		
		for(int i1 = 0; i1 < this.inventorySlots.inventorySlots.size(); ++i1)
		{
			Slot slot = (Slot) this.inventorySlots.inventorySlots.get(i1);
			
			if(!isValid(slot))
				continue;
			
			this.func_146977_a(slot);
			
			if(this.isMouseOverSlot(slot, p_73863_1_, p_73863_2_) && slot.func_111238_b())
			{
				this.theSlot = slot;
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				int j1 = slot.xDisplayPosition;
				k1 = slot.yDisplayPosition;
				GL11.glColorMask(true, true, true, false);
				this.drawGradientRect(j1, k1, j1 + 16, k1 + 16, -2130706433, -2130706433);
				GL11.glColorMask(true, true, true, true);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
			}
		}
		
		GL11.glDisable(GL11.GL_LIGHTING);
		drawGuiContainerForegroundLayer(p_73863_1_, p_73863_2_);
		GL11.glEnable(GL11.GL_LIGHTING);
		
		InventoryPlayer inventoryplayer = this.mc.thePlayer.inventory;
		ItemStack itemstack = inventoryplayer.getItemStack();
		
		if(itemstack != null)
		{
			byte b0 = 8;
			k1 = 8;
			String s = null;
			
			if(this.field_147007_t && this.field_147008_s.size() > 1)
			{
				itemstack = itemstack.copy();
				itemstack.stackSize = this.field_146996_I;
				
				if(itemstack.stackSize == 0)
				{
					s = "" + EnumChatFormatting.YELLOW + "0";
				}
			}
			
			this.drawItemStack(itemstack, p_73863_1_ - k - b0, p_73863_2_ - l - k1, s);
		}
		
		GL11.glPopMatrix();
		
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		this.drawForeground(p_73863_1_, p_73863_2_, p_73863_3_);
		
		if(inventoryplayer.getItemStack() == null && this.theSlot != null && this.theSlot.getHasStack())
		{
			ItemStack itemstack1 = this.theSlot.getStack();
			this.renderToolTip(itemstack1, p_73863_1_, p_73863_2_);
		}
		
		this.drawHints(p_73863_1_, p_73863_2_, p_73863_3_);
	}
	
	private void drawItemStack(ItemStack p_146982_1_, int p_146982_2_, int p_146982_3_, String p_146982_4_)
	{
		GL11.glTranslatef(0.0F, 0.0F, 32.0F);
		this.zLevel = 200.0F;
		itemRender.zLevel = 200.0F;
		FontRenderer font = null;
		
		if(p_146982_1_ != null)
			font = p_146982_1_.getItem().getFontRenderer(p_146982_1_);
		
		if(font == null)
			font = this.fontRendererObj;
		
		itemRender.renderItemAndEffectIntoGUI(font, this.mc.getTextureManager(), p_146982_1_, p_146982_2_, p_146982_3_);
		itemRender.renderItemOverlayIntoGUI(font, this.mc.getTextureManager(), p_146982_1_, p_146982_2_, p_146982_3_, p_146982_4_);
		this.zLevel = 0.0F;
		itemRender.zLevel = 0.0F;
	}
	
	private void func_146977_a(Slot p_146977_1_)
	{
		int i = p_146977_1_.xDisplayPosition;
		int j = p_146977_1_.yDisplayPosition;
		ItemStack itemstack = p_146977_1_.getStack();
		boolean flag = false;
		boolean flag1 = false;
		ItemStack itemstack1 = this.mc.thePlayer.inventory.getItemStack();
		String s = null;
		
		if(this.field_147007_t && this.field_147008_s.contains(p_146977_1_) && itemstack1 != null)
		{
			if(this.field_147008_s.size() == 1)
			{
				return;
			}
			
			if(Container.func_94527_a(p_146977_1_, itemstack1, true) && this.inventorySlots.canDragIntoSlot(p_146977_1_))
			{
				itemstack = itemstack1.copy();
				flag = true;
				Container.func_94525_a(this.field_147008_s, this.field_146987_F, itemstack, p_146977_1_.getStack() == null ? 0
					: p_146977_1_.getStack().stackSize);
				
				if(itemstack.stackSize > itemstack.getMaxStackSize())
				{
					s = EnumChatFormatting.YELLOW + "" + itemstack.getMaxStackSize();
					itemstack.stackSize = itemstack.getMaxStackSize();
				}
				
				if(itemstack.stackSize > p_146977_1_.getSlotStackLimit())
				{
					s = EnumChatFormatting.YELLOW + "" + p_146977_1_.getSlotStackLimit();
					itemstack.stackSize = p_146977_1_.getSlotStackLimit();
				}
			}
			else
			{
				this.field_147008_s.remove(p_146977_1_);
				this.func_146980_g();
			}
		}
		
		this.zLevel = 100.0F;
		itemRender.zLevel = 100.0F;
		
		if(itemstack == null)
		{
			IIcon iicon = p_146977_1_.getBackgroundIconIndex();
			
			if(iicon != null)
			{
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				
				GL11.glDisable(GL11.GL_LIGHTING);
				this.mc.getTextureManager().bindTexture(TextureMap.locationItemsTexture);
				this.drawTexturedModelRectFromIcon(i, j, iicon, 16, 16);
				GL11.glEnable(GL11.GL_LIGHTING);
				flag1 = true;
			}
		}
		
		if(!flag1)
		{
			if(flag)
			{
				drawRect(i, j, i + 16, j + 16, -2130706433);
			}
			
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), itemstack, i, j);
			itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), itemstack, i, j, s);
		}
		
		itemRender.zLevel = 0.0F;
		this.zLevel = 0.0F;
	}
	
	private void func_146980_g()
	{
		ItemStack itemstack = this.mc.thePlayer.inventory.getItemStack();
		
		if(itemstack != null && this.field_147007_t)
		{
			this.field_146996_I = itemstack.stackSize;
			ItemStack itemstack1;
			int i;
			
			for(Iterator iterator = this.field_147008_s.iterator(); iterator.hasNext(); this.field_146996_I -= itemstack1.stackSize - i)
			{
				Slot slot = (Slot) iterator.next();
				itemstack1 = itemstack.copy();
				
				if(!isValid(slot))
				{
					i = 0;
					continue;
				}
				
				i = slot.getStack() == null ? 0 : slot.getStack().stackSize;
				
				Container.func_94525_a(this.field_147008_s, this.field_146987_F, itemstack1, i);
				
				if(itemstack1.stackSize > itemstack1.getMaxStackSize())
				{
					itemstack1.stackSize = itemstack1.getMaxStackSize();
				}
				
				if(itemstack1.stackSize > slot.getSlotStackLimit())
				{
					itemstack1.stackSize = slot.getSlotStackLimit();
				}
			}
		}
	}
	
	/**
	 * Returns the slot at the given coordinates or null if there is none.
	 */
	private Slot getSlotAtPosition(int p_146975_1_, int p_146975_2_)
	{
		for(int k = 0; k < this.inventorySlots.inventorySlots.size(); ++k)
		{
			Slot slot = (Slot) this.inventorySlots.inventorySlots.get(k);
			
			if(!isValid(slot))
				continue;
			
			if(this.isMouseOverSlot(slot, p_146975_1_, p_146975_2_))
			{
				return slot;
			}
		}
		
		return null;
	}
	
	/**
	 * Called when the mouse is clicked.
	 */
	@Override
	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_)
	{
		super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
		boolean flag = p_73864_3_ == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100;
		Slot slot = this.getSlotAtPosition(p_73864_1_, p_73864_2_);
		
		long l = Minecraft.getSystemTime();
		this.field_146993_M = this.field_146998_K == slot && l - this.field_146997_J < 250L && this.field_146992_L == p_73864_3_;
		this.field_146995_H = false;
		
		if(p_73864_3_ == 0 || p_73864_3_ == 1 || flag)
		{
			int i1 = this.guiLeft;
			int j1 = this.guiTop;
			boolean flag1 = p_73864_1_ < i1 || p_73864_2_ < j1 || p_73864_1_ >= i1 + this.xSize || p_73864_2_ >= j1 + this.ySize;
			int k1 = -1;
			
			if(slot != null)
			{
				k1 = slot.slotNumber;
			}
			
			if(flag1)
			{
				k1 = -999;
			}
			
			if(this.mc.gameSettings.touchscreen && flag1 && this.mc.thePlayer.inventory.getItemStack() == null)
			{
				this.mc.displayGuiScreen((GuiScreen) null);
				return;
			}
			
			if(k1 != -1)
			{
				if(!this.field_147007_t)
				{
					if(this.mc.thePlayer.inventory.getItemStack() == null)
					{
						if(p_73864_3_ == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100)
						{
							this.handleMouseClick(slot, k1, p_73864_3_, 3);
						}
						else
						{
							boolean flag2 = k1 != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
							byte b0 = 0;
							
							if(flag2)
							{
								this.field_146994_N = slot != null && slot.getHasStack() ? slot.getStack() : null;
								b0 = 1;
							}
							else if(k1 == -999)
							{
								b0 = 4;
							}
							
							this.handleMouseClick(slot, k1, p_73864_3_, b0);
						}
						
						this.field_146995_H = true;
					}
					else
					{
						this.field_147007_t = true;
						this.field_146988_G = p_73864_3_;
						this.field_147008_s.clear();
						
						if(p_73864_3_ == 0)
						{
							this.field_146987_F = 0;
						}
						else if(p_73864_3_ == 1)
						{
							this.field_146987_F = 1;
						}
					}
				}
			}
		}
		
		this.field_146998_K = slot;
		this.field_146997_J = l;
		this.field_146992_L = p_73864_3_;
	}
	
	/**
	 * Called when a mouse button is pressed and the mouse is moved around.
	 * Parameters are : mouseX, mouseY, lastButtonClicked & timeSinceMouseClick.
	 */
	@Override
	protected void mouseClickMove(int p_146273_1_, int p_146273_2_, int p_146273_3_, long p_146273_4_)
	{
		Slot slot = this.getSlotAtPosition(p_146273_1_, p_146273_2_);
		
		ItemStack itemstack = this.mc.thePlayer.inventory.getItemStack();
		
		if(this.field_147007_t && slot != null && itemstack != null && itemstack.stackSize > this.field_147008_s.size()
			&& Container.func_94527_a(slot, itemstack, true) && slot.isItemValid(itemstack) && this.inventorySlots.canDragIntoSlot(slot))
		{
			this.field_147008_s.add(slot);
			this.func_146980_g();
		}
	}
	
	/**
	 * Called when the mouse is moved or a mouse button is released. Signature:
	 * (mouseX, mouseY, which) which==-1 is mouseMove, which==0 or which==1 is
	 * mouseUp
	 */
	@Override
	protected void mouseMovedOrUp(int p_146286_1_, int p_146286_2_, int p_146286_3_)
	{
		super.mouseMovedOrUp(p_146286_1_, p_146286_2_, p_146286_3_);
		Slot slot = this.getSlotAtPosition(p_146286_1_, p_146286_2_);
		int l = this.guiLeft;
		int i1 = this.guiTop;
		boolean flag = p_146286_1_ < l || p_146286_2_ < i1 || p_146286_1_ >= l + this.xSize || p_146286_2_ >= i1 + this.ySize;
		int j1 = -1;
		
		if(slot != null)
		{
			j1 = slot.slotNumber;
		}
		
		if(flag)
		{
			j1 = -999;
		}
		
		Slot slot1;
		Iterator iterator;
		
		if(this.field_146993_M && slot != null && p_146286_3_ == 0 && this.inventorySlots.func_94530_a((ItemStack) null, slot))
		{
			if(isShiftKeyDown())
			{
				if(slot.inventory != null && this.field_146994_N != null)
				{
					iterator = this.inventorySlots.inventorySlots.iterator();
					
					while(iterator.hasNext())
					{
						slot1 = (Slot) iterator.next();
						
						if(!isValid(slot))
							continue;
						
						if(slot1 != null && slot1.canTakeStack(this.mc.thePlayer) && slot1.getHasStack() && slot1.inventory == slot.inventory
							&& Container.func_94527_a(slot1, this.field_146994_N, true))
						{
							this.handleMouseClick(slot1, slot1.slotNumber, p_146286_3_, 1);
						}
					}
				}
			}
			else
			{
				this.handleMouseClick(slot, j1, p_146286_3_, 6);
			}
			
			this.field_146993_M = false;
			this.field_146997_J = 0L;
		}
		else
		{
			if(this.field_147007_t && this.field_146988_G != p_146286_3_)
			{
				this.field_147007_t = false;
				this.field_147008_s.clear();
				this.field_146995_H = true;
				return;
			}
			
			if(this.field_146995_H)
			{
				this.field_146995_H = false;
				return;
			}
			
			boolean flag1;
			
			if(this.field_147007_t && !this.field_147008_s.isEmpty())
			{
				this.handleMouseClick((Slot) null, -999, Container.func_94534_d(0, this.field_146987_F), 5);
				iterator = this.field_147008_s.iterator();
				
				while(iterator.hasNext())
				{
					slot1 = (Slot) iterator.next();
					
					if(!isValid(slot))
						continue;
					
					this.handleMouseClick(slot1, slot1.slotNumber, Container.func_94534_d(1, this.field_146987_F), 5);
				}
				
				this.handleMouseClick((Slot) null, -999, Container.func_94534_d(2, this.field_146987_F), 5);
			}
			else if(this.mc.thePlayer.inventory.getItemStack() != null)
			{
				if(p_146286_3_ == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100)
				{
					this.handleMouseClick(slot, j1, p_146286_3_, 3);
				}
				else
				{
					flag1 = j1 != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
					
					if(flag1)
					{
						this.field_146994_N = slot != null && slot.getHasStack() ? slot.getStack() : null;
					}
					
					this.handleMouseClick(slot, j1, p_146286_3_, flag1 ? 1 : 0);
				}
			}
		}
		
		if(this.mc.thePlayer.inventory.getItemStack() == null)
		{
			this.field_146997_J = 0L;
		}
		
		this.field_147007_t = false;
	}
	
	/**
	 * Returns if the passed mouse position is over the specified slot.
	 */
	private boolean isMouseOverSlot(Slot p_146981_1_, int p_146981_2_, int p_146981_3_)
	{
		return this.func_146978_c(p_146981_1_.xDisplayPosition, p_146981_1_.yDisplayPosition, 16, 16, p_146981_2_, p_146981_3_);
	}
	
	protected boolean func_146978_c(int p_146978_1_, int p_146978_2_, int p_146978_3_, int p_146978_4_, int p_146978_5_, int p_146978_6_)
	{
		int k1 = this.guiLeft;
		int l1 = this.guiTop;
		p_146978_5_ -= k1;
		p_146978_6_ -= l1;
		return p_146978_5_ >= p_146978_1_ - 1 && p_146978_5_ < p_146978_1_ + p_146978_3_ + 1 && p_146978_6_ >= p_146978_2_ - 1
			&& p_146978_6_ < p_146978_2_ + p_146978_4_ + 1;
	}
	
	protected void handleMouseClick(Slot p_146984_1_, int p_146984_2_, int p_146984_3_, int p_146984_4_)
	{
		if(p_146984_1_ != null)
		{
			p_146984_2_ = p_146984_1_.slotNumber;
		}
		
		this.mc.playerController.windowClick(this.inventorySlots.windowId, p_146984_2_, p_146984_3_, p_146984_4_, this.mc.thePlayer);
	}
	
	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_)
	{
		super.keyTyped(p_73869_1_, p_73869_2_);
		
		this.checkHotbarKeys(p_73869_2_);
		
		if(this.theSlot != null && this.theSlot.getHasStack())
		{
			if(p_73869_2_ == this.mc.gameSettings.keyBindPickBlock.getKeyCode())
			{
				this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, 0, 3);
			}
			else if(p_73869_2_ == this.mc.gameSettings.keyBindDrop.getKeyCode())
			{
				this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, isCtrlKeyDown() ? 1 : 0, 4);
			}
		}
	}
	
	/**
	 * This function is what controls the hotbar shortcut check when you press a
	 * number key when hovering a stack.
	 */
	protected boolean checkHotbarKeys(int p_146983_1_)
	{
		if(this.mc.thePlayer.inventory.getItemStack() == null && this.theSlot != null)
		{
			for(int j = 0; j < 9; ++j)
			{
				if(p_146983_1_ == this.mc.gameSettings.keyBindsHotbar[j].getKeyCode())
				{
					this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, j, 2);
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat
	 * events
	 */
	@Override
	public void onGuiClosed()
	{
		if(this.mc.thePlayer != null && this.inventorySlots != null)
		{
			this.inventorySlots.onContainerClosed(this.mc.thePlayer);
		}
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
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen()
	{
		super.updateScreen();
		
		if(!this.mc.thePlayer.isEntityAlive() || this.mc.thePlayer.isDead)
		{
			this.mc.thePlayer.closeScreen();
		}
	}
}
