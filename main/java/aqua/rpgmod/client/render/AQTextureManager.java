package aqua.rpgmod.client.render;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import aqua.rpgmod.AQModInfo;
import aqua.rpgmod.AquaMod;
import aqua.rpgmod.client.render.AQTextureMap.TextureType;
import aqua.rpgmod.player.AQPlayerWrapper;
import aqua.rpgmod.service.network.AQWebSettings;

@SideOnly(Side.CLIENT)
public class AQTextureManager
{
	public static ResourceLocation load(TextureType texType, String playerId)
	{
		ResourceLocation resourcelocation = new ResourceLocation(AQModInfo.MODID + ":" + playerId + "/" + texType);
		ResourceLocation def = new ResourceLocation(AQModInfo.MODID + ":" + "textures/player/" + texType + ".png");
		
		URL url = null;
		
		try
		{
			url = new URL(AQWebSettings.domain + "profiles/" + playerId + "/" + texType + ".png");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return def;
		}
		
		ThreadDownloadImageData threaddownloadimagedata = new ThreadDownloadImageData(null, url.toString(), def, new IImageBuffer()
		{
			@Override
			public BufferedImage parseUserSkin(BufferedImage p_78432_1_)
			{
				return p_78432_1_;
			}
			
			@Override
			public void func_152634_a()
			{	
				
			}
		});
		
		Minecraft.getMinecraft().renderEngine.loadTexture(resourcelocation, threaddownloadimagedata);
		
		return resourcelocation;
	}
	
	public static void setLocal(File image, TextureType type, AQTextureMap textures, String playerId)
	{
		ResourceLocation texture = new ResourceLocation(AQModInfo.MODID + ":" + playerId + "/" + type);
		ResourceLocation def = new ResourceLocation(AQModInfo.MODID + ":" + "textures/player/" + type + ".png");
		
		ThreadDownloadImageData threaddownloadimagedata = new ThreadDownloadImageData(image, image.toString(), def, new IImageBuffer()
		{
			@Override
			public BufferedImage parseUserSkin(BufferedImage p_78432_1_)
			{
				return p_78432_1_;
			}
			
			@Override
			public void func_152634_a()
			{	
				
			}
		});
		
		Minecraft.getMinecraft().renderEngine.loadTexture(texture, threaddownloadimagedata);
		textures.set(type, texture);
	}
	
	public static void bindTexture(TextureType type, AQTextureMap textures)
	{
		ResourceLocation texture = textures.get(type);
		
		if(texture == null)
		{
			if(textures.userName == null)
				texture = new ResourceLocation(AQModInfo.MODID + ":" + "textures/player/" + type + ".png");
			else
				texture = load(type, textures.userName);
			
			textures.set(type, texture);
		}
		
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
	}
	
	public static void bindTexture(EntityPlayer player, TextureType type, AQTextureMap textures)
	{
		if(player == null)
		{
			bindTexture(type, textures);
			return;
		}
		
		AQPlayerWrapper<EntityPlayer> wrapper = AquaMod.proxy.getWrapper(player);
		
		ResourceLocation tex = wrapper.textures.get(type);
		
		if(tex == null)
		{
			tex = load(type, wrapper.getPlayerName());
			wrapper.textures.set(type, tex);
		}
		
		Minecraft.getMinecraft().renderEngine.bindTexture(tex);
	}
	
	public static void bindTexture(ResourceLocation texture)
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
	}
}
