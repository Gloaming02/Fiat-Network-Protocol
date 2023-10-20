/************************************************
*
* Author: Zhengyan Hu
* Assignment: Fiat Food IntAke Tracker
* Class: CSI4321
*
************************************************/


package fiat.serialization;


import java.nio.charset.StandardCharsets;
/** 
 * Non-blocking I/O deframer for message. 
 * The message delimiter is the given byte sequence
 * @author Zhengyan Hu
 * @version 1.0
 * @since 1.0
*/
public class MessageNIODeframer {
	/**
	 * Delimiter for the Fiat, follow protocol should be '\n'
	 */
	private byte[] delimiter;
	/**
	* Create message deframer with specified byte sequence delimiter
	*
	* @param delimiter bytes of message delimiter
	* 
	* @throws NullPointerException 
	* if delimiter is null
	*/
	public MessageNIODeframer(byte[] delimiter) {
		if(delimiter == null) {
			throw new NullPointerException("delimiter cannot be null");
		}
		this.delimiter = delimiter;
	}
	
	/**
	* Decode the next message (if available)
	*
	* @param buffer next bytes of message
	* @return deframed message or null if frame incomplete
	* 
	* @throws NullPointerException 
	* if buffer is null
	*/
	public byte[] nextMsg(byte[] buffer) {
		if(buffer == null) {
			throw new NullPointerException("buffer cannot be null");
		}
		String buff = new String(buffer, StandardCharsets.UTF_8);
		String delimit = new String(delimiter, StandardCharsets.UTF_8);
		if(!buff.contains(delimit)) {
			return null;
		}else {
			String splitter[] = buff.split(delimit, 2);
			return (splitter[0] + "\n").getBytes(StandardCharsets.UTF_8);
		}
	}

}
