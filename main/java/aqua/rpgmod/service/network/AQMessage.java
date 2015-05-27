package aqua.rpgmod.service.network;

import net.minecraft.entity.player.EntityPlayer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class AQMessage implements IMessage
{
	public int type;
	public ByteBuf data = null;
	public EntityPlayer player = null;
	
	public static enum PlayerMessage
	{
		UPDATE,
		INTERACT,
		DEATH,
		RENDER,
		GET_RACE,
		SET_RACE,
		LOAD_ABILITY_GROUP,
		SET_ABILITY_GROUP,
		SET_SKILL_MODIFIER,
		CHANGE_EXPERIENCE;
	}
	
	public static enum ServiceMessage
	{
		ACTION,
		SET_BLOCK,
		SET_TILE,
		UPDATE_TILE,
		LOAD_TILE,
		SAVE_TILE;
	}
	
	public static enum RegionMessage
	{
		SYNC,
		SYNC_RESPONSE,
		ADD,
		UPDATE,
		REMOVE;
	}
	
	public AQMessage()
	{
		this.type = 0;
		this.data = null;
	}
	
	public AQMessage(Enum type)
	{
		this.type = type.ordinal();
		this.data = null;
	}
	
	public AQMessage(Enum type, AQTranslator ... translators)
	{
		this.type = type.ordinal();
		this.data = Unpooled.buffer();
		
		for(int i = 0; i < translators.length; ++i)
			translators[i].write(this.data);
	}
	
	@Override
	final public void fromBytes(ByteBuf buf)
	{
		this.type = buf.readInt();
		this.data = buf.readableBytes() == 0 ? null : buf.readBytes(buf.readableBytes());
	}
	
	@Override
	final public void toBytes(ByteBuf buf)
	{
		buf.writeInt(this.type);
		
		if(this.data == null)
			return;
		
		buf.writeBytes(this.data);
	}
	
}
