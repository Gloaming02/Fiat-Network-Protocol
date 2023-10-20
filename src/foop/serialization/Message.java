
/************************************************
*
* Author: Zhengyan Hu
* Assignment: FOOP
* Class: CSI4321
*
************************************************/

package foop.serialization;

import java.net.Inet4Address;


import java.util.Objects;
import java.util.regex.Pattern;

import fiat.serialization.MealType;
import java.net.InetSocketAddress;

/** 
 * Represents abstract Message packet
 * @author Zhengyan Hu
 * @version 3.0
 * @since 1.0
 */
public abstract class Message {
	/**
	 * represents Maximum of the a string length and integer
	 */
	public static final int MAX_MSGID = 255;
	/**
	 * represents regular expression of punctuation
	 */
	static final String PUNCTUATION  = "\\p{P}";
	/**
	 * represents regular expression of symbol
	 */
	static final String SYMBOL  = "\\p{S}";
	
	/**
	 * represents regular expression of digital or numeric
	 */
	static final String LETTER_OR_NUMERIC = "[\\p{L}\\p{N}]";
	
	/**
	 * represents maximum error message length
	 */
	static final int MAX_ERROR_LEN = 65505;
	
	/**
	 * represents regular expression of tab
	 */
	static final char TAB  = '\t';
	/**
	 * represents regular expression of tab
	 */
	static final int MAX_NAME_LEN  = 255;

	/**
	 * represents regular expression of tab
	 */
	static final int MAX_MESSAGE_LEN  = 255;
	
	/**
	 * represents regular expression of tab
	 */
	static final int MAX_PORT  = 65535;
	/**
	 * represents regular expression of tab
	 */
	static final int MAX_ADDRESS  = 4;
	
	/**
	 * the packet id store as msgID
	 */
	protected int msgID;
	
	/**
	 * get message ID
	 * @return message ID
	 */
	public int getMsgID() {
		return msgID;
	}
	/**
	 * Set message ID
	 * @param msgID message ID
	 * @return this object with new value
	 * @throws IllegalArgumentException 
	 * 		if message ID is out of range (see serialization spec)
	 */
	public Message setMsgID(int msgID) {
		if(!(checkMsgID(msgID))) {
			throw new IllegalArgumentException("invalid message ID " + msgID);
		}
		this.msgID = msgID;
		return this;
	}

	/**
	 * validation for non empty string
	 * @param message string to be check
	 * @return the message
	 */
	public static boolean checkName(String message) {
		//check for empty and null value
		if (message == null || message == "" || message.length() > MAX_NAME_LEN) {
			return false;
		}

		for(int i = 0; i < message.length(); i ++) {
			if ((!Pattern.matches(PUNCTUATION, Character.toString(message.charAt(i)))
					&& !Pattern.matches(LETTER_OR_NUMERIC,Character.toString(message.charAt(i)))
					&& message.charAt(i) != ' '
					&& !Pattern.matches(SYMBOL, Character.toString(message.charAt(i))))
					|| message.charAt(i) == TAB) {
				return false;
			}
		}
		return true;
		
	}
	
	
	/**
	 * validation for non empty string
	 * @param message string to be check
	 * @return the message
	 */
	public static boolean checkErrorMessage(String message) {
		//check for empty and null value
		if (message == null || message.length() > MAX_ERROR_LEN) {
			return false;
		}
		for(int i = 0; i < message.length(); i ++) {
			if ((!Pattern.matches(PUNCTUATION, Character.toString(message.charAt(i)))
					&& !Pattern.matches(LETTER_OR_NUMERIC,Character.toString(message.charAt(i)))
					&& message.charAt(i) != ' '
					&& !Pattern.matches(SYMBOL, Character.toString(message.charAt(i))))
					|| message.charAt(i) == TAB) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * validation for message id
	 * @param c message id
	 * @return boolean of validation fail or not
	 */
	public static boolean checkMsgID(int c) {
		return !(c > MAX_MSGID || c < 0);
	}
	/**
	 * validation for port
	 * @param c port
	 * @return boolean of validation fail or not
	 */
	public static boolean checkPort(int c) {
		return !(c > MAX_PORT || c < 0);
	}
	/**
	 * validation for address
	 * @param c Inet4Address
	 * @return boolean of validation fail or not
	 */
	public static boolean checkAddress(Inet4Address c) {
		//address cannot be null or Multicast Address
		if(c == null || c.isMulticastAddress()) {
			return false;
		}
		return !(c.getAddress().length > MAX_ADDRESS || c.getAddress().length < 0);
	}

	/**
	 * Returns a hash code value for the object. 
	 * @return a hash code value for this object.
	 * 
	 */
	@Override
	public int hashCode() {
		return Objects.hash(msgID);
	}
	/**
	 * compare two item if they are equals
	 * @param obj - the reference Add with which to compare.
	 * @return true if this Add is the same as the obj argument; false otherwise.
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		return msgID == other.msgID;
	}
	/**
	 * subclass getName
	 * @return String
	 */
	public String getName() {
		return null;
	}
	/**
	 * subclass setName
	 * @param name string name
	 * @return Addition
	 */
	public Addition setName(String name) {
		return null;
	}
	/**
	 * subclass getMealType
	 * @return MealType
	 */
	public MealType getMealType(){
		return null;
	}
	/**
	 * subclass setMealType
	 * @param mealType MealType
	 * @return Addition
	 */
	public Addition setMealType(MealType mealType) {
		return null;
	}
	/**
	 * subclass getCalories
	 * @return integer
	 */
	public int getCalories() {
		return 0;
	}
	/**
	 * subclass setCalories
	 * @param calories integer calories
	 * @return Addition
	 */
	public Addition setCalories(int calories) {
		return null;
	}
	/**
	 * subclass setMessage
	 * @param message string of message
	 * @return Error
	 */
	public Error setMessage(String message) {
		return null;
	}
	/**
	 * subclass getMessage
	 * @return String
	 */
	public String getMessage() {
		return null;
	}
	/**
	 * subclass getAddress
	 * @return Inet4Address
	 */
	public Inet4Address getAddress() {
		return null;
	}
	/**
	 * subclass setAddress
	 * @param address Inet4Address
	 * @return Register
	 */
	public Register setAddress(Inet4Address address) {
		return null;
	}
	/**
	 * subclass getPort
	 * @return int
	 */
	public int getPort() {
		return 0;
	}
	/**
	 * subclass setPort
	 * @param port port
	 * @return Register
	 */
	public Register setPort(int port) {
		return null;
	}
	/**
	 * subclass getSocketAddress
	 * @return InetSocketAddress
	 */
	public InetSocketAddress getSocketAddress() {
		return null;
	}
}
