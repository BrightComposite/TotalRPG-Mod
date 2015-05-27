package aqua.rpgmod.service.config;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import aqua.rpgmod.service.AQStringTranslator;

public class AQMapProperty extends AQProperty
{
	final HashMap<String, String> properties = new HashMap<String, String>();

	public AQMapProperty() {}
	
	public AQMapProperty(String string)
	{
		fromString(string);
	}
	
	public boolean getProperty(String name, AQProperty value)
	{
		String str = this.properties.get(name);

		if(str == null)
			return false;
		
		return value.fromString(str);
	}

	public boolean setProperty(String name, AQProperty value)
	{
		String str = value.toString();
		
		if(str == null)
			return false;
		
		this.properties.put(name, str);
		return true;
	}

	public void deleteProperty(String name)
	{
		this.properties.remove(name);
	}

	@Override
	public void read(DataInputStream stream) throws IOException
	{
		if(stream.available() <= 0)
			return;
		
		int i = stream.readInt();
		
		while(--i >= 0)
			this.properties.put(AQStringTranslator.deserialize(stream), AQStringTranslator.deserialize(stream));
	}
	
	@Override
	public void write(DataOutputStream stream) throws IOException
	{
		stream.writeInt(this.properties.size());
		
		for(Iterator<Entry<String, String>> i = this.properties.entrySet().iterator(); i.hasNext();)
		{
			Entry<String, String> entry = i.next();
			
			AQStringTranslator.serialize(stream, entry.getKey());
			AQStringTranslator.serialize(stream, entry.getValue().toString());
		}
	}
	
}
