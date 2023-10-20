/************************************************
*
* Author: Zhengyan Hu
* Assignment: FOOP Client
* Class: CSI4321
*
************************************************/

package foop.app.client;

import foop.serialization.Register;

import foop.serialization.MessageFactory;
import foop.utility.AddressUtility;
import foop.serialization.ACK;
import foop.serialization.Addition;
import foop.serialization.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Random;

/** 
 * Client for foop application
 * @author Zhengyan Hu
 * @version 1.0
 * @since 3.0
 */
public class FOOPClient {
	//Maximum size of a UDP packet
	public static final int MAX_PACKET_SIZE = 65507;
	
	//retransmission time in ms
	public static final int ROUND_TRIP_TIME = 3000;

	//retransmission time
	public static final int RETRANSMISSION_TIMES = 2;

	public static void main(String[] args) {
		// Test for correct # of args
		if (args.length != 2 ) {
			throw new IllegalArgumentException("Parameter(s): <Server> <Port>");
		}	
		
	    String server = args[0]; // Store Server
	    int servPort = 0;
	    try {
		    servPort = Integer.parseInt(args[1]); // Store port of Server
	    } catch (NumberFormatException e) {
			throw new IllegalArgumentException("Parameter(s): <Server> <Port>");
	    }
	    
	    //store server's address
	    InetAddress serverAddress = null;
	    try {
	    	//get the address by the name of server
	    	serverAddress = InetAddress.getByName(server);	    	
	    } catch (UnknownHostException e){
		    System.err.println("Unable to communicate: cannot find server " + e.getMessage());
			throw new IllegalArgumentException("Parameter(s): bad server name");
	    }
	    
    	//get the random message id
		Random rand = new Random();
		int rMsgId = rand.nextInt(ACK.MAX_MSGID);
		
    	//get the local address to register
		Inet4Address addr = null;
		try {
			addr = (Inet4Address) AddressUtility.getAddress();
		} catch (SocketException e) {
		    System.err.println("Unable to get client socket: "+ e.getMessage());
			e.printStackTrace();
		}
		
		//create a UPD socket
	    DatagramSocket socket = null;
    	try {
    		socket = new DatagramSocket();
    		//set the time out within 3 sec
			socket.setSoTimeout(ROUND_TRIP_TIME);
		} catch (IOException e) {
		    System.err.println("Unable to communicate: cannot connect to server " + e.getMessage());
			e.printStackTrace();
		}
		//create a register message
    	Register register = new Register(rMsgId, addr, socket.getLocalPort());
    	//encode the message
    	byte[] RegEncode = MessageFactory.encode(register);
    	//create a sending packet with register message
    	DatagramPacket sPacket = new DatagramPacket(RegEncode,
    			RegEncode.length, serverAddress, servPort);
    	
    	//create a receiving packet
    	DatagramPacket rPacket = new DatagramPacket(
    			new byte[MAX_PACKET_SIZE], MAX_PACKET_SIZE);
    	//count for 2 chance for receive ACK packet
    	int chance = 0;
    	boolean received = false;
    	do{
			try {
				// send the send register packet
				socket.send(sPacket);
			} catch (SocketException e) {
			    System.err.println("Bad Server" + e.getMessage());
				throw new IllegalArgumentException("Parameter(s): bad server name");
			}catch (IOException e1) {
			    System.err.println("Unable send reg pkt: " + e1.getMessage());
			    
				e1.printStackTrace();
			}
			try {
				//receiving the packet from server
				socket.receive(rPacket);
				//get rid of the redundant byte 
				byte[] slice = Arrays.copyOfRange(rPacket.getData(), 0, rPacket.getLength());
		        //check the source address is correct
				if(!rPacket.getAddress().equals(serverAddress)) {
				    System.err.println("Received packet from different server");
				}
				try {
					//decode the message
					Message temp = MessageFactory.decode(slice);
					if(temp instanceof ACK) {
						if(temp.getMsgID() != rMsgId) {
						    System.err.println("Unexpected MSGID = "
						    		+ rMsgId + " vs. " + temp.getMsgID());
				            Thread.sleep(3000);
						}else {
						    System.out.println(temp.toString());
							//if message is ACK, we can stop looping
							received = true;	
						}
					}else {
						//if message isn't ACK, print the error message
					    System.err.print("Unexpected message type: ");
					    System.err.println(temp.toString());
					}
				}catch(Exception e) {
					//decode message throw a exception, cannot parse message
				    System.err.println("Unable to parse message:" + e.getMessage());
					e.printStackTrace();
				}
				
			}catch(InterruptedIOException e) {
				//haven't receive message after 3 sec
				chance ++;
				//increment chance
			    System.err.println("Timed out in " + ROUND_TRIP_TIME/1000 + " sec: " + chance);
			} catch (IOException e1) {
			    System.err.println("Received Packet Erorr: " + e1.getMessage());
				e1.printStackTrace();
			}

    	}while(!received && chance < RETRANSMISSION_TIMES);
    	
		try {
			//reset the timeout
			socket.setSoTimeout(0);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		//if the loop is end because of the out of chance
		// we will terminate
		if(chance >= RETRANSMISSION_TIMES) {
		    System.err.println("Unable to register");
		    socket.close();
		    System.exit(0);
		}
		//create a thread for receiving packet
		
		Thread child = new Thread(new PacketHandler(socket,rPacket));
		child.start();
		
		
		//create a reader to read input
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        //reading until user input "quit"
		do {
	        String name = null;
			try {
				name = reader.readLine();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if("quit".equals(name)) {
				break;
			}
		}while(!socket.isClosed());
		
		//terminate everything and end
		socket.close();
		child.interrupt();
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    private static class PacketHandler implements Runnable {
    	DatagramSocket socket;
    	DatagramPacket rPacket;
    	public PacketHandler(DatagramSocket socket, DatagramPacket rPacket) {
    		this.socket = socket;
    		this.rPacket = rPacket;
    	}
	
		@Override
		public void run() {

			//continue listening to the server
			while(!socket.isClosed()) {
				try {
					//wait for receiving packet
					socket.receive(rPacket);
				} catch (IOException e1) {
					//here mean main thread has close the socket
					//so we will stop listening
					return;
				}
				
				//get rid of the redundant byte 
				byte[] slice = Arrays.copyOfRange(rPacket.getData(), 0, rPacket.getLength());
				try {
					//decode the message
					Message temp = MessageFactory.decode(slice);
					//print the message according to the their type
					if(temp instanceof foop.serialization.Error) {
					    System.err.println("Error " + temp.toString());
					}else if(temp instanceof Addition) {
					    System.out.println(temp.toString());
					}else {
					    System.err.println("Unexpected message type");
					}
				}catch(Exception e) {
					//decode message throw an exception, parse fail
				    System.err.println("Unable to parse message:" + e.getMessage());
					e.printStackTrace();
				}

			}
			
		}
		
    }
}
