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

import fiat.serialization.MealType;

public class MealTypeTest {
	/*
	 * Unit Test for values
	 */
	@Test
	public void testValues() {
		MealType[] a = MealType.values();
        assertAll(()->assertEquals(MealType.Breakfast, a[0]),
                () -> assertEquals(MealType.Lunch, a[1]),
                () -> assertEquals(MealType.Dinner, a[2]),
                () -> assertEquals(MealType.Snack, a[3]));
	}
	
	/*
	 * Unit Test for valueOf
	 */
	@Test
	public void testValueOf() {
        assertAll(
        	()->assertEquals(MealType.Breakfast, MealType.valueOf("Breakfast")),
        	()->assertEquals(MealType.Lunch, MealType.valueOf("Lunch")),
        	()->assertEquals(MealType.Dinner, MealType.valueOf("Dinner")),
       		()->assertEquals(MealType.Snack, MealType.valueOf("Snack")));
        
	}
	
	/*
	 * Unit Test for getMealTypeCode
	 */
	@Test
	public void testGetMealTypeCode() {
		MealType b = MealType.Breakfast;
		MealType l = MealType.Lunch;
		MealType d = MealType.Dinner;
		MealType s = MealType.Snack;
        assertAll(()->assertEquals('B', b.getMealTypeCode()),
        		()->assertEquals('L', l.getMealTypeCode()),
        		()->assertEquals('D', d.getMealTypeCode()),
        		()->assertEquals('S', s.getMealTypeCode()));
	}
	
	
	/*
	 * Unit Test for getMealType
	 */
	@Test
	public void testGetMealType() {
		MealType b = MealType.getMealType('B');
		MealType l = MealType.getMealType('L');
		MealType d = MealType.getMealType('D');
		MealType s = MealType.getMealType('S');
        assertAll(()->assertEquals(MealType.Breakfast, b),
        		()->assertEquals(MealType.Lunch, l),
        		()->assertEquals(MealType.Dinner, d),
        		()->assertEquals(MealType.Snack, s));
        
	}
	
	
	/*
	 * Unit Test for getMealType with invalid code
	 */
	@Test
	public void testGetMealTypeInvalid() {
	    assertThrows(IllegalArgumentException.class, () -> {
	    	MealType.getMealType('b');
	    });	
	}
	
	
}
