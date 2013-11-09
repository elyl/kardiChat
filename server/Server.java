import java.net.MulticastSocket;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class Server
{
    private DatagramSocket	ds;
    private DatagramPacket	dp;
    private MulticastSocket	s;

    public Server() throws Exception
    {
	ds = new DatagramSocket(9876);
	dp = new DatagramPacket(new byte[1024], 1024);
	s = new MulticastSocket();
	s.joinGroup(InetAddress.getByName("224.0.0.1"));
    }

    public void chat() throws Exception
    {
	String	str;

	while (true)
	    {
		ds.receive(dp);
		str = new String(dp.getData(), dp.getOffset(), dp.getLength());
		if (str.equals("toto 42"))
		    {
			ds.send(new DatagramPacket(new String("OK").getBytes(), 2, dp.getAddress(), dp.getPort()));
			sleep(2);
			s.send(new DatagramPacket(new String("Bienvenue").getBytes(), 9, dp.getAddress(), dp.getPort()));
		    }
		else
		    ds.send(dp);
	    }
		
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

private class Room implements Runnable
{
    public Room()
    {
	
    }

    public void run()
    {

    }
}