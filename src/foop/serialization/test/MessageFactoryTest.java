package foop.serialization.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import fiat.serialization.MealType;

import foop.serialization.ACK;
import foop.serialization.Addition;
import foop.serialization.Error;
import foop.serialization.Register;

public class MessageFactoryTest {
	/*
	 * test for decode with null message
	 */
	@Test
	public void decodeNullPointerTest(){
	    assertThrows(NullPointerException.class, () -> {
	    	foop.serialization.MessageFactory.decode(null);
		});
	}
	@Test
	public void decodeRegister() {
		byte[] a = {48,100,0,1,2,-1,-72,11};
		foop.serialization.Message b = foop.serialization.MessageFactory.decode(a);
		assertEquals("Register: MsgID=100 Address=255.2.1.0 Port=3000"
				, b.toString());
	}
	@Test
	public void decodeAddition() {
		byte[] a = {49,-56,8,110,97,109,101,32,104,122,121,66,0,0,8};
		foop.serialization.Message b = foop.serialization.MessageFactory.decode(a);
		assertEquals("Addition: MsgID=200 Name=name hzy Calories=2048 Meal=Breakfast"
				, b.toString());
		
	}
	@Test
	public void decodeError() {
		byte[] a = {50,12,116,104,105,115,32,105,115,32,97,32,
				101,114,114,111,114,32,109,101,115,115,97,103,101};
		foop.serialization.Message b = foop.serialization.MessageFactory.decode(a);
		assertEquals("Error: MsgID=12 Message=this is a error message"
				, b.toString());
	}
	@Test
	public void decodeACK(){
		byte[] a = {51,30};
		foop.serialization.Message b = foop.serialization.MessageFactory.decode(a);
		assertEquals("ACK: MsgID=30"
				, b.toString());
	}
	
	@Test
	public void decodeACKWrongSize(){
		byte[] a = {51,0};
		foop.serialization.Message b = foop.serialization.MessageFactory.decode(a);
		assertEquals("ACK: MsgID=0"
				, b.toString());
	}
	
	@Test
	public void decodeWrongSize(){
		byte[] a = {51};
		assertThrows(IllegalArgumentException.class, () -> {
			foop.serialization.MessageFactory.decode(a);
	    });
	}
	
	@Test
	public void decodeRegisterMoreSize() {
		byte[] a = {48,100,0,1,2,-1,-72,11,12};
		assertThrows(IllegalArgumentException.class, () -> {
			foop.serialization.MessageFactory.decode(a);
	    });
		
	}
	
	@Test
	public void decodeAdditionLessSize() {
		byte[] a = {48,100,0,1,2,-1,-72};
		assertThrows(IllegalArgumentException.class, () -> {
			foop.serialization.MessageFactory.decode(a);
	    });
		
	}
	@Test
	public void decodeAdditionLessSize01() {
		byte[] a = {49,-56};
		assertThrows(IllegalArgumentException.class, () -> {
			foop.serialization.MessageFactory.decode(a);
	    });
	}
	@Test
	public void decodeAdditionLessSize02() {
		byte[] a = {49,-56,3};
		assertThrows(IllegalArgumentException.class, () -> {
			foop.serialization.MessageFactory.decode(a);
	    });
	}
	@Test
	public void decodeAdditionMoreSize() {
		byte[] a = {49,-56,8,110,97,109,101,32,104,122,121,66,0,0,8,11};
		assertThrows(IllegalArgumentException.class, () -> {
			foop.serialization.MessageFactory.decode(a);
	    });
	}
	
	@Test
	public void decodeAdditionWrongName() {
		byte[] a = {49,-56,8,0,97,109,101,32,104,122,121,66,0,0,8};
		assertThrows(IllegalArgumentException.class, () -> {
			foop.serialization.MessageFactory.decode(a);
	    });
	}
	
	@Test
	public void decodeAdditionWrongMealType() {
		byte[] a = {49,-56,8,110,97,109,101,32,104,122,121,0,0,0,8};
		assertThrows(IllegalArgumentException.class, () -> {
			foop.serialization.MessageFactory.decode(a);
	    });
	}
	
	@Test
	public void decodeAdditionWrongDelimiter() {
		byte[] a = {49,-56,8,110,97,109,101,32,104,122,121,66,1,0,8};
		assertThrows(IllegalArgumentException.class, () -> {
			foop.serialization.MessageFactory.decode(a);
	    });
	}
	
	
	@Test
	public void decodeAdditionWrongCalories() {
		byte[] a = {49,-56,8,110,97,109,101,32,104,122,121,66,0,-1,8};
		assertThrows(IllegalArgumentException.class, () -> {
			foop.serialization.MessageFactory.decode(a);
	    });
	}
	
	
	@Test
	public void decodeErrorLessBytes() {
		byte[] a = {50,-1};
		assertThrows(IllegalArgumentException.class, () -> {
			foop.serialization.MessageFactory.decode(a);
	    });
	}
	
	@Test
	public void decodeErrorInvalidMessage() {
		byte[] a = {50,-1,0};
		assertThrows(IllegalArgumentException.class, () -> {
			foop.serialization.MessageFactory.decode(a);
	    });
	}
	
	//

	@Test
	public void encodeNullPointerTest(){
	    assertThrows(NullPointerException.class, () -> {
	    	foop.serialization.MessageFactory.encode(null);
		});
	}
	@Test
	public void encodeRegister() throws UnknownHostException{
		Inet4Address i4Addr = (Inet4Address) InetAddress
				.getByAddress(new byte[] {-1,2,1,0});
		Register r = new Register(100, i4Addr, 3000);
		byte[] b = foop.serialization.MessageFactory.encode(r);
		byte[] a = {48,100,0,1,2,-1,-72,11};

		Assert.assertArrayEquals(a,b);
		
	}
	
	@Test
	public void encodeAddition(){
		Addition add = new Addition(200, "name hzy", MealType.Breakfast, 2048);
		byte[] b = foop.serialization.MessageFactory.encode(add);
		byte[] a = {49,-56,8,110,97,109,101,32,104,122,121,66,0,0,8};

		Assert.assertArrayEquals(a,b);
		
	}
	@Test
	public void encodeError() {
		foop.serialization.Error err = new Error(12,"this is a error message");
		byte[] b = foop.serialization.MessageFactory.encode(err);
		byte[] a = {50,12,116,104,105,115,32,105,115,32,97,32,
				101,114,114,111,114,32,109,101,115,115,97,103,101};

		Assert.assertArrayEquals(a,b);
	}
	@Test
	public void encodeACK(){
		ACK ack = new ACK(30);
		byte[] b = foop.serialization.MessageFactory.encode(ack);
		byte[] a = {51,30};

		Assert.assertArrayEquals(a,b);
	}
}
