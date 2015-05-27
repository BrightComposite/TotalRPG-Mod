package aqua.rpgmod.service.config;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import aqua.rpgmod.service.config.AQProperty;

public class AQIntProperty extends AQProperty
{
	public int value;

	public AQIntProperty()
	{
		this.value = 0;
	}
	
	public AQIntProperty(int value)
	{
		this.value = value;
	}
	
	@Override
	public void read(DataInputStream stream) throws IOException
	{
		this.value = stream.readInt();
	}

	@Override
	public void write(DataOutputStream stream) throws IOException
	{
		stream.writeInt(this.value);
	}
}
