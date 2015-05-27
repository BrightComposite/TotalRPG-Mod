package aqua.rpgmod.service.network;

public class AQWebSettings
{
	public static String domain = "http://172.25.36.114/minecraft/";
	
	public static String getFileURL(String name)
	{
		return domain + "/files/" + name;
	}
}
