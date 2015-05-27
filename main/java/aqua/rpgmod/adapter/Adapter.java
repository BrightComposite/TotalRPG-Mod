package aqua.rpgmod.adapter;

import java.util.ArrayList;

public class Adapter
{
	protected static AdapterSide sideMod = null;
	protected static AdapterSide sidePlugin = null;
	
	public static void SetMod(AdapterSide Side)
	{
		sideMod = Side;
	}
	
	public static void SetPlugin(AdapterSide Side)
	{
		sidePlugin = Side;
	}
	
	public static boolean SendMessageToMod(AdapterMessage Id, ArrayList<Object> Data)
	{
		if(sideMod != null)
			return sideMod.readMessage(Id, Data);
		
		return false;
	}
	
	public static boolean SendMessageToPlugin(AdapterMessage Id, ArrayList<Object> Data)
	{
		if(sidePlugin != null)
			return sidePlugin.readMessage(Id, Data);
		
		return false;
	}
}
