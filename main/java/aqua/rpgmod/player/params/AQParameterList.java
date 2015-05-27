package aqua.rpgmod.player.params;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.Iterator;

public class AQParameterList implements AQReadWritable
{
	protected ArrayList<AQPlayerParameter> list = new ArrayList<AQPlayerParameter>();
	
	public void add(AQPlayerParameter parameter)
	{
		this.list.add(parameter);
	}
	
	@Override
	public ByteBuf write(ByteBuf buffer)
	{
		for(Iterator<AQPlayerParameter> i = this.list.iterator(); i.hasNext();)
			i.next().write(buffer);
		
		return buffer;
	}
	
	@Override
	public ByteBuf read(ByteBuf buffer)
	{
		for(Iterator<AQPlayerParameter> i = this.list.iterator(); i.hasNext();)
			i.next().read(buffer);
		
		return buffer;
	}
}
