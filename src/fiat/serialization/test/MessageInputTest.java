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

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import fiat.serialization.MessageInput;
import fiat.serialization.TokenizerException;

public class MessageInputTest {
	
	/*
	 * Unit Test for constructor
	 */
	@Test
	void testMessageInputBasic() throws IOException, TokenizerException {
		ByteArrayInputStream a = 
				new ByteArrayInputStream("5 FriesB512 5.6 ".getBytes("UTF8"));
		MessageInput message = new MessageInput(a);
		assertEquals(message.getInputStream(),a);
		
		assertEquals("5",message.readBySpace());
		assertEquals("Fries",message.readByChar(5));
		}
	
	/*
	 * Unit Test for passing null to constructor
	 */
	@Test
	void testMessageInputNull() throws IOException {
	    assertThrows(NullPointerException.class, () -> {
	    	new MessageInput(null);
	    });	
	}
	
	/*
	 * Unit Test for set input steam method
	 * 
	 */
	@Test
	void testSetInputStream() throws IOException {
		ByteArrayInputStream a = 
				new ByteArrayInputStream("5 FriesB512 5.6 6 BurgerL1000 10.2 ".getBytes("UTF8"));
		MessageInput message = new MessageInput(a);
		ByteArrayInputStream b = 
				new ByteArrayInputStream("abcdefg".getBytes("UTF8"));
		message.setInputStream(b);
		assertEquals(message.getInputStream(),b);
	}
	

	/*
	 * Unit Test for setting input steam method to null
	 * 
	 */
	@Test
	void testSetInputStreamNull() throws IOException {
		ByteArrayInputStream a = 
				new ByteArrayInputStream("5 FriesB512 5.6 6 BurgerL1000 10.2 ".getBytes("UTF8"));
		MessageInput message = new MessageInput(a);
		
	    assertThrows(NullPointerException.class, () -> {
	    	message.setInputStream(null);
	    });	
	}
	
	/*
	 * Unit Test for read character count from message
	 * method will read until it reach a space
	 */
	@Test
	void testReadBySpace() throws IOException ,TokenizerException{
		ByteArrayInputStream a = 
				new ByteArrayInputStream("5 FriesB512 5.6 6 BurgerL1000 10.2 ".getBytes("UTF8"));
		MessageInput message = new MessageInput(a);
		assertEquals("5", message.readBySpace());
		assertEquals("FriesB512", message.readBySpace());
		assertEquals("5.6", message.readBySpace());
		assertEquals("6", message.readBySpace());
	}

	
	/*
	 * Unit Test for read character count from message with only space
	 */
	@Test
	void testReadBySpaceSpace() throws IOException ,TokenizerException{
		ByteArrayInputStream a = 
				new ByteArrayInputStream(" ".getBytes("UTF8"));
		MessageInput message = new MessageInput(a);
		assertEquals("", message.readBySpace());
	}
	
	/*
	 * Unit Test for read character count from message with empty stream
	 */
	@Test
	void testReadBySpaceEmpty() throws IOException {
		ByteArrayInputStream a = 
				new ByteArrayInputStream("".getBytes("UTF8"));
		MessageInput message = new MessageInput(a);
	    assertThrows(IOException.class, () -> {
			message.readBySpace();
	    });	
	 }
	
	/*
	 * Unit Test for read character count from message
	 * method will read until it reach a space
	 */
	@Test
	void testReadByChar() throws IOException, TokenizerException {
		ByteArrayInputStream a = 
				new ByteArrayInputStream("5 FriesB512 5.6 6 ".getBytes("UTF8"));
		MessageInput message = new MessageInput(a);
		assertEquals("5 ", message.readByChar(2));
		assertEquals("FriesB512 ", message.readByChar(10));
		assertEquals("5.6 ", message.readByChar(4));
		assertEquals("6 ", message.readByChar(2));
	}
	
	/*
	 * Unit Test for read by char method with zero as parameter
	 * readByChar will read the n bytes from stream
	 */
	@Test
	void testReadByCharZero() throws IOException, TokenizerException {
		ByteArrayInputStream a = 
				new ByteArrayInputStream("1234".getBytes("UTF8"));
		MessageInput message = new MessageInput(a);
		assertEquals("", message.readByChar(0));
	    
	 }
	
	
	/*
	 * Unit Test for read by char method with less byte in array
	 * readByChar will read the n bytes from stream
	 */
	@Test
	void testReadByCharNotEnough() throws IOException {
		ByteArrayInputStream a = 
				new ByteArrayInputStream("123".getBytes("UTF8"));
		MessageInput message = new MessageInput(a);
	    assertThrows(IOException.class, () -> {
			message.readByChar(4);
	    });	
	 }
	
	
	/*
	 * Unit Test for read by char method with empty string
	 * readByChar will read the n bytes from stream
	 */
	@Test
	void testReadByCharEmpty() throws IOException {
		ByteArrayInputStream a = 
				new ByteArrayInputStream("".getBytes("UTF8"));
		MessageInput message = new MessageInput(a);
	    assertThrows(IOException.class, () -> {
			message.readByChar(4);
	    });	
	 }
	
}
