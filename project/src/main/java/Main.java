import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
   
     public static void main(String[] args) {

      
            // Creating a student
            Studentt student = new Studentt("ZN", "ZN@Gmail.com");
    
            // Creating a course
            Courset course1 = new Courset("Math 101", 3);
            course1.addWeeklyMeeting(new WeeklyMeeting(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(3, 0))); // Adding a weekly meeting to the course
            course1.addWeeklyMeeting(new WeeklyMeeting(DayOfWeek.MONDAY, LocalTime.of(2, 0), LocalTime.of(4, 0)));

            // Creating a course
            Courset course2 = new Courset("Math 101", 3);
            course2.addWeeklyMeeting(new WeeklyMeeting(DayOfWeek.MONDAY, LocalTime.of(1, 40), LocalTime.of(2, 0)));

            // Creating a course
            Courset course3 = new Courset("English", 3);
            course3.addWeeklyMeeting(new WeeklyMeeting(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0)));

            // Creating a course
            Courset course5 = new Courset("Arabic120", 3);
            course5.addWeeklyMeeting(new WeeklyMeeting(DayOfWeek.SUNDAY, LocalTime.of(1, 0), LocalTime.of(2, 0)));
  
            // Adding the courses to the student's schedule
            student.addCourse(course1);
            student.addCourse(course2);
            student.addCourse(course3);
            student.addCourse(course5);

            // Displaying the student's weekly schedule
            student.printWeeklySchedule();
            student.completeCourse(course1, 0.0);
            student.completeCourse(course3, 3.0);
            student.completeCourse(course5, 2.2);
 
            
            System.out.println(student.calculateGPA()); 
            System.out.println(student.getStudentStatus()); 

            // Creating a faculty
             Faculty fac1=new Faculty("Mohammad", "123@gmail.com");

             // Creating a course
             Courset course4 = new Courset("Math 141", 3);
             course4.addWeeklyMeeting(new WeeklyMeeting(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(11, 0)));

             // Adding the courses to the faculty's schedule
             fac1.addTeachingCourse(course4);
             fac1.addTeachingCourse(course4); 

             // Displaying the faculty's weekly schedule
             System.out.println(fac1.getWeeklyMeetingSchedule());

//////////////////////
//For GPA

Studentt student2 = new Studentt("ZNN", "ZNN@HII.com");

// Add some sample completed courses
Courset mathCourse = new Courset("Math101", 4);
student2.completeCourse(mathCourse, 3.5);

Courset englishCourse = new Courset("English202", 3);
student2.completeCourse(englishCourse, 4.0);

Courset physicsCourse = new Courset("Physics301", 5);
student2.completeCourse(physicsCourse, 3.0);

// Calculate GPA
double calculatedGPA = student2.calculateGPA();

// Print the calculated GPA
System.out.println("Calculated GPA: " + calculatedGPA);
System.out.println(student2.getStudentStatus()); 

// Compare with the expected GPA 
double expectedGPA = 3.4166666666666665; 

// Check if the calculated GPA matches the expected GPA
if (Math.abs(calculatedGPA - expectedGPA) < 0.01) {
    System.out.println("Test passed!");
} else {
    System.out.println("Test failed!");
}
}













        
     }

