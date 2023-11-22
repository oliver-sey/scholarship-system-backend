
/**
 * A main file, that will run our whole project.
 */

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		BackendSystem backend = new BackendSystem();

		boolean successfulLogin = backend.login();

		// only keep going if it was a successful login
		if (successfulLogin) {
			
		
		}
	}
}
