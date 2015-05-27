package aqua.rpgmod.service.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.minecraftforge.common.DimensionManager;

public class AQFileResource extends AQNamedResource
{
	public static File root = new File(DimensionManager.getCurrentSaveRootDirectory(), "total-rpg");
	
	public AQFileResource(String path)
	{
		super(path);
	}
	
	public AQFileResource(String domain, String path)
	{
		super(domain, path);
	}
	
	@Override
	public InputStream getInput() throws IOException
	{
		File file = getFile();
		
		if(!file.exists())
			return null;
		
		return new FileInputStream(file);
	}
	
	@Override
	public OutputStream getOutput() throws IOException
	{
		File file = getFile();
		String path = file.getPath();
		int slash = path.lastIndexOf(File.separator);
		
		if(slash > 0)
		{
			File subPath = new File(path.substring(0, slash));
			
			if(!subPath.exists())
				subPath.mkdirs();
		}
		
		if(!file.exists())
			file.createNewFile();
		
		return new FileOutputStream(file);
	}
	
	public File getFile()
	{
		return new File(new File(root, this.domain), this.path);
	}
	
	public AQFileResource append(String filename)
	{
		return new AQFileResource(this.domain, this.path + File.separator + filename);
	}

	public boolean exists()
	{
		return getFile().exists();
	}

	public boolean makeFile() throws IOException
	{
		File file = getFile();
		String path = file.getPath();
		int slash = path.lastIndexOf(File.separator);
		
		if(slash > 0)
		{
			File subPath = new File(path.substring(0, slash));
			
			if(!subPath.exists())
				subPath.mkdirs();
		}
		
		return file.createNewFile();
	}

	public boolean makeDirectory()
	{
		File file = getFile();
		String path = file.getPath();
		int slash = path.lastIndexOf(File.separator);
		
		if(slash > 0)
		{
			File subPath = new File(path.substring(0, slash));
			
			if(!subPath.exists())
				subPath.mkdirs();
		}
		
		return file.mkdir();
	}
	
	public static void delete(File file)
	{
		try
		{
			if(!file.exists())
				return;
			
			if(file.isDirectory())
			{
				for(File f : file.listFiles())
					delete(f);
				
				file.delete();
			}
			else
				file.delete();
		}
		catch(Exception localException)
		{}
	}
	
	public void delete()
	{
		delete(getFile());
	}
	
	@Override
	public boolean canRead()
	{
		File file = getFile();
		
		return file.exists() && file.canRead();
	}
	
	@Override
	public boolean canWrite()
	{
		File file = getFile();
		
		return !file.exists() || file.canWrite();
	}
}
