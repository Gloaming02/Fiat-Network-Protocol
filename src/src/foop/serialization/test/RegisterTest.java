package foop.serialization.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.jupiter.api.Test;

import foop.serialization.Register;

public class RegisterTest {
	@Test
	public void RegisterBasic() throws UnknownHostException{
		Inet4Address i4Addr = (Inet4Address) InetAddress
				.getByAddress(new byte[] {-1,2,1,0});
		Register a = new Register(100, i4Addr, 3000);
		
		assertEquals("Register: MsgID=100 Address=255.2.1.0 Port=3000", a.toString());

	}
	
	@Test
	public void RegisterBasicNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			 new Register(100, null, 3000);
	    });
		
	}
	
}

