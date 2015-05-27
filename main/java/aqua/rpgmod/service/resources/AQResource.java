package aqua.rpgmod.service.resources;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

abstract public class AQResource
{
	abstract public InputStream getInput() throws IOException;
	
	abstract public OutputStream getOutput() throws IOException;
	
	@SuppressWarnings("unused")
	public void onClose(InputStream stream) throws IOException
	{}
	
	@SuppressWarnings("unused")
	public void onClose(OutputStream stream) throws IOException
	{}
	
	abstract public boolean canRead();
	
	abstract public boolean canWrite();
	
	public static void writeString(DataOutputStream output, String s) throws IOException
	{
		output.writeInt(s.length());
		output.writeBytes(s);
	}
	
	public static String readString(DataInputStream input) throws IOException
	{
		int length = input.readInt();
		byte[] bytes = new byte[length];
		input.readFully(bytes);
		
		return new String(bytes);
		
	}
}
