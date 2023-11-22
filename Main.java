
/**
 * A main file, that will run our whole project.
 */

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		// TODO: do we need a backend instance in the main method, or should each testing method
		// just make its own?
		// BackendSystem backend = new BackendSystem();

		// call my method that checks if my Scholarship.due5PlusYearsAgo() method works
		check5YearsPastDue();
	}

	public static void check5YearsPastDue() throws IOException {
		// make a new BackendSystem object, which calls methods to 
		// instantiate all scholarships, donors, etc.
		BackendSystem backend = new BackendSystem();

		// scholarship4 has a due date that's more than 5 years ago, want to check if
		// my Scholarship.due5PlusYearsAgo() method works
		Scholarship oldScholarship = backend.getAllScholarships().get(4);

		System.out.println("Was due 5+ years ago: " + oldScholarship.due5PlusYearsAgo());
		// TODO: eventually write code to archive scholarships past 5 years?? I guess it's not
		// in the requirement but maybe we should add it

		// and then here we could check if the archive status got changed to true
	}

}
