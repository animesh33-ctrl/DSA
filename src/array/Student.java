package array;

import java.time.LocalDate;
import java.util.Objects;

public class Student implements Comparable<Student>{
    private int id;
    private String name;
    private String email;
    private String gender;
    private LocalDate dob;

    public Student(){}

    public Student(int id, String name, String email, String gender, LocalDate dob) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.dob = dob;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return getId() == student.getId() && Objects.equals(getName(), student.getName()) && Objects.equals(getEmail(), student.getEmail()) && Objects.equals(getGender(), student.getGender()) && Objects.equals(getDob(), student.getDob());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getEmail(), getGender(), getDob());
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", dob=" + dob +
                '}';
    }


    @Override
    public int compareTo(Student o) {
        return Integer.compare(o.getId(),this.getId());
    }
}