/************************************************
*
* Author: Zhengyan Hu
* Assignment: Fiat Food IntAke Tracker
* Class: CSI4321
*
************************************************/

package fiat.app.server;

import java.net.SocketTimeoutException;

import java.io.File;


import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import fiat.serialization.Add;
import fiat.serialization.Get;
import fiat.serialization.Interval;
import fiat.serialization.Item;
import fiat.serialization.ItemList;
import fiat.serialization.MealType;
import fiat.serialization.Message;
import fiat.serialization.MessageFactory;
import fiat.serialization.MessageInput;
import fiat.serialization.MessageOutput;
import fiat.serialization.TokenizerException;
import foop.app.server.FOOPServer;

/** 
 * Server for Fiat application
 * @author Zhengyan Hu
 * @version 1.0
 * @since 1.0
 */
public class Server{
	//initialize the logger
    static Logger logger = Logger.getLogger("fiat.log");
	public final static int TIME_OUT_VALUE = 15 * 1000;

	public static void main(String[] args) {
		//open logger file
		FileHandler fh = null; 
	    try {
			fh = new FileHandler("fiat.log");
			//set the format of logger to text, instead of XML
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);
		} catch (SecurityException e) {  
            logger.log(Level.WARNING, "SecurityException: opening fiat.log"+ e.getMessage() );
	    } catch (IOException ex) {  
            logger.log(Level.WARNING, "IOException: opening fiat.log"+ ex.getMessage() );
	    } 
	    //don't allow to log to console
	    logger.setUseParentHandlers(false);
        logger.addHandler(fh);
        logger.setLevel(Level.FINE);
        //set the level for visibility 
        
        //validation for arguments
		if (args.length != 3 ) {
			//args should have 3 strings
            logger.log(Level.WARNING, "Incorrect Paramter(s) "+ args.toString() );
			throw new IllegalArgumentException("Parameter(s): <Port> <Filename> <#Threads>");
		}
		//parse the arguments
		int servPort = 0;
		int numThread = 0;
		//validation for integer arguments
		try {
		    servPort = Integer.parseInt(args[0]); // Store port of Server
		    numThread = Integer.parseInt(args[2]); // Store thread of Server
		} catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Incorrect Paramter(s) "+ args.toString() );
			throw new IllegalArgumentException("Parameter(s): <Port> <Filename> <#Threads>");
		}

		FOOPServer child = new FOOPServer(args);
		child.start();

		TreeMap<Long, Item> map = initMap(args[1]);

		ItemList iList = initItemList(map);

	    //create a server and connect to provide port
	    ServerSocket server = null;
	    try {
			server = new ServerSocket(servPort);
			//set reuse, we don't need to wait the TIME_WAIT
	        server.setReuseAddress(true); 
	        //create threads for multiple client to connect
            for(int i = 0 ; i < numThread; i++) {
                new Thread(new ClientHandler(server,args[1],map,iList,child)).start();
            }
            
		} catch (IOException e) {
            logger.log(Level.WARNING, "IO exception connect to provided port"+ e.toString() );
		} 
    }
    
    public static TreeMap<Long, Item> initMap(String fileName) {
		//open a tree map for storing the timestamp and item, because the timestamp never be stored
        TreeMap<Long, Item> map = new TreeMap<Long, Item>(); 
        //open file to read the information and store in to map
        File myfile = new File(fileName);
		try {
			//try to create the file
			//if file is exist, will return false
			boolean created = myfile.createNewFile();
			//if file exist
			if (!created) {
				//read the info
			    Scanner myReader = new Scanner(myfile);
			    while (myReader.hasNextLine()) {
			    	String data = myReader.nextLine();
			    	String[] splitter = data.split(" ", 5);
			  
				    try {
				    	//read the item information
						char m = splitter[0].charAt(0);
						int c = Integer.parseInt(splitter[1]);
						double f = Double.parseDouble(splitter[2]);
						//read the timestamp
						long l = Long.parseLong(splitter[3]);
				    	String n = splitter[4];
				    	//add to the map
						map.put(l, new Item(n,MealType.getMealType(m),c,f));
				    } catch (final NumberFormatException e) {
				    }
		        }
			    myReader.close();
			}
		} catch (IOException e1) {
            logger.log(Level.WARNING, "IO exception when reading file "+ e1.toString() );
  		}
		return map;
    }
    
    public static ItemList initItemList(TreeMap<Long, Item> map) {
		//make a itemlist message by map
		ItemList iList = new ItemList(0,0);
	    for (Map.Entry<Long, Item> entry : map.entrySet()) {     
	    	iList.setModifiedTimestamp(entry.getKey());
	    	iList.addItem(entry.getValue());
	    }
		return iList;
    }

	
    private static class ClientHandler implements Runnable {
    	//server port
    	private ServerSocket serverThread;
    	//map with all the item added
    	private TreeMap<Long, Item> map;
    	//Itemlist with all the item added
		private ItemList res;
		//client connect to this thread
		private Socket client;
		//the message we receive from the client
		private Message receive = null;
		//FileWriter, once item added, we will write to it
		private FileWriter myWriter = null;
		//the filename, it will write to
		String filename;
		FOOPServer UDPServer;
		
    	public ClientHandler(ServerSocket serverThread, String myfile, TreeMap<Long, Item> map, ItemList res, FOOPServer t) {
    		this.serverThread = serverThread;
    		this.map = map;
    		this.res = res;
    		this.filename = myfile;
    		this.UDPServer = t;
    	}

		@Override
		public void run() {
			while(true) {
				//continue accept the client
				try {
					client = serverThread.accept();	
					client.setSoTimeout(TIME_OUT_VALUE);
				} catch (SocketException e1) {
					// TODO Auto-generated catch block
				} catch (IOException e) {
		            logger.log(Level.WARNING, "IO exception: cannot accept a client"+ e.toString() );
				}

	            logger.log(Level.INFO, "New connection " + client.getInetAddress() + "-" +client.getPort()
	            			+ " with thread id " + Thread.currentThread().getId());
	            
				while(!client.isClosed()) {
					MessageInput mi = null;

					try {
						mi = new MessageInput(client.getInputStream());
						receive = MessageFactory.decode(mi);
					} catch (SocketTimeoutException e) {
	        			logger.log(Level.INFO, "Close connection"+ client.getInetAddress() 
    					+ "-" +client.getPort()+ " with thread id " + Thread.currentThread().getId());
	        			try {
							client.close();
						} catch (IOException e1) {
							logger.log(Level.WARNING, "IOException when close connection "+ client.getInetAddress() 
        					+ "-" +client.getPort()+ " with thread id " + Thread.currentThread().getId());
						}
						break;
					} catch (IOException e) {
		    			logger.log(Level.WARNING, "IOException when read the message"+ e.getMessage());
					}  catch (TokenizerException e) {
						//when we reach here, mean we receive wrong packet.
				        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				        
						//so send a error message back;
						fiat.serialization.Error error = 
								new fiat.serialization.Error(timestamp.getTime(),"Invalid message connection end");
						//send and encode the list to the client
						try {
							MessageFactory.encode(error, new MessageOutput(client.getOutputStream()));
			    			logger.log(Level.INFO, "Sent Message to "+ client.getInetAddress() 
									+ "-" +client.getPort()+ error.toString());
						} catch (IOException eee) {
			    			logger.log(Level.WARNING, "IOException when sent message to "+ client.getInetAddress() 
									+ "-" +client.getPort()+ res.toString());
						}
						
		    			logger.log(Level.INFO, "Close connection "+ client.getInetAddress() 
								+ "-" +client.getPort()+ " with thread id " + Thread.currentThread().getId());
		    			break;
					}
					
		            logger.log(Level.INFO, "Received message from" + client.getInetAddress() + "-" + client.getPort()
        			+ " " + receive.toString());
		            
		            
		            // once we receive the message
					if(receive.getRequest().equals(Get.REQUEST_GET)) {
						// if we receive get, we just send encode message of ItemList to client/
				        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				        res.setTimestamp(timestamp.getTime());
						try {
							MessageFactory.encode(res, new MessageOutput(client.getOutputStream()));
			    			logger.log(Level.INFO, "Sent Message to "+ client.getInetAddress() 
									+ "-" +client.getPort()+ res.toString());
						} catch (IOException e) {
			    			logger.log(Level.WARNING, "IOException when sending encode the message"+ e.getMessage());

						}
					}else if(receive.getRequest().equals(Interval.REQUEST_INTERVAL)) {
						//get the current timestamp
				        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
						// create a new ItemList, store item that whose time is in the interval
						ItemList iList = new ItemList(timestamp.getTime(),res.getModifiedTimestamp());
						
						//current timestamp - interval minutes = interval timestamp
						long timeInterval = timestamp.getTime() - (60000 * receive.getIntervalTime());
	
						//for which has a timestamp that is bigger than interval timestamp, add to the itemlist
						for (Map.Entry<Long, Item> entry : map.entrySet()) {     
					    	if(entry.getKey() >= timeInterval) {
					    		iList.addItem(entry.getValue());
					    	}
					    }
						
						//encode and send the itemlist message to Client
						try {
							MessageFactory.encode(iList, new MessageOutput(client.getOutputStream()));
			    			logger.log(Level.INFO, "Sent Message to "+ client.getInetAddress() 
									+ "-" +client.getPort()+ iList.toString());
						} catch (IOException e) {
			    			logger.log(Level.WARNING, "IOException when sent message to "+ client.getInetAddress() 
									+ "-" +client.getPort()+ iList.toString());
						}
					    
					}else if(receive.getRequest().equals(Add.REQUEST_ADD)) {
				        Timestamp lasttimestamp = new Timestamp(System.currentTimeMillis());

						//first add the item to map
				        synchronized(map) {
					 		map.put(lasttimestamp.getTime(), receive.getItem());
				        }
						// add the item to itemlist
				        synchronized(res) {
					        res.addItem(receive.getItem());
					        res.setModifiedTimestamp(lasttimestamp.getTime());
				        }
				        UDPServer.sendAddtion(receive.getItem().getName(),
				        		receive.getItem().getMealType(), receive.getItem().getCalories());
				        
						// write the item to the file
						try {
							this.myWriter = new FileWriter(filename, true);
					        synchronized(myWriter) {
								myWriter.write(receive.getItem().getMealType().getMealTypeCode());
					            myWriter.write(" ");
								myWriter.write(String.valueOf(receive.getItem().getCalories()));
					            myWriter.write(" ");
								myWriter.write(String.valueOf(receive.getItem().getFat()));
					            myWriter.write(" ");
								myWriter.write(String.valueOf(lasttimestamp.getTime()));
					            myWriter.write(" ");
					            myWriter.write(receive.getItem().getName());
					            myWriter.write("\n");
							}
				            myWriter.close();

						} catch (FileNotFoundException e) {
			    			logger.log(Level.WARNING, "Cannot find the record file: " + receive.toString() + " filename: "
			    					+ filename);
						} catch (IOException e) {
			    			logger.log(Level.WARNING, "IOException cannot write to the file: " + filename);
						}
						
				        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				        res.setTimestamp(timestamp.getTime());
						//send and encode the list to the client
						try {
							MessageFactory.encode(res, new MessageOutput(client.getOutputStream()));
			    			logger.log(Level.INFO, "Sent Message to "+ client.getInetAddress() 
									+ "-" +client.getPort()+ res.toString());
						} catch (IOException e) {
			    			logger.log(Level.WARNING, "IOException when sent message to "+ client.getInetAddress() 
									+ "-" +client.getPort()+ res.toString());
						}
						
					}else {
						//when we reach here, mean we receive wrong packet.
				        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				        
						//so send a error message back;
						fiat.serialization.Error error = 
								new fiat.serialization.Error(timestamp.getTime(),"Unexpected message receive on Server");
						//send and encode the list to the client
						try {
							MessageFactory.encode(error, new MessageOutput(client.getOutputStream()));
			    			logger.log(Level.INFO, "Sent Message to "+ client.getInetAddress() 
									+ "-" +client.getPort()+ error.toString());
						} catch (IOException e) {
			    			logger.log(Level.WARNING, "IOException when sent message to "+ client.getInetAddress() 
									+ "-" +client.getPort()+ res.toString());
						}
	
					}
					
				}

                
			}

		} 
    	
    }

}
