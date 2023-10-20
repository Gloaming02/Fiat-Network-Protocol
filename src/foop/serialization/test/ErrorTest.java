package foop.serialization.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;



public class ErrorTest {
	@Test
	public void ErrorBasic(){
		foop.serialization.Error err = new foop.serialization.Error(200, "Message");
		assertEquals("Error: MsgID=200 Message=Message", err.toString());
	}
	@Test
	public void ErrorInvalidMsgID(){
		assertThrows(IllegalArgumentException.class, () -> {
			new foop.serialization.Error(-1, "Message");
		});
	}
	@Test
	public void ErrorInvalidErrorMessage(){
		assertThrows(IllegalArgumentException.class, () -> {
			new foop.serialization.Error(1, "-Message");
		});
	}
	
	@Test
	public void ErrorSetMessage(){
		foop.serialization.Error err = new foop.serialization.Error(200, 
							"a");
		err.setMessage("Error: MsgID=200 Message=Message");
		assertEquals("Error: MsgID=200 Message=Message", err.getMessage());
	}
	
	@Test
	public void ErrorEquals(){
		foop.serialization.Error err = new foop.serialization.Error(200, "Message");
		foop.serialization.Error err1 = new foop.serialization.Error(200, "Message");
		assertEquals(err1, err);

	}
}
