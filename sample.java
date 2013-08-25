import jpcap.NetworkInterfaceAddress;
import jpcap.NetworkInterface;
import jpcap.JpcapCaptor;
import jpcap.packet.Packet;
import jpcap.packet.ARPPacket;
import jpcap.PacketReceiver;
import java.io.IOException;
import jpcap.JpcapSender;
import jpcap.packet.ICMPPacket;
import java.net.InetAddress;

public class sample {

    public static void sample(String args[]) {

	boolean noerr = true;
	// getdevs();
	while (noerr) {
	    try {
		int index = 2;
		NetworkInterface[] devices = JpcapCaptor.getDeviceList();
		JpcapCaptor captor = JpcapCaptor.openDevice(devices[index], 4096, false, 5000);
		capture(captor);
		tracert(captor); // doesnt work
		
	    } catch (IOException e) {
		System.out.println("IOException");
		System.err.println(e);
		noerr = false;
	    }
	}
    }

    // Unfinished, create inetaddress and use in captor.setfilter
    public static void tracert(JpcapCaptor captor) {
	InetAddress iaddr = 
	captor.setFilter("icmp and dst" + );
	JpcapSender sender = captor.getJpcapSenderInstance();
	ICMPPacket icmp = new ICMPPacket();
	boolean doing = true;
	
	while (doing) {
	    ICMPPacket icmppack = captor.getPacket();
	    System.out.println("received " + icmppack);
	    if (icmppack == null) {
		System.out.println("Timeout");
	    } else if (icmppack.type == ICMPPacket.ICMP_TIMXCEED){
		icmppack.src_ip.getHostName();
		System.out.println(icmp.hop_limit + ": " + icmppack.src_ip);
		icmp.hop_limit++;
	    } else if (icmppack.type == ICMPPacket.ICMP_UNREACH){
		icmppack.src_ip.getHostName();
		System.out.println(icmp.hop_limit+": "+ icmppack.src_ip);
		System.exit(0);
	    } else if (icmppack.type == ICMPPacket.ICMP_ECHOREPLY){
		icmppack.src_ip.getHostName();
		System.out.println(icmp.hop_limit + ": " + icmppack.src_ip);
		System.exit(0);
	    }
	    sender.sendPacket(icmp);
	    System.out.println();
	    doing = false;
	}
    }

    public static void capture(JpcapCaptor captor) throws IOException {

	/*
	captor.setFilter("arp", true);
	System.out.println("Sender IP Address:\t" + arppack.getSenderProtocolAddress());
	System.out.println("Sender HW Address:\t" + arppack.getSenderHardwareAddress());
	System.out.println("Target IP Address:\t" + arppack.getTargetProtocolAddress());
	System.out.println("Target HW Address:\t" + arppack.getTargetHardwareAddress());
	*/

	boolean doing = true;
	while (doing) {
	    int index = 2;
	    NetworkInterface[] devices = JpcapCaptor.getDeviceList();
	    Packet pack = captor.getPacket();
	    
	    if (pack != null) {

		System.out.println(pack);
		System.out.println();
		doing = false;
	    }
	}
    }

    //Get Interfaces
    public static void getdevs() throws IOException {
	//for each network interface
	int index = 2;
	NetworkInterface[] devices = JpcapCaptor.getDeviceList();
	JpcapCaptor captor = JpcapCaptor.openDevice(devices[index], 4096, false, 5000);
	for (int i = 0; i < devices.length; i++) {
	    //print out its name and description
	    System.out.println(i + ": " + devices[i].name + "(" + devices[i].description + ")");

	    //print out its datalink name and description
	    System.out.println(" datalink: " + devices[i].datalink_name + "(" + devices[i].datalink_description + ")");

	    //print out its MAC address
	    System.out.print(" MAC address:");
	    for (byte b : devices[i].mac_address) {
		System.out.print(Integer.toHexString(b&0xff) + ":");
	    }
	    System.out.println();

	    //print out its IP address, subnet mask and broadcast address
	    for (NetworkInterfaceAddress a : devices[i].addresses) {
		System.out.println(" address: " + a.address + " " + a.subnet + " " + a.broadcast);
	    }
	    System.out.println();
	}
    }
}
