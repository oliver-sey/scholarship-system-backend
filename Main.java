
/**
 * A main file, that will run our whole project.
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static Scanner scnr = new Scanner(System.in);
	public static void main(String[] args) throws Exception {
		BackendSystem backend = new BackendSystem();
		// userRunSystem will prompt them to log in
		userRunSystem(backend);

		//runDifferentTests();

	}

	// prompt the user to log in and answer security questions, and then let them perform one action at a time, until they want to quit 
	public static void userRunSystem(BackendSystem backend) throws IOException {
		// prompt the user to log in, when this line of code is done they are successfully logged in or the system should stop
		boolean successfulLogin = backend.login();

		if (successfulLogin) {
			String userInput = "";
			do {
				// prompt them for one action
				oneUserAction(backend);

				// see if they want to quit
				Scanner scnr = new Scanner(System.in);
				System.out.print("Do you want to fully quit the program? (type y/n): ");
				userInput = scnr.next();
			// while the first letter of the user's input is either y or Y, keep going
			} while (userInput.toLowerCase().charAt(0) != 'y');
		}
		// else just quit
	}


	/**
	 * You should get the user to log in before calling this!
	 * prompts the user for which action they want to perform based on their profile type
	 */
	public static void oneUserAction(BackendSystem backend) throws IOException {
		//Scanner scnr = new Scanner(System.in);
		int userSelection = -1;
		
		// if (backend.getUserType().compareTo("student") == 0) {
		if (backend.getCurrentUser() instanceof StudentProfile) {
			/*
			- edit profile
			- see all scholarships
			- see in-progress applications/ update application
			- see submitted applications
			- search scholarships
			*/
			do {
				System.out.println("\nPlease enter a number to select which action you want to do:");

				System.out.println("1 - View and edit your profile");
				System.out.println("2 - See all active scholarships (can apply to them also)");
				System.out.println("3 - See applications in progress");
				System.out.println("4 - See submitted applications");
				System.out.println("5 - Search scholarships");
				System.out.println("0 - EXIT");
				
				System.out.print("Your selection: ");
				userSelection = scnr.nextInt();

				// is it as simple as this???
				if (userSelection == 1) {
					// typecast it to a StudentProfile because it will be a profile object,
					// but this is ok because we will only get here if currentUser is a student object
					backend.editStudentInfo((StudentProfile)backend.getCurrentUser());
				}
				
				// see all scholarships and maybe apply
				else if (userSelection == 2) {
					Boolean quitBrowse = false;

					while (!quitBrowse) {
						// call printAllScholarships, we want basic info and no archived scholarships, yes approved scholarships, no unapproved scholarships
						backend.printAllScholarships(false, false, true, false);

						System.out.println("What would you like to do:");
						System.out.println("1 - Go back");
						System.out.println("2 - view one scholarship in more detail (can also apply to it)");

						System.out.print("Your choice: ");

						// want to keep this separate from userSelection, so we don't accidentally exit the outer do-while loop or something
						int userAction = scnr.nextInt();
						scnr.nextLine();

						if (userAction == 1) {
							quitBrowse = true;
						}
						else if (userAction == 2) {
							
							int fileIndex;
							Boolean validSelection = false;
							do {
								System.out.print("Please enter the file index of the scholarship you want to view: ");
								fileIndex = scnr.nextInt();
								scnr.nextLine();
								
								if (backend.getOneScholarshipByFileIndex(fileIndex) == null) {
									System.out.println("The scholarship with file index " + fileIndex + " could not be found.");
									System.out.println("Please enter a valid index.");
								}
								else {
									// print the scholarship's information, in more detail than before
									validSelection = true;
									System.out.println(backend.getOneScholarshipByFileIndex(fileIndex).getAllInfoString());

									System.out.println("\nPlease select an option:");
									System.out.println("1 - go back");
									System.out.println("2 - apply");

									System.out.print("Your choice: ");
									userAction = scnr.nextInt();
									scnr.nextLine();

									if (userAction == 1) {
										// do nothing?
									}
									else if (userAction == 2) {
										backend.applyToScholarship(fileIndex);
									}
								}
								
							} while (!validSelection);
						}
						
					}
					
				}
				else if (userSelection == 3) {
					Boolean quit = false;

					while (!quit) {
						
					}
				}
				//get submitted applications
				else if (userSelection == 4) {
					backend.getSubmittedApplications((StudentProfile) backend.getCurrentUser());
				}
				//search for scholarships
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

						//uses search scholarship method to return search results
						ArrayList<Scholarship> scholarshipsFound = backend.searchScholarships(searchIndex, searchValue);

						exitResults = false;

						//program will stay within search results until requested to exit or do new search
						while (!exitResults) {

							//if no scholarships are found
							if (scholarshipsFound.size() == 0) {
								System.out.println("No scholarships found!");

								System.out.println("What would you like to do:");
								System.out.println("2 - Enter new search criteria");
								System.out.println("3 - Exit search");
								
							}
							//offers all options if scholarships are found
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
							
							//quit search
							if (userAction == 3) {
								exitResults = true;
								quitSearch = true;
							}
							//start new search
							else if (userAction == 2) {
								exitResults = true;
							}
							//look at scholarship
							else if (userAction == 1) {
								
								int fileIndex;
								Boolean validSelection = false;
								do {
									System.out.print("Please enter the file index of the scholarship you want to view: ");
									fileIndex = scnr.nextInt();
									scnr.nextLine();
									
									if (backend.getOneScholarshipByFileIndex(fileIndex) == null) {
										System.out.println("The scholarship with file index " + fileIndex + " could not be found.");
										System.out.println("Please enter a valid index.");
									}
									else {
										// print the scholarship's information, in more detail than before
										validSelection = true;
										System.out.println(backend.getOneScholarshipByFileIndex(fileIndex).getAllInfoString());

										System.out.println("\nPlease select an option:");
										System.out.println("1 - go back");
										System.out.println("2 - apply");

										System.out.print("Your choice: ");
										userAction = scnr.nextInt();
										scnr.nextLine();

										if (userAction == 1) {
											// do nothing?
										}
										else if (userAction == 2) {
											backend.applyToScholarship(fileIndex);
										}
									}
									
								} while (!validSelection);
							}
						}

						
					}
					
				}


			} while(userSelection != 0);
		}
		// else if (backend.getUserType().compareTo("donor") == 0) {
		else if (backend.getCurrentUser() instanceof DonorProfile) {
			/*
			- create and submit scholarship for review

			*/
			do {
				System.out.println("\nPlease enter a number to select which action you want to do:");

				System.out.println("1 - See your posted scholarships");
				System.out.println("2 - Enter a new scholarship");
				System.out.println("0 - EXIT");
				
				System.out.print("Your selection: ");
				userSelection = scnr.nextInt();

				if (userSelection == 1) {
					for (Scholarship scholarship : ((DonorProfile)backend.getCurrentUser()).getScholarships()) {
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
			 * 
			 */
			System.out.println("Have to still implement oneUserAction() for StaffProfile.");
		}
		// else if (backend.getUserType().compareTo("fund steward") == 0) {
		else if (backend.getCurrentUser() instanceof FundStewardProfile) {
			// TODO: implement this!!
			/*
			 * -view archived/awarded scholarships
			 */
			do{
				//querys user about what they would like to do
				System.out.println("\nPlease enter a number to select which action you want to do:");

				System.out.println("1 - View awarded scholarships");
				System.out.println("0 - EXIT");
				
				System.out.print("Your selection: ");
				userSelection = scnr.nextInt();

				//runs through viewing an awarded scholarship
				if(userSelection == 1){
					//print out a list of all awarded scholarships that have been awarded
					backend.printArchivedScholarships(false, true, true, false);						
					boolean quitBrowse = true;
					while(quitBrowse){
						//gives the option to view a scholarship more indepth
						System.out.println("What would you like to do:");
						System.out.println("1 - Go back");
						System.out.println("2 - view one scholarship in more detail");

						System.out.print("Your choice: ");

						//need new variable for new loop
						int userAction = scnr.nextInt();
						scnr.nextLine();

						//If the user wants to quir quitBrowse needs to be false
						if(userAction == 1){
							quitBrowse = false;
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
									// print the scholarship's information, in more detail
									System.out.println(backend.getOneScholarshipByFileIndex(fileIndex).getAllInfoString());

									System.out.println("\nEnter 1 to go back: ");
									userAction = scnr.nextInt();
									scnr.nextLine();

									if (userAction == 1) {
										//if the answer is valid it is true and restarts outer loop
										validSelection = true;
									}
								}
								
							} while (!validSelection);
						}

					}
				}

			}while(userSelection != 0);

			System.out.println("Have to still implement oneUserAction() for FundStewardProfile.");
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
				System.out.println("1 - view unapproved scholarships to approve or delete them");
				System.out.println("2 - view all students");
				// TODO: implement this!!
				System.out.println("3 - view all scholarships (that are not archived)");
				
				System.out.println("0 - EXIT");

				System.out.print("Your selection: ");
				userSelection = scnr.nextInt();

				// if they want to view unapproved scholarships to approve them
				if (userSelection == 1) {
					System.out.println("Unapproved scholarships:");

					// call printAllScholarships, we want basic info and only unapproved scholarships
					backend.printAllScholarships(false, false, false, true);

					// prompt them to approve one scholarship

					int adminAction = -1;
					do {
						System.out.println("Which actions would you like to perform on these archived scholarships: ");
						System.out.println("1 - approve a scholarship");
						System.out.println("2 - delete a scholarship");
						System.out.println("0 - EXIT");
						adminAction = scnr.nextInt();
						scnr.nextLine();

						// approve a scholarship
						if (adminAction == 1) {
							System.out.print("Please enter a scholarship ID that you want to approve, or -1 if you want to go back: ");
							int scholFileIndexToApprove = scnr.nextInt();

							// if they want to approve a scholarship
							if (scholFileIndexToApprove != -1) {
								// TODO: will this go all the way through the chain of pointers and actually change the original scholarship object????
								System.out.println("Setting the scholarship to approved");
								// unapprovedSchols.get(scholFileIndexToApprove).setApproved(true);
								Scholarship scholToApprove = backend.getOneScholarshipByFileIndex(scholFileIndexToApprove);
								scholToApprove.setApproved(true);

								System.out.println("Checking if the scholarship was actually successfully approved. isApproved: " + backend.getOneScholarshipByFileIndex(scholFileIndexToApprove).getIsApproved());
								// update the file
								backend.updateScholarshipFile(scholToApprove);
							}
						}
						// delete a scholarship
						else if (adminAction == 2) {
							System.out.print("Please enter a scholarship ID that you want to delete: ");
							int deleteFileIndex = scnr.nextInt();
							scnr.nextLine();

							// try to remove the scholarship, but catch the exception if they enter an out of bounds value
							// couldn't figure out how to implement this with if-statements
							try {
								backend.getAllScholarships().remove(deleteFileIndex);
							} catch (IndexOutOfBoundsException e) {
								// TODO: handle exception
								System.out.println("That scholarship ID value was invalid.");
							}
						}
					} while(adminAction != 0);
				}

				// view all scholarships
				else if (userSelection == 2) {
					for (StudentProfile student : backend.getAllStudents()) {
						System.out.println(student.getAllDetailsString() + "\n");
					}
				}
			} while(userSelection != 0);
		}
		else {
			// should never get here
			System.out.println("Invalid user type in userOptions");
		}
		
		

	}

	public static void runDifferentTests() throws Exception {
		//Scanner scnr = new Scanner(System.in);
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
			System.out.println("7 - (**OUTDATED, use oneUserAction() method) login as an admin and then approve 1 preselected scholarship");
			System.out.println("8 - Edit student profile manually");
			System.out.println("9 - Test printOneScholarship and printAllScholarships");
			System.out.println("10 - test getScholarshipFromInput");			
			System.out.println("11 - Add new user to the system");

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
					System.out.print(scholarshipObj.getName() + ", due date: " + scholarshipObj.getDateDueString());// + ", due 5+ years ago: " + scholarshipObj.due5PlusYearsAgo());    
					if(scholarshipObj.due5PlusYearsAgo() == true){
						System.out.println(" -> Archived");
					}
					else{
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

				System.out.println("Changed the details in the donor file, please check that it looks good and then enter anything in the console so we can restore the file to its original state.");
				System.out.println("Waiting for you to type anything: ");
				scnr.next();

				System.out.println("Reverting the file back to its original state");
				
				// change the properties in the DonorProfile object, **back to what they were before
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
				// 	System.out.println("'" + admin.getName() + "'");
				// }

				// for donors
				// System.out.println("all donors: " + backend.getAllDonors());
				// System.out.println("all donor names: ");
				// for (DonorProfile donor : backend.getAllDonors()) {
				// 	System.out.println("'" + donor.getName() + "'");
				// }

				// for students
				// System.out.println("all student names: ");
				// for (StudentProfile student : backend.getAllStudents()) {
				// 	System.out.println("'" + student.getName() + "'");
				// }

				// for staff
				// System.out.println("all staff names: ");
				// for (StaffProfile staff : backend.getAllStaff()) {
				// 	System.out.println("'" + staff.getName() + "'");
				// }

				// for fund stewards
				// System.out.println("all fund steward names: ");
				// for (FundStewardProfile fundsteward : backend.getAllFundstewards()) {
				//	System.out.println("'" + fundsteward.getName() + "'");
				// }

				// for scholarships
				System.out.println("all scholarships (detailed): ");
				for (Scholarship scholarship : backend.getAllScholarships()) {
					System.out.println(scholarship.getAllInfoString());
					System.out.println();
				}
			}
			
			else if (userSelection == 6) {
				BackendSystem backend = new BackendSystem();

				// TODO: just using the first one for now, not exactly sure
				// pick one donor and make a duplicate folder with the files for that donor to test this
				//DonorProfile donorToCopy = backend.getAllDonors().get(0);
				DonorProfile newDonor = backend.getDonorFromInput();

				//backend.storeNewDonorProfile(donorToCopy);
				backend.storeNewDonorProfile(newDonor);
				System.out.println("Made new folder and files for that donor, the folder name should be /donors/donor" + (backend.findNextFileIndex("donor") - 1));
				System.out.println("!!!! Please be sure to delete that folder so we don't have duplicates.");
			}

			// replacing this with code in user actions method
			else if (userSelection == 7) {
				BackendSystem backend = new BackendSystem();

				StudentProfile newStudent = new StudentProfile("Jess", "Mess", 12345, "user", "pass", "IE", true, "SFWEE", true,
				3.6, true, true, "Freshman", 5, 2025, "Female", true, false, 12, false, "I love school!", "Smith",
				"The eagles", "New York");

				backend.setCurrentUser(newStudent);

				System.out.println(backend.getCurrentUser().toString());
				
			}

			else if (userSelection == 8) {
				BackendSystem backend = new BackendSystem();

				StudentProfile newStudent = new StudentProfile("Jess", "Mess", 12345, "user", "pass", "IE", true, "SFWEE", true,
				3.6, true, true, "Freshman", 5, 2025, "Female", true, false, 12, false, "I love school!", "Smith",
				"The eagles", "New York");
				
				backend.login();

				
			}

			// test print scholarships
			else if (userSelection == 9) {
				BackendSystem backend = new BackendSystem();

				System.out.println("\nTesting the printOneScholarship method (for #1, aka index 0):");
				System.out.print(backend.getAllScholarships().get(1).getBasicInfoString());

				System.out.println("\n\nTesting the printAllScholarships method (basic info, include archived and include approved and unapproved):");
				backend.printAllScholarships(false, true, true, true);

				System.out.println("\n\nTesting the printAllScholarships method (basic info, do not include archived, include only unapproved):");
				backend.printAllScholarships(false, false, false, true);

				System.out.println("\n\nTesting the printAllScholarships method (**detailed info, do not include archived or unapproved, but include approved):");
				backend.printAllScholarships(true, false, true, false);
			}

			else if (userSelection == 10) {
				BackendSystem backend = new BackendSystem();
				System.out.println("To make this use case work, please log in as a donor");

				backend.login();

				String infoString = backend.createScholarshipFromInput().getAllInfoString();

				System.out.println("The new scholarship:");
				System.out.println(infoString);
			}

			else if (userSelection == 11){
				BackendSystem backend = new BackendSystem();
				String userType;

				System.out.println("Please enter your usertype. (Enter as one word, i.e. student, fundsteward, etc.)");
				scnr.nextLine();
				userType = scnr.nextLine();

				/*if (!(userType.equalsIgnoreCase("student") || userType.equalsIgnoreCase("admin")
						|| userType.equalsIgnoreCase("staff")
						|| userType.equalsIgnoreCase("donor") || userType.equalsIgnoreCase("fundsteward"))) {
					System.out.println(
							"That user type was not recognized. Accepted user types are: "
							+ "student, admin, staff, donor, and fundsteward (capitalization doesn't matter).\n");
					continue;
				}*/

				if(userType.equalsIgnoreCase("student")){
					StudentProfile newStudent = new StudentProfile();
					newStudent = backend.getStudentFromInput();
					backend.setCurrentUser(newStudent);
					System.out.println(((StudentProfile) backend.getCurrentUser()).toString());
					backend.storeNewStudentProfile(newStudent);
					System.out.println("Made new folder and files for that student, the folder name should be /students/student" + (backend.findNextFileIndex("student") - 1));
					System.out.println("!!!! Please be sure to delete that folder so we don't have duplicates.");
				}
				else if(userType.equalsIgnoreCase("admin")){
					AdminProfile newAdmin = new AdminProfile();
					newAdmin = backend.getAdminFromInput();
					backend.setCurrentUser(newAdmin);
					System.out.println(((AdminProfile) backend.getCurrentUser()).toString());
					backend.storeNewAdminProfile(newAdmin);
					System.out.println("Made new folder and files for that administrator, the folder name should be /administrators/admin" + (backend.findNextFileIndex("admin") - 1));
					System.out.println("!!!! Please be sure to delete that folder so we don't have duplicates.");
				}
				else if(userType.equalsIgnoreCase("staff")){
					StaffProfile newStaff = new StaffProfile();
					newStaff = backend.getStaffFromInput();
					backend.setCurrentUser(newStaff);
					System.out.println(((StaffProfile) backend.getCurrentUser()).toString());
					backend.storeNewStaffProfile(newStaff);
					System.out.println("Made new folder and files for that staff, the folder name should be /engr staff/staff" + (backend.findNextFileIndex("staff") - 1));
					System.out.println("!!!! Please be sure to delete that folder so we don't have duplicates.");
				}
				else if(userType.equalsIgnoreCase("fundsteward")){
					FundStewardProfile newFundsteward = new FundStewardProfile();
					newFundsteward = backend.getFundStewardFromInput();
					backend.setCurrentUser(newFundsteward);
					System.out.println(((FundStewardProfile) backend.getCurrentUser()).toString());
					backend.storeNewFundStewardProfile(newFundsteward);
					System.out.println("Made new folder and files for that fundsteward, the folder name should be /fundstewards/fundsteward" + (backend.findNextFileIndex("fundsteward") - 1));
					System.out.println("!!!! Please be sure to delete that folder so we don't have duplicates.");
				}
				else if(userType.equalsIgnoreCase("donor")){
					DonorProfile newDonor = new DonorProfile();
					newDonor = backend.getDonorFromInput();
					backend.setCurrentUser(newDonor);
					System.out.println(((DonorProfile) backend.getCurrentUser()).toString());
					backend.storeNewDonorProfile(newDonor);
					System.out.println("Made new folder and files for that donor, the folder name should be /donors/donor" + (backend.findNextFileIndex("donor") - 1));
					System.out.println("!!!! Please be sure to delete that folder so we don't have duplicates.");
				}

			}

			else {
				System.out.println("Invalid selection, please try again");
			}
		} while (userSelection != 0);

		scnr.close();
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
