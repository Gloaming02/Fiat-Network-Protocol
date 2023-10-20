/************************************************
*
* Author: Zhengyan Hu
* Assignment: Fiat Food IntAke Tracker
* Class: CSI4321
*
************************************************/

package fiat.serialization.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import fiat.serialization.MessageOutput;

public class MessageOutputTest {
	/*
	 * Unit Test for constructor
	 */
	@Test
	void testMessageOutputBasic() throws IOException {
		ByteArrayOutputStream a = 
				new ByteArrayOutputStream();
		a.write(8000);
		MessageOutput message = new MessageOutput(a);
		
		assertEquals(a, message.getOutputStream());
	}
	
	/*
	 * Unit Test for constructor with null pass in
	 */
	@Test
	void testMessageOutputNull() throws IOException {
	    assertThrows(NullPointerException.class, () -> {
	    	new MessageOutput(null);
	    });	
	}
	
	
	/*
	 * Unit Test for writeMessage and getEncoding
	 */
	@Test
	void testWriteMessageAndEncoding() throws IOException {
		MessageOutput message = new MessageOutput(new ByteArrayOutputStream());
		message.writeMessage("5 FriesB512 5.6 ");
		assertEquals("5 FriesB512 5.6 ", message.getEncoding());
		message.writeMessage("A FriesZ512 5.6 ");
		assertEquals("5 FriesB512 5.6 A FriesZ512 5.6 ", message.getEncoding());

	}
	
	/*
	 * Unit Test for writeMessage with null pass in
	 */
	@Test
	void testWriteMessageNull() throws IOException {
		MessageOutput message = new MessageOutput(new ByteArrayOutputStream());

	    assertThrows(NullPointerException.class, () -> {
			message.writeMessage(null);
	    });	
	}
	
	/*
	 * Unit Test for set output stream
	 */
	@Test
	void testSetOutputStream() throws IOException {
		ByteArrayOutputStream a = 
				new ByteArrayOutputStream();
		a.write(8000);
		MessageOutput message = new MessageOutput(new ByteArrayOutputStream());
		message.setOutputStream(a);
		
		assertEquals(a, message.getOutputStream());
	}
	
	/*
	 * Unit Test for set output stream to null
	 */
	@Test
	void testSetOutputStreamNull() throws IOException {
		MessageOutput message = new MessageOutput(new ByteArrayOutputStream());
	    assertThrows(NullPointerException.class, () -> {
			message.setOutputStream(null);
	    });		
	}
}
