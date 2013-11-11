import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.net.DatagramSocket;
import java.net.DatagramPacket;

public class Room
{
    private List<User>		ulist;
    private String		name;
    private Server		srv;
    private DatagramPacket	dp_out;
    private DatagramSocket	ds;

    public Room(String name, Server srv) throws Exception
    {
	this.name = name;
	this.srv = srv;
	this.ulist = new LinkedList<User>();
	this.dp_out = new DatagramPacket(new byte[0], 0);
	this.ds = new DatagramSocket();
    }

    public void sendAll(String msg) throws Exception
    {
	Iterator<User>	itr;

	itr = ulist.iterator();
	while (itr.hasNext())
	    send(itr.next(), msg);
    }

    public void send(User usr, String msg) throws Exception
    {
	System.out.println("Préparation de l'envoi...");
	dp_out.setData(msg.getBytes());
	dp_out.setLength(msg.length());
	dp_out.setPort(usr.getPort());
	dp_out.setAddress(usr.getAddress());
	ds.send(dp_out);
	System.out.println("Envoi termine");
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

    public String toString()
    {
	return (name);
    }
}