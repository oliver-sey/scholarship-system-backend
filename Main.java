/**
 * A main file, that will run our whole project.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;




public class Main {
	public static void main(String[] args) throws IOException {
	
		//StudentProfile student = readStudentProfile();
		//System.out.print(student.toString());

		Scholarship scholarship = ReadScholarship("scholarships/scholarship1");
		System.out.print(scholarship.toString());
       
	}


	//reads student demographics from comma seperated file and initializes a 
	public static StudentProfile readStudentProfile(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
		
		ArrayList<String> values = new ArrayList<String>();
		String str;

		while((str = br.readLine()) != null) {
			values.add(str);
		}

		br.close();

		// Extract values from the array and convert them to the appropriate types
		String firstName = values.get(0);
		String lastName = values.get(1);
		int studentID = Integer.parseInt(values.get(2));
		String username = values.get(3);
		String password = values.get(4);
		String major = values.get(5);
		boolean hasAMinor = Boolean.parseBoolean(values.get(6));
		String minor = values.get(7);
		boolean isUSCitizen = Boolean.parseBoolean(values.get(8));
		float GPA = Float.parseFloat(values.get(9));
		boolean inGoodStanding = Boolean.parseBoolean(values.get(10));
		boolean hasAdvStanding = Boolean.parseBoolean(values.get(11));
		String gradeLevel = values.get(12);
		int gradMonth = Integer.parseInt(values.get(13));
		int gradYear = Integer.parseInt(values.get(14));
		String gender = values.get(15);
		boolean isFullTimeStudent = Boolean.parseBoolean(values.get(16));
		boolean isTransferStudent = Boolean.parseBoolean(values.get(17));
		int curNumCredits = Integer.parseInt(values.get(18));
		boolean receivesFunding = Boolean.parseBoolean(values.get(19));
		String personalStatement = values.get(20);

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

	//creates scholarship object from 3 files and donor object
	public static Scholarship ReadScholarship(String folderPath) throws IOException {
        BufferedReader detailsBr = new BufferedReader(new FileReader(folderPath + "/details.txt"));
		ArrayList<String> application = new ArrayList<String>();
		ArrayList<String> requirements = new ArrayList<String>();
		DonorProfile donor;
		//scholarships\scholarship1\requirements.txt
		
		ArrayList<String> values = new ArrayList<String>();
		String str;

		//read deatails file and store in variables
		while((str = detailsBr.readLine()) != null) {
			values.add(str);
		}

		String name = values.get(0);
		String description = values.get(1);
		String donorName = values.get(2);
		float awardAmount = Float.parseFloat(values.get(3));
		boolean isApproved = Boolean.parseBoolean(values.get(4));
		boolean isArchived = Boolean.parseBoolean(values.get(5));

		detailsBr.close();
		
		//read application file and store in array
		BufferedReader applicationBr = new BufferedReader(new FileReader(folderPath + "/application.txt"));

		values.clear();
		while((str = applicationBr.readLine()) != null) {
			values.add(str);
		}

		application = values;

		applicationBr.close();

		//read requirements file and store in array
		BufferedReader requirementsBr = new BufferedReader(new FileReader(folderPath + "/requirements.txt"));

		values.clear();
		while((str = requirementsBr.readLine()) != null) {
			values.add(str);
		}

		requirements = values;

		requirementsBr.close();

		//find donor object
		try {
			donor = SearchForDonor(donorName);
		}
		catch(Exception except) {
			System.out.println(except.getMessage());
			donor = new DonorProfile("", "", "", "");
		}

		return new Scholarship(name, description, donor, awardAmount, requirements, application, applicants, isApproved, isArchived);
        
	}

	//instansiate all scholarships
	public static ArrayList<Scholarship> InstansiateAllScholarships() {
		ArrayList<Scholarship> scholarships = new ArrayList<Scholarship>();
		File dir = new File("scholarships");
  		File[] directoryListing = dir.listFiles();
		Scholarship scholarship;

		for (File child : directoryListing) {

			try {
				scholarship  = ReadScholarship(child.getCanonicalPath());
				scholarships.add(scholarship);
			}
			catch (IOException except) {
				System.out.println("File not found.");
			}
			
		}

		return scholarships;
	}

	public static ArrayList<StudentProfile> InstansiateAllStudents() {
		ArrayList<StudentProfile> students = new ArrayList<StudentProfile>();
		File dir = new File("students");
  		File[] directoryListing = dir.listFiles();
		StudentProfile student;

		for (File child : directoryListing) {

			try {
				student  = readStudentProfile(child.getCanonicalPath());
				students.add(student);
			}
			catch (IOException except) {
				System.out.println("File not found.");
			}
			
		}

		return students;
	}

	//creates match object from student object, scholarship object, and file
	public static MatchRelationship initializeMatch(StudentProfile student, Scholarship scholarship, String filePath) throws NumberFormatException, IOException {
		float matchPercentage;
		float matchIndex;
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		
		matchPercentage = Float.parseFloat(br.readLine());
		matchIndex = Float.parseFloat(br.readLine());

		br.close();

		return new MatchRelationship(student, scholarship, matchPercentage, matchIndex);
	}

	//creates donor object from file
	public static DonorProfile readDonor(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		ArrayList<String> values = new ArrayList<String>();
		String str;

		//read deatails file and store in variables
		while((str = br.readLine()) != null) {
			values.add(str);
		}

		String firstName = values.get(0);
		String lastName = values.get(1);
		String username = values.get(2);
		String password = values.get(3);

		br.close();

		return new DonorProfile(firstName, lastName, username, password);
	}

	//search donors by name
	public static DonorProfile SearchForDonor(String donorName) throws IOException, Exception {
		File dir = new File("donors");
  		File[] directoryListing = dir.listFiles();
		String correctDonorPath = "";

  		try {

			for (File child : directoryListing) {
      			BufferedReader br = new BufferedReader(new FileReader(child));
				ArrayList<String> values = new ArrayList<String>();
				String str;

				//read deatails file and store in variables
				while((str = br.readLine()) != null) {
					values.add(str);
				}

				String firstName = values.get(0);
				String lastName = values.get(1);

				br.close();

				String fullNameFound = firstName + " " + lastName;

				if (donorName.equals(fullNameFound)) {
					correctDonorPath = child.getPath();
				}
    		}

			if (correctDonorPath == "") {
				throw new Exception("Donor not found.");
			}

		} 
		catch (FileNotFoundException exept) {
			System.out.println("No donors in system.");
		} 

		return readDonor(correctDonorPath);

	}
	
	//searches a folder for a scholarship with inputted value
	public static ArrayList<Scholarship> searchScholarships(String inputCategory, String inputSearchValue, ArrayList<Scholarship> scholarshipsToSearch) {
		ArrayList<Scholarship> scholarshipsFound = new ArrayList<Scholarship>();
		HashMap<String, String> requirements = new HashMap<String, String>();

		if (inputCategory.compareTo("name") == 0) {

		}
		else if (inputCategory.compareTo("donor") == 0) {

		}
		else if (inputCategory.compareTo("applicant") == 0) {

		}
		else {
			for(Scholarship scholarship : scholarshipsToSearch) {
				requirements = scholarship.getRequirments();

				for (Map.Entry<String, String> entry : requirements.entrySet()) {
					//String key = entry.getKey();
					//Integer value = entry.getValue();
				}
			}
		}
		
		return scholarshipsFound;
	}
}
