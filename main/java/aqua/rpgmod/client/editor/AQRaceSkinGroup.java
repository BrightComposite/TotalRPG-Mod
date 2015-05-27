package aqua.rpgmod.client.editor;

import net.minecraft.client.resources.I18n;
import aqua.rpgmod.character.AQRace;
import aqua.rpgmod.service.AQLogger;
import aqua.rpgmod.service.resources.AQFileResource;

public class AQRaceSkinGroup
{
	public AQRace race;
	public AQFileResource resource;
	
	public SSTree tree;
	public SSLayers layers;
	public SSPages pages;
	
	public AQRaceSkinGroup(AQRace race) throws Exception
	{
		this.race = race;
		this.resource = new AQFileResource("skins", race.name);
		
		AQLogger.log(I18n.format("skins.readracegroup", I18n.format("race." + race.name)));
		
		this.tree = new SSTree(treeResource());
		this.layers = new SSLayers(layersResource());
		this.pages = new SSPages(pagesResource());
	}
	
	public AQFileResource treeResource()
	{
		return this.resource.append("trees");
	}
	
	public AQFileResource layersResource()
	{
		return this.resource.append("layers");
	}
	
	public AQFileResource pagesResource()
	{
		return this.resource.append("pages");
	}
	
}
