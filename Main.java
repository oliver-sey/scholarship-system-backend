
/**
 * A main file, that will run our whole project.
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static Scanner scnr = new Scanner(System.in);
	public static void main(String[] args) throws Exception {
		// BackendSystem backend = new BackendSystem();
		// userRunSystem will prompt them to log in
		// userRunSystem(backend);

		runDifferentTests();

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
			} while (userInput.toLowerCase().charAt(0) == 'y');
		}
		// else just quit
		scnr.close();
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
					// call printAllScholarships, we want basic info and no archived scholarships, yes approved scholarships, no unapproved scholarships
					backend.printAllScholarships(false, false, true, false);

					System.out.println("What would you like to do:");
					System.out.println("1 - Go back");
					System.out.println("2 - view one scholarship in more detail (can also apply to it)");

					System.out.print("Your choice: ");

					// want to keep this separate from userSelection, so we don't accidentally exit the outer do-while loop or something
					int userAction = scnr.nextInt();

					if (userAction == 1) {
						// do nothing?
					}
					else if (userAction == 2) {
						scnr.nextLine();

						int fileIndex;
						do {
							System.out.print("Please enter the file index of the scholarship you want to view, or -1 to quit: ");
							fileIndex = scnr.nextInt();

							if (fileIndex != -1) {
								if (backend.getOneScholarshipByFileIndex(fileIndex) == null) {
									System.out.println("The scholarship with file index " + fileIndex + " could not be found.");
								}
								else {
									// print the scholarship's information, in more detail than before
									backend.printOneScholarshipDetailed(fileIndex);

									System.out.println("\nPlease select an option:");
									System.out.println("1 - go back");
									System.out.println("2 - apply");

									System.out.print("Your choice: ");
									userAction = scnr.nextInt();
									if (userAction == 1) {
										// do nothing?
									}
									else if (userAction == 2) {
										System.out.println("***Have to still implement applying!!!");
										// TODO: implement applying from the terminal and add it here
									}
								}
							}
						} while (fileIndex != -1);
					}
				}


			} while(userSelection != 0);
		}
		// else if (backend.getUserType().compareTo("donor") == 0) {
		else if (backend.getCurrentUser() instanceof DonorProfile) {
			/*
			- create and submit scholarship for review

			*/
			System.out.println("Have to still implement oneUserAction() for DonorProfile.");
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
			System.out.println("Have to still implement oneUserAction() for FundStewardProfile.");
		}
		// else if (backend.getUserType().compareTo("admin") == 0) {
		else if (backend.getCurrentUser() instanceof AdminProfile) {
			// TODO: implement this!!
			/*
			 * - approve scholarships
			 * - search students
			 */
			do {
				System.out.println("Options: ");
				System.out.println("1 - view unapproved scholarships to approve them");
				System.out.println("0 - EXIT");

				System.out.print("Your selection: ");
				userSelection = scnr.nextInt();

				// if they want to view unapproved scholarships to approve them
				if (userSelection == 1) {
					System.out.println("Unapproved scholarships:");
					// TODO: clean this up!!!
					// ****old code:
					// get all the unapproved scholarships and print a number and the scholarship name and description
					// ArrayList<Scholarship> unapprovedSchols = new ArrayList<Scholarship>();
					// for (int i = 0; i < unapprovedSchols.size(); i++) {
					// 	System.out.println((i + 1) + ": " + unapprovedSchols.get(i).getName());
					// 	System.out.println(unapprovedSchols.get(i).getDescription());
					// 	System.out.println();
					// }

					// call printAllScholarships, we want basic info and only unapproved scholarships
					backend.printAllScholarships(false, false, false, true);

					// prompt them to approve one scholarship

					int scholFileIndexToApprove = -1;
					do {
						System.out.print("Please enter a scholarship file index that you want to approve, or -1 if you are done: ");
						scholFileIndexToApprove = scnr.nextInt();

						// if they want to approve a scholarship
						if (scholFileIndexToApprove != -1) {
							// TODO: will this go all the way through the chain of pointers and actually change the original scholarship object????
							System.out.println("Setting the scholarship to approved");
							// unapprovedSchols.get(scholFileIndexToApprove).setApproved(true);
							backend.getOneScholarshipByFileIndex(scholFileIndexToApprove).setApproved(true);

							System.out.println("Checking if the scholarship was actually successfully approved. isApproved: " + backend.getOneScholarshipByFileIndex(scholFileIndexToApprove).getIsApproved());
						}
					} while(scholFileIndexToApprove != -1);
				}

				else if (userSelection > 1) {
					System.out.println("Have to still write code for userSelection values > 1 for AdminProfile in oneUserAction()");
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
					System.out.println(scholarshipObj.getName() + ", due date: " + scholarshipObj.getDateDueString() + ", due 5+ years ago: " + scholarshipObj.due5PlusYearsAgo());
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
				System.out.println("all admin names: ");
				for (AdminProfile admin : backend.getAllAdmins()) {
					System.out.println("'" + admin.getName() + "'");
				}

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
			}
			
			else if (userSelection == 6) {
				BackendSystem backend = new BackendSystem();

				// TODO: just using the first one for now, not exactly sure
				// pick one donor and make a duplicate folder with the files for that donor to test this
				DonorProfile donorToCopy = backend.getAllDonors().get(0);

				backend.storeNewDonorProfile(donorToCopy);
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
				

				backend.editStudentInfo(newStudent);
				System.out.println(newStudent.toString());
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
