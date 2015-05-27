package aqua.rpgmod.service.config;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import aqua.rpgmod.service.config.AQProperty;

public class AQFloatProperty extends AQProperty
{
	public float value;

	public AQFloatProperty()
	{
		this.value = 0.0f;
	}
	
	public AQFloatProperty(float value)
	{
		this.value = value;
	}
	
	@Override
	public void read(DataInputStream stream) throws IOException
	{
		this.value = stream.readFloat();
	}

	@Override
	public void write(DataOutputStream stream) throws IOException
	{
		stream.writeFloat(this.value);
	}
}
