# scholarship-system-backend
A Java backend for a hypothetical Scholarship Management System (creating scholarship funds, applying, selecting recipients, and more). We also wrote a complete set of software requirements for this backend and also 3 other subsystems, in Jama. We also created a full set of test procedures
to show we fulfilled all of our backend requirements, and presented in an acceptance test review.

Fall 2023, by Anna Fravel, Katelyn McLean, Oliver Seymour, Juliette Nevarez, and MiLee Vogel.

# **A summary:**
This code is just the backend for this hypothetical system, it is not meant to be a complete application. A complete system would also include the Frontend, Matching Engine (between a scholarship and applicants), and a Reports Generation Engine.

Users can run the code from the Main file, which creates an instance of BackendSystem, which actually has all of our business logic.
In Main you can also run various acceptance tests, which show that we successfully implemented all of the backend subsystem requirements that we wrote and refined as part of this project.


To test our backend subsystem, we need test data, which gets imported from text files, in folders such as 'donors', 'matches' (between a student and a scholarship), 'scholarships', and 'students'. 

Within these folders you have, for example, a folder called 'scholarship1', which contains multiple text files with different details about it.
(**More information on the specifics of what information is in each data import file, can be found below.)

The files in these folders get imported and parsed, and the data gets put into various objects, of type Scholarship, StudentProfile, MatchRelationship, and more.


### Student Applicants Qualifications
We are not concerned with whether or not a student that applies to a scholarship actually qualifies for the scholarship, that is not relevant to back end subsystem functionalities. 


# Details of the data import files:

NOTE: you have to be very careful to not have extra blank lines at the end of files, or within the text file somewhere, it will affect the parsing of the data.


## In the Scholarship folder:
Each scholarship has its own folder within 'Scholarship', and within that there are 4 text files with information about the scholarship.

### Scholarship Applicants File (applicants.txt):
student name 1, student name 2, etc.

### Scholarship Application File (applicants.txt):
Like the other files, there is one property per line. They will be Question1, Question2, etc.

### Scholarship Details File (details.txt):
There is one property per line. In order, they are:
Name, description, donor name, award amount, isApproved, isArchived, isAwarded, dateAdded, and dateDue.
The two dates are in the format YYYY-MM-DD, and they eventually get parsed into LocalDate objects. (This is the format that LocalDate.parse(String) accepts).

### Scholarship Requirements File (requirements.txt):
Like the other files, there is one property per line. They will be Category1, desired value 1 (the value for Category1), Category2, desired value 2, etc.
There has to be an equal number of categories and values, each category needs a value.

If a category accepts multiple values, i.e., you want to allow students from multiple different majors to apply to this scholarship, then you should do Major (newline) major1, and then Major (newline) major2, etc. As if each accepted major is a value for a different category.

Categories are all capitalized. Not written as variables from student profile. Categories are:
Major
Grade Level
GPA
Full Time Student
Graduation Year
Good Standing
Gender
Transfer Student
Currently Receives Funding 
  
## In the Match folder:

### Scholarship Match Info File:
Student name, scholarship name, Match percentage, match index, application status, isActive

### Application File:
Question 1, answer 1, question 2, answer2, etc.  

## In the Donor folder:

### Donor details:
First name, Last name, username, password, secturityAnswer1(str), secturityAnswer2(str), secturityAnswer3(str), 

### Scholarships:
Scholarship name 1, scholarship name 2


## In the Students folder:

### Student Awards File:

TODO: fill this out!!

### Student Details File:
firstName(str), lastName(str), studentID(int), username(str), password(str), major(str), hasAMinor(T/F), minor(str), isUSCitizen(T/F), GPA(fl), inGoodStanding(T/F), hasAdvStanding(T/F), gradeLevel(str), gradMonth(int), gradYear(int), gender(str), isFullTimeStudent(T/F), isTransferStudent(T/F), curNumCredits(int), receivesFunding(T/F), personalStatement(str),securityAnswer1(str), securityAnswer2(str), securityAnswer3(str)

### Student Awards file
List scholarships by name, line by line (str)

### Security Questions:
Mother’s Maiden Name
Mascot of your middle school
Name of the city where you were born