package aqua.rpgmod.player.chat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public class AQChatMessage implements AQChatMessageBase
{
	protected String message;
	
	public AQChatMessage(String message)
	{
		this.message = message;
	}
	
	@Override
	public void send(EntityPlayer player)
	{
		player.addChatMessage(new ChatComponentText(this.message));
	}
}
