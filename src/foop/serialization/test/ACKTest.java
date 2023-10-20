package foop.serialization.test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import foop.serialization.ACK;

public class ACKTest {
	@Test
	public void ACKBasic(){
		ACK a = new ACK(1);
		assertEquals("ACK: MsgID=1", a.toString());

	}
	@Test
	public void ACKInvalidMsgId01(){
		assertThrows(IllegalArgumentException.class, () -> {
			new ACK(-1);
	    });
	}
	@Test
	public void ACKInvalidMsgId02(){
		assertThrows(IllegalArgumentException.class, () -> {
			new ACK(1000000000);
	    });
	}
	
	@Test
	public void ACKEquals(){
		ACK a = new ACK(1);
		ACK b = new ACK(1);
		assertEquals(a, b);

	}
}
