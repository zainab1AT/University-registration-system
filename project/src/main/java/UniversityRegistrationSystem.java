import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UniversityRegistrationSystem {

    private List<Studentt> students;
    private List<Faculty> facultyMembers;
    private List<Semestert> semesters;
    private List<Courset> courses;

    public UniversityRegistrationSystem() {
        this.students = Collections.synchronizedList(new ArrayList<>());
        this.facultyMembers = Collections.synchronizedList(new ArrayList<>());
        this.semesters = Collections.synchronizedList(new ArrayList<>());
        this.courses = Collections.synchronizedList(new ArrayList<>());
    }

    public List<Studentt> getStudents() {
        return students;
    }

    public List<Faculty> getFacultyMembers() {
        return facultyMembers;
    }

    public List<Semestert> getSemesters() {
        return semesters;
    }

    public List<Courset> getCourses() {
        return courses;
    }

    // Method to add a new student
    public void addStudent(Studentt student) {
        students.add(student);
    }

    // Method to add a new faculty member
    public void addFacultyMember(Faculty faculty) {
        facultyMembers.add(faculty);
    }

    // Method to add a new semester
    public void addSemester(Semestert semester) {
        semesters.add(semester);
    }

    // Method to add a new course with conflict checking
    public void addCourse(Courset newCourse) {
        if (!hasScheduleConflict(newCourse)) {
            courses.add(newCourse);
            System.out.println("Course added successfully.");
        } else {
            System.out.println("Schedule conflict! The course could not be added.");
            // Handle the conflict as needed (throw an exception, show a message, etc.)
        }
    }

    // Check if there is a schedule conflict with the new course
    public boolean hasScheduleConflict(Courset newCourse) {
        for (Courset existingCourse : courses) {
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
    courses.stream()
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


}