package aqua.rpgmod.service.plugins;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class AQPluginLoader
{
	ArrayList<URL> urls = new ArrayList<URL>();
	
	public void addLibrary(File path)
	{
		try
		{
			this.urls.add(path.toURI().toURL());
		}
		catch(MalformedURLException e)
		{
			e.printStackTrace();
		}
	}
	
	public void load() throws Exception
	{
		for(URL url : this.urls)
		{
			URLClassLoader cl = new URLClassLoader(new URL[]
			{
				url
			});
			InputStream is = cl.getResourceAsStream("/META-INF/MANIFEST.MF");
			
			if(is == null)
			{
				cl.close();
				continue;
			}
			
			Manifest mf = new Manifest(is);
			Attributes attr = mf.getMainAttributes();
			String mainClass = attr.getValue("TotalRPGPluginMain");
			
			if(mainClass != null)
			{
				Class<?> c = cl.loadClass(mainClass);
				Method main = c.getMethod("main", new Class[]
				{
					String[].class
				});
				main.invoke(null, new Object[0]);
			}
			
			is.close();
			cl.close();
		}
	}
}
