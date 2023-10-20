/************************************************
*
* Author: Zhengyan Hu
* Assignment: Fiat Food IntAke Tracker
* Class: CSI4321
*
************************************************/


package fiat.serialization;

/** 
 * Exception indicating the token and location where the problem occurred
 * @author Zhengyan Hu
 * @version 1.0
 * @since 1.0
*/
public class TokenizerException extends Exception{
	private static final long serialVersionUID = 1L;

	/**
	 * offset starts at 0 and must be within the 
	 * token or trailing delimiter where the problem occurred.
	 */
	int offset;
	
	/**
	* Constructs validation exception
	*
	* @param message exception message
	* @param cause exception cause
	* @param offset in bytes of tokenizer problem
	* 
	* @throws IllegalArgumentException
	* 	if offset is negative or message is null 
	*/
	public TokenizerException(int offset, String message, Throwable cause){
        super(message, cause);
		if (offset < 0) {
			throw new IllegalArgumentException("Offset cannot be negative");
		}
		if(message == null) {
			throw new IllegalArgumentException("Message cannot be null");
		}
        this.offset = offset;
	}
	
	/**
	* Constructs validation exception with null cause
	*
	* @param message exception message
	* @param offset in bytes of tokenizer problem
	* 
	* @throws IllegalArgumentException
	* 	if offset is negative or message is null 
	*/
	public TokenizerException(int offset, String message){
        super(message);
        
		if (offset < 0) {
			throw new IllegalArgumentException("Offset cannot be negative");
		}
		if(message == null) {
			throw new IllegalArgumentException("Message cannot be null");
		}
        this.offset = offset;
        
	}
	
	/**
	* Get offset in bytes from start of stream for token that failed validation
	*
	* 
	* @return offset in bytes from start of stream
	* 
	*/
	public int getOffset() {
		return offset;
	}


}
