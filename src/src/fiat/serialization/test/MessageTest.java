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

import fiat.serialization.Add;
import fiat.serialization.ItemList;
import fiat.serialization.MealType;
import fiat.serialization.Get;
import fiat.serialization.Error;
import fiat.serialization.Message;

public class MessageTest {
	Message add = new Add(1, 
			new fiat.serialization.Item("Fries",MealType.Breakfast,512,5.6));
	Message error = new Error(2,"5 Error");
	Message get = new Get(3);
	Message list = new ItemList(4,5);
	
	/*
	 * test for get request
	 */
	@Test
	void TestGetRequest() {
        assertAll(()->assertEquals("ADD", add.getRequest()),
                () -> assertEquals("ERROR",error.getRequest()),
                () -> assertEquals("LIST",list.getRequest()),
                () -> assertEquals("GET",get.getRequest())
        );
	}
	
	@Test
	void TestGetTimeStamp() {
        assertAll(()->assertEquals(1, add.getTimestamp()),
                () -> assertEquals(2,error.getTimestamp()),
                () -> assertEquals(3,get.getTimestamp())
        );
	}
	
	@Test
	void TestSetTimeStamp() {
		add.setTimestamp(5);
		error.setTimestamp(4);
		get.setTimestamp(2);
		assertAll(()->assertEquals(5, add.getTimestamp()),
                () -> assertEquals(4,error.getTimestamp()),
                () -> assertEquals(2,get.getTimestamp())
        );
	}
	
	@Test
	void TestSetTimeStampNegInAdd() {
	    assertThrows(IllegalArgumentException.class, () -> {
			add.setTimestamp(-1);
	    });
	}

	@Test
	void TestSetTimeStampNegInError() {
	    assertThrows(IllegalArgumentException.class, () -> {
			error.setTimestamp(-1);
	    });
	}
	
	@Test
	void TestSetTimeStampNegInGet() {
	    assertThrows(IllegalArgumentException.class, () -> {
			get.setTimestamp(-1);
	    });
	}
	
	@Test
	void TestSetTimeStampMaxInAdd() {
		
		add.setTimestamp((long) (Math.pow(2,63)-1));
		error.setTimestamp((long) (Math.pow(2,63)-1));
		get.setTimestamp((long) (Math.pow(2,63)-1));
		assertAll(()->assertEquals((long) (Math.pow(2,63)-1), add.getTimestamp()),
                () -> assertEquals((long) (Math.pow(2,63)-1),error.getTimestamp()),
                () -> assertEquals((long) (Math.pow(2,63)-1),get.getTimestamp())
        );
		
	}

}
