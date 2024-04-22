import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.junit.Test;

public class SemesterTest {
    private static final String TEST_VALUES = "src/test/resources/semester.txt";
        private static final String TEST_VALUES2= "src/test/resources/semester2.txt";

    //For addCourse method 
    @Test
    public void testAddCourse() {
        try (BufferedReader reader = new BufferedReader(new FileReader(TEST_VALUES))) {
            String line;
            Semestert semester = null;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                
                // If semester is not initialized or semester name changes, create a new semester
                if (semester == null || !data[0].equals(semester.getName())) {
                    String semesterName = data[0];
                    int year = Integer.parseInt(data[1]);
                    semester = new Semestert(semesterName, year);
                }

                // Read course  information
                String courseName = data[2];
                int credits = Integer.parseInt(data[3]);

                Courset course = new Courset(courseName, credits);

                // Add course using the addCourse method
                semester.addCourse(course);

                // Assert that the course is added to the semester
                assertTrue(semester.getCourses().contains(course));

                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //-------------------------------------------------------------------------
    @Test
    public void testGetCourseScheduleFromFile() throws IOException {
        // Create a sample semester
        Semestert semester = new Semestert("Spring", 2023);

        // Read data from the text file
        try (BufferedReader reader = new BufferedReader(new FileReader(TEST_VALUES2))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                
                if ("Semester".equals(values[0])) {
                    // It's a semester record
                    semester = new Semestert(values[1], Integer.parseInt(values[2]));
                } else {
                    // It's a course record
                    String courseName = values[0];
                    int credits = Integer.parseInt(values[1]);
                    DayOfWeek dayOfWeek = DayOfWeek.valueOf(values[2].toUpperCase());
                    
                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
                    LocalTime startTime = LocalTime.parse(values[3], timeFormatter);           
                    LocalTime endTime = LocalTime.parse(values[4]);


                    Courset course = new Courset(courseName, credits);
                    WeeklyMeeting meeting = new WeeklyMeeting(dayOfWeek, startTime, endTime);
                    course.addWeeklyMeeting(meeting);

                    semester.addCourse(course);
                    
                }
            }
        }

        // Test the getCourseSchedule method
        List<WeeklyMeeting> schedule = semester.getCourseSchedule();

        assertNotNull(schedule);
        // Adjust the expected size based on the number of courses in the test data
        assertEquals(4, schedule.size());
    }
}
