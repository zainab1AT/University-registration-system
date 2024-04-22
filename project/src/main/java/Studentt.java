import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Studentt extends Person {

    private Map<Courset,Double> completedCourses;
    private List<Courset> currentCourses;
    private Double GPA ;
    private String StateOfStudent;

    public Studentt(String name, String contactDetails) {
        super(name, contactDetails);
            this.completedCourses = Collections.synchronizedMap(new HashMap<>());
        this.currentCourses = Collections.synchronizedList(new ArrayList<>());
    }

    public Map<Courset,Double> getCompletedCourses() {
        return completedCourses;
    }

    public List<Courset> getCurrentCourses() {
        return currentCourses;
    }

    public Double getGPA(){
        this.GPA=calculateGPA();
        return calculateGPA();
    }

      public String getState(){
        this.StateOfStudent=getState();
        return getState();
    }

    // Method to check if the student has completed a specific course
    public boolean hasCompletedCourse(Courset course) {
        return  Optional.ofNullable(completedCourses.get(course)).isPresent();
    }
   

    // Method to mark a course as completed for the student
    public void completeCourse(Courset course,Double grade) {
        if (!completedCourses.containsKey(course)) {
            completedCourses.put(course,grade);
            System.out.println(name + " completed the course: " + course.getName());
        } else {
            System.out.println(name + " has already completed the course: " + course.getName());
            // Handle the situation where the course is already completed
        }
    }

    // Method to enroll in a course
    public void enrollInCourse(Courset course) {
        if (!currentCourses.contains(course) && !hasCompletedCourse(course)) {
            currentCourses.add(course);
            System.out.println(name + " enrolled in the course: " + course.getName());
        } else {
            System.out.println(name + " is already enrolled or has completed the course: " + course.getName());
            // Handle the situation where the student is already enrolled or has completed the course
        }
    }

    // Method to drop a course
    public void dropCourse(Courset course) {
        if (currentCourses.contains(course)) {
            currentCourses.remove(course);
            System.out.println(name + " dropped the course: " + course.getName());
        } else {
            System.out.println(name + " is not currently enrolled in the course: " + course.getName());
            // Handle the situation where the student is not enrolled in the course
        }
    }

// Method to print the student's weekly schedule
public void printWeeklySchedule() {
    System.out.println("Weekly Schedule for " + getName() + ":");
    currentCourses.stream()
            .forEach(course -> {
                System.out.println("Course: " + course.getName());
                course.getWeeklyMeetings().forEach(meeting -> {
                    System.out.println("Day: " + meeting.getDayOfWeek() +
                            ", Time: " + meeting.getStartTime() + " - " + meeting.getEndTime());
                });
                System.out.println("-------------------------------");
            });
}


    // Method to add a new course with conflict checking
    public void addCourse(Courset newCourse) {
        if (!hasScheduleConflict(newCourse)) {
            currentCourses.add(newCourse);
            System.out.println("Course added successfully.");
        } else {
            System.out.println("Schedule conflict! The course could not be added.");
            // Handle the conflict as needed (throw an exception, show a message, etc.)
        }
    }

    // Check if there is a schedule conflict with the new course
    public boolean hasScheduleConflict(Courset newCourse) {
        for (Courset existingCourse : currentCourses) {
            if (haveConflictingMeetings(existingCourse, newCourse)) {
                return true;
            }
        }
        return false;
    }

    // Check if two courses have conflicting meetings
private boolean haveConflictingMeetings(Courset course1, Courset course2) {
    return course1.getWeeklyMeetings().stream()
            .anyMatch(meeting1 -> course2.getWeeklyMeetings().stream()
                    .anyMatch(meeting2 -> haveConflictingTimes(meeting1, meeting2, course1, course2)));
}

// Helper method to check if two meetings have conflicting times
private boolean haveConflictingTimes(WeeklyMeeting meeting1, WeeklyMeeting meeting2, Courset course1, Courset course2) {
    if (meeting1.getDayOfWeek() == meeting2.getDayOfWeek() &&
            !(meeting2.getEndTime().isBefore(meeting1.getStartTime()) ||
                    meeting2.getStartTime().isAfter(meeting1.getEndTime()))) {
        System.out.println("Conflict detected between " + course1.getName() + " and " + course2.getName());
        System.out.println("Meeting 1: " + meeting1);
        System.out.println("Meeting 2: " + meeting2);
        return true;
    }
    return false;
}


  // Method to browse available courses
public void browseCourses() {
    currentCourses.stream()
            .forEach(course -> System.out.println(course.getName() + " - " + course.getCredits() + " credits"));
}

    // Method to register a student for a class
    public void registerStudentForClass(Studentt student, Courset course) {
        if (course != null && student != null && course.getPrerequisites().stream().allMatch(student::hasCompletedCourse)) {
            course.enroll(student);
            System.out.println(student.getName() + " successfully registered for " + course.getName());
        } else {
            System.out.println("Registration unsuccessful. Prerequisites not met.");
            // Handle the prerequisites not met situation as needed
        }
    }


    public double calculateGPA() {
        if (completedCourses.isEmpty()) {
            System.out.println(name + " has not completed any courses yet. GPA is not applicable.");
            return 0.0;
        }
    
        double totalCredits = 0.0;
        double totalWeightedPoints = 0.0;
    
        totalCredits = completedCourses.entrySet().parallelStream()
                .mapToDouble(entry -> {
                    Courset course = entry.getKey();
                    double grade = entry.getValue();
                    int credits = course.getCredits();
                    return grade * credits;
                })
                .sum();
    
        totalWeightedPoints = completedCourses.entrySet().parallelStream()
                .mapToDouble(entry -> entry.getKey().getCredits())
                .sum();
    
        if (totalWeightedPoints == 0.0) {
            System.out.println(name + " has completed courses, but credits are not available for GPA calculation.");
            return 0.0;
        }
    
        return totalCredits / totalWeightedPoints;
    }


      // Method to determine student status based on GPA
      public String getStudentStatus() {
        double gpa = calculateGPA();

        if (gpa < 2.0) {
            return "Preparation";
        } else if (gpa < 3.0) {
            return "Not Acceptable";
        } else if (gpa < 3.50) {
            return "Honors";
        } else {
            return "Dean's List ";
        }
    }


}