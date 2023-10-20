/************************************************
*
* Author: Zhengyan Hu
* Assignment: Fiat Food IntAke Tracker
* Class: CSI4321
*
************************************************/

package fiat.serialization;

import java.util.Objects;


/** 
 * Represents an Error and provides serialization/deserialization
 * @author Zhengyan Hu
 * @version 1.0
 * @since 1.0
 */
public class Error extends Message{
	/**
	 * Request string for error
	 */
	public static final String REQUEST_ERROR = "ERROR";
	
	/**
	 * represents an error message
	 */
	private String message;
	/**
	 * represents an empty string
	 */
	static final String EMPTY_STRING = "";
	
	/**
	 * represents regular expression of punctuation
	 */
	static final String PUNCTUATION  = "\\p{P}";
	/**
	 * represents regular expression of symbol
	 */
	static final String SYMBOL  = "[\\p{P}";
	/**
	 * represents regular expression of tab
	 */
	static final char TAB  = '\t';


	/**
	* Constructs error message using set values
	*
	* @param messageTimestamp message time stamp
	* @param errorMessage error message
	*/
	public Error(long messageTimestamp, String errorMessage){
		//check time stamp in range 0 < time stamp < max
		if (!checkTimeStamp(messageTimestamp)) {
			throw new IllegalArgumentException("invalid times stamp value: " + messageTimestamp);
		}
		// validation for message
		if (!Item.CheckNonEmptyString(errorMessage)) {
			throw new IllegalArgumentException("invalid message " + errorMessage);
		}
		this.timestamp = messageTimestamp;
		this.message = errorMessage;
	}
	
	/**
	 * Return error message
	 * @return error message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Set error message
	 * @param message new error message
	 * @return this object with new value
	 * @throws IllegalArgumentException if invalid error message
	 */
	public Error setMessage(String message) {
		if (!Item.CheckNonEmptyString(message)) {
			throw new IllegalArgumentException("error message cannot be null or empty " + message);
		}
		this.message = message;
		return this;
	}

	/**
	 * Returns string of the form
	 * ERROR (TS=&lt;ts&gt;) message=&lt;message&gt;
	 * For example
	 * ERROR (TS=5) message=Server on fyre
	 * @return string of this object
	 */
	@Override
	public String toString() {
		return "ERROR (TS=" + timestamp 
				+ ") message=" + message;
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
		result = prime * result + Objects.hash(message);
		return result;
	}
	
	/**
	 * Returns request (e.g., ADD)
	 * @return request
	 */
	public String getRequest() {
		return REQUEST_ERROR;
	}

	/**
	 * compare two item if they are equals
	 * @param obj - the reference Error with which to compare.
	 * @return true if this Error is the same as the obj argument; false otherwise.
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
		return Objects.equals(message, other.message);
	}
	
}
