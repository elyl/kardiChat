import java.net.MulticastSocket;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Scanner;

public class Client
{
    private DatagramSocket	ds;
    private DatagramPacket	dp;
    private MulticastSocket	multi;
    private String		pseudo;

    public Client() throws Exception
    {
	this("127.0.0.1", 9876, "224.0.0.1", 9877);
    }

    public Client(String serverAddress, int port, String multicastAddress, int multicastPort) throws Exception
    {
	ds = new DatagramSocket();
	dp = new DatagramPacket(new byte[0], 0);
	dp.setAddress(InetAddress.getByName(serverAddress));
	dp.setPort(port);
	multi = new MulticastSocket();
	multi.joinGroup(InetAddress.getByName(multicastAddress));
    }

    public void login() throws Exception
    {
	boolean	bool;
	String tmp;

	bool = true;
	while (bool)
	    {
		System.out.println("Entrez votre pseudo et mot de passe:");
		send(readString());
		tmp = receive(ds);
		if (tmp.equals("OK"))
		    bool = false;
	    }
    }

    public void send(String s) throws Exception
    {
	dp.setData(s.getBytes());
	dp.setLength((s.getBytes().length < 1024) ? s.getBytes().length : 1024);
	System.out.print("Envoi en cours....");
	ds.send(dp);
	System.out.println("OK");
    }

    public String receive(DatagramSocket s) throws Exception
    {
	dp.setData(new byte[1024]);
	dp.setLength(1024);
	System.out.print("En attente de reception...");
	s.receive(dp);
	System.out.println("OK");
	return (new String(dp.getData(), dp.getOffset(), dp.getLength()));
    }

    public void chat() throws Exception
    {
	while (true)
	    {
		send(pseudo + readString());
		System.out.println(receive(multi));
	    }
    }

    public String readString() throws Exception
    {
	Scanner	sc;

	sc = new Scanner(System.in);
	return (sc.nextLine());
    }

    public static void main(String args[]) throws Exception
    {
	Client	c;

	c = new Client();
	c.login();
	c.chat();
    }
}