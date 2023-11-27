import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class BackendSystem {

	private ArrayList<StudentProfile> allStudents = new ArrayList<StudentProfile>();
	private ArrayList<Scholarship> allScholarships = new ArrayList<Scholarship>();
	private ArrayList<MatchRelationship> allMatchRelationships = new ArrayList<MatchRelationship>();
	private ArrayList<DonorProfile> allDonors = new ArrayList<DonorProfile>();
	private ArrayList<AdminProfile> allAdmins = new ArrayList<AdminProfile>();
	private ArrayList<StaffProfile> allStaff = new ArrayList<StaffProfile>();
	private ArrayList<FundStewardProfile> allFundStewards = new ArrayList<FundStewardProfile>();

	// the currently logged-in user, there can only be one user logged in at a time
	// with our current setup
	// Profile is an abstract class but all the other types of profiles inherit from it
	// so this can work nicely with some occasional typecasting, when we e.g. need
	// currentUser to be of type StudentProfile for a method call
	private Profile currentUser;

	public Profile getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(Profile newUser) {
		this.currentUser = newUser;
	}
	
	// constructor
	public BackendSystem() throws NumberFormatException, IOException {
		// have to be careful about the order of these, because they rely on each other
		// existing already
		// scholarships relies on students and donors already existing
		this.allScholarships = InstantiateAllScholarships();

		// students relies on scholarships already existing
		this.allStudents = InstantiateAllStudents();
		// donors relies on scholarships already existing
		this.allDonors = InstantiateAllDonors();
		
		// match relies on students and scholarships
		this.allMatchRelationships = InstantiateAllMatches();
		this.allAdmins = instantiateAllAdmins();
		this.allStaff = InstantiateAllStaff();
		this.allFundStewards = instantiateAllFundStewards();

		// connect scholarships (which has just strings for students
		// and donors), and connect it with actual student and donor objects
		setScholarshipObjects();

		// now that we have the full list of scholarships, archive past ones that are
		// past due, and delete 5+ year old ones

		archivePastDueScholarships();
		deleteScholsDue5PlusYrsAgo();
	}

	/**
	 * calls get<profiletype>FromInput() to make a new object of the selected user type,
	 * sets that to the currentUser, prints the profile details, adds it to the list of users of that type,
	 * and adds it to a new fine
	 * 
	 * @throws Exception
	 */
	public void createNewProfile() throws Exception {
		String userType;
		boolean success = false;
		do {
			Main.scnr.nextLine();
			System.out.println("Please enter your user type. (Enter as one word, i.e. student, fundsteward, etc.)");
			userType = Main.scnr.nextLine();

			/*
			 * if (!(userType.equalsIgnoreCase("student") ||
			 * userType.equalsIgnoreCase("admin")
			 * || userType.equalsIgnoreCase("staff")
			 * || userType.equalsIgnoreCase("donor") ||
			 * userType.equalsIgnoreCase("fundsteward"))) {
			 * System.out.println(
			 * "That user type was not recognized. Accepted user types are: "
			 * +
			 * "student, admin, staff, donor, and fundsteward (capitalization doesn't matter).\n"
			 * );
			 * continue;
			 * }
			 */

			if (userType.equalsIgnoreCase("student")) {
				success = true;
				StudentProfile newStudent = getStudentFromInput();
				setCurrentUser(newStudent);
				System.out.println(((StudentProfile) getCurrentUser()).toString());
				// add this new profile to the list here in backend
				allStudents.add(newStudent);
				// store this new profile in a file
				storeNewStudentProfile(newStudent);
				// System.out.println("Made new folder and files for that student, the folder
				// name should be /students/student" + (findNextFileIndex("student") -
				// 1));
				// System.out.println("!!!! Please be sure to delete that folder so we don't
				// have duplicates.");
			} 
			else if (userType.equalsIgnoreCase("admin")) {
				success = true;
				AdminProfile newAdmin = getAdminFromInput();
				setCurrentUser(newAdmin);
				System.out.println(((AdminProfile) getCurrentUser()).toString());
				// add this new profile to the list here in backend
				allAdmins.add(newAdmin);
				// store this new profile in a file
				storeNewAdminProfileFile(newAdmin);
				// System.out.println("Made new folder and files for that administrator, the
				// folder name should be /administrators/admin" +
				// (findNextFileIndex("admin") - 1));
				// System.out.println("!!!! Please be sure to delete that folder so we don't
				// have duplicates.");
			} 
			else if (userType.equalsIgnoreCase("staff")) {
				success = true;
				StaffProfile newStaff = getStaffFromInput();
				setCurrentUser(newStaff);
				System.out.println(((StaffProfile) getCurrentUser()).toString());
				// add this new profile to the list here in backend
				allStaff.add(newStaff);
				// store this new profile in a file
				storeNewStaffProfile(newStaff);
				// System.out.println("Made new folder and files for that staff, the folder name
				// should be /engr staff/staff" + (findNextFileIndex("staff") - 1));
				// System.out.println("!!!! Please be sure to delete that folder so we don't
				// have duplicates.");
			} 
			else if (userType.equalsIgnoreCase("fundsteward")) {
				success = true;
				FundStewardProfile newFundSteward = getFundStewardFromInput();
				setCurrentUser(newFundSteward);
				System.out.println(((FundStewardProfile) getCurrentUser()).toString());
				// add this new profile to the list here in backend
				allFundStewards.add(newFundSteward);
				// store this new profile in a file
				storeNewFundStewardProfile(newFundSteward);
				// System.out.println("Made new folder and files for that fundsteward, the
				// folder name should be /fundstewards/fundsteward" +
				// (findNextFileIndex("fundsteward") - 1));
				// System.out.println("!!!! Please be sure to delete that folder so we don't
				// have duplicates.");
			} 
			else if (userType.equalsIgnoreCase("donor")) {
				success = true;
				DonorProfile newDonor = getDonorFromInput();
				setCurrentUser(newDonor);
				System.out.println(
						"Here are the details you entered: " + ((DonorProfile) getCurrentUser()).toString());
				// add this new profile to the list here in backend
				allDonors.add(newDonor);
				// store this new profile in a file
				storeNewDonorProfile(newDonor);
				// System.out.println("Made new folder and files for that donor, the folder name
				// should be /donors/donor" + (findNextFileIndex("donor") - 1));
				// System.out.println("!!!! Please be sure to delete that folder so we don't
				// have duplicates.");
			} 
			else {
				System.out.println("That user type was not recognized, please try again.");
			}

			// if the user successfully created an account, print this success message
			if (success) {
				System.out.println("New user profile created! Now please login using this account.");
			}
			// success is false by default, but is set to true if the user type is **valid
		} while (!success);
	}

	// reads a StudentProfile from text files, makes and returns a StudentProfile object
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

		for (String name : values) {
			for (Scholarship scholarship : this.allScholarships) {
				if (name.compareTo(scholarship.getName()) == 0) {
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

		for (Scholarship scholarship : student.getAwardsReceived()) {
			awardNames.add(scholarship.getName());
		}

		awardsW.write(String.join("\n", awardNames));

		awardsW.close();

	}

	/**
	 * 
	 * @param student the newly created StudentProfile object to store to a new set of files
	 * @throws Exception
	 */
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

		for (Scholarship scholarship : student.getAwardsReceived()) {
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
		} else {
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
		// this is an empty donor object, this will get overwritten by the
		// actual correct Donor object after we create the DonorProfile objects
		DonorProfile correctDonor = new DonorProfile();

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

		return new Scholarship(name, description, donorName, awardAmount, requirements, application, applicantNames,
				isApproved, isArchived, fileIndex, dateAddedString, dateDueString);

	}

	public void setScholarshipObjects() {
		ArrayList<StudentProfile> applicants = new ArrayList<StudentProfile>();
		DonorProfile correctDonor = new DonorProfile();

		for (Scholarship schol : this.allScholarships) {
			// find student objects
			for (String applicantName : schol.getApplicantNames()) {
				for (StudentProfile student : this.allStudents) {
					if (applicantName.equals(student.getName())) {
						applicants.add(student);
					}
				}
			}

			// find donor object
			for (DonorProfile donor : this.allDonors) {
				if (schol.getDonorName().equals(donor.getName())) {
					correctDonor = donor;
				}
			}

			// setting the objects in each scholarship object
			schol.setApplicants(applicants);
			schol.setDonor(correctDonor);
		}

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

	/**
	 * uses Scholarship.isPastDue() to check if scholarships were due yesterday or
	 * before (the due date has passed)
	 * sets scholarships that were due in the past, to archived
	 * 
	 * @return - the number of scholarships that we set to archived. Don't have to
	 *         do anything with this value,
	 *         I just decided to add it
	 * @throws IOException
	 */
	public int archivePastDueScholarships() throws IOException {
		int numSetToArchived = 0;

		for (int i = 0; i < allScholarships.size(); i++) {
			// if the due date has passed, set it to archived
			if (allScholarships.get(i).isPastDue() && !allScholarships.get(i).getIsArchived()) {
				allScholarships.get(i).setArchived(true);
				numSetToArchived++;
				updateScholarshipFile(allScholarships.get(i));
			}
		}

		return numSetToArchived;
	}

	// TODO: should this delete the files for the old scholarships??
	/**
	 * Deletes the objects for scholarships in allScholarships
	 * that were due yesterday 5 years ago, or longer
	 * **Does not delete the files!
	 * 
	 * @return the number of scholarships deleted, not sure if we'll need this value
	 *         but it can't hurt to have
	 */
	public int deleteScholsDue5PlusYrsAgo() {
		int numDeleted = 0;

		// have to use an iterator because Java won't let you iterate through
		// and delete at the same time with a loop
		Iterator<Scholarship> itr = allScholarships.iterator();

		// loop through allScholarships
		while (itr.hasNext()) {
			// if the current scholarship's dateDue was 5 years ago or longer, delete it
			if (itr.next().due5PlusYearsAgo()) {
				itr.remove();
				numDeleted++;
			}
		}

		return numDeleted;
	}

	/**
	 * Stores a newly created Scholarship object to a new set of text files
	 * 
	 * @param scholarship the newly created scholarship object
	 * @throws IOException
	 */
	public void storeNewScholarship(Scholarship scholarship) throws IOException {
		int nextFileIndex = findNextFileIndex("scholarship");
		File folder = new File("scholarships/scholarship" + String.valueOf(nextFileIndex));
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

	/**
	 * update the files for an existing Scholarship object
	 * 
	 * @param scholarship
	 * @throws IOException
	 */
	public void updateScholarshipFile(Scholarship scholarship) throws IOException {
		int fileIndex = scholarship.getFileIndex();
		File folder = new File("scholarships/scholarship" + String.valueOf(fileIndex));

		File detailsFile = new File(folder, "details.txt");
		FileWriter detailsWriter = new FileWriter(detailsFile, false);

		detailsWriter.write(scholarship.getDetailsFileText());
		detailsWriter.close();

		File applicationFile = new File(folder, "application.txt");
		FileWriter applicationWriter = new FileWriter(applicationFile, false);

		applicationWriter.write(scholarship.getApplicationFileText());
		applicationWriter.close();

		File applicantsFile = new File(folder, "applicants.txt");
		FileWriter applicantsWriter = new FileWriter(applicantsFile, false);

		applicantsWriter.write(scholarship.getApplicantsFileText());
		applicantsWriter.close();

		File requirementsFile = new File(folder, "requirements.txt");
		FileWriter requirementsWriter = new FileWriter(requirementsFile, false);

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
		// starting value is 1
		int fileIndex = 1;
		for (File child : directoryListing) {

			try {
				donor = readDonor(fileIndex);
				donors.add(donor);
			} catch (IOException except) {
				System.out.println("File not found: " + child.getAbsolutePath());
			}
			// increment fileIndex to go from donor1 to donor2, etc.
			fileIndex++;
		}

		return donors;
	}

	public void storeNewDonorProfile(DonorProfile donor) throws Exception {
		int nextFileIndex = findNextFileIndex("donor");
		String folderPath = "donors/donor" + String.valueOf(nextFileIndex);
		File dir = new File(folderPath);
		dir.mkdir();

		// ********* copy-pasted from the update donor method

		// for details.txt
		File detailsFile = new File(folderPath + "/details.txt");
		FileWriter detailsWriter = new FileWriter(detailsFile, false);

		// get the text that should be written to the details.txt file, and write it
		detailsWriter.write(donor.getDetailsFileText());
		detailsWriter.close();

		// for scholarships.txt
		File scholsFile = new File(folderPath + "/scholarships.txt");
		FileWriter scholsWriter = new FileWriter(scholsFile, false);

		// get the text that should be written to the scholarships.txt file, and write
		// it
		scholsWriter.write(donor.getScholarshipFileText());
		scholsWriter.close();
	}

	// writes donor profile data to file
	// TODO: finish updating this for donors
	public void updateDonorProfileFile(DonorProfile donor) throws IOException {
		String folderPath = "donors/donor" + String.valueOf(donor.getFileIndex());

		// for details.txt
		File detailsFile = new File(folderPath + "/details.txt");
		FileWriter detailsWriter = new FileWriter(detailsFile, false);

		// get the text that should be written to the details.txt file, and write it
		detailsWriter.write(donor.getDetailsFileText());
		detailsWriter.close();

		// for scholarships.txt
		File scholsFile = new File(folderPath + "/scholarships.txt");
		FileWriter scholsWriter = new FileWriter(scholsFile, false);

		// get the text that should be written to the scholarships.txt file, and write
		// it
		scholsWriter.write(donor.getScholarshipFileText());
		scholsWriter.close();
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
		Boolean isActive = Boolean.parseBoolean(detailsBr.readLine());

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
				applicationStatus, isActive);
	}

	public MatchRelationship produceNewMatch(StudentProfile student, Scholarship scholarship) throws IOException {
		Random rand = new Random();

		float matchIndex = (float) (rand.nextInt(10) + rand.nextInt(10) / 10.0);
		float matchPercentage = (float) rand.nextInt(101);
		int fileIndex = findNextFileIndex("match");

		MatchRelationship newMatch = new MatchRelationship(student, scholarship, matchPercentage, matchIndex,
				fileIndex);

		storeNewMatch(newMatch);
		this.allMatchRelationships.add(newMatch);
		return newMatch;
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

	public void storeNewMatch(MatchRelationship match) throws IOException {
		int nextFileIndex = findNextFileIndex("match");
		File folder = new File("matches/match" + String.valueOf(nextFileIndex));
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

	public void updateMatchFile(MatchRelationship match) throws IOException {

		File folder = new File("matches/match" + String.valueOf(match.getID()));

		File detailsFile = new File(folder, "details.txt");
		FileWriter detailsWriter = new FileWriter(detailsFile, false);

		detailsWriter.write(match.getDetailsFileText());
		detailsWriter.close();

		File applicationFile = new File(folder, "application.txt");
		FileWriter applicationWriter = new FileWriter(applicationFile, false);

		applicationWriter.write(match.getApplicationFileText());
		applicationWriter.close();
	}

	// creates donor object from file
	public DonorProfile readDonor(int fileIndex) throws IOException {
		String folderPath = "donors/donor" + String.valueOf(fileIndex);
		// for details.txt
		BufferedReader detailsBr = new BufferedReader(new FileReader(folderPath + "/details.txt"));
		ArrayList<String> detailsValues = new ArrayList<String>();

		// for scholarships.txt
		BufferedReader scholsBr = new BufferedReader(new FileReader(folderPath + "/scholarships.txt"));
		ArrayList<String> scholsValues = new ArrayList<String>();
		String str;

		// read details file and store in variables
		while ((str = detailsBr.readLine()) != null) {
			detailsValues.add(str);
		}

		String firstName = detailsValues.get(0);
		String lastName = detailsValues.get(1);
		String username = detailsValues.get(2);
		String password = detailsValues.get(3);
		String securityQAnswer1 = detailsValues.get(4);
		String securityQAnswer2 = detailsValues.get(5);
		String securityQAnswer3 = detailsValues.get(6);
		// the list of actual scholarship objects that we find based on the scholarship
		// names
		ArrayList<Scholarship> scholarshipsDonated = new ArrayList<Scholarship>();
		detailsBr.close();

		// read scholarships file and store in variables
		while ((str = scholsBr.readLine()) != null) {
			scholsValues.add(str);
		}

		// loop through scholsValues, the list of String names of scholarships that we
		// just read from donorX/scholarships.txt
		for (int scholsValuesI = 0; scholsValuesI < scholsValues.size(); scholsValuesI++) {
			String donorScholName = scholsValues.get(scholsValuesI);

			// find the scholarship object with that name in the list of all scholarships,
			// and connect this new donor with that actual object
			for (int allScholsI = 0; allScholsI < allScholarships.size(); allScholsI++) {
				String allScholName = this.allScholarships.get(allScholsI).getName();

				if (donorScholName.compareTo(allScholName) == 0) {
					scholarshipsDonated.add(allScholarships.get(allScholsI));
					// break out of the inner loop, looping through the scholarships because we
					// found the scholarship
					break;
				}
			}
		}

		return new DonorProfile(firstName, lastName, username, password, securityQAnswer1, securityQAnswer2,
				securityQAnswer3, scholarshipsDonated, fileIndex);
	}

	// instantiate all admins
	public ArrayList<AdminProfile> instantiateAllAdmins() {
		ArrayList<AdminProfile> scholarships = new ArrayList<AdminProfile>();
		// open the 'administrators' folder
		File dir = new File("administrators");
		// all the files/folders in the administrators folder
		File[] directoryListing = dir.listFiles();
		AdminProfile admin;
		int fileIndex = 1;

		// loop through the list of files/folders that are in the 'administrators'
		// folder
		// each child is for one scholarship
		for (File child : directoryListing) {

			try {
				admin = readAdminProfile(fileIndex);
				scholarships.add(admin);
			} catch (IOException except) {
				System.out.println("File not found in instantiateAllAdmins(): " + child.getAbsolutePath());
			}

			fileIndex++;
		}

		return scholarships;
	}

	public AdminProfile readAdminProfile(int fileIndex) throws IOException {
		String filePath = "administrators/admin" + String.valueOf(fileIndex) + ".txt";
		BufferedReader detailsBr = new BufferedReader(new FileReader(filePath));

		ArrayList<String> values = new ArrayList<String>();
		String str;

		while ((str = detailsBr.readLine()) != null) {
			values.add(str);
		}

		detailsBr.close();

		String firstName = values.get(0);
		String lastName = values.get(1);
		String username = values.get(2);
		String password = values.get(3);
		String sq1 = values.get(4);
		String sq2 = values.get(5);
		String sq3 = values.get(6);

		// Create and return new AdminProfile
		return new AdminProfile(firstName, lastName, username, password, sq1, sq2, sq3, fileIndex);
	}

	public void storeNewAdminProfileFile(AdminProfile admin) throws Exception {
		int nextFileIndex = findNextFileIndex("admin");
		String folderPath = "administrators/admin" + String.valueOf(nextFileIndex);
		File dir = new File(folderPath);
		dir.mkdir();

		// for admin.txt
		File detailsFile = new File(folderPath + ".txt");
		FileWriter detailsWriter = new FileWriter(detailsFile, false);

		// get the text that should be written to the adminx.txt file, and writes it
		detailsWriter.write(admin.getDetailsFileText());
		detailsWriter.close();
	}

	// writes admin profile data to file
	public void updateAdminProfileFile(AdminProfile admin) throws IOException {
		String folderPath = "administrators/admin" + String.valueOf(admin.getFileIndex());

		File detailsF = new File(folderPath + ".txt");
		FileWriter detailsW = new FileWriter(detailsF, false);

		detailsW.write(admin.getDetailsFileText());

		detailsW.close();
	}

	public StaffProfile readStaffProfile(int fileIndex) throws IOException {
		String folderPath = "staff/staff" + String.valueOf(fileIndex) + ".txt";
		BufferedReader detailsBr = new BufferedReader(new FileReader(folderPath));

		ArrayList<String> values = new ArrayList<String>();
		String str;

		while ((str = detailsBr.readLine()) != null) {
			values.add(str);
		}

		detailsBr.close();

		String firstName = values.get(0);
		String lastName = values.get(1);
		String username = values.get(2);
		String password = values.get(3);
		String jobRole = values.get(4);
		String sq1 = values.get(5);
		String sq2 = values.get(6);
		String sq3 = values.get(7);

		return new StaffProfile(firstName, lastName, username, password, jobRole, sq1, sq2, sq3, fileIndex);
	}

	// Instantiates all staff
	public ArrayList<StaffProfile> InstantiateAllStaff() {
		ArrayList<StaffProfile> staffList = new ArrayList<StaffProfile>();
		// open the 'staff' folder
		File dir = new File("staff");
		// all the files/folders in the staff folder
		File[] directoryListing = dir.listFiles();
		StaffProfile staff;
		int fileIndex = 1;

		// loop through the list of files/folders that are in the 'staff'
		// folder
		// each child is for one staff
		for (File child : directoryListing) {

			try {
				staff = readStaffProfile(fileIndex);
				staffList.add(staff);
			} catch (IOException except) {
				System.out.println("File not found in instantiateAllStaff(): " + child.getAbsolutePath());
			}

			fileIndex++;
		}

		return staffList;
	}

	public void storeNewStaffProfile(StaffProfile staff) throws Exception {
		int nextFileIndex = findNextFileIndex("staff");
		String folderPath = "staff/staff" + String.valueOf(nextFileIndex);
		File dir = new File(folderPath);
		dir.mkdir();

		// for staff.txt
		File detailsFile = new File(folderPath + ".txt");
		FileWriter detailsWriter = new FileWriter(detailsFile, false);

		// get the text that should be written to the staffX.txt file, and writes it
		detailsWriter.write(staff.getDetailsFileText());
		detailsWriter.close();
	}

	// writes staff profile data to file
	public void updateStaffProfileFile(StaffProfile staff) throws IOException {
		String folderPath = "staff/staff" + String.valueOf(staff.getFileIndex());

		File detailsF = new File(folderPath + ".txt");
		FileWriter detailsW = new FileWriter(detailsF, false);

		detailsW.write(staff.getDetailsFileText());

		detailsW.close();
	}

	public FundStewardProfile readFundStewardProfile(int fileIndex) throws IOException {
		String folderPath = "fundstewards/fundsteward" + String.valueOf(fileIndex) + ".txt";
		BufferedReader detailsBr = new BufferedReader(new FileReader(folderPath));

		ArrayList<String> values = new ArrayList<String>();
		String str;

		while ((str = detailsBr.readLine()) != null) {
			values.add(str);
		}

		detailsBr.close();

		String firstName = values.get(0);
		String lastName = values.get(1);
		String username = values.get(2);
		String password = values.get(3);
		String sq1 = values.get(4);
		String sq2 = values.get(5);
		String sq3 = values.get(6);

		return new FundStewardProfile(firstName, lastName, username, password, sq1, sq2, sq3, fileIndex);
	}

	// Instantiates all fund stewards
	public ArrayList<FundStewardProfile> instantiateAllFundStewards() {
		ArrayList<FundStewardProfile> fundStewards = new ArrayList<FundStewardProfile>();
		// open the 'fundstewards' folder
		File dir = new File("fundstewards");
		// all the files/folders in the fundsteward folder
		File[] directoryListing = dir.listFiles();
		FundStewardProfile fundSteward;
		int fileIndex = 1;

		// loop through the list of files/folders that are in the 'fundSteward'
		// folder
		// each child is for one fund steward
		for (File child : directoryListing) {

			try {
				fundSteward = readFundStewardProfile(fileIndex);
				fundStewards.add(fundSteward);
			} catch (IOException except) {
				System.out.println("File not found in instantiateAllFundStewards(): " + child.getAbsolutePath());
			}

			fileIndex++;
		}

		return fundStewards;
	}

	public void storeNewFundStewardProfile(FundStewardProfile fundsteward) throws Exception {
		int nextFileIndex = findNextFileIndex("fundsteward");
		String folderPath = "fundstewards/fundsteward" + String.valueOf(nextFileIndex);
		File dir = new File(folderPath);
		dir.mkdir();

		// for fundstewards.txt
		File detailsFile = new File(folderPath + ".txt");
		FileWriter detailsWriter = new FileWriter(detailsFile, false);

		// get the text that should be written to the fundstewardX.txt file, and writes
		// it
		detailsWriter.write(fundsteward.getDetailsFileText());
		detailsWriter.close();
	}

	// writes fund steward profile data to file
	public void updateFundStewardProfileFile(FundStewardProfile fundsteward) throws IOException {
		String folderPath = "fundstewards/fundsteward" + String.valueOf(fundsteward.getFileIndex());

		File detailsF = new File(folderPath + ".txt");
		FileWriter detailsW = new FileWriter(detailsF, false);

		detailsW.write(fundsteward.getDetailsFileText());

		detailsW.close();
	}

	/**
	 * IMPORTANT: at least one parameter for archived and one for approved need to be true, otherwise you will get nothing!
	 * 
	 * Ex: to get only scholarships that are archived (approved or not), do includeArchived = true,
	 * includeNotArchived = false, and the other 2 true
	 * 
	 * to get only scholarships that are not archived and are approved, do includeArchived = false, includeNotArchived=true
	 * includeApproved = true, includeNotApproved = false
	 * 
	 * @param detailedInfo      whether or not you want to print detailed
	 *                          information about the scholarship, or something more
	 *                          basic
	 *                          Either calls printOneScholarshipBasic() or
	 *                          printOneScholarshipDetailed depending on your choice
	 * 
	 * @param includeArchived   whether or not to print scholarships that are marked
	 *                          as isArchived = true
	 * @param includeNotArchived   whether or not to print scholarships that are marked
	 *                          as isArchived = **false
	 * 
	 * @param includeApproved   whether or not to print scholarships that are marked
	 *                          as isApproved = **true
	 * @param includeNotApproved whether or not to print scholarships that are marked
	 *                          as isApproved = **false
	 *                          This param is so we can print only unapproved
	 *                          scholarships for an admin to approve
	 */
	public void printAllScholarships(boolean detailedInfo, boolean includeArchived, boolean includeNotArchived, boolean includeApproved,
			boolean includeNotApproved) {
		for (Scholarship scholarship : allScholarships) {
			// if the scholarship is either archived and we want to include archived ones,
			// or it's not archived and we want to include non-archived scholarships,
			// *and it's either approved and we want to include approved scholarships, or
			// it's not approved and we want to include unapproved scholarships
			if ((scholarship.getIsArchived() && includeArchived || !scholarship.getIsArchived() && includeNotArchived) &&
            (scholarship.getIsApproved() && includeApproved || !scholarship.getIsApproved() && includeNotApproved)) {
				if (detailedInfo) {
					System.out.print(scholarship.getAllInfoString());
				} else {
					System.out.print(scholarship.getBasicInfoString());
				}

				System.out.println();
				System.out.println();
			}
		}
	}

	public void printArchivedScholarships(boolean detailedInfo, boolean includeArchived, boolean includeApproved,
			boolean includeUnapproved) {
		for (Scholarship scholarship : allScholarships) {
			if((scholarship.getIsArchived() && includeArchived)){
				if (detailedInfo) {
					System.out.print(scholarship.getAllInfoString());
					System.out.println();
					System.out.print(scholarship.getRecipient());
				} else {
					System.out.print(scholarship.getBasicInfoString());
				}
				System.out.println();
				System.out.println();
			}
		}
	}

	// searches a folder for a scholarship with inputted value
	public ArrayList<Scholarship> searchScholarships(int inputCategory, String inputSearchValue) {
		ArrayList<Scholarship> scholarshipsFound = new ArrayList<Scholarship>();
		HashMap<String, ArrayList<String>> requirements = new HashMap<String, ArrayList<String>>();
		ArrayList<Scholarship> scholarshipsToSearch = getScholarshipsAvailableToStudents();
		LocalDate inputDate;

		if (inputCategory == 1) {

			double percentage;
			double max1 = 0.0;
			double max2 = 0.0;
			double max3 = 0.0;
			Scholarship schol1 = new Scholarship();
			Scholarship schol2 = new Scholarship();
			Scholarship schol3 = new Scholarship();

			for (Scholarship scholarship : scholarshipsToSearch) {
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

		} else if (inputCategory == 2) {
			// retrieves donor name from each scholarship
			for (Scholarship scholarship : scholarshipsToSearch) {
				if (inputSearchValue.compareTo(scholarship.getDonorName()) == 0) {
					scholarshipsFound.add(scholarship);
				}
			}
		} else if (inputCategory == 19) {
			// retrieves array of applicant names from each scholarship and iterates through
			// them
			ArrayList<String> applicantNames = new ArrayList<String>();

			for (Scholarship scholarship : scholarshipsToSearch) {
				applicantNames = scholarship.getApplicantNames();

				for (String name : applicantNames) {
					if (inputSearchValue.compareTo(name) == 0) {
						scholarshipsFound.add(scholarship);
					}
				}
				applicantNames.clear();
			}
		} else if (inputCategory == 3) {

			for (Scholarship scholarship : scholarshipsToSearch) {
				inputDate = LocalDate.parse(inputSearchValue);
				if (scholarship.getDateDue().compareTo(inputDate) == 0) {
					scholarshipsFound.add(scholarship);
				}
			}

		} else if (inputCategory == 4) {

			for (Scholarship scholarship : scholarshipsToSearch) {
				inputDate = LocalDate.parse(inputSearchValue);
				if (scholarship.getDateAdded().compareTo(inputDate) == 0) {
					scholarshipsFound.add(scholarship);
				}
			}

		} else if (inputCategory == 5) {

			for (Scholarship scholarship : scholarshipsToSearch) {
				if (Float.compare(scholarship.getAwardAmount(), Float.parseFloat(inputSearchValue)) >= 0) {
					scholarshipsFound.add(scholarship);
				}
			}
		} else if (inputCategory == 9) {
			for (Scholarship scholarship : scholarshipsToSearch) {
				
				requirements = scholarship.getRequirements();

				for (Map.Entry<String, ArrayList<String>> entry : requirements.entrySet()) {
					if (entry.getKey().equalsIgnoreCase("GPA")) {
						if (Float.valueOf(entry.getValue().get(1)).compareTo(Float.valueOf(inputSearchValue)) <= 0){
							scholarshipsFound.add(scholarship);
						}

					}

				}
			}

		}else {
			// assumes any search category will be a requirement
			// retrieves requirement hashmap from scholarship and compares category and
			// value

			String inputCategoryName;

			switch (inputCategory) {
				case 6:
					inputCategoryName = "major";
					break;
				case 7:
					inputCategoryName = "minor";
					break;
				case 8:
					inputCategoryName = "US Citizen";
					break;
				case 10:
					inputCategoryName = "Good Standing";
					break;
				case 11:
					inputCategoryName = "hasAdvStanding";
					break;
				case 12:
					inputCategoryName = "grade Level";
					break;
				case 13:
					inputCategoryName = "graduation Year";
					break;
				case 14:
					inputCategoryName = "gender";
					break;
				case 15:
					inputCategoryName = "Full Time Student";
					break;
				case 16:
					inputCategoryName = "Transfer Student";
					break;
				case 17:
					inputCategoryName = "curNumCredits";
					break;
				case 18:
					inputCategoryName = "currently receives Funding";
					break;
				default:
					inputCategoryName = "";
					break;
			}


			for (Scholarship scholarship : scholarshipsToSearch) {
				
				requirements = scholarship.getRequirements();

				for (Map.Entry<String, ArrayList<String>> entry : requirements.entrySet()) {
					if (entry.getKey().equalsIgnoreCase(inputCategoryName)) {
						for (int i = 0; i < entry.getValue().size(); i++) {
							if (entry.getValue().get(i).equalsIgnoreCase(inputSearchValue)) {
								scholarshipsFound.add(scholarship);
							}
						}

					}

				}
			}
		}

		return scholarshipsFound;
	}

	public ArrayList<StudentProfile> searchStudents(int inputCategory, String inputSearchValue) {
		ArrayList<StudentProfile> studentsFound = new ArrayList<StudentProfile>();

		if (inputCategory == 1) {

			for (StudentProfile student : this.allStudents) {
				if (student.getName().equalsIgnoreCase(inputSearchValue)) {
					studentsFound.add(student);
				}
			}

		} else if (inputCategory == 2) {

			for (StudentProfile student : this.allStudents) {
				if (student.getGradeLevel().equalsIgnoreCase(inputSearchValue)) {
					studentsFound.add(student);
				}
			}

		} else if (inputCategory == 3) {

			for (StudentProfile student : this.allStudents) {

				if (student.getMajor().equalsIgnoreCase(inputSearchValue)) {
					studentsFound.add(student);
				}
			}

		} else if (inputCategory == 4) {

			for (StudentProfile student : this.allStudents) {

				if (student.getMinor().equalsIgnoreCase(inputSearchValue)) {
					studentsFound.add(student);
				}
			}

		} else if (inputCategory == 5) {

			for (StudentProfile student : this.allStudents) {
				if (Double.compare(student.getGPA(), Double.parseDouble(inputSearchValue)) >= 0) {
					studentsFound.add(student);
				}
			}

		}

		return studentsFound;
	}

	/**
	 * Prompts the user for their login details and then security questions
	 * If this function ends, either the program should end or the user is fully
	 * successfully logged in
	 * 
	 * from our discussion/from the elicitation:
	 * get 3 max wrong password attempts then get a message to contact admin,
	 * program stops
	 * after 2 wrong passwords get to still do 3 security question attempts
	 * 
	 * @return returns true for a successful login, false if the login was
	 *         unsuccessful
	 *         // and the program should stop!!
	 */
	public boolean login() {
		int returnVal = -1;
		// the number of times the user gets a wrong password
		int failedPWAttempts = 0;

		// these are up here so we can set the value in the if-statement within the loop
		// below, and reference the variable outside
		// the if-statement. (If we declared them in the if, it wouldn't work).
		// but also we don't want to reset the value to an empty string at the start of
		// each iteration of the loop, only the first
		String userType = "";
		String username = "";

		do {
			// only ask them for their user type and password if they're trying to log in
			// the first time, after that just ask them
			// for their password
			if (failedPWAttempts == 0) {
				// Main.scnr.nextLine();
				System.out.print("Please enter your user type (as one word, i.e. 'student', 'admin', 'fundsteward'): ");
				Main.scnr.nextLine();
				userType = Main.scnr.nextLine();

				// check for valid user type
				// if it's not a valid user type, print a message and go to the next iteration
				// of this do-while loop
				if (!(userType.equalsIgnoreCase("student") || userType.equalsIgnoreCase("admin")
						|| userType.equalsIgnoreCase("staff")
						|| userType.equalsIgnoreCase("donor") || userType.equalsIgnoreCase("fundsteward"))) {
					System.out.println(
							"That user type was not recognized. Accepted user types are: "
									+ "student, admin, staff, donor, and fundsteward (capitalization doesn't matter).\n");
					continue;
				}

				System.out.print("Please enter your username: ");

				username = Main.scnr.nextLine();
			}

			System.out.print("Please enter your password: ");

			String password = Main.scnr.nextLine();

			// possible return values from checkLoginDetails():
			// 0 if the username and password matched,
			// 1 if the password was wrong and they loose an attempt
			// 2 if a wrong value was entered but they dont loose an attempt
			returnVal = checkLoginDetails(userType, username, password);

			if (returnVal == 0) {
				return true;
			} else if (returnVal == 1) {
				failedPWAttempts++;

				// if they have now gotten their password wrong 3 times, return false
				// for an unsuccessful login, and the program should quit
				if (failedPWAttempts == 3) {
					return false;
				}

				System.out.println("Trying again. " + String.valueOf(3 - failedPWAttempts) + " attempt/s left.");
			} else {
				System.out.println("Trying again.");
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
		boolean typeValid = false;

		if (userType.equalsIgnoreCase("Student")) {
			typeValid = true;
			for (int i = 0; i < allStudents.size(); i++) {
				if (allStudents.get(i).username.equalsIgnoreCase(enteredUsername)) {
					if (allStudents.get(i).password.equals(enteredPassword)) {
						// store this user in the currentUser variable
						// studentUser = allStudents.get(i);
						currentUser = allStudents.get(i);
						for (int questionNum = 1; questionNum <= 3; questionNum++) {

							// String questionText = studentUser.getOneSecurityQuestion(questionNum);
							// String correctAnswer = studentUser.getOneSecurityQAnswer(questionNum);
							String questionText = currentUser.getOneSecurityQuestion(questionNum);
							String correctAnswer = currentUser.getOneSecurityQAnswer(questionNum);

							System.out
									.println("Please answer this security question (capitalization doesn't matter): \n"
											+ questionText);

							System.out.print("Your answer: ");

							String userAnswer = Main.scnr.nextLine();

							if (userAnswer.equalsIgnoreCase(correctAnswer)) {
								System.out.println("Correct answer!");
								userType = "student";
								return 0;
							} else {
								System.out.println("Incorrect answer");

							}

						}

					} else {

						System.out.println("Incorrect password for the entered username and user type");
						return 1;
					}
				}
			}
		}

		else if (userType.equalsIgnoreCase("Donor")) {
			typeValid = true;
			for (int i = 0; i < this.allDonors.size(); i++) {
				if (allDonors.get(i).username.equalsIgnoreCase(enteredUsername)) {
					if (allDonors.get(i).password.equals(enteredPassword)) {

						// donorUser = allDonors.get(i);
						currentUser = allDonors.get(i);
						for (int questionNum = 1; questionNum <= 3; questionNum++) {

							// String questionText = donorUser.getOneSecurityQuestion(questionNum);
							// String correctAnswer = donorUser.getOneSecurityQAnswer(questionNum);
							String questionText = currentUser.getOneSecurityQuestion(questionNum);
							String correctAnswer = currentUser.getOneSecurityQAnswer(questionNum);

							System.out
									.println("Please answer this security question (capitalization doesn't matter): \n"
											+ questionText);

							System.out.print("Your answer: ");

							String userAnswer = Main.scnr.nextLine();

							if (userAnswer.equalsIgnoreCase(correctAnswer)) {
								System.out.println("Correct answer!");
								userType = "donor";
								return 0;
							} else {
								System.out.println("Incorrect answer");

							}
						}

					} else {

						System.out.println("Incorrect password for the entered username and user type");
						return 1;
					}
				}
			}
		} else if (userType.equalsIgnoreCase("Admin")) {
			typeValid = true;
			for (int i = 0; i < this.allAdmins.size(); i++) {
				if (allAdmins.get(i).username.equalsIgnoreCase(enteredUsername)) {
					if (allAdmins.get(i).password.equals(enteredPassword)) {
						// store this user in the currentUser variable
						// adminUser = allAdmins.get(i);
						currentUser = allAdmins.get(i);
						for (int questionNum = 1; questionNum <= 3; questionNum++) {
							// String questionText = adminUser.getOneSecurityQuestion(questionNum);
							// String correctAnswer = adminUser.getOneSecurityQAnswer(questionNum);

							String questionText = currentUser.getOneSecurityQuestion(questionNum);
							String correctAnswer = currentUser.getOneSecurityQAnswer(questionNum);
							System.out
									.println("Please answer this security question (capitalization doesn't matter): \n"
											+ questionText);

							System.out.print("Your answer: ");

							String userAnswer = Main.scnr.nextLine();

							if (userAnswer.equalsIgnoreCase(correctAnswer)) {
								System.out.println("Correct answer!");
								userType = "admin";
								return 0;
							} else {
								System.out.println("Incorrect answer");

							}
						}
						// return 0 since we have a successful username/password match
						// return 0;
					} else {
						// valid username but incorrect password
						// return 2;
						System.out.println("Incorrect password for the entered username and user type");
						return 1;
					}
				}
			}
		} else if (userType.equalsIgnoreCase("FundSteward")) {
			// TODO: implement this method for FundStewards!!
			typeValid = true;
			for (int i = 0; i < this.allFundStewards.size(); i++) {
				if (allFundStewards.get(i).username.equalsIgnoreCase(enteredUsername)) {
					if (allFundStewards.get(i).password.equals(enteredPassword)) {

						// donorUser = allDonors.get(i);
						currentUser = allFundStewards.get(i);
						for (int questionNum = 1; questionNum <= 3; questionNum++) {

							// String questionText = donorUser.getOneSecurityQuestion(questionNum);
							// String correctAnswer = donorUser.getOneSecurityQAnswer(questionNum);
							String questionText = currentUser.getOneSecurityQuestion(questionNum);
							String correctAnswer = currentUser.getOneSecurityQAnswer(questionNum);

							System.out
									.println("Please answer this security question (capitalization doesn't matter): \n"
											+ questionText);

							System.out.print("Your answer: ");

							String userAnswer = Main.scnr.nextLine();

							if (userAnswer.equalsIgnoreCase(correctAnswer)) {
								System.out.println("Correct answer!");
								userType = "fund steward";
								return 0;
							} else {
								System.out.println("Incorrect answer");

							}
						}

					} else {

						System.out.println("Incorrect password for the entered username and user type");
						return 1;
					}
				}
			}
			// System.out.println("checkLoginDetails has not yet been implemented for
			// FundStewards");
		} else if (userType.equalsIgnoreCase("Staff")) {
			// TODO: implement this method for Staffs!!
			typeValid = true;
			for (int i = 0; i < this.allStaff.size(); i++) {
				if (allStaff.get(i).username.equalsIgnoreCase(enteredUsername)) {
					if (allStaff.get(i).password.equals(enteredPassword)) {

						// donorUser = allDonors.get(i);
						currentUser = allStaff.get(i);
						for (int questionNum = 1; questionNum <= 3; questionNum++) {

							// String questionText = donorUser.getOneSecurityQuestion(questionNum);
							// String correctAnswer = donorUser.getOneSecurityQAnswer(questionNum);
							String questionText = currentUser.getOneSecurityQuestion(questionNum);
							String correctAnswer = currentUser.getOneSecurityQAnswer(questionNum);

							System.out
									.println("Please answer this security question (capitalization doesn't matter): \n"
											+ questionText);

							System.out.print("Your answer: ");

							String userAnswer = Main.scnr.nextLine();

							if (userAnswer.equalsIgnoreCase(correctAnswer)) {
								System.out.println("Correct answer!");
								userType = "staff";
								return 0;
							} else {
								System.out.println("Incorrect answer");

							}
						}

					} else {

						System.out.println("Incorrect password for the entered username and user type");
						return 1;
					}
				}
			}
			// System.out.println("checkLoginDetails has not yet been implemented for
			// Staffs");
		} else {

			System.out.println("Invalid user type in checkLoginDetails()");
			return 2;
		}

		System.out.println("The entered username could not be found in the system for the entered user type.");
		return 2;

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

	// A getter that I added so I could check some details of scholarships in a test
	// method in Main
	public ArrayList<Scholarship> getAllScholarships() {
		return this.allScholarships;
	}

	/**
	 * Returns one Scholarship object based on the fileIndex value. This assumes
	 * that the fileIndex is unique,
	 * which it should have to be, because you can't have e.g. two folders called
	 * 'Scholarship1' (fileIndex 1) in the same Scholarships folder.
	 * 
	 * @param fileIndex the fileIndex of the scholarship object you want, i.e. 3 if
	 *                  you want the scholarship whose details
	 *                  are stored in the 'scholarship3' folder
	 * 
	 * @return the Scholarship object, or null if nothing was found
	 */
	public Scholarship getOneScholarshipByFileIndex(int fileIndex) {
		for (int i = 0; i < allScholarships.size(); i++) {
			if (allScholarships.get(i).getFileIndex() == fileIndex) {
				return allScholarships.get(i);
			}
		}

		// if we didn't find anything, return null
		return null;
	}

	public StudentProfile getOneStudentByFileIndex(int fileIndex) {
		for (int i = 0; i < allStudents.size(); i++) {
			if (allStudents.get(i).getFileIndex() == fileIndex) {
				return allStudents.get(i);
			}
		}

		// if we didn't find anything, return null
		return null;
	}

	/**
	 * Needed this for certain things, like figuring out the end value for a loop
	 * through fileIndex values in printAllScholarships, and maybe more
	 * **This will not always be the size of the allScholarships ArrayList, because
	 * we remove scholarships that are 5+ years past due, etc.
	 * but the fileIndex values don't get shifted
	 * 
	 * @return the highest fileIndex for a scholarship, ***that is currently in the
	 *         allScholarships ArrayList
	 */
	public int getMaxScholarshipFileIndex() {
		int maxIndex = 0;
		for (Scholarship scholarship : allScholarships) {
			if (scholarship.getFileIndex() > maxIndex) {
				maxIndex = scholarship.getFileIndex();
			}
		}

		return maxIndex;
	}

	/**
	 * @return all scholarship objects where isApproved is false
	 */
	public ArrayList<Scholarship> getAllUnapprovedScholarships() {
		ArrayList<Scholarship> outputScholarships = new ArrayList<Scholarship>();

		// add the scholarship to the output ArrayList if it is not approved
		for (Scholarship scholarship : getAllScholarships()) {
			if (!scholarship.getIsApproved()) {
				outputScholarships.add(scholarship);
			}
		}

		return outputScholarships;
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

	public ArrayList<StaffProfile> getAllStaff() {
		return allStaff;
	}

	public ArrayList<FundStewardProfile> getAllFundstewards() {
		return allFundStewards;
	}

	public void AwardScholarship(StudentProfile recipient, Scholarship scholarship) {
		LocalDate today = LocalDate.now();
		LocalDate due = scholarship.getDateDue();

		// if the due date has passed, don't want to award a scholarship before it's due
		// or on the due date
			scholarship.setAwarded(true);
			scholarship.setRecipient(recipient);
			// because it's after the due date
			scholarship.setArchived(true);
			recipient.addScholarship(scholarship);
			scholarship.setDateAdded(LocalDate.now());
	}

	public void AwardScholarshipPastDue(StudentProfile recipient, Scholarship scholarship) {
		LocalDate today = LocalDate.now();
		LocalDate due = scholarship.getDateDue();

		// if the due date has passed, don't want to award a scholarship before it's due
		// or on the due date
		if (today.isAfter(due)) {
			scholarship.setAwarded(true);
			scholarship.setRecipient(recipient);
			// because it's after the due date
			scholarship.setArchived(true);
			recipient.addScholarship(scholarship);
			scholarship.setDateAdded(LocalDate.now());
		}
	}

	// TO DO: test
	public void editStudentInfo(StudentProfile student) throws IOException {
		int choice;
		boolean end = false;

		while (!end) {
			System.out.println("1: First name: " + student.getFirstName());
			System.out.println("2: Last name: " + student.getLastName());
			System.out.println("3: Username: " + student.getUsername());
			System.out.println("4: Password: " + student.getPassword());
			System.out.println("5: Major: " + student.getMajor());
			if (student.getHasAMinor()) {
				System.out.println("6: Minor: " + student.getMinor());
			} else {
				System.out.println("6: Minor: none");
			}
			System.out.println("7: US Citizen: " + student.getIsUSCitizen());
			System.out.printf("8: GPA: %.3f", student.getGPA());
			System.out.println();
			System.out.println("9: In good standing?: " + student.getInGoodStanding());
			System.out.println("10: Has advanced standing?: " + student.getHasAdvStanding());
			System.out.println("11: Year: " + student.getGradeLevel());
			System.out.println("12: Graduation month: " + student.getGradMonth());
			System.out.println("13: Graduation year: " + student.getGradYear());
			System.out.println("14: Gender: " + student.getGender());
			System.out.println("15: Is a full-time student?: " + student.getIsFullTimeStudent());
			System.out.println("16: Is a transfer student?: " + student.getIsTransferStudent());
			System.out.println("17: Receives funding?: " + student.getReceivesFunding());
			System.out.println("18: Number of credits: " + student.getCurNumCredits());
			System.out.println("19: Personal statement: " + student.getPersonalStatement());

			System.out.print("\nEnter the number of the profile attribute you would like to change, or -1 to exit: ");
			choice = Main.scnr.nextInt();

			// -1, the user wants to exit
			if (choice == -1) {
				// just stop the loop
				end = true;
			}

			// Choice 1: first name (String)
			else if (choice == 1) {
				Main.scnr.nextLine();
				System.out.print("Enter the new first name: ");
				String newFirstName = Main.scnr.nextLine();
				student.setFirstName(newFirstName);
			}

			// Choice 2: Last Name (String)
			else if (choice == 2) {
				Main.scnr.nextLine();
				System.out.print("Enter the new last name: ");
				String newLastName = Main.scnr.nextLine();
				student.setLastName(newLastName);
			}

			// Choice 3: Username (String)
			else if (choice == 3) {
				Main.scnr.nextLine();
				System.out.print("Enter the new username: ");
				String newUsername = Main.scnr.nextLine();
				student.setUsername(newUsername);
			}

			// Choice 4: Password (String)
			else if (choice == 4) {
				Main.scnr.nextLine();
				System.out.print("Enter the new password: ");
				String newPassword = Main.scnr.nextLine();
				student.setPassword(newPassword);
			}

			// Choice 5: Major (String)
			else if (choice == 5) {
				Main.scnr.nextLine();
				System.out.print("Enter the new major: ");
				String newMajor = Main.scnr.nextLine();
				student.setMajor(newMajor);
			}

			// Choice 6: Minor (String)
			else if (choice == 6) {
				if (student.getHasAMinor()) {
					Main.scnr.nextLine();
					Boolean changeMinor = false;

					while (!changeMinor) {
						System.out.print("Would you like to change or remove your minor? (c/r): ");
						String changeMinorChoice = Main.scnr.nextLine();
						if (changeMinorChoice.compareTo("c") == 0) {
							changeMinor = true;
							System.out.print("Enter the new minor: ");
							String newMinor = Main.scnr.nextLine();

							student.setMinor(newMinor);
						} else if (changeMinorChoice.compareTo("r") == 0) {
							changeMinor = true;
							student.setHasAMinor(false);
							student.setMinor("");
						} else {
							System.out.println("Please enter 'r' or 'c'.");
						}
					}

				} else {
					Main.scnr.nextLine();
					System.out.print("Enter the new minor: ");
					String newMinor = Main.scnr.nextLine();

					student.setMinor(newMinor);
					student.setHasAMinor(true);
				}
			}

			// Choice 7: US Citizen (boolean)
			else if (choice == 7) {
				System.out.print("Enter the new value (true/false): ");
				boolean newBooleanValue = Main.scnr.nextBoolean();
				Main.scnr.nextLine();
				student.setIsUSCitizen(newBooleanValue);
			}

			//Choice 8: GPA (Float)
			else if (choice == 8) {
				System.out.print("Enter the new GPA: ");
				float newGPA = Main.scnr.nextFloat();
				Main.scnr.nextLine();
				student.setGPA(newGPA);
			}

			// Choice 9: In Good Standing? (boolean)
			else if (choice == 9) {
				System.out.print("Enter the new value (true/false): ");
				boolean newBooleanValue = Main.scnr.nextBoolean();
				Main.scnr.nextLine();
				student.setInGoodStanding(newBooleanValue);
			}

			// Choice 10: Has Advanced Standing? (boolean)
			else if (choice == 10) {
				System.out.print("Enter the new value (true/false): ");
				boolean newBooleanValue = Main.scnr.nextBoolean();
				Main.scnr.nextLine();
				student.setHasAdvStanding(newBooleanValue);
			}

			// Choice 11: Year (String)
			else if (choice == 11) {
				Main.scnr.nextLine();
				System.out.print("Enter the new grade year: ");
				String newYear = Main.scnr.nextLine();
				student.setGradeLevel(newYear);
			}

			// Choice 12: Graduation Month (int)
			else if (choice == 12) {
				System.out.print("Enter the new graduation month (in number form with 2 digits): ");
				int newMonth = Main.scnr.nextInt();
				Main.scnr.nextLine();
				student.setGradMonth(newMonth);
			}

			// Choice 13: Graduation Year (int)
			else if (choice == 13) {
				System.out.print("Enter the new graduation year (with 4 digits): ");
				int newYear = Main.scnr.nextInt();
				Main.scnr.nextLine();
				student.setGradYear(newYear);
			}

			// Choice 14: Gender (String)
			else if (choice == 14) {
				Main.scnr.nextLine();
				System.out.print("Enter the new gender: ");
				String newGender = Main.scnr.nextLine();
				student.setGender(newGender);
			}

			// Choices 15-17: Booleans (isFullTimeStudent, isTransferStudent,
			// receivesFunding)
			else if (choice >= 15 && choice <= 17) {
				System.out.print("Enter the new value (true/false): ");
				boolean newBooleanValue = Main.scnr.nextBoolean();
				Main.scnr.nextLine();

				if (choice == 15) {

					student.setIsFullTimeStudent(newBooleanValue);
				} else if (choice == 16) {

					student.setIsTransferStudent(newBooleanValue);
				} else {

					student.setReceivesFunding(newBooleanValue);
				}
			}

			// Choice 18: Number of Credits (int)
			else if (choice == 18) {
				System.out.print("Enter the new number of credits: ");
				int newCredits = Main.scnr.nextInt();
				Main.scnr.nextLine();
				student.setCurNumCredits(newCredits);
			}

			// Choice 19: Personal Statement (String)
			else if (choice == 19) {
				Main.scnr.nextLine();
				System.out.print("Enter the new personal statement: ");
				String newStatement = Main.scnr.nextLine();
				student.setPersonalStatement(newStatement);
			}

			// if they entered a valid choice number, 1 through 19
			if (choice <= 19 && choice >= 1) {

				System.out.print("Would you like to change anything else? (y/n): ");
				if (Main.scnr.next().equals("n")) {
					end = true;

					updateStudentProfileFile(student);
				}
			} else {
				System.out.print("Please enter the number of your choice.");
			}
		}
	}


	// TODO: ****** test these q+a methods!

	// Q&A with donor, passing in obj to create donor profile with appropriate
	// associated information
	public DonorProfile getDonorFromInput() {

		DonorProfile donorObj = new DonorProfile();
		String firstName, lastName, username, password, securityQuestion1, securityQuestion2, securityQuestion3;

		System.out.println("Please enter in your first name.");
		firstName = Main.scnr.nextLine();
		donorObj.setFirstName(firstName);

		System.out.println("Please enter in your last name.");
		lastName = Main.scnr.nextLine();
		donorObj.setLastName(lastName);

		System.out.println("Please enter in your username.");
		username = Main.scnr.nextLine();
		donorObj.setUsername(username);

		System.out.println("Please enter in your password.");
		password = Main.scnr.nextLine();
		donorObj.setPassword(password);

		System.out.println(
				"Please enter in your response for the first security question which is the following: What is your mother's maiden name?");
		securityQuestion1 = Main.scnr.nextLine();
		donorObj.setOneSecurityQAnswer(1, securityQuestion1);

		System.out.println(
				"Please enter in your response for the first security question which is the following: What is the mascot of your middle school?");
		securityQuestion2 = Main.scnr.nextLine();
		donorObj.setOneSecurityQAnswer(2, securityQuestion2);

		System.out.println(
				"Please enter in your response for the first security question which is the following: What is the name of the city you were born in?");
		securityQuestion3 = Main.scnr.nextLine();
		donorObj.setOneSecurityQAnswer(3, securityQuestion3);

		return donorObj;
	}

	// Q&A with admin, passing in obj to create admin profile with appropriate
	// associated information
	public AdminProfile getAdminFromInput() {

		AdminProfile AdminObj = new AdminProfile();
		String firstName, lastName, username, password, securityQuestion1, securityQuestion2, securityQuestion3;

		System.out.println("Please enter in your first name.");
		firstName = Main.scnr.nextLine();
		AdminObj.setFirstName(firstName);

		System.out.println("Please enter in your last name.");
		lastName = Main.scnr.nextLine();
		AdminObj.setLastName(lastName);

		System.out.println("Please enter in your username.");
		username = Main.scnr.nextLine();
		AdminObj.setUsername(username);

		System.out.println("Please enter in your password.");
		password = Main.scnr.nextLine();
		AdminObj.setPassword(password);

		System.out.println(
				"Please enter in your response for the first security question which is the following: What is your mother's maiden name?");
		securityQuestion1 = Main.scnr.nextLine();
		AdminObj.setOneSecurityQAnswer(1, securityQuestion1);

		System.out.println(
				"Please enter in your response for the first security question which is the following: What is the mascot of your middle school?");
		securityQuestion2 = Main.scnr.nextLine();
		AdminObj.setOneSecurityQAnswer(2, securityQuestion2);

		System.out.println(
				"Please enter in your response for the first security question which is the following: What is the name of the city you were born in?");
		securityQuestion3 = Main.scnr.nextLine();
		AdminObj.setOneSecurityQAnswer(3, securityQuestion3);


		return AdminObj;
	}

	// Q&A with fundSteward, passing in obj to create fundSteward profile with
	// appropriate associated information
	public FundStewardProfile getFundStewardFromInput() {

		FundStewardProfile FundStewardObj = new FundStewardProfile();
		String firstName, lastName, username, password, securityQuestion1, securityQuestion2, securityQuestion3;

		System.out.println("Please enter in your first name.");
		firstName = Main.scnr.nextLine();
		FundStewardObj.setFirstName(firstName);

		System.out.println("Please enter in your last name.");
		lastName = Main.scnr.nextLine();
		FundStewardObj.setLastName(lastName);

		System.out.println("Please enter in your username.");
		username = Main.scnr.nextLine();
		FundStewardObj.setUsername(username);

		System.out.println("Please enter in your password.");
		password = Main.scnr.nextLine();
		FundStewardObj.setPassword(password);

		System.out.println(
				"Please enter in your response for the first security question which is the following: What is your mother's maiden name?");
		securityQuestion1 = Main.scnr.nextLine();
		FundStewardObj.setOneSecurityQAnswer(1, securityQuestion1);

		System.out.println(
				"Please enter in your response for the first security question which is the following: What is the mascot of your middle school?");
		securityQuestion2 = Main.scnr.nextLine();
		FundStewardObj.setOneSecurityQAnswer(2, securityQuestion2);

		System.out.println(
				"Please enter in your response for the first security question which is the following: What is the name of the city you were born in?");
		securityQuestion3 = Main.scnr.nextLine();
		FundStewardObj.setOneSecurityQAnswer(3, securityQuestion3);


		return FundStewardObj;
	}

	// Q&A with staff, passing in obj to create staff profile with appropriate
	// associated information
	public StaffProfile getStaffFromInput() {

		StaffProfile StaffObj = new StaffProfile();
		String firstName, lastName, username, password, jobRole, securityQuestion1, securityQuestion2,
				securityQuestion3;

		System.out.println("Please enter in your first name.");
		firstName = Main.scnr.nextLine();
		StaffObj.setFirstName(firstName);

		System.out.println("Please enter in your last name.");
		lastName = Main.scnr.nextLine();
		StaffObj.setLastName(lastName);

		System.out.println("Please enter in your username.");
		username = Main.scnr.nextLine();
		StaffObj.setUsername(username);

		System.out.println("Please enter in your password.");
		password = Main.scnr.nextLine();
		StaffObj.setPassword(password);

		System.out.println("Please enter in your job Role.");
		jobRole = Main.scnr.nextLine();
		// getting errors for calling function setJobRole for some reason
		StaffObj.setJobRole(jobRole);

		System.out.println(
				"Please enter in your response for the first security question which is the following: What is your mother's maiden name?");
		securityQuestion1 = Main.scnr.nextLine();
		StaffObj.setOneSecurityQAnswer(1, securityQuestion1);

		System.out.println(
				"Please enter in your response for the first security question which is the following: What is the mascot of your middle school?");
		securityQuestion2 = Main.scnr.nextLine();
		StaffObj.setOneSecurityQAnswer(2, securityQuestion2);

		System.out.println(
				"Please enter in your response for the first security question which is the following: What is the name of the city you were born in?");
		securityQuestion3 = Main.scnr.nextLine();
		StaffObj.setOneSecurityQAnswer(3, securityQuestion3);


		return StaffObj;
	}

	// Q&A with student, passing in obj to create student profile with appropriate
	// associated information
	// TODO: (MiLee) basic outline of same Q&A as other profiles, not sure if we
	// need to expand with more values?
	public StudentProfile getStudentFromInput() {

		StudentProfile StudentObj = new StudentProfile();
		String firstName, lastName, username, password, major, minor, gradeLevel, gender, personalStatement,
				securityQuestion1, securityQuestion2, securityQuestion3;
		int studentID, gradMonth, gradYear, numCredits;
		float gpa;
		char boolAnswer1, boolAnswer2, boolAnswer3, boolAnswer4, boolAnswer5, boolAnswer6, boolAnswer7;
		

		System.out.println("Please enter in your first name.");
		firstName = Main.scnr.nextLine();
		StudentObj.setFirstName(firstName);

		System.out.println("Please enter in your last name.");
		lastName = Main.scnr.nextLine();
		StudentObj.setLastName(lastName);

		System.out.println("Please enter your student ID number (an integer).");
		studentID = Main.scnr.nextInt();
		StudentObj.setStudentID(studentID);

		Main.scnr.nextLine();

		System.out.println("Please enter in your username.");
		username = Main.scnr.nextLine();
		StudentObj.setUsername(username);

		System.out.println("Please enter in your password.");
		password = Main.scnr.nextLine();
		StudentObj.setPassword(password);

		System.out.println("Please enter in your major.");
		major = Main.scnr.nextLine();
		StudentObj.setMajor(major);

		System.out.println("Do you have a minor? (y/n)");
		boolAnswer1 = Main.scnr.next().charAt(0);
		Main.scnr.nextLine();
		if (boolAnswer1 == 'y' || boolAnswer1 == 'Y') {
			StudentObj.setHasAMinor(true);
			System.out.println("Please enter in your minor.");
			minor = Main.scnr.nextLine();
			StudentObj.setMinor(minor);
		}

		System.out.println("Are you a US Citizen? (y/n)");
		boolAnswer2 = Main.scnr.next().charAt(0);
		Main.scnr.nextLine();
		if (boolAnswer2 == 'y' || boolAnswer2 == 'Y') {
			StudentObj.setIsUSCitizen(true);
		}

		System.out.println("Please enter in your GPA.");
		gpa = Main.scnr.nextFloat();
		StudentObj.setGPA(gpa);

		Main.scnr.nextLine();

		System.out.println("Are you in Good Standing? (y/n)");
		boolAnswer3 = Main.scnr.next().charAt(0);
		Main.scnr.nextLine();
		if (boolAnswer3 == 'y' || boolAnswer3 == 'Y') {
			StudentObj.setInGoodStanding(true);
		}

		System.out.println("Do you have Advanced Standing? (y/n)");
		boolAnswer4 = Main.scnr.next().charAt(0);
		Main.scnr.nextLine();
		if (boolAnswer4 == 'y' || boolAnswer4 == 'Y') {
			StudentObj.setHasAdvStanding(true);
		}

		System.out.println("Please enter in your grade level (freshman, sophomore...).");
		gradeLevel = Main.scnr.nextLine();
		StudentObj.setGradeLevel(gradeLevel);

		System.out.println("Please enter your graduation Month (ex: 01)");
		gradMonth = Main.scnr.nextInt();
		StudentObj.setGradMonth(gradMonth);

		Main.scnr.nextLine();

		System.out.println("Please enter your graduation Year (ex: 2024)");
		gradYear = Main.scnr.nextInt();
		StudentObj.setGradYear(gradYear);

		Main.scnr.nextLine();

		System.out.println("Please enter in your gender");
		gender = Main.scnr.nextLine();
		StudentObj.setGender(gender);

		System.out.println("Are you a Full Time Student? (y/n)");
		boolAnswer5 = Main.scnr.next().charAt(0);
		Main.scnr.nextLine();
		if (boolAnswer5 == 'y' || boolAnswer5 == 'Y') {
			StudentObj.setIsFullTimeStudent(true);
		}

		System.out.println("Are you a Transfer Student? (y/n)");
		boolAnswer6 = Main.scnr.next().charAt(0);
		Main.scnr.nextLine();
		if (boolAnswer6 == 'y' || boolAnswer6 == 'Y') {
			StudentObj.setIsTransferStudent(true);
		}

		System.out.println("Please enter the number of credits you are currently enrolled in.");
		numCredits = Main.scnr.nextInt();
		StudentObj.setCurNumCredits(numCredits);

		Main.scnr.nextLine();

		System.out.println("Please enter in your personal statement (max 500 words)");
		personalStatement = Main.scnr.nextLine();
		StudentObj.setPersonalStatement(personalStatement);

		System.out.println("Do you already recieve funding? (y/n)");
		boolAnswer7 = Main.scnr.next().charAt(0);
		Main.scnr.nextLine();
		if (boolAnswer7 == 'y' || boolAnswer7 == 'Y') {
			StudentObj.setReceivesFunding(true);
		}

		System.out.println(
				"Please enter in your response for the first security question which is the following: What is your mother's maiden name?");
		securityQuestion1 = Main.scnr.nextLine();
		StudentObj.setOneSecurityQAnswer(1, securityQuestion1);

		System.out.println(
				"Please enter in your response for the first security question which is the following: What is the mascot of your middle school?");
		securityQuestion2 = Main.scnr.nextLine();
		StudentObj.setOneSecurityQAnswer(2, securityQuestion2);

		System.out.println(
				"Please enter in your response for the first security question which is the following: What is the name of the city you were born in?");
		securityQuestion3 = Main.scnr.nextLine();
		StudentObj.setOneSecurityQAnswer(3, securityQuestion3);


		return StudentObj;
	}

	/**
	 * this is for a donor who is creating a new scholarship and typing in the
	 * details. If they confirm it,
	 * the scholarship gets added to allScholarships and printed to a new file
	 * 
	 * NOTE: this assumes that the currentUser is a Donor, who is also the only
	 * donor to this new scholarship
	 * 
	 * @return a scholarship object containing the info that was entered, don't know
	 *         if we'll need this
	 */
	public Scholarship createScholarshipFromInput() throws IOException {
		Main.scnr.nextLine();
		System.out.println("Enter the name of the scholarship:");
		String name = Main.scnr.nextLine();

		System.out.println("Enter the description of the scholarship:");
		String description = Main.scnr.nextLine();

		// the donor is the current logged in user
		// we are assuming that this code will only run when it is a DonorProfile, so it
		// is safe to typecast this
		DonorProfile donor = (DonorProfile) getCurrentUser();

		System.out.println("Enter the award amount:");
		float awardAmount = Main.scnr.nextFloat();
		Main.scnr.nextLine(); // consume the newline character

		System.out.println("Enter the requirements (comma-separated):");
		ArrayList<String> requirements = new ArrayList<>();

		int userSelection = 0;
		// loop through until they're done, adding one category and then the allowed
		// valued for it, at a time
		do {
			System.out.println("Please enter a number, to add it as a requirement, or type -1 to exit"
					+ " (Remember that you can add a requirement multiple times if you want to allow multiple different values): ");

			System.out.println("1 - major");
			System.out.println("2 - minor");
			System.out.println("3 - hasAMinor");
			System.out.println("4 - isUSCitizen");
			System.out.println("5 - GPA");
			System.out.println("6 - inGoodStanding");
			System.out.println("7 - hasAdvStanding");
			System.out.println("8 - gradeLevel");
			System.out.println("9 - gradMonth");
			System.out.println("10 - gradYear");
			System.out.println("11 - gender");
			System.out.println("12 - isFullTimeStudent");
			System.out.println("13 - isTransferStudent");
			System.out.println("14 - curNumCredits");
			System.out.println("15 - receivesFunding");

			System.out.print("\nYour selection: ");
			userSelection = Main.scnr.nextInt();
			// consume the newline
			Main.scnr.nextLine();

			if (userSelection == -1) {
				break;
			} else if (userSelection == 1) {
				requirements.add("major");
			} else if (userSelection == 2) {
				requirements.add("minor");
			} else if (userSelection == 3) {
				requirements.add("hasAMinor");
			} else if (userSelection == 4) {
				requirements.add("isUSCitizen");
			} else if (userSelection == 5) {
				requirements.add("GPA");
			} else if (userSelection == 6) {
				requirements.add("inGoodStanding");
			} else if (userSelection == 7) {
				requirements.add("hasAdvStanding");
			} else if (userSelection == 8) {
				requirements.add("gradeLevel");
			} else if (userSelection == 9) {
				requirements.add("gradMonth");
			} else if (userSelection == 10) {
				requirements.add("gradYear");
			} else if (userSelection == 11) {
				requirements.add("gender");
			} else if (userSelection == 12) {
				requirements.add("isFullTimeStudent");
			} else if (userSelection == 13) {
				requirements.add("isTransferStudent");
			} else if (userSelection == 14) {
				requirements.add("curNumCredits");
			} else if (userSelection == 15) {
				requirements.add("receivesFunding");
			}

			System.out.println("Enter the allowed value:");
			String value = Main.scnr.nextLine();
			requirements.add(value);
		} while (userSelection != -1);

		System.out.println("Enter the application questions (line-separated):");
		ArrayList<String> application = new ArrayList<>();
		String applicationDetail;

		while (true) {
			System.out.println("Enter the application question (or 'done' to finish):");
			applicationDetail = Main.scnr.nextLine();
			if (applicationDetail.equals("done")) {
				break;
			}
			application.add(applicationDetail);
		}

		System.out.println("Enter the due date (in the format YYYY-MM-DD):");
		String dateDueString = Main.scnr.nextLine();

		// Create a new Scholarship object
		// have to have this up here so we can print the details
		Scholarship scholarship = new Scholarship(name, description, donor, awardAmount, requirements, application,
				dateDueString);

		System.out.println("Here is the scholarship you have created:");
		System.out.println(scholarship.getAllInfoString());

		System.out.print("\nDo you want to add this new scholarship to the system? (y/n): ");
		String userConfirm = Main.scnr.nextLine();

		if (userConfirm.equals("y")) {
			allScholarships.add(scholarship);

			// print it to a new file
			storeNewScholarship(scholarship);

			return scholarship;
		}
		// else do nothing??

		// we didn't actually add the scholarship to the system, so return null
		return null;
	}

	/*
	 * - look for match object
	 * - if exists, have student continue
	 * - if does not exist, have student start
	 * - ask if student wants to save or submit or discard
	 * - store changes
	 */
	public void applyToScholarship(int fileIndex) throws IOException {
		Boolean matchExists = false;
		Scholarship scholarship = getOneScholarshipByFileIndex(fileIndex);

		for (MatchRelationship match : this.allMatchRelationships) {
			if (scholarship.getName().equals(match.getScholarshipName())
					&& currentUser.getName().equals(match.getStudentName())) {
				matchExists = true;
				if (match.getApplicationStatus().equals("in progress")) {
					int qIndex = 1;
					System.out.println("Looks like you've started your application. This is what you have currently:");
					System.out.println();

					for (Map.Entry<String, String> pair : match.getApplication().entrySet()) {
						System.out.println("Question " + qIndex + ":");
						System.out.println(pair.getKey());
						System.out.println(pair.getValue());
						qIndex++;
					}

					editApplication(match);
				} else if (match.getApplicationStatus().equals("submitted")) {
					System.out.println("You've already applied!");
				} else {
					int qIndex = 1;
					System.out.println("Heres the application questions: ");
					System.out.println();

					for (Map.Entry<String, String> pair : match.getApplication().entrySet()) {
						System.out.println("Question " + qIndex + ":");
						System.out.println(pair.getKey());
						qIndex++;
					}

					editApplication(match);
				}
			}

		}

		if (!matchExists) {
			MatchRelationship newMatch = produceNewMatch((StudentProfile) currentUser, scholarship);

			int qIndex = 1;
			System.out.println("Heres the application questions: ");
			System.out.println();

			for (Map.Entry<String, String> pair : newMatch.getApplication().entrySet()) {
				System.out.println("Question " + qIndex + ":");
				System.out.println(pair.getKey());
				qIndex++;
			}

			editApplication(newMatch);
		}

	}

	public void editApplication(MatchRelationship match) throws IOException {
		Boolean quit = false;
		int questionIndex;
		int currIndex;
		String answer;
		String saveChoice;
		String continueChoice;

		while (!quit) {
			System.out.print("Enter the number of the question you'd like to answer or change: ");
			questionIndex = Main.scnr.nextInt();
			Main.scnr.nextLine();
			currIndex = 1;

			if (questionIndex < 1 || questionIndex > match.getApplication().size()) {
				System.out.println("Not valid input. Please enter the number of a question.");
			} else {
				System.out.println("Type your answer: ");
				answer = Main.scnr.nextLine();

				System.out.println("Your answer to question " + questionIndex + " is now:");
				System.out.println(answer);
				System.out.println("Would you like to save it? (y/n)");
				saveChoice = Main.scnr.nextLine();

				if (saveChoice.equals("y")) {
					for (Map.Entry<String, String> pair : match.getApplication().entrySet()) {
						if (currIndex == questionIndex) {
							pair.setValue(answer);
						}
						currIndex++;
					}

					System.out.println("Change saved. Would you like to continue editing? (y/n)");
					continueChoice = Main.scnr.nextLine();
					if (!continueChoice.equals("y")) {
						quit = true;
					}

					updateMatchFile(match);
				} else {
					System.out.println("Changes discarded. Would you like to continue editing? (y/n)");
					continueChoice = Main.scnr.nextLine();
					if (!continueChoice.equals("y")) {
						quit = true;
					}
				}

			}

		}

		int saveOrSubmit;
		Boolean validInput = false;

		while (!validInput) {
			System.out.println("Would you like to submit this application or save it to continue later?");
			System.out.println("1 - Save");
			System.out.println("2 - Submit");

			saveOrSubmit = Main.scnr.nextInt();
			Main.scnr.nextLine();

			if (saveOrSubmit == 1) {
				validInput = true;
				match.setApplicationToInProgress();
			} else if (saveOrSubmit == 2) {
				validInput = true;
				match.setApplicationToSubmitted();
			} else {
				System.out.println("Invalid input. Please enter 1 or 2.");
			}

			updateMatchFile(match);
			this.allMatchRelationships.add(match);
		}
	}

	public void getSubmittedApplications () {
		ArrayList<MatchRelationship> matchesFound = new ArrayList<MatchRelationship>();

		for (MatchRelationship match : this.allMatchRelationships) {
			if(match.getStudentName().equals(((StudentProfile)currentUser).getName()) && match.getApplicationStatus().equals("submitted") 
			&& match.getIsActive()) {
				matchesFound.add(match);
			}
		}

		if (matchesFound.size() == 0) {
			System.out.println("You have not submitted any applications!");
		}
		else {
			for (MatchRelationship matchFound: matchesFound) {
				System.out.println("Scholarship: " + matchFound.getScholarshipName());
				System.out.println("Submitted application: ");
				for (Map.Entry<String, String> pair : matchFound.getApplication().entrySet()) {
					System.out.println(pair.getKey());
					System.out.println(pair.getValue());
					System.out.println();
				}
			}
		}
	}

	public ArrayList<Scholarship> getScholarshipsAvailableToStudents() {
		ArrayList<Scholarship> availableScholarships = new ArrayList<Scholarship>();

		for(Scholarship scholarship : this.allScholarships) {
			if (scholarship.getIsApproved() && !scholarship.getIsArchived()) {
				availableScholarships.add(scholarship);
			}
		}

		return availableScholarships;
	}

	public ArrayList<MatchRelationship> getInProgressApplications() {
		ArrayList<MatchRelationship> inProgressMatches = new ArrayList<MatchRelationship>();

		for (MatchRelationship match : this.allMatchRelationships) {
			if(match.getStudentName().equals(((StudentProfile)currentUser).getName()) && match.getApplicationStatus().equals("in progress")
			&& match.getIsActive()) {
				inProgressMatches.add(match);
			}
		}

		return inProgressMatches;
		
	}

}
