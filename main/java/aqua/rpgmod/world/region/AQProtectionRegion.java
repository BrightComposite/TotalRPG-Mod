package aqua.rpgmod.world.region;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import aqua.rpgmod.geometry.AQShape3D;
import aqua.rpgmod.world.region.AQPlayerProtectionUnit.Status;

public class AQProtectionRegion extends AQRegion implements AQProtectionUnit
{
	final Set<AQProtectionUnit.Flag> flags = new HashSet<AQProtectionUnit.Flag>();
	final HashMap<String, AQPlayerProtectionUnit> members = new HashMap<String, AQPlayerProtectionUnit>();
	
	AQProtectionRegion(String name)
	{
		super(name, RegionType.PROTECTION);
	}
	
	public AQProtectionRegion(String name, AQRegion parent, AQShape3D shape, EntityPlayer owner)
	{
		super(name, RegionType.PROTECTION, parent, shape);
		this.flags.addAll(Arrays.asList(AQProtectionUnit.Flag.values()));
		new AQPlayerProtectionUnit(this, owner.getCommandSenderName().toLowerCase(), Status.OWNER);
	}

	public AQProtectionRegion(String name, AQRegion parent, AQShape3D shape, EntityPlayer owner, Flag ... flags)
	{
		super(name, RegionType.PROTECTION, parent, shape);
		this.flags.addAll(Arrays.asList(flags));
		new AQPlayerProtectionUnit(this, owner.getCommandSenderName().toLowerCase(), Status.OWNER);
	}

	public void addPlayer(String player, Status status)
	{
		new AQPlayerProtectionUnit(this, player.toLowerCase(), status);
		update();
	}
	
	public void removePlayer(String player)
	{
		this.members.remove(player);
		update();
	}

	public void clearPlayers()
	{
		this.members.clear();
		update();
	}

	public String[] getPlayers()
	{
		ArrayList<String> list = new ArrayList<String>();
		
		for(Iterator<AQPlayerProtectionUnit> i = this.members.values().iterator(); i.hasNext();)
		{
			AQPlayerProtectionUnit unit = i.next();
			list.add((unit.status == Status.OWNER ? EnumChatFormatting.GOLD : EnumChatFormatting.GREEN) + unit.player);
		}
		
		return list.toArray(new String[0]);
	}
	
	@Override
	public void addFlags(Flag ... flags)
	{
		this.flags.addAll(Arrays.asList(flags));
		update();
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
			update();
			return;
		}
		
		this.flags.removeAll(Arrays.asList(flags));
		update();
	}
	
	@Override
	public boolean checkFlags(Flag ... flags)
	{
		return this.flags.containsAll(Arrays.asList(flags));
	}
	
	public Flag[] getFlags()
	{
		return this.flags.toArray(new Flag[0]);
	}
	
	public AQPlayerProtectionUnit findPlayer(EntityPlayer player)
	{
		return this.members.get(player.getCommandSenderName().toLowerCase());
	}
	
	@Override
	public boolean shouldDraw()
	{
		return true;
	}
	
	@Override
	public boolean permanentView()
	{
		return false;
	}
	
	@Override
	public float[] color()
	{
		return new float[]
		{
			0.2f, 0.7f, 1.0f
		};
	}
	
	@Override
	public ByteBuf writeInfo(ByteBuf buf)
	{
		super.writeInfo(buf);
		buf.writeInt(this.flags.size());
		
		for(Iterator<Flag> i = this.flags.iterator(); i.hasNext();)
			buf.writeInt(i.next().ordinal());

		buf.writeInt(this.members.size());

		for(Iterator<AQPlayerProtectionUnit> i = this.members.values().iterator(); i.hasNext();)
			i.next().writeInfo(buf);

		return buf;
	}
	
	@Override
	public ByteBuf readInfo(ByteBuf buf)
	{
		super.readInfo(buf);
		int flags = buf.readInt();
		
		for(int i = 0; i < flags; ++i)
			this.flags.add(Flag.values()[buf.readInt()]);
		
		int members = buf.readInt();

		for(int i = 0; i < members; ++i)
			new AQPlayerProtectionUnit(this, buf);
		
		return buf;
	}
}
