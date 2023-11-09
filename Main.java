/**
 * A main file, that will run our whole project.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;




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
		int studentID = Integer.parseInt(values[0].trim());
		String major = values[1].trim();
		String minor = values[2].trim();
		boolean hasAMinor = Boolean.parseBoolean(values[3].trim());
		boolean isUSCitizen = Boolean.parseBoolean(values[4].trim());
		float GPA = Float.parseFloat(values[5].trim());
		boolean inGoodStanding = Boolean.parseBoolean(values[6].trim());
		boolean hasAdvStanding = Boolean.parseBoolean(values[7].trim());
		String gradeLevel = values[8].trim();
		int gradMonth = Integer.parseInt(values[9].trim());
		int gradYear = Integer.parseInt(values[10].trim());
		String gender = values[11].trim();
		boolean isFullTimeStudent = Boolean.parseBoolean(values[12].trim());
		boolean isTransferStudent = Boolean.parseBoolean(values[13].trim());
		int curNumCredits = Integer.parseInt(values[14].trim());
		boolean receivesFunding = Boolean.parseBoolean(values[15].trim());
		String personalStatement = values[16].trim();

		// Create and return a new instance of StudentProfile
		return new StudentProfile(studentID, major, minor, hasAMinor, isUSCitizen, GPA, inGoodStanding, hasAdvStanding,
				gradeLevel, gradMonth, gradYear, gender, isFullTimeStudent, isTransferStudent,
				curNumCredits, receivesFunding, personalStatement);
        
	}
}
