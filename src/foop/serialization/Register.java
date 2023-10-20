/************************************************
*
* Author: Zhengyan Hu
* Assignment: FOOP
* Class: CSI4321
*
************************************************/

package foop.serialization;

import java.net.Inet4Address;

import java.net.InetSocketAddress;
import java.util.Objects;
/** 
 * Represents a client registration
 * @author Zhengyan Hu
 * @version 3.0
 * @since 1.0
 */
public class Register extends Message{
	/** 
	 * store the client address
	 */
	private Inet4Address address;
	/** 
	 * store the port of client address 
	 */
	private int port;
	/**
	 * Constructs register message
	 *
	 * @param msgID message ID
	 * @param address address to register
	 * @param port port to register
	 * 
	 * @throws IllegalArgumentException if validation fails
	 */
	public Register(int msgID, Inet4Address address, int port) {
		if(!(checkMsgID(msgID))) {
			throw new IllegalArgumentException("invalid message ID " + msgID);
		}
		if(!(checkPort(port))) {
			throw new IllegalArgumentException("invalid port " + port);
		}

		if(!(checkAddress(address))) {
			throw new IllegalArgumentException("invalid address " + address);
		}
		this.msgID = msgID;
		this.address = address;
		this.port = port;
	}
	
	/**
	 * Returns string of the form
	 * Register: MsgID=msgid Address=address Port=port
	 *
	 * @return string of the object 
	 */
	@Override
	public String toString() {
		return "Register: MsgID="+ msgID +" Address="+address.getHostAddress()+" Port=" + port;
	}
	/**
	 * Get register address
	 * @return register address
	 */
	public Inet4Address getAddress() {
		return address;
	}
	/**
	 * Set register address
	 * @param address register address
	 * @return this object with new value
	 * @throws IllegalArgumentException if validation fails
	 */
	public Register setAddress(Inet4Address address) throws IllegalArgumentException{
		if(!(checkAddress(address))) {
			throw new IllegalArgumentException("invalid address " + address);
		}
		this.address = address;
		return this;
	}
	
	/**
	 * Get port address
	 * @return port address
	 */
	public int getPort() {
		return port;
	}
	/**
	 * Set port address
	 * @param port registration port
	 * @return this object with new value
	 * @throws IllegalArgumentException if validation fails
	 */
	public Register setPort(int port) {
		if(!(checkPort(port))) {
			throw new IllegalArgumentException("invalid port " + port);
		}
		this.port = port;
		return this;
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
		result = prime * result + Objects.hash(address, port);
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
		Register other = (Register) obj;
		return Objects.equals(address, other.address) && port == other.port;
	}

	/**
	 * Get the register socket address
	 * @return register socket address
	 */
	public InetSocketAddress getSocketAddress() {
		return new InetSocketAddress(address, port);
	}

}
