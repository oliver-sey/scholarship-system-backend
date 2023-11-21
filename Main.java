
/**
 * A main file, that will run our whole project.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class Main {
	public static void main(String[] args) throws IOException {
		ArrayList<StudentProfile> students = new ArrayList<StudentProfile>();
		ArrayList<DonorProfile> donors = new ArrayList<DonorProfile>();
		// StudentProfile student = readStudentProfile();
		// System.out.print(student.toString());

		students = InstantiateAllStudents();
		donors = InstantiateAllDonors();

		Scholarship scholarship = ReadScholarship(1, donors, students);
		System.out.println(scholarship.toString());
	}

	// reads student demographics from comma separated file and initializes a
	// _______????
	public static StudentProfile readStudentProfile(int fileIndex) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("students/student" + String.valueOf(fileIndex) + ".txt"));

		ArrayList<String> values = new ArrayList<String>();
		String str;

		while ((str = br.readLine()) != null) {
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
		return new StudentProfile(firstName, lastName, studentID, username, password, major, hasAMinor, minor,
				isUSCitizen, GPA, inGoodStanding, hasAdvStanding,
				gradeLevel, gradMonth, gradYear, gender, isFullTimeStudent, isTransferStudent,
				curNumCredits, receivesFunding, personalStatement, fileIndex);

	}

	//writes student profile data to file
	public static void StoreStudentProfile(StudentProfile student, int fileIndex) throws IOException {
		File studentFile = new File("students/student" + String.valueOf(fileIndex) + ".txt");

		studentFile.createNewFile();

		FileWriter writer = new FileWriter(studentFile);

		writer.write(student.getFileText());

		writer.close();
	}

	//find next index available in folder to store new object
	public static int findNextFileIndex(String dataType) throws Exception{
		int fileIndex;
		
		//counts files in appropriate folder and returns next available
		if (dataType.compareTo("donor") == 0) {
			File dir = new File("donors");
			
			fileIndex = dir.listFiles().length + 1;
		}
		else if (dataType.compareTo("scholarhsip") == 0) {
			File dir = new File("scholarships");
			
			fileIndex = dir.listFiles().length + 1;
		}
		else if (dataType.compareTo("student") == 0) {
			File dir = new File("students");
			
			fileIndex = dir.listFiles().length + 1;
		}
		else {
			throw new Exception("Data type is not valid.");
		}

		return fileIndex;
	}

	// reads a text file and converts comma separated text to a list
	//Maybe not needed
	public static ArrayList<String> ConvertTextToArray(String filePath) throws IOException {
		ArrayList<String> textArray = new ArrayList<>();

		BufferedReader br = new BufferedReader(new FileReader(filePath));

		String line;
		while ((line = br.readLine()) != null) {
			textArray.add(line);
		}

		br.close();

		return textArray;
	}

	// creates scholarship object from 3 files and donor object
	public static Scholarship ReadScholarship(int fileIndex, ArrayList<DonorProfile> donors,
			ArrayList<StudentProfile> students) throws IOException {
		String folderPath = "scholarships/scholarship" + String.valueOf(fileIndex);
		BufferedReader detailsBr = new BufferedReader(new FileReader(folderPath + "/details.txt"));
		ArrayList<String> application = new ArrayList<String>();
		ArrayList<String> requirements = new ArrayList<String>();
		ArrayList<StudentProfile> applicants = new ArrayList<StudentProfile>();
		DonorProfile correctDonor = new DonorProfile("", "", "", "");

		ArrayList<String> values = new ArrayList<String>();
		String str;

		// read details file and store in variables
		while ((str = detailsBr.readLine()) != null) {
			values.add(str);
		}

		String name = values.get(0);
		String description = values.get(1);
		String donorName = values.get(2);
		float awardAmount = Float.parseFloat(values.get(3));
		boolean isApproved = Boolean.parseBoolean(values.get(4));
		boolean isArchived = Boolean.parseBoolean(values.get(5));
		
		detailsBr.close();

		// read application file and store in array
		BufferedReader applicationBr = new BufferedReader(new FileReader(folderPath + "/application.txt"));

		values.clear();
		while ((str = applicationBr.readLine()) != null) {
			values.add(str);
		}

		application = values;

		applicationBr.close();

		// read requirements file and store in array
		BufferedReader requirementsBr = new BufferedReader(new FileReader(folderPath + "/requirements.txt"));

		values.clear();
		while ((str = requirementsBr.readLine()) != null) {
			values.add(str);
		}

		requirements = values;

		requirementsBr.close();

		// read applicants file
		BufferedReader applicantsBr = new BufferedReader(new FileReader(folderPath + "/applicants.txt"));

		values.clear();
		while ((str = applicantsBr.readLine()) != null) {
			values.add(str);
		}

		applicantsBr.close();

		// find student objects
		for (String applicantName : values) {
			for (StudentProfile student : students) {
				if (applicantName.compareTo(student.getName()) == 0) {
					applicants.add(student);
				}
			}
		}

		// find donor object
		for (DonorProfile donor : donors) {
			if (donorName.compareTo(donor.getName()) == 0) {
				correctDonor = donor;
			}
		}

		return new Scholarship(name, description, correctDonor, awardAmount, requirements, application, applicants,
				isApproved, isArchived, fileIndex);

	}

	// instantiate all scholarships
	public static ArrayList<Scholarship> InstantiateAllScholarships(ArrayList<DonorProfile> donors,
			ArrayList<StudentProfile> students) {
		ArrayList<Scholarship> scholarships = new ArrayList<Scholarship>();
		// open the 'scholarships' folder
		File dir = new File("scholarships");
		// all the files/folders in the scholarships folder
		File[] directoryListing = dir.listFiles();
		Scholarship scholarship;
		int fileIndex = 1;
		
		// loop through the list of files/folders that are in the 'scholarships' folder
		// each child is for one scholarship
		for (File child : directoryListing) {

			try {
				scholarship = ReadScholarship(fileIndex, donors, students);
				scholarships.add(scholarship);
			} catch (IOException except) {
				System.out.println("File not found: " + child.getAbsolutePath());
			}

			fileIndex++;
		}

		return scholarships;
	}

	public static void StoreScholarship(Scholarship scholarship, int fileIndex) {
		
		/*
		File studentFile = new File("students/student" + String.valueOf(fileIndex) + ".txt");

		studentFile.createNewFile();

		FileWriter writer = new FileWriter(studentFile);

		writer.write(student.getFileText());

		writer.close();
		*/
	}

	public static ArrayList<StudentProfile> InstantiateAllStudents() {
		ArrayList<StudentProfile> students = new ArrayList<StudentProfile>();
		File dir = new File("students");
		File[] directoryListing = dir.listFiles();
		StudentProfile student;
		int fileIndex = 1;

		for (File child : directoryListing) {

			try {
				student = readStudentProfile(fileIndex);
				students.add(student);
			} catch (IOException except) {
				System.out.println("File not found: " + child.getAbsolutePath());
			}

			fileIndex++;
		}

		return students;
	}

	public static ArrayList<DonorProfile> InstantiateAllDonors() {
		ArrayList<DonorProfile> donors = new ArrayList<DonorProfile>();
		File dir = new File("donors");
		File[] directoryListing = dir.listFiles();
		DonorProfile donor;

		for (File child : directoryListing) {

			try {
				donor = readDonor(child.getCanonicalPath());
				donors.add(donor);
			} catch (IOException except) {
				System.out.println("File not found: " + child.getAbsolutePath());
			}

		}

		return donors;
	}

	// creates match object from student object, scholarship object, and file
	public static MatchRelationship readMatch(ArrayList<Scholarship> scholarships, ArrayList<StudentProfile> students, int ID)
			throws NumberFormatException, IOException {
		String folderPath = "matches/match" + String.valueOf(ID);
		BufferedReader detailsBr = new BufferedReader(new FileReader(folderPath + "/details.txt"));
		StudentProfile student = new StudentProfile();
		Scholarship scholarship = new Scholarship();
		ArrayList<String> application = new ArrayList<String>();

		String studentName = detailsBr.readLine();
		String scholarshipName = detailsBr.readLine();
		float matchPercentage = Float.parseFloat(detailsBr.readLine());
		float matchIndex = Float.parseFloat(detailsBr.readLine());
		String applicationStatus = detailsBr.readLine();

		detailsBr.close();

		// find student objects
		for (StudentProfile currStudent : students) {
			if (studentName.compareTo(currStudent.getName()) == 0) {
				student = currStudent;
			}
		}

		// find scholarship object
		for (Scholarship currScholarship : scholarships) {
			if (scholarshipName.compareTo(currScholarship.getName()) == 0) {
				scholarship = currScholarship;
			}
		}

		BufferedReader applicationBr = new BufferedReader(new FileReader(folderPath + "/application.txt"));

		ArrayList<String> values = new ArrayList<String>();
		String str;

		// read details file and store in variables
		while ((str = applicationBr.readLine()) != null) {
			values.add(str);
		}

		application = values;
		applicationBr.close();

		return new MatchRelationship(student, scholarship, ID, matchPercentage, matchIndex, application, applicationStatus);
	}

	public static ArrayList<MatchRelationship> InstantiateAllMatches(ArrayList<Scholarship> scholarships, 
			ArrayList<StudentProfile> students) throws NumberFormatException, IOException {

		ArrayList<MatchRelationship> matches = new ArrayList<MatchRelationship>();
		File dir = new File("matches");		
		MatchRelationship match;

		for (int i = 1; i <= dir.listFiles().length; i++) {

			match = readMatch(scholarships, students, i);
			matches.add(match);
		}

		return matches;

	}

	// creates donor object from file
	public static DonorProfile readDonor(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		ArrayList<String> values = new ArrayList<String>();
		String str;

		// read details file and store in variables
		while ((str = br.readLine()) != null) {
			values.add(str);
		}

		String firstName = values.get(0);
		String lastName = values.get(1);
		String username = values.get(2);
		String password = values.get(3);

		br.close();

		return new DonorProfile(firstName, lastName, username, password);
	}

	// search donors by name
	//Maybe not needed
	public static DonorProfile SearchForDonor(String donorName) throws IOException, Exception {
		File dir = new File("donors");
		File[] directoryListing = dir.listFiles();
		String correctDonorPath = "";

		try {

			for (File child : directoryListing) {
				BufferedReader br = new BufferedReader(new FileReader(child));
				ArrayList<String> values = new ArrayList<String>();
				String str;

				// read details file and store in variables
				while ((str = br.readLine()) != null) {
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

		} catch (FileNotFoundException except) {
			System.out.println("No donors in system.");
		}

		return readDonor(correctDonorPath);

	}

	// searches a folder for a scholarship with inputted value
	public static ArrayList<Scholarship> searchScholarships(String inputCategory, String inputSearchValue,
		ArrayList<Scholarship> scholarshipsToSearch) {
		ArrayList<Scholarship> scholarshipsFound = new ArrayList<Scholarship>();
		HashMap<String, String> requirements = new HashMap<String, String>();

		if (inputCategory.compareTo("name") == 0) {
			for (Scholarship scholarship: scholarshipsToSearch) {
				// TODO: ****implement me!
			}
		} else if (inputCategory.compareTo("donor") == 0) {
			//retrieves donor name from each scholarship
			for (Scholarship scholarship: scholarshipsToSearch) {
				if (inputSearchValue.compareTo(scholarship.getDonorName()) == 0) {
					scholarshipsFound.add(scholarship);
				}
			}
		} else if (inputCategory.compareTo("applicant") == 0) {
			//retrieves array of applicant names from each scholarship and iterates through them
			ArrayList<String> applicantNames = new ArrayList<String>();

			for (Scholarship scholarship: scholarshipsToSearch) {
				applicantNames = scholarship.getApplicantNames();

				for (String name : applicantNames){
					if (inputSearchValue.compareTo(name) == 0) {
						scholarshipsFound.add(scholarship);
					}
				}
				applicantNames.clear();
			}
		} else {
			//assumes any search category will be a requirement
			//retrieves requirement hashmap from scholarship and compares category and value
			for (Scholarship scholarship : scholarshipsToSearch) {
				requirements = scholarship.getRequirements();

				for (Map.Entry<String, String> entry : requirements.entrySet()) {
					if (entry.getKey().compareTo(inputCategory) == 0){
						if (entry.getValue().compareTo(inputSearchValue) == 0) {
							scholarshipsFound.add(scholarship);
						}
					}
					
				}
			}
		}

		return scholarshipsFound;
	}
}
