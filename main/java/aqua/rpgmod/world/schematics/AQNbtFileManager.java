package aqua.rpgmod.world.schematics;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

public class AQNbtFileManager
{
	public static void write(String name, NBTTagCompound tag, OutputStream out) throws IOException
	{
		NBTTagCompound compound = new NBTTagCompound();
		compound.setTag(name, tag);
		
		CompressedStreamTools.write(compound, new DataOutputStream(out));
	}
	
	public static NBTTagCompound read(String name, InputStream in) throws IOException
	{
		NBTTagCompound compound = CompressedStreamTools.read(new DataInputStream(in));
		return compound.getCompoundTag(name);
	}
}
