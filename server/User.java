import java.net.InetAddress;

public class User
{
    private final	int		port;
    private final	InetAddress	ip;
    private		String		pseudo;
    private		Room		r;

    public User(int port, InetAddress ip, String pseudo, Room r)
    {
	this.port = port;
	this.ip = ip;
	this.pseudo = pseudo;
	this.r = r;
    }

    public int getPort()
    {
	return (this.port);
    }

    public InetAddress getAddress()
    {
	return (this.ip);
    }

    public String getPseudo()
    {
	return (this.pseudo);
    }

    public Room getRoom()
    {
	return (this.r);
    }

    public void setRoom(Room r)
    {
	this.r = r;
    }

    public void setPseudo(String pseudo)
    {
	this.pseudo = pseudo;
    }
}