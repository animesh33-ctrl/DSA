package hashing;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.Objects;

public class Employee implements Comparable<Employee>{

    private int employeeld;
    private String employeename;
    private String gender;
    private LocalDate dob;
    private int exp;
    private String email;
    private long phonenumber;

    public Employee() {
    }

    public Employee(int employeeld, String gender, String employeename, LocalDate dob, int exp, String email, long phonenumber) {
        this.employeeld = employeeld;
        this.gender = gender;
        this.employeename = employeename;
        this.dob = dob;
        this.exp = exp;
        this.email = email;
        this.phonenumber = phonenumber;
    }

    public int getEmployeeld() {
        return employeeld;
    }

    public void setEmployeeld(int employeeld) {
        this.employeeld = employeeld;
    }

    public String getEmployeename() {
        return employeename;
    }

    public void setEmployeename(String employeename) {
        this.employeename = employeename;
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

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(long phonenumber) {
        this.phonenumber = phonenumber;
    }

    @Override
    public boolean equals(Object o) {
//        if (o == null || getClass() != o.getClass()) return false;
//        Employee employee = (Employee) o;
//        return employeeld == employee.employeeld && exp == employee.exp && phonenumber == employee.phonenumber && Objects.equals(employeename, employee.employeename) && Objects.equals(gender, employee.gender) && Objects.equals(dob, employee.dob) && Objects.equals(email, employee.email);
        System.out.println("Equals method invoked");
        return true;
    }

    @Override
    public int hashCode() {
        System.out.println("Hashcode method invoked");
        return Objects.hash(employeeld, employeename, gender, dob, exp, email, phonenumber);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeld=" + employeeld +
                ", employeename='" + employeename + '\'' +
                ", gender='" + gender + '\'' +
                ", dob=" + dob +
                ", exp=" + exp +
                ", email='" + email + '\'' +
                ", phonenumber=" + phonenumber +
                '}';
    }

    public static void main(String[] args) {

    }

    @Override
    public int compareTo(@NotNull Employee o) {
        System.out.println("Compare To method");
        return this.getEmployeeld()-o.getEmployeeld();
    }
}
