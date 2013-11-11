import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Map;
import java.util.HashMap;

public class Server implements Runnable
{
    private DatagramSocket		ds;
    private DatagramPacket		dp_in;
    private Map<String, User>		m;
    Room	room;

    public Server() throws Exception
    {
	System.out.print("Initialisation du serveur....");
	ds = new DatagramSocket(9876);
	dp_in = new DatagramPacket(new byte[1024], 1024);
	m = new HashMap<String, User>();
	room = new Room("Mouvements extends Plateau", this);
	System.out.println("OK");
    }

    public void chat() throws Exception
    {
	while (true)
	    {
		System.out.println("En attente de reception....");
		ds.receive(dp_in);
		System.out.println("Paquet reçu");
		new Thread(this).start();
	    }
    }

    public void run()
    {
	InetAddress	adr;
	String		usr;

	try
	    {
		System.out.println("Traitement du paquet");
		adr = dp_in.getAddress();
		usr = new String(adr.toString() + ":" + dp_in.getPort());
		if (!m.containsKey(usr))
		    log();
		m.get(usr).getRoom().sendAll(new String(dp_in.getData(), dp_in.getOffset(), dp_in.getLength()));
		System.out.println("Traitement termine");
		return;
	    }
	catch (Exception e)
	    {
		e.printStackTrace();
	    }
    }

    public void log()
    {
	User	usr;

	System.out.println("Enregistrement du nouvel utilisateur....");
	usr = new User(dp_in.getPort(), dp_in.getAddress(), dp_in.getAddress().toString(), room);
	m.put(new String(dp_in.getAddress().toString() + ":" + dp_in.getPort()), usr);
	System.out.println(m);
	room.addClient(usr);
	System.out.println("Enregistrement termine");
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