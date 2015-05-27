package aqua.rpgmod.client.render;

import java.util.HashMap;

import net.minecraft.util.ResourceLocation;

public class AQTextureMap
{
	public enum TextureType
	{
		SKIN,
		BEARD,
		EAR,
		SPIDER,
		HORSE;
		
		public final String name;
		
		private TextureType()
		{
			this.name = this.name().toLowerCase();
		}
		
		@Override
		public String toString()
		{
			return this.name;
		}
	}
	
	protected HashMap<TextureType, ResourceLocation> textures = new HashMap<TextureType, ResourceLocation>();
	protected String userName;
	
	public AQTextureMap()
	{
		this.userName = null;
	}
	
	public AQTextureMap(String userName)
	{
		this.userName = userName;
	}
	
	public ResourceLocation get(TextureType type)
	{
		return this.textures.get(type);
	}
	
	public void set(TextureType type, ResourceLocation texture)
	{
		this.textures.put(type, texture);
	}
}
