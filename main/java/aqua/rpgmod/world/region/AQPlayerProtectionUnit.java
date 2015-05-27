package aqua.rpgmod.world.region;

import io.netty.buffer.ByteBuf;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import aqua.rpgmod.service.AQStringTranslator;

public class AQPlayerProtectionUnit implements AQProtectionUnit
{
	final Set<AQProtectionUnit.Flag> flags = new HashSet<AQProtectionUnit.Flag>();
	public final String player;
	public Status status;

	public static enum PlayerAction
	{
		ADD,
		REMOVE,
		CLEAR,
		LIST,
		STATUS;
		
		public static PlayerAction get(String name)
		{
			for(PlayerAction action : PlayerAction.values())
			{
				if(name.compareToIgnoreCase(action.toString()) == 0)
					return action;
			}
			
			return null;
		}
	}
	
	public static enum Status
	{
		MEMBER,
		OWNER;
		
		public static Status get(String name)
		{
			for(Status status : Status.values())
			{
				if(name.compareToIgnoreCase(status.toString()) == 0)
					return status;
			}
			
			return null;
		}
	}

	AQPlayerProtectionUnit(AQProtectionRegion region, ByteBuf buf)
	{
		this(region, AQStringTranslator.deserialize(buf), Status.values()[buf.readInt()]);
		
		int flags = buf.readInt();
		
		for(int i = 0; i < flags; ++i)
			this.flags.add(Flag.values()[buf.readInt()]);
	}

	AQPlayerProtectionUnit(AQProtectionRegion region, String player, Status status)
	{
		this.player = player;
		this.status = status;

		this.flags.addAll(Arrays.asList(Flag.values()));
		region.members.put(this.player, this);
	}

	public ByteBuf writeInfo(ByteBuf buf)
	{
		AQStringTranslator.serialize(buf, this.player);
		buf.writeInt(this.status.ordinal());
		
		buf.writeInt(this.flags.size());
		
		for(Iterator<Flag> i = this.flags.iterator(); i.hasNext();)
			buf.writeInt(i.next().ordinal());

		return buf;
	}

	public void setStatus(Status status)
	{
		this.status = status;
	}
	
	@Override
	public void addFlags(Flag ... flags)
	{
		this.flags.addAll(Arrays.asList(flags));
	}
	
	@Override
	public void setFlags(Flag ... flags)
	{
		this.flags.clear();
		addFlags(flags);
	}
	
	@Override
	public void clearFlags(Flag ... flags)
	{
		if(flags.length == 0)
		{
			this.flags.clear();
			return;
		}
		
		this.flags.remove(Arrays.asList(flags));
	}
	
	@Override
	public boolean checkFlags(Flag ... flags)
	{
		return this.flags.containsAll(Arrays.asList(flags));
	}

	@Override
	public Flag[] getFlags()
	{
		return this.flags.toArray(Flag.values());
	}
}
