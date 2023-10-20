/************************************************
*
* Author: Zhengyan Hu
* Assignment: Fiat Food IntAke Tracker
* Class: CSI4321
*
************************************************/

package fiat.serialization;


import java.util.Objects;
import java.util.regex.Pattern;

/** 
 * Represents Item and provides serialization/deserialization
 * @author Zhengyan Hu
 * @version 1.0
 * @since 1.0
*/
public class Item {


	/**
	 * represents Maximum of the a string length and integer
	 */
	static final int MAXIMUN_INTEGER =2048;
	
	/**
	 * represents Maximum of the a double
	 */
	static final double MAXIMUN_DOUBLE =100000.0;
	
	/**
	 * represents regular expression of punctuation
	 */
	static final String PUNCTUATION  = "\\p{P}";
	/**
	 * represents regular expression of symbol
	 */
	static final String SYMBOL  = "\\p{S}";
	
	/**
	 * represents regular expression of digital or numeric
	 */
	static final String LETTER_OR_NUMERIC = "[\\p{L}\\p{N}]";
	
	/**
	 * represents regular expression of tab
	 */
	static final char TAB  = '\t';
	/**
	 * maximum value of time stamp
	 */
	static final long MAX_OF_TIMESTAMP = (long) (Math.pow(2, 63)-1);
	
	/**
	 * name of item
	 */
	private String name;
	/**
	 * type of meal of this item
	 */
	private MealType mealType;
	/**
	 * calories contain in item
	 */
	private int calories;
	/**
	 * fat contain in item
	 */
	private double fat;
	
	/**
	* Constructs item with set values
	*
	* @param name name of item
	* @param mealType type of meal
	* @param calories number of calories in item
	* @param fat grams of fat in item
	* 
	* @throws IllegalArgumentException
	* if validation fails (e.g., invalid/null name, etc.)
	*/
	public Item(String name, MealType mealType, int calories, double fat) {
		// check validation of parameter 		
		if(!CheckNonEmptyString(name)) {
			throw new IllegalArgumentException("second invalid name" + name);
		}
		
		if(mealType == null) {
			throw new IllegalArgumentException("invalid mealType" + mealType);
		}
		
		if(!(CheckInteger(calories))) {
			throw new IllegalArgumentException("invalid calories" + mealType);
		}
		
		if(!CheckDouble(fat)) {
			throw new IllegalArgumentException("invalid fat" + mealType);
		}
		
		//assign value
		this.name = name;
		this.mealType = mealType;
		this.calories = calories;
		this.fat = fat;
	}
	


	/**
	* Returns name
	*
	* @return name 
	*/
	public String getName() {
		return name;
	}

	/**
	* Sets name
	* @param name new name
	* @return this object with new value
	* @throws IllegalArgumentException
	*  if validation fails (e.g., name is null/invalid)
	*/
	public Item setName(String name) {
		if(!CheckNonEmptyString(name)) {
			throw new IllegalArgumentException("second invalid name" + name);
		}
		this.name = name;
		return this;
	}

	/**
	* Returns mealType
	*
	* @return mealType 
	*/	
	public MealType getMealType() {
		return mealType;
	}

	/**
	* Sets mealType
	* @param mealType new mealType
	* @return this object with new value
	* @throws IllegalArgumentException
	*  if mealType is null
	*/
	public Item setMealType(MealType mealType) {
		// check validation of parameter 
		if(mealType == null) {
			throw new IllegalArgumentException("invalid mealType" + mealType);
		}
		
		this.mealType = mealType;
		return this;
	}

	/**
	* Returns calories
	*
	* @return calories 
	*/
	public int getCalories() {
		return calories;
	}

	/**
	* Sets calories
	* 
	* @param calories new calories
	* 
	* @return this object with new value
	* @throws IllegalArgumentException
	*  if negative calories or calories exceed maximum
	*/	
	public Item setCalories(int calories) {
		// check validation of parameter 
		if(!(CheckInteger(calories))) {
			throw new IllegalArgumentException("invalid calories" + mealType);
		}
		this.calories = calories;
		return this;
	}

	/**
	* Returns fat
	*
	* @return fat 
	*/
	public double getFat() {
		return fat;
	}

	/**
	* Sets fat
	* @param fat new fat
	* @return this object with new value
	* @throws IllegalArgumentException
	*  if negative fat or fat exceed maximum
	*/
	public Item setFat(double fat) {
		// check validation of parameter 
		if(!CheckDouble(fat)) {
			throw new IllegalArgumentException("invalid fat" + mealType);
		}
		this.fat = fat;
		return this;
	}
	
	/**
	* Returns string of the form
	* &lt;name&gt; with &lt;cal&gt; calories 
	* and &lt;fat&gt;g of fat eaten at &lt;meal&gt;
	* Note: fat value must be rounded to nearest integer For example
	* Orange with 5 calories and 7g of fat eaten at Breakfast
	* 
	*/
	@Override
	public String toString() {
		return name + " with " + calories + " calories and " 
				+ Math.round(fat) + "g of fat eaten at "
				+ mealType;
		
	}
	
	
	/**
	 * Returns a hash code value for the object. 
	 * @return a hash code value for this object.
	 * 
	 */
	@Override
	public int hashCode() {
		return Objects.hash(calories, fat, mealType, name);
	}

	
	/**
	 * compare two item if they are equals
	 * @param obj - the reference Item with which to compare.
	 * @return true if this Item is the same as the obj argument; false otherwise.
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		return calories == other.calories && Double.doubleToLongBits(fat) == Double.doubleToLongBits(other.fat)
				&& mealType == other.mealType && Objects.equals(name, other.name);
	}

	/**
	 * validation for integer
	 * @param cal integer to be check
	 * @return validation
	 */
	public static boolean CheckInteger(int c) {
		return !(c > MAXIMUN_INTEGER || c < 0);
	}
	
	/**
	 * validation for double
	 * @param cal integer to be check
	 * @return validation
	 */
	public static boolean CheckDouble(double f) {

		return !(f > MAXIMUN_DOUBLE || f < 0);
	}
	

	/**
	 * validation for non empty string
	 * @param message string to be check
	 * @return boolean value for validation
	 */
	public static boolean CheckNonEmptyString(String message) {
		//check for empty and null value
		if (message == null || message == "" || message.length() > MAXIMUN_INTEGER) {
			return false;
		}
		//first char in string should be letter or digital
		if(!Pattern.matches(LETTER_OR_NUMERIC,Character.toString(message.charAt(0)))) {
			return false;
		}
		
		//check string should only be punctuation, letter, digital
		// and symbol
		for(int i = 1; i < message.length(); i ++) {
			if ((!Pattern.matches(PUNCTUATION, Character.toString(message.charAt(i)))
					&& !Pattern.matches(LETTER_OR_NUMERIC,Character.toString(message.charAt(i)))
					&& message.charAt(i) != ' '
					&& !Pattern.matches(SYMBOL, Character.toString(message.charAt(i))))
					|| message.charAt(i) == TAB) {
				return false;
			}
		}
		return true;
	}
	/**
	 * validation for Meal Type code
	 * @param code of meal type
	 * @return boolean value for validation
	 */
	public static boolean CheckMealTypeCode(char code) {
		String str = code + "";
		return ("BLDS".contains(str));
	}
	
}
