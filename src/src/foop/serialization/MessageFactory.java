/************************************************
*
* Author: Zhengyan Hu
* Assignment: FOOP
* Class: CSI4321
*
************************************************/

package foop.serialization;

import java.io.UnsupportedEncodingException;


import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

import fiat.serialization.Item;
import fiat.serialization.MealType;
/** 
 * Represents MessageFactory class for encode or decode
 * @author Zhengyan Hu
 * @version 3.0
 * @since 1.0
 */
public class MessageFactory {
	/**
	 * represents minimum length of packet
	 */
	static final int PKT_MIN_SIZE = 2;
	/**
	 * represents the code of register with version prefix
	 * binary: 0011 0000
	 */
	static final int REGISTER_CODE = 48;
	/**
	 * represents the code of addition with version prefix
	 * binary: 0011 0001
	 */
	static final int ADDITION_CODE = 49;
	/**
	 * represents the code of addition with version prefix
	 * binary: 0011 0010
	 */
	static final int ERROR_CODE = 50;
	/**
	 * represents the code of ACK with version prefix
	 * binary: 0011 0011
	 */
	static final int ACK_CODE = 51;
	
	/**
	 * represent the format of address in bit
	 */
	static final int ADDRESS_BIT = 32;
	/**
	 * represent the format of port in bit
	 */
	static final int PORT_BIT = 16;
	/**
	 * represent the format of MealType in bit
	 */
	static final int MEALTYPE_BIT = 8;
	/**
	 * represent the format of calories in bit
	 */
	static final int CALORIES_BIT = 16;
	/**
	 * represent the format of Length in bit
	 */
	static final int LENGTH_BIT = 8;
	
	/**
	 * Constructs message through deserialization
	 * @param pkt buffer containing bytes of a single message
	 * @return new (deserialized) message
	 * @throws NullPointerException if pkt is null
	 * @throws IllegalArgumentException 
	 * 		if validation failure (e.g., invalid name, etc.), packet too long/short, etc.
	 */
	public static Message decode(byte[] pkt) {
		//check if the packet is null
		if(pkt == null) {
			throw new NullPointerException
						("decode byte packet cannot be null " + pkt);
		}
		
		//check the packet size if it contains version code and message id
		if(pkt.length < PKT_MIN_SIZE) {
			throw new IllegalArgumentException
			("decode packet is too short " +pkt+ pkt.length);
		}
		
		//parse the version, code and message id from the packet
		int code = pkt[0] & 0xff;
		int msgID = pkt[1] & 0xff;

		//base on the code, to determine which packet should decode
		if(code == REGISTER_CODE) {
			//check the length of the packet if it has the correct length
			if(pkt.length < (PORT_BIT + ADDRESS_BIT)/Byte.SIZE + PKT_MIN_SIZE) {
				throw new IllegalArgumentException
							("decode packet is too short " + pkt.length + pkt);
			}else if(pkt.length > (PORT_BIT + ADDRESS_BIT)/Byte.SIZE + PKT_MIN_SIZE) {
				throw new IllegalArgumentException
						("decode packet is too long :" + pkt.length + pkt);
			}
			//now to make sure the length is right
			Inet4Address i4Addr = null;
	        try {
	        	//get the address from the packet with little endian after the message id
				i4Addr = (Inet4Address) InetAddress
						.getByAddress(new byte[] {pkt[5],pkt[4],pkt[3],pkt[2]});
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        //validation for address
	        if(!Message.checkAddress(i4Addr)) {
				throw new IllegalArgumentException
							("fail on validation of Inet4Address" + i4Addr);
	        }
        	//get the port from the packet with little endian after ip address
	        int port = ((pkt[7] & 0xff) << 8) | (pkt[6] & 0xff);
	        //return the new message register
	        return new Register(msgID, i4Addr, port);
	        
		}else if(code == ADDITION_CODE){
			//check if the length is enough to read next byte
			if(pkt.length < PKT_MIN_SIZE + 1) {
				throw new IllegalArgumentException
							("decode packet is too short " + pkt.length + pkt);
			}
			//read the next byte and convert to int
			//this is the length of name
			int length = pkt[2] & 0xff;
			//once we make sure the length of name, 
			//we can check if the length of packet is correct
			if(pkt.length < (CALORIES_BIT + MEALTYPE_BIT + LENGTH_BIT)
								/Byte.SIZE + PKT_MIN_SIZE + length + 1) {
				
				throw new IllegalArgumentException
							("decode packet is too short " + pkt.length + pkt);
			}else if(pkt.length > (CALORIES_BIT + MEALTYPE_BIT + LENGTH_BIT)
								/Byte.SIZE + PKT_MIN_SIZE + length + 1) {
				throw new IllegalArgumentException
					("decode packet is too long " + pkt.length + pkt);
			}
			//byte be ready to read and store the name
			byte nameBuffer[] = new byte[length];
			//initialize a counter for name
			int count = 0;
			//read the name to the name buffer
			for(int i = 3; i < length + 3; i ++) {
				nameBuffer[count] = pkt[i];
				count++;
			}
			//convert name in byte to string
		    String name = new String(nameBuffer, StandardCharsets.UTF_8);
		    //validation for name
		    if(!Message.checkName(name)) {
				throw new IllegalArgumentException
					("Addition: validation fail on name" + name + pkt);
		    }
		    //read and validate for meal type
		    if(!Item.CheckMealTypeCode((char)pkt[3 + length])) {
				throw new IllegalArgumentException
					("Addition: validation fail on Meal Type" + (char)pkt[3 + length] + pkt);
		    }
		    //store the meal type, once it pass the validation
			MealType meal = MealType.getMealType((char)pkt[3 + length]);
			//check there is zero append after to the meal type 
			if((int)pkt[4 + length] != 0) {
				throw new IllegalArgumentException
				    ("Addition: byte after calories should be 0 " + pkt[4 + length] + pkt);
			}
			// read the calories from the packet(little endian)
	        int cal = ((pkt[6 + length] & 0xff) << 8) | (pkt[5 + length] & 0xff);
	        // validation for calories
			if(!(Item.CheckInteger(cal))) {
				throw new IllegalArgumentException
					("Addition: validation fail on calories " + cal +" "+pkt);
			}
	        // return the new addition with all value we get
	        return new Addition(msgID,name,meal,cal);
			
		}else if(code == ERROR_CODE){
			//check the length of packet if the error message is empty
			if(pkt.length - 2 == 0) {
				return new foop.serialization.Error(msgID,"");
			}
	        // message buffer is ready to read the error message
			byte messageBuffer[] = new byte[pkt.length - 2];
			//counter for message buffer
			int count = 0;
			// read the error message from packet
			for(int i = 2; i < pkt.length; i ++) {
				messageBuffer[count] = pkt[i];
				count++;
			}
			// convert the error message buffer to string 
		    String errorMessage = new String(messageBuffer, StandardCharsets.UTF_8);
		    //validation for error message
			if(!(Message.checkErrorMessage(errorMessage))) {
				throw new IllegalArgumentException
					("Error Data: invalid error message " + errorMessage);
			}
			//return the new error with error message
			return new foop.serialization.Error(msgID,errorMessage);
		}else if(code == ACK_CODE){
			//check the packet size if it contains version code and message id
			if(pkt.length != PKT_MIN_SIZE) {
				throw new IllegalArgumentException
				("decode packet is too long " +pkt + pkt.length);
			}
			
			//return the ACK with message id
			return new ACK(msgID);
		}
		
		throw new IllegalArgumentException("Invalid code or version " + pkt);
	}
	
	
	/**
	 * Serializes message
	 * @param msg message to serialize
	 * @return serialized byte array
	 * @throws NullPointerException if msg is null
	 * @throws IllegalArgumentException if validation failure
	 */
	public static byte[] encode(Message msg) {
		//message cannot be null
		if(msg == null) {
			throw new NullPointerException
						("encode message cannot be null " + msg);
		}
		//determine what kind of message it is
		if (msg instanceof Register) {
			//byte array for append the result byte
			byte result[] = new byte[2 + (ADDRESS_BIT + PORT_BIT)/Byte.SIZE];
			//append the version, code, message id to the result buffer
			result[0] = (byte)REGISTER_CODE;
			result[1] = (byte)msg.getMsgID();
			//the the address bytes from message 
			byte[] addr = msg.getAddress().getAddress();
			//validation for address
			if(addr.length != ADDRESS_BIT/Byte.SIZE) {
				throw new IllegalArgumentException
					("Register: invalid address(more than 4 byte)" + addr);
			}
			//counter for address
			int count = addr.length - 1;
			//append the address array to result buffer with little endian
			for(int i = 2; i < ADDRESS_BIT/Byte.SIZE + 2; i++) {
				result[i] = addr[count];
				count --;
			}
			//append the address array to result buffer with little endian
			int port = msg.getPort();
			//convert port integer to byte array 
			byte[] portbuffer = intToByte(port);
			//append the port buffer with little endian 
			result[ADDRESS_BIT/Byte.SIZE + 2] = portbuffer[3];
			result[ADDRESS_BIT/Byte.SIZE + 3] = portbuffer[2];
			//finish appending the buffer
			return result;
		}else if (msg instanceof Addition) {
			//name buffer is ready to get the name 
			byte nameBuffer[] = null;
			try {
				//convert name String into byte array in utf-8
				nameBuffer = msg.getName().getBytes("UTF8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//get the length of the name byte array 
			int len = nameBuffer.length;
			// now we know the total byte of encoded message
			int resLen = 2 + len + 1
					+ (LENGTH_BIT+MEALTYPE_BIT+CALORIES_BIT)/Byte.SIZE;
			//result buffer is ready for append 
			byte result[] = new byte[resLen];
			//append version, code, message and name length to the result
			result[0] = (byte)ADDITION_CODE;
			result[1] = (byte)msg.getMsgID();
			result[2] = (byte)len;
			//create a offset of current index in the result array
			int offset = 2 + LENGTH_BIT/Byte.SIZE;
			//counter for name buffer
			int count = 0;
			//append the name byte to the result
			for(int i = offset; i < nameBuffer.length + offset; i++) {
				result[i] = nameBuffer[count];
				count ++;
			}
			//update the offset to current index
			offset = nameBuffer.length + offset;
			//convert the meal type and append to the result buffer
			result[offset] = (byte)msg.getMealType().getMealTypeCode();
			//update the offset
			offset ++;
			//append a zero delimiter
			result[offset] = (byte)0;
			offset ++;
			//update the offset
			
			//get the calories from the message
			int calories = msg.getCalories();
			//convert the calories to byte array
			byte[] calbuffer = intToByte(calories);
			//store the calories to the result by little endian
			result[offset] = calbuffer[3];
			result[offset+1] = calbuffer[2];
			//finish appending the buffer
			return result;
		}else if (msg instanceof foop.serialization.Error) {
			//message buffer is ready to get the message 
			byte messageBuffer[] = null;
			try {
				//convert message String into message array in utf-8
				messageBuffer = msg.getMessage().getBytes("UTF8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//result buffer is ready for append 
			byte result[] = new byte[messageBuffer.length + 2];
			//append version, code and message to the result
			result[0] = (byte)ERROR_CODE;
			result[1] = (byte)msg.getMsgID();
			
			//append the message to result
			int count = 0;
			for(int i = 2; i < messageBuffer.length + 2; i++) {
				result[i] = messageBuffer[count];
				count ++;
			}
			//finish appending the buffer
			return result;
			
		}else if (msg instanceof ACK) {
			//result buffer is ready for append 
			byte result[] = new byte[2];
			//append version, code and message to the result
			result[0] = (byte)ACK_CODE;
			result[1] = (byte)msg.getMsgID();
			//finish appending the buffer
			return result;
		}
		//receive a invalid message
		throw new IllegalArgumentException("invalid message type " + msg);
	}
	/**
	 * convert int to a size 4 byte array  
	 * @param a int to be converted
	 * @return byte array
	 */
	public static final byte[] intToByte(int a) {
	    return new byte[] { (byte)(a >>> 24), (byte)(a >>> 16),
	    			(byte)(a >>> 8),(byte) a};
	}
	
}
