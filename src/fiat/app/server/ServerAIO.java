/************************************************
*
* Author: Zhengyan Hu
* Assignment: Fiat Food IntAke Tracker
* Class: CSI4321
*
************************************************/

package fiat.app.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import fiat.serialization.Add;
import fiat.serialization.Get;
import fiat.serialization.Interval;
import fiat.serialization.Item;
import fiat.serialization.MessageNIODeframer;
import fiat.serialization.MessageOutput;
import fiat.serialization.TokenizerException;
import fiat.serialization.ItemList;
import fiat.serialization.Message;
import fiat.serialization.MessageFactory;
import fiat.serialization.MessageInput;
import foop.app.server.FOOPServer;
/** 
 * Server with Asynchronous I/O for fiat application
 * @author Zhengyan Hu
 * @version 1.0
 * @since 1.0
 */
public class ServerAIO {
	
	/**
	 * Message delimiter
	 */
	static final String MESSAGE_DELIMITER  = "\n";
    /**
     * Buffer size (bytes)
     */
    private static final int BUFSIZE = 4096;
    /**
     * Global logger
     */
    private static final Logger logger = Logger.getLogger("fiat.log");

    
    public static void main(String[] args) throws IOException {
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
        
        
        if (args.length != 2) { // Test for correct # of args
			//args should have 2 strings
            logger.log(Level.WARNING, "Incorrect Paramter(s) "+ args.toString() );
			throw new IllegalArgumentException("Parameter(s): <Port> <Filename>");
		}

		//parse the arguments
		int servPort = 0;
		//validation for integer arguments
		try {
		    servPort = Integer.parseInt(args[0]); // Store port of Server
		} catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Incorrect Paramter(s) "+ args.toString() );
			throw new IllegalArgumentException("Parameter(s): <Port> <Filename>");
		}
		//Open Foop thread
		FOOPServer child = new FOOPServer(args);
		child.start();
		
		//create a map for last modified time and all item
		//base on the input file
		TreeMap<Long, Item> map = Server.initMap(args[1]);
		ItemList iList = Server.initItemList(map);
		
        try (AsynchronousServerSocketChannel listenChannel = AsynchronousServerSocketChannel.open()) {
            // set reuse address avoid time wait
        	listenChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            // Bind local port
            listenChannel.bind(new InetSocketAddress(servPort));

            // Create accept handler
            listenChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
                @Override
                public void completed(AsynchronousSocketChannel clntChan, Void attachment) {
                    listenChannel.accept(null, this);
                    handleAccept(clntChan, iList, map, args[1], child);
                }
                @Override
                public void failed(Throwable e, Void attachment) {
                    logger.log(Level.WARNING, "Close Failed", e);
                }
            });
            // Block until current thread dies
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            logger.log(Level.WARNING, "Server Interrupted", e);
        }
    }


    public static void handleAccept(AsynchronousSocketChannel clntChan, 
    								ItemList iList, TreeMap<Long, Item> map, String fileName,
    								FOOPServer child) {
        // After accepting, the server reads from the client
        initRead(clntChan, ByteBuffer.allocateDirect(BUFSIZE), iList, map, fileName, child);
    }

    public static void initRead(AsynchronousSocketChannel clntChan, ByteBuffer buf, 
    							ItemList iList, TreeMap<Long, Item> map, String fileName,
    							FOOPServer child) {
    	//create a deframer
    	MessageNIODeframer framer = new MessageNIODeframer(MESSAGE_DELIMITER.getBytes(StandardCharsets.UTF_8));

    	clntChan.read(buf, buf, new CompletionHandler<Integer, ByteBuffer>() {
            public void completed(Integer bytesRead, ByteBuffer buf) {
            	//if read -1 bytes means this channel is done with sending
		        if (bytesRead == -1) {
		        	//close the channel
		            try {
		                clntChan.close();
		            } catch (IOException e) {
		                die(clntChan, "Unable to close", e);
		            }
		        } else if (bytesRead > 0) {
		        	//cope the buf
		        	ByteBuffer check = buf.duplicate();
		        	//flip the copy buf
		        	check.flip();
		        	//convert copy buf to string 
		        	byte[] b = new byte[check.remaining()];
		        	check.get(b);
		        	//read a full message is ready
		        	byte[] msg = framer.nextMsg(b);
		        	//msg is null mean the message is not ready
		        	if(msg == null) {
		        		//so we read again with the original buf
			            initRead(clntChan, buf, iList, map, fileName, child);
		        	}else {
		        		//if full message is ready, decode the message
			        	Message receive = null;
						try {
							receive = MessageFactory.decode(new MessageInput(new ByteArrayInputStream(msg)));
			    			logger.log(Level.WARNING, "Receive Message " + receive.toString());
						} catch (TokenizerException e) {
			    			logger.log(Level.WARNING, "Invalid Message " + msg);
						}
						//finish read a message, clear the buffer for next read
			        	buf.clear();
			        	//make a send message
			        	byte [] array = makeSendMsg(clntChan, receive, iList, map, fileName, child);
			        	//convert the message to byte buffer and write to the channel
			        	ByteBuffer buffer = ByteBuffer.wrap(array);     
	                    initWrite(clntChan, buffer, buf, iList, map,fileName, child);
		        	}
		        	
		        }
		        logger.info(() -> "Handled read of " + bytesRead + " bytes"); 
            }
            public void failed(Throwable ex, ByteBuffer v) {
                die(clntChan, "Fail on Read Message", ex);
            }
        });
    }
    
    public static byte[] makeSendMsg(AsynchronousSocketChannel clntChan, Message receive, ItemList res,
    						TreeMap<Long, Item> map, String fileName,FOOPServer UDPServer) {
		MessageOutput mout = new MessageOutput(new ByteArrayOutputStream());
		//check if encode message fail
		if(receive == null) {
			//when we reach here, mean we receive wrong packet.
	        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	        
			//so send a error message back;
			fiat.serialization.Error error = 
					new fiat.serialization.Error(timestamp.getTime(),"Invalid message connection end");
			//send and encode the list to the client
			try {
				MessageFactory.encode(error, mout);
    			logger.log(Level.INFO, "Sent Message to "+ clntChan.getLocalAddress() 
						+ "-" + error.toString());
			} catch (IOException eee) {

			}
			
		}else if(receive.getRequest().equals(Get.REQUEST_GET)) {
			//get current time stamp
	        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	        res.setTimestamp(timestamp.getTime());
	        
			try {
				//send the List of item
				MessageFactory.encode(res, mout);
    			logger.log(Level.INFO, "Sent Message to "+ clntChan.getLocalAddress() + res.toString());
			} catch (IOException e) {
    			logger.log(Level.WARNING, "IOException when sending encode the message"+ e.getMessage());

			}
		}else if(receive.getRequest().equals(Interval.REQUEST_INTERVAL)) {
			// create a new ItemList, store item that whose time is in the interval
			ItemList iList = new ItemList(res.getTimestamp(),res.getModifiedTimestamp());
			//get the current timestamp
	        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
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
				MessageFactory.encode(iList, mout);
    			logger.log(Level.INFO, "Sent Message to "+ clntChan.getLocalAddress() + iList.toString());
			} catch (IOException e) {
    			logger.log(Level.WARNING, "IOException when sending encode the message"+ e.getMessage());

			}
		}else if(receive.getRequest().equals(Add.REQUEST_ADD)) {
			//get current time
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
				FileWriter myWriter = new FileWriter(fileName, true);
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
    					+ fileName);
			} catch (IOException e) {
    			logger.log(Level.WARNING, "IOException cannot write to the file: " + fileName);
			}
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	        res.setTimestamp(timestamp.getTime());

	        //send and encode the list to the client
			try {
				MessageFactory.encode(res, mout);
    			logger.log(Level.INFO, "Sent Message to "+ clntChan.getLocalAddress() + res.toString());

			} catch (IOException e) {
    			logger.log(Level.WARNING, "IOException when sending encode the message"+ e.getMessage());
			}
			
		}else {
			//when we reach here, mean we receive wrong packet.
	        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	        
			//so send a error message back;
			fiat.serialization.Error error = 
					new fiat.serialization.Error(timestamp.getTime(),"Unexpected message receive on Server");
			//send and encode the list to the client
			try {
				MessageFactory.encode(error, mout);
    			logger.log(Level.INFO, "Sent Message to "+ clntChan.getLocalAddress() + error.toString());
			} catch (IOException e) {
    			logger.log(Level.WARNING, "IOException when sent message");
			}
		}
		//get the message from MessageOutputStreeam
		byte [] result = null;
		try {
			result =  mout.getEncoding().getBytes(StandardCharsets.UTF_8);
		} catch (IOException e) {
			logger.log(Level.WARNING, "get encoding message");
		}
		return result;
    }


    public static void initWrite(AsynchronousSocketChannel clntChan, ByteBuffer b, ByteBuffer buf,
    		ItemList iList, TreeMap<Long, Item> map, String fileName, FOOPServer child) {
    	
        clntChan.write(b, b, new CompletionHandler<Integer, ByteBuffer>() {
        	
            public void completed(Integer bytesWritten, ByteBuffer b) {
                if (b.hasRemaining()) { // More to write
                    initWrite(clntChan, b, buf, iList, map,fileName, child);
                } else { // Back to reading
                    // After writing all bytes, the server again reads
                    b.clear();
		            initRead(clntChan, buf, iList, map,fileName, child);
                }
            }
            public void failed(Throwable ex, ByteBuffer buf) {
                die(clntChan, "Write failed", ex);
            }
        });
    }
   


    public static void die(AsynchronousSocketChannel clntChan, String msg, Throwable ex) {
        logger.log(Level.SEVERE, msg, ex);
        try {
            clntChan.close();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Close Failed", e);
        }
    }

}
