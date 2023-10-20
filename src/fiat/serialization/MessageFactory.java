/************************************************
*
* Author: Zhengyan Hu
* Assignment: Fiat Food IntAke Tracker
* Class: CSI4321
*
************************************************/

package fiat.serialization;

import java.io.IOException;
import java.util.List;


/** 
 * Factory to create deserialized and serialized items
 * @author Zhengyan Hu
 * @version 1.0
 * @since 1.0
*/
public class MessageFactory {
	/**
	 * end of line char
	 */
	static final char END_OF_LINE = '\n';  
	/**
	 * represent version in string
	 */
	static final String VERSION = "FT1.0";  

	/**
	 * Request an empty string
	 */
	static final String EMPTY_STRING = "";  
	/**
	 * Request space string
	 */
	static final String SPACE = " ";  

	/**
	* Constructs message using deserialization
	*
	* @param in deserialization input source
	* @return new (deserialized) message
	* @throws NullPointerException if in is null
	* @throws fiat.serialization.TokenizerException if validation failure 
	* (e.g., invalid name, etc.), I/O problem, etc.
	*/
	public static Message decode(MessageInput in) throws TokenizerException{
		//check in is not null
		if(in == null) {
			throw new NullPointerException("MessageInput cannot be null" + in);
		}
		//set the offset
		int offset = 0;
		//read the version from input stream
		String version = in.readBySpace();
		//update offset
		offset += version.length();
		
		//compare current version to input version 
		if(!VERSION.equals(version)) {
			//different version throw exception
			throw new TokenizerException(offset, 
					"Version not match in " + offset + " obj: " + version); 
		}
		//update offset
		//read the time stamp
		String timestempString = in.readBySpace();
		//update offset
		offset += timestempString.length() + 1;
		// initialize time stamp
		long timestemp = 0;
	    try {
	    	//convert time stamp from string to long
			timestemp = Long.parseLong(timestempString);  
	    } catch (final NumberFormatException e) {
	    	//convert fail, wrong input
			throw new TokenizerException(offset, 
					"Timestemp should be long value" + offset + " obj: " + version);
	    }
    	//validation of time stamp 
		if(!Message.checkTimeStamp(timestemp)) {
			throw new TokenizerException(offset, "modified time of range: " + timestemp + ", " + offset);
		}
		
		//update offset
		offset ++;
		//read request from input stream
		String request = in.readBySpace();
		
		//different request decode in different way
		if(Add.REQUEST_ADD.equals(request)) {
			// in add request
			
			// get a item from input
			Item i = ItemFactory.decode(in);
			offset += Add.REQUEST_ADD.length();

			char c;
			//check if read end of line
			if((c = in.readNext()) != END_OF_LINE) {
				throw new TokenizerException(offset, "no end of line detected" + c + ", " + offset);
			}
			//return add request
			return new Add(timestemp, i);
			
		}else if(Get.REQUEST_GET.equals(request)) {
			// in get request
			offset += Get.REQUEST_GET.length();
			//check if read end of line
			char c;
			if((c = in.readNext()) != END_OF_LINE) {
				throw new TokenizerException(offset, "no end of line detected" + c + ", " + offset);
			}
			//return get request
			return new Get(timestemp);
			
		}else if(ItemList.REQUEST_LIST.equals(request)) {
			// in List request

			//read the modify time stamp
			String number = in.readBySpace();
			//initialize for parsing
			long modifiedtime = -1;
			//update offset
			offset += ItemList.REQUEST_LIST.length();
			offset += number.length();
			try {
				//convert modified time from string to integer
				modifiedtime = Long.parseLong(number);
			}catch (NumberFormatException ex) {
				//input wrong, convert fail
				throw new TokenizerException(offset, "invalid modified: "+ number + ", " + offset);
		    }
			//validation of integer(modified time)
			if(modifiedtime < 0 || modifiedtime > Message.MAX_OF_TIMESTAMP) {
				throw new TokenizerException(offset, "modified time of range: "+ modifiedtime+ ", " + offset);
			}

			//read number of item from input stream
			number = in.readBySpace();
			//initialize for parsing
			int count = 0;
			offset += number.length();
			try {
				//convert number of items from string to integer
				count = Integer.parseInt(number);
			}catch (NumberFormatException ex) {
				//convert fail, invalid input
				throw new TokenizerException(offset, "invalid integer: " + count +", "+ offset);
		    }
			//validation for number(integer)
			if (!Item.CheckInteger(count)) {
				throw new TokenizerException(offset, "list count out of range: " + count +", "+ offset);
			}
			//initialize time list for collection
			ItemList items = new ItemList(timestemp, modifiedtime);
			// read n numbers of item from input
			for(int i = 0; i < count; i++) {
				//add each item to the item list
				items.addItem(ItemFactory.decode(in));
			}
			//c for collection the unexpected char
			char c;
			if((c = in.readNext()) != END_OF_LINE) {
				//no end of line reach, throw what we read
				throw new TokenizerException(offset, "no end of line detected "+ c +", " + offset);
			}
			//return item list
			return items;
			
		}else if(Error.REQUEST_ERROR.equals(request)) {
			//in error request 
			offset += Error.REQUEST_ERROR.length();
			//read the count of string
			String first = in.readBySpace();
			//validation for integer
			for(int i = 0; i < first.length(); i ++) {
				if (!Character.isDigit(first.charAt(i))) {
					offset ++; 
					throw new TokenizerException(offset, 
							"character count should be integer offset: " + first + ", " + offset);
				}
			}
			
			//parse the count of character to integer
			int charCount = Integer.parseInt(first);
			//check character is in valid range
			if (!Item.CheckInteger(charCount)) {
				throw new TokenizerException(offset, "charCount out of range: " +charCount+", "+ offset);
			}
			//read name from input stream with one more end of line
			String s = in.readByChar(charCount + 1);
			//get rid of last element(end of line)
			String name = s.substring(0, s.length() - 1);

			//validation for non empty string
			if(!Item.CheckNonEmptyString(name)){
				throw new TokenizerException(offset, "invalid string" +name+", "+ offset);
			}
			//check if last element is end of line
			char c;
			if((c = s.charAt(s.length()-1)) != END_OF_LINE) {
				//throw with the last element and offset
				throw new TokenizerException(offset, "no end of line detected" + c + ", "+ offset);
			}
			// return the error with time stamp and name
			return new fiat.serialization.Error(timestemp, name);
		}else if(Interval.REQUEST_INTERVAL.equals(request)) {
			//in error request 
			offset += Interval.REQUEST_INTERVAL.length();
			//read the time which is a integer
			String first = in.readBySpace();
			//validation for integer
			for(int i = 0; i < first.length(); i ++) {
				if (!Character.isDigit(first.charAt(i))) {
					offset ++; 
					throw new TokenizerException(offset, 
							"time interval should be integer offset: " + first + ", " + offset);
				}
			}
			
			//parse the count of character to integer
			int charCount = Integer.parseInt(first);
			//check character is in valid range
			if (!Item.CheckInteger(charCount)) {
				throw new TokenizerException(offset, "Time(Integer) is out of range" +charCount+", "+ offset);
			}
			//make sure there is nothing else in the message
			if(in.readNext() != '\n') {
				throw new TokenizerException(offset, "Wrong packet detected" +charCount+", "+ offset);
			}
			return new Interval(timestemp,charCount);
		}

		// the input request was wrong not in ADD|LIST|GET|ERROR
		throw new TokenizerException(offset, 
				"incorrect request" + offset + " obj: " + request);
		
	}
	
	/**
	* Serializes item
	*
	* @param msg message to serialize
	* @param out output sink target for serialization
	* @throws NullPointerException if msg is null
	* @throws IOException if I/O problem
	*/
	public static void encode(Message msg,
			 		MessageOutput out) throws IOException{
		//check msg or out is null
		if(msg == null || out == null) {
			throw new NullPointerException("output or message cannot be null"+ out + msg);
		}
		//add the version and time stamp as the begining
		String result = VERSION + SPACE + msg.getTimestamp() + SPACE;
		
		//output for each request
		if(Add.REQUEST_ADD.equals(msg.getRequest())) {
			//append request string 
			result += Add.REQUEST_ADD;
			result += SPACE;
			//write string to output 
			out.writeMessage(result);
			//write the item using encode function
			ItemFactory.encode(msg.getItem(),out);
		}else if(Error.REQUEST_ERROR.equals(msg.getRequest())) {
			//append error string 
			result += Error.REQUEST_ERROR;
			result += SPACE;
			//append character count 
			result += msg.getMessage().length();
			result += SPACE;
			
			//append message 
			result += msg.getMessage();
			//write to output
			out.writeMessage(result);
		}else if(Get.REQUEST_GET.equals(msg.getRequest())) {
			//append get string 
			result += Get.REQUEST_GET;
			result += SPACE;
			
			//write to output
			out.writeMessage(result);
		}else if(ItemList.REQUEST_LIST.equals(msg.getRequest())) {
			//append list string 
			result += ItemList.REQUEST_LIST;
			result += SPACE;
			
			// append modified time
			result += msg.getModifiedTimestamp();
			result += SPACE;
			
			//append number of items in the list
			List<Item> temp = msg.getItemList();
			result += temp.size();
			result += SPACE;
			
			//write to output
			out.writeMessage(result);
			//iterate over the item list and write all the item to output
			for(Item e : temp) {
				ItemFactory.encode(e,out);
			}
		}else if(Interval.REQUEST_INTERVAL.equals(msg.getRequest())) {
			//append interval string 
			result += Interval.REQUEST_INTERVAL;
			result += SPACE;
			//append integer 
			result += msg.getIntervalTime();
			result += SPACE;
			//write to output
			out.writeMessage(result);
			
		}
		//finally write \n to output stream
		out.writeMessage(Character.toString(END_OF_LINE));
	}
	
}
