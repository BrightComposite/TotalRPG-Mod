package aqua.rpgmod.adapter;

import java.util.ArrayList;

public class AdapterSideMod implements AdapterSide
{
	@Override
	public boolean readMessage(AdapterMessage id, ArrayList data)
	{
		switch(id)
		{
			case SetEnabled:
				ArrayList a = new ArrayList(1);
				
				if(!Adapter.SendMessageToPlugin(AdapterMessage.GetState, a))
					return false;
				
				return true;
				
			default:
				break;
		}
		
		return false;
	}
}
