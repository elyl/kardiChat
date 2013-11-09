import java.util.List;
import java.util.LinkedList;

public class Room
{
    private List<User>	ulist;
    private String	name;
    private Server	srv;

    public Room(String name, Server srv)
    {
	this.name = name;
	this.srv = srv;
	this.ulist = new LinkedList<User>();
    }

    public List<User> getUserList()
    {
	return (this.ulist);
    }

    public void addClient(User client)
    {
	this.ulist.add(client);
	client.setRoom(this);
    }

    public void delClient(User client)
    {
	this.ulist.remove(client);
	client.setRoom(null);
    }
}