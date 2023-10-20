/************************************************
*
* Author: Zhengyan Hu
* Assignment: Fiat Food IntAke Tracker
* Class: CSI4321
*
************************************************/

package fiat.app.client;

import fiat.serialization.Message;


import fiat.serialization.MessageFactory;
import fiat.serialization.MessageInput;
import fiat.serialization.MessageOutput;
import fiat.serialization.TokenizerException;
import fiat.serialization.Add;
import fiat.serialization.Get;
import fiat.serialization.Interval;
import fiat.serialization.Item;
import fiat.serialization.ItemList;
import fiat.serialization.MealType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.Timestamp;

/** 
 * Client for Fiat application
 * @author Zhengyan Hu
 * @version 1.0
 * @since 1.0
 */
public class Client {
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
	    try {
	    	//create socket to connected server on servPort
	    	Socket socket = new Socket(server, servPort);

	    	//use reader to connect to std in and communicate with user
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			
			//start command
			while(true) {
				// prompt for request add or get
				System.out.print("Request (ADD|GET|INTERVAL): ");
		        String req = br.readLine(); // get the first line
		        // validation for request(can only be add or get)
				while(!Add.REQUEST_ADD.equals(req) && !Get.REQUEST_GET.equals(req)
						&& !Interval.REQUEST_INTERVAL.equals(req)) {
				    System.err.println("Unknown request");
					//get a unknown request, read request again
					System.out.print("Request (ADD|GET|INTERVAL): ");
			        req = br.readLine();
				}
				// store timestamp by system current time
		        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		    	//get the outputStream of socket for encode factory
				MessageOutput out = new MessageOutput(socket.getOutputStream());
				
		        //if the request is add, we will read a item.
				if(Add.REQUEST_ADD.equals(req)) {
					// prompt for reading the name of item
					System.out.print("Name: ");
			        String name = br.readLine();
			        //validation for name
			        while(!Item.CheckNonEmptyString(name)) {
					    System.err.println("name cannot be null, empty, too large, "
					    					+ "or contain illegal characters");
						//invalid name detected, prompt to read again
					    System.out.print("Name: ");
						name = br.readLine();
			        }
			        
					// prompt for reading the meal type
					System.out.print("Meal type (B, L, D, S): ");
					String meal = br.readLine();
			        //validation for meal type
			        while(!("BLDS".contains(meal))) {
					    System.err.println("illegal code");
						//invalid meal type detected, prompt to read again
					    System.out.print("Meal type (B, L, D, S): ");
						meal = br.readLine();
			        }
			        //store the meal type we just read
			        MealType mt = MealType.getMealType(meal.charAt(0));
			        
					// prompt for reading the Calories
					System.out.print("Calories: ");
					String input = br.readLine();
					int calories = -1;		
					//loop to read until there is a valid integer 
					while(true) {
				        try {
				        	//parse the input to integer
					    	calories = Integer.parseInt(input);
					        if(!(Item.CheckInteger(calories))) {
						    	//validation fail, read and loop back
							    System.err.println("calories has illegal format or is null");
								System.out.print("Calories: ");
								input = br.readLine();
					        }else {
						    	//validation success, end of loop
					        	break;
					        }
					    } catch (final NumberFormatException e) {
					    	//if there is a non-digital character in the input stream
					    	// we will detect at here and read a again.
					        System.err.println("calories has illegal format or is null");
			    			System.out.print("Calories: ");
				        	input = br.readLine();
					    }
					}
					// prompt for reading the fat
					// it is almost as same as reading calories
					double fat = -1.0;
					System.out.print("Fat: ");
					input = br.readLine();
					//loop to read until there is a valid double value 
					while(true) {
				        try {
				        	//parse the input to double
					    	fat = Double.parseDouble(input);
					        if(!(Item.CheckDouble(fat))) {
						    	//validation fail, read and loop back
							    System.err.println("fat has illegal format or is null");
								System.out.print("Fat: ");
								input = br.readLine();
					        }else {
						    	//validation success, end of loop
					        	break;
					        }
					    } catch (final NumberFormatException e) {
					    	//we cannot parse the input to double, such as "abc".
					    	//In this way, we will prompt to read again.
					        System.err.println("fat has illegal format or is null");
			    			System.out.print("Fat: ");
				        	input = br.readLine();
					    }
					}
			        // create a add message with item inputed by user
			        Message request = new Add(timestamp.getTime(),new Item(name,mt,calories,fat));
			        // encode the add message, it will write to output
			        MessageFactory.encode(request, out);
			  
				}else if(Get.REQUEST_GET.equals(req)){

			        // create a get message with system timestamp
			        Message request = new Get(timestamp.getTime());
			        // encode the add message, it will write to output
			        MessageFactory.encode(request, out);
				}else if(Interval.REQUEST_INTERVAL.equals(req)){
					System.out.print("Time: ");
					String input = br.readLine();
					int time = -1;		
					//loop to read until there is a valid integer 
					while(true) {
				        try {
				        	//parse the input to integer
					    	time = Integer.parseInt(input);
					        if(!(Item.CheckInteger(time))) {
						    	//validation fail, read and loop back
							    System.err.println("Interval has illegal format or is null");
								System.out.print("Time: "); 
								input = br.readLine();
					        }else {
						    	//validation success, end of loop
					        	break;
					        }
					    } catch (final NumberFormatException e) {
					    	//if there is a non-digital character in the input stream
					    	// we will detect at here and read a again.
					        System.err.println("time has illegal format or is null");
			    			System.out.print("Time: ");
				        	input = br.readLine();
					    }
					}
			        // create a get message with system timestamp
			        Message request = new Interval(timestamp.getTime(),time);
			        // encode the add message, it will write to output
			        MessageFactory.encode(request, out);
				}
				
				//store input stream of the server
				MessageInput in = new MessageInput(socket.getInputStream());
				
				try {
					//read a message from the server and decode to message object
					Message mReceive = MessageFactory.decode(in);
					// this message can only be list or error
					
					if(mReceive.getRequest().equals(fiat.serialization.Error.REQUEST_ERROR)) {
						//when request is error, print error message
				        System.err.println("Error: " + mReceive.getMessage());
					}else if(mReceive.getRequest().equals(ItemList.REQUEST_LIST)){
						//when request is list, print list to string
				        System.out.println(mReceive.toString());
					}else {
						//reach here, we get a unexpected message
				        System.err.println("Unexpected message: " + mReceive.toString());
					}
				} catch (TokenizerException e) {
					//reach here means we get an invalid message(add or get) 
			        System.err.println("Invalid message: " + e.getMessage());
					e.printStackTrace();
			        //terminate the program
					System.exit(0);
				}
				
				//finally comment to check if user want to continue or not
				System.out.print("Continue (y/n): ");
				//read the input from user
				String cont = br.readLine();
				//validation for the input
				while(!(cont.length() == 1 && "yn".contains(cont))) {
					//if not receiving y or n, prompt to read again
					System.out.print("Continue (y/n): ");
					cont = br.readLine();
				}
				//if read character 'n', break the loop  
				if(cont.charAt(0) == 'n') {
					break;
				}
			}
			//close the stdin and socket
			br.close();
			socket.close();	
		} catch (IOException e) {
			//problem communicate with server(IOException)
		    System.err.println("Unable to communicate: cannot connect to server " + e.getMessage());
			System.exit(0);
		}
		    		    
	}

}
