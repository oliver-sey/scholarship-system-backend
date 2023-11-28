import java.util.Arrays;

/**
 * An abstract class for a Profile, that will be the parent class for
 * StudentProfile and more
 */
public abstract class Profile {
	protected String firstName;
	protected String lastName;
	protected String username;
	protected String password;
	protected int clearanceLevel;
	protected int fileIndex;

	// the questions and the answers for the 3 security questions
	// setting the length to 3 because we know there will be 3 questions
	protected String[] securityQuestions = {"Your mother's maiden name?", "You middle school's mascot?", "The city you were born in?"};
	protected String[] securityQAnswers = new String[3];

	// Clearance Level?


	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public String getName() {
		return this.firstName + " " + this.lastName;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public int getFileIndex() {
		return this.fileIndex;
	}

	public String[] getAllSecurityQuestions() {
		return this.securityQuestions;
	}

	/*
	 * @param questionNum - the question number for which you want to get the
	 *                    question (not the answer).
	 *                    This should be 1, 2, or 3
	 * @return the String text of the question that's at index (questionNum + 1)
	 */
	public String getOneSecurityQuestion(int questionNum) {
		// subtract 1 because for inputs 1, 2, 3 we want the elements at index 0, 1, 2
		return this.securityQuestions[questionNum - 1];
	}

	public String[] getAllSecurityQAnswers() {
		return this.securityQAnswers;
	}

	/*
	 * @param questionNum - the question number for which you want to get the answer
	 *                    This should be 1, 2, or 3
	 * @return the String text of the answer that's at index (questionNum + 1)
	 */
	public String getOneSecurityQAnswer(int questionNum) {
		// same as in getOneSecurityQuestion() above, subtract 1 because for inputs 1, 2, 3 we want the elements at index 0, 1, 2
		return this.securityQAnswers[questionNum - 1];
	}

	// remember that there might be restrictions on what values we want to allow the
	// user to set their name, username, etc. to

	public void setFirstName(String inputFirstName) {
		this.firstName = inputFirstName;
	}

	public void setLastName(String inputLastName) {
		this.lastName = inputLastName;
	}

	public void setUsername(String inputUsername) {
		this.username = inputUsername;
	}

	public void setPassword(String inputPassword) {
		this.password = inputPassword;
	}

	public void setAllSecurityQuestions(String[] securityQuestions) {
		this.securityQuestions = securityQuestions;
	}

	/*
	 * @param questionNum - the question number for which you want to set the
	 *                    question (not the answer).
	 *                    This should be 1, 2, or 3
	 */
	public void setOneSecurityQuestion(int questionNum, String questionText) {
		this.securityQuestions[questionNum - 1] = questionText;
	}

	public void setAllSecurityQAnswers(String[] securityQAnswers) {
		this.securityQAnswers = securityQAnswers;
	}

	/*
	 * @param questionNum - the question number for which you want to set the answer
	 *                    This should be 1, 2, or 3
	 */
	public void setOneSecurityQAnswer(int questionNum, String answerText) {
		this.securityQAnswers[questionNum - 1] = answerText;
	}

	@Override
	public String toString() {
		return "firstName=" + firstName + ", lastName=" + lastName + ", username=" + username + ", password="
				+ password + ", clearanceLevel=" + clearanceLevel + ", fileIndex=" + fileIndex + ", securityQuestions="
				+ Arrays.toString(securityQuestions) + ", securityQAnswers=" + Arrays.toString(securityQAnswers);
	}
}
