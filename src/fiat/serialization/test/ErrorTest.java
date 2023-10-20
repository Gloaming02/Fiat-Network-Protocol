/************************************************
*
* Author: Zhengyan Hu
* Assignment: Fiat Food IntAke Tracker
* Class: CSI4321
*
************************************************/

package fiat.serialization.test;

import static org.junit.jupiter.api.Assertions.assertAll;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import fiat.serialization.Error;

public class ErrorTest {

	/*
	 * test error constructor
	 */
	@Test
	void ErrorBasic() {
		Error e = new Error(1,"5 Fries");
        assertAll(()->assertEquals("5 Fries", e.getMessage()),
                () -> assertEquals(1,e.getTimestamp()));
	}
	
	/*
	 * test error constructor when message is null
	 */
	@Test
	void ErrorBasicNullStr() {
	    assertThrows(IllegalArgumentException.class, () -> {
	    	new Error(1,null);
	    });
	}
	
	/*
	 * test error constructor when message is empty
	 */
	@Test
	void ErrorBasicEmptyStr() {
	    assertThrows(IllegalArgumentException.class, () -> {
	    	new Error(1,"");
	    });
	}
	
	/*
	 * test error constructor when message is long string
	 */
	@Test
	void ErrorBasicLongStr() {
    	String a = "1".repeat(2049);
	    assertThrows(IllegalArgumentException.class, () -> {
	    	new Error(1,a);
	    });
	}
	
	/*
	 * test error constructor when string is invalid from protocol
	 */
	@Test
	void ErrorBasicInvalidStr() {
	    assertThrows(IllegalArgumentException.class, () -> {
	    	new Error(1,"-a");
	    });
	}
	
	/*
	 * test error constructor when long is negative
	 */
	@Test
	void ErrorBasicNegLong() {
	    assertThrows(IllegalArgumentException.class, () -> {
			new Error(-1,"5 Fries");
	    });
	}
	
	/*
	 * test error constructor long is maximum
	 */
	@Test
	void ErrorBasicMaxLong() {
		new Error((long) (Math.pow(2,63)-1),"5 Fries");
	}
	
	/*
	 * test message setter
	 */
	
	@Test
	void ErrorSetMessageTest() {
		Error e = new Error(1,"5 Fries");
		Error eTemp = e.setMessage("6 Fries1");
		
        assertAll(()->assertEquals(e, eTemp),
                () -> assertEquals("6 Fries1", e.getMessage()));	
        
	}
	/*
	 * test set message to null
	 */
	@Test
	void ErrorSetMessageNull() {
		Error e = new Error(1,"5 Fries");
	    assertThrows(IllegalArgumentException.class, () -> {
			e.setMessage(null);
	    });
        
	}
	
	/*
	 * test set message to empty
	 */
	@Test
	void ErrorSetMessageEmpty() {
		Error e = new Error(1,"5 Fries");
	    assertThrows(IllegalArgumentException.class, () -> {
			e.setMessage("");
	    });
        
	}
	/*
	 * test set invalid message
	 */
	@Test
	void ErrorSetMessageInvalid() {
		Error e = new Error(1,"5 Fries");
	    assertThrows(IllegalArgumentException.class, () -> {
			e.setMessage("-a");
	    });
        
	}
	
	/*
	 * test message getter
	 */
	@Test
	void ErrorGetMessage() {
		Error e = new Error(1,"5 Fries");
        assertAll(()->assertEquals("5 Fries", e.getMessage()),
                () -> assertEquals(1,e.getTimestamp()));
        
	}
	
	/*
	 * test for to string method
	 */
	@Test
	void ErrorToString() {
		Error e = new Error(5,"5 Fries");
		assertEquals("ERROR (TS=5) message=5 Fries", e.toString());
	}
	
	
}
