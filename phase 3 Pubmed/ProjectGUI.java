
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ProjectGUI extends Frame { // start of projectGUI class
	
	public static String textAreaResults; // stores contents of Text Area after the user goes on to review
	
	Hashtable<String, Integer> storage = new Hashtable<String, Integer>(); // main storage

	// this constructor will hold all the elements of the GUI
	ProjectGUI(String outputPath, String inputPath) throws Exception {

		// global variables

		// setting the GUI to appear close to center of the screen
		// uses toolkit to get the user's dimension of their screen and sets it so GUI
		// compiles close to center of user's screen
		Dimension screen_of_User = Toolkit.getDefaultToolkit().getScreenSize(); // gets information from pc
		int x_screen_of_User = (int) screen_of_User.getWidth() / 4; // gets x value of user screen
		int y_screen_of_User = (int) screen_of_User.getHeight() / 4; // gets y value of user screen
		setLocation(x_screen_of_User, y_screen_of_User); // sets area where GUI is supposed to compile

		// closing window when pressing x
		addWindowListener(new WindowAdapter() { // windows listener to create event to close window of GUI
			public void windowClosing(WindowEvent e) { // event that will close window

				// want to save contents of GUI, so data isn't deleted

				dispose(); // dispose method used to close window of GUI when x is pressed [all components
							// removed, hence GUI closes]
							// better to use rather than System.exit() [too abrupt on JVM]
			}// end windows event
		});// end WindowListener

		// sets up the layout of the GUI

		// miscellaneous design features
		setSize(350, 320); // sets the size of the frame
		setTitle("Data Retreiver"); // sets the title of the frame
		this.setBackground(Color.LIGHT_GRAY); // sets GUI background to light gray

		// layout settings
		GridBagLayout gridLayout = new GridBagLayout();
		setLayout(gridLayout);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 10, 5, 10); // top left bottom right
		gbc.anchor = GridBagConstraints.NORTH; // pulls components to top

		// ###################################################################################
		// # DO NOT TOUCH ANYTHING BELOW PLEASE #
		// ###################################################################################
		// first row of GUI (search bar & search button) | textfield | searchButton |
		// clearButton |

		// create components for first row

		// Textfield [searchBar] component (left)
		TextField searchBar = new TextField();
		searchBar.setFont(new Font("Arial", Font.PLAIN, 12));

		// Button [searchButton] component (middle)
		Button searchButton = new Button("Search");
		searchButton.setFont(new Font("Arial", Font.PLAIN, 12));

		// Button [clearButton] component (right)
		Button clearButton = new Button("Clear");
		clearButton.setFont(new Font("Arial", Font.PLAIN, 12));

		// adjust constraints for first row
		gbc.fill = GridBagConstraints.HORIZONTAL; // Fills constraints in their horizontal pos
		gbc.weightx = 1; // Controls how additional space is distributed to components in the
							// row(x)/col(y)

		gridLayout.setConstraints(searchBar, gbc); // adds to gridlayout with current constraints
		add(searchBar); // adds to Layout

		gbc.gridwidth = GridBagConstraints.RELATIVE; // used when last row/col comp; GUI will start new sector
		gridLayout.setConstraints(searchButton, gbc);
		add(searchButton);

		gbc.gridwidth = GridBagConstraints.REMAINDER; // used when last row/col comp; GUI will start new sector
		gridLayout.setConstraints(clearButton, gbc);
		add(clearButton);

		// #######################################################################################
		// second row of gui (checkboxes) | toggle1 | toggle 2 | toggle 3 |
		// create components for second row

		// Checkbox [toggleBoxAll (left)], [toggleBoxExact (right)] components
		CheckboxGroup checkBoxGroup = new CheckboxGroup(); // checkbox group that holds the checbox comps
		Checkbox toggleBoxAll = new Checkbox("All", checkBoxGroup, true);
		toggleBoxAll.setFont(new Font("Arial", Font.PLAIN, 12));
		Checkbox toggleBoxExact = new Checkbox("Exact", checkBoxGroup, false);
		toggleBoxExact.setFont(new Font("Arial", Font.PLAIN, 12));

		// adjust constraints for second row
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 1; // how much width comp takes
		gbc.anchor = GridBagConstraints.CENTER;
		gridLayout.setConstraints(toggleBoxAll, gbc);
		add(toggleBoxAll);

		gbc.gridwidth = GridBagConstraints.REMAINDER; // used when second to last row/col comp, or that will be placed
														// next to previous comp
		gridLayout.setConstraints(toggleBoxExact, gbc);
		add(toggleBoxExact);

		// #######################################################################################
		// third row of gui (TextArea) | TEXTAREA |

		// create components for third row

		// TextArea [resultGUI] component (Center)
		TextArea resultGUI = new TextArea();
		resultGUI.setBackground(Color.WHITE); // background of text area is white
		resultGUI.setEditable(false); // makes text area uneditable

		// adjust constraints for third row
		gbc.fill = GridBagConstraints.VERTICAL; // makes component expand vertically with remain space
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gridLayout.setConstraints(resultGUI, gbc);
		add(resultGUI);
		// #######################################################################################
		// fourth row of gui (review button) | button |

		// Button [reviewButton] components (center)
		Button reviewButton = new Button("Review");
		reviewButton.setFont(new Font("Arial", Font.PLAIN, 12));

		// adjust constraints for fourth row
		gbc.weightx = 1; // Controls how additional space is distributed to components in the
							// row(x)/col(y)

		gridLayout.setConstraints(reviewButton, gbc); // adds to gridlayout with current constraints
		add(reviewButton); // adds to Layout

		// #######################################################################################
		// #######################################################################################
		// #######################################################################################
		// #######################################################################################
		// CREATING ACTIONS FOR ALL THE COMPONENTS ABOVE:

		// Making search button use the inputted value to search through the pubmed
		// website
		
		searchButton.addActionListener(new ActionListener() { // start action listenter

			@Override
			public void actionPerformed(ActionEvent e) { //start event

				// store the url of the page you want to visit
				String theURL = "https://www.ncbi.nlm.nih.gov/pubmed/?term=" + searchBar.getText(); // to store link of url
				theURL.replace(" ", "").trim(); // safety net

				// read all of the information from the page:
				BufferedReader reader = null;
				String urlInfo = "";
				try {
					reader = WebpageReaderWithAgent.read(theURL); // Method provided in class
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				String line = null; // to read from buffered Reader
				try {
					line = reader.readLine();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				StringBuilder urlStrings = new StringBuilder(); //stores all read from buffered reader
				
				// write to file line by line to the stringBuilder
				while (line != null) { // start while

					urlStrings.append(line);

					try {
						line = reader.readLine();
					} catch (IOException e1) {

						e1.printStackTrace();
					}
				}//end while

				// separating only the urls found from the search to be displayed in the search box
				// E.g. https://www.ncbi.nlm.nih.gov/pubmed/32364094

				String regex = "(pubmed/\\d{8})";
				Pattern pattern = Pattern.compile(regex);
				Matcher match = pattern.matcher(urlStrings.toString());

				// Search through all the matches contained from the Stringbuilder and everytime
				// the group, i.e. (pubmed/\\d{8})
				// is found then append it to the TextArea
				while (match.find()) { //start while
					String partOfURL = match.group(1);
					resultGUI.append("https://www.ncbi.nlm.nih.gov/" + partOfURL + "\n");
					HashTable.addToTable(storage, "https://www.ncbi.nlm.nih.gov/" + partOfURL + "\n" );
					HashTable.setLogger("SEARCH | https://www.ncbi.nlm.nih.gov/" + partOfURL + "\n");
				} //end while
			}// end event

		}); // end listener 
		

		// clear textArea
		clearButton.addActionListener(new ActionListener() {//start action listener

			@Override
			public void actionPerformed(ActionEvent e) {//start event
				resultGUI.setText(""); // clears text Area
				searchBar.setText(""); // clear search bar
				storage.clear();
				HashTable.setLogger("CLEAR" );
			} //end action event
		});//end event listener
		
		// review
		reviewButton.addActionListener(new ActionListener() { //start action listener
			public void actionPerformed(ActionEvent e) { //start event
				
				// makes first GUI invisible
				setVisible(false); 
				
				//Store data in textArea of first gui
				textAreaResults = resultGUI.getText();
				
				// opening new GUI if pressed				
				projectGUI2 projectscreen2 = new projectGUI2(outputPath, inputPath); // opens second GUI
				HashTable.setLogger("REVIEW");
				
			} //end action event
		}); //end event listener
		
		// #######################################################################################
		// #######################################################################################
		// #######################################################################################
		// #######################################################################################

		
		// ###################################################################################
		// # DO NOT TOUCH ABOVE HERE PLEASE #
		// ###################################################################################

		setVisible(true); // makes GUI screen visible to user

	} // end of projectGUI constructor

	class projectGUI2 extends Frame { // start of projectGUI2 class

		projectGUI2(String outputPath, String inputPath) { // start of projectGUI2 constructor

			// setting the GUI to appear close to center of the screen
			// uses toolkit to get the user's dimension of their screen and sets it so GUI
			// compiles close to center of user's screen
			Dimension screen_of_User = Toolkit.getDefaultToolkit().getScreenSize(); // gets information from pc
			int x_screen_of_User = (int) screen_of_User.getWidth() / 4; // gets x value of user screen
			int y_screen_of_User = (int) screen_of_User.getHeight() / 4; // gets y value of user screen
			setLocation(x_screen_of_User, y_screen_of_User); // sets area where GUI is supposed to compile

			// closing window when pressing x
			addWindowListener(new WindowAdapter() { // windows listener to create event to close window of GUI
				public void windowClosing(WindowEvent e) { // event that will close window

					// want to save contents of GUI, so data isn't deleted

					dispose(); // dispose method used to close window of GUI when x is pressed
				}// end windows event
			});// end WindowListener

			// sets up the layout of the GUI

			// miscellaneous design features
			setSize(400, 350); // sets the size of the frame
			setTitle("Database Manager"); // sets the title of the frame
			this.setBackground(Color.LIGHT_GRAY); // sets GUI background to light gray

			// layout settings
			GridBagLayout gridLayout = new GridBagLayout();
			setLayout(gridLayout);
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(5, 10, 5, 10); // top left bottom right
			gbc.anchor = GridBagConstraints.NORTH; // pulls components to top

			// ###################################################################################
			// # DO NOT TOUCH ANYTHING BELOW PLEASE #
			// ###################################################################################
			// first row of GUI (search bar & search button) | textfield | button |

			// create components for first row

			// Textfield [searchBar] components (left)
			TextField searchBar = new TextField();
			searchBar.setFont(new Font("Arial", Font.PLAIN, 12));

			// Button [searchButton] components (Right)
			Button searchButton = new Button("Search");
			searchButton.setFont(new Font("Arial", Font.PLAIN, 12));

			// adjust constraints for first row
			gbc.fill = GridBagConstraints.HORIZONTAL; // Fills constraints in their horizontal pos
			gbc.weightx = 1; // Controls how additional space is distributed to components in the
								// row(x)/col(y)

			gridLayout.setConstraints(searchBar, gbc); // adds to gridlayout with current constraints
			add(searchBar); // adds to Layout

			gbc.gridwidth = GridBagConstraints.REMAINDER;
			gridLayout.setConstraints(searchButton, gbc);
			add(searchButton);

			// #######################################################################################
			// second row of gui (buttons) | insertButton1 | deleteButton2 | modifyButton |
			// create components for second row

			Button insertButton = new Button("Insert");
			insertButton.setFont(new Font("Arial", Font.PLAIN, 12));
			Button deleteButton = new Button("Delete");
			deleteButton.setFont(new Font("Arial", Font.PLAIN, 12));
			Button modifyButton = new Button("Modify");
			modifyButton.setFont(new Font("Arial", Font.PLAIN, 12));
			// opening previous GUI if pressed
			modifyButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// super.setVisibe(true);
				}
			});

			// adjust constraints for second row
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridwidth = 1; // how much width comp takes
			gbc.anchor = GridBagConstraints.CENTER;
			gridLayout.setConstraints(insertButton, gbc);
			add(insertButton);

			gbc.gridwidth = GridBagConstraints.RELATIVE; // used when second to last row/col comp, or that will be
															// placed next to previous comp
			gridLayout.setConstraints(deleteButton, gbc);
			add(deleteButton);

			gbc.gridwidth = GridBagConstraints.REMAINDER;
			gridLayout.setConstraints(modifyButton, gbc);
			add(modifyButton);
			// #######################################################################################
			// third row of gui (TextArea) | TEXTAREA |

			// create components for third row

			// TextArea [resultGUI] component (Center)
			TextArea resultGUI = new TextArea();
			resultGUI.setBackground(Color.WHITE); // background of text area is white
			resultGUI.setEditable(false); // makes text area uneditable
			
			// transfer data from previous GUI
			resultGUI.append(ProjectGUI.textAreaResults);
			
			

			// adjust constraints for third row
			gbc.fill = GridBagConstraints.VERTICAL; // makes component expand vertically with remain space
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			gridLayout.setConstraints(resultGUI, gbc);
			add(resultGUI);
			// #######################################################################################
			// fourth row of gui (export button) | button |

			// Button [modifyButton] components (center)
			Button exportButton = new Button("Export");
			exportButton.setFont(new Font("Arial", Font.PLAIN, 12));

			// adjust constraints for fourth row
			gbc.weightx = 1; // Controls how additional space is distributed to components in the
								// row(x)/col(y)

			gridLayout.setConstraints(exportButton, gbc); // adds to gridlayout with current constraints
			add(exportButton); // adds to Layout
			
			
			//export all information into a file 		
			exportButton.addActionListener(new ActionListener()  { //start action listener
			public void actionPerformed(ActionEvent e) { //start event

				
				// reading the URLs line by line inside the input file
				Scanner readingInput = new Scanner(resultGUI.getText());
				while (readingInput.hasNextLine()) { // start while
					String tempInputURL = readingInput.nextLine(); // store temp URL to be processed
					if (tempInputURL.isEmpty())
						break; // end case at end of input file

					// creating flags for different inputs
					// if inputs are .htm / .html / .txt
					boolean inputFlag_IMG = false, inputFlag_DOC = false;
	
					// if inputs are .jpeg/ .jpg/ .gif
					if (tempInputURL.endsWith(".jpeg") || tempInputURL.endsWith(".jpg")
							|| tempInputURL.endsWith(".gif")|| tempInputURL.endsWith(".JPEG") ||
							tempInputURL.endsWith(".JPG") || tempInputURL.endsWith(".GIF"))
						inputFlag_IMG = true;

					// if inputs are .pdf/.docx/
					if (tempInputURL.endsWith(".pdf") || tempInputURL.endsWith(".docx") ||
							tempInputURL.endsWith(".PDF") || tempInputURL.endsWith(".DOCX"))
						inputFlag_DOC = true;

					else if (inputFlag_IMG != true && inputFlag_DOC != true) { //other cases
						// checking if given fileName has a path

							// print URL info
							try {
								ParsingFinal.GetUrlInfo(tempInputURL, inputPath, outputPath);
							} catch (IOException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}

							// print HTML data
							try {
								ParsingFinal.PrintHTML(tempInputURL, inputPath, outputPath);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

						}

					// IMG
					else if (inputFlag_IMG == true) { // start of check to see if IMG path exists

							// print URL info
							try {
								ParsingFinal.GetUrlInfo(tempInputURL, inputPath, outputPath);
							} catch (IOException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}

							// save image to computer
							try {
								ParsingFinal.SaveImagetoFile(tempInputURL, inputPath, outputPath);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} // img url, pathToDIr

						}
						

					// DOC
					else if (inputFlag_DOC == true) { // start of check to see if DOC path exists

							// print URL info
							try {
								ParsingFinal.GetUrlInfo(tempInputURL, inputPath, outputPath);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							// pring DOC info
							try {
								ParsingFinal.PrintDOC(tempInputURL, inputPath, outputPath);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}


						}
						
					} // end while

	              	readingInput.close(); // close scanner
	              	
	              	try {
						HashTable.exportFile(storage, outputPath);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
	              	HashTable.setLogger("EXPORT | Location: " + outputPath);
				 
			} //end action event
		}); //end event listener
			
			deleteButton.addActionListener(new ActionListener() {//start action listener

				@Override
				public void actionPerformed(ActionEvent e) {//start event
					
					String todelete = searchBar.getText(); //store element you want to delete
					HashTable.removeFromTable(storage, todelete); // remove from hashtable
					
					String tempData = resultGUI.getText(); //store current data inside a temp variable
					tempData = tempData.replaceAll(todelete, ""); // replace the field to be removed from the table
					resultGUI.setText(""); // clear current table
					resultGUI.append(tempData.toString()); // put back data without containing deleted element
					
				} //end action event
			});//end event listener
			
			deleteButton.addActionListener(new ActionListener() {//start action listener

				@Override
				public void actionPerformed(ActionEvent e) {//start event
					
					String todelete = searchBar.getText(); //store element you want to delete
					HashTable.removeFromTable(storage, todelete); // remove from hashtable
					
					String tempData = resultGUI.getText(); //store current data inside a temp variable
					tempData = tempData.replaceAll(todelete, ""); // replace the field to be removed from the table
					resultGUI.setText(""); // clear current table
					resultGUI.append(tempData.toString()); // put back data without containing deleted element
					HashTable.setLogger("DELETE | " + todelete + "\n");
					
				} //end action event
			});//end event listener
			
			
			insertButton.addActionListener(new ActionListener() {//start action listener

				@Override
				public void actionPerformed(ActionEvent e) {//start event
					
					String toAdd = searchBar.getText(); //store element you want to delete
					HashTable.addToTable(storage, toAdd); // remove from hashtable
					
					StringBuilder tempData = new StringBuilder(resultGUI.getText()); //store current data inside a temp variable
					tempData.append(toAdd + "\n"); // Add new field to the current data
					resultGUI.setText(""); // clear current table
					resultGUI.append(tempData.toString()); // put back data containing added element
					HashTable.setLogger("INSERT | " + toAdd + "\n");
					
				} //end action event
			});//end event listener			
			
			searchButton.addActionListener(new ActionListener() { // start action listenter

				@Override
				public void actionPerformed(ActionEvent e) { //start event

					// store the url of the page you want to visit
					String theURL = "https://www.ncbi.nlm.nih.gov/pubmed/?term=" + searchBar.getText(); // to store link of url
					theURL.replace(" ", "").trim(); // safety net

					// read all of the information from the page:
					BufferedReader reader = null;
					String urlInfo = "";
					try {
						reader = WebpageReaderWithAgent.read(theURL); // Method provided in class
					} catch (Exception e2) {
						e2.printStackTrace();
					}
					String line = null; // to read from buffered Reader
					try {
						line = reader.readLine();
					} catch (IOException e2) {
						e2.printStackTrace();
					}
					StringBuilder urlStrings = new StringBuilder(); //stores all read from buffered reader
					
					// write to file line by line to the stringBuilder
					while (line != null) { // start while

						urlStrings.append(line);

						try {
							line = reader.readLine();
						} catch (IOException e1) {

							e1.printStackTrace();
						}
					}//end while

					// separating only the urls found from the search to be displayed in the search box
					// E.g. https://www.ncbi.nlm.nih.gov/pubmed/32364094

					String regex = "(pubmed/\\d{8})";
					Pattern pattern = Pattern.compile(regex);
					Matcher match = pattern.matcher(urlStrings.toString());

					// Search through all the matches contained from the Stringbuilder and everytime
					// the group, i.e. (pubmed/\\d{8})
					// is found then append it to the TextArea
					while (match.find()) { //start while
						String partOfURL = match.group(1);
						resultGUI.append("https://www.ncbi.nlm.nih.gov/" + partOfURL + "\n");
						HashTable.addToTable(storage, "https://www.ncbi.nlm.nih.gov/" + partOfURL + "\n" );
						HashTable.setLogger("SEARCH | https://www.ncbi.nlm.nih.gov/" + partOfURL + "\n");
				
					} //end while
				}// end event

			}); // end listener 
			
			
			

			// ###################################################################################
			// # DO NOT TOUCH ABOVE HERE PLEASE #
			// ###################################################################################

			setVisible(true); // makes GUI screen visible to user

		} // end of projectGUI2 constructor
	} // end of projectGUI2 class

	
	public static void main(String[] args) throws Exception {// start main
	
		
		// other variables
				int i = 0;
				String arg;


				String inputPath = ""; // will store the input Path of Directory
				String outputPath = ""; // will store the output Path to store File

				// standard output if user only runs .class file without inputting an additional parameter
				if (i == args.length)
					System.err.println("Format: ParsingFinal.java [-help] [-d \"file_path\"] [-o output_file] [-i input_file]");

				// searches inputs that begin with "-"
				while (i < args.length && args[i].startsWith("-")) { // start of cases with "-" in front
					arg = args[i++];	
		
		
					// -HELP
					if (arg.equals("-help")) { // start -help if statement

						// show user the format for commands
						System.out.println("Format: ParsingFinal.java [-help] [-d \"file_path\"] [-o output_file]");

					} // end -help if statement

					
					
					// Directory (-d)
					if (arg.equals("-d")) { // start -d if statement

						if (i < args.length) { // start args check

							// store path entered by user
							String tempInputPath = args[i++];

							// checking if path exists on user's computer
							Boolean logValid = ParsingFinal.PathExist(tempInputPath);

							if (logValid == true) { // output name of file & path if true
								
								inputPath = tempInputPath;
								System.out.println();
								System.out.println("Directory Path found\n");
								
							}

							else
								System.err.println("Path does not exist, try again and make sure you surround it with \" \" ");

						} // end args check

						else
							System.err.println("Please enter a valid directory, and make sure you surround it with \" \" ");
						
					} // end -d if statement
		
					
					// Output (-o)
					if (arg.equals("-o")) { // start -output if statement

						if (i < args.length) {
							// store outputFile after -o
							String tempOutputFile = args[i++];

							// check if output is really .txt
							boolean outputFlag = tempOutputFile.endsWith(".txt");

							if (outputFlag == true) { // start of check to see if path exists

								// checking if tempOutputFile location exists
								Boolean outputValid = ParsingFinal.FileExist(inputPath + "\\" + tempOutputFile);

								// if exists
								if (outputValid == true) {
									outputPath = inputPath + "\\" + tempOutputFile;
									System.out.println("Output File path established");
									System.out.println();
								} else if (outputValid == false) {
									System.err.println("file Path could not be found");
								}
							} // end of check to see if path exists

							else // if error in original output, output error
								System.err.println("-o requires an output file, please try again");
							
						} // end if args.length

					} // end -output if statement
				
					
				// Input (-i)
				if (arg.equals("-i")) { // start -i if statement
					
					if (i < args.length) { // start args check

						// store input file after -i
						String inputtedFileName = args[i++];

						// need to separate each url ending here through cases.
						String tempInputURL = null;
						FileInputStream read = null;

						try { // checking if input file exists
							read = new FileInputStream(inputPath + "\\" + inputtedFileName); 
						} catch (FileNotFoundException e) {
							System.out.println("There was an error with the input file, please try again");
							e.printStackTrace();
						}

						// reading the URLs line by line inside the input file
						Scanner readingInput = new Scanner(read);
						while (readingInput.hasNextLine()) { // start while
							tempInputURL = readingInput.nextLine(); // store temp URL to be processed
							if (tempInputURL.isEmpty())
								break; // end case at end of input file

							// creating flags for different inputs
							// if inputs are .htm / .html / .txt
							boolean inputFlag_HTML = false, inputFlag_IMG = false, inputFlag_DOC = false;
							if (tempInputURL.endsWith(".htm") || tempInputURL.endsWith(".html")
									|| tempInputURL.endsWith(".txt") || tempInputURL.endsWith(".HTM") ||
									tempInputURL.endsWith(".HTML") || tempInputURL.endsWith(".TXT"))
								inputFlag_HTML = true;

							// if inputs are .jpeg/ .jpg/ .gif
							if (tempInputURL.endsWith(".jpeg") || tempInputURL.endsWith(".jpg")
									|| tempInputURL.endsWith(".gif")|| tempInputURL.endsWith(".JPEG") ||
									tempInputURL.endsWith(".JPG") || tempInputURL.endsWith(".GIF"))
								inputFlag_IMG = true;

							// if inputs are .pdf/.docx/
							if (tempInputURL.endsWith(".pdf") || tempInputURL.endsWith(".docx") ||
									tempInputURL.endsWith(".PDF") || tempInputURL.endsWith(".DOCX"))
								inputFlag_DOC = true;

							// HMTL
							if (inputFlag_HTML == true) { // start of check to see if HTML path exists

								// checking if given fileName has a path
								Boolean inputValid = ParsingFinal.FileExist(inputPath + "\\" + inputtedFileName);

								if (inputValid == true) { // if the directory exists save onto separate file on system

									// print URL info
									ParsingFinal.GetUrlInfo(tempInputURL, inputPath, outputPath);

									// print HTML data
									ParsingFinal.PrintHTML(tempInputURL, inputPath, outputPath);

								} else if (inputValid == false) { // otherwise output error
									System.err.println("HTML Path could not be found");
								}
								
							} // end of check to see if HTML path exists

							// IMG
							else if (inputFlag_IMG == true) { // start of check to see if IMG path exists

								// checking if given fileName has a path
								Boolean inputValid = ParsingFinal.FileExist(inputPath + "\\" + inputtedFileName);

								if (inputValid == true) { // if the directory exists save onto separate file on system

									// print URL info
									ParsingFinal.GetUrlInfo(tempInputURL, inputPath, outputPath);

									// save image to computer
									ParsingFinal.SaveImagetoFile(tempInputURL, inputPath, outputPath); // img url, pathToDIr

								} else if (inputValid == false) { // otherwise output error
									System.err.println("IMG Path could not be found");
								}
								
							} // end of check to see if IMG path exists

							// DOC
							else if (inputFlag_DOC == true) { // start of check to see if DOC path exists

								// checking if given fileName has a path
								Boolean inputValid = ParsingFinal.FileExist(inputPath + "\\" + inputtedFileName);

								if (inputValid == true) { // if the directory exists save onto separate file on system

									// print URL info
									ParsingFinal.GetUrlInfo(tempInputURL, inputPath, outputPath);

									// pring DOC info
									ParsingFinal.PrintDOC(tempInputURL, inputPath, outputPath);

								} else if (inputValid == false) { // otherwise output error
									System.err.println("DOC Path could not be found");
								}
								
							} // end of check to see if DOC path exists

							else // if error in original input, output error
								System.err.println("-i requires an input file, please try again");

						} // end while
						
						readingInput.close(); // close scanner
						
						
					} // end if args length

				} // end -i if statement
		
			}// end - cases
		
   ProjectGUI gui = new ProjectGUI(outputPath, inputPath);
		
   

}//end main


			


}// end class
