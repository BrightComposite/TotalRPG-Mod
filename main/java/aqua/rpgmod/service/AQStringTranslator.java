package aqua.rpgmod.service;

import io.netty.buffer.ByteBuf;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import aqua.rpgmod.service.network.AQTranslator;

public class AQStringTranslator implements AQTranslator
{
	public final String string;
	
	public AQStringTranslator(String string)
	{
		this.string = string;
	}
	
	public AQStringTranslator(ByteBuf buf)
	{
		this.string = deserialize(buf);
	}

	@Override
	public ByteBuf write(ByteBuf buf)
	{
		return serialize(buf, this.string);
	}

	public static ByteBuf serialize(ByteBuf buffer, String string)
	{
		if(string == null || string.isEmpty())
			return buffer.writeInt(0);
		
		return buffer.writeInt(string.length()).writeBytes(string.getBytes());
	}
	
	public static String deserialize(ByteBuf buffer)
	{
		return buffer.readBytes(buffer.readInt()).toString(Charset.defaultCharset());
	}
	
	public static DataOutputStream serialize(DataOutputStream stream, String string)
	{
		try
		{
			if(string == null || string.isEmpty())
			{
				stream.writeInt(0);
				return stream;
			}
			
			stream.writeInt(string.length());
			stream.write(string.getBytes());
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		return stream;
	}
	
	public static String deserialize(DataInputStream stream)
	{
		try
		{
			int length = stream.readInt();
			byte bytes[] = new byte[length];
			
			stream.read(bytes);
			return new String(bytes);
		}
		catch(IOException e)
		{
			return null;
		}
	}
}
