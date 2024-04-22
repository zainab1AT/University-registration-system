
    import java.time.DayOfWeek;
    import java.time.LocalTime;
    import java.util.ArrayList;
    import java.util.List;
import java.util.stream.Collectors;

    public class Faculty extends Person {

        private List<Courset> teachingCourses;
        private List<WeeklyMeeting> facultyMeetings;

        public Faculty(String name, String contactDetails) {
            super(name, contactDetails);
            this.teachingCourses = new ArrayList<>();
        }

        public List<Courset> getTeachingCourses() {
            return teachingCourses;
        }

        
        // Check if the faculty is available during the specified time
        public boolean isAvailable(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
            // Implement the logic to check faculty availability here

            // For simplicity, let's assume the faculty is always available if they have no conflicting meetings
            return facultyMeetings.stream()
                    .noneMatch(meeting ->
                            meeting.getDayOfWeek() == dayOfWeek &&
                                    !(endTime.isBefore(meeting.getStartTime()) || startTime.isAfter(meeting.getEndTime()))
                    );
        }


        // Method to add a course to the faculty's teaching schedule
        public void addTeachingCourse(Courset course) {
            if (!teachingCourses.contains(course)) {
                teachingCourses.add(course);
                System.out.println(getName() + " is now teaching the course: " + course.getName());
            } else {
                System.out.println(getName() + " is already teaching the course: " + course.getName());
                // Handle the situation where the faculty is already teaching the course
            }
        }

        // Method to remove a course from the faculty's teaching schedule
        public void removeTeachingCourse(Courset course) {
            if (teachingCourses.contains(course)) {
                teachingCourses.remove(course);
                System.out.println(getName() + " stopped teaching the course: " + course.getName());
            } else {
                System.out.println(getName() + " is not currently teaching the course: " + course.getName());
                // Handle the situation where the faculty is not teaching the course
            }
        }

   // Method to get the weekly meeting schedule for the faculty
public List<WeeklyMeeting> getWeeklyMeetingSchedule() {
    return teachingCourses.stream()
            .flatMap(course -> course.getWeeklyMeetings().stream())
            .collect(Collectors.toList());
}


// Check if there is a schedule conflict with the new course
public boolean hasScheduleConflict(Courset newCourse) {
    for (Courset existingCourse : teachingCourses) {
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
teachingCourses.stream()
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