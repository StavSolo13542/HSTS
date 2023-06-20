package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Grade;

import java.util.List;

public class StudentGradesEvent {
    private List<Grade> grades;

    public List<Grade> getGrades() {
        return grades;
    }

    public StudentGradesEvent(List<Grade> grades) {this.grades = grades;}
}
