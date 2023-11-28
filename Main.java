
/**
 * A main file, that will run our whole project.
 */

import java.io.File;
import java.io.IOException;
import java.security.KeyStore.TrustedCertificateEntry;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class Main {

	public static Scanner scnr = new Scanner(System.in);

	public static void main(String[] args) throws Exception {
		BackendSystem backend = new BackendSystem();
		// userRunSystem will prompt them to log in
		userRunSystem(backend);

		// runDifferentTests();

		// **only close the Scanner at the very end, after everything is done
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
			System.out.print("Do you want to fully quit the program? (type y/n): ");
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

									int qIndex = 1;
									System.out.printf("Match percentage: f%.1", matchSelected.getMatchPercentage());
									System.out.println();
									System.out.printf("Match Index: f%.2", matchSelected.getMatchIndex());
									System.out.println();
									System.out.println("Your current application: ");
									System.out.println();

									for (Map.Entry<String, String> pair : matchSelected.getApplication().entrySet()) {
										System.out.println("Question " + qIndex + ":");
										System.out.println(pair.getKey());
										System.out.println(pair.getValue());
										qIndex++;
									}

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

						System.out.print("Enter the value to search: ");
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
									System.out.println(schol.getBasicInfoString());
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

						// If the user wants to quir quitBrowse needs to be false
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
											File file = new File("scholarships/sholarship" + fileIndex);
											file.delete();

											int index = backend.getAllScholarships()
													.indexOf(backend.getOneScholarshipByFileIndex(fileIndex));
											backend.getAllScholarships().remove(index);

											System.out.println("Scholarship deleted.");
										} else {
											System.out.println("Action dissmissed.");
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
						//gives the option to view a scholarship more indepth
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
								//if the user does not select a valid scholarship reinquires user
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
					int fileIndex;

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
				}
				else {
					// should never get here
					System.out.println("Invalid user type in userOptions");
				}

			} while (userSelection != 0);
		}
	} 
		

	

	public static void runDifferentTests() throws Exception {
		System.out.println("Hello! Welcome to the backend for our Scholarship Management System.");

		// the number the user enters
		int userSelection;
		do {
			System.out.println("\nPlease enter a number to select which test you would like to run:");
			// TODO: do we want to be able to just run the program normally? wouldn't we
			// have to add the ability to give extra commands
			System.out.println("1 - Run the program normally, no specific test case");
			System.out.println("2 - check if scholarship that were due 5+ years ago, are handled correctly");
			System.out.println("3 - check login");
			System.out.println("4 - test updateDonorProfileFile()");
			System.out.println("5 - print all of one thing, can customize");
			System.out.println("6 - test storeNewDonorProfile()");
			System.out.println(
					"7 - (**OUTDATED, use oneUserAction() method) login as an admin and then approve 1 preselected scholarship");
			System.out.println("8 - Edit student profile manually");
			System.out.println("9 - Test printOneScholarship and printAllScholarships");
			System.out.println("10 - test getScholarshipFromInput");
			System.out.println("11 - Add new user to the system");
			System.out.println("12 - testing a student deleting themselves");

			System.out.println("0 - EXIT");

			System.out.print("\nYour choice: ");

			userSelection = scnr.nextInt();

			if (userSelection == 0) {
				System.out.println("Exiting, thank you for using our Scholarship Management System today!");
			}

			else if (userSelection == 1) {
				// instantiate the backend and all the students, scholarships, etc.
				BackendSystem backend = new BackendSystem();
				// print the backend object to make sure everything worked ok
				// TODO: have to implement a backend toString first!!!!!
				// System.out.println(backend.toString());
			}

			else if (userSelection == 2) {
				// instantiate the backend and all the students, scholarships, etc.
				BackendSystem backend = new BackendSystem();
				// print the backend object to make sure everything worked ok
				// System.out.println(backend.toString());
				for (Scholarship scholarshipObj : backend.getAllScholarships()) {
					System.out.print(scholarshipObj.getName() + ", due date: " + scholarshipObj.getDateDueString());// +
																													// ",
																													// due
																													// 5+
																													// years
																													// ago:
																													// "
																													// +
																													// scholarshipObj.due5PlusYearsAgo());
					if (scholarshipObj.due5PlusYearsAgo() == true) {
						System.out.println(" -> Archived");
					} else {
						System.out.println(" -> Available");
					}
				}
			}

			// checking the login
			else if (userSelection == 3) {
				// instantiate the backend and all the students, scholarships, etc.
				BackendSystem backend = new BackendSystem();
				// prompt users for their login details
				// this will call checkLoginDetails(), which will do the security questions
				backend.login();
			}

			else if (userSelection == 4) {
				// instantiate the backend and all the students, scholarships, etc.
				BackendSystem backend = new BackendSystem();

				// TODO: just using the first one for now, not exactly sure
				DonorProfile donorToUpdate = backend.getAllDonors().get(0);

				// store these so we can restore it again later
				String origFirstName = donorToUpdate.getFirstName();
				String origUsername = donorToUpdate.getUsername();
				String origSeqQAnswer1 = donorToUpdate.getOneSecurityQAnswer(1);

				// change the properties in the DonorProfile object
				donorToUpdate.setFirstName("Changedname");
				donorToUpdate.setUsername("changedusername");
				donorToUpdate.setOneSecurityQAnswer(1, "changed security answer text for #1");

				backend.updateDonorProfileFile(donorToUpdate);

				System.out.println(
						"Changed the details in the donor file, please check that it looks good and then enter anything in the console so we can restore the file to its original state.");
				System.out.println("Waiting for you to type anything: ");
				scnr.next();

				System.out.println("Reverting the file back to its original state");

				// change the properties in the DonorProfile object, **back to what they were
				// before
				donorToUpdate.setFirstName(origFirstName);
				donorToUpdate.setUsername(origUsername);
				donorToUpdate.setOneSecurityQAnswer(1, origSeqQAnswer1);

				// write the original values back to the file
				backend.updateDonorProfileFile(donorToUpdate);

				System.out.println("Wrote the original info back to the file");
			}

			else if (userSelection == 5) {
				BackendSystem backend = new BackendSystem();

				// for admins
				// System.out.println("all admin names: ");
				// for (AdminProfile admin : backend.getAllAdmins()) {
				// System.out.println("'" + admin.getName() + "'");
				// }

				// for donors
				// System.out.println("all donors: " + backend.getAllDonors());
				// System.out.println("all donor names: ");
				// for (DonorProfile donor : backend.getAllDonors()) {
				// System.out.println("'" + donor.getName() + "'");
				// }

				// for students
				System.out.println("all student names: ");
				for (StudentProfile student : backend.getAllStudents()) {
					System.out.println("'" + student.getName() + "'");
				}

				// for staff
				// System.out.println("all staff names: ");
				// for (StaffProfile staff : backend.getAllStaff()) {
				// System.out.println("'" + staff.getName() + "'");
				// }

				// for fund stewards
				// System.out.println("all fund steward names: ");
				// for (FundStewardProfile fundsteward : backend.getAllFundstewards()) {
				// System.out.println("'" + fundsteward.getName() + "'");
				// }

				// for scholarships
				// System.out.println("all scholarships (detailed): ");
				// for (Scholarship scholarship : backend.getAllScholarships()) {
				// System.out.println(scholarship.getAllInfoString());
				// System.out.println();
				// }
			}

			else if (userSelection == 6) {
				BackendSystem backend = new BackendSystem();

				// TODO: just using the first one for now, not exactly sure
				// pick one donor and make a duplicate folder with the files for that donor to
				// test this
				// DonorProfile donorToCopy = backend.getAllDonors().get(0);
				DonorProfile newDonor = backend.getDonorFromInput();

				// backend.storeNewDonorProfile(donorToCopy);
				backend.storeNewDonorProfile(newDonor);
				System.out.println("Made new folder and files for that donor, the folder name should be /donors/donor"
						+ (backend.findNextFileIndex("donor") - 1));
				System.out.println("!!!! Please be sure to delete that folder so we don't have duplicates.");
			}

			// replacing this with code in user actions method
			else if (userSelection == 7) {
				BackendSystem backend = new BackendSystem();

				StudentProfile newStudent = new StudentProfile("Jess", "Mess", 12345, "user", "pass", "IE", true,
						"SFWEE", true,
						3.6, true, true, "Freshman", 5, 2025, "Female", true, false, 12, false, "I love school!",
						"Smith",
						"The eagles", "New York");

				backend.setCurrentUser(newStudent);

				System.out.println(backend.getCurrentUser().toString());

			}

			else if (userSelection == 8) {
				BackendSystem backend = new BackendSystem();

				StudentProfile newStudent = new StudentProfile("Jess", "Mess", 12345, "user", "pass", "IE", true,
						"SFWEE", true,
						3.6, true, true, "Freshman", 5, 2025, "Female", true, false, 12, false, "I love school!",
						"Smith",
						"The eagles", "New York");

				backend.login();

			}

			// test print scholarships
			else if (userSelection == 9) {
				BackendSystem backend = new BackendSystem();

				System.out.println("\nTesting the printOneScholarship method (for #1, aka index 0):");
				System.out.print(backend.getAllScholarships().get(1).getBasicInfoString());

				System.out.println("\n\nTesting the printAllScholarships method (basic info, include everything):");
				backend.printAllScholarships(false, true, true, true, true);

				System.out.println(
						"\n\nTesting the printAllScholarships method (basic info, do not include archived, include only *unapproved and non-archived):");
				backend.printAllScholarships(false, false, true, false, true);

				System.out.println(
						"\n\nTesting the printAllScholarships method (**detailed info, include only not archived, and yes approved):");
				backend.printAllScholarships(true, false, true, true, false);
			}

			else if (userSelection == 10) {
				BackendSystem backend = new BackendSystem();
				System.out.println("To make this use case work, please log in as a donor");

				backend.login();

				String infoString = backend.createScholarshipFromInput().getAllInfoString();

				System.out.println("The new scholarship:");
				System.out.println(infoString);
			}

			else if (userSelection == 11) {
				BackendSystem backend = new BackendSystem();
				String userType;

				System.out.println("Please enter your usertype. (Enter as one word, i.e. student, fundsteward, etc.)");
				scnr.nextLine();
				userType = scnr.nextLine();

				/*
				 * if (!(userType.equalsIgnoreCase("student") ||
				 * userType.equalsIgnoreCase("admin")
				 * || userType.equalsIgnoreCase("staff")
				 * || userType.equalsIgnoreCase("donor") ||
				 * userType.equalsIgnoreCase("fundsteward"))) {
				 * System.out.println(
				 * "That user type was not recognized. Accepted user types are: "
				 * +
				 * "student, admin, staff, donor, and fundsteward (capitalization doesn't matter).\n"
				 * );
				 * continue;
				 * }
				 */

				if (userType.equalsIgnoreCase("student")) {
					StudentProfile newStudent = new StudentProfile();
					newStudent = backend.getStudentFromInput();
					backend.setCurrentUser(newStudent);
					System.out.println(((StudentProfile) backend.getCurrentUser()).toString());
					backend.storeNewStudentProfile(newStudent);
					System.out.println(
							"Made new folder and files for that student, the folder name should be /students/student"
									+ (backend.findNextFileIndex("student") - 1));
					System.out.println("!!!! Please be sure to delete that folder so we don't have duplicates.");
				} else if (userType.equalsIgnoreCase("admin")) {
					AdminProfile newAdmin = new AdminProfile();
					newAdmin = backend.getAdminFromInput();
					backend.setCurrentUser(newAdmin);
					System.out.println(((AdminProfile) backend.getCurrentUser()).toString());
					backend.storeNewAdminProfileFile(newAdmin);
					System.out.println(
							"Made new folder and files for that administrator, the folder name should be /administrators/admin"
									+ (backend.findNextFileIndex("admin") - 1));
					System.out.println("!!!! Please be sure to delete that folder so we don't have duplicates.");
				} else if (userType.equalsIgnoreCase("staff")) {
					StaffProfile newStaff = new StaffProfile();
					newStaff = backend.getStaffFromInput();
					backend.setCurrentUser(newStaff);
					System.out.println(((StaffProfile) backend.getCurrentUser()).toString());
					backend.storeNewStaffProfile(newStaff);
					System.out.println(
							"Made new folder and files for that staff, the folder name should be /engr staff/staff"
									+ (backend.findNextFileIndex("staff") - 1));
					System.out.println("!!!! Please be sure to delete that folder so we don't have duplicates.");
				} else if (userType.equalsIgnoreCase("fundsteward")) {
					FundStewardProfile newFundsteward = new FundStewardProfile();
					newFundsteward = backend.getFundStewardFromInput();
					backend.setCurrentUser(newFundsteward);
					System.out.println(((FundStewardProfile) backend.getCurrentUser()).toString());
					backend.storeNewFundStewardProfile(newFundsteward);
					System.out.println(
							"Made new folder and files for that fundsteward, the folder name should be /fundstewards/fundsteward"
									+ (backend.findNextFileIndex("fundsteward") - 1));
					System.out.println("!!!! Please be sure to delete that folder so we don't have duplicates.");
				} else if (userType.equalsIgnoreCase("donor")) {
					DonorProfile newDonor = new DonorProfile();
					newDonor = backend.getDonorFromInput();
					backend.setCurrentUser(newDonor);
					System.out.println(((DonorProfile) backend.getCurrentUser()).toString());
					backend.storeNewDonorProfile(newDonor);
					System.out
							.println("Made new folder and files for that donor, the folder name should be /donors/donor"
									+ (backend.findNextFileIndex("donor") - 1));
					System.out.println("!!!! Please be sure to delete that folder so we don't have duplicates.");
				}

			}

			// testing a student deleting themselves
			else if (userSelection == 12) {
				BackendSystem backend = new BackendSystem();
				System.out.println("Testing a student deleting themselves");
				System.out.println("Please login as a student:");
				backend.login();

				backend.deleteStudentProfile((StudentProfile) backend.getCurrentUser());
				System.out.println("done");
			}

			else {
				System.out.println("Invalid selection, please try again");
			}
		} while (userSelection != 0);
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
