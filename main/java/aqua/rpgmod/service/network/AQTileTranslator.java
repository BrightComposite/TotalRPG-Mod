package aqua.rpgmod.service.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import aqua.rpgmod.world.schematics.AQNbtFileManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class AQTileTranslator implements AQTranslator
{
	byte data[];
	
	public AQTileTranslator(ByteBuf buf)
	{
		this.data = new byte[buf.readableBytes()];
		buf.readBytes(this.data);
	}
	
	public AQTileTranslator(TileEntity tile)
	{
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream output = new DataOutputStream(bytes);
		
		NBTTagCompound compound = new NBTTagCompound();
		NBTTagCompound tag = new NBTTagCompound();
		tile.writeToNBT(tag);
		
		compound.setTag("0", tag);
		
		try
		{
			AQNbtFileManager.write("tileData", compound, output);
			output.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		this.data = bytes.toByteArray();
	}
	
	@Override
	public ByteBuf write(ByteBuf buf)
	{
		return buf.writeBytes(this.data);
	}
	
	public void setTileNBT(TileEntity tile)
	{
		ByteArrayInputStream bytes = new ByteArrayInputStream(this.data);
		DataInputStream input = new DataInputStream(bytes);
		
		try
		{
			NBTTagCompound compound = AQNbtFileManager.read("tileData", input);
			input.close();
			tile.readFromNBT(compound.getCompoundTag("0"));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public TileEntity readTile()
	{
		ByteArrayInputStream bytes = new ByteArrayInputStream(this.data);
		DataInputStream input = new DataInputStream(bytes);
		
		try
		{
			NBTTagCompound compound = AQNbtFileManager.read("tileData", input);
			input.close();
			
			return TileEntity.createAndLoadEntity(compound.getCompoundTag("0"));
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
