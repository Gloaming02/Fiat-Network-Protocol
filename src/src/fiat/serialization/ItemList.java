/************************************************
*
* Author: Zhengyan Hu
* Assignment: Fiat Food IntAke Tracker
* Class: CSI4321
*
************************************************/

package fiat.serialization;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** 
 * Represents a List of items and provides serialization/deserialization Note: 
 * The insertion order of items MUST be preserved by getItemList() and serialization.
 * 
 * @author Zhengyan Hu
 * @version 1.0
 * @since 1.0
*/
public class ItemList extends Message{
	/**
	 * Request string for list
	 */
	public static final String REQUEST_LIST = "LIST";  
	
	/**
	 * last modified time stamp of item list
	 */
	private long modifiedTimestamp = 0;
	/**
	 * list of items
	 */
	private List<Item> itemList = new ArrayList<Item>();
	
	/**
	* Constructs ItemList using set values
	*
	* @param messageTimestamp message time stamp
	* @param modifiedTimestamp time stamp of last modification
	* @throws IllegalArgumentException if validation fails
	*/
	public ItemList(long messageTimestamp, long modifiedTimestamp) throws IllegalArgumentException{
		super();
		if (!checkTimeStamp(messageTimestamp)) {
			throw new IllegalArgumentException(
					"invalid message times stamp value: " + messageTimestamp);
		}
		if (!checkTimeStamp(modifiedTimestamp)) {
			throw new IllegalArgumentException(
					"invalid modified times stamp value: " + modifiedTimestamp);
		}
		
		this.timestamp = messageTimestamp;
		this.modifiedTimestamp = modifiedTimestamp;
	}
	
	/**
	* Returns string of the form
	* LIST (TS=&lt;ts&gt;) last mod=&lt;time&gt;, list={&lt;item0&gt;, &lt;item1&gt;...}<p>
	* For example<p>
	* LIST (TS=5) last mod=7, list={Plum with 5 calories and 6g of fat eaten at 
	* Breakfast,Bacon with 50 calories and 60g of fat eaten at Breakfast}
	* @return String of class
	*/
	@Override
	public String toString() {
		String result = "LIST (TS="+timestamp+") last mod="+modifiedTimestamp+", list={";
		for(Item i : itemList) {
			result += i.toString();
			result += ",";
		}
		result = result.substring(0, result.length() - 1);
		return result + "}";
	}
	
	/**
	* Adds item
	* @param item new item to add
	* @return this object with new value
	* @throws IllegalArgumentException if null item
	*/
	public ItemList addItem(Item item) throws IllegalArgumentException{
		if(item == null) {
			throw new IllegalArgumentException("null item" + item);
		}
		itemList.add(item);
		return this;
	}
	
	/**
	* Returns list of items
	* @return list of items
	*/
	public List<Item> getItemList(){
		return itemList;
	}
	
	/**
	 * Returns modified time stamp
	 * @return the modifiedTimestamp
	 */
	public long getModifiedTimestamp() {
		return modifiedTimestamp;
	}
	
	/**
	 * Set modified time stamp
	 * @param modifiedTimestamp modification time stamp
	 * @return this object with new value
	 * @throws IllegalArgumentException if validation fails
	 */
	public ItemList setModifiedTimestamp(long modifiedTimestamp) throws IllegalArgumentException{
		if (!checkTimeStamp(modifiedTimestamp)) {
			throw new IllegalArgumentException(
					"invalid modified times stamp value: " + modifiedTimestamp);
		}
		
		this.modifiedTimestamp = modifiedTimestamp;
		return this;
	}
	/**
	 * Returns request (e.g., ADD)
	 * @return request
	 */
	public String getRequest() {
		return REQUEST_LIST;
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
		result = prime * result + Objects.hash(itemList, modifiedTimestamp);
		return result;
	}

	/**
	 * compare two item if they are equals
	 * @param obj - the reference ItemList with which to compare.
	 * @return true if this ItemList is the same as the obj argument; false otherwise.
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
		ItemList other = (ItemList) obj;
		return Objects.equals(itemList, other.itemList) && modifiedTimestamp == other.modifiedTimestamp;
	}


}
