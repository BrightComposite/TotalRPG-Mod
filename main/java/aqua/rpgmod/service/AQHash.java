package aqua.rpgmod.service;

import java.util.zip.CRC32;

public class AQHash
{	
	public static long hash(byte[] bytes)
	{
		CRC32 crc32 = new CRC32();
		crc32.reset();
		crc32.update(bytes);
		return crc32.getValue();
	}
	
	public static long hash(String value)
	{
		return hash(value.getBytes());
	}
	
	public static long hash(int value)
	{
		return hash(new byte[]{(byte) (value & 0xFF), (byte) ((value >>= 8) & 0xFF), (byte) ((value >>= 8) & 0xFF), (byte) ((value >>= 8) & 0xFF)});
	}
}
