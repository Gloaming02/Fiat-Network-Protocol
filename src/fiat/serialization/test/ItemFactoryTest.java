/************************************************
*
* Author: Zhengyan Hu
* Assignment: Fiat Food IntAke Tracker
* Class: CSI4321
*
************************************************/

package fiat.serialization.test;
import static org.junit.jupiter.api.Assertions.*;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import fiat.serialization.*;


public class ItemFactoryTest {
	Item testItem = new Item("Fries",MealType.Breakfast,512,5.6);
	ByteArrayOutputStream output = new ByteArrayOutputStream();
	MessageOutput out = new MessageOutput(output);
	
	/*
	 * Unit Test for decode method
	 */
	@Test
	void decodeBasic() throws IOException, TokenizerException {
		Item i = ItemFactory.decode(
			new MessageInput(
				new ByteArrayInputStream("5 FriesB512 5.6 ".getBytes("UTF8"))));
		assertAll(()->assertEquals("Fries", i.getName()),
	        () -> assertEquals(MealType.Breakfast, i.getMealType()),
	        () -> assertEquals(512, i.getCalories()),
	        () -> assertEquals(5.6, i.getFat(), 0.0001));
	}
	
	/*
	 * Unit Test for decode method, run twice.
	 */
	@Test
	void decodeBasicMutiply() throws IOException, TokenizerException {
		MessageInput m = new MessageInput(
			new ByteArrayInputStream(
				"5 FriesB512 5.6 6 BurgerL1000 10.1 ".getBytes("UTF8")));
		
		Item i1 = ItemFactory.decode(m);
		Item i2 = ItemFactory.decode(m);
		assertAll(()->assertEquals("Fries", i1.getName()),
	        () -> assertEquals(MealType.Breakfast, i1.getMealType()),
	        () -> assertEquals(512, i1.getCalories()),
	        () -> assertEquals(5.6, i1.getFat(), 0.0001),
	        ()->assertEquals("Burger", i2.getName()),
	        () -> assertEquals(MealType.Lunch, i2.getMealType()),
	        () -> assertEquals(1000, i2.getCalories()),
	        () -> assertEquals(10.1, i2.getFat(), 0.0001));
	}
	/*
	 * Unit Test for passing null to decode, expect NullPointerException throw
	 */
	@Test
	void decodeNullPointer() throws IOException, TokenizerException {
	    assertThrows(NullPointerException.class, () -> {
			ItemFactory.decode(null);
	    });	
	}
	
	/*
	 * Unit Test for passing null to decode, expect NullPointerException throw
	 */
	@Test
	void decodeWithTokenExc1() throws IOException, TokenizerException {
		MessageInput m = new MessageInput(
				new ByteArrayInputStream(
					"A FriesB512 5.6 ".getBytes("UTF8")));
		
		TokenizerException ex = assertThrows(TokenizerException.class, () -> {
			ItemFactory.decode(m);
		});
		assertEquals(0, ex.getOffset());
	}
	
	@Test
	void decodeWithTokenExc2() throws IOException, TokenizerException {
		MessageInput m = new MessageInput(
				new ByteArrayInputStream(
					"5 FriesZ512 5.6 ".getBytes("UTF8")));
		
		TokenizerException ex = assertThrows(TokenizerException.class, () -> {
			ItemFactory.decode(m);
		});
		assertEquals(7, ex.getOffset());

	}
	
	/*
	 * Unit Test for passing invalid calories to decode
	 */
	@Test
	void decodeWithTokenExc3() throws IOException, TokenizerException {
		MessageInput m = new MessageInput(
				new ByteArrayInputStream(
					"5 FriesB-512 5.6 ".getBytes("UTF8")));
		
		TokenizerException ex = assertThrows(TokenizerException.class, () -> {
			ItemFactory.decode(m);
		});
		assertEquals(8, ex.getOffset());
	}
	
	/*
	 * Unit Test for passing invalid fat to decode
	 */
	@Test
	void decodeWithTokenExc4() throws IOException, TokenizerException {
		MessageInput m = new MessageInput(
				new ByteArrayInputStream(
					"5 FriesB512 -5.6 ".getBytes("UTF8")));
		
		TokenizerException ex = assertThrows(TokenizerException.class, () -> {
			ItemFactory.decode(m);
		});
		assertEquals(12, ex.getOffset());
	}
	
	/*
	 * Unit Test for encode
	 */
	@Test
	void encodeBasic() throws IOException {
		ItemFactory.encode(testItem, out);
		assertEquals("5 FriesB512 5.6 ", out.getEncoding());
	}
	
	/*
	 * Unit Test for passing null Output stream to encode
	 */
	@Test
	void encodeNullPointerOut() throws IOException {
	    assertThrows(NullPointerException.class, () -> {
			ItemFactory.encode(testItem, null);
	    });	
	}
	
	/*
	 * Unit Test for passing null item to encode
	 */
	@Test
	void encodeNullPointerItem() throws IOException {
	    assertThrows(NullPointerException.class, () -> {
			ItemFactory.encode(null, out);
	    });	
	}
	
	
}
