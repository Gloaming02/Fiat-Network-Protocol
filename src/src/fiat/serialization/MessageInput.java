/************************************************
*
* Author: Zhengyan Hu
* Assignment: Fiat Food IntAke Tracker
* Class: CSI4321
*
************************************************/

package fiat.serialization;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.io.InputStream;


/** 
 * Deserialization input source
 * @author Zhengyan Hu
 * @version 1.0
 * @since 1.0
*/
public class MessageInput {
	/**
	 * Input stream
	 */
	private InputStream in;
	
	static final int BYTE_ARRAY_SIZE = 4096;  

	/**
	* Constructs a new input source from an InputStream 
	*
	* @param in byte input source
	* 
	* @throws NullPointerException 
	* if in is null
	*/
	public MessageInput(InputStream in){
		// NullPointerException - if in is null
		if (in == null) {
			throw new NullPointerException("Null input stream");
		}
		this.in = in;
	}
	
	/**
	* Returns InputStream
	*
	* @return in
	*/
	public InputStream getInputStream() {
		return in;
	}
	
	/**
	* Set InputStream
	*
	* @param in Input stream will be to read
	* 
	* @throws NullPointerException 
	* if in is null
	*/
	public void setInputStream(InputStream in) {
		//NullPointerException - if in is null
		if (in == null) {
			throw new NullPointerException("Null input stream");
		}
		this.in = in;
	}
	
	/**
	* read character count of name from the input stream
	* 
	* @return the character count
	* @throws TokenizerException
	* if unexpected end of steam 
	*/
	public String readBySpace() throws TokenizerException {
		//record of input stream
		String result = "";
		//read a byte from stream
		byte b = -1;
		try {
			b = (byte)in.read();
	        while (b != -1) {
	    		//convert the byte we read to char
	        	char c = (char) b;  
	        	// return the string once we reach a space
	        	if(c == ' ') {
	        		return result;
	        	}
	        	//append char we read to result
	        	result += c; 
	        	
	        	//read next byte and loop back
				b = (byte)in.read();

	        }
		} catch (IOException e) {

		}
		// while not end of stream
        //A formatted message shouldn't reach the end of file.
		throw new TokenizerException(result.length(), "wrong packet type");

	}
	
	/**
	* read n character from input stream
	* 
	* @param n number of char it will read
	* @return n-character string from input stream
	* @throws TokenizerException
	* if unexpected end of steam
	*/
	public String readByChar(int n) throws TokenizerException {
		//assign byte with max size
		byte[] b = new byte[BYTE_ARRAY_SIZE];
		//initialize offset byte array
		int offset = 0;
		//number of byte be read
		int count = 0;
		//String for return
		String str = "";
		//if the length of string not reach n
		// no reach the end of stream
		while(str.length() < n && count != -1) {
			try {
				//read n byte from input
				count = in.read(b, offset, n - str.length());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//not reach end of stream
			if(count != -1) {
				//update the offset of byte array
				offset += count;				
			}
			//get rid of the unused byte in byte array
			ByteBuffer bb = ByteBuffer.wrap(b);
			byte[] result = new byte[offset];
			bb.get(result, 0, offset);
			
			//update the result string and loop back to check if the length is reach n
			str = new String(result, Charset.forName("utf-8"));
		}
		//early end of stream
		if(count == -1) {
			throw new TokenizerException(str.length(), "wrong packet type");
		}
		//return the final result string 
		return str;
	}

	/**
	* read next byte
	* 
	* @return a byte in char
	* @throws TokenizerException
	* if unexpected end of steam
	*/
	public char readNext() throws TokenizerException {
		//initialize result
		int result = -1;
		try {
			//read one byte
			result = in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//read fail
		if(result == -1) {
			throw new TokenizerException(1, "wrong packet type");
		}
		return (char)(result);
	}
	


}
