package aqua.rpgmod.craft;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

abstract public class AQCraft
{
	protected ArrayList<AQCraftCollection> collections = new ArrayList<AQCraftCollection>();
	
	public void register(AQCraftCollection collection)
	{
		this.collections.add(collection);
	}
	
	public AQCraftResult getResult(IInventory inv, EntityPlayer player, int width, int height)
	{
		AQHashItemStack[] stacks = genBuffer(inv.getSizeInventory(), 1);
		
		for(int i = 0; i < stacks.length; i++)
			stacks[i] = new AQHashItemStack(inv.getStackInSlot(i));
		
		for(Iterator<AQCraftCollection> i = this.collections.iterator(); i.hasNext();)
		{
			AQCraftResult result = i.next().get(player, width, height, stacks);
			
			if(result != null)
				return result;
		}
		
		return null;
	}
	
	public static AQHashItemStack[] genBuffer(int width, int height)
	{
		AQHashItemStack[] buffer = new AQHashItemStack[width * height];
		
		for(int i = 0; i < buffer.length; i++)
			buffer[i] = new AQHashItemStack((ItemStack) null);
		
		return buffer;
	}
	
	public static AQHashItemStack[] genHashItemStacks(ItemStack ... stacks)
	{
		AQHashItemStack[] buffer = genBuffer(stacks.length, 1);
		
		for(int i = 0; i < buffer.length; i++)
			buffer[i] = new AQHashItemStack(stacks[i]);
		
		return buffer;
	}
	
	public static AQHashItemStack[] genHashItemStacks(List stacks)
	{
		AQHashItemStack[] buffer = genBuffer(stacks.size(), 1);
		
		Iterator it = stacks.iterator();
		
		for(int i = 0; it.hasNext(); i++)
			buffer[i] = new AQHashItemStack((ItemStack) it.next());
		
		return buffer;
	}
	
	public static int generateHash(AQHashItemStack ... stacks)
	{
		CRC32 crc = new CRC32();
		crc.reset();
		
		for(int i = 0; i < stacks.length; i++)
		{
			int id = stacks[i].id;
			crc.update(new byte[]{(byte) (id & 0xFF), (byte) ((id >>= 8) & 0xFF), (byte) ((id >>= 8) & 0xFF), (byte) ((id >>= 8) & 0xFF)});
		}
		
		return (int) crc.getValue();
	}
	
}
