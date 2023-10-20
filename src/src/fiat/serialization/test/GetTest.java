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

import org.junit.jupiter.api.Test;

import fiat.serialization.Get;

public class GetTest {
	/*
	 * test get constructor
	 */
	@Test
	void GetBasic() {
		Get e = new Get(1);
        assertEquals(1, e.getTimestamp());
	}
	
	/*
	 * test for negative long value
	 */
	@Test
	void GetBasicNegLong() {
	    assertThrows(IllegalArgumentException.class, () -> {
			new Get(-1);
	    });
	}
	
	/*
	 * test for long at maximum
	 */
	@Test
	void GetBasicMaxLong() {
		new Get((long) (Math.pow(2,63)-1));
	}
	/*
	 * test for to string method
	 */
	@Test
	void GetToString() {
		Get e = new Get(5);
        assertEquals("GET (TS=5)", e.toString());
	}
}
