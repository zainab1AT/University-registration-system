import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class StudentTest {
    private Studentt student;
    private List<Studentt> testStudents;
    private List<Courset> testCourses;
//For method hasCompletedCourse
    @Before
    public void setUp() {
        testStudents = new ArrayList<>();
        testCourses = new ArrayList<>();
        readTestDataFromFile("src/test/resources/stuudentf.txt");
        student = new Studentt("Nour", "202109947@bethlehem.edu");
    }

    private void readTestDataFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            br.lines()
              .map(line -> line.split("\\|"))
              .forEach(parts -> {
                  if (parts.length == 2) {
                      Studentt testStudent = new Studentt(parts[0], parts[1]);
                      testStudents.add(testStudent);
                  } else if (parts.length == 3) {
                      Courset testCourse = new Courset(parts[0], Integer.parseInt(parts[1]));
                      testCourses.add(testCourse);
                  }
              });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    @Test
    public void testHasCompletedCourse() {
        testStudents.forEach(testStudent -> 
            testCourses.forEach(testCourse -> {
                System.out.println("Testing course completion for student "
                        + testStudent.getName() + ": " + testCourse.getName());
    
                // Simulate completing a course for the student
                testStudent.completeCourse(testCourse, 90.0);
    
                assertTrue(testStudent.hasCompletedCourse(testCourse));
            })
        );
    }
    
    //---------------------------------------------------------------------------

//For completeCourse method 
    @Before
    public void setUp2() {
        // Read test data from the text file
        readTestDataFromFile2("src/test/resources/c.txt");

    }

    private void readTestDataFromFile2(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            br.lines()
              .map(line -> line.split(","))
              .filter(parts -> parts.length == 2)
              .forEach(parts -> {
                  Courset testCourse = new Courset(parts[0], Integer.parseInt(parts[1]));
                  student.enrollInCourse(testCourse);
              });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
public void testCompleteCourse() {
    student.getCurrentCourses().forEach(testCourse -> {
        System.out.println("Testing course completion for: " + testCourse.getName());
        student.completeCourse(testCourse, 90.0);
        assertTrue(student.hasCompletedCourse(testCourse));
        System.out.println("Course completion test passed for: " + testCourse.getName());
    });
}


    //--------------------------------------------------------------------------------------

   @Before
    public void setUp3() {
        // Read test data from the text file
        readTestDataFromFile3("src/test/resources/c.txt");
    }

    private void readTestDataFromFile3(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            br.lines()
              .map(line -> line.split(","))
              .filter(parts -> parts.length == 2)
              .forEach(parts -> {
                  Courset testCourse = new Courset(parts[0], Integer.parseInt(parts[1]));
                  student.enrollInCourse(testCourse);
              });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    @Test
public void testEnrollInCourse() {
    student.getCurrentCourses().forEach(testCourse -> {
        System.out.println("Testing enrollment in course: " + testCourse.getName());
        student.enrollInCourse(testCourse);
        // Print a message based on the enrollment status
        if (student.getCurrentCourses().contains(testCourse)) {
            System.out.println("Course enrollment test passed for: " + testCourse.getName());
        } else {
            System.out.println("Course enrollment test failed for: " + testCourse.getName());
        }
    });
}

    //-----------------------------------------------------------
    //
    
    @Before
    public void setUp4() {
        testStudents = new ArrayList<>();
        testCourses = new ArrayList<>();
        readTestDataFromFile("src/test/resources/print.txt");
    }

    
    public void readTestDataFromFile4(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            br.lines()
              .map(line -> line.split("\\|"))
              .forEach(parts -> {
                  if (parts.length == 2) {
                      Studentt testStudent = new Studentt(parts[0], parts[1]);
                      testStudents.add(testStudent);
                  } else if (parts.length == 3) {
                      Courset testCourse = new Courset(parts[0], Integer.parseInt(parts[1]));
                      testCourses.add(testCourse);
                  }
              });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    @Test
    public void testDropCourse() {
        // Enroll the student in a course for testing
        Courset testCourse = new Courset("SWER141", 3);
        student.enrollInCourse(testCourse);

        // Drop the enrolled course
        System.out.println("This course dropped for student " + student.getName() +
                ": " + testCourse.getName());
        student.dropCourse(testCourse);

        // Verify that the student is not currently enrolled in the dropped course
        assertFalse(student.getCurrentCourses().contains(testCourse));
    }
//---------------------------------------------------------------------
//For CalculateGPA
@Test
public void testCalculateGPA() {
    Studentt student = new Studentt("Nour", "Nour@Gmail.com");

    try (BufferedReader reader = new BufferedReader(new FileReader("src/test/resources/gpa.txt"))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            String courseName = data[0].trim();
            double grade = Double.parseDouble(data[1].trim());
            int credits = Integer.parseInt(data[2].trim());

            // Create a test course
            Courset course = new Courset(courseName, credits);

            // Add the completed course to the student
            student.completeCourse(course, grade);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    // Calculate GPA
    double calculatedGPA = student.calculateGPA();

    // Set the expected GPA
    double expectedGPA = 3.4157894736842107;

    // Assert that the calculated GPA matches the expected GPA with a small delta for floating-point precision
    assertEquals(expectedGPA, calculatedGPA, 0.01); 
}
}