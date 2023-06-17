package il.cshaifasweng.server;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

// TODO: get password from each student, go to test page, get access to principal's response

public class Test
{
    private String testId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int timeLimit;
    private int scorePerQuestion;
    private String teacherName;
    private int startedStudents;
    private int finishedStudents;
    private int failedStudents;

    private static final Map<String, Test> activeTests = new HashMap<>();

    public Test(String testId, int timeLimit, int scorePerQuestion, String teacherName) {
        this.testId = testId;
        this.timeLimit = timeLimit;
        this.scorePerQuestion = scorePerQuestion;
        this.teacherName = teacherName;
    }

    // Method to start the test with the provided code
    public static Test startTest(String testId, String studentCode) {
        Test test = activeTests.get(testId);
        if (test != null && test.startTime == null && test.endTime == null) {
            // Validate the student code here
            // ...

            test.startedStudents++;
            test.startTime = LocalDateTime.now();
            return test;
        }
        return null; // Invalid test ID or test already started/closed
    }

    // Method to close the test
    public void closeTest() {
        if (endTime == null) {
            endTime = LocalDateTime.now();
            // Calculate the duration of the test
            long durationMinutes = java.time.Duration.between(startTime, endTime).toMinutes();
            // Update the number of finished and failed students
            finishedStudents++;
            failedStudents += (startedStudents - finishedStudents);
        }
    }

    // Method to add more time to the test (requires principal approval)
    public boolean addTime(int additionalMinutes, String principalApprovalCode) {
        // Validate the principal approval code here
        // ...

        if (endTime == null) {
            endTime = endTime.plusMinutes(additionalMinutes);
            timeLimit += additionalMinutes;
            return true;
        }
        return false; // Test already closed
    }

    // Getters for test properties

    public String getTestId() {
        return testId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public int getScorePerQuestion() {
        return scorePerQuestion;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public int getStartedStudents() {
        return startedStudents;
    }

    public int getFinishedStudents() {
        return finishedStudents;
    }

    public int getFailedStudents() {
        return failedStudents;
    }

    // Method to create a new test and add it to the active tests
    public static void createTest(String testId, int timeLimit, int scorePerQuestion, String teacherName) {
        Test test = new Test(testId, timeLimit, scorePerQuestion, teacherName);
        activeTests.put(testId, test);
    }

    // Method to retrieve an active test by its ID
    public static Test getActiveTest(String testId) {
        return activeTests.get(testId);
    }
}

