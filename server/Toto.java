import java.net.InetAddress;

public class Toto
{
    private int		port;
    private InetAddress	adr;

    public Toto(int port, InetAddress adr)
    {
	this.port = port;
	this.adr = adr;
    }

    public int getPort()
    {
	return (port);
    }

    public InetAddress getAdr()
    {
	return (adr);
    }

    public boolean equals(Object o)
    {
	Toto	t;

	System.out.println("prout");
	t = (Toto)o;
	return (port == t.port && adr.equals(t.adr));
    }
}