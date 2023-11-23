
/**
 * A main file, that will run our whole project.
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws Exception {
		runDifferentTests();

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
				System.out.println("all scholarship names: ");
				for (Scholarship scholarship : backend.getAllScholarships()) {
					System.out.println("'" + scholarship.getName() + "'");
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
