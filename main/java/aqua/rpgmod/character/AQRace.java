package aqua.rpgmod.character;

import java.util.HashMap;

import aqua.rpgmod.player.AQPlayerWrapper;
import aqua.rpgmod.player.AQThisPlayerWrapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class AQRace
{
	public static final HashMap<String, AQRace> map = new HashMap<String, AQRace>();
	public static final AQRace defaultRace = Dwarf.instance();
	public final String name;
	
	static
	{
		addRace(Human.instance());
		addRace(Elf.instance());
		addRace(Dwarf.instance());
		addRace(Goblin.instance());
		addRace(Octoid.instance());
		addRace(Arachnoid.instance());
		addRace(Centaurus.instance());
		addRace(Fairy.instance());
	}
	
	public static void addRace(AQRace race)
	{
		map.put(race.name, race);
	}
	
	public AQRace(String name)
	{
		this.name = name;
	}
	
	@Override
	public int hashCode()
	{
		return this.name.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof AQRace && this.name.equals(((AQRace) obj).name);
	}
	
	@Override
	public String toString()
	{
		return this.name;
	}
	
	@SuppressWarnings("static-method")
	public boolean canClimpUpTheWall()
	{
		return false;
	}

	@SuppressWarnings("static-method")
	public boolean canMountEntity()
	{
		return true;
	}
	
	@SuppressWarnings("static-method")
	public boolean slowFalling()
	{
		return false;
	}
	
	@SuppressWarnings("static-method")
	public float width()
	{
		return 0.6f;
	}
	
	@SuppressWarnings("static-method")
	public float height()
	{
		return 1.8f;
	}
	
	@SuppressWarnings("static-method")
	@SideOnly(Side.CLIENT)
	public int disabledArmor()
	{
		return 0;
	}
	
	@SuppressWarnings("static-method")
	@SideOnly(Side.CLIENT)
	public boolean canBreatheUnderwater()
	{
		return false;
	}
	
	@SuppressWarnings("static-method")
	@SideOnly(Side.CLIENT)
	public boolean canSwim()
	{
		return true;
	}

	@SuppressWarnings("static-method")
	@SideOnly(Side.CLIENT)
	public boolean canSwimLikeHuman()
	{
		return true;
	}
	
	@SuppressWarnings("unused")
	@SideOnly(Side.CLIENT)
	public void setModifiers(AQThisPlayerWrapper wrapper)
	{}
	
	public static class Human extends AQRace
	{
		static AQRace __instance = null;
		
		public static AQRace instance()
		{
			if(__instance == null)
				__instance = new Human();
			
			return __instance;
		}
		
		public Human()
		{
			super("human");
		}
	}
	
	public static class Elf extends AQRace
	{
		static AQRace __instance = null;
		
		public static AQRace instance()
		{
			if(__instance == null)
				__instance = new Elf();
			
			return __instance;
		}
		
		public Elf()
		{
			super("elf");
		}
		
		@Override
		public float width()
		{
			return 0.5f;
		}
		
		@Override
		public float height()
		{
			return 2.0f;
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public void setModifiers(AQThisPlayerWrapper wrapper)
		{
			wrapper.Y_OFFSET.setDefault(-0.2f);
		}
	}
	
	public static class Dwarf extends AQRace
	{
		static AQRace __instance = null;
		
		public static AQRace instance()
		{
			if(__instance == null)
				__instance = new Dwarf();
			
			return __instance;
		}
		
		public Dwarf()
		{
			super("dwarf");
		}
		
		@Override
		public float width()
		{
			return 0.8f;
		}
		
		@Override
		public float height()
		{
			return 1.54f;
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public void setModifiers(AQThisPlayerWrapper wrapper)
		{
			wrapper.SPEED.setDefault(0.75f);
			wrapper.Y_OFFSET.setDefault(0.36f);
		}
	}
	
	public static class Goblin extends AQRace
	{
		static AQRace __instance = null;
		
		public static AQRace instance()
		{
			if(__instance == null)
				__instance = new Goblin();
			
			return __instance;
		}
		
		public Goblin()
		{
			super("goblin");
		}
	}
	
	public static class Octoid extends AQRace
	{
		static AQRace __instance = null;
		
		public static AQRace instance()
		{
			if(__instance == null)
				__instance = new Octoid();
			
			return __instance;
		}
		
		public Octoid()
		{
			super("octoid");
		}

		@Override
		public boolean canMountEntity()
		{
			return false;
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public int disabledArmor()
		{
			return AQPlayerWrapper.disableLegs;
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public boolean canBreatheUnderwater()
		{
			return true;
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public void setModifiers(AQThisPlayerWrapper wrapper)
		{
			wrapper.WATER_SPEED.setDefault(1.0f);
		}
	}
	
	public static class Arachnoid extends AQRace
	{
		static AQRace __instance = null;
		
		public static AQRace instance()
		{
			if(__instance == null)
				__instance = new Arachnoid();
			
			return __instance;
		}
		
		public Arachnoid()
		{
			super("arachnoid");
		}

		@Override
		public boolean canMountEntity()
		{
			return false;
		}
		
		@Override
		public float width()
		{
			return 1.5f;
		}
		
		@Override
		public boolean canClimpUpTheWall()
		{
			return true;
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public int disabledArmor()
		{
			return AQPlayerWrapper.disableLegs;
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public boolean canSwim()
		{
			return false;
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public void setModifiers(AQThisPlayerWrapper wrapper)
		{
			wrapper.SPEED.setDefault(0.9f);
		}
	}
	
	public static class Centaurus extends AQRace
	{
		static AQRace __instance = null;
		
		public static AQRace instance()
		{
			if(__instance == null)
				__instance = new Centaurus();
			
			return __instance;
		}
		
		public Centaurus()
		{
			super("centaurus");
		}

		@Override
		public boolean canMountEntity()
		{
			return false;
		}
		
		@Override
		public float height()
		{
			return 2.4f;
		}

		@Override
		@SideOnly(Side.CLIENT)
		public int disabledArmor()
		{
			return AQPlayerWrapper.disableLegs;
		}
		
		@SideOnly(Side.CLIENT)
		public boolean canSwimLikeHuman()
		{
			return false;
		}

		@Override
		@SideOnly(Side.CLIENT)
		public void setModifiers(AQThisPlayerWrapper wrapper)
		{
			wrapper.SPEED.setDefault(1.25f);
			wrapper.STEP_HEIGHT.setDefault(1.0f);
			wrapper.Y_OFFSET.setDefault(-0.4f);
		}
	}
	
	public static class Fairy extends AQRace
	{
		static AQRace __instance = null;
		
		public static AQRace instance()
		{
			if(__instance == null)
				__instance = new Fairy();
			
			return __instance;
		}
		
		public Fairy()
		{
			super("fairy");
		}
		
		@Override
		public boolean slowFalling()
		{
			return true;
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public int disabledArmor()
		{
			return AQPlayerWrapper.disableAll & ~AQPlayerWrapper.disableHelmet;
		}
	}
}
