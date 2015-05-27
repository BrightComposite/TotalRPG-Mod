package aqua.rpgmod.service.config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class AQProperty
{	
	public String toString()
	{
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream stream = new DataOutputStream(bytes);
		
		try
		{
			write(stream);
			stream.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return null;
		}
		
		return new String(bytes.toByteArray());
	}
	
	public boolean fromString(String string)
	{
		DataInputStream stream = new DataInputStream(new ByteArrayInputStream(string.getBytes()));
		
		try
		{
			read(stream);
			return true;
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	abstract public void read(DataInputStream stream) throws IOException;
	abstract public void write(DataOutputStream stream) throws IOException;
}
