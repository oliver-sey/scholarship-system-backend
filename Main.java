
/**
 * A main file, that will run our whole project.
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws Exception {
		BackendSystem backend = new BackendSystem();
		// userRunSystem will prompt them to log in
		// userRunSystem(backend);

		runDifferentTests();

	}

	// prompt the user to log in and get their 
	public static void userRunSystem(BackendSystem backend) {
		// prompt the user to log in, when this line of code is done they are successfully logged in or the system should stop
		boolean successfulLogin = backend.login();

		if (successfulLogin) {
			String userInput = "";
			do {
				// prompt them for one action
				oneUserAction(backend);

				// see if they want to quit
				Scanner scnr = new Scanner(System.in);
				System.out.print("Enter 'q' to quit, or type anything else to keep going: ");
				userInput = scnr.next();
			} while (userInput != "q");
		}
		// else just quit
	}


	/**
	 * You should get the user to log in before calling this!
	 * prompts the user for which action they want to perform based on their profile type
	 */
	public static void oneUserAction(BackendSystem backend) {
		Scanner scnr = new Scanner(System.in);
		int userSelection = -1;
		// TODO: should we use clearance level?
		if (backend.getCurrentUser() instanceof StudentProfile) {
			// TODO: implement this!!
			System.out.println("Have to still implement oneUserAction() for StudentProfile.");
		}
		else if (backend.getCurrentUser() instanceof DonorProfile) {
			// TODO: implement this!!
			System.out.println("Have to still implement oneUserAction() for DonorProfile.");
		}
		else if (backend.getCurrentUser() instanceof StaffProfile) {
			// TODO: implement this!!
			System.out.println("Have to still implement oneUserAction() for StaffProfile.");
		}
		else if (backend.getCurrentUser() instanceof FundStewardProfile) {
			// TODO: implement this!!
			System.out.println("Have to still implement oneUserAction() for FundStewardProfile.");
		}
		else if (backend.getCurrentUser() instanceof AdminProfile) {
			// TODO: implement this!!
			System.out.println("Options: ");
			System.out.println("0 - view unapproved scholarships to approve them");

			System.out.print("Your selection: ");
			userSelection = scnr.nextInt();

			// if they want to view unapproved scholarships to approve them
			if (userSelection == 0) {
				System.out.println("Unapproved scholarships:");
				// get all the unapproved scholarships and print a number and the scholarship name and description
				ArrayList<Scholarship> unapprovedSchols = new ArrayList<Scholarship>();
				for (int i = 0; i < unapprovedSchols.size(); i++) {
					System.out.println((i + 1) + ": " + unapprovedSchols.get(i).getName());
					System.out.println(unapprovedSchols.get(i).getDescription());
					System.out.println();
				}

				// prompt them to approve one scholarship

				int scholNumToApprove = -1;
				do {
					System.out.print("Please enter a scholarship number that you want to approve, or -1 if you are done: ");
					scholNumToApprove = scnr.nextInt();

					// if they want to approve a scholarship
					if (scholNumToApprove != -1) {
						// TODO: will this go all the way through the chain of pointers and actually change the original scholarship object????
						System.out.println("Setting the scholarship to approved");
						unapprovedSchols.get(scholNumToApprove).setApproved(true);
					}
				} while(scholNumToApprove != -1);
			}

			else if (userSelection == 1) {
				System.out.println("Have to still write code for userSelection == 1 for AdminProfile in oneUserAction()");
			}
		}
		else {
			// should never get here
			System.out.println("Invalid user type in userOptions");
		}
		
		

	}

	public static void runDifferentTests() throws Exception {
		Scanner scnr = new Scanner(System.in);
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
			System.out.println("7 - login as an admin and then approve scholarships");
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
				// System.out.println("all donors: " + backend.getAllDonors());
				System.out.println("all admin names: ");
				for (AdminProfile admin : backend.getAllAdmins()) {
					System.out.println("'" + admin.getName() + "'");
				}
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

			else if (userSelection == 7) {
				// TODO: fill this out
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
