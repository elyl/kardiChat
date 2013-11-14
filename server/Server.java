import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.File;

public class Server implements Runnable
{
    private DatagramSocket		ds;
    private DatagramPacket		dp_in;
    private Map<String, User>		m;
    private List<Room>			roomList;
    Room	room;

    public Server() throws Exception
    {
	System.out.print("Initialisation du serveur....");
	ds = new DatagramSocket(9876);
	dp_in = new DatagramPacket(new byte[1024], 1024);
	m = new HashMap<String, User>();
	roomList = new LinkedList<Room>();
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
		//new Thread(this).start();
		run();
	    }
    }

    public void run()
    {
	InetAddress	adr;
	String		uid;
	String		msg;
	User		usr;

	try
	    {
		System.out.println("Traitement du paquet");
		adr = dp_in.getAddress();
		System.out.println("Etape -1");
		uid = new String(adr.toString() + ":" + dp_in.getPort());
		System.out.println("Etape 0");
		msg = new String(dp_in.getData(), dp_in.getOffset(), dp_in.getLength());
		System.out.println("Etape 1 : " + msg);
		if (msg.length() == 0)
		    return;
		if (!m.containsKey(uid))
		    log();
		usr = m.get(uid);
		if (msg.charAt(0) == '/')
		    command(msg, usr);
		else
		    usr.getRoom().sendAll(usr.getPseudo() + ": " + new String(dp_in.getData(), dp_in.getOffset(), dp_in.getLength()));
		System.out.println("Traitement termine");
		return;
	    }
	catch (Exception e)
	    {
		e.printStackTrace();
	    }
    }

    public void log() throws Exception
    {
	User	usr;

	System.out.println("Enregistrement du nouvel utilisateur....");
	usr = new User(dp_in.getPort(), dp_in.getAddress(), dp_in.getAddress().toString(), room);
	m.put(new String(dp_in.getAddress().toString() + ":" + dp_in.getPort()), usr);
	System.out.println(m);
	room.addClient(usr);
	System.out.println("Enregistrement termine");
    }

    public void command(String msg, User usr) throws Exception
    {
	if (msg.length() > 6 && msg.substring(0, 5).equals("/join"))
	    {
		System.out.println(msg.substring(6));
		if (roomList.contains(new Room(msg.substring(6), null)))
		    {
			usr.getRoom().delClient(usr);
			roomList.get(roomList.indexOf(new Room(msg.substring(6), null))).addClient(usr);
		    }
		else
		    send(usr, "This room does not seem to exist. Type /roomlist for a complete list of the different existing rooms");
	    }
	else if (msg.length() > 7 && msg.substring(0, 5).equals("/nick"))
	    {
		usr.setPseudo(msg.substring(6));
		send(usr, "Nickname changed");
	    }
	else if (msg.length() > 8 && msg.substring(0, 7).equals("/create"))
	    {
		if (roomList.indexOf(new Room(msg.substring(8), null)) == -1)
		    {
			roomList.add(new Room(msg.substring(8), this));
			send(usr, "Room " + msg.substring(8) + " created");
		    }
		else
		    send(usr, "This room already exists");
	    }
	else if (msg.equals("/list"))
	    send(usr, usr.getRoom().getUserList().toString());
	else if (msg.equals("/roomlist"))
	    send(usr, roomList.toString());
	else if (msg.equals("/poney"))
	    poney(usr);
	else if (msg.equals("/help"))
	    {
		send(usr,	"Alviable commands:\n" +
				"/nick <name> : Change your nickname to <name>\n" +
				"/join <romm name> : join the specified room \n" +
				"/roomlist : display the list of existing rooms\n" +
				"/list : display all users curently logged in your room\n" +
				"/create <room name> : create and join the specified room\n" +
				"/leave : Leave the currend room\n" +
				"/ban <name> : ban <name> from the chat\n" +
				"/poney : display a wonderful poney\n" +
				"/help : display this list of commands");
	    }
	else
	    send(usr, "Command not found, type /help for a list of valid commands");
    }

    public void poney(User usr) throws Exception
    {
	String		line;
	BufferedReader	r;

	r = new BufferedReader(new FileReader(new File("rainbow dash.txt")));
	line = r.readLine();
	while (line != null)
	    {
		line = r.readLine();
		send(usr, line);
	    }
    }

    public void send(User usr, String msg) throws Exception
    {
	DatagramPacket	dp_out;

	dp_out = new DatagramPacket(new byte[0], 0);
	System.out.println("Preparation de l'envoi...");
	dp_out.setData(msg.getBytes());
	dp_out.setLength(msg.length());
	dp_out.setAddress(usr.getAddress());
	dp_out.setPort(usr.getPort());
	ds.send(dp_out);
	System.out.println("Envoi termine");
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