package mock;

import java.util.*;
import java.io.*;
import java.util.stream.*;

// ============= INTERFACES =============

interface IStudent {
    String getName();
    String getRollNo();
    void addMark(String subject, int mark);
    Map<String, Integer> getMarks();
    double getAverage();
    String getGrade(); // A>=90, B>=75, C>=60, D>=40, F<40
}

interface IGradeBook {
    void enrollStudent(IStudent s);
    IStudent findByRollNo(String rollNo);
    List<IStudent> getToppers();
    List<IStudent> getAllStudents();
    void exportToFile(String filename) throws IOException;
    void importFromFile(String filename) throws IOException;
    int getTotalStudents();
}

// ============= IMPLEMENTATIONS =============

class Student implements IStudent {
    String name;
    String rollNo;
    Map<String, Integer> marks = new HashMap<>();

    public Student(String rollNo, String name) {
        this.rollNo = rollNo;
        this.name = name;
    }

    @Override
    public String getName() { return name; }

    @Override
    public String getRollNo() { return rollNo; }

    @Override
    public void addMark(String subject, int mark) {
        // TODO
    }

    @Override
    public Map<String, Integer> getMarks() {
        // TODO: return defensive copy
        return null;
    }

    @Override
    public double getAverage() {
        // TODO
        return 0;
    }

    @Override
    public String getGrade() {
        // TODO: A>=90, B>=75, C>=60, D>=40, F<40
        return null;
    }

    public String toCSV() {
        // TODO: return "rollNo,name,subject1:mark1;subject2:mark2"
        return null;
    }

    public static Student fromCSV(String line) {
        // TODO: parse CSV and return Student
        return null;
    }
}

class GradeBook implements IGradeBook {
    List<IStudent> students = new ArrayList<>();

    @Override
    public void enrollStudent(IStudent s) {
        // TODO
    }

    @Override
    public IStudent findByRollNo(String rollNo) {
        // TODO
        return null;
    }

    @Override
    public List<IStudent> getToppers() {
        // TODO: return students with grade A
        return null;
    }

    @Override
    public List<IStudent> getAllStudents() {
        // TODO: return defensive copy
        return null;
    }

    @Override
    public void exportToFile(String filename) throws IOException {
        // TODO: write each student as CSV line
    }

    @Override
    public void importFromFile(String filename) throws IOException {
        // TODO: read CSV lines and populate students list
    }

    @Override
    public int getTotalStudents() {
        // TODO
        return 0;
    }
}

// ============= MAIN =============

public class _16_StudentGradeBook {
    public static void main(String[] args) throws IOException {
        IGradeBook gradeBook = new GradeBook();

        // 1. Enroll 3 students and verify count

        // 2. Add marks to a student and verify average

        // 3. Grade calculation — verify A grade student

        // 4. Grade calculation — verify F grade student

        // 5. findByRollNo — found case

        // 6. findByRollNo — not found (null check)

        // 7. getToppers() returns only grade A students

        // 8. getAllStudents() returns defensive copy

        // 9. Export to file — verify file is created

        // 10. Clear gradebook, import from file, verify count restored

        // 11. Imported student has correct marks after round-trip

        // 12. Load from non-existent file — graceful handling

        // 13. getToppers() returns empty list when no A grade students
    }
}