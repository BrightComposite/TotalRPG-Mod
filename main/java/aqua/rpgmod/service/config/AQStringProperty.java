package aqua.rpgmod.service.config;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import aqua.rpgmod.service.AQStringTranslator;

public class AQStringProperty extends AQProperty
{
	public String value;

	public AQStringProperty()
	{
		this.value = "";
	}
	
	public AQStringProperty(String value)
	{
		this.value = value;
	}
	
	@Override
	public boolean fromString(String string)
	{
		this.value = string;
		return true;
	}
	
	@Override
	public String toString()
	{
		return this.value;
	}
	
	@Override
	public void read(DataInputStream stream) throws IOException
	{
		this.value = AQStringTranslator.deserialize(stream);
	}

	@Override
	public void write(DataOutputStream stream) throws IOException
	{
		AQStringTranslator.serialize(stream, this.value);
	}
}
