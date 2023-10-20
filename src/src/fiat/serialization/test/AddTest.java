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
import fiat.serialization.MealType;

public class AddTest{
	fiat.serialization.Item i = new fiat.serialization.Item("Plum",fiat.serialization.MealType.Breakfast,5,5.6);

	/*
	 * test add constructor
	 */
	@Test
	void AddBasic() {
		Add a = new Add(1,i);
        assertAll(()->assertEquals(a.getItem(), i),
                () -> assertEquals(a.getTimestamp(), 1));
	}
	
	/*
	 * test add constructor when item is null
	 */
	@Test
	void AddBasicNullItem() {
	    assertThrows(IllegalArgumentException.class, () -> {
			new Add(1,null);
	    });
	}
	/*
	 * test add constructor when long is negative
	 */
	@Test
	void AddBasicNegLong() {
	    assertThrows(IllegalArgumentException.class, () -> {
			new Add(-1,i);
	    });
	}
	/*
	 * test add constructor when maximum time stamp
	 */
	@Test
	void AddBasicMaxLong() {
		new Add((long) (Math.pow(2,63)-1),i);
	}
	
	/*
	 * test to string method
	 */
	@Test
	void AddToStringTest() {
		Add a = new Add(5,i);
		assertEquals("ADD (TS=5) item=Plum with 5 calories and 6g of fat eaten at Breakfast",
				a.toString());
	}
	
	/*
	 * test for item getter
	 */
	@Test
	void AddGetItemTest() {
		Add a = new Add(1 , i);
        assertEquals(a.getItem(), i);
	}
	
	/*
	 * test for item setter
	 */
	@Test
	void AddSetItemTest() {
		Add a = new Add(1 , i);
		fiat.serialization.Item temp = new fiat.serialization.Item("Fries",MealType.Breakfast,512,6.4);
		Add aTemp = a.setItem(temp);
		
        assertAll(()->assertEquals(a, aTemp),
                () -> assertEquals(temp, a.getItem()));	
        
	}
	
	/*
	 * test for set item to null
	 */
	@Test
	void AddSetItemNull() {
		Add a = new Add(1,i);
	    assertThrows(IllegalArgumentException.class, () -> {
			a.setItem(null);
	    });
        
	}
	
	
}
