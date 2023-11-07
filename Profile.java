/**
 * An abstract class for a Profile, that will be the parent class for StudentProfile and more
 */
public abstract class Profile {
	private String firstName;
	private String lastName;
	private String username;
	// TODO: is it ok that this is very low security storing the password as plain text??
	private String password;

	// Not sure what data type we should use for the answers to the security questions
	// maybe a dictionary? I think the syntax would be Hashtable<String, String> my_dict = new Hashtable<String, String>();


	// Clearance Level?

	public String getFirstName() {
		return this.firstName;
	};


	public String getLastName() {
		return this.lastName;
	};
	

	public String getusername() {
		return this.username;
	};

	public String getpassword(){
		return this.password;
	}

	// TODO: will we need getters and setters for the password?
	// remember that there might be restrictions on what values we want to allow the user to set their name, username, etc. to

	public void setFirstName(String inputFirstName) {
		this.firstName = inputFirstName;
	};


	public void setLastName(String inputLastName) {
		this.lastName = inputLastName;
	};
	

	public void setUsername(String inputUsername) {
		this.username = inputUsername;
	};

	public void setPassword(String inputPassword) {
		this.password = inputPassword;
	};
}
