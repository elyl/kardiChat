import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

public class Server implements Runnable
{
    private DatagramSocket		ds;
    private DatagramPacket		dp;
    private List<InetAddress>		iplist;
    private Map<InetAddress, Room>	m;
    Room	room;

    public Server() throws Exception
    {
	System.out.print("Initialisation du serveur....");
	ds = new DatagramSocket(9876);
	dp = new DatagramPacket(new byte[1024], 1024);
	iplist = new LinkedList<InetAddress>();
	m = new HashMap<InetAddress, Room>();
	room = new Room("Mouvements extends Plateau", this);
	System.out.println("OK");
    }

    public void chat() throws Exception
    {
	while (true)
	    {
		System.out.print("En attente de reception....");
		ds.receive(dp);
		System.out.println("OK");
		new Thread(this).start();
	    }
    }

    public void run()
    {
	InetAddress	adr;

	try
	    {
		System.out.println("Traitement du paquet");
		adr = dp.getAddress();
		if (!iplist.contains(adr))
		    log();
		sendAll(new String(dp.getData(), dp.getOffset(), dp.getLength()));
		return;
	    }
	catch (Exception e)
	    {
		e.printStackTrace();
	    }
    }

    public void sendAll(String msg) throws Exception
    {
	Iterator<User>	itr;

	itr = m.get(dp.getAddress()).getUserList().iterator();
	while (itr.hasNext())
	    {
		send(msg, itr.next());
	    }
    }

    public void send(String msg, User usr) throws Exception
    {
	System.out.print("Préparation de l'envoie....");
	dp.setData(msg.getBytes());
	dp.setLength(msg.length());
	dp.setPort(usr.getPort());
	dp.setAddress(usr.getIp());
	ds.send(dp);
	System.out.println("OK");
    }

    public void log()
    {
	User	usr;

	System.out.print("Enregistrement du nouvel utilisateur....");
	iplist.add(dp.getAddress());
	usr = new User(dp.getPort(), dp.getAddress(), dp.getAddress().toString(), room);
	m.put(dp.getAddress(), room);
	room.addClient(usr);
	System.out.println("OK");
    }

    public static void main(String args[])
    {
	try
	    {
		new Server().chat();
	    }
	catch (Exception e)
	    {
		e.printStackTrace();
	    }
    }
}