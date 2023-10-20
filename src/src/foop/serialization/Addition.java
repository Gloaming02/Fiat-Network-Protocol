
/************************************************
*
* Author: Zhengyan Hu
* Assignment: FOOP
* Class: CSI4321
*
************************************************/

package foop.serialization;

import java.util.Objects;

import fiat.serialization.Item;
import fiat.serialization.MealType;
/** 
 * Represents Addition packet
 * @author Zhengyan Hu
 * @version 3.0
 * @since 1.0
 */
public class Addition extends Message{
	/**
	 * name for item Addition
	 */
	private String name;
	/**
	 * mealType for item Addition
	 *
	 */
	private MealType mealType;
	/**
	 * calories for item Addition
	 *
	 */
	private int calories;
	/**
	 * Constructs from given values
	 *
	 * @param msgID message ID
	 * @param name name of item
	 * @param mealType type of meal
	 * @param calories number of calories in item
	 * @throws IllegalArgumentException if validation fails
	 */
	public Addition(int msgID, String name, MealType mealType, int calories) {
		if(!(checkMsgID(msgID))) {
			throw new IllegalArgumentException("invalid message ID " + msgID);
		}
		if(!(checkName(name))) {
			throw new IllegalArgumentException("invalid name " + name);
		}
		if(mealType == null) {
			throw new IllegalArgumentException("invalid mealType " + mealType);
		}
		
		if(!(Item.CheckInteger(calories))) {
			throw new IllegalArgumentException("invalid calories " + calories);
		}
		this.msgID = msgID;
		this.name = name;
		this.mealType = mealType;
		this.calories = calories;
	}
	/**
	 * Returns string of the form
	 * Addition: MsgID=msgid Name=name Calories=calories Meal=mealtype
	 * @return string of the form
	 */
	@Override
	public String toString() {
		return "Addition: MsgID="+msgID+" Name="+name+" Calories="+calories+" Meal=" + mealType;
	}
	
	/**
	 * Returns name
	 * @return name
	 */
	public String getName() {
		return name;
	}
	/**
	 * Sets name
	 * @param name new name
	 * @return this object with new value
	 * @throws IllegalArgumentException if validation fails
	 */
	public Addition setName(String name) {
		if(!(checkName(name))) {
			throw new IllegalArgumentException("invalid name " + name);
		}
		this.name = name;
		return this;
	}
	
	/**
	 * Returns meal type
	 * @return meal type
	 */
	public MealType getMealType(){
		return mealType;
	}
	
	/**
	 * Sets meal type
	 * @param mealType new mealType
	 * @return this object with new value
	 * @throws IllegalArgumentException if validation fails
	 */
	public Addition setMealType(MealType mealType) {
		if(mealType == null) {
			throw new IllegalArgumentException("invalid mealType " + mealType);
		}
		this.mealType = mealType;
		return this;
	}
	
	/**
	 * Returns calories
	 * @return calories
	 */
	public int getCalories() {
		return calories;
	}
	/**
	 * Sets calories
	 * @param calories new calories
	 * @return this object with new value
	 * @throws IllegalArgumentException if validation fails
	 */
	public Addition setCalories(int calories) {
		if(!(Item.CheckInteger(calories))) {
			throw new IllegalArgumentException("invalid calories " + calories);
		}
		this.calories = calories;
		return this;
	}
	
	/**
	 * Returns a hash code value for the object. 
	 * @return a hash code value for this object.
	 * 
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(calories, mealType, msgID, name);
		return result;
	}
	/**
	 * compare two item if they are equals
	 * @param obj - the reference Add with which to compare.
	 * @return true if this Add is the same as the obj argument; false otherwise.
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Addition other = (Addition) obj;
		return calories == other.calories && mealType == other.mealType && msgID == other.msgID
				&& Objects.equals(name, other.name);
	}
}
