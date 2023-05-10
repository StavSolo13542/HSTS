package il.cshaifasweng.OCSFMediatorExample.entities;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StudentGrade {
    private StringProperty name;
    public void setName(String value) { nameProperty().set(value); }
    public String getName() { return nameProperty().get(); }
    public StringProperty nameProperty() {
        if (name == null) name = new SimpleStringProperty(this, "name");
        return name;
    }
    private StringProperty testId;
    public void setTestId(String value) { testIdProperty().set(value); }
    public String getTestId() { return testIdProperty().get(); }
    public StringProperty testIdProperty() {
        if (testId == null) testId = new SimpleStringProperty(this, "testId");
        return testId;
    }
    private StringProperty grade;
    public void setGrade(String value) { gradeProperty().set(value); }
    public String getGrade() { return gradeProperty().get(); }
    public StringProperty gradeProperty() {
        if (grade == null) grade = new SimpleStringProperty(this, "grade");
        return grade;
    }
    public StudentGrade(String name, String test_id, String grade)
    {
        setName(name);
        setTestId(test_id);
        setGrade(grade);
    }
}
