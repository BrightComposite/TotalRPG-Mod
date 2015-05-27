package aqua.rpgmod.service.network;

import java.util.HashMap;

import aqua.rpgmod.AquaMod;

import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

abstract public class AQMessenger<Msg extends AQMessage> implements IMessageHandler<Msg, Msg>
{
	private HashMap<Integer, AQMessageReader<Msg>> readers = new HashMap<Integer, AQMessageReader<Msg>>();
	
	@Override
	public Msg onMessage(Msg message, MessageContext ctx)
	{
		AQMessageReader reader = this.readers.get(Integer.valueOf(message.type));
		message.player = AquaMod.proxy.getPlayer(ctx);
		
		if(reader != null)
			reader.read(message);
		
		return null;
	}
	
	public void put(Enum e, AQMessageReader<Msg> reader)
	{
		this.readers.put(Integer.valueOf(e.ordinal()), reader);
	}
	
}
