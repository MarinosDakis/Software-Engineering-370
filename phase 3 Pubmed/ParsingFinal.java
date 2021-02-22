import java.net.*;
import java.util.*;
import java.io.*;
import java.net.URLConnection;
import java.awt.image.*;
import javax.imageio.*;
import java.net.MalformedURLException;

///////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                          provided in class (below)                                    //
///////////////////////////////////////////////////////////////////////////////////////////////////////////

// this method gets the information from an intputted URL
class WebpageReaderWithAgent { //start WebpageReaderWithAgent

	public static final String USER_AGENT = "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.2) Gecko/20100115 Firefox/3.6";

	// reads from webpage
	public static InputStream getURLInputStream(String sURL) throws Exception {
		URLConnection oConnection = (new URL(sURL)).openConnection();
		oConnection.setRequestProperty("User-Agent", USER_AGENT);
		return oConnection.getInputStream();
	} // getURLInputStream

	//reads through buffer
	public static BufferedReader read(String url) throws Exception {
		InputStream content = (InputStream) getURLInputStream(url);
		return new BufferedReader(new InputStreamReader(content));
	} // read

} // end WebpageReaderWithAgent

//This is a modified example based on the code from from the book _Java in a Nutshell_ by David Flanagan.
//Written by David Flanagan.  Copyright (c) 1996 O'Reilly & Associates.
// this method displays the information about a URL
class GetURLInfo { // start GetURLInfo
	public static byte[] printURLinfo(URLConnection uc) throws IOException {
		
		// Display the URL address, and information about it.
		StringBuilder URLcontents = new StringBuilder();
		URLcontents.append((uc.getURL().toExternalForm() + ":\n"));
		URLcontents.append(("  Content Type: " + uc.getContentType() + "\n"));
		URLcontents.append(("  Content Length: " + uc.getContentLength() + "\n"));
		URLcontents.append(("  Last Modified: " + new Date(uc.getLastModified()) + "\n"));
		URLcontents.append(("  Expiration: " + uc.getExpiration() + "\n"));
		URLcontents.append(("  Content Encoding: " + uc.getContentEncoding() + "\n\n"));

		// turn the toString of the StringBuilder into byte format so that it can be written
		byte[] bytedata = URLcontents.toString().getBytes();

		return bytedata;
		
	} // printURLinfo

} // end GetURLInfo


// References used from word document:
// bytestreams: https://docs.oracle.com/javase/tutorial/essential/io/bytestreams.html 
// byte sizes: https://www.mkyong.com/java/how-to-get-file-size-in-java/ 

///////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                          provided in class (above)                                    //
///////////////////////////////////////////////////////////////////////////////////////////////////////////


public class ParsingFinal { // start Parser class

	// The following method checks if a given path exists
	static boolean PathExist(String path) { // start PathExist

		// store given path in file
		File pathTest = new File(path);

		// check if path exists
		Boolean inputValid = pathTest.exists();

		return inputValid;

	}// end PathExist

	// The following method checks if a given filename exists
	static Boolean FileExist(String fileName) { // start FileExist

		// store given file name in a file
		File file = new File(fileName);

		// get the path given fileName
		String filePath = file.getAbsolutePath();

		// check if that path exists
		return PathExist(filePath);

	} // End FileExist

	// the following method prints all the info of a URL by passing through a string.
	// the method calls "GetURLInfo", which was provided in class
	static void GetUrlInfo(String inputtedURL, String dirPath, String outputFile)
			throws IOException, MalformedURLException { // start GetUrlInfo

		URL url = new URL(inputtedURL); // create URL with current string in input
		URLConnection connection = url.openConnection();  
		GetURLInfo.printURLinfo(connection);

		// store contents of GetURLInfo into output file
		File targetFile = new File(outputFile);
		FileOutputStream out = new FileOutputStream(targetFile, true); // true so file is not overwritten
		out.write(GetURLInfo.printURLinfo(connection));

		System.out.println();
		
		// close writer
		out.close();

	} // end GetUrlInfo

	// the following method saves image files of a URL & outputs data to output file
	// the method uses code provided in class from "GetURLImage" & "WebpageReaderWithAgent" classes
	static void SaveImagetoFile(String imgURL, String pathToDir, String outputPath)
			throws MalformedURLException, IOException, Exception { // start SaveImageToFile

		System.out.println("Current URL being processed: " + imgURL + "\n");
		
		//process IMG URL except for GIF
		BufferedImage img = null;
		URL url = new URL(imgURL); // image that contains IMG URL
		img = ImageIO.read(url); // reads image
		
		// need to use bytestream to read Gif URL or it doesn't save as animated
		InputStream inputGIF = WebpageReaderWithAgent.getURLInputStream(imgURL); // stores data from URL
		
		// to store path of IMG copy
		File outputImageFile = null;

		// creating flags for different endings
		Boolean endInJPG = false, endInJPEG = false, endInGIF = false;

		if (imgURL.endsWith(".jpg") || imgURL.endsWith(".JPG"))
			endInJPG = true;
		if (imgURL.endsWith(".jpeg") || imgURL.endsWith(".JPEG"))
			endInJPEG = true;
		if (imgURL.endsWith(".gif") || imgURL.endsWith(".GIF"))
			endInGIF = true;

		// Case jpg/jpeg
		if (endInJPG || endInJPEG) { // start jpg/jpeg case

			outputImageFile = new File(pathToDir + "\\" + Rename(imgURL)); // to store at given path
			ImageIO.write(img, "jpg", outputImageFile); // writes image

		} // end jpg/jpeg case

		// Case Gif
		if (endInGIF) { // start case gif
		
			outputImageFile = new File(pathToDir + "\\" + Rename(imgURL)); // to store given at path
			FileOutputStream outputGif = new FileOutputStream(outputImageFile); // need to store bytestream to output file
			
			// writes entire bytestream until reaches end
			int stopper;
			while((stopper = inputGIF.read()) != -1) {
				outputGif.write(stopper);
			}
			
			//close outputStream
			outputGif.close();

		} // end gif case

		else if (endInJPG == false && endInJPEG == false && endInGIF == false) // no case
			System.out.println("There was an error saving the image");

		// writing additional data to file:
		File targetFile = new File(outputPath);
		FileWriter out = new FileWriter(targetFile, true); // true so file is not overwritten
		out.write("The name of the file is: " + Rename(imgURL) + "\n"); // output name of file
		out.write("The total number of bytes contained within the file is: " + outputImageFile.length() + "\n"); // output bytes
		out.write("-----------------------------------------------------------------------------------------------\n");
		
		// close writer
		out.close();

	} // end SaveImageToFile

	// this method is called to create the name of the URL to be saved
	private static String Rename(String stringToBeProcessed) {

		// set index originally at the end
		int slashIndex = stringToBeProcessed.length();

		// decrease counter until it matches the final / position
		while (stringToBeProcessed.charAt(--slashIndex) != '/') {
			if (slashIndex < 0)
				break;
		}

		// create new substring starting from after last / to get file name of URL
		String newString = stringToBeProcessed.substring(slashIndex + 1);

		// removes characters that may be invalid
		newString = newString.replaceAll("/", " ").replace('*', ' ').replace('?', ' ').replace('<', ' ')
				.replace('>', ' ').replace('|', ' ').replaceAll("\"", " ").replaceAll(":", " ").trim();

		return newString;

	}

	// this method saves the HTML file into a separate HTML file within the inputted directory
	// & outputs data to output file
	// the method uses code provided in class from "WebpageReaderWithAgent" class
	static void PrintHTML(String inputURL, String pathToDir, String outputPath) throws Exception { // start printHTML
																											
		System.out.println("Current URL being processed: " + inputURL + "\n");

		// process the URL to be outputted
		BufferedReader reader = WebpageReaderWithAgent.read(inputURL);
		String line = reader.readLine();
		int lineCounter = 0; // to count total # of lines read

		// create new file to store HTML data
		File fileContainsHTMLData = new File(pathToDir + "\\" + Rename(inputURL) + ".html"); // will contain Html data
		FileWriter writeToFile = new FileWriter(fileContainsHTMLData); // writes to html file

		// write to file line by line of HTML file
		while (line != null) { // start while

			writeToFile.write(line + "\n"); // write current copied line
			line = reader.readLine(); // get next line of html code
			lineCounter++; 

		} // end while

		// writing data to file:
		File targetFile = new File(outputPath);
		FileWriter out = new FileWriter(targetFile, true); // true so file is not overwritten
		out.write("Number of lines read: " + lineCounter + "\n"); // output # lines
		out.write("The name of the file is: " + Rename(inputURL) + "\n"); // output name of file
		out.write("The total number of bytes contained within the file is: " + fileContainsHTMLData.length() + "\n"); // output # bytes
		out.write("-----------------------------------------------------------------------------------------------\n");

		// close writers
		writeToFile.close();
		out.close();

	} // end printHTML

	static void PrintDOC(String inputURL, String pathToDir, String outputPath) throws Exception { // start printHTML
																											
		System.out.println("Current URL being processed: " + inputURL + "\n");

		// create new file to store DOC data
		File fileContainsDOCData = new File(pathToDir + "\\" + Rename(inputURL)); 

		// create byte streams
		FileOutputStream writeToFile = new FileOutputStream(fileContainsDOCData); // writes to DOC
		InputStream input = WebpageReaderWithAgent.getURLInputStream(inputURL); // stores data from URL

		// writes entire bytestream until reaches end
		int stopper; // will stop when file = -1;
		while ((stopper = input.read()) != -1) {
			writeToFile.write(stopper);
		}

		// writing name of file and other info:
		File targetFile = new File(outputPath);
		FileWriter out = new FileWriter(targetFile, true); // true so file is not overwritten
		out.write("The name of the file is: " + Rename(inputURL) + "\n"); // output name of file
		out.write("The total number of bytes contained within the file is: " + fileContainsDOCData.length() + "\n"); // output # bytes
		out.write("-----------------------------------------------------------------------------------------------\n");

		// close writers
		writeToFile.close();
		out.close();
		
	} // end printDOC

}// end Parser class
