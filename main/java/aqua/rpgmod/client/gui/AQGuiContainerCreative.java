package aqua.rpgmod.client.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import aqua.rpgmod.client.gui.menu.AQInventoryMenu;
import aqua.rpgmod.client.gui.menu.AQModMenu;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.gui.inventory.CreativeCrafting;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class AQGuiContainerCreative extends AQInventoryMenu
{
	private static final ResourceLocation field_147061_u = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
	private static InventoryBasic field_147060_v = new InventoryBasic("tmp", true, 45);
	/** Currently selected creative inventory tab index. */
	private static int selectedTabIndex = CreativeTabs.tabBlock.getTabIndex();
	/** Amount scrolled in Creative mode inventory (0 = top, 1 = bottom) */
	private float currentScroll;
	/** True if the scrollbar is being dragged */
	private boolean isScrolling;
	/**
	 * True if the left mouse button was held down last time drawScreen was
	 * called.
	 */
	private boolean wasClicking;
	private GuiTextField searchField;
	private Slot field_147064_C;
	private boolean field_147057_D;
	private CreativeCrafting field_147059_E;
	private static int tabPage = 0;
	private int maxPages = 0;
	
	public AQGuiContainerCreative()
	{
		super();
		this.allowUserInput = true;
		
		this.xSize = 195;
		this.ySize = 136;
	}
	
	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen()
	{
		if(!this.mc.playerController.isInCreativeMode())
		{
			AQModMenu.instance.selectPage(1);
		}
	}
	
	protected void handleMouseClick(Slot p_146984_1_, int p_146984_2_, int p_146984_3_, int p_146984_4_)
	{
		this.field_147057_D = true;
		boolean flag = p_146984_4_ == 1;
		p_146984_4_ = p_146984_2_ == -999 && p_146984_4_ == 0 ? 4 : p_146984_4_;
		ItemStack itemstack1;
		InventoryPlayer inventoryplayer;
		
		if(p_146984_1_ == null && p_146984_4_ != 5)
		{
			inventoryplayer = this.mc.thePlayer.inventory;
			
			if(inventoryplayer.getItemStack() != null)
			{
				if(p_146984_3_ == 0)
				{
					this.mc.thePlayer.dropPlayerItemWithRandomChoice(inventoryplayer.getItemStack(), true);
					this.mc.playerController.sendPacketDropItem(inventoryplayer.getItemStack());
					inventoryplayer.setItemStack((ItemStack) null);
				}
				
				if(p_146984_3_ == 1)
				{
					itemstack1 = inventoryplayer.getItemStack().splitStack(1);
					this.mc.thePlayer.dropPlayerItemWithRandomChoice(itemstack1, true);
					this.mc.playerController.sendPacketDropItem(itemstack1);
					
					if(inventoryplayer.getItemStack().stackSize == 0)
					{
						inventoryplayer.setItemStack((ItemStack) null);
					}
				}
			}
		}
		else
		{
			int l;
			
			if(p_146984_1_ == this.field_147064_C && flag)
			{
				for(l = 0; l < this.mc.thePlayer.inventoryContainer.getInventory().size(); ++l)
				{
					this.mc.playerController.sendSlotPacket((ItemStack) null, l);
				}
			}
			else
			{
				ItemStack itemstack;
				
				if(p_146984_4_ != 5 && p_146984_1_ != null && p_146984_1_.inventory == field_147060_v)
				{
					inventoryplayer = this.mc.thePlayer.inventory;
					itemstack1 = inventoryplayer.getItemStack();
					ItemStack itemstack2 = p_146984_1_.getStack();
					ItemStack itemstack3;
					
					if(p_146984_4_ == 2)
					{
						if(itemstack2 != null && p_146984_3_ >= 0 && p_146984_3_ < 9)
						{
							itemstack3 = itemstack2.copy();
							itemstack3.stackSize = itemstack3.getMaxStackSize();
							this.mc.thePlayer.inventory.setInventorySlotContents(p_146984_3_, itemstack3);
							this.mc.thePlayer.inventoryContainer.detectAndSendChanges();
						}
						
						return;
					}
					
					if(p_146984_4_ == 3)
					{
						if(inventoryplayer.getItemStack() == null && p_146984_1_.getHasStack())
						{
							itemstack3 = p_146984_1_.getStack().copy();
							itemstack3.stackSize = itemstack3.getMaxStackSize();
							inventoryplayer.setItemStack(itemstack3);
						}
						
						return;
					}
					
					if(p_146984_4_ == 4)
					{
						if(itemstack2 != null)
						{
							itemstack3 = itemstack2.copy();
							itemstack3.stackSize = p_146984_3_ == 0 ? 1 : itemstack3.getMaxStackSize();
							this.mc.thePlayer.dropPlayerItemWithRandomChoice(itemstack3, true);
							this.mc.playerController.sendPacketDropItem(itemstack3);
						}
						
						return;
					}
					
					if(itemstack1 != null && itemstack2 != null && itemstack1.isItemEqual(itemstack2)
						&& ItemStack.areItemStackTagsEqual(itemstack1, itemstack2)) // Forge:
																					// Bugfix,
																					// Compare
																					// NBT
																					// data,
																					// allow
																					// for
																					// deletion
																					// of
																					// enchanted
																					// books,
																					// MC-12770
					{
						if(p_146984_3_ == 0)
						{
							if(flag)
							{
								itemstack1.stackSize = itemstack1.getMaxStackSize();
							}
							else if(itemstack1.stackSize < itemstack1.getMaxStackSize())
							{
								++itemstack1.stackSize;
							}
						}
						else if(itemstack1.stackSize <= 1)
						{
							inventoryplayer.setItemStack((ItemStack) null);
						}
						else
						{
							--itemstack1.stackSize;
						}
					}
					else if(itemstack2 != null && itemstack1 == null)
					{
						inventoryplayer.setItemStack(ItemStack.copyItemStack(itemstack2));
						itemstack1 = inventoryplayer.getItemStack();
						
						if(flag)
						{
							itemstack1.stackSize = itemstack1.getMaxStackSize();
						}
					}
					else
					{
						inventoryplayer.setItemStack((ItemStack) null);
					}
				}
				else
				{
					this.inventorySlots.slotClick(p_146984_1_ == null ? p_146984_2_ : p_146984_1_.slotNumber, p_146984_3_, p_146984_4_, this.mc.thePlayer);
					
					if(Container.func_94532_c(p_146984_3_) == 2)
					{
						for(l = 0; l < 9; ++l)
						{
							this.mc.playerController.sendSlotPacket(this.inventorySlots.getSlot(45 + l).getStack(), 36 + l);
						}
					}
					else if(p_146984_1_ != null)
					{
						itemstack = this.inventorySlots.getSlot(p_146984_1_.slotNumber).getStack();
						this.mc.playerController.sendSlotPacket(itemstack, p_146984_1_.slotNumber - this.inventorySlots.inventorySlots.size() + 9 + 36);
					}
				}
			}
		}
	}
	
	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui()
	{
		if(this.mc.playerController.isInCreativeMode())
		{
			super.initGui();
			
			this.guiTop += 10;
			this.mc.thePlayer.openContainer = this.inventorySlots = new AQGuiContainerCreative.ContainerCreative(Minecraft.getMinecraft().thePlayer);
			
			Keyboard.enableRepeatEvents(true);
			this.searchField = new GuiTextField(this.fontRendererObj, this.guiLeft + 82, this.guiTop + 6, 89, this.fontRendererObj.FONT_HEIGHT);
			this.searchField.setMaxStringLength(15);
			this.searchField.setEnableBackgroundDrawing(false);
			this.searchField.setVisible(false);
			this.searchField.setTextColor(16777215);
			int i = selectedTabIndex;
			selectedTabIndex = -1;
			this.setCurrentCreativeTab(CreativeTabs.creativeTabArray[i]);
			this.field_147059_E = new CreativeCrafting(this.mc);
			this.mc.thePlayer.inventoryContainer.addCraftingToCrafters(this.field_147059_E);
			
			int tabCount = CreativeTabs.creativeTabArray.length;
			
			if(tabCount > 12)
			{
				AQGuiButton prev = new AQGuiButton(this, 101, this.guiLeft, this.guiTop - 50, 20, 20, "<");
				prev.setHint(I18n.format("gui.page.previous"));
				prev.drawHint = true;
				this.buttonList.add(prev);
				
				AQGuiButton next = new AQGuiButton(this, 102, this.guiLeft + this.xSize - 20, this.guiTop - 50, 20, 20, ">");
				next.setHint(I18n.format("gui.page.next"));
				next.drawHint = true;
				this.buttonList.add(next);
				
				this.maxPages = Math.round((((tabCount - 12) - 1) / 10) + 1);
			}
		}
		else
		{
			AQModMenu.instance.selectPage(1);
		}
	}
	
	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat
	 * events
	 */
	public void onGuiClosed()
	{
		super.onGuiClosed();
		
		if(this.mc.thePlayer != null && this.mc.thePlayer.inventory != null)
		{
			this.mc.thePlayer.inventoryContainer.removeCraftingFromCrafters(this.field_147059_E);
		}
		
		Keyboard.enableRepeatEvents(false);
	}
	
	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	protected void keyTyped(char p_73869_1_, int p_73869_2_)
	{
		if(!CreativeTabs.creativeTabArray[selectedTabIndex].hasSearchBar())
		{
			if(GameSettings.isKeyDown(this.mc.gameSettings.keyBindChat))
			{
				this.setCurrentCreativeTab(CreativeTabs.tabAllSearch);
			}
			else
			{
				super.keyTyped(p_73869_1_, p_73869_2_);
			}
		}
		else
		{
			if(this.field_147057_D)
			{
				this.field_147057_D = false;
				this.searchField.setText("");
			}
			
			if(!this.checkHotbarKeys(p_73869_2_))
			{
				if(this.searchField.textboxKeyTyped(p_73869_1_, p_73869_2_))
				{
					this.updateCreativeSearch();
				}
				else
				{
					super.keyTyped(p_73869_1_, p_73869_2_);
				}
			}
		}
	}
	
	private void updateCreativeSearch()
	{
		AQGuiContainerCreative.ContainerCreative containercreative = (AQGuiContainerCreative.ContainerCreative) this.inventorySlots;
		containercreative.itemList.clear();
		
		CreativeTabs tab = CreativeTabs.creativeTabArray[selectedTabIndex];
		if(tab.hasSearchBar() && tab != CreativeTabs.tabAllSearch)
		{
			tab.displayAllReleventItems(containercreative.itemList);
			updateFilteredItems(containercreative);
			return;
		}
		
		Iterator iterator = Item.itemRegistry.iterator();
		
		while(iterator.hasNext())
		{
			Item item = (Item) iterator.next();
			
			if(item != null && item.getCreativeTab() != null)
			{
				item.getSubItems(item, (CreativeTabs) null, containercreative.itemList);
			}
		}
		
		updateFilteredItems(containercreative);
	}
	
	// split from above for custom search tabs
	private void updateFilteredItems(AQGuiContainerCreative.ContainerCreative containercreative)
	{
		Iterator iterator;
		Enchantment[] aenchantment = Enchantment.enchantmentsList;
		int j = aenchantment.length;
		
		if(CreativeTabs.creativeTabArray[selectedTabIndex] != CreativeTabs.tabAllSearch)
			j = 0; // Forge: Don't add enchants to custom tabs.
		for(int i = 0; i < j; ++i)
		{
			Enchantment enchantment = aenchantment[i];
			
			if(enchantment != null && enchantment.type != null)
			{
				Items.enchanted_book.func_92113_a(enchantment, containercreative.itemList);
			}
		}
		
		iterator = containercreative.itemList.iterator();
		String s1 = this.searchField.getText().toLowerCase();
		
		while(iterator.hasNext())
		{
			ItemStack itemstack = (ItemStack) iterator.next();
			boolean flag = false;
			Iterator iterator1 = itemstack.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips).iterator();
			
			while(true)
			{
				if(iterator1.hasNext())
				{
					String s = (String) iterator1.next();
					
					if(!s.toLowerCase().contains(s1))
					{
						continue;
					}
					
					flag = true;
				}
				
				if(!flag)
				{
					iterator.remove();
				}
				
				break;
			}
		}
		
		this.currentScroll = 0.0F;
		containercreative.scrollTo(0.0F);
	}
	
	/**
	 * Called when the mouse is clicked.
	 */
	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_)
	{
		if(p_73864_3_ == 0)
		{
			int l = p_73864_1_ - this.guiLeft;
			int i1 = p_73864_2_ - this.guiTop;
			CreativeTabs[] acreativetabs = CreativeTabs.creativeTabArray;
			int j1 = acreativetabs.length;
			
			for(int k1 = 0; k1 < j1; ++k1)
			{
				CreativeTabs creativetabs = acreativetabs[k1];
				
				if(creativetabs != null && this.func_147049_a(creativetabs, l, i1))
				{
					return;
				}
			}
		}
		
		super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
	}
	
	/**
	 * Called when the mouse is moved or a mouse button is released. Signature:
	 * (mouseX, mouseY, which) which==-1 is mouseMove, which==0 or which==1 is
	 * mouseUp
	 */
	protected void mouseMovedOrUp(int p_146286_1_, int p_146286_2_, int p_146286_3_)
	{
		if(p_146286_3_ == 0)
		{
			int l = p_146286_1_ - this.guiLeft;
			int i1 = p_146286_2_ - this.guiTop;
			CreativeTabs[] acreativetabs = CreativeTabs.creativeTabArray;
			int j1 = acreativetabs.length;
			
			for(int k1 = 0; k1 < j1; ++k1)
			{
				CreativeTabs creativetabs = acreativetabs[k1];
				
				if(creativetabs != null && this.func_147049_a(creativetabs, l, i1))
				{
					this.setCurrentCreativeTab(creativetabs);
					return;
				}
			}
		}
		
		super.mouseMovedOrUp(p_146286_1_, p_146286_2_, p_146286_3_);
	}
	
	/**
	 * returns (if you are not on the inventoryTab) and (the flag isn't set) and
	 * (you have more than 1 page of items)
	 */
	private boolean needsScrollBars()
	{
		if(CreativeTabs.creativeTabArray[selectedTabIndex] == null)
			return false;
		return CreativeTabs.creativeTabArray[selectedTabIndex].shouldHidePlayerInventory()
			&& ((AQGuiContainerCreative.ContainerCreative) this.inventorySlots).func_148328_e();
	}
	
	private void setCurrentCreativeTab(CreativeTabs p_147050_1_)
	{
		if(p_147050_1_ == null)
			return;
		
		selectedTabIndex = p_147050_1_.getTabIndex();
		AQGuiContainerCreative.ContainerCreative containercreative = (AQGuiContainerCreative.ContainerCreative) this.inventorySlots;
		this.field_147008_s.clear();
		containercreative.itemList.clear();
		p_147050_1_.displayAllReleventItems(containercreative.itemList);
		
		if(this.searchField != null)
		{
			if(p_147050_1_.hasSearchBar())
			{
				this.searchField.setVisible(true);
				this.searchField.setCanLoseFocus(false);
				this.searchField.setFocused(true);
				this.searchField.setText("");
				this.searchField.width = p_147050_1_.getSearchbarWidth();
				this.searchField.xPosition = this.guiLeft + (82 /* default left */+ 89 /*
																						 * default
																						 * width
																						 */) - this.searchField.width;
				this.updateCreativeSearch();
			}
			else
			{
				this.searchField.setVisible(false);
				this.searchField.setCanLoseFocus(true);
				this.searchField.setFocused(false);
			}
		}
		
		this.currentScroll = 0.0F;
		containercreative.scrollTo(0.0F);
	}
	
	/**
	 * Handles mouse input.
	 */
	public void handleMouseInput()
	{
		super.handleMouseInput();
		int i = Mouse.getEventDWheel();
		
		if(i != 0 && this.needsScrollBars())
		{
			int j = ((AQGuiContainerCreative.ContainerCreative) this.inventorySlots).itemList.size() / 9 - 5 + 1;
			
			if(i > 0)
			{
				i = 1;
			}
			
			if(i < 0)
			{
				i = -1;
			}
			
			this.currentScroll = (float) (this.currentScroll - (double) i / (double) j);
			
			if(this.currentScroll < 0.0F)
			{
				this.currentScroll = 0.0F;
			}
			
			if(this.currentScroll > 1.0F)
			{
				this.currentScroll = 1.0F;
			}
			
			((AQGuiContainerCreative.ContainerCreative) this.inventorySlots).scrollTo(this.currentScroll);
		}
	}
	
	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawBackground(int p_73863_1_, int p_73863_2_, float p_73863_3_)
	{
		boolean flag = Mouse.isButtonDown(0);
		int k = this.guiLeft;
		int l = this.guiTop;
		int i1 = k + 175;
		int j1 = l + 18;
		int k1 = i1 + 14;
		int l1 = j1 + 112;
		
		if(!this.wasClicking && flag && p_73863_1_ >= i1 && p_73863_2_ >= j1 && p_73863_1_ < k1 && p_73863_2_ < l1)
		{
			this.isScrolling = this.needsScrollBars();
		}
		
		if(!flag)
		{
			this.isScrolling = false;
		}
		
		this.wasClicking = flag;
		
		if(this.isScrolling)
		{
			this.currentScroll = (p_73863_2_ - j1 - 7.5F) / (l1 - j1 - 15.0F);
			
			if(this.currentScroll < 0.0F)
			{
				this.currentScroll = 0.0F;
			}
			
			if(this.currentScroll > 1.0F)
			{
				this.currentScroll = 1.0F;
			}
			
			((AQGuiContainerCreative.ContainerCreative) this.inventorySlots).scrollTo(this.currentScroll);
		}
		
		CreativeTabs creativetabs = CreativeTabs.creativeTabArray[selectedTabIndex];
		
		this.background = new ResourceLocation("textures/gui/container/creative_inventory/tab_" + creativetabs.getBackgroundImageName());
		super.drawBackground(p_73863_1_, p_73863_2_, p_73863_3_);
	}
	
	@Override
	public void drawForeground(int mouseX, int mouseY, float ticks)
	{
		super.drawForeground(mouseX, mouseY, ticks);
		
		CreativeTabs[] acreativetabs = CreativeTabs.creativeTabArray;
		
		int start = tabPage * 10;
		int i2 = Math.min(acreativetabs.length, ((tabPage + 1) * 10) + 2);
		if(tabPage != 0)
			start += 2;
		boolean rendered = false;
		
		for(int j2 = start; j2 < i2; ++j2)
		{
			CreativeTabs creativetabs = acreativetabs[j2];
			
			if(creativetabs == null || creativetabs == CreativeTabs.tabInventory)
				continue;
			
			if(this.renderCreativeInventoryHoveringText(creativetabs, mouseX, mouseY))
			{
				rendered = true;
				break;
			}
		}
		
		if(!rendered)
			renderCreativeInventoryHoveringText(CreativeTabs.tabAllSearch, mouseX, mouseY);
		
		if(this.maxPages != 0)
		{
			String page = String.format("%d / %d", Integer.valueOf(tabPage + 1), Integer.valueOf(this.maxPages + 1));
			int width = this.fontRendererObj.getStringWidth(page);
			GL11.glDisable(GL11.GL_LIGHTING);
			this.zLevel = 300.0F;
			itemRender.zLevel = 300.0F;
			this.fontRendererObj.drawString(page, this.guiLeft + (this.xSize / 2) - (width / 2), this.guiTop - 44, -1);
			this.zLevel = 0.0F;
			itemRender.zLevel = 0.0F;
		}
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_LIGHTING);
	}
	
	protected void renderToolTip(ItemStack p_146285_1_, int p_146285_2_, int p_146285_3_)
	{
		if(selectedTabIndex == CreativeTabs.tabAllSearch.getTabIndex())
		{
			List list = p_146285_1_.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);
			CreativeTabs creativetabs = p_146285_1_.getItem().getCreativeTab();
			
			if(creativetabs == null && p_146285_1_.getItem() == Items.enchanted_book)
			{
				Map map = EnchantmentHelper.getEnchantments(p_146285_1_);
				
				if(map.size() == 1)
				{
					Enchantment enchantment = Enchantment.enchantmentsList[((Integer) map.keySet().iterator().next()).intValue()];
					CreativeTabs[] acreativetabs = CreativeTabs.creativeTabArray;
					int k = acreativetabs.length;
					
					for(int l = 0; l < k; ++l)
					{
						CreativeTabs creativetabs1 = acreativetabs[l];
						
						if(creativetabs1.func_111226_a(enchantment.type))
						{
							creativetabs = creativetabs1;
							break;
						}
					}
				}
			}
			
			if(creativetabs != null)
			{
				list.add(1, "" + EnumChatFormatting.BOLD + EnumChatFormatting.BLUE + I18n.format(creativetabs.getTranslatedTabLabel(), new Object[0]));
			}
			
			for(int i1 = 0; i1 < list.size(); ++i1)
			{
				if(i1 == 0)
				{
					list.set(i1, p_146285_1_.getRarity().rarityColor + (String) list.get(i1));
				}
				else
				{
					list.set(i1, EnumChatFormatting.GRAY + (String) list.get(i1));
				}
			}
			
			this.func_146283_a(list, p_146285_2_, p_146285_3_);
		}
		else
		{
			super.renderToolTip(p_146285_1_, p_146285_2_, p_146285_3_);
		}
	}
	
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderHelper.enableGUIStandardItemLighting();
		
		CreativeTabs[] acreativetabs = CreativeTabs.creativeTabArray;
		CreativeTabs creativetabs = acreativetabs[selectedTabIndex];
		
		int k = acreativetabs.length;
		int l;
		
		int start = tabPage * 10;
		k = Math.min(acreativetabs.length, ((tabPage + 1) * 10 + 2));
		if(tabPage != 0)
			start += 2;
		
		for(l = start; l < k; ++l)
		{
			CreativeTabs creativetabs1 = acreativetabs[l];
			
			if(creativetabs1 == null || creativetabs1 == CreativeTabs.tabInventory || creativetabs1.getTabIndex() == selectedTabIndex)
				continue;
			
			this.mc.getTextureManager().bindTexture(field_147061_u);
			this.func_147051_a(creativetabs1);
		}
		
		if(tabPage != 0)
		{
			if(creativetabs != CreativeTabs.tabAllSearch && creativetabs != CreativeTabs.tabInventory)
			{
				this.mc.getTextureManager().bindTexture(field_147061_u);
				func_147051_a(CreativeTabs.tabAllSearch);
			}
		}
	}
	
	protected void drawGuiContainerElements(float p_73863_3_, int p_146979_1_, int p_146979_2_)
	{
		CreativeTabs creativetabs = CreativeTabs.creativeTabArray[selectedTabIndex];
		
		if(creativetabs == null || creativetabs == CreativeTabs.tabInventory)
			return;
		
		this.searchField.drawTextBox();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		int i1 = this.guiLeft + 175;
		int k = this.guiTop + 18;
		int l = k + 112;
		
		this.mc.getTextureManager().bindTexture(field_147061_u);
		
		if(creativetabs.shouldHidePlayerInventory())
			this.drawTexturedModalRect(i1, k + (int) ((l - k - 17) * this.currentScroll), 232 + (this.needsScrollBars() ? 0 : 12), 0, 12, 15);
		
		if(creativetabs.getTabPage() != tabPage && creativetabs != CreativeTabs.tabAllSearch)
			return;
		
		this.func_147051_a(creativetabs);
	}
	
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
	{
		CreativeTabs creativetabs = CreativeTabs.creativeTabArray[selectedTabIndex];
		
		if(creativetabs != null && creativetabs != CreativeTabs.tabInventory && creativetabs.drawInForegroundOfTab())
		{
			GL11.glDisable(GL11.GL_BLEND);
			this.fontRendererObj.drawString(I18n.format(creativetabs.getTranslatedTabLabel(), new Object[0]), 8, 6, 4210752);
		}
	}
	
	protected boolean func_147049_a(CreativeTabs p_147049_1_, int p_147049_2_, int p_147049_3_)
	{
		if(p_147049_1_ == CreativeTabs.tabInventory)
			return false;
		
		if(p_147049_1_.getTabPage() != tabPage)
		{
			if(p_147049_1_ != CreativeTabs.tabAllSearch)
			{
				return false;
			}
		}
		
		int k = p_147049_1_.getTabColumn();
		int l = 28 * k;
		byte b0 = 0;
		
		if(k == 5)
		{
			l = this.xSize - 28 + 2;
		}
		else if(k > 0)
		{
			l += k;
		}
		
		int i1;
		
		if(p_147049_1_.isTabInFirstRow())
		{
			i1 = b0 - 32;
		}
		else
		{
			i1 = b0 + this.ySize;
		}
		
		return p_147049_2_ >= l && p_147049_2_ <= l + 28 && p_147049_3_ >= i1 && p_147049_3_ <= i1 + 32;
	}
	
	/**
	 * Renders the creative inventory hovering text if mouse is over it. Returns
	 * true if did render or false otherwise. Params: current creative tab to be
	 * checked, current mouse x position, current mouse y position.
	 */
	protected boolean renderCreativeInventoryHoveringText(CreativeTabs p_147052_1_, int p_147052_2_, int p_147052_3_)
	{
		int k = p_147052_1_.getTabColumn();
		int l = 28 * k;
		byte b0 = 0;
		
		if(k == 5)
		{
			l = this.xSize - 28 + 2;
		}
		else if(k > 0)
		{
			l += k;
		}
		
		int i1;
		
		if(p_147052_1_.isTabInFirstRow())
		{
			i1 = b0 - 32;
		}
		else
		{
			i1 = b0 + this.ySize;
		}
		
		if(this.func_146978_c(l + 3, i1 + 3, 23, 27, p_147052_2_, p_147052_3_))
		{
			this.drawCreativeTabHoveringText(I18n.format(p_147052_1_.getTranslatedTabLabel(), new Object[0]), p_147052_2_, p_147052_3_);
			return true;
		}
		
		return false;
	}
	
	protected void func_147051_a(CreativeTabs p_147051_1_)
	{
		boolean flag = p_147051_1_.getTabIndex() == selectedTabIndex;
		boolean flag1 = p_147051_1_.isTabInFirstRow();
		int i = p_147051_1_.getTabColumn();
		int j = i * 28;
		int k = 0;
		int l = this.guiLeft + 28 * i;
		int i1 = this.guiTop;
		byte b0 = 32;
		
		if(flag)
		{
			k += 32;
		}
		
		if(i == 5)
		{
			l = this.guiLeft + this.xSize - 28;
		}
		else if(i > 0)
		{
			l += i;
		}
		
		if(flag1)
		{
			i1 -= 28;
		}
		else
		{
			k += 64;
			i1 += this.ySize - 4;
		}
		
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glColor3f(1F, 1F, 1F); // Forge: Reset color in case Items change
									// it.
		GL11.glEnable(GL11.GL_BLEND); // Forge: Make sure blend is enabled else
										// tabs show a white border.
		this.drawTexturedModalRect(l, i1, j, k, 28, b0);
		this.zLevel = 100.0F;
		itemRender.zLevel = 100.0F;
		l += 6;
		i1 += 8 + (flag1 ? 1 : -1);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		ItemStack itemstack = p_147051_1_.getIconItemStack();
		itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), itemstack, l, i1);
		itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), itemstack, l, i1);
		GL11.glDisable(GL11.GL_LIGHTING);
		itemRender.zLevel = 0.0F;
		this.zLevel = 0.0F;
	}
	
	public void actionPerformed(GuiButton button)
	{
		if(button.id == 0)
		{
			this.mc.displayGuiScreen(new GuiAchievements(this, this.mc.thePlayer.getStatFileWriter()));
		}
		
		if(button.id == 1)
		{
			this.mc.displayGuiScreen(new GuiStats(this, this.mc.thePlayer.getStatFileWriter()));
		}
		
		if(button.id == 101)
		{
			tabPage = Math.max(tabPage - 1, 0);
		}
		else if(button.id == 102)
		{
			tabPage = Math.min(tabPage + 1, this.maxPages);
		}
		
		if(this.parent != null)
			this.parent.actionPerformed(button);
	}
	
	@SuppressWarnings("static-method")
	public int func_147056_g()
	{
		/** Currently selected creative inventory tab index. */
		return selectedTabIndex;
	}
	
	@SideOnly(Side.CLIENT)
	static class ContainerCreative extends Container
	{
		/** the list of items in this container */
		public List itemList = new ArrayList();
		
		public ContainerCreative(EntityPlayer p_i1086_1_)
		{
			InventoryPlayer inventoryplayer = p_i1086_1_.inventory;
			int i;
			
			for(i = 0; i < 5; ++i)
			{
				for(int j = 0; j < 9; ++j)
				{
					this.addSlotToContainer(new Slot(AQGuiContainerCreative.field_147060_v, i * 9 + j, 9 + j * 18, 18 + i * 18));
				}
			}
			
			for(i = 0; i < 9; ++i)
			{
				this.addSlotToContainer(new Slot(inventoryplayer, i, 9 + i * 18, 112));
			}
			
			this.scrollTo(0.0F);
		}
		
		public boolean canInteractWith(EntityPlayer p_75145_1_)
		{
			return true;
		}
		
		/**
		 * Updates the gui slots ItemStack's based on scroll position.
		 */
		public void scrollTo(float p_148329_1_)
		{
			int i = this.itemList.size() / 9 - 5 + 1;
			int j = (int) (p_148329_1_ * i + 0.5D);
			
			if(j < 0)
			{
				j = 0;
			}
			
			for(int k = 0; k < 5; ++k)
			{
				for(int l = 0; l < 9; ++l)
				{
					int i1 = l + (k + j) * 9;
					
					if(i1 >= 0 && i1 < this.itemList.size())
					{
						AQGuiContainerCreative.field_147060_v.setInventorySlotContents(l + k * 9, (ItemStack) this.itemList.get(i1));
					}
					else
					{
						AQGuiContainerCreative.field_147060_v.setInventorySlotContents(l + k * 9, (ItemStack) null);
					}
				}
			}
		}
		
		public boolean func_148328_e()
		{
			return this.itemList.size() > 45;
		}
		
		protected void retrySlotClick(int p_75133_1_, int p_75133_2_, boolean p_75133_3_, EntityPlayer p_75133_4_)
		{}
		
		/**
		 * Called when a player shift-clicks on a slot. You must override this
		 * or you will crash when someone does that.
		 */
		public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int p_82846_2_)
		{
			if(p_82846_2_ >= this.inventorySlots.size() - 9 && p_82846_2_ < this.inventorySlots.size())
			{
				Slot slot = (Slot) this.inventorySlots.get(p_82846_2_);
				
				if(slot != null && slot.getHasStack())
				{
					slot.putStack((ItemStack) null);
				}
			}
			
			return null;
		}
		
		public boolean func_94530_a(ItemStack p_94530_1_, Slot p_94530_2_)
		{
			return p_94530_2_.yDisplayPosition > 90;
		}
		
		/**
		 * Returns true if the player can "drag-spilt" items into this slot,.
		 * returns true by default. Called to check if the slot can be added to
		 * a list of Slots to split the held ItemStack across.
		 */
		public boolean canDragIntoSlot(Slot p_94531_1_)
		{
			return p_94531_1_.inventory instanceof InventoryPlayer || p_94531_1_.yDisplayPosition > 90 && p_94531_1_.xDisplayPosition <= 162;
		}
	}
	
	@SideOnly(Side.CLIENT)
	class CreativeSlot extends Slot
	{
		private final Slot field_148332_b;
		
		public CreativeSlot(Slot p_i1087_2_, int p_i1087_3_)
		{
			super(p_i1087_2_.inventory, p_i1087_3_, 0, 0);
			this.field_148332_b = p_i1087_2_;
		}
		
		public void onPickupFromSlot(EntityPlayer p_82870_1_, ItemStack p_82870_2_)
		{
			this.field_148332_b.onPickupFromSlot(p_82870_1_, p_82870_2_);
		}
		
		/**
		 * Check if the stack is a valid item for this slot. Always true beside
		 * for the armor slots.
		 */
		public boolean isItemValid(ItemStack p_75214_1_)
		{
			return this.field_148332_b.isItemValid(p_75214_1_);
		}
		
		/**
		 * Helper fnct to get the stack in the slot.
		 */
		public ItemStack getStack()
		{
			return this.field_148332_b.getStack();
		}
		
		/**
		 * Returns if this slot contains a stack.
		 */
		public boolean getHasStack()
		{
			return this.field_148332_b.getHasStack();
		}
		
		/**
		 * Helper method to put a stack in the slot.
		 */
		public void putStack(ItemStack p_75215_1_)
		{
			this.field_148332_b.putStack(p_75215_1_);
		}
		
		/**
		 * Called when the stack in a Slot changes
		 */
		public void onSlotChanged()
		{
			this.field_148332_b.onSlotChanged();
		}
		
		/**
		 * Returns the maximum stack size for a given slot (usually the same as
		 * getInventoryStackLimit(), but 1 in the case of armor slots)
		 */
		public int getSlotStackLimit()
		{
			return this.field_148332_b.getSlotStackLimit();
		}
		
		/**
		 * Returns the icon index on items.png that is used as background image
		 * of the slot.
		 */
		public IIcon getBackgroundIconIndex()
		{
			return this.field_148332_b.getBackgroundIconIndex();
		}
		
		/**
		 * Decrease the size of the stack in slot (first int arg) by the amount
		 * of the second int arg. Returns the new stack.
		 */
		public ItemStack decrStackSize(int p_75209_1_)
		{
			return this.field_148332_b.decrStackSize(p_75209_1_);
		}
		
		/**
		 * returns true if this slot is in par2 of par1
		 */
		public boolean isSlotInInventory(IInventory p_75217_1_, int p_75217_2_)
		{
			return this.field_148332_b.isSlotInInventory(p_75217_1_, p_75217_2_);
		}
	}
}
