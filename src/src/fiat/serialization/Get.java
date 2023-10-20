/************************************************
*
* Author: Zhengyan Hu
* Assignment: Fiat Food IntAke Tracker
* Class: CSI4321
*
************************************************/

package fiat.serialization;

/** 
 * Represents a Get and provides serialization/deserialization
 * @author Zhengyan Hu
 * @version 1.0
 * @since 1.0
 */
public class Get extends Message{
	
	/**
	 * Request string for get
	 */
	public static final String REQUEST_GET = "GET";  
	
	/**
	* Constructs Get using set values
	*
	* @param messageTimestamp message timestamp
	* @throws IllegalArgumentException if validation fails
	*/
	public Get(long messageTimestamp) throws IllegalArgumentException{
		if (!checkTimeStamp(messageTimestamp)) {
			throw new IllegalArgumentException("invalid times stamp value: " + messageTimestamp);
		}
		this.timestamp = messageTimestamp;
	}
	
	/**
	 * Returns string of the form
	 * GET (TS=&lt;ts&gt;)
	 * For example
	 * GET (TS=5)
	 * @return string of this object
	 */
	@Override
	public String toString() {
		return "GET (TS=" + timestamp + ")";
	}
	
	/**
	 * Returns request (e.g., ADD)
	 * @return request
	 */
	public String getRequest() {
		return REQUEST_GET;
	}
	
	/**
	 * Returns a hash code value for the object. 
	 * @return a hash code value for this object.
	 * 
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	/**
	 * compare two item if they are equals
	 * @param obj - the reference Get with which to compare.
	 * @return true if this Get is the same as the obj argument; false otherwise.
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
		return true;
	}
	
	
}
