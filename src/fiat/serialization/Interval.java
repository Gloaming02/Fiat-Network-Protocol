/************************************************
*
* Author: Zhengyan Hu
* Assignment: Fiat Food IntAke Tracker
* Class: CSI4321
*
************************************************/

package fiat.serialization;

import java.util.Objects;

/** 
 * Represents a Interval and provides serialization/deserialization
 * @author Zhengyan Hu
 * @version 1.0
 * @since 1.0
 */
public class Interval extends Message{
	/**
	 * Request string for interval
	 */
	public static final String REQUEST_INTERVAL = "INTERVAL"; 
	
	/**
	 * interval time 
	 */
	int interval;

	/**
	 * Constructs Interval using set values
	 * @param messageTimestamp message timestamp
	 * @param intervalTime minutes into the past for interval
	 * 
	 * @return this object with new value
	 * @throws IllegalArgumentException if invalid error message
	 */
	public Interval(long messageTimestamp, int intervalTime){
		if (!checkTimeStamp(messageTimestamp)) {
			throw new IllegalArgumentException("invalid times stamp value: " + messageTimestamp);
		}
		this.timestamp = messageTimestamp;
		if (!Item.CheckInteger(intervalTime)) {
			throw new IllegalArgumentException("invaild interval time (0 < intervalTime < "
					+ MAXIMUN_INTEGER + ") obj:" + intervalTime);
		}

		interval = intervalTime;
	}
	/**
	 * Return the interval
	 * @return the interval
	 */
	public int getIntervalTime() {
		return interval;
	}
	
	/**
	 * Returns request (e.g., ADD)
	 * @return request
	 */
	public String getRequest() {
		return REQUEST_INTERVAL;
	}
	
	/**
	 * Set the interval
	 * @param intervalTime interval time
	 * @return this object with new value
	 * @throws IllegalArgumentException if invalid error message
	 */
	public Interval setIntervalTime(int intervalTime) {
		if (!Item.CheckInteger(intervalTime)) {
			throw new IllegalArgumentException("invaild interval time (0 < intervalTime < "
					+ MAXIMUN_INTEGER + ") obj:" + intervalTime);
		}

		interval = intervalTime;
		return this;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(interval);
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Interval other = (Interval) obj;
		return interval == other.interval;
	}
	/**
	 * Returns string of the form
	 * INTERVAL (TS=ts) time=time
	 * @return return the string type of object
	 */
	public String toString() {
		return "INTERVAL (TS=" + timestamp + ") time=" + interval;
	}
}
