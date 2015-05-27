package aqua.rpgmod.service.resources;


abstract public class AQNamedResource extends AQResource
{
	public final String path;
	public String domain;
	
	public AQNamedResource(String path)
	{
		this.path = path;
		this.domain = "";
	}
	
	public AQNamedResource(String domain, String path)
	{
		this.path = path;
		this.domain = domain;
	}
	
}
