/************************************************
*
* Author: Zhengyan Hu
* Assignment: FOOP Server
* Class: CSI4321
*
************************************************/

package foop.app.server;

import java.io.IOException;


import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.DatagramPacket;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.Random;

import foop.app.client.FOOPClient;
import fiat.serialization.MealType;
import foop.serialization.ACK;
import foop.serialization.Addition;
import foop.serialization.Message;
import foop.serialization.MessageFactory;
import foop.serialization.Register;

/** 
 * Server for Foop application
 * @author Zhengyan Hu
 * @version 1.0
 * @since 1.0
 */
public class FOOPServer extends Thread{
	//the range of code(0-3)
	public static final int CODE_RANGE = 3;
	
	//initialize the logger
    static Logger logger = Logger.getLogger("foop.log");
    //udp server declaration
    private DatagramSocket socket;
    //port of foop server
    private String[] args;
    //store the address of registered address and port
    private Set<InetSocketAddress> set = new HashSet<InetSocketAddress>(); 

    //constructor of the thread
    public FOOPServer(String[] args) {
    	this.args = args;
	}
    //once receive add message, fiat sever will send addition message
	public void sendAddtion(String name, MealType meal, int cal) {
		new Thread(new AdditionHandler(set, socket,name,meal,cal)).start();
	}
	
	public void run() {
		//open logger file
		FileHandler fh = null; 
	    try {
			fh = new FileHandler("foop.log");
			//set the format of logger to text, instead of XML
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);
		} catch (SecurityException e) {  
            logger.log(Level.WARNING, "SecurityException: opening foop.log"+ e.getMessage() );
	    } catch (IOException ex) {  
            logger.log(Level.WARNING, "IOException: opening foop.log"+ ex.getMessage() );
	    } 
	    //don't allow to log to console
	    logger.setUseParentHandlers(false);
        logger.addHandler(fh);
        logger.setLevel(Level.FINE);
        //set the level for visibility 
        
        int servPort = Integer.parseInt(args[0]); // Store port of Server
        
        try {
        	//open socket on given port
			socket = new DatagramSocket(servPort);
		} catch (SocketException e) {
            logger.log(Level.WARNING, "IOException: opening FOOP server fail"+ e.getMessage());
            System.exit(0);
		}
        //get the local address for logger
        InetAddress addr = socket.getLocalAddress();
        
        //continue to receive packet
        while(true) {
        	//Initialize the packet with max UDP packet size 
        	DatagramPacket packet = new DatagramPacket(
					new byte[FOOPClient.MAX_PACKET_SIZE],FOOPClient.MAX_PACKET_SIZE);
        	try {
        		//try to receive the packet
				socket.receive(packet);
								
	            logger.log(Level.INFO, "Receive packet from "+ 
	            		packet.getAddress()+"/" + packet.getPort());
			} catch (IOException e) {
	            logger.log(Level.WARNING, "Communication Problem: receive packet "
	            			+ e.getMessage());
			}
        	//get the code from the receive packet
   		 	int code = packet.getData()[0] & 0x0F;
        	//check if code is valid
        	if(code > CODE_RANGE || code < 0) {
        		//make error message object with msg unknown code
        		foop.serialization.Error err = new foop.serialization.Error
		    			(0, "Unknown code: " + code);
        		//encode the message
		    	byte[] errEncode = MessageFactory.encode(err);
		    	//make the UDP packet with encode message
		    	DatagramPacket s = new DatagramPacket(errEncode,
	        			errEncode.length, packet.getAddress(),packet.getPort());
				try {
					//send out the packet
					socket.send(s);
				} catch (IOException e) {
				}
	            logger.log(Level.INFO, "Send Error Message: <"+ err.toString()
	            		+ "> from "+ addr.getHostAddress() +"/" + servPort);
	            //code is wrong, so continue to receive next packet
	            continue;
        	}
        	//truncate the packet to get rid of unused space
        	// in this way, the message decode will not say "message to long"
			byte[] slice = Arrays.copyOfRange(packet.getData(), 0, packet.getLength());
			try {
				//decode the message after truncated
				Message temp = MessageFactory.decode(slice);
	            logger.log(Level.INFO, "Message Parsed: <"+ temp.toString()
	            	+ "> from "+ packet.getAddress()+"/" + packet.getPort());	
				if(temp instanceof Register) {
		            //if temp is a register message
					if(!(packet.getPort() == temp.getPort())) {
						//the register port is incorrect
				    	foop.serialization.Error err = new foop.serialization.Error
				    			(temp.getMsgID(), "Incorrect port");
				    	//send the error message with incorrect port
				    	byte[] errEncode = MessageFactory.encode(err);
			        	DatagramPacket sendPacket = new DatagramPacket(errEncode,
			        			errEncode.length, packet.getAddress(),packet.getPort());
						socket.send(sendPacket);
			            logger.log(Level.INFO, "Send Error Message: <"+ err.toString()
			            		+ "> from "+ addr.getHostAddress() +"/" + servPort);				
					}else if(set.contains(temp.getSocketAddress())) {
						//the client list has this address
				    	foop.serialization.Error err = new foop.serialization.Error
				    			(temp.getMsgID(), "Already register");
				    	//send the error message with already register
				    	byte[] errEncode = MessageFactory.encode(err);
			        	DatagramPacket sendPacket = new DatagramPacket(errEncode,
			        			errEncode.length, packet.getAddress(),packet.getPort());
						socket.send(sendPacket);
			            logger.log(Level.INFO, "Send Error Message: <"+ err.toString()
			            	+ "> from "+ addr.getHostAddress() +"/" + servPort);
					}else {
						//after all checking
						//we can add the address and port to the client list 
						set.add(temp.getSocketAddress());
						//send the ack message to the socket
				    	ACK ack = new ACK(temp.getMsgID());
				    	byte[] ackEncode = MessageFactory.encode(ack);
			        	DatagramPacket sendPacket = new DatagramPacket(ackEncode,
			        			ackEncode.length, packet.getAddress(),packet.getPort());
						socket.send(sendPacket);
			            logger.log(Level.INFO, "Send ACK Message: <"+ ack.toString()
		            		+ "> from "+ addr.getHostAddress()+"/" + servPort);
					}
				}else {
					//temp is not a register message
					//send error message with receive unexpected message type
			    	foop.serialization.Error err = new foop.serialization.Error
			    			(temp.getMsgID(), "Unexpected message type: "+ code);
			    	byte[] errEncode = MessageFactory.encode(err);
		        	DatagramPacket sendPacket = new DatagramPacket(errEncode,
		        			errEncode.length, packet.getAddress(),packet.getPort());
					socket.send(sendPacket);
		            logger.log(Level.INFO, "Send Error Message: <"+ err.toString()
	            		+ "> from "+ addr.getHostAddress()+"/" + servPort);

				}
			}catch(Exception e) {
				//decode message throw a exception, cannot parse message
				//send error message with unable to parse message

		    	foop.serialization.Error err = new foop.serialization.Error(0,
		    					"Unable to parse message: " + 
		    					 packet.getData());
		    	byte[] errEncode = MessageFactory.encode(err);
	        	DatagramPacket sendPacket = new DatagramPacket(errEncode,
	        			errEncode.length, packet.getAddress(),packet.getPort());				
				try {
					socket.send(sendPacket);
				} catch (IOException e1) {
		            logger.log(Level.WARNING, "Communication Problem: "+e.getMessage());
				}
	            logger.log(Level.INFO, "Send Error Message: <"+ err.toString()
            		+ "> from "+ addr.getHostAddress()+"/" + servPort);
			}
        }
	}
    
	//AdditionHandler handle the event to send all the addition message
    private static class AdditionHandler implements Runnable {
    	//client list
        private Set<InetSocketAddress> CopySet; 
        //current socket 
        private DatagramSocket socket;
        //name for addition message 
        private String name;
        //MealType for addition message 
        private MealType meal;
        //calories for addition message 
        private int cal;
        //constructor of thread information of the addition
    	public AdditionHandler(Set<InetSocketAddress> set,DatagramSocket socket,
    			String name, MealType meal, int cal) {
    		this.CopySet = set;
    		this.socket = socket;
    		this.name = name;
    		this.meal = meal;
    		this.cal = cal;
    	}
    	
		@Override
		public void run() {

			//get a random messsage id within [0-255]
			Random rand = new Random();
			int rMsgId = rand.nextInt(foop.serialization.Message.MAX_MSGID);
			//make addition message with information
	    	Addition add = new Addition(rMsgId, name, meal, cal);
	    	//encode the addition message
			byte[] encode = MessageFactory.encode(add);
			
			for (InetSocketAddress so : CopySet) {
				//make up the packet with each address/port in the client list
				DatagramPacket sendPacket = new DatagramPacket(encode,
						encode.length, so.getAddress(),so.getPort());
				try {
					//send the addition message
					socket.send(sendPacket);
		            logger.log(Level.INFO, "Send Addition Message: <"+ add.toString()
            		+ "> from "+ socket.getLocalPort() +"/" + socket.getPort());
				} catch (IOException e) {
		            logger.log(Level.WARNING, "Communication Problem: "+e.getMessage());
				}
			}
		}
    	
    }

    
}
