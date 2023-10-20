/************************************************
*
* Author: Zhengyan Hu
* Assignment: FOOP
* Class: CSI4321
*
************************************************/

package foop.serialization;


import java.nio.charset.StandardCharsets;
import java.util.Objects;
/** 
 * Represents Error packet
 * @author Zhengyan Hu
 * @version 3.0
 * @since 1.0
 */
public class Error extends Message{
	/**
	 * store error message for packet
	 *
	 */
	private String errorMessage;
	
	/**
	 * Constructs from given values
	 *
	 * @param msgID message ID
	 * @param errorMessage error message
	 * @throws IllegalArgumentException if validation fails
	 */
	public Error(int msgID, String errorMessage) {
		if(!(checkMsgID(msgID))) {
			throw new IllegalArgumentException("invalid message ID " + msgID);
		}
		if(!(checkErrorMessage(errorMessage))) {
			throw new IllegalArgumentException("invalid error message " + errorMessage);
		}
		this.msgID = msgID;
		this.errorMessage = errorMessage;
	}
	/**
	 * Returns string of the form
	 * Error: MsgID=msgid Message=message
	 * 
	 * @return string of the form
	 */
	@Override
	public String toString() {
		return "Error: MsgID=" + msgID + " Message=" + errorMessage;
	}
	/**
	 * Set error message
	 * @param message error message
	 * @return this object with new value
	 * @throws IllegalArgumentException if validation fails，message is null, etc.
	 */
	public Error setMessage(String message) {
		if(!(checkErrorMessage(message))) {
//		    byte[] a = null;
//			try {
//				a = message.getBytes("UTF8");
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			String str = "";
//		    for(int i=0; i< a.length ; i++) {
//		    	str += (a[i] + " ");
//		    }			
		    throw new IllegalArgumentException("invalid error message " + message);
		}
		errorMessage = message;
		return this;
	}
	/**
	 * get error message
	 * @return error message
	 */
	public String getMessage() {
		return errorMessage;
	}
	
	/**
	 * Returns a hash code value for the object. 
	 * @return a hash code value for this object.
	 * 
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(errorMessage);
		return result;
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
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Error other = (Error) obj;
		return Objects.equals(errorMessage, other.errorMessage);
	}
	
	public static void main(String args[]) {
	      byte[] a = {-30,-72,-95,109,101};
	      System.out.println(new String(a, StandardCharsets.UTF_8));

	}
}
