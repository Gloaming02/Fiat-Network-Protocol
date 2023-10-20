/************************************************
*
* Author: Zhengyan Hu
* Assignment: Fiat Food IntAke Tracker
* Class: CSI4321
*
************************************************/

package fiat.serialization;



/** 
 * Meal type
 * @author Zhengyan Hu
 * @version 1.0
 * @since 1.0
*/
public enum MealType{
    /**
     * Breakfast
     */
	Breakfast,
    /**
     * Lunch
     */
	Lunch,
    /**
     * Dinner
     */
	Dinner,
    /**
     * Snack
     */
	Snack;
	
	/**
	* Get meal type for given code
	*
	* @param code code of meal type
	* 
	* @return meal type corresponding to code
	* @throws IllegalArgumentException if bad code value
	*/
	public static MealType getMealType(char code) {
		switch(code) {
	      case 'B':
	        return Breakfast;

	      case 'D':
	        return Dinner;

	      case 'L':
	        return Lunch;

	      case 'S':
	        return Snack;

	      default:
	    	  //if code is in valid we will throw exception
	    	  throw new IllegalArgumentException("code is in valid we will throw exception"+ code);
		}
	}
	
	/**
	* Get code for meal type
	* 
	* @return meal type code
	* @throws IllegalArgumentException if meal type is not in BDLS
	*/
	public char getMealTypeCode() {
		switch(this) {
	      case Breakfast:
	        return 'B';

	      case Dinner:
	        return 'D';

	      case Lunch:
	        return 'L';

	      case Snack:
	        return 'S';

	      default:
	    	  //if meal type is not in BDLS
	    	  throw new IllegalArgumentException("code is in valid we will throw exception" + this);
	      }
	}

}
