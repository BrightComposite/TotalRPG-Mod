package aqua.rpgmod.service.actions;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import aqua.rpgmod.AquaMod;
import aqua.rpgmod.geometry.AQPoint3D;
import aqua.rpgmod.service.AQChatManager;
import aqua.rpgmod.service.AQStringTranslator;
import aqua.rpgmod.service.network.AQTranslator;

abstract public class AQAction<T extends AQActionManager, A extends AQAction>
{
	public final T manager;
	public final ArrayList<String> params = new ArrayList<String>();
	public final EntityPlayer player;
	public final World world;
	
	public AQAction(T manager)
	{
		this.manager = manager;
		this.player = null;
		this.world = null;
	}
	
	public AQAction(T manager, EntityPlayer player, List<String> params)
	{
		this.manager = manager;
		this.player = player;
		
		World world = player.worldObj;
		
		for(Iterator<String> i = params.iterator(); i.hasNext();)
		{
			if(i.next().equals("/w"))
			{
				i.remove();
				world = AquaMod.proxy.getWorld(i.next(), player instanceof EntityPlayerMP);
				i.remove();
				break;
			}
		}
		
		this.params.addAll(params);
		this.world = world;
	}
	
	abstract public String getName();
	
	@Override
	public String toString()
	{
		return getName();
	}
	
	public String getFullName()
	{
		return this.manager.getName() + "." + getName();
	}
	
	public void incorrectUsage()
	{
		if(this.player == null)
			return;

		AQChatManager.message(this.player, AQChatManager.unite(
			AQChatManager.chatComponent(EnumChatFormatting.GREEN, "command.usage"),
			new ChatComponentText("`"), usage()));
	}
	
	public IChatComponent usage()
	{
		return AQChatManager.chatComponent(EnumChatFormatting.GOLD, "command." + getFullName() + ".usage");
	}
	
	public AQPoint3D getCoords(int startIndex)
	{
		if(this.params.size() < startIndex + 3)
			return null;
		
		try
		{
			return new AQPoint3D(
				Integer.valueOf(this.params.get(startIndex)).intValue(),
				Integer.valueOf(this.params.get(startIndex + 1)).intValue(),
				Integer.valueOf(this.params.get(startIndex + 2)).intValue());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public int getInt(int index, int def)
	{
		if(this.params.size() < index + 1)
			return def;
		
		try
		{
			return Integer.valueOf(this.params.get(index)).intValue();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return def;
		}
	}
	
	public EntityPlayer getPlayer(int index)
	{
		if(this.params.size() < index + 1)
			return null;
		
		return AquaMod.proxy.getWorld(this.player.dimension, this.player instanceof EntityPlayerMP).getPlayerEntityByName(this.params.get(index));
	}
	
	@SuppressWarnings("static-method")
	protected boolean isUndoable()
	{
		return false;
	}
	
	@SuppressWarnings("static-method")
	protected boolean undo()
	{
		return true;
	}
	
	public String getPermission()
	{
		return "totalrpg." + getFullName();
	}
	
	class Translator implements AQTranslator
	{
		@Override
		public ByteBuf write(ByteBuf buf)
		{
			AQStringTranslator.serialize(buf, AQAction.this.manager.getName());
			AQStringTranslator.serialize(buf, AQAction.this.getName());
			
			return AQAction.this.write(buf);
		}
	}
	
	public ByteBuf write(ByteBuf buffer)
	{
		buffer.writeInt(this.params.size());
		
		for(Iterator<String> i = this.params.iterator(); i.hasNext();)
		{
			AQStringTranslator.serialize(buffer, i.next());
		}
		
		return buffer;
	}
	
	public AQAction read(T manager, EntityPlayer player, ByteBuf buffer)
	{
		int count = buffer.readInt();
		List<String> params = new ArrayList<String>(count);
		
		for(int i = 0; i < count; ++i)
		{
			params.add(AQStringTranslator.deserialize(buffer));
		}
		
		return clone(manager, player, params);
	}
	
	abstract public boolean needPermission();
	
	abstract protected A clone(T manager, EntityPlayer player, List<String> params);
	
	abstract protected boolean execute();
}
