/************************************************
*
* Author: Zhengyan Hu
* Assignment: Fiat Food IntAke Tracker
* Class: CSI4321
*
************************************************/

package fiat.serialization;


import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/** 
 * Serialization output source
 * @author Zhengyan Hu
 * @version 1.0
 * @since 1.0
*/
public class MessageOutput {
	/**
	 * output stream
	 */
	private OutputStream output;
	/**
	 * string of stream
	 */
	private String encoding = "";
	
	/**
	* Constructs a new output source from an OutputStream
	*
	* @param out byte input source
	* 
	* @throws NullPointerException 
	* if in is null
	*/
	public MessageOutput(OutputStream out) {
		if (out == null) {
			throw new NullPointerException("Null input stream");
		}
		this.output = out;
	}
	
	/**
	* return outputSteam
	* 
	* @return output the internal outputSteam
	*/
	public OutputStream getOutputStream() {
		return output;
	}
	
	/**
	* Sets outputSteam
	* 
	* @param out the new outputSteam
	*/
	public void setOutputStream(OutputStream out) {
		if (out == null) {
			throw new NullPointerException("Null input stream");
		}
		this.output = out;
	}
	
	/**
	* write the string to the outputStream
	* 
	* @param str the new outputSteam
	* @throws IOException if I/O problem
	*/
	public void writeMessage(String str) throws IOException {
		encoding += str;
		output.write(str.getBytes(Charset.forName("UTF-8")));
	}

	/**
	* return the string type of OutputStream
	* 
	* @return encode string 
	* @throws IOException if I/O problem
	*/
	public String getEncoding() throws IOException {
		return encoding;
	}
	
}
