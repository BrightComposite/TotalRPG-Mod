package aqua.rpgmod.client.model;

import aqua.rpgmod.character.AQRace.Human;

public class AQModelHuman extends AQModelPlayer
{
	public AQModelHuman()
	{
		super("Human", Human.instance());
	}
}
