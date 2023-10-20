/************************************************
*
* Author: Zhengyan Hu

* Assignment: Fiat Food IntAke Tracker
* Class: CSI4321
*
************************************************/

package fiat.serialization;

import java.util.List;
import java.util.Objects;

/** 
 * Represents generic portion of message
 * 
 * @author Zhengyan Hu
 * @version 1.0
 * @since 1.0
*/
public abstract class Message {

	
	/**
	 * represents maximum value of time stamp
	 */
	public static final long MAX_OF_TIMESTAMP = (long) (Math.pow(2, 63)-1);  
	
	/**
	 * represents Maximum of the integer
	 */
	static final int MAXIMUN_INTEGER =2048;
	
	/**
	 * represents time stamp
	 */
	protected long timestamp;

	/**
	 * subclass getItem
	 * @return item
	 */
	public Item getItem(){
		return null;
	}
	/**
	 * subclass setItem
	 * @param item item to be set
	 * @return Add
	 */
	public Add setItem(Item item){
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
	 * subclass setMessage
	 * @return Error
	 */
	public Error setMessage() {
		return null;
	}
	/**
	 * subclass addItem
	 * @param item item to add
	 * @return ItemList
	 */
	public ItemList addItem(Item item) {
		return null;
	}
	/**
	 * subclass getItemList
	 * @return List of item
	 */
	public List<Item> getItemList() {
		return null;
	}
	/**
	 * subclass getModifiedTimestamp
	 * @return Modified Time stamp
	 */
	public long getModifiedTimestamp() {
		return -1;
	}
	/**
	 * subclass setModifiedTimestamp
	 * @param modifiedTimestamp Timestamp of modified time
	 * @return ItemList
	 */
	public ItemList setModifiedTimestamp(long modifiedTimestamp) {
		return null;
	}

	/**
	 * subclass getTimestamp
	 * @return time stamp
	 */
	public long getTimestamp() {
		return timestamp;
	}
	
	/**
	 * subclass setIntervalTime
	 * @param int time for interval
	 * @return Interval
	 */
	public Interval setIntervalTime(int intervalTime) {
		return null;
	}
	
	/**
	 * subclass getIntervalTime
	 * @return long
	 */
	public int getIntervalTime() {
		return -1;
	}

	/**
	 * Sets times tamp
	 * @param timestamp time stamp
	 * @throws IllegalArgumentException if validation fails
	 * 
	 * @return obj of message
	 */
	public Message setTimestamp(long timestamp) {
		if (!checkTimeStamp(timestamp)) {
			throw new IllegalArgumentException("invalid times stamp value: " + timestamp);
		}
		this.timestamp = timestamp;
		return this;
	}
	
	/**
	 * Returns request (e.g., ADD)
	 * @return request
	 */
	public String getRequest() {
		return null;
	}
	
	/**
	 * validation for timestamp
	 * @return request
	 */
	public static boolean checkTimeStamp(long messageTimestamp) {
		return !(messageTimestamp < 0 || messageTimestamp > MAX_OF_TIMESTAMP);
	}
	

	/**
	 * Returns a hash code value for the object. 
	 * @return a hash code value for this object.
	 * 
	 */
	@Override
	public int hashCode() {
		return Objects.hash(timestamp);
	}

	/**
	 * compare two item if they are equals
	 * @param obj - the reference Message with which to compare.
	 * @return true if this Message is the same as the obj argument; false otherwise.
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
		return timestamp == other.timestamp;
	}
	
}
