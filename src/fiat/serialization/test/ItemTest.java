/************************************************
*
* Author: Zhengyan Hu
* Assignment: Fiat Food IntAke Tracker
* Class: CSI4321
*
************************************************/

package fiat.serialization.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import fiat.serialization.*;

public class ItemTest {

	Item i = new Item("Fries",MealType.Breakfast,512,5.6);

	/*
	* Unit Test for constructor, using getter to check if they are correct
	*/
	@Test
	public void testConstructor() {
        assertAll(()->assertEquals("Fries", i.getName()),
                () -> assertEquals(MealType.Breakfast, i.getMealType()),
                () -> assertEquals(512, i.getCalories()),
                () -> assertEquals(5.6, i.getFat(), 0.0001));
	}
	
	/*
	* Unit Test for constructor. 
	* if the Name is null, expect throws illegal argument exception
	*/
	@Test
	public void testConstructorNullName() {
	    assertThrows(IllegalArgumentException.class, () -> {
			new Item(null,MealType.Breakfast,512,5.6);
	    });
	}
	
	/*
	* Unit Test for constructor. 
	* if the Name is empty, expect throws illegal argument exception
	*/
	@Test
	public void testConstructorEmptyName() {
	    assertThrows(IllegalArgumentException.class, () -> {
			new Item("",MealType.Breakfast,512,5.6);
	    });
	}
	
	/*
	* Unit Test for constructor. 
	* if the meal type is null, expect throws illegal argument exception
	*/
	@Test
	public void testConstructorNullMealType() {
	    assertThrows(IllegalArgumentException.class, () -> {
			new Item("Fries",null,512,5.6);
	    });
	}

	/*
	* Unit Test for constructor. 
	* if the calories is negative, expect throws illegal argument exception
	*/
	@Test
	public void testConstructorNegCalories() {
	    assertThrows(IllegalArgumentException.class, () -> {
			new Item("Fries",MealType.Breakfast,-512,5.6);
	    });
	}
	
	/*
	* Unit Test for constructor. 
	* if the fat is negative, expect throws illegal argument exception
	*/
	@Test
	public void testConstructorNegFat() {
	    assertThrows(IllegalArgumentException.class, () -> {
			new Item("Fries",MealType.Breakfast,512,-5.6);
	    });
	}
	
	/*
	* Unit Test for toString expect fat to round up
	*/
	@Test
	public void testToStringRoundUp() {
		assertEquals("Fries with 512 calories and 6g of fat eaten at Breakfast",
					i.toString());
	}
	
	/*
	* Unit Test for toString expect fat to round down
	*/
	@Test
	public void testToStringRoundDown() {
		Item ii = new Item("Fries",MealType.Breakfast,512,5.4);
		assertEquals("Fries with 512 calories and 5g of fat eaten at Breakfast", 
					ii.toString());
	}
	
	/*
	* Unit Test for getName, expect Fries.
	*/
	@Test
	public void testGetName() {
        assertEquals("Fries", i.getName());
	}
	
	/*
	* Unit Test for getMealType, expect Breakfast.
	*/
	@Test
	public void testGetMealType() {
		assertEquals(MealType.Breakfast, i.getMealType());
	}	
	
	
	/*
	* Unit Test for getCalories, expect 512.
	*/
	@Test
	public void testGetCalories() {
		assertEquals(512, i.getCalories());
	}
	
	/*
	* Unit Test for getFat, expect 5.6.
	*/
	@Test
	public void testGetFat() {
		assertEquals(5.6, i.getFat(), 0.0001);
	}
	
	/*
	* Unit Test for setName
	*/	
	@Test
	public void testSetName() {
        assertSame(i,i.setName("Noodles"));
        assertEquals("Noodles", i.getName());
	}
	
	/*
	* Unit Test for setName to set to null, expect throw IllegalArgumentException
	*/	
	@Test
	public void testSetNameNull() {
	    assertThrows(IllegalArgumentException.class, () -> {
			i.setName(null);
	    });
	}
	/*
	* Unit Test for setName to set to Empty String, expect throw IllegalArgumentException
	*/	
	@Test
	public void testSetNameEmptyString() {
	    assertThrows(IllegalArgumentException.class, () -> {
			i.setName("");
	    });
	}
	
	/*
	* Unit Test for setMealType
	*/	
	@Test
	public void testSetMealType() {
        assertSame(i,i.setMealType(MealType.Dinner));
        assertEquals(MealType.Dinner, i.getMealType());
	}
	
	/*
	* Unit Test for setMealType to set to null, expect throw IllegalArgumentException
	*/	
	@Test
	public void testSetMealTypeNull() {
	    assertThrows(IllegalArgumentException.class, () -> {
			i.setMealType(null);
	    });	
	}
	
	/*
	* Unit Test for setCalories
	*/	
	@Test
	public void testSetCalories() {
        assertSame(i,i.setCalories(100));

        assertEquals(100, i.getCalories());
	}
	
	/*
	 * Unit Test for setCalories to set to negative, expect throw IllegalArgumentException
	 */
	@Test
	public void testSetCaloriesNeg() {
	    assertThrows(IllegalArgumentException.class, () -> {
			i.setCalories(-100);
	    });	
	}
	/*
	 * Unit Test for setCalories to set over max, expect throw IllegalArgumentException
	 */
	@Test
	public void testSetCaloriesExceed() {
	    assertThrows(IllegalArgumentException.class, () -> {
			i.setCalories(2049);
	    });	
	}
	
	/*
	 * Unit Test for setFat
	 */
	@Test
	public void testSetFat() {
        assertSame(i,i.setFat(4.4));
        assertEquals(4.4, i.getFat(), 0.0001);
	}
	/*
	 * Unit Test for setFat to set to negative, expect throw IllegalArgumentException
	 */
	@Test
	public void testSetFatNeg() {
	    assertThrows(IllegalArgumentException.class, () -> {
			i.setFat(-4.4);
	    });	
	}
	
	/*
	 * Unit Test for setFat to set over max, expect throw IllegalArgumentException
	 */
	@Test
	public void testSetFatExceed() {
	    assertThrows(IllegalArgumentException.class, () -> {
			i.setFat(100000.1);
	    });	
	}
	
	
	
}
