/************************************************
*
* Author: Zhengyan Hu
* Assignment: FOOP
* Class: CSI4321
*
************************************************/

package foop.serialization;

/** 
 * Represents ACK packet
 * @author Zhengyan Hu
 * @version 3.0
 * @since 1.0
 */
public class ACK extends Message{
	/**
	 * Constructs from given values
	 *
	 * @param msgID message ID
	 * @throws IllegalArgumentException if validation fails
	 */
	public ACK(int msgID) {
		if(!(checkMsgID(msgID))) {
			throw new IllegalArgumentException("invalid message ID " + msgID);
		}
		this.msgID = msgID;
	}
	
	/**
	 * Returns string of the form
	 * ACK: MsgID=msgid
	 * @return string of the form
	 */
	@Override
	public String toString() {
		return "ACK: MsgID=" + msgID;	
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
		return true;
	}
	
	
}
