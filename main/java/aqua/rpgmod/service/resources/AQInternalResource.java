package aqua.rpgmod.service.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AQInternalResource extends AQNamedResource
{
	public AQInternalResource(String path)
	{
		super(path);
	}
	
	public AQInternalResource(String domain, String path)
	{
		super(domain, path);
	}
	
	public String getPath()
	{
		return "/assets/totalrpg/" + this.domain + "/" + this.path;
	}
	
	@Override
	public InputStream getInput() throws IOException
	{
		return AQInternalResource.class.getResourceAsStream(getPath());
	}
	
	@Override
	public OutputStream getOutput() throws IOException
	{
		return null;
	}
	
	@Override
	public boolean canRead()
	{
		return AQInternalResource.class.getResource(getPath()) != null;
	}
	
	@Override
	public boolean canWrite()
	{
		return false;
	}
}
