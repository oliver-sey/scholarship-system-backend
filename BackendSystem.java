import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class BackendSystem {

	private ArrayList<StudentProfile> allStudents = new ArrayList<StudentProfile>();
	private ArrayList<Scholarship> allScholarships = new ArrayList<Scholarship>();
	private ArrayList<MatchRelationship> allMatchRelationships = new ArrayList<MatchRelationship>();
	private ArrayList<DonorProfile> allDonors = new ArrayList<DonorProfile>();
	private ArrayList<AdminProfile> allAdmins = new ArrayList<AdminProfile>();

	// the user that is currently using the system
	private Profile currentUser;

	// constructor
	public BackendSystem() throws NumberFormatException, IOException {
		this.allStudents = InstantiateAllStudents();
		this.allScholarships = InstantiateAllScholarships();
		this.allDonors = InstantiateAllDonors();
		this.allMatchRelationships = InstantiateAllMatches();
		// TODO: make the rest of the instantiate all methods
	}

	public StudentProfile readStudentProfile(int fileIndex) throws IOException {
		String folderPath = "students/student" + String.valueOf(fileIndex);
		BufferedReader detailsBr = new BufferedReader(new FileReader(folderPath + "/details.txt"));

		ArrayList<String> values = new ArrayList<String>();
		String str;

		while ((str = detailsBr.readLine()) != null) {
			values.add(str);
		}

		detailsBr.close();

		ArrayList<Scholarship> awardedScholarships = new ArrayList<Scholarship>();

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
		String sq1 = values.get(21);
		String sq2 = values.get(22);
		String sq3 = values.get(23);

		BufferedReader awardsBr = new BufferedReader(new FileReader(folderPath + "/awards.txt"));

		values.clear();

		while ((str = awardsBr.readLine()) != null) {
			values.add(str);
		}

		awardsBr.close();

		for(String name : values) {
			for (Scholarship scholarship : this.allScholarships) {
				if(name.compareTo(scholarship.getName()) == 0) {
					awardedScholarships.add(scholarship);
				}
			}
		}

		// Create and return a new instance of StudentProfile
		return new StudentProfile(firstName, lastName, studentID, username, password, major, hasAMinor, minor,
				isUSCitizen, GPA, inGoodStanding, hasAdvStanding,
				gradeLevel, gradMonth, gradYear, gender, isFullTimeStudent, isTransferStudent,
				curNumCredits, receivesFunding, personalStatement, sq1, sq2, sq3, awardedScholarships, fileIndex);

	}

	// writes student profile data to file
	public void updateStudentProfileFile(StudentProfile student) throws IOException {
		String folderPath = "students/student" + String.valueOf(student.getFileIndex());
		ArrayList<String> awardNames = new ArrayList<String>();

		File detailsF = new File(folderPath + "/details.txt");
		FileWriter detailsW = new FileWriter(detailsF, false);

		detailsW.write(student.getFileText());

		detailsW.close();

		File awardsF = new File(folderPath + "/awards.txt");
		FileWriter awardsW = new FileWriter(awardsF, false);

		for (Scholarship scholarship : student.getAwardsRecieved()) {
			awardNames.add(scholarship.getName());
		}

		awardsW.write(String.join("\n", awardNames));
		
		awardsW.close();

	}

	public void storeNewStudentProfile(StudentProfile student) throws Exception {
		int nextFileIndex = findNextFileIndex("student");
		String folderPath = "students/student" + String.valueOf(nextFileIndex);
		ArrayList<String> awardNames = new ArrayList<String>();
		File dir = new File(folderPath);
		dir.mkdir();

		File detailsF = new File(folderPath + "/details.txt");
		detailsF.createNewFile();
		FileWriter detailsW = new FileWriter(detailsF);

		detailsW.write(student.getFileText());

		detailsW.close();

		File awardsF = new File(folderPath + "/awards.txt");
		awardsF.createNewFile();
		FileWriter awardsW = new FileWriter(awardsF, false);

		for (Scholarship scholarship : student.getAwardsRecieved()) {
			awardNames.add(scholarship.getName());
		}

		awardsW.write(String.join("\n", awardNames));
		
		awardsW.close();
		
	}

	// find next index available in folder to store new object
	public int findNextFileIndex(String dataType) throws IllegalArgumentException {
		int fileIndex;

		// counts files in appropriate folder and returns next available
		if (dataType.compareTo("donor") == 0) {
			File dir = new File("donors");

			fileIndex = dir.listFiles().length + 1;
		} else if (dataType.compareTo("scholarship") == 0) {
			File dir = new File("scholarships");

			fileIndex = dir.listFiles().length + 1;
		} else if (dataType.compareTo("student") == 0) {
			File dir = new File("students");

			fileIndex = dir.listFiles().length + 1;
		} else if (dataType.compareTo("match") == 0) {
			File dir = new File("matches");

			fileIndex = dir.listFiles().length + 1;
		}
		else {
			throw new IllegalArgumentException("Data type is not valid.");
		}

		return fileIndex;
	}

	// reads a text file and converts comma separated text to a list
	// Maybe not needed
	public ArrayList<String> ConvertTextToArray(String filePath) throws IOException {
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
	public Scholarship ReadScholarship(int fileIndex) throws IOException {
		String folderPath = "scholarships/scholarship" + String.valueOf(fileIndex);
		BufferedReader detailsBr = new BufferedReader(new FileReader(folderPath + "/details.txt"));
		ArrayList<String> application = new ArrayList<String>();
		ArrayList<String> requirements = new ArrayList<String>();
		ArrayList<String> applicantNames = new ArrayList<String>();
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

		// just using the Strings here because that's what we read from the file,
		// and that's what the constructor takes as a parameter
		// The constructor will then convert it to a LocalDate object
		String dateAddedString = values.get(6);
		String dateDueString = values.get(7);

		detailsBr.close();

		// read application file and store in array
		BufferedReader applicationBr = new BufferedReader(new FileReader(folderPath + "/application.txt"));

		while ((str = applicationBr.readLine()) != null) {
			application.add(str);
		}


		applicationBr.close();

		// read requirements file and store in array
		BufferedReader requirementsBr = new BufferedReader(new FileReader(folderPath + "/requirements.txt"));

		while ((str = requirementsBr.readLine()) != null) {
			requirements.add(str);
		}

		requirementsBr.close();

		// read applicants file
		BufferedReader applicantsBr = new BufferedReader(new FileReader(folderPath + "/applicants.txt"));

		while ((str = applicantsBr.readLine()) != null) {
			applicantNames.add(str);
		}

		applicantsBr.close();

		// find student objects
		for (String applicantName : applicantNames) {
			for (StudentProfile student : this.allStudents) {
				if (applicantName.compareTo(student.getName()) == 0) {
					applicants.add(student);
				}
			}
		}

		// find donor object
		for (DonorProfile donor : this.allDonors) {
			if (donorName.compareTo(donor.getName()) == 0) {
				correctDonor = donor;
			}
		}

		return new Scholarship(name, description, correctDonor, awardAmount, requirements, application, applicants,
				isApproved, isArchived, fileIndex, dateAddedString, dateDueString);

	}

	// instantiate all scholarships
	public ArrayList<Scholarship> InstantiateAllScholarships() {
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
				scholarship = ReadScholarship(fileIndex);
				scholarships.add(scholarship);
			} catch (IOException except) {
				System.out.println("File not found: " + child.getAbsolutePath());
			}

			fileIndex++;
		}

		return scholarships;
	}

	public void StoreScholarship(Scholarship scholarship, int fileIndex) throws IOException {

		File folder = new File("scholarships/scholarship" + String.valueOf(fileIndex));
		folder.mkdirs();

		File detailsFile = new File(folder, "details.txt");
		FileWriter detailsWriter = new FileWriter(detailsFile);

		detailsWriter.write(scholarship.getDetailsFileText());
		detailsWriter.close();

		File applicationFile = new File(folder, "application.txt");
		FileWriter applicationWriter = new FileWriter(applicationFile);

		applicationWriter.write(scholarship.getApplicationFileText());
		applicationWriter.close();

		File applicantsFile = new File(folder, "applicants.txt");
		FileWriter applicantsWriter = new FileWriter(applicantsFile);

		applicantsWriter.write(scholarship.getApplicantsFileText());
		applicantsWriter.close();

		File requirementsFile = new File(folder, "requirements.txt");
		FileWriter requirementsWriter = new FileWriter(requirementsFile);

		requirementsWriter.write(scholarship.getRequirementsFileText());
		requirementsWriter.close();

	}

	public ArrayList<StudentProfile> InstantiateAllStudents() {
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

	public ArrayList<DonorProfile> InstantiateAllDonors() {
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
	public MatchRelationship readMatch(int ID)
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
		for (StudentProfile currStudent : this.allStudents) {
			if (studentName.compareTo(currStudent.getName()) == 0) {
				student = currStudent;
			}
		}

		// find scholarship object
		for (Scholarship currScholarship : this.allScholarships) {
			if (scholarshipName.compareTo(currScholarship.getName()) == 0) {
				scholarship = currScholarship;
			}
		}

		BufferedReader applicationBr = new BufferedReader(new FileReader(folderPath + "/application.txt"));

		String str;

		// read details file and store in variables
		while ((str = applicationBr.readLine()) != null) {
			application.add(str);
		}

		applicationBr.close();

		return new MatchRelationship(student, scholarship, ID, matchPercentage, matchIndex, application,
				applicationStatus);
	}

	public ArrayList<MatchRelationship> InstantiateAllMatches() throws NumberFormatException, IOException {

		ArrayList<MatchRelationship> matches = new ArrayList<MatchRelationship>();
		File dir = new File("matches");
		MatchRelationship match;

		for (int i = 1; i <= dir.listFiles().length; i++) {
			match = readMatch(i);
			matches.add(match);
		}

		return matches;

	}

	public void StoreMatch(MatchRelationship match, int fileIndex) throws IOException {
		File folder = new File("matches/match" + String.valueOf(fileIndex));
		folder.mkdirs();

		File detailsFile = new File(folder, "details.txt");
		FileWriter detailsWriter = new FileWriter(detailsFile);

		detailsWriter.write(match.getDetailsFileText());
		detailsWriter.close();

		File applicationFile = new File(folder, "application.txt");
		FileWriter applicationWriter = new FileWriter(applicationFile);

		applicationWriter.write(match.getApplicationFileText());
		applicationWriter.close();
	}

	// creates donor object from file
	public DonorProfile readDonor(String filePath) throws IOException {
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
	// Maybe not needed
	public DonorProfile SearchForDonor(String donorName) throws IOException, Exception {
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
	public ArrayList<Scholarship> searchScholarships(String inputCategory, String inputSearchValue) {
		ArrayList<Scholarship> scholarshipsFound = new ArrayList<Scholarship>();
		HashMap<String, String> requirements = new HashMap<String, String>();

		if (inputCategory.compareTo("name") == 0) {

			double percentage;
			double max1 = 0.0;
			double max2 = 0.0;
			double max3 = 0.0;
			Scholarship schol1 = new Scholarship();
			Scholarship schol2 = new Scholarship();
			Scholarship schol3 = new Scholarship();

			for (Scholarship scholarship : this.allScholarships) {
				percentage = stringSimilarity(scholarship.getName(), inputSearchValue);

				if (percentage - max1 > 0.00001) {
					schol3 = schol2;
					max3 = max2;
					schol2 = schol1;
					max2 = max1;
					schol1 = scholarship;
					max1 = percentage;
				} else if (percentage - max2 > 0.00001) {
					schol3 = schol2;
					max3 = max2;
					schol2 = scholarship;
					max2 = percentage;
				} else if (percentage - max3 > 0.00001) {
					schol3 = scholarship;
					max3 = percentage;
				}
			}

			scholarshipsFound.add(schol1);
			scholarshipsFound.add(schol2);
			scholarshipsFound.add(schol3);

		} else if (inputCategory.compareTo("donor") == 0) {
			// retrieves donor name from each scholarship
			for (Scholarship scholarship : this.allScholarships) {
				if (inputSearchValue.compareTo(scholarship.getDonorName()) == 0) {
					scholarshipsFound.add(scholarship);
				}
			}
		} else if (inputCategory.compareTo("applicant") == 0) {
			// retrieves array of applicant names from each scholarship and iterates through
			// them
			ArrayList<String> applicantNames = new ArrayList<String>();

			for (Scholarship scholarship : this.allScholarships) {
				applicantNames = scholarship.getApplicantNames();

				for (String name : applicantNames) {
					if (inputSearchValue.compareTo(name) == 0) {
						scholarshipsFound.add(scholarship);
					}
				}
				applicantNames.clear();
			}
		} else {
			// assumes any search category will be a requirement
			// retrieves requirement hashmap from scholarship and compares category and
			// value
			for (Scholarship scholarship : this.allScholarships) {
				requirements = scholarship.getRequirements();

				for (Map.Entry<String, String> entry : requirements.entrySet()) {
					if (entry.getKey().compareTo(inputCategory) == 0) {
						if (entry.getValue().compareTo(inputSearchValue) == 0) {
							scholarshipsFound.add(scholarship);
						}
					}

				}
			}
		}

		return scholarshipsFound;
	}

	

	/**
	 * Prompts the user for their login details and then security questions
	 * If this function ends, either the program should end or the user is fully successfully logged in
	 * 
	 * from our discussion/from the elicitation:
	 * get 3 max wrong password attempts then get a message to contact admin,
	 * program stops
	 * after 2 wrong passwords get to still do 3 security question attempts
	 * 
	 * @return returns true for a successful login, false if the login was unsuccessful 
	// and the program should stop!!
	 */
	public boolean login() {
		// TODO: implement me!!!
		Scanner scnr = new Scanner(System.in);
		int returnVal;
		// the number of times the user gets a wrong password
		int failedPWAttempts = 0;
		do {
			System.out.print("Please enter your user type (as one word, i.e. 'Student', 'FundSteward'): ");

			// TODO: what to do with scanning newlines '\n'?
			System.out.print("Please enter your user type: ");
			String userType = scnr.nextLine();

			System.out.print("Please enter your username: ");
			String username = scnr.nextLine();

			System.out.print("Please enter your password: ");
			String password = scnr.nextLine();

			// possible return values from checkLoginDetails():
			// 0 if the username and password matched,
			// 1 if the user could not be found/does not exist,
			// 2 if the user was found but the password was wrong
			// 3 if the user type was not accepted
			returnVal = checkLoginDetails(userType, username, password);

			scnr.close();
			// if the return value is 0, checkLoginDetails() will set the currentUser
			// variable
			if (returnVal == 0) {
				// correct username and password, have to now make them do a security question
				// TODO: get the Profile object here? need it for the call to checkOneSecurity
				// question and will have to return it from this method
				for (int questionNum = 1; questionNum <= 3; questionNum++) {
					// if the security question answer was correct
					if (checkOneSecurityQuestion(questionNum, currentUser)) {
						// return and exit the loop early, don't ask them the later security questions
						return true;
					}
				}
				// if we get here they went through the 3 questions and didn't get any right
				// so we return false for an unsuccessful login
				return false;
			}
			if (returnVal == 1) {
				System.out.println("The entered username could not be found in the system for the entered user type.");
			} else if (returnVal == 2) {
				System.out.println("Incorrect password for the entered username and user type");
				failedPWAttempts++;

				// if they have now gotten their password wrong 3 times, return false
				// for an unsuccessful login, and the program should quit
				if (failedPWAttempts == 3) {
					return false;
				}
			} else if (returnVal == 3) {
				System.out.println("Invalid user type");
				// don't return false, we want to let them try more logins
			}
		} while (returnVal != 0);

		// will never get here but have to return something to make the compiler happy
		return false;
	}

	/**
	 * A method to check if a username exists and if the entered password is correct
	 * 
	 * @param userType        - 'Student', 'Donor', 'Admin', 'FundSteward', 'Staff'
	 * @param enteredUsername - the username/email that the user entered
	 * @param enteredPassword - the password the user entered
	 * 
	 * @return 0 if the username and password matched,
	 *         1 if the user could not be found/does not exist,
	 *         2 if the user was found but the password was wrong
	 *         3 if the user type was not accepted
	 */
	public int checkLoginDetails(String userType, String enteredUsername, String enteredPassword) {
		if (userType.equals("Student")) {
			for (int i = 0; i < allStudents.size(); i++) {
				if (allStudents.get(i).username.equalsIgnoreCase(enteredUsername)) {
					if (allStudents.get(i).password.equals(enteredPassword)) {
						// store this user in the currentUser variable
						currentUser = allStudents.get(i);

						// return 0 since we have a successful username/password match
						return 0;
					} else {
						// valid username but incorrect password
						// usernames are unique so we know that we found the right user
						// but the password was just wrong
						return 2;
					}
				}
			}
		}

		else if (userType.equals("Donor")) {
			for (int i = 0; i < this.allDonors.size(); i++) {
				if (allDonors.get(i).username.equalsIgnoreCase(enteredUsername)) {
					if (allDonors.get(i).password.equals(enteredPassword)) {
						// store this user in the currentUser variable
						currentUser = allDonors.get(i);

						// return 0 since we have a successful username/password match
						return 0;
					} else {
						// valid username but incorrect password
						return 2;
					}
				}
			}
		} else if (userType.equals("Admin")) {
			for (int i = 0; i < this.allAdmins.size(); i++) {
				if (allAdmins.get(i).username.equalsIgnoreCase(enteredUsername)) {
					if (allAdmins.get(i).password.equals(enteredPassword)) {
						// store this user in the currentUser variable
						currentUser = allAdmins.get(i);

						// return 0 since we have a successful username/password match
						return 0;
					} else {
						// valid username but incorrect password
						return 2;
					}
				}
			}
		} else if (userType.equals("FundSteward")) {
			// TODO: implement this method for FundStewards!!
			System.out.println("checkLoginDetails has not yet been implemented for FundStewards");
		} else if (userType.equals("Staff")) {
			// TODO: implement this method for Staffs!!
			System.out.println("checkLoginDetails has not yet been implemented for Staffs");
		} else {
			System.out.println("Invalid user type in checkLoginDetails() - returning 3.");
			return 3;
		}

		// should only reach here if there was a valid userType and we entered
		// one of the if or else-if statements (not the else because we would have
		// returned early)

		// had a valid userType but never found the username and thus didn't return
		// early
		// return 1 since the user was not found
		return 1;
	}

	// security question answers are case **insensitive
	/**
	 * 
	 * @param questionNum should be 1, 2, or 3 just like in
	 *                    Profile.getOneSecurityQuestion()
	 * @param user        the user object who is trying to log in
	 * @return if the answer to the security question was correct or not
	 *         (capitalization doesn't matter)
	 */
	public boolean checkOneSecurityQuestion(int questionNum, Profile user) {
		Scanner scnr = new Scanner(System.in);

		String questionText = user.getOneSecurityQuestion(questionNum);
		String correctAnswer = user.getOneSecurityQAnswer(questionNum);

		System.out.println("Please answer this security question (capitalization doesn't matter): " + questionText);
		System.out.print("Your answer: ");

		String userAnswer = scnr.nextLine();

		scnr.close();

		if (userAnswer.equalsIgnoreCase(correctAnswer)) {
			System.out.println("Correct answer!");
			return true;
		} else {
			System.out.println("Incorrect answer");
			return false;
		}
	}

	public double stringSimilarity(String str1, String str2) {
		String longer = str1, shorter = str2;

		if (str1.length() < str2.length()) { // longer should always have greater length
			longer = str2;
			shorter = str1;
		}

		int longerLength = longer.length();
		if (longerLength == 0) {
			return 1.0;
		}

		String s1 = longer;
		String s2 = shorter;

		s1 = s1.toLowerCase();
		s2 = s2.toLowerCase();

		int[] costs = new int[s2.length() + 1];

		for (int i = 0; i <= s1.length(); i++) {
			int lastValue = i;

			for (int j = 0; j <= s2.length(); j++) {
				if (i == 0)
					costs[j] = j;
				else {
					if (j > 0) {
						int newValue = costs[j - 1];

						if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
							newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
						}

						costs[j - 1] = lastValue;
						lastValue = newValue;
					}
				}
			}

			if (i > 0) {
				costs[s2.length()] = lastValue;
			}
		}

		return (longerLength - costs[s2.length()]) / (double) longerLength;

	}

	// A getter that I added so I could check some details of scholarships in a test method in Main
	public ArrayList<Scholarship> getAllScholarships() {
		return this.allScholarships;
	}

	public ArrayList<StudentProfile> getAllStudents() {
		return allStudents;
	}

	public ArrayList<MatchRelationship> getAllMatchRelationships() {
		return allMatchRelationships;
	}

	public ArrayList<DonorProfile> getAllDonors() {
		return allDonors;
	}

	public ArrayList<AdminProfile> getAllAdmins() {
		return allAdmins;
	}

	public Profile getCurrentUser() {
		return currentUser;
	}


	public void testStoringStudents() throws Exception {
		StudentProfile newStudent = new StudentProfile("Jess", "Mess", 12345, "user", "pass", "IE", true, "SFWEE", true, 
		3.6, true, true, "Freshman", 5, 2025, "Female", true, false, 12, false, "I love school!");

		storeNewStudentProfile(newStudent);

		System.out.print(newStudent.toString());
	}
}
