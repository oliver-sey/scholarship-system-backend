/**
 * A main file, that will run our whole project.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.ArrayList;




public class Main {
	public static void main(String[] args) {
		

       
	}


	//reads student demographics from comma seperated file and initializes a 
	public static StudentProfile readStudentProfile(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
		// Read the lines from the file
		String line = br.readLine();

		// Split the line into an array of strings
		String[] values = line.split(",");

		// Extract values from the array and convert them to the appropriate types
		String firstName = values[0].trim();
		String lastName = values[1].trim();
		int studentID = Integer.parseInt(values[2].trim());
		String username = values[3].trim();
		String password = values[4].trim();
		String major = values[5].trim();
		boolean hasAMinor = Boolean.parseBoolean(values[6].trim());
		String minor = values[7].trim();
		boolean isUSCitizen = Boolean.parseBoolean(values[8].trim());
		float GPA = Float.parseFloat(values[9].trim());
		boolean inGoodStanding = Boolean.parseBoolean(values[10].trim());
		boolean hasAdvStanding = Boolean.parseBoolean(values[11].trim());
		String gradeLevel = values[12].trim();
		int gradMonth = Integer.parseInt(values[13].trim());
		int gradYear = Integer.parseInt(values[14].trim());
		String gender = values[15].trim();
		boolean isFullTimeStudent = Boolean.parseBoolean(values[16].trim());
		boolean isTransferStudent = Boolean.parseBoolean(values[17].trim());
		int curNumCredits = Integer.parseInt(values[18].trim());
		boolean receivesFunding = Boolean.parseBoolean(values[19].trim());
		String personalStatement = values[20].trim();

		// Create and return a new instance of StudentProfile
		return new StudentProfile(firstName, lastName, studentID, username, password, major, hasAMinor, minor, isUSCitizen, GPA, inGoodStanding, hasAdvStanding,
				gradeLevel, gradMonth, gradYear, gender, isFullTimeStudent, isTransferStudent,
				curNumCredits, receivesFunding, personalStatement);
        
	}

	//reads a text file and converts comma seperated text to a list
	public static ArrayList<String> ConvertTextToArray(String filePath) throws IOException {
		ArrayList<String> textArray = new ArrayList<>();

		BufferedReader br = new BufferedReader(new FileReader(filePath));

		String line;
		while ((line = br.readLine()) != null) {
			textArray.add(line);
		}

		return textArray;
	}

	public static Scholarship readScholarship(String detailsFile, String applicationFile, String requirementsFile, DonorProfile donor) throws IOException {
        BufferedReader detailsBr = new BufferedReader(new FileReader(detailsFile));
		ArrayList<String> application = new ArrayList<String>();
		ArrayList<String> requirements = new ArrayList<String>();
		
		// Read the lines from the file
		String line = detailsBr.readLine();

		// Split the line into an array of strings
		String[] values = line.split(",");

		// Extract values from the array and convert them to the appropriate types
		String name = values[0].trim();
		String description = values[1].trim();
		float awardAmount = Float.parseFloat(values[2].trim());

		detailsBr.close();
		
		BufferedReader applicationBr = new BufferedReader(new FileReader(applicationFile));

		line = applicationBr.readLine();
		values = null;
		values = line.split(",");

		for (int i = 0; i < values.length; i++) {
			application.add(values[i]);
		}

		applicationBr.close();

		BufferedReader requirementsBr = new BufferedReader(new FileReader(requirementsFile));

		line = requirementsBr.readLine();
		values = null;
		values = line.split(",");

		for (int i = 0; i < values.length; i++) {
			requirements.add(values[i]);
		}

		requirementsBr.close();

		// Create and return a new instance of StudentProfile
		return new Scholarship(name, description, donor, awardAmount, requirements, application);
        
	}
}
