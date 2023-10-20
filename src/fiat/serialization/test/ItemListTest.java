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


import fiat.serialization.ItemList;

public class ItemListTest {	
	
	/*
	 * test ItemList constructor
	 */
	@Test
	void ItemListBasic() {
		ItemList list = new ItemList(1,2);
        assertAll(()->assertEquals(1, list.getTimestamp()),
                () -> assertEquals(2, list.getModifiedTimestamp()));

	}
	
	/*
	 * test ItemList constructor when long is maximun
	 */
	@Test
	void ItemListBasicMax() {
		ItemList list = new ItemList(1,(long) (Math.pow(2,63)-1));
        assertAll(()->assertEquals(1, list.getTimestamp()),
                () -> assertEquals((long) (Math.pow(2,63)-1), list.getModifiedTimestamp()));

	}
	/*
	 * test ItemList constructor when time stamp is negative
	 */
	@Test
	void ItemListNegTime() {
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
			new ItemList(-1,1);
	    });
		assertEquals("invalid message times stamp value: -1", ex.getMessage());
	}
	
	/*
	 * test ItemList constructor when modified time is negative
	 */
	@Test
	void ItemListNegModified() {
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
			new ItemList(1,-1);
	    });
		assertEquals("invalid modified times stamp value: -1", ex.getMessage());
	}
	
	/*
	 * test get modified time
	 */
	@Test
	void ItemListGetModifiedTest() {
		ItemList list = new ItemList(1,2);
		assertEquals(2, list.getModifiedTimestamp());
	}
	
	/*
	 * test set modified time
	 */
	@Test
	void ItemListSetModifiedTest() {
		ItemList list = new ItemList(1,2);
		ItemList temp = list.setModifiedTimestamp(100);
		assertAll(()->assertEquals(100, list.getModifiedTimestamp()),
				() -> assertEquals(list, temp));
	}
	
	/*
	 * test set modified time to negative
	 */
	@Test
	void ItemListSetModifiedNeg() {
		ItemList list = new ItemList(1,2);
		
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
			list.setModifiedTimestamp(-100);
	    });
		assertEquals("invalid modified times stamp value: -100", ex.getMessage());
		
	}
	
	/*
	 * test set modified time to maximum
	 */
	@Test
	void ItemListSetModifiedMax() {
		ItemList list = new ItemList(1,2);
		ItemList temp = list.setModifiedTimestamp((long) (Math.pow(2,63)-1));
		assertAll(()->assertEquals((long) (Math.pow(2,63)-1), list.getModifiedTimestamp()),
				() -> assertEquals(list, temp));
		
	}
	
	/*
	 * test to string method
	 */
	@Test
	void ItemListToStringTest() {
		ItemList list = new ItemList(5,7);
		fiat.serialization.Item a = 
				new fiat.serialization.Item("Plum",fiat.serialization.MealType.Breakfast,5,5.6);
		fiat.serialization.Item b = 
				new fiat.serialization.Item("Bacon",fiat.serialization.MealType.Dinner,50,60.1);

		list.addItem(a);
		list.addItem(b);
		assertEquals("LIST (TS=5) last mod=7, list={Plum with 5 calories and 6g of fat eaten at Breakfast,"
				+"Bacon with 50 calories and 60g of fat eaten at Dinner}", 
				list.toString());
		
	}
	
	/*
	 * test  for get and add item to list 
	 */
	@Test
	void GetAndAddItemListTest() {
		ItemList list = new ItemList(5,7);
		fiat.serialization.Item a = 
				new fiat.serialization.Item("Plum",fiat.serialization.MealType.Breakfast,5,5.6);
		fiat.serialization.Item b = 
				new fiat.serialization.Item("Bacon",fiat.serialization.MealType.Dinner,50,60.1);

		list.addItem(a);
		list.addItem(b);
		assertAll(()-> assertEquals(a, list.getItemList().get(0)),
				() -> assertEquals(b, list.getItemList().get(1)));
		
	}
	
	/*
	 * test to add null time
	 */
	@Test
	void AddNullItemTest() {
		ItemList list = new ItemList(5,7);
		assertThrows(IllegalArgumentException.class, () -> {
			list.addItem(null);
	    });
	}
	
	/*
	 * test set time
	 */
	@Test
	void ItemListSetTimeTest1() {
		ItemList list = new ItemList(1,2);
		assertThrows(IllegalArgumentException.class, () -> {
			list.setTimestamp(-1);
	    });
	}
	
}
