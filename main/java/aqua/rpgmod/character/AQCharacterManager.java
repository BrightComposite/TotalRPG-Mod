package aqua.rpgmod.character;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.util.Session;
import aqua.common.web.WebTarget;
import aqua.rpgmod.AquaMod;
import aqua.rpgmod.client.gui.editor.AQGuiCharacter;
import aqua.rpgmod.client.model.AQModelManager;
import aqua.rpgmod.client.model.AQModelPlayer;
import aqua.rpgmod.client.render.AQTextureMap;
import aqua.rpgmod.service.AQLogger;
import aqua.rpgmod.service.network.AQWebSettings;

public class AQCharacterManager
{
	public final HashMap<AQRace, AQModelPlayer> models = AQModelManager.createMap();
	public final String username = AquaMod.inDevelopment() ? "Nothing" : Minecraft.getMinecraft().getSession().getUsername();
	public final String token = AquaMod.inDevelopment() ? "01234567890abcdef01234567890abcdef" : Minecraft.getMinecraft().getSession().getToken();
	
	public String name = this.username;
	public String uuid = null;
	public AQRace race = null;
	
	public boolean hasCharacter = false;
	
	public final AQTextureMap textures = new AQTextureMap(this.username);
	public final ModelBiped rootModel = new ModelBiped();
	public AQModelPlayer currentModel = null;
	
	public final static AQCharacterManager instance = new AQCharacterManager();
	
	AQGuiCharacter currentGui = null;
	
	public AQCharacterManager()
	{	
		
	}
	
	public void patchSession()
	{
		try
		{
			AQLogger.log("Patching session...");
			Session session = Minecraft.getMinecraft().getSession();
			
			Field username = Session.class.getDeclaredField(AquaMod.inDevelopment() ? "username" : "field_74286_b");
			Field playerID = Session.class.getDeclaredField(AquaMod.inDevelopment() ? "playerID" : "field_148257_b");
			
			username.set(session, this.name);
			playerID.set(session, this.uuid);

			if(AquaMod.inDevelopment())
			{
				Field token = Session.class.getDeclaredField("token");
				token.set(session, this.token);
			}
			
			AQLogger.log("Ok!");
		}
		catch(NoSuchFieldException e)
		{
			AQLogger.log("Couldn't patch session!");
			e.printStackTrace();
		}
		catch(SecurityException e)
		{
			e.printStackTrace();
		}
		catch(IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch(IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}
	
	public void useGui(AQGuiCharacter gui)
	{
		if(gui != null)
			this.currentGui = gui;
	}
	
	public void closeGui(AQGuiCharacter gui)
	{
		if(this.currentGui == gui)
			this.currentGui = null;
	}
	
	public AQGuiCharacter getCurrentGui()
	{
		return this.currentGui;
	}
	
	public static enum WebResult
	{
		TRUE(true),
		FALSE(false),
		ERROR(false);
		
		public final boolean value;
		
		WebResult(boolean value)
		{
			this.value = value;
		}
	}
	
	protected void setCharacter(String name, AQRace race, String uuid)
	{
		this.name = name;
		this.uuid = uuid;
		setRace(race);
		
		patchSession();
	}
	
	protected void setRace(AQRace race)
	{
		this.race = race;
		this.currentModel = this.models.get(this.race);
	}
	
	public WebResult loadCharacter()
	{
		WebResult result;
		String response = "";
		
		try
		{
			WebTarget target = new WebTarget(new URL(AQWebSettings.domain + "char.php"));
			
			target.post(new Object[]
			{
				"username", this.username, "token", this.token, "action", "getcurrent"
			});
			
			response = target.getResponse();
			
			if(response == null)
				return WebResult.ERROR;
			
			if(response.contains("ok"))
				result = WebResult.TRUE;
			else
			{
				if(response.contains("unknown action"))
					AQLogger.log("Unknown action!");
				
				result = WebResult.ERROR;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return WebResult.ERROR;
		}
		
		if(result == WebResult.TRUE)
		{
			String[] parts = response.split(",");
			
			if(parts.length < 4)
			{
				return WebResult.ERROR;
			}
			
			String name = parts[1];
			
			AQRace race = AQRace.map.get(parts[2]);
			
			if(race == null)
				race = AQRace.defaultRace;
			
			String uuid = parts[3];
			
			StringBuilder sb = new StringBuilder("");
			
			for(int i = 0; i < 32; i++)
			{
				if(i == 8 || i == 12 || i == 16 || i == 20)
					sb.append("-");
				
				sb.append(uuid.charAt(i));
			}
			
			uuid = sb.toString();
			
			setCharacter(name, race, uuid);
		}
		
		return result;
	}
	
	public String createCharacter(String name, AQRace race)
	{
		String response;
		
		try
		{
			WebTarget target = new WebTarget(new URL(AQWebSettings.domain + "char.php"));
			
			target.post(new Object[]
			{
				"username", this.username, "token", this.token, "action", "create", "name", name, "race", race.name
			});
			
			response = target.getResponse();
			
			if(response == null)
				response = "error";
			else if(response.contains("unknown action"))
			{
				AQLogger.log("Unknown action!");
				response = "error";
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "error";
		}
		
		return response;
	}
	
	public String changeName(String name)
	{
		String response;
		
		try
		{
			WebTarget target = new WebTarget(new URL(AQWebSettings.domain + "char.php"));
			
			target.post(new Object[]
			{
				"username", this.username, "token", this.token, "action", "changename", "name", name,
			});
			
			response = target.getResponse();
			
			if(response == null)
				response = "error";
			else if(response.contains("unknown action"))
			{
				AQLogger.log("Unknown action!");
				response = "error";
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "error";
		}
		
		if(response.equals("ok"))
			this.name = name;
		
		return response;
	}
	
	public WebResult changeRace(AQRace race)
	{
		WebResult result;
		
		try
		{
			WebTarget target = new WebTarget(new URL(AQWebSettings.domain + "char.php"));
			
			target.post(new Object[]
			{
				"username", this.username, "token", this.token, "action", "changerace", "race", race,
			});
			
			String response = target.getResponse();
			
			if(response == null)
				result = WebResult.ERROR;
			else if(response.equals("true"))
				result = WebResult.TRUE;
			else if(response.equals("false"))
				result = WebResult.FALSE;
			else
			{
				if(response.contains("unknown action"))
					AQLogger.log("Unknown action!");
				
				result = WebResult.ERROR;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return WebResult.ERROR;
		}
		
		if(result.value)
			setRace(race);
		
		return result;
	}
	
	public WebResult checkCharacter()
	{
		WebResult result;
		
		try
		{
			WebTarget target = new WebTarget(new URL(AQWebSettings.domain + "char.php"));
			
			target.post(new Object[]
			{
				"username", this.username, "token", this.token, "action", "haschar"
			});
			
			String response = target.getResponse();
			
			if(response == null)
				result = WebResult.ERROR;
			else if(response.equals("true"))
				result = WebResult.TRUE;
			else if(response.equals("false"))
				result = WebResult.FALSE;
			else
			{
				if(response.contains("unknown action"))
					AQLogger.log("Unknown action!");
				
				result = WebResult.ERROR;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return WebResult.ERROR;
		}
		
		this.hasCharacter = result.value;
		
		if(this.hasCharacter)
			result = loadCharacter();
		
		return result;
	}
}
