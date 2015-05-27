package aqua.rpgmod.service.config;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import aqua.rpgmod.service.config.AQProperty;

public class AQLongProperty extends AQProperty
{
	public long value;

	public AQLongProperty()
	{
		this.value = 0;
	}
	
	public AQLongProperty(long value)
	{
		this.value = value;
	}
	
	@Override
	public void read(DataInputStream stream) throws IOException
	{
		this.value = stream.readLong();
	}

	@Override
	public void write(DataOutputStream stream) throws IOException
	{
		stream.writeLong(this.value);
	}
}
