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
import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Test;

import fiat.serialization.Add;
import fiat.serialization.Get;
import fiat.serialization.Item;
import fiat.serialization.ItemList;
import fiat.serialization.MealType;
import fiat.serialization.Message;
import fiat.serialization.MessageFactory;
import fiat.serialization.MessageInput;
import fiat.serialization.MessageOutput;
import fiat.serialization.TokenizerException;

public class MessageFactoryTest {
	
	ByteArrayOutputStream output = new ByteArrayOutputStream();
	MessageOutput out = new MessageOutput(output);
	
	/*
	 * test for decode with null message
	 */
	@Test
	public void decodeNullPointerTest(){
	    assertThrows(NullPointerException.class, () -> {
			MessageFactory.decode(null);
		});
	}
	/*
	 * test for negative offset
	 */
	@Test
	public void decodeTokenOffsetTest() throws TokenizerException, UnsupportedEncodingException{
		assertThrows(TokenizerException.class, () -> {
	    	MessageFactory.decode(new MessageInput(
					new ByteArrayInputStream("FT1.0 -1 ADD 5 FriesB512 5.6 \n".getBytes("UTF8"))));
	    });
	}
	/*
	 * test for incorrect request
	 */
	@Test
	public void decodeWrongRequestTest() throws TokenizerException, UnsupportedEncodingException{
		TokenizerException ex =assertThrows(TokenizerException.class, () -> {
	    	MessageFactory.decode(new MessageInput(
					new ByteArrayInputStream("FT1.0 1 GOOD 5 FriesB512 5.6 \n".getBytes("UTF8"))));
	    });
		assertEquals(8, ex.getOffset());
	}
	
	/*
	 * test for add request
	 */
	@Test
	public void decodeAddRequestTest() throws TokenizerException, UnsupportedEncodingException{
		Message i = MessageFactory.decode(new MessageInput(
				new ByteArrayInputStream("FT1.0 1 ADD 5 FriesB512 5.6 \n".getBytes("UTF8"))));
		fiat.serialization.Item item = 
				new fiat.serialization.Item("Fries",fiat.serialization.MealType.Breakfast,512,5.6);
        assertAll(()->assertEquals("ADD", i.getRequest()),
                () -> assertEquals(1, i.getTimestamp()),
                () -> assertEquals(item, i.getItem()));
	}
	
	/*
	 * test for incorrect add request
	 */
	@Test
	public void decodeItemTokenOffsetTest() throws TokenizerException, UnsupportedEncodingException{
		assertThrows(TokenizerException.class, () -> {
	    	MessageFactory.decode(new MessageInput(
					new ByteArrayInputStream("FT1.0 1 ADD 5 Fries9512 5.6 \n".getBytes("UTF8"))));
	    });
	}
	
	/*
	 * test for get request
	 */
	@Test
	public void decodeGetRequestTest() throws UnsupportedEncodingException, TokenizerException{
		Message i = MessageFactory.decode(new MessageInput(
				new ByteArrayInputStream("FT1.0 1 GET \n".getBytes("UTF8"))));
        assertAll(()->assertEquals("GET", i.getRequest()),
                () -> assertEquals(1, i.getTimestamp()));
	}
	/*
	 * test for error request 
	 */
	@Test
	public void decodeErrorRequestTest() throws UnsupportedEncodingException, TokenizerException{
		Message i = MessageFactory.decode(new MessageInput(
				new ByteArrayInputStream("FT1.0 3 ERROR 16 NON-EMPTY STRING\n".getBytes("UTF8"))));
        assertAll(()->assertEquals("ERROR", i.getRequest()),
                () -> assertEquals(3, i.getTimestamp()),
                () -> assertEquals("NON-EMPTY STRING", i.getMessage()) );
	}
	
	/*
	 * test for offset check in error request
	 */
	@Test
	public void decodeErrorInvalidInput() throws UnsupportedEncodingException, TokenizerException{
		 assertThrows(TokenizerException.class, () -> {
			MessageFactory.decode(new MessageInput(
					new ByteArrayInputStream("FT1.0 3 ERROR a -ON-EMPTY STRING\n".getBytes("UTF8"))));
	    });
	}
	
	/*
	 * test for list message
	 */
	@Test
	public void decodeListMessageTest() throws UnsupportedEncodingException, TokenizerException{
		Message i = MessageFactory.decode(new MessageInput(
				new ByteArrayInputStream("FT1.0 0 LIST 3 2 5 FriesB512 5.6 6 BurgerB999 132.1 \n".getBytes("UTF8"))));
		fiat.serialization.Item item1 = 
				new fiat.serialization.Item("Fries",fiat.serialization.MealType.Breakfast,512,5.6);
		fiat.serialization.Item item2 = 
				new fiat.serialization.Item("Burger",fiat.serialization.MealType.Breakfast,999,132.1);
		
		assertAll(()->assertEquals("LIST", i.getRequest()),
                () -> assertEquals(0, i.getTimestamp()),
                () -> assertEquals(3, i.getModifiedTimestamp()),
                () -> assertEquals(item1, i.getItemList().get(0)),
        		() -> assertEquals(item2, i.getItemList().get(1)));
	}
	
	/*
	 * test for encode add message
	 */
	@Test
	public void encodeAddMessage() throws IOException{
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		MessageOutput out = new MessageOutput(output);
		Item i = new Item("Fries",MealType.Breakfast,512,5.6);
		Add add = new Add(1,i);
		MessageFactory.encode(add,out);
		assertEquals("FT1.0 1 ADD 5 FriesB512 5.6 \n", out.getEncoding());
	}
	
	/*
	 * test for encode error message
	 */
	@Test
	public void encodeErrorMessage() throws IOException{
		fiat.serialization.Error e = new fiat.serialization.Error(3,"NON-EMPTY STRING");
		MessageFactory.encode(e,out);
		assertEquals("FT1.0 3 ERROR 16 NON-EMPTY STRING\n", out.getEncoding());
	}
	
	/*
	 * test for encode get message
	 */
	@Test
	public void encodeGetMessage() throws IOException{
		Get e = new Get(1);
		MessageFactory.encode(e,out);
		assertEquals("FT1.0 1 GET \n", out.getEncoding());
	}
	
	/*
	 * test for encode list message
	 */
	@Test
	public void encodeListMessage() throws IOException{
		ItemList i = new ItemList(0,3);
		i.addItem(new Item("Fries",fiat.serialization.MealType.Breakfast,512,5.6));
		i.addItem(new Item("Burger",fiat.serialization.MealType.Breakfast,999,132.1));
		MessageFactory.encode(i,out);
		assertEquals("FT1.0 0 LIST 3 2 5 FriesB512 5.6 6 BurgerB999 132.1 \n", out.getEncoding());
	}
	
	
	/*
	 * test for null output stream
	 */
	@Test
	void encodeNullPointerOut() throws IOException {
	    assertThrows(NullPointerException.class, () -> {
			MessageFactory.encode(new Get(1), null);
	    });	
	}
	/*
	 * test for null message
	 */
	@Test
	void encodeNullPointerItem() throws IOException {
	    assertThrows(NullPointerException.class, () -> {
	    	MessageFactory.encode(null, out);
	    });	
	}
	
	/*
	 * test for negative offset
	 */
	@Test
	public void decodeAndEncode() throws TokenizerException, IOException{
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		MessageOutput out = new MessageOutput(output);
		Item i = new Item("Fries",MealType.Breakfast,512,5.6);
		Add add = new Add(1,i);
		MessageFactory.encode(add,out);
		assertEquals("FT1.0 1 ADD 5 FriesB512 5.6 \n", out.getEncoding());
	}
	
}
