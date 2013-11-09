import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Scanner;

public class Client implements Runnable
{
    private DatagramSocket	ds;
    private DatagramPacket	dp;
    private String		pseudo;

    public Client() throws Exception
    {
	this("127.0.0.1", 9876);
    }

    public Client(String serverAddress, int port) throws Exception
    {
	ds = new DatagramSocket();
	dp = new DatagramPacket(new byte[0], 0);
	dp.setAddress(InetAddress.getByName(serverAddress));
	dp.setPort(port);
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
	ds.receive(dp);
	System.out.println("OK");
	return (new String(dp.getData(), dp.getOffset(), dp.getLength()));
    }

    public void run()
    {
	try
	    {
		while (true)
		    send(readString());
	    }
	catch (Exception e)
	    {
		e.printStackTrace();
	    }
    }

    public void chat() throws Exception
    {
	new Thread(this).start();
	while (true)
	    {
		System.out.println(receive(ds));
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
	c.chat();
    }
}