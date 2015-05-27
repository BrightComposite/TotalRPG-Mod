package aqua.rpgmod.service.resources;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AQByteResource extends AQResource
{
	public byte[] data;
	
	public AQByteResource()
	{
		this.data = null;
	}
	
	public AQByteResource(byte[] data)
	{
		this.data = data;
	}
	
	@Override
	public InputStream getInput() throws IOException
	{
		if(this.data == null)
			return null;
		
		return new ByteArrayInputStream(this.data);
	}
	
	@Override
	public OutputStream getOutput() throws IOException
	{
		return new ByteArrayOutputStream();
	}
	
	@Override
	public void onClose(OutputStream stream) throws IOException
	{
		if(stream instanceof ByteArrayOutputStream)
		{
			this.data = ((ByteArrayOutputStream) stream).toByteArray();
		}
	}
	
	@Override
	public boolean canRead()
	{
		return this.data != null;
	}
	
	@Override
	public boolean canWrite()
	{
		return true;
	}
	
}
