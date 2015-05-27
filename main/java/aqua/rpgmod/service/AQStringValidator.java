package aqua.rpgmod.service;

import java.util.regex.PatternSyntaxException;

public class AQStringValidator
{
	public static String[] validateUsername(String name)
	{
		if(name == null || name.isEmpty())
			return new String[]
			{
				"menu.emptyname"
			};
		
		if(name.length() < 2 || name.length() > 25)
			return new String[]
			{
				"menu.wrongnamelength", String.valueOf(2), String.valueOf(25)
			};
		
		try
		{
			if(!name.matches("^(\\w|\\-|\\_)+$"))
				return new String[]
				{
					"menu.wrongnamesymbols"
				};
		}
		catch(PatternSyntaxException e)
		{
			e.printStackTrace();
			return new String[]
			{
				"menu.developererror"
			};
		}
		
		return null;
	}
	
	public static Object[] getVargs(String[] validation)
	{
		Object[] vargs = new Object[validation.length - 1];
		
		for(int i = 1; i < validation.length; ++i)
			vargs[i - 1] = validation[i];
		
		return vargs;
	}
}
