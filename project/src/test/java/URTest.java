import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class URTest {
   private UniversityRegistrationSystem registrationSystem;
       private final PrintStream originalSystemOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
//For addCourse method
    @Before
    public void setUp() {
        registrationSystem = new UniversityRegistrationSystem();
    }
    @Test
    public void testAddCourse() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/test/resources/testDataWithoutConflict.txt"))) {
            reader.lines()
                  .map(line -> line.split(","))
                  .forEach(courseData -> {
                      String courseName = courseData[0];
                      int credits = Integer.parseInt(courseData[1]);
                      DayOfWeek dayOfWeek = DayOfWeek.valueOf(courseData[2].toUpperCase());
                      LocalTime startTime = LocalTime.parse(courseData[3]);
                      LocalTime endTime = LocalTime.parse(courseData[4]);
    
                      Courset newCourse = new Courset(courseName, credits);
                      WeeklyMeeting meeting = new WeeklyMeeting(dayOfWeek, startTime, endTime);
                      newCourse.addWeeklyMeeting(meeting);
    
                      registrationSystem.addCourse(newCourse);
                  });
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        assertFalse(registrationSystem.hasScheduleConflict(new Courset("SwER141", 3))); // Test for no conflict
    
        assertFalse(registrationSystem.hasScheduleConflict(new Courset("Biology", 3))); // Test for no conflict
        
        // Debugging Statements
        Courset conflictCourse = new Courset("ConflictCourse", 3);
        WeeklyMeeting conflictMeeting = new WeeklyMeeting(DayOfWeek.MONDAY, LocalTime.parse("11:00"), LocalTime.parse("13:00"));
        conflictCourse.addWeeklyMeeting(conflictMeeting);
    
        assertTrue(registrationSystem.hasScheduleConflict(conflictCourse)); // Test for conflict
    }
    
//---------------------------------------------------------------------
//For browseCourses method 

@BeforeEach
    void setUp2() {
        registrationSystem = new UniversityRegistrationSystem();
    }

    @Test
    public void testBrowseCoursesFromFile() {
        // Load courses from the file
        try {
            loadCoursesFromFile("src/test/resources/c.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Redirect the standard output to capture the printed text
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        // Call the browseCourses method
        registrationSystem.browseCourses();

        // Verify the output against the expected result
        String expectedOutput = "SWER141 - 3 credits\nSWER348 - 4 credits\nSWER142 - 2 credits";
        assertEquals(expectedOutput.replaceAll("\\s", ""), outputStreamCaptor.toString().trim().replaceAll("\\s", ""));
    }


    private void loadCoursesFromFile(String filePath) throws IOException {
        Files.lines(Path.of(filePath))
             .map(line -> line.split(","))
             .filter(parts -> parts.length == 2)
             .map(parts -> new Courset(parts[0].trim(), Integer.parseInt(parts[1].trim())))
             .forEach(registrationSystem::addCourse);
    }
    
//--------------------------------------------------------------------------
//For registerStudentForClass method
@Before
    public void setUp3() {
        registrationSystem = new UniversityRegistrationSystem();
    }

    @Test
    public void testRegisterStudentForClassFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/test/resources/student.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String studentName = data[0].trim();
                String studentEmail = data[1].trim();
                String courseName = data[2].trim();

                // Create Studentt object
                Studentt student = new Studentt(studentName, studentEmail);
                registrationSystem.addStudent(student);

                // Create Courset object
                Courset course = new Courset(courseName, 3); // Assuming 3 credits for simplicity
                registrationSystem.addCourse(course);

                // Register the student for the class
                registrationSystem.registerStudentForClass(student, course);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Assertions to check the state of the system after registration
        assertEquals(4, registrationSystem.getStudents().size()); // Assuming there were initially 3 students
        assertEquals(0, registrationSystem.getSemesters().size());
        assertEquals(4, registrationSystem.getCourses().size()); // Assuming there were initially 3 courses
        assertTrue(registrationSystem.getCourses().get(0).getEnrolledStudents().contains(registrationSystem.getStudents().get(0)));
    }
    //-----------------------------------------------------------
    //For addStudent method 
    
    @BeforeEach
   public void setUp4() {
        registrationSystem = new UniversityRegistrationSystem();
    }

    @Test
    public void testAddStudentFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/test/resources/student.txt"))) {
            reader.lines()
                  .map(line -> line.split(","))
                  .forEach(data -> {
                      String studentName = data[0].trim();
                      String studentEmail = data[1].trim();
    
                      // Create Studentt object
                      Studentt student = new Studentt(studentName, studentEmail);
                      registrationSystem.addStudent(student);
                      System.out.println("This student has been added: " + student.getName() + " - " + student.getContactDetails());
    
                      // Verify that the student is added correctly
                      assertTrue(registrationSystem.getStudents().contains(student));
                  });
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error reading student data from file.");
        }
        System.out.println("Number of students in the system: " + registrationSystem.getStudents().size());
    
        // Assertions to check the state of the system after adding students
        assertEquals(4, registrationSystem.getStudents().size()); // Assuming there were initially 3 students
        assertEquals(0, registrationSystem.getSemesters().size());
        assertEquals(0, registrationSystem.getCourses().size());
    }
    
//---------------------------------------------------------------------------
//For addFacultyMember method 

@BeforeEach
public void setUp5() {
    registrationSystem = new UniversityRegistrationSystem();
}

@Test
public void testAddFacultyMemberFromFile() {
    try (BufferedReader reader = new BufferedReader(new FileReader("src/test/resources/F2.txt"))) {
        reader.lines()
              .map(line -> line.split(","))
              .forEach(data -> {
                  String facultyName = data[0].trim();
                  String facultyContactDetails = data[1].trim();

                  // Create Faculty object
                  Faculty faculty = new Faculty(facultyName, facultyContactDetails);

                  // Debug information using a logging framework (replace with your preferred logging framework)
                  System.out.println("This faculty has been added: " + faculty.getName() + " - " + faculty.getContactDetails());

                  registrationSystem.addFacultyMember(faculty);

                  // Verify that the faculty member is added correctly
                  assertTrue(registrationSystem.getFacultyMembers().contains(faculty));
              });
    } catch (IOException e) {
        // Log the error using a logging framework
        e.printStackTrace();
        System.err.println("Error reading faculty data from file.");
    }

    // Debug information using a logging framework
    System.out.println("Number of faculty members in the system: " + registrationSystem.getFacultyMembers().size());

    // Assertions to check the state of the system after adding faculty members
    assertEquals(3, registrationSystem.getFacultyMembers().size()); // Assuming there were initially 2 faculty members
}

//---------------------------------------------------------------------
//For addSemester method 

@BeforeEach
public void setUp6() {
    registrationSystem = new UniversityRegistrationSystem();
}
@Test
public void testAddSemesterFromFile() {
    try (BufferedReader reader = new BufferedReader(new FileReader("src/test/resources/semester.txt"))) {
        reader.lines()
              .map(line -> line.split(","))
              .forEach(data -> {
                  String semesterName = data[0].trim();
                  int semesterYear = Integer.parseInt(data[1].trim());
                  String courseName = data[2].trim();
                  int courseCredits = Integer.parseInt(data[3].trim());

                  // Create Semestert object
                  Semestert semester = new Semestert(semesterName, semesterYear);
                  registrationSystem.addSemester(semester);

                  // Optional: Verify that the semester is added correctly
                  assertTrue(registrationSystem.getSemesters().contains(semester));

                  // Create Courset object
                  Courset course = new Courset(courseName, courseCredits);
                  registrationSystem.addCourse(course);

                  // Add the course to the semester
                  semester.addCourse(course);
              });
    } catch (IOException e) {
        e.printStackTrace();
    }

    // Assertions to check the state of the system after adding semesters
    assertEquals(3, registrationSystem.getSemesters().size()); // Assuming there were initially 2 semesters
    assertEquals(3, registrationSystem.getCourses().size()); // Assuming there were initially 0 courses
}

}