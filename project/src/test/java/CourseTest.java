import org.junit.Test;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public class CourseTest {

    private static final String TEST_VALUES = "src/test/resources/coursefile.txt";
        private static final String TEST_VALUES2 = "src/test/resources/c.txt";

//For addWeeklyMeeting method
    @Test
    public void testAddWeeklyMeetingNoConflict() {
        // Reading values from the file for test 1
        Object[] objects = readCourseAndMeetingFromTestValues(1);
        Courset course = (Courset) objects[0];
        Faculty faculty = (Faculty) objects[1];
        WeeklyMeeting meeting = (WeeklyMeeting) objects[2];

        // Expecting successful addition
        course.addWeeklyMeeting(meeting);
        assertTrue(course.getWeeklyMeetings().contains(meeting));
    }

    @Test
    public void testAddWeeklyMeetingWithConflict() {
        // Reading values from the file for test 2
        Object[] objects = readCourseAndMeetingFromTestValues(2);
        Courset course = (Courset) objects[0];
        Faculty faculty = (Faculty) objects[1];
        WeeklyMeeting conflictingMeeting = (WeeklyMeeting) objects[2];
    
        // Adding a conflicting weekly meeting
        WeeklyMeeting existingMeeting = new WeeklyMeeting(DayOfWeek.MONDAY, LocalTime.of(10, 30), LocalTime.of(11, 30));
        course.addWeeklyMeeting(existingMeeting);
    
        // Expecting conflict and no addition
        course.addWeeklyMeeting(conflictingMeeting);
        assertFalse(course.getWeeklyMeetings().contains(conflictingMeeting));
    }



     private Object[] readCourseAndMeetingFromTestValues(int testNumber) {
        try (BufferedReader reader = new BufferedReader(new FileReader(TEST_VALUES))) {
            String line;
            Courset course = null;
            Faculty faculty = null;
            WeeklyMeeting meeting = null;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("# Test " + testNumber)) {
                    Object[] objects = readCourseAndMeeting(reader);
                    course = (Courset) objects[0];
                    faculty = (Faculty) objects[1];
                    meeting = (WeeklyMeeting) objects[2];
                    break;
                }
            }

            if (course != null && faculty != null && meeting != null) {
                return new Object[]{course, faculty, meeting};
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("Test values not found for Test " + testNumber);
    }

     private Object[] readCourseAndMeeting(BufferedReader reader) throws IOException {
        Courset course = null;
        Faculty faculty = null;
        DayOfWeek dayOfWeek = null;
        LocalTime startTime = null;
        LocalTime endTime = null;

        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            String[] parts = line.split("=");
            if (parts.length == 2) {
                String key = parts[0].trim();
                String value = parts[1].trim();
                switch (key) {
                    case "courseName":
                        course = new Courset(value, 0);
                        break;
                    case "credits":
                        course.setCredits(Integer.parseInt(value));
                        break;
                    case "facultyName":
                        faculty = new Faculty(value, null);
                        break;
                    case "facultyContactDetails":
                        if (faculty != null) {
                            faculty.setContactDetails(value);
                            course.addFaculty(faculty);
                        }
                        break;
                    case "dayOfWeek":
                        dayOfWeek = DayOfWeek.valueOf(value);
                        break;
                    case "startTime":
                        startTime = LocalTime.parse(value);
                        break;
                    case "endTime":
                        endTime = LocalTime.parse(value);
                        break;
                }
            }
        }

        if (course != null && faculty != null && dayOfWeek != null && startTime != null && endTime != null) {
            WeeklyMeeting meeting = new WeeklyMeeting(dayOfWeek, startTime, endTime);
            return new Object[]{course, faculty, meeting};
        }

        throw new RuntimeException("Incomplete data for reading course and meeting.");
    }
//////////////////////////////////////////////////////////////////////////////////////////////////

//For enroll method
@Test
    public void testEnrollFromFile() {
        // Assuming the file contains student details in each line (name, contactDetails, courseName)
        Path filePath = Paths.get("src/test/resources/student.txt");

        try {
            List<String> lines = Files.readAllLines(filePath);

            for (String line : lines) {
                String[] parts = line.split(",");
                String name = parts[0].trim();
                String contactDetails = parts[1].trim();
                String courseName = parts[2].trim();

                // Create a student object
                Studentt student = new Studentt(name, contactDetails);

                // Create a course object
                Courset course = new Courset(courseName, 3);

                // Enroll the student in the course
                course.enroll(student);

                // Verify that the student is enrolled in the course
                assertTrue(course.getEnrolledStudents().contains(student));
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail("Error reading student details from file.");
        }
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////// 
//For addPrerequisite method    
@Test
public void testAddPrerequisite() {
    try (BufferedReader reader = new BufferedReader(new FileReader(TEST_VALUES2))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            String courseName = data[0];
            int credits = Integer.parseInt(data[1]);

            Courset course = new Courset(courseName, credits);

            // Add prerequisite using the addPrerequisite method
            Courset prerequisite = new Courset("PrerequisiteCourse", 3);
            course.addPrerequisite(prerequisite);

            // Assert that the prerequisite is added
            assertTrue(course.getPrerequisites().contains(prerequisite));
            
            // Output a message indicating successful test (optional)
            System.out.println("You just added "+ courseName+ " successfully");
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}
