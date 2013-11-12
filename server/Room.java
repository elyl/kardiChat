import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

public class Room
{
    private List<User>		ulist;
    private String		name;
    private Server		srv;

    public Room(String name, Server srv) throws Exception
    {
	this.name = name;
	this.srv = srv;
	this.ulist = new LinkedList<User>();
    }

    public void sendAll(String msg) throws Exception
    {
	Iterator<User>	itr;

	itr = ulist.iterator();
	while (itr.hasNext())
	   srv.send(itr.next(), msg);
    }

    public List<User> getUserList()
    {
	return (this.ulist);
    }

    public void addClient(User client) throws Exception
    {
	this.ulist.add(client);
	client.setRoom(this);
	sendAll(client.toString() + " joined the room");
    }

    public void delClient(User client) throws Exception
    {
	this.ulist.remove(client);
	client.setRoom(null);
	sendAll(client.toString() + " left the room");
    }

    public String toString()
    {
	return (name);
    }

    public boolean equels(Object o)
    {
	Room	r;

	r = (Room)o;
	return (r.name == name);
    }
}