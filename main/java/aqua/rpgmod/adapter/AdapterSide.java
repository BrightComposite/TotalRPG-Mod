package aqua.rpgmod.adapter;

import java.util.ArrayList;

public interface AdapterSide
{
	public boolean readMessage(AdapterMessage Id, ArrayList<Object> Data);
}
