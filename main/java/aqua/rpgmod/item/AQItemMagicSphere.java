package aqua.rpgmod.item;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

import aqua.rpgmod.AquaMod;
import aqua.rpgmod.fx.AQFxPlacement;
import aqua.rpgmod.fx.AQRandomFxSystem;
import aqua.rpgmod.player.AQPlayerWrapper;
import aqua.rpgmod.player.chat.AQChatMessages;
import aqua.rpgmod.service.AQLogger;
import aqua.rpgmod.world.AQTeleporter;
import cpw.mods.fml.common.registry.GameRegistry;

public class AQItemMagicSphere extends Item
{
	public AQItemMagicSphere()
	{
		Object Recipe[] = new Object[]
		{
			"0BA", "BAB", "0B0", Character.valueOf('A'), Items.ender_eye, Character.valueOf('B'), Items.diamond
		};
		GameRegistry.addRecipe(new ItemStack(this, 1), Recipe);
		
		setUnlocalizedName("magic_sphere.tp");
		this.maxStackSize = 1;
		AQItems.addItem(this);
	}
	
	@Override
	public void onCreated(ItemStack itemStack, World world, EntityPlayer player)
	{
		NBTTagCompound stackTagCompound = new NBTTagCompound();
		stackTagCompound.setString("owner", player.getCommandSenderName());
		itemStack.stackTagCompound = stackTagCompound;
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack itemStack)
	{
		return EnumAction.block;
	}
	
	@Override
	public boolean canItemEditBlocks()
	{
		return false;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer)
	{
		NBTTagCompound stackTagCompound = itemStack.stackTagCompound;
		AQPlayerWrapper wrapper = AquaMod.proxy.getWrapper(entityPlayer);
		
		float mana = wrapper.getMana();
		
		if(mana < 20)
		{
			wrapper.chatMessage(AQChatMessages.needMana);
			return itemStack;
		}
		
		int coords[];
		
		if(stackTagCompound != null && stackTagCompound.hasKey("coords") && stackTagCompound.hasKey("dim"))
		{
			if(mana < 30)
			{
				wrapper.chatMessage(AQChatMessages.needMana);
				return itemStack;
			}
			
			coords = stackTagCompound.getIntArray("coords");
			int dim = stackTagCompound.getInteger("dim");
			
			float color[] = new float[]
			{
				0.7f, 0.0f, 1.0f
			};
			
			AQRandomFxSystem.instance.spawnFx(entityPlayer.getEntityWorld(), new AQFxPlacement.StaticPlacement(entityPlayer), color);
			
			if(AQTeleporter.teleportTo(entityPlayer, dim, coords))
			{
				AQRandomFxSystem.instance.spawnFx(entityPlayer.getEntityWorld(), new AQFxPlacement.StaticPlacement(entityPlayer), color);
				wrapper.mana.changeBy(-30);
			}
			
			return itemStack;
		}
		
		stackTagCompound = new NBTTagCompound();
		stackTagCompound.setString("owner", entityPlayer.getCommandSenderName());
		itemStack.stackTagCompound = stackTagCompound;
		
		AquaMod.proxy.getWrapper(entityPlayer).mana.changeBy(-20);
		
		coords = new int[]
		{
			(int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ
		};
		
		stackTagCompound.setIntArray("coords", coords);
		stackTagCompound.setInteger("dim", entityPlayer.dimension);
		
		AQLogger.log(
			Level.INFO,
			"Create magic teleporter for player " + entityPlayer.getCommandSenderName() + ", dimension: " + entityPlayer.worldObj.provider.getDimensionName()
				+ ", coords: " + coords[0] + ", " + coords[1] + ", " + coords[2]);
		
		return itemStack;
	}
	
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean b)
	{
		super.addInformation(itemStack, entityPlayer, list, b);
		
		NBTTagCompound stackTagCompound = itemStack.stackTagCompound;
		
		if(stackTagCompound == null)
			return;
		
		String owner = stackTagCompound.getString("owner");
		
		if(owner == null)
			return;
		
		list.add(EnumChatFormatting.DARK_GREEN + StatCollector.translateToLocalFormatted("item.sphere.ownedby ") + owner);
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		if(super.onLeftClickEntity(stack, player, entity))
			return true;
		
		return true;
	}
	
}
