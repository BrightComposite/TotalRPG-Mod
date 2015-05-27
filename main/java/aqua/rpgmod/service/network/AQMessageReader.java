package aqua.rpgmod.service.network;

public interface AQMessageReader<Msg extends AQMessage>
{
	public void read(Msg message);
}
