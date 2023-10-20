/************************************************
*
* Author: Zhengyan Hu
* Assignment: FOOP Server
* Class: CSI4321
*
************************************************/

package foop.app.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;

import java.net.InetSocketAddress;
import java.net.MulticastSocket;

import java.util.Arrays;

import foop.serialization.Addition;
import foop.serialization.Message;
import foop.serialization.MessageFactory;


/** 
 * Multicast Client for Foop application
 * @author Zhengyan Hu
 * @version 1.0
 * @since 1.0
 */
public class MulticastClient {
	public static final int MAX_PACKET_SIZE = 65507;

	public static void main(String[] args) {
		
		// Test for correct # of args
		if (args.length != 2) {
			throw new IllegalArgumentException("Parameter(s): <Multicast Addr> <Port>");
		}
		 // Test if port is a number
	    int multicastPort = 0;
	    try {
	    	multicastPort = Integer.parseInt(args[1]); // Store port of Server
	    } catch (NumberFormatException e) {
			throw new IllegalArgumentException("Parameter(s): <Multicast Addr> <Port>");
	    }
	    
		// Test if input multicast address
	    InetSocketAddress multicastAddress = new InetSocketAddress(args[0], 0);
	    if (!multicastAddress.getAddress().isMulticastAddress()) { 
	      throw new IllegalArgumentException("Not a multicast address");
	    }

	    //connect to the multicast address
	    MulticastSocket sock;
		try {
			sock = new MulticastSocket(multicastPort);
			// Join the multicast group on any interface
			sock.joinGroup(multicastAddress, null); 

			//open thread to keep receiving the packets
			PacketHandler thread = new PacketHandler(sock);
		    thread.start();

		    //reading stdin and packets at the same time
		    
		    //create a reader to read input
	        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	        //reading until user input "quit"
			do {
		        String name = null;
				try {
					name = reader.readLine();
				} catch (IOException e1) {
				}
				if("quit".equals(name)) {
					break;
				}
			}while(true);
			//leave the group
			sock.leaveGroup(multicastAddress, null);
			//close the connection to break the thread's while loop
			sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}


	/** 
	 * thread for receive the datagram packet
	 * @author Zhengyan Hu
	 * @version 1.0
	 * @since 1.0
	 */
    private static class PacketHandler extends Thread {
    	MulticastSocket sock;

    	public PacketHandler(MulticastSocket sock) {
    		this.sock = sock;
    	}
		@Override
		public void run() {
			while(!sock.isClosed()) {      
		        // Receive a datagram
		        DatagramPacket packet = new DatagramPacket(new byte[MAX_PACKET_SIZE], MAX_PACKET_SIZE);
		        try {
					sock.receive(packet);
				} catch (IOException e1) {
					//IO problem with Packet means we have close the socket
					return;
				}
		        //truncate the datagram
		        byte[] slice = Arrays.copyOfRange(packet.getData(), 0, packet.getLength());
		        try {
			        //decode the message
					Message temp = MessageFactory.decode(slice);
					//print the message according to their type
					if(temp instanceof foop.serialization.Error) {
					    System.err.println("Error :" + temp.toString());
					}else if(temp instanceof Addition) {
					    System.out.println(temp.toString());
					}else {
					    System.err.println("Unexpected message type: " + temp.toString());
					}
				}catch(IllegalArgumentException a) {
					//means we cannot decode the packet
				    System.err.println("Unable Parse the Packet");
				}

			}
		}
    	
    }

}
