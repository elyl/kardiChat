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
    private DatagramPacket		dp_in;
    private DatagramPacket		dp_out;
    private List<Toto>		iplist;
    private Map<InetAddress, Room>	m;
    Room	room;

    public Server() throws Exception
    {
	System.out.print("Initialisation du serveur....");
	ds = new DatagramSocket(9876);
	dp_in = new DatagramPacket(new byte[1024], 1024);
	dp_out = new DatagramPacket(new byte[0], 0);
	iplist = new LinkedList<Toto>();
	m = new HashMap<InetAddress, Room>();
	room = new Room("Mouvements extends Plateau", this);
	System.out.println("OK");
    }

    public void chat() throws Exception
    {
	while (true)
	    {
		System.out.print("En attente de reception....");
		ds.receive(dp_in);
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
		adr = dp_in.getAddress();
		if (!iplist.contains(new Toto(dp_in.getPort(), adr)))
		    log();
		sendAll(new String(dp_in.getData(), dp_in.getOffset(), dp_in.getLength()));
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

	itr = m.get(dp_in.getAddress()).getUserList().iterator();
	while (itr.hasNext())
	    {
		send(msg, itr.next());
	    }
    }

    public void send(String msg, User usr) throws Exception
    {
	System.out.print("Préparation de l'envoi....");
	dp_out.setData(msg.getBytes());
	dp_out.setLength(msg.length());
	dp_out.setPort(usr.getPort());
	dp_out.setAddress(usr.getIp());
	ds.send(dp_out);
	System.out.println("OK");
    }

    public void log()
    {
	User	usr;

	System.out.print("Enregistrement du nouvel utilisateur....");
	iplist.add(new Toto(dp_in.getPort(), dp_in.getAddress()));
	usr = new User(dp_in.getPort(), dp_in.getAddress(), dp_in.getAddress().toString(), room);
	m.put(dp_in.getAddress(), room);
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