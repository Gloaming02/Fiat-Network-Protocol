/************************************************
*
* Author: Zhengyan Hu
* Assignment: Fiat Food IntAke Tracker
* Class: CSI4321
*
************************************************/

package fiat.serialization;


import java.io.IOException;
import java.util.regex.Pattern;

/** 
 * Factory to create deserialized and serialized items
 * @author Zhengyan Hu
 * @version 1.0
 * @since 1.0
*/
public class ItemFactory  {
	
	/**
	* Constructs item using deserialization
	*
	* @param in deserialization input source
	* 
	* @return new (deserialized) item
	* @throws NullPointerException if in is null
	* @throws TokenizerException
	*  if validation failure (e.g., invalid name, etc.), I/O problem, etc.
	*/
	public static Item decode(MessageInput in) throws TokenizerException{
		if(in == null) {
			throw new NullPointerException("input cannot be null" + in);
		}
				
		//initialize the value for creating item
		String name = null;
		MealType mealType = null;
		int calories = -1;
		double fat = -1.1;
		
		//record the offset of current position for throwing exception
		int offset = 0;

		//read the character count for name
		String first;

		first = in.readBySpace();
		//check validation for character count which can only be integer
		for(int i = 0; i < first.length(); i ++) {
			//if there is a character that is not a digital,
			//throw exception with the position of it
			if (!Character.isDigit(first.charAt(i))) {
				throw new TokenizerException(i, 
						"character count should be integer offset: " + i);
			}
		}
		
		//store the character count as integer
		int charCount = Integer.parseInt(first);
        
		if (charCount > 2048 || charCount < 0) {
			throw new TokenizerException(offset, "charCount out of range: " + offset);
		}
		
		//read name from stream with length of name
		//this is for include some special character such as " "
		String s = in.readByChar(charCount + 1);
		
		name = s.substring(0, s.length() - 1);
		
		if(name == "") {
			throw new TokenizerException(offset, "invalid name" + name);
		}
		if(!Character.isLetterOrDigit(name.charAt(0))) {
			throw new TokenizerException(offset, "invalid name" + name);
		}
		for(int i = 1; i < name.length(); i ++) {
			if ((!Pattern.matches("\\p{Punct}", Character.toString(name.charAt(i)))
					&& !Character.isLetterOrDigit(name.charAt(i))
					&& name.charAt(i) != ' '
					&& !Pattern.matches("[^\\u2012-\\u204A]", Character.toString(name.charAt(i))))
					|| name.charAt(i) == '\t') {
				throw new TokenizerException(offset,"second invalid name" + name);
			}
		}
		
		
		String meal = s.substring(s.length() - 1);

		//update the offset to current position
		offset += (first.length() + charCount);
		//read one char from the stream as meal
		
		//check validation for meal type = B|L|D|S
		offset += 1;
		if(!("BLDS".contains(meal))){
			throw new TokenizerException(offset,"mealType should be in B|L|D|S offset: " + meal + ", " + offset);
		}
		//store meal type
		mealType = MealType.getMealType(meal.charAt(0));

		//read the rest of the message from stream
		String calString = in.readBySpace();
		String fatString = in.readBySpace();
		
		//check validation for calories which should be a positive integer
		offset ++;
		for(int i = 0; i < calString.length(); i ++) {
			//if there is a character that is not a digital,
			//throw exception with the position of it
			if (!Character.isDigit(calString.charAt(i))) {
				throw new TokenizerException(i + offset, 
						"calories count should be positive integer offset: " + calString + ", " + (i + offset));
			}
		}
		//parse and store the calories and update offset

		offset += (calString.length() + 1);
		calories = Integer.parseInt(calString);
		if (calories > 2048) {
			throw new TokenizerException(offset, "calories out of range: " + calories + ",  " + offset);
		}
		
		
		//check validation for fat which should be a positive float
		int dot = 0;
		for(int i = 0; i < fatString.length(); i ++) {
			//record for dot in the string
			if(fatString.charAt(i) == '.') {
				dot ++;
			}
			//throws exception when detect a char which is non-digital and not a dot
			// or the dot in the string is more than 1
			if ((!Character.isDigit(fatString.charAt(i)) && fatString.charAt(i) != '.') 
					|| dot > 1) {
				throw new TokenizerException(i + offset ,
						"fat should be float offset: " + fatString + (i + offset));
			}
		}
		if(dot == 0) {
			throw new TokenizerException(offset, "fat should be float: "+ fatString + offset);
		}
		//store the fat
        fat = Double.parseDouble(fatString);
        
		if (fat > 100000.0 || fat < 0) {
			throw new TokenizerException(offset, "fat out of range: " + fatString + offset);
		}
		

		return new Item(name, mealType, calories, fat);
		
	}

	
	/**
	* Serializes item
	*
	* @param item item to serialize
	* @param out output sink target for serialization
	* 
	* @throws NullPointerException if in is null
	* @throws IOException if I/O problem
	*/
	public static void encode(Item item, MessageOutput out) throws IOException{
		//throws NullPointerException if item OR out is null
		if(out == null ||  item == null) {
			throw new NullPointerException("message or item cannot be null");
		}
		// append item size of name to the string
		String encoding = Integer.toString(item.getName().length());
		encoding += " ";
		// append item name to the string
		encoding += item.getName();
		// append item meal type to the string
		encoding += item.getMealType().getMealTypeCode();
		// append item calories to string
		encoding += Integer.toString(item.getCalories());
		encoding += " ";
		// append item fat to string
		encoding += Double.toString(item.getFat());
		encoding += " ";
		out.writeMessage(encoding);
	}
}
