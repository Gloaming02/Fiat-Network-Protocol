/************************************************
*
* Author: Zhengyan Hu
* Assignment: Fiat Food IntAke Tracker
* Class: CSI4321
*
************************************************/

package fiat.serialization.test;


import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


import fiat.serialization.TokenizerException;

public class TokenizerExceptionTest {

	/*
	 * Unit test tokenizerException first constructor
	 */
	@Test
	public void testExceptionBasic1() {
		TokenizerException e = new TokenizerException(1 , "bad");
		assertEquals(e.getMessage(),"bad");
		assertEquals(e.getOffset(),1);
	}

	/*
	 * Unit test tokenizerException second constructor
	 */
	@Test
	public void testExceptionBasic2() {
		IllegalArgumentException ex = new IllegalArgumentException();
		TokenizerException e = new TokenizerException(1 , "bad", ex);
		assertEquals("bad",e.getMessage());
		assertEquals(1,e.getOffset());
		assertEquals(ex, e.getCause());
	}
	

	/*
	 * Unit test tokenizerException first constructor with null message
	 */
	@Test
	public void testExceptionBasicNull1() {
	    assertThrows(IllegalArgumentException.class, () -> {
	    	new TokenizerException(1 , null);	    
	    });
	}
	
	/*
	 * Unit test tokenizerException first constructor with negative offset
	 */
	@Test
	public void testExceptionBasicNeg1() {
	    assertThrows(IllegalArgumentException.class, () -> {
	    	new TokenizerException(-1 , "bad");	    
	    });
	}
	
	/*
	 * Unit test tokenizerException second constructor with null message
	 */
	@Test
	public void testExceptionBasicNull2() {
		NullPointerException ex = new NullPointerException();

	    assertThrows(IllegalArgumentException.class, () -> {
	    	new TokenizerException(1 , null, ex);	    
	    });
	}
	
	/*
	 * Unit test tokenizerException second constructor with negative offset
	 */
	@Test
	public void testExceptionBasicNeg2() {
		NullPointerException ex = new NullPointerException();

	    assertThrows(IllegalArgumentException.class, () -> {
	    	new TokenizerException(-1 , "bad", ex);	    
	    });
	}
	
	
}
