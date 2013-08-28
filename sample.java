import jpcap.JpcapCaptor;
import jpcap.JpcapSender;
import jpcap.NetworkInterfaceAddress;
import jpcap.NetworkInterface;
import jpcap.PacketReceiver;
import jpcap.packet.Packet;
import jpcap.packet.ARPPacket;
import jpcap.packet.ICMPPacket;
import java.net.InetAddress;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;

public class sample {

    public static void main(String args[]) throws IOException {

	// for capturing
	int index = 2;
	NetworkInterface[] devices = JpcapCaptor.getDeviceList();
	JpcapCaptor captor = JpcapCaptor.openDevice(devices[index], 4096, false, 5000);
	Packet pack = captor.getPacket();

	// for sending
	byte[] senderaddr = new byte[4];
	ICMPPacket icmp = new ICMPPacket();
	JpcapSender sender = captor.getJpcapSenderInstance();

	try {
	    System.out.println("try catch block");
	    capture(captor, pack, true, args[0]);
	} catch (IOException e) {
	    System.out.println("IOException");
	    System.err.println(e);
	}
    }

    public static void getSenderTarget(JpcapCaptor captor) throws IOException {

	captor.setFilter("arp", true);
	ARPPacket arppack = (ARPPacket)captor.getPacket();
	System.out.println("Sender IP Address:\t" + arppack.getSenderProtocolAddress());
	System.out.println("Sender HW Address:\t" + arppack.getSenderHardwareAddress());
	System.out.println("Target IP Address:\t" + arppack.getTargetProtocolAddress());
	System.out.println("Target HW Address:\t" + arppack.getTargetHardwareAddress());

    }

    public static void capture(JpcapCaptor captor, Packet pack, boolean write, String file) throws IOException {
	pack = captor.getPacket();
	if (pack != null) {
	    if (write == true) {
		FileWriter dumpwrite = new FileWriter(file);
		char[] c = new char[pack.data.length];
		String packdata = new String(pack.data, "UTF-8");
		String packheader = new String(pack.header, "UTF-8");
		System.out.println(packdata);
		System.out.println(packheader);
		dumpwrite.write(packheader, 0, packheader.length());
		dumpwrite.write("\n\n", 0, "\n\n".length());
		dumpwrite.write(packdata, 0, packdata.length());
		dumpwrite.write("\n", 0, "\n".length());
		dumpwrite.close();
	    }
	}
    }

    public static void getdevs(NetworkInterface[] devices) throws IOException {
	//for each network interface
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
