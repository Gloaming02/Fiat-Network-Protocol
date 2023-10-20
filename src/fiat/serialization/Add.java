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
 * Represents an Add and provides serialization/deserialization
 * @author Zhengyan Hu
 * @version 1.0
 * @since 1.0
 */
public class Add extends Message{
	/**
	 * Request string for add
	 */
	public static final String REQUEST_ADD = "ADD";   
	
	/**
	 * stored a item for addition
	 */
	private Item item;
	
	
	/**
	* Constructs new Add using attribute values
	*
	* @param messageTimestamp message timestamp
	* @param item new item
	* @throws IllegalArgumentException if validation fails (e.g., item null)
	*/
	public Add(long messageTimestamp, Item item) throws IllegalArgumentException{
		//check time stamp in range 0 < time stamp < max
		if (!checkTimeStamp(messageTimestamp)) {
			throw new IllegalArgumentException("invalid times stamp value: " + messageTimestamp);
		}
		// check null item
		if (item == null) {
			throw new IllegalArgumentException("item cannot be null");
		}
		this.timestamp = messageTimestamp;
		this.item = item;
	}

	/**
	 * Returns item
	 * @return item to add
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * Sets item
	 * @param item new item
	 * @return this object with new value
	 */
	public Add setItem(Item item) {
		if (item == null) {
			throw new IllegalArgumentException("item cannot be null" + item);
		}
		this.item = item;
		return this;
	}

	/**
	 * Returns string of the form
	 * ADD (TS=&lt;ts&gt;) item=&lt;item&gt;
	 * For example
	 * ADD (TS=5) item=Plum with 5 calories and 6g of fat eaten at Breakfast
	 * @return string of this object
	 */
	@Override
	public String toString() {
		return "ADD (TS=" + timestamp 
				+ ") item=" + item.toString();
	}
	
	/**
	 * Returns request (e.g., ADD)
	 * @return request
	 */
	public String getRequest() {
		return REQUEST_ADD;
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
		result = prime * result + Objects.hash(item, timestamp);
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
		Add other = (Add) obj;
		return Objects.equals(item, other.item) && timestamp == other.timestamp;
	}
	
	
}
