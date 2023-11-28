
/**
 * A main file, that will run our whole project.
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static Scanner scnr = new Scanner(System.in);

	public static void main(String[] args) throws Exception {
		Boolean quitSystem = false;
		Boolean validInput = false;
		int endChoice;

		do {
			BackendSystem backend = new BackendSystem();
			userRunSystem(backend);

			validInput = false;
			while (!validInput) {
				System.out.println();
				System.out.println("Choose an option: ");
				System.out.println("1 - Log in again");
				System.out.println("2 - Close program");

				endChoice = scnr.nextInt();

				if (endChoice == 1){
					validInput = true;
				}
				else if (endChoice == 2) {
					validInput = true;
					quitSystem = true;
				}
				else {
					System.out.println("Please enter 1 or 2.");
				}
			}
			
		} while (!quitSystem);
		

		scnr.close();
	}

	// prompt the user to log in and answer security questions, and then let them
	// perform one action at a time, until they want to quit
	public static void userRunSystem(BackendSystem backend) throws Exception { // used to be IOException FYI
		

		
		int userChoice;
		System.out.println(
				"Welcome to the UASAMS backend subsystem! Please select an option: \n1- Returning User\n2- New User");
		userChoice = scnr.nextInt();

		if (userChoice == 2) {
			backend.createNewProfile(); // TODO: INCOMPLETE- need to update profile lists when a new profile is added
		}

		
		String userInput = "";
		do {
			// prompt them to login (if they are not already logged in),
			// and then to do one action
			oneUserAction(backend);

			// see if they want to quit
			System.out.print("Are you sure you want to log out? (type y/n): ");
			userInput = scnr.next();
			// while the first letter of the user's input is either y or Y, keep going
		} while (userInput.toLowerCase().charAt(0) != 'y');
		
	}

	/**
	 * Prompts the user to log in if they are not already, then
	 * prompts them for which action they want to perform based on their profile
	 * type
	 */
	public static void oneUserAction(BackendSystem backend) throws IOException {
		int userSelection = -1;

		// if no one is currently logged in
		if (backend.getCurrentUser() == null) {
			// prompt them to log in
			boolean successfulLogin = backend.login();

			// if their login was not successful, exit this method early
			if (!successfulLogin) {
				return;
			}
		}

		// if (backend.getUserType().compareTo("student") == 0) {
		if (backend.getCurrentUser() instanceof StudentProfile) {
			/*
			 * - edit profile
			 * - see all scholarships
			 * - see in-progress applications/ update application
			 * - see submitted applications
			 * - search scholarships
			 */
			do {
				System.out.println("\nPlease enter a number to select which action you want to do:");

				System.out.println("1 - View and edit your profile");
				System.out.println("2 - See all active scholarships (can apply to them also)");
				System.out.println("3 - See applications in progress");
				System.out.println("4 - See submitted applications");
				System.out.println("5 - Search scholarships");
				System.out.println("6 - delete your profile and all associated data");

				System.out.println("0 - EXIT");

				System.out.print("Your selection: ");
				userSelection = scnr.nextInt();

				// is it as simple as this???
				if (userSelection == 1) {
					// typecast it to a StudentProfile because it will be a profile object,
					// but this is ok because we will only get here if currentUser is a student
					// object
					backend.editStudentInfo((StudentProfile) backend.getCurrentUser());
				}

				// see all scholarships and maybe apply
				else if (userSelection == 2) {
					Boolean quitBrowse = false;

					while (!quitBrowse) {
						// call printAllScholarships, we want basic info and no archived scholarships,
						// yes to not-archived,
						// yes approved scholarships, no unapproved scholarships
						backend.printAllScholarships(false, false, true, true, false);

						System.out.println("What would you like to do:");
						System.out.println("1 - Go back");
						System.out.println("2 - view one scholarship in more detail (can also apply to it)");

						System.out.print("Your choice: ");

						// want to keep this separate from userSelection, so we don't accidentally exit
						// the outer do-while loop or something
						int userAction = scnr.nextInt();
						scnr.nextLine();

						if (userAction == 1) {
							quitBrowse = true;
						} else if (userAction == 2) {

							int fileIndex;
							Boolean validSelection = false;
							do {
								System.out.print("Please enter the file index of the scholarship you want to view: ");
								fileIndex = scnr.nextInt();
								scnr.nextLine();

								if (backend.getOneScholarshipByFileIndex(fileIndex) == null) {
									System.out.println(
											"The scholarship with file index " + fileIndex + " could not be found.");
									System.out.println("Please enter a valid index.");
								} else {
									// print the scholarship's information, in more detail than before
									validSelection = true;
									System.out.println(
											backend.getOneScholarshipByFileIndex(fileIndex).getAllInfoString());

									System.out.println("\nPlease select an option:");
									System.out.println("1 - go back");
									System.out.println("2 - apply");

									System.out.print("Your choice: ");
									userAction = scnr.nextInt();
									scnr.nextLine();

									if (userAction == 1) {
										// do nothing?
									} else if (userAction == 2) {
										backend.applyToScholarship(fileIndex);
									}
								}

							} while (!validSelection);
						}

					}

				}
				// see applications in progress
				else if (userSelection == 3) {
					Boolean quit = false;
					ArrayList<MatchRelationship> matchesFound = backend.getInProgressApplications();
					MatchRelationship matchSelected = new MatchRelationship();

					while (!quit) {
						for (MatchRelationship match : matchesFound) {
							System.out.println(match.getScholarship().getBasicInfoString());
							System.out.println();
						}

						System.out.println("What would you like to do:");
						System.out.println("1 - Go back");
						System.out.println("2 - View one application in more detail");

						System.out.print("Your choice: ");

						int userAction = scnr.nextInt();
						scnr.nextLine();

						if (userAction == 1) {
							quit = true;
						} else if (userAction == 2) {

							int fileIndex;
							Boolean validSelection = false;
							do {
								System.out.print(
										"Please enter the file index of the scholarship you want to view the application for: ");
								fileIndex = scnr.nextInt();
								scnr.nextLine();

								if (backend.getOneScholarshipByFileIndex(fileIndex) == null) {
									System.out.println(
											"The scholarship with file index " + fileIndex + " could not be found.");
									System.out.println("Please enter a valid index.");
								} else {
									// print the scholarship's information, in more detail than before
									validSelection = true;
									for (MatchRelationship match : matchesFound) {
										if (match.getScholarshipName()
												.equals(backend.getOneScholarshipByFileIndex(fileIndex).getName())) {
											matchSelected = match;
										}
									}

									System.out.println(matchSelected.getDetailsString());

									System.out.println("\nPlease select an option:");
									System.out.println("1 - Go back");
									System.out.println("2 - Edit application");

									System.out.print("Your choice: ");
									userAction = scnr.nextInt();
									scnr.nextLine();

									if (userAction == 1) {
										// do nothing?
									} else if (userAction == 2) {
										backend.editApplication(matchSelected);
									}
								}

							} while (!validSelection);
						}

					}
				}
				// get submitted applications
				else if (userSelection == 4) {
					backend.getSubmittedApplications();
				}
				// search for scholarships
				else if (userSelection == 5) {
					Boolean quitSearch = false;
					Boolean exitResults;

					while (!quitSearch) {

						System.out.println("1 - name");
						System.out.println("2 - donor");
						System.out.println("3 - due date");
						System.out.println("4 - date posted");
						System.out.println("5 - award amount");
						System.out.println("6 - major");
						System.out.println("7 - minor");
						System.out.println("8 - isUSCitizen");
						System.out.println("9 - GPA");
						System.out.println("10 - inGoodStanding");
						System.out.println("11 - hasAdvStanding");
						System.out.println("12 - gradeLevel");
						System.out.println("13 - gradYear");
						System.out.println("14 - gender");
						System.out.println("15 - isFullTimeStudent");
						System.out.println("16 - isTransferStudent");
						System.out.println("17 - curNumCredits");
						System.out.println("18 - receivesFunding");

						System.out.print("Enter the number of the category you'd like to search by: ");
						int searchIndex = scnr.nextInt();
						scnr.nextLine();

						System.out.print("Enter the value to search (booleans should be 'true' or 'false'): ");
						String searchValue = scnr.nextLine();

						// uses search scholarship method to return search results
						ArrayList<Scholarship> scholarshipsFound = backend.searchScholarships(searchIndex, searchValue);

						exitResults = false;

						// program will stay within search results until requested to exit or do new
						// search
						while (!exitResults) {

							// if no scholarships are found
							if (scholarshipsFound.size() == 0) {
								System.out.println("No scholarships found!");

								System.out.println("What would you like to do:");
								System.out.println("2 - Enter new search criteria");
								System.out.println("3 - Exit search");

							}
							// offers all options if scholarships are found
							else {
								for (Scholarship schol : scholarshipsFound) {
									System.out.println(schol.getBasicInfoString() + "\n");
								}

								System.out.println("What would you like to do:");
								System.out.println("1 - Expand a scholarship");
								System.out.println("2 - Enter new search criteria");
								System.out.println("3 - Exit search");
							}

							System.out.print("Your choice: ");

							int userAction = scnr.nextInt();
							scnr.nextLine();

							// quit search
							if (userAction == 3) {
								exitResults = true;
								quitSearch = true;
							}
							// start new search
							else if (userAction == 2) {
								exitResults = true;
							}
							// look at scholarship
							else if (userAction == 1) {

								int fileIndex;
								Boolean validSelection = false;
								do {
									System.out
											.print("Please enter the file index of the scholarship you want to view: ");
									fileIndex = scnr.nextInt();
									scnr.nextLine();

									if (backend.getOneScholarshipByFileIndex(fileIndex) == null) {
										System.out.println("The scholarship with file index " + fileIndex
												+ " could not be found.");
										System.out.println("Please enter a valid index.");
									} else {
										// print the scholarship's information, in more detail than before
										validSelection = true;
										System.out.println(
												backend.getOneScholarshipByFileIndex(fileIndex).getAllInfoString());

										System.out.println("\nPlease select an option:");
										System.out.println("1 - go back");
										System.out.println("2 - apply");

										System.out.print("Your choice: ");
										userAction = scnr.nextInt();
										scnr.nextLine();

										if (userAction == 1) {
											// do nothing?
										} else if (userAction == 2) {
											backend.applyToScholarship(fileIndex);
										}
									}

								} while (!validSelection);
							}
						}

					}

				}

				// user wants to delete themselves
				else if (userSelection == 6) {
					StudentProfile loggedInStudent = (StudentProfile) backend.getCurrentUser();
					// delete the currentUser, which is a student
					backend.deleteStudentProfile(loggedInStudent);
					// set userSelection to 0 so it exits this loop within the code for a student,
					//  we don't want to let the student keep attempting actions after their account was deleted
					userSelection = 0;

					// TODO: is this right?? 
					// set the current user to null so it stops being the student that deleted themselves
					backend.setCurrentUser(null);
				}

			} while (userSelection != 0);
			scnr.nextLine();
		}
		// else if (backend.getUserType().compareTo("donor") == 0) {
		else if (backend.getCurrentUser() instanceof DonorProfile) {
			/*
			 * - create and submit scholarship for review
			 * 
			 */
			do {
				System.out.println("\nPlease enter a number to select which action you want to do:");

				System.out.println("1 - See your posted scholarships");
				System.out.println("2 - Enter a new scholarship");
				System.out.println("0 - EXIT");

				System.out.print("Your selection: ");
				userSelection = scnr.nextInt();

				if (userSelection == 1) {
					for (Scholarship scholarship : ((DonorProfile) backend.getCurrentUser()).getScholarships()) {
						System.out.println(scholarship.getBasicInfoString());
					}
				}
				// TODO: is there more to do for this?
				else if (userSelection == 2) {
					backend.createScholarshipFromInput();
				}
			} while (userSelection != 0);
			scnr.nextLine();
		}
		// else if (backend.getUserType().compareTo("staff") == 0) {
		else if (backend.getCurrentUser() instanceof StaffProfile) {
			/*
			 * -view all scholarships
			 * -archive scholarships
			 */
			System.out.println("Have to still implement oneUserAction() for StaffProfile.");
		}
		// else if (backend.getUserType().compareTo("fund steward") == 0) {
		else if (backend.getCurrentUser() instanceof FundStewardProfile) {
			// TODO: implement this!!
			/*
			 * -view archived/awarded scholarships
			 * 
			 */
			do {
				// querys user about what they would like to do
				System.out.println("\nPlease enter a number to select which action you want to do:");

				System.out.println("1 - View awarded scholarships");
				System.out.println("0 - EXIT");

				System.out.print("Your selection: ");
				userSelection = scnr.nextInt();

				// runs through viewing an awarded scholarship
				if (userSelection == 1) {
					// print out a list of all awarded scholarships that have been awarded
					backend.printArchivedScholarships(false, true, true, false);
					boolean quitBrowse = true;
					while (quitBrowse) {
						// gives the option to view a scholarship more indepth
						System.out.println("What would you like to do:");
						System.out.println("1 - Go back");
						System.out.println("2 - view one scholarship in more detail");

						System.out.print("Your choice: ");

						// need new variable for new loop
						int userAction = scnr.nextInt();
						scnr.nextLine();

						// If the user wants to quit, quitBrowse needs to be false
						if (userAction == 1) {
							quitBrowse = false;
						} else if (userAction == 2) {

							int fileIndex;
							Boolean validSelection = false;
							do {
								// Grabs the index so that that scholarship can be printed
								System.out.print("Please enter the file index of the scholarship you want to view: ");
								fileIndex = scnr.nextInt();
								scnr.nextLine();
								// if the user does not select a valid scholarship reinquires user
								if (backend.getOneScholarshipByFileIndex(fileIndex) == null) {
									System.out.println(
											"The scholarship with file index " + fileIndex + " could not be found.");
									System.out.println("Please enter a valid index.");
								} else {
									// print the scholarship's information, in more detail
									System.out.println(
											backend.getOneScholarshipByFileIndex(fileIndex).getAllInfoString());

									System.out.println("\nEnter 1 to go back: ");
									userAction = scnr.nextInt();
									scnr.nextLine();

									if (userAction == 1) {
										// if the answer is valid it is true and restarts outer loop
										validSelection = true;
									}
								}

							} while (!validSelection);
						}

					}
				}

			} while (userSelection != 0);

		}
		// else if (backend.getUserType().compareTo("admin") == 0) {
		else if (backend.getCurrentUser() instanceof AdminProfile) {
			/*
			 * - approve scholarships
			 * - search students
			 * - award scholarships
			 * - delete student profile
			 */
			do {
				System.out.println("Options: ");
				System.out.println("1 - View unapproved scholarships, and then can approve or delete them");
				System.out.println("2 - View all students");
				System.out.println("3 - Search students");
				System.out.println("4 - View all active scholarships");
				System.out.println("5 - View all archived scholarships");
				System.out.println("6 - Award scholarship");
				System.out.println("7 - Find and delete student profile");
				System.out.println("8 - View and change a student/scholarship match");

				System.out.println("0 - EXIT");

				System.out.print("Your selection: ");
				userSelection = scnr.nextInt();

				// if they want to view unapproved scholarships to approve them
				if (userSelection == 1) {
					System.out.println("Unapproved scholarships:");

					// call printAllScholarships, we want basic info and only unapproved
					// scholarships
					// TODO: would we want archived scholarships here?

					// prompt them to approve one scholarship

					Boolean quit = false;
					do {
						backend.printAllScholarships(false, false, true, false, true);

						System.out.println("What would you like to do:");
						System.out.println("1 - Go back");
						System.out.println("2 - view one scholarship in more detail");

						System.out.print("Your choice: ");

						// want to keep this separate from userSelection, so we don't accidentally exit
						// the outer do-while loop or something
						int userAction = scnr.nextInt();
						scnr.nextLine();

						if (userAction == 1) {
							quit = true;
						} else if (userAction == 2) {

							int fileIndex;
							Boolean validSelection = false;

							do {
								System.out.print("Please enter the file index of the scholarship you want to view: ");
								fileIndex = scnr.nextInt();
								scnr.nextLine();

								if (backend.getOneScholarshipByFileIndex(fileIndex) == null) {
									System.out.println(
											"The scholarship with file index " + fileIndex + " could not be found.");
									System.out.println("Please enter a valid index.");
								} else {
									// print the scholarship's information, in more detail than before
									validSelection = true;
									System.out.println(
											backend.getOneScholarshipByFileIndex(fileIndex).getAllInfoString());

									System.out.println("\nPlease select an option:");
									System.out.println("1 - Return to list");
									System.out.println("2 - Approve");
									System.out.println("3 - Delete");

									System.out.print("Your choice: ");
									userAction = scnr.nextInt();
									scnr.nextLine();

									if (userAction == 1) {
										// do nothing?
									} else if (userAction == 2) {
										backend.getOneScholarshipByFileIndex(fileIndex).setApproved(true);
										backend.updateScholarshipFile(backend.getOneScholarshipByFileIndex(fileIndex));

										System.out.println(backend.getOneScholarshipByFileIndex(fileIndex).getName()
												+ " is now approved!");
									} else if (userAction == 3) {
										System.out.println(
												"Are you sure you'd like to delete this scholarship from the system? (y/n)");

										String donorChoice = scnr.nextLine();

										if (donorChoice.equalsIgnoreCase("y")) {
											File folder = new File("scholarships/scholarship" + fileIndex);

											for (File child : folder.listFiles()) {
												child.delete();
											}
											
											folder.delete();

											int index = backend.getAllScholarships()
													.indexOf(backend.getOneScholarshipByFileIndex(fileIndex));
											backend.getAllScholarships().remove(index);

											System.out.println("Scholarship deleted.");
										} else {
											System.out.println("Action dismissed.");
										}

									}
								}

							} while (!validSelection);
						}

					} while (!quit);
				}

				// view all students
				else if (userSelection == 2) {
					Boolean quitBrowse = false;

					while (!quitBrowse) {
						// call printAllScholarships, we want basic info and no archived scholarships,
						// yes to not-archived,
						// yes approved scholarships, no unapproved scholarships
						for (StudentProfile student : backend.getAllStudents()) {
							System.out.println(student.getBasicDetailsString());
							System.out.println();
						}

						System.out.println("What would you like to do:");
						System.out.println("1 - Go back");
						System.out.println("2 - View a student in more detail");

						System.out.print("Your choice: ");

						// want to keep this separate from userSelection, so we don't accidentally exit
						// the outer do-while loop or something
						int userAction = scnr.nextInt();
						scnr.nextLine();

						if (userAction == 1) {
							quitBrowse = true;
						} else if (userAction == 2) {

							int fileIndex;
							Boolean validSelection = false;
							do {
								System.out.print("Please enter the index of the student you want to view: ");
								fileIndex = scnr.nextInt();
								scnr.nextLine();

								if (backend.getOneStudentByFileIndex(fileIndex) == null) {
									System.out.println(
											"The student with file index " + fileIndex + " could not be found.");
									System.out.println("Please enter a valid index.");
								} else {
									// print the scholarship's information, in more detail than before
									validSelection = true;
									System.out
											.println(backend.getOneStudentByFileIndex(fileIndex).getAllDetailsString());

									System.out.println("\nPlease press enter when you would like to return to the menu");
									scnr.nextLine();
								}

							} while (!validSelection);
						}

					}
				}

				// search students
				else if (userSelection == 3) {
					Boolean quitSearch = false;
					Boolean exitResults;

					while (!quitSearch) {

						System.out.println("1 - Name");
						System.out.println("2 - Year");
						System.out.println("3 - Major");
						System.out.println("4 - Minor");
						System.out.println("5 - GPA");

						System.out.print("Enter the number of the category you'd like to search by: ");
						int searchIndex = scnr.nextInt();
						scnr.nextLine();

						System.out.print("Enter the value to search: ");
						String searchValue = scnr.nextLine();

						// uses search scholarship method to return search results
						ArrayList<StudentProfile> studentsFound = backend.searchStudents(searchIndex, searchValue);

						exitResults = false;

						// program will stay within search results until requested to exit or do new
						// search
						while (!exitResults) {

							// if no scholarships are found
							if (studentsFound.size() == 0) {
								System.out.println("No students found!");

								System.out.println("What would you like to do:");
								System.out.println("2 - Enter new search criteria");
								System.out.println("3 - Exit search");

							}
							// offers all options if scholarships are found
							else {
								for (StudentProfile student : studentsFound) {
									System.out.println(student.getBasicDetailsString());
								}

								System.out.println("What would you like to do:");
								System.out.println("1 - Expand a student profile");
								System.out.println("2 - Enter new search criteria");
								System.out.println("3 - Exit search");
							}

							System.out.print("Your choice: ");

							int userAction = scnr.nextInt();
							scnr.nextLine();

							// quit search
							if (userAction == 3) {
								exitResults = true;
								quitSearch = true;
							}
							// start new search
							else if (userAction == 2) {
								exitResults = true;
							}
							// look at student
							else if (userAction == 1) {

								int fileIndex;
								Boolean validSelection = false;
								do {
									System.out.print("Please enter the index of the student you want to view: ");
									fileIndex = scnr.nextInt();
									scnr.nextLine();

									if (backend.getOneStudentByFileIndex(fileIndex) == null) {
										System.out.println(
												"The student with file index " + fileIndex + " could not be found.");
										System.out.println("Please enter a valid index.");
									} else {
										// print the scholarship's information, in more detail than before
										validSelection = true;
										System.out.println(
												backend.getOneStudentByFileIndex(fileIndex).getAllDetailsString());

										System.out.println("\nPlease press enter when you would like to return to the menu");
										scnr.nextLine();
									}

								} while (!validSelection);
							}
						}

					}
				}

				// Print all scholarships not archived
				else if (userSelection == 4) {

					backend.printAllScholarships(false, false, true, true, false);
					boolean keepGoing = true;

					do {
						// gives the option to view a scholarship more indepth
						System.out.println("What would you like to do:");
						System.out.println("1 - Go back");
						System.out.println("2 - view one scholarship in more detail");

						System.out.print("Your choice: ");

						// need new variable for new loop
						int userAction = scnr.nextInt();
						scnr.nextLine();

						// If the user wants to quit, keepGoing needs to be false
						if (userAction == 1) {
							keepGoing = false;
						} else if (userAction == 2) {

							int fileIndex;
							Boolean validSelection = false;
							do {
								// Grabs the index so that that scholarship can be printed
								System.out.print("Please enter the file index of the scholarship you want to view: ");
								fileIndex = scnr.nextInt();
								scnr.nextLine();
								// if the user does not select a valid scholarship reinquires user
								if (backend.getOneScholarshipByFileIndex(fileIndex) == null) {
									System.out.println(
											"The scholarship with file index " + fileIndex + " could not be found.");
									System.out.println("Please enter a valid index.");
								}
								else {
									validSelection = true;
									// print the scholarship's information, in more detail
									System.out.println(
											backend.getOneScholarshipByFileIndex(fileIndex).getAllInfoString());

									System.out.println("\nPress enter to go back");
									scnr.nextLine();
								}

							} while (!validSelection);
						}

					} while (keepGoing);

				}
				//view all archived scholarships
				else if (userSelection == 5) {
					backend.printAllScholarships(false, true, false, true, false);						
					boolean keepGoing = true;

					do {
						//gives the option to view a scholarship more in-depth
						System.out.println("What would you like to do:");
						System.out.println("1 - Go back");
						System.out.println("2 - view one scholarship in more detail");

						System.out.print("Your choice: ");

						//need new variable for new loop
						int userAction = scnr.nextInt();
						scnr.nextLine();

						//If the user wants to quit, keepGoing needs to be false
						if (userAction == 1) {
							keepGoing = false;
						}
						else if (userAction == 2) {
							
							int fileIndex;
							Boolean validSelection = false;
							do {
								//Grabs the index so that that scholarship can be printed
								System.out.print("Please enter the file index of the scholarship you want to view: ");
								fileIndex = scnr.nextInt();
								scnr.nextLine();
								//if the user does not select a valid scholarship re-inquires user
								if (backend.getOneScholarshipByFileIndex(fileIndex) == null) {
									System.out.println("The scholarship with file index " + fileIndex + " could not be found.");
									System.out.println("Please enter a valid index.");
								}
								else {
									validSelection = true;
									// print the scholarship's information, in more detail
									System.out.println(backend.getOneScholarshipByFileIndex(fileIndex).getAllInfoString());

									System.out.println("\nPress enter to go back");
									scnr.nextLine();
								}
								
							} while (!validSelection);
						}

					} while (keepGoing);
				}

				// award scholarship
				else if (userSelection == 6) {
					boolean quitBrowse = false;

					while (!quitBrowse) {
						// we want only not-archived and yes approved scholarships for this
						
						for(Scholarship schol : backend.getScholarshipsToBeAwarded()) {
							System.out.println(schol.getBasicInfoString());
						}

						System.out.println("What would you like to do:");
						System.out.println("1 - Go back");
						System.out.println("2 - view one scholarship in more detail");

						System.out.print("Your choice: ");

						// want to keep this separate from userSelection, so we don't accidentally exit
						// the outer do-while loop or something
						int userAction = scnr.nextInt();
						scnr.nextLine();

						if (userAction == 1) {
							quitBrowse = true;
						} else if (userAction == 2) {
							Boolean validSelection = false;

							do {
								System.out.print("Please enter the file index of the scholarship you want to view: ");
								int scholFileIndex = scnr.nextInt();
								scnr.nextLine();

								if (backend.getOneScholarshipByFileIndex(scholFileIndex) == null) {
									System.out.println(
											"The scholarship with file index " + scholFileIndex + " could not be found.");
									System.out.println("Please enter a valid index.");
								} else {
									// print the scholarship's information, in more detail than before
									validSelection = true;
									System.out.println(backend.getOneScholarshipByFileIndex(scholFileIndex).getAllInfoString());

									System.out.println("\nPlease select an option:");
									System.out.println("1 - Return to list");
									System.out.println("2 - See applicants (and award scholarship)");

									System.out.print("Your choice: ");
									userAction = scnr.nextInt();

									if (userAction == 1) {
										// do nothing?
									} else if (userAction == 2) {
										boolean quitStudentBrowse = false;

										while (!quitStudentBrowse) {
											for (StudentProfile applicant : backend.getOneScholarshipByFileIndex(scholFileIndex).getApplicants()) {
												System.out.println(applicant.getBasicDetailsString());
											}

											System.out.println("What would you like to do:");
											System.out.println("1 - Go back to scholarships");
											System.out.println("2 - View student in more detail");

											System.out.print("Your choice: ");

											// want to keep this separate from userSelection, so we don't accidentally exit
											// the outer do-while loop or something
											int inputAction = scnr.nextInt();
											scnr.nextLine();

											if (inputAction == 1) {
												quitStudentBrowse = true;
											} else if (inputAction == 2) {
												Boolean validSelection2 = false;

												do {
													System.out.print("Please enter the index of the student you want to view: ");
													int studentFileIndex = scnr.nextInt();
													scnr.nextLine();

													if (backend.getOneScholarshipByFileIndex(studentFileIndex) == null) {
														System.out.println(
																"The student with file index " + studentFileIndex + " could not be found.");
														System.out.println("Please enter a valid index.");
													} else {
														// print the scholarship's information, in more detail than before
														validSelection2 = true;
														System.out.println(backend.getOneStudentByFileIndex(studentFileIndex).getAllDetailsString());

														System.out.println("\nPlease select an option:");
														System.out.println("1 - Return to applicant list");
														System.out.println("2 - Award scholarship to this student");

														System.out.print("Your choice: ");
														inputAction = scnr.nextInt();

														if (inputAction == 1) {
															quitStudentBrowse = true;
														} else if (inputAction == 2) {
															backend.awardScholarship(backend.getOneScholarshipByFileIndex(studentFileIndex), backend.getOneStudentByFileIndex(studentFileIndex));
															quitStudentBrowse = true;
														}
													}

												} while (!validSelection2);
											} 

										}
									} 

								} 
							} while (!validSelection);
						}
					}
				
				// find and delete student profile
				} else if (userSelection == 7) {
					System.out.println("Which student would you like to delete?");
					// print the students, with their file index
					for (StudentProfile student : backend.getAllStudents()) {
						System.out.println(student.getBasicDetailsString());
					}

					System.out.print("Please enter the file index of the student you wish to delete: ");
					int fileIndex = scnr.nextInt();
					scnr.nextLine();

					// print the student in more detail
					System.out.println("The selected student's details:");
					StudentProfile studentToDelete = backend.getOneStudentByFileIndex(fileIndex);
					System.out.println(studentToDelete);

					System.out.print("Are you sure you want to delete this student from the system? (y/n): ");
					String userConfirmation = scnr.nextLine();

					if (userConfirmation.equalsIgnoreCase("y")) {
						System.out.println("Deleting the student");
						backend.deleteStudentProfile(studentToDelete);
					}
					else {
						System.out.println("Ok, will not delete the student. Exiting this menu");
					}
				} else if (userSelection == 8) {
					MatchRelationship match = backend.getAllMatchRelationships().get(12);
					float newPercentage = 0;

					System.out.println();
					System.out.println("Current match details: ");
					System.out.println(match.getDetailsString());
					System.out.println();

					Boolean correctInput = false;
					while (!correctInput) {
						System.out.println("Enter the new match percentage: (0-100)");
						newPercentage = scnr.nextFloat();
						scnr.nextLine();

						if (Float.compare(newPercentage, 0) >= 0 && Float.compare(newPercentage, 100) <= 0) {
							correctInput = true;
						}
						else {
							System.out.println("Enter value between 0 and 100.");
						}
					}

					match.setMatchPercentage(newPercentage);
					backend.updateMatchFile(match);

					System.out.println();
					System.out.println("New match details: ");
					System.out.println(match.getDetailsString());
					System.out.println();

					System.out.println("Press enter to quit.");
					scnr.nextLine();


				}
				else {
					
				}

			} while (userSelection != 0);

			scnr.nextLine();
		}
	} 
		

	

	public static void check5YearsPastDue() throws IOException {
		// make a new BackendSystem object, which calls methods to
		// instantiate all scholarships, donors, etc.
		BackendSystem backend = new BackendSystem();

		// scholarship4 has a due date that's more than 5 years ago, want to check if
		// my Scholarship.due5PlusYearsAgo() method works
		Scholarship oldScholarship = backend.getAllScholarships().get(4);

		System.out.println("Was due 5+ years ago: " + oldScholarship.due5PlusYearsAgo());
		// TODO: eventually write code to archive scholarships past 5 years?? I guess
		// it's not
		// in the requirement but maybe we should add it

		// and then here we could check if the archive status got changed to true

	}
}
