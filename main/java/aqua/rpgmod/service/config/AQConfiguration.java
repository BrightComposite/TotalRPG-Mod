package aqua.rpgmod.service.config;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import aqua.rpgmod.service.resources.AQFileResource;

public class AQConfiguration extends AQMapProperty
{	
	final AQFileResource path;
	
	public AQConfiguration(AQFileResource path)
	{
		this.path = path;
	}
	
	public void load() throws IOException
	{
		if(!this.path.exists())
			return;

		DataInputStream stream = new DataInputStream(this.path.getInput());
		read(stream);
		stream.close();
	}
	
	public void save() throws IOException
	{
		if(this.path.exists())
			this.path.delete();

		DataOutputStream stream = new DataOutputStream(this.path.getOutput());
		write(stream);
		stream.close();
	}
}
