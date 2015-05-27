package aqua.rpgmod.service.interaction;

public class AQInteractionInfo
{	
	public final AQRayTraceInfo rayTrace;
	public final AQInteractor interactor;
	public final int id;
	
	public AQInteractionInfo(AQRayTraceInfo rayTrace, AQInteractor interactor, int id)
	{
		this.rayTrace = rayTrace;
		this.interactor = interactor;
		this.id = id;
	}
	
	public String getDesc()
	{
		return this.interactor.getDesc(this.rayTrace, this.id);
	}
}
