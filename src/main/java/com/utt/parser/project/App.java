package com.utt.parser.project;

import java.util.Scanner;

public class App {
	public static void main(String[] args) {
		try {
			// If the user wants to check what is been logged into the file on the screen
			Logger.LOGTOMONITOR = false;
			Logger.getInstance().log("Started the parser program");
			Scanner scIn = new Scanner(System.in);
			Scanner scData = new Scanner(System.in);

			int ch1 = 0;
			boolean flag = true;
			Model model = new Model();
			while (ch1 != 3 && flag == true) {
				System.out.println(
						"Enter 1 to parse the Array of Json Object Data to excel and mail the excel file \nEnter 2 to convert excel data to Json \nEnter 3 to exit");
				if (scIn.hasNextInt()) {
					ch1 = scIn.nextInt();
				} else {
					ch1 = 0;
					scIn.next();
				}
				switch (ch1) {
				case 1: {
					System.out.println(
							"Entered 1 to parse the Array of Json Object Data to excel and mail the excel file");
					Logger.getInstance()
							.log("Entered 1 to parse the Array of Json Object Data to excel and mail the excel file");
					flag = true;
					int ch2 = 0;
					while (ch2 != 5 && flag == true) {
						System.out.println(
								"Enter 1 to parse the Json Data from text file \nEnter 2 parse Json data from pdf \nEnter 3 parse Json data from Api \nEnter 4 to exit \nEnter 5 to go back to previous option");
						if (scIn.hasNextInt()) {
							ch2 = scIn.nextInt();
						} else {
							ch2 = 0;
							scIn.next();
						}
						switch (ch2) {
						case 1: {
							System.out.println("Entered 1 to parse the Json Data from text file");
							Logger.getInstance().log("Entered 1 to parse the Json data from text file");
							System.out.println("Enter email id");
							String emailTo = scData.nextLine();
							model.readDataFromTextFile();
							model.sendEmail(emailTo);
							break;
						}
						case 2: {
							System.out.println("Entered 2 to parse the Array of Json Object Data from pdf");
							Logger.getInstance().log("Entered 2 to parse the Array of Json Object Data from pdf");
							model.readDataFromPdfFile();
							break;
						}
						case 3: {
							System.out.println("Entered 3 to parse the Array of Json Object Data from Api");
							Logger.getInstance().log("Entered 3 to parse the Array of Json Object Data from Api");

							break;
						}
						case 4: {
							System.out.println("Entered exit case");
							Logger.getInstance().log("Entered exit case");

							flag = false;
							break;
						}
						case 5: {
							System.out.println("Entered 5 to go back");
							Logger.getInstance().log("Entered 5 to go back");

							break;
						}
						default:
							System.out.println("Please enter the number from the option only");
							break;
						}
					}
					break;
				}
				case 2: {
					System.out.println("Entered 2 to convert excel data to Json");
					Logger.getInstance().log("Entered 2 to convert excel data to Json");
					model.excelToJson();
					break;
				}
				case 3: {
					System.out.println("Entered exit case");
					Logger.getInstance().log("Entered exit case");
					flag = false;
					break;
				}
				default:
					System.out.println("Please enter the number from the option only");
					break;

				}
			}
		} catch (Throwable t) {
			// TODO: handle exception
			t.printStackTrace();
			Logger.getInstance().log(t.getMessage());
		}
	}
}
