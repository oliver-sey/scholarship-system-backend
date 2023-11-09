import java.util.HashMap;
import java.util.StringTokenizer;

public class StudentProfile extends Profile {
	/* "Requirement text: "The Back End system shall store the  name, Student ID, selected major, selected minor if applicable, 
	citizenship statuses, GPA, academic standing, grade level, expected graduation date, gender, 
	student demographics, type of student, number of currently enrolled units, 
	whether or not they already receive funding, and a 500 word maximum personal statement." */ 

	// firstName and lastName are already in Profile
	
	private int studentID;
	private String major;
	private String minor;
	// TODO: do we need this??
	private boolean hasAMinor;

	// TODO: what do we do for citizenship statuses?
	private boolean isUSCitizen;

	private float GPA;

	// good standing, bad standing, advanced standing
	private boolean inGoodStanding;
	private boolean hasAdvStanding;

	private String gradeLevel;

	private int gradMonth;
	private int gradYear;

	private String gender;

	// TODO: what do we do for 'student demographics'?
	private HashMap<String, String> extraDemographics = new HashMap<String, String>();

	// for "type of student"
	private boolean isFullTimeStudent;
	private boolean isTransferStudent;

	private int curNumCredits;

	// whether or not a student already receives scholarship funding
	private boolean receivesFunding;

	// a 500-word max personal statement
	private String personalStatement;


	// getters

	// a getter for studentID
	public int getStudentID() {
		return this.studentID;
	}

	// a getter for major
	public String getMajor() {
		return this.major;
	}

	// a getter for minor
	public String getMinor() {
		return this.minor;
	}

	// a getter for hasAMinor
	public boolean getHasAMinor() {
		return this.hasAMinor;
	}

	// a getter for boolean isUSCitizen
	public boolean getIsUSCitizen() {
		return this.isUSCitizen;
	}

	// a getter for GPA
	public float getGPA() {
		return this.GPA;
	}

	// a getter for inGoodStanding
	public boolean getInGoodStanding() {
		return this.inGoodStanding;
	}

	// a getter for hasAdvStanding
	public boolean getHasAdvStanding() {
		return this.hasAdvStanding;
	}

	// a getter for gradeLevel
	public String getGradeLevel() {
		return this.gradeLevel;
	}

	// a getter for gradMonth
	public int getGradMonth() {
		return this.gradMonth;
	}

	// a getter for gradYear
	public int getGradYear() {
		return this.gradYear;
	}

	// a getter for gender
	public String getGender() {
		return this.gender;
	}

	// a getter for isFullTimeStudent
	public boolean getIsFullTimeStudent() {
		return this.isFullTimeStudent;
	}

	// a getter for isTransferStudent
	public boolean getIsTransferStudent() {
		return this.isTransferStudent;
	}

	// a getter for curNumCredits
	public int getCurNumCredits() {
		return this.curNumCredits;
	}

	// a getter for receivesFunding
	public boolean getReceivesFunding() {
		return this.receivesFunding;
	}

	// a getter for personalStatement
	public String getPersonalStatement() {
		return this.personalStatement;
	}



	// setters
	// TODO: **** eventually add more to the setters to control what values can be set!

	// a setter for int studentID
	public void studentID(int inputStudentID) {
		this.studentID = inputStudentID;
	}

	// a setter for String major
	public void major(String inputMajor) {
		this.major = inputMajor;
	}

	// a setter for String minor
	public void minor(String inputMinor) {
		this.minor = inputMinor;
	}

	// a setter for boolean hasAMinor
	public void hasAMinor(boolean inputHasAMinor) {
		this.hasAMinor = inputHasAMinor;
	}

	// a getter for boolean isUSCitizen
	public void getIsUSCitizen(boolean inputIsUSCitizen) {
		this.isUSCitizen = inputIsUSCitizen;
	}

	// a setter for float GPA
	public void GPA(float inputGPA) {
		// TODO: don't let them set a GPA below 0.0 or above 4.0
		// have to be careful with comparing floats since sometimes you store 3.0 and it ends up as 3.000004000
		this.GPA = inputGPA;
	}

	// a setter for boolean inGoodStanding
	public void inGoodStanding(boolean inputInGoodStanding) {
		this.inGoodStanding = inputInGoodStanding;
	}

	// a setter for boolean hasAdvStanding
	public void hasAdvStanding(boolean inputHasAdvStanding) {
		this.hasAdvStanding = inputHasAdvStanding;
	}

	// a setter for String gradeLevel
	public void gradeLevel(String inputGradeLevel) {
		this.gradeLevel = inputGradeLevel;
	}

	// a setter for int gradMonth
	public void gradMonth(int inputGradMonth) {
		if (inputGradMonth < 1 || inputGradMonth > 12) {
			throw new IllegalArgumentException("gradMonth must be >= 1 and <= 12");
		}
		
		this.gradMonth = inputGradMonth;
	}

	// a setter for int gradYear
	public void gradYear(int inputGradYear) {
		this.gradYear = inputGradYear;
	}

	// a setter for String gender
	public void gender(String inputGender) {
		this.gender = inputGender;
	}

	// a setter for boolean isFullTimeStudent
	public void isFullTimeStudent(boolean inputIsFullTimeStudent) {
		this.isFullTimeStudent = inputIsFullTimeStudent;
	}

	// a setter for boolean isTransferStudent
	public void isTransferStudent(boolean inputIsTransferStudent) {
		this.isTransferStudent = inputIsTransferStudent;
	}

	// a setter for int curNumCredits
	public void curNumCredits(int inputCurNumCredits) {
		// TODO: what is a reasonable lower and upper limit for a number of credits?
		if (inputCurNumCredits < 0 || inputCurNumCredits > 35) {
			throw new IllegalArgumentException("curNumCredits must be >= 0 and <= 35");
		}
		this.curNumCredits = inputCurNumCredits;
	}

	// a setter for boolean receivesFunding
	public void receivesFunding(boolean inputReceivesFunding) {
		this.receivesFunding = inputReceivesFunding;
	}

	// a setter for String personalStatement
	public void personalStatement(String inputPersonalStatement) {
		// count the actual number of words, since the requirement said max 500 words
		int numWords = new StringTokenizer(inputPersonalStatement, " ").countTokens();

		if (numWords > 500) {
			throw new IllegalArgumentException("personalStatement must be <= 500 words (gets counted as space-separated tokens)");
		}
		this.personalStatement = inputPersonalStatement;
	}

}
