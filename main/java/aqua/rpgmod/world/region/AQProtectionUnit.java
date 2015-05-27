package aqua.rpgmod.world.region;

public interface AQProtectionUnit
{
	public static enum Flag
	{
		BLOCK,
		PLACE,
		PLAYERS,
		CHEST,
		USE;

		public static String[] getNames(Flag[] flags)
		{
			String[] list = new String[flags.length];
			
			for(int i = 0; i < flags.length; ++i)
				list[i] = flags[i].toString();
			
			return list;
		}
		
		public static Flag get(String name)
		{
			for(Flag flag : Flag.values())
			{
				if(name.compareToIgnoreCase(flag.toString()) == 0)
					return flag;
			}
			
			return null;
		}
		
		@Override
		public String toString()
		{
			return super.toString().toLowerCase();
		}
	}

	public static enum FlagAction
	{
		ADD,
		SET,
		CLEAR,
		LIST;
		
		public static FlagAction get(String name)
		{
			for(FlagAction action : FlagAction.values())
			{
				if(name.compareToIgnoreCase(action.toString()) == 0)
					return action;
			}
			
			return null;
		}
	}
	
	public void addFlags(Flag ... flags);
	
	public void setFlags(Flag ... flags);
	
	public void clearFlags(Flag ... flags);
	
	public boolean checkFlags(Flag ... flags);
	
	public Flag[] getFlags();
}
