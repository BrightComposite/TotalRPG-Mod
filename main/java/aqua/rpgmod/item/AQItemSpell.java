package aqua.rpgmod.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import aqua.rpgmod.AquaMod;
import aqua.rpgmod.entity.AQEntitySpellCharge;
import aqua.rpgmod.player.AQPlayerWrapper;
import aqua.rpgmod.player.chat.AQChatMessages;
import cpw.mods.fml.common.registry.GameRegistry;

public class AQItemSpell extends Item
{
	public AQItemSpell()
	{
		Object recipe[] = new Object[]
		{
			"AAA", "ABA", "AAA", Character.valueOf('A'), Items.paper, Character.valueOf('B'), Items.diamond
		};
		
		GameRegistry.addRecipe(new ItemStack(this, 1), recipe);
		
		setUnlocalizedName("spell");
		this.maxStackSize = 1;
		AQItems.addItem(this);
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack itemStack)
	{
		return EnumAction.block;
	}

    public int getMaxItemUseDuration(ItemStack p_77626_1_)
    {
        return 72000;
    }

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer)
	{
		AQPlayerWrapper wrapper = AquaMod.proxy.getWrapper(entityPlayer);
		
		float mana = wrapper.getMana();
		
        if(entityPlayer.capabilities.isCreativeMode || mana >= 20)
        {
        	entityPlayer.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
        }
        else
		{
			wrapper.chatMessage(AQChatMessages.needMana);
			return itemStack;
		}
		

		return itemStack;
	}
	
    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer entityPlayer, int ticksUsed)
    {
    	world.spawnEntityInWorld(new AQEntitySpellCharge(world, entityPlayer));
    }
}
