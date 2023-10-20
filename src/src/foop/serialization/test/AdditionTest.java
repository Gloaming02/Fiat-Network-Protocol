package foop.serialization.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import fiat.serialization.MealType;
import foop.serialization.Addition;

public class AdditionTest {
	@Test
	public void AdditionBasic(){
		Addition add = new Addition(200, "name hzy", MealType.Breakfast, 2048);
		assertEquals("Addition: MsgID=200 Name=name hzy Calories=2048 Meal=Breakfast", add.toString());
	}
	@Test
	public void AdditionInvalidMsgId01(){
		assertThrows(IllegalArgumentException.class, () -> {
			new Addition(-1, "name hzy", MealType.Breakfast, 2048);
	    });
	}
	@Test
	public void AdditionInvalidMsgId02(){
		assertThrows(IllegalArgumentException.class, () -> {
			new Addition(1000000000, "name hzy", MealType.Breakfast, 2048);
	    });
	}
	
	@Test
	public void AdditionInvalidName(){
		assertThrows(IllegalArgumentException.class, () -> {
			new Addition(1, "-name hzy", MealType.Breakfast, 2048);
	    });
	}
	@Test
	public void AdditionInvalidMeal(){
		assertThrows(IllegalArgumentException.class, () -> {
			new Addition(1, "name hzy", null, 2048);
	    });
	}
	
	@Test
	public void AdditionInvalidCal(){
		assertThrows(IllegalArgumentException.class, () -> {
			new Addition(1, "name hzy", MealType.Breakfast, 2049);
	    });
	}
	@Test
	public void AdditionEquals(){
		Addition a = new Addition(200, "name hzy", MealType.Breakfast, 2048);
		Addition b = new Addition(200, "name hzy", MealType.Breakfast, 2048);
		assertEquals(a, b);
		assertEquals(a.getName(), b.getName());
		assertEquals(a.getMsgID(), b.getMsgID());
		assertEquals(a.getMealType(), b.getMealType());
		assertEquals(a.getCalories(), b.getCalories());

	}
	
	@Test
	public void AdditionSetter(){
		Addition a = new Addition(200, "name hzy", MealType.Breakfast, 2048);
		a.setName("good");
		assertEquals("good", a.getName());
		a.setMsgID(0);
		assertEquals(0, a.getMsgID());
		a.setMealType(MealType.Lunch);
		assertEquals(MealType.Lunch, a.getMealType());
		a.setCalories(2010);

		assertEquals(2010,a.getCalories());

	}
}
