import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
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

	public void updateScholarshipFile(Scholarship scholarship) throws IOException {
		int nextFileIndex = findNextFileIndex("scholarship");
		File folder = new File("scholarships/scholarship" + String.valueOf(nextFileIndex));

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

		// File detailsF = new File(folderPath + "/details.txt");
		// detailsF.createNewFile();
		// FileWriter detailsW = new FileWriter(detailsF);

		// detailsW.write(donor.getDetailsFileText());

		// detailsW.close();

		// File scholsF = new File(folderPath + "/scholarships.txt");
		// scholsF.createNewFile();
		// FileWriter scholsW = new FileWriter(scholsF, false);

		// for (Scholarship scholarship : donor.getAwardsReceived()) {
		// awardNames.add(scholarship.getName());
		// }

		// awardsW.write(String.join("\n", awardNames));

		// awardsW.close();

		// ********* from the update donor method
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

	public AdminProfile readAdminProfile(int fileIndex) throws IOException {
		String folderPath = "administrators/admin" + String.valueOf(fileIndex);
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

		// Create and return new AdminProfile
		return new AdminProfile(firstName, lastName, username, password, sq1, sq2, sq3, fileIndex);
	}

	public StaffProfile readStaffProfile(int fileIndex) throws IOException {
		String folderPath = "staff/staff" + String.valueOf(fileIndex);
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

	public FundStewardProfile readFundStewardProfile(int fileIndex) throws IOException{
		String folderPath = "fundstewards/fundsteward" + String.valueOf(fileIndex);
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


	// searches a folder for a scholarship with inputted value
	public ArrayList<Scholarship> searchScholarships(String inputCategory, String inputSearchValue) {
		ArrayList<Scholarship> scholarshipsFound = new ArrayList<Scholarship>();
		HashMap<String, String> requirements = new HashMap<String, String>();
		LocalDate inputDate;

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
		} else if (inputCategory.compareTo("due date") == 0) {

			for (Scholarship scholarship : this.allScholarships) {
				inputDate = LocalDate.parse(inputSearchValue);
				if (scholarship.getDateDue().compareTo(inputDate) == 0) {
					scholarshipsFound.add(scholarship);
				}
			}

		} else if (inputCategory.compareTo("date posted") == 0) {

			for (Scholarship scholarship : this.allScholarships) {
				inputDate = LocalDate.parse(inputSearchValue);
				if (scholarship.getDateAdded().compareTo(inputDate) == 0) {
					scholarshipsFound.add(scholarship);
				}
			}

		} else if (inputCategory.compareTo("award amount") == 0) {

			for (Scholarship scholarship : this.allScholarships) {
				if (Float.compare(scholarship.getAwardAmount(), Float.parseFloat(inputSearchValue)) >= 0) {
					scholarshipsFound.add(scholarship);
				}
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

	public ArrayList<StudentProfile> searchStudents(String inputCategory, String inputSearchValue) {
		ArrayList<StudentProfile> studentsFound = new ArrayList<StudentProfile>();
		HashMap<String, String> requirements = new HashMap<String, String>();

		if (inputCategory.compareTo("name") == 0) {

			for (StudentProfile student : this.allStudents) {
				if (student.getName().compareTo(inputSearchValue) == 0) {
					studentsFound.add(student);
				}
			}

		} else if (inputCategory.compareTo("year") == 0) {

			for (StudentProfile student : this.allStudents) {
				if (student.getGradeLevel().compareTo(inputSearchValue) == 0) {
					studentsFound.add(student);
				}
			}

		} else if (inputCategory.compareTo("major") == 0) {

			for (StudentProfile student : this.allStudents) {

				if (student.getMajor().compareTo(inputSearchValue) == 0) {
					studentsFound.add(student);
				}
			}

		} else if (inputCategory.compareTo("minor") == 0) {

			for (StudentProfile student : this.allStudents) {

				if (student.getMinor().compareTo(inputSearchValue) == 0) {
					studentsFound.add(student);
				}
			}

		} else if (inputCategory.compareTo("GPA") == 0) {

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

	// A getter that I added so I could check some details of scholarships in a test
	// method in Main
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
				3.6, true, true, "Freshman", 5, 2025, "Female", true, false, 12, false, "I love school!", "Smith",
				"The eagles", "New York");

		storeNewStudentProfile(newStudent);

		System.out.print(newStudent.toString());
	}

	public void AwardScholarship(StudentProfile recipient, Scholarship scholarship) {
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

	//TO DO: test
	public void editStudentInfo(StudentProfile student) {
		int choice;

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
		System.out.println("8: GPA: " + student.getGPA());
		System.out.println("9: In good standing?: " + student.getInGoodStanding());
		System.out.println("10: Has advanced standing?: " + student.getHasAdvStanding());
		System.out.println("11: Year: " + student.getGradeLevel());
		System.out.println("12: Graduation month: " + student.getGradMonth());
		System.out.println("13: Graduation year: " + student.getGradYear());
		System.out.println("14: Gender: " + student.getGender());
		System.out.println("15: Is a full-time student?: " + student.getIsFullTimeStudent());
		System.out.println("16: Is a transfer student?: " + student.getIsTransferStudent());
		System.out.println("17: Number of credits: " + student.getCurNumCredits());
		System.out.println("18: Receives credit?: " + student.getReceivesFunding());
		System.out.println("19: Personal statement: " + student.getPersonalStatement());

		Scanner scnr = new Scanner(System.in);

		System.out.print("Enter the number of the profile attribute you would like to change: ");
		choice = scnr.nextInt();

		if (choice == 1) {
			scnr.nextLine(); 
			System.out.print("Enter the new first name: ");
			String newFirstName = scnr.nextLine();
			student.setFirstName(newFirstName);
		}
		
		// Choice 2: Last Name (String)
		else if (choice == 2) {
			scnr.nextLine(); 
			System.out.print("Enter the new last name: ");
			String newLastName = scnr.nextLine();
			student.setLastName(newLastName);
		}
		
		// Choice 3: Username (String)
		else if (choice == 3) {
			scnr.nextLine(); 
			System.out.print("Enter the new username: ");
			String newUsername = scnr.nextLine();
			student.setUsername(newUsername);
		}
		
		// Choice 4: Password (String)
		else if (choice == 4) {
			scnr.nextLine(); 
			System.out.print("Enter the new password: ");
			String newPassword = scnr.nextLine();
			student.setPassword(newPassword);
		}
		
		// Choice 5: Major (String)
		else if (choice == 5) {
			scnr.nextLine(); 
			System.out.print("Enter the new major: ");
			String newMajor = scnr.nextLine();
			student.setMajor(newMajor);
		}
		
		// Choice 6: Minor (String)
		else if (choice == 6) {
			if (student.getHasAMinor()) {
				scnr.nextLine(); 
				System.out.print("Enter the new minor: ");
				String newMinor = scnr.nextLine();

				student.setMinor(newMinor);
			} else {
				System.out.println("The student does not have a minor to change.");
			}
		}
		
		// Choice 7: US Citizen (boolean)
		else if (choice == 7) {
			System.out.print("Enter the new value (true/false): ");
			boolean newBooleanValue = scnr.nextBoolean();
			student.setIsUSCitizen(newBooleanValue);
		}

		else if (choice == 8) {
			System.out.print("Enter the new GPA: ");
			float newGPA = scnr.nextFloat();
			student.setGPA(newGPA);
		}
		
		// Choice 9: In Good Standing? (boolean)
		else if (choice == 9) {
			System.out.print("Enter the new value (true/false): ");
			boolean newBooleanValue = scnr.nextBoolean();
			student.setInGoodStanding(newBooleanValue);
		}
		
		// Choice 10: Has Advanced Standing? (boolean)
		else if (choice == 10) {
			System.out.print("Enter the new value (true/false): ");
			boolean newBooleanValue = scnr.nextBoolean();
			student.setHasAdvStanding(newBooleanValue);
		}
		
		// Choice 11: Year (String)
		else if (choice == 11) {
			scnr.nextLine(); 
			System.out.print("Enter the new grade year: ");
			String newYear = scnr.nextLine();
			student.setGradeLevel(newYear);
		}
		
		// Choice 12: Graduation Month (int)
		else if (choice == 12) {
			System.out.print("Enter the new graduation month: ");
			int newMonth = scnr.nextInt();
			student.setGradMonth(newMonth);
		}
		
		// Choice 13: Graduation Year (int)
		else if (choice == 13) {
			System.out.print("Enter the new graduation year: ");
			int newYear = scnr.nextInt();
			student.setGradYear(newYear);
		}
		
		// Choice 14: Gender (String)
		else if (choice == 14) {
			scnr.nextLine(); 
			System.out.print("Enter the new gender: ");
			String newGender = scnr.nextLine();
			student.setGender(newGender);
		}
		
		// Choices 15-17: Booleans (isFullTimeStudent, isTransferStudent, receivesFunding)
		else if (choice >= 15 && choice <= 17) {
			System.out.print("Enter the new value (true/false): ");
			boolean newBooleanValue = scnr.nextBoolean();
		
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
			int newCredits = scnr.nextInt();
			student.setCurNumCredits(newCredits);
		}
		
		// Choice 19: Personal Statement (String)
		else if (choice == 19) {
			scnr.nextLine(); 
			System.out.print("Enter the new personal statement: ");
			String newStatement = scnr.nextLine();
			student.setPersonalStatement(newStatement);
		}


	}

}
