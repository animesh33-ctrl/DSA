package mock;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Employee implements Comparable<Employee>{
    private int id;
    private String name;
    private String email;
    private int exp;
    private LocalDate dateOfBirth;
    private LocalTime timeOfBirth;
    private long phoneNumber;

    public Employee() {
    }

    public Employee(int id, String name, String email, int exp, LocalDate dateOfBirth, LocalTime timeOfBirth, long phoneNumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.exp = exp;
        this.dateOfBirth = dateOfBirth;
        this.timeOfBirth = timeOfBirth;
        this.phoneNumber = phoneNumber;
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

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalTime getTimeOfBirth() {
        return timeOfBirth;
    }

    public void setTimeOfBirth(LocalTime timeOfBirth) {
        this.timeOfBirth = timeOfBirth;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id == employee.id && exp == employee.exp && phoneNumber == employee.phoneNumber && Objects.equals(name, employee.name) && Objects.equals(email, employee.email) && Objects.equals(dateOfBirth, employee.dateOfBirth) && Objects.equals(timeOfBirth, employee.timeOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, exp, dateOfBirth, timeOfBirth, phoneNumber);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", exp=" + exp +
                ", dateOfBirth=" + dateOfBirth +
                ", timeOfBirth=" + timeOfBirth +
                ", phoneNumber=" + phoneNumber +
                '}';
    }

    @Override
    public int compareTo(@NotNull Employee o) {
        return this.id-o.getId();
    }
}
