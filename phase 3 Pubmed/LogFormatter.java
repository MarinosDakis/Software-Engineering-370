
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter { // start LogFormatter class

	public static void main(String[] args) { // start main

	}// end main

	// |date|methodName|
	@Override
	public String format(LogRecord record) { // start of format() method
		StringBuilder logString = new StringBuilder();

		// Initializing data for Logger
		String date = null, methodName = null;

		// date
		date = getDate();
		if (date == null)
			date = "-1";

		// methodName
		methodName = record.getSourceMethodName();
		if (methodName == null)
			methodName = "-1";

		// |date|
		logString.append(date);
		logString.append(" | ");

		// |methodName|
		logString.append(record.getMessage());
		logString.append(" \n ");

		// returns completed String
		return logString.toString();

	} // end of format() method

	// method gets current date
	public static String getDate() { // start of getDate() method
		// create date
		Date date = new Date();
		// simple date format for logger
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		return dateFormat.format(date);
	}// end of getDate() method

	// create methods to edit data for transition logs

} // end LogFormatter class
