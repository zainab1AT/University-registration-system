import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Courset {

 
    private String name;
    private int credits;
    private List<Faculty> faculty;
    private List<WeeklyMeeting> weeklyMeetings;
    private List<Courset> prerequisites;
    private List<Studentt> enrolledStudents;

    public Courset(String name, int credits) {
        this.name = name;
        this.credits = credits;
        this.faculty = new ArrayList<>();
        this.weeklyMeetings = new ArrayList<>();
        this.prerequisites = new ArrayList<>();
        this.enrolledStudents = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getCredits() {
        return credits;
    }

    public List<Faculty> getFaculty() {
        return faculty;
    }

    public void addFaculty(Faculty newFa){
        faculty.add(newFa);
    }

    public List<WeeklyMeeting> getWeeklyMeetings() {
        return weeklyMeetings;
    }

    public List<Courset> getPrerequisites() {
        return prerequisites;
    }

    public List<Studentt> getEnrolledStudents() {
        return enrolledStudents;
    }

    // Method to add a weekly meeting to the course
    public synchronized void addWeeklyMeeting(WeeklyMeeting meeting) {
        // Validate that the faculty does not have a conflicting meeting time
        if (isFacultyAvailable(meeting.getDayOfWeek(), meeting.getStartTime(), meeting.getEndTime())) {
            weeklyMeetings.add(meeting);
            System.out.println("Weekly meeting added successfully.");
        } else {
            System.out.println("Faculty has a conflicting meeting at the same time.");
            // Handle the conflict as needed (throw an exception, show a message, etc.)
        }
    }

    // Method to add a prerequisite course
    public synchronized void addPrerequisite(Courset prerequisite) {
        prerequisites.add(prerequisite);
    }

    // Method to enroll a student in the course
    public synchronized void enroll(Studentt student) {
        if (!enrolledStudents.contains(student) && prerequisitesSatisfied(student)) {
            enrolledStudents.add(student);
            System.out.println(student.getName() + " successfully enrolled in " + name);
        } else {
            System.out.println("Enrollment unsuccessful. Prerequisites not met or already enrolled.");
            // Handle the prerequisites not met or already enrolled situation as needed
        }
    }   

    // Check if the faculty is available during the specified time
    private boolean isFacultyAvailable(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        return weeklyMeetings.stream()
                .noneMatch(meeting ->
                        meeting.getDayOfWeek() == dayOfWeek &&
                        !(endTime.isBefore(meeting.getStartTime()) || startTime.isAfter(meeting.getEndTime())));
    }

    // Check if the prerequisites for the course are satisfied by the student
    private boolean prerequisitesSatisfied(Studentt student) {
        return prerequisites.stream().allMatch(student::hasCompletedCourse);
    }

    public void setCredits(int Credits) {
        this.credits=Credits;
    }

}