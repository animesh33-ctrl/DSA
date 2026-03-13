package mock;

import java.util.*;
import java.util.stream.Collectors;

class Doctor {
    String doctorId;
    String name;
    String specialization;
    boolean isAvailable;

    public Doctor() {
    }

    public Doctor(String doctorId, String name, String specialization){
        this.doctorId=doctorId;
        this.name = name;
        this.specialization = specialization;
    }

    public Doctor(String doctorId,String name,String specialization,boolean isAvailable){
        this(doctorId,name,specialization);
        this.isAvailable = isAvailable;
    }

}

class Patient {
    String patientId;
    String name;
    int age;
    String bloodGroup;

    public Patient() {
    }

    public Patient(String patientId, String name, int age, String bloodGroup) {
        this.patientId = patientId;
        this.name = name;
        this.age = age;
        this.bloodGroup = bloodGroup;
    }
}

class Appointment {
    String appointmentId;
    Patient patient;
    Doctor doctor;
    String date;           // format: "YYYY-MM-DD"
    AppointmentStatus status;
    double consultationFee;

    public Appointment() {}

    public Appointment(String appointmentId, Patient patient, Doctor doctor,
                       String date, AppointmentStatus status, double consultationFee) {
        this.appointmentId = appointmentId;
        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
        this.status = status;
        this.consultationFee = consultationFee;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId='" + appointmentId + '\'' +
                ", patient=" + patient.name +
                ", doctor=" + doctor.name +
                ", date='" + date + '\'' +
                ", status=" + status +
                ", fee=" + consultationFee +
                '}';
    }
}

enum AppointmentStatus { SCHEDULED, COMPLETED, CANCELLED }

interface IHospitalSystem {
    // Register a doctor; print "[name] registered successfully" or "[name] already exists"
    void registerDoctor(Doctor doctor);
    // Register a patient; print "[name] registered successfully" or "[name] already exists"
    void registerPatient(Patient patient);
    /**
     * Book an appointment:
     * - Doctor must exist and be available
     * - Patient must exist
     * - A doctor cannot have more than 3 appointments on the same date (across all statuses)
     * - If valid: assign appointmentId ("APT1", "APT2", ...), status = SCHEDULED
     * - Print appropriate messages for each failure case
     * - Return appointmentId on success, null on failure
     */
    String bookAppointment(String patientId, String doctorId, String date);

    /**
     * Cancel an appointment:
     * - Only SCHEDULED appointments can be cancelled
     * - On cancel: set status = CANCELLED, mark doctor as available
     * - Print "[appointmentId] cancelled" or appropriate error
     */
    void cancelAppointment(String appointmentId);

    /**
     * Complete an appointment:
     * - Only SCHEDULED appointments can be completed
     * - On complete: set status = COMPLETED
     * - Print "[appointmentId] completed"
     */
    void completeAppointment(String appointmentId);

    // Return total fees collected from all COMPLETED appointments for a given doctor
    double getTotalRevenueByDoctor(String doctorId);

    // Return list of all SCHEDULED appointments for a given date (sorted by appointmentId)
    List<Appointment> getScheduledAppointmentsForDate(String date);

    // Return the doctor who has the most COMPLETED appointments; null if none
    Doctor getMostBusyDoctor();
}

public class _11_HospitalSystem implements IHospitalSystem {

    Map<String,Doctor> doctors = new HashMap<>();
    Map<String,Patient> patients = new HashMap<>();
    Map<String,Appointment> appointmentMap = new HashMap<>();
    int id = 1;
    String OFFSET = "APPOINTMENT";

    @Override
    public void registerDoctor(Doctor doctor) {
        if (doctors.putIfAbsent(doctor.doctorId, doctor) == null)
            System.out.println(doctor.name + " registered successfully");
        else
            System.out.println(doctor.name + " already exists");
    }

    @Override
    public void registerPatient(Patient patient) {
        if (patients.putIfAbsent(patient.patientId, patient) == null)
            System.out.println(patient.name + " registered successfully");
        else
            System.out.println(patient.name + " already exists");
    }

    @Override
    public String bookAppointment(String patientId, String doctorId, String date) {
        Doctor doctor = doctors.get(doctorId);
        Patient patient = patients.get(patientId);

        if (doctor == null || !doctor.isAvailable) {
            System.out.println("Doctor not found or unavailable: " + doctorId);
            return null;
        }
        if (patient == null) {
            System.out.println("Patient not found: " + patientId);
            return null;
        }

        // Max 3 appointments per doctor per date
        long count = appointmentMap.values().stream()
                .filter(a -> a.doctor.doctorId.equals(doctorId) && a.date.equals(date))
                .count();
        if (count >= 3) {
            System.out.println("Doctor " + doctorId + " fully booked on " + date);
            return null;
        }

        String aptId = "APT" + id++;
        appointmentMap.put(aptId, new Appointment(aptId, patient, doctor,
                date, AppointmentStatus.SCHEDULED, 500.0));
        System.out.println("Appointment booked: " + aptId);
        return aptId;
    }

    @Override
    public void cancelAppointment(String appointmentId) {
        Appointment a = appointmentMap.get(appointmentId);
        if (a == null) {
            System.out.println("Appointment not found: " + appointmentId);
            return;
        }
        if (a.status != AppointmentStatus.SCHEDULED) {
            System.out.println(appointmentId + " cannot be cancelled (status: " + a.status + ")");
            return;
        }
        a.status = AppointmentStatus.CANCELLED;
        a.doctor.isAvailable = true;
        System.out.println(appointmentId + " cancelled");
    }

    @Override
    public void completeAppointment(String appointmentId) {
        Appointment a = appointmentMap.get(appointmentId);
        if (a == null) {
            System.out.println("Appointment not found: " + appointmentId);
            return;
        }
        if (a.status != AppointmentStatus.SCHEDULED) {
            System.out.println(appointmentId + " cannot be completed (status: " + a.status + ")");
            return;
        }
        a.status = AppointmentStatus.COMPLETED;
        System.out.println(appointmentId + " completed");
    }

    @Override
    public double getTotalRevenueByDoctor(String doctorId) {
        return appointmentMap.values().stream()
                .filter(a -> a.doctor.doctorId.equals(doctorId)
                        && a.status == AppointmentStatus.COMPLETED)
                .mapToDouble(a -> a.consultationFee)
                .sum();
    }

    @Override
    public List<Appointment> getScheduledAppointmentsForDate(String date) {
        return appointmentMap.values().stream().filter(obj -> obj.date.equals(date) &&
                obj.status==AppointmentStatus.SCHEDULED).collect(Collectors.toList());

    }

    @Override
    public Doctor getMostBusyDoctor() {
        return appointmentMap.values().stream()
                .filter(a -> a.status == AppointmentStatus.COMPLETED)
                .collect(Collectors.groupingBy(
                        a -> a.doctor,
                        Collectors.counting()
                ))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }
}

class HospitalTest {
    public static void main(String[] args) {

        IHospitalSystem system = new _11_HospitalSystem();

        System.out.println("========== REGISTER DOCTORS ==========");
        Doctor d1 = new Doctor();
        d1.doctorId = "D001"; d1.name = "Dr. Sharma";
        d1.specialization = "Cardiology"; d1.isAvailable = true;

        Doctor d2 = new Doctor();
        d2.doctorId = "D002"; d2.name = "Dr. Verma";
        d2.specialization = "Neurology"; d2.isAvailable = true;

        system.registerDoctor(d1);
        system.registerDoctor(d2);
        system.registerDoctor(d1); // duplicate

        System.out.println("\n========== REGISTER PATIENTS ==========");
        Patient p1 = new Patient();
        p1.patientId = "P001"; p1.name = "Animesh";
        p1.age = 25; p1.bloodGroup = "O+";

        Patient p2 = new Patient();
        p2.patientId = "P002"; p2.name = "Rahul";
        p2.age = 30; p2.bloodGroup = "A+";

        Patient p3 = new Patient();
        p3.patientId = "P003"; p3.name = "Priya";
        p3.age = 28; p3.bloodGroup = "B+";

        system.registerPatient(p1);
        system.registerPatient(p2);
        system.registerPatient(p3);
        system.registerPatient(p2); // duplicate

        System.out.println("\n========== BOOK APPOINTMENTS ==========");
        String apt1 = system.bookAppointment("P001", "D001", "2025-06-10");
        System.out.println("Booked: " + apt1); // APT1

        String apt2 = system.bookAppointment("P002", "D001", "2025-06-10");
        System.out.println("Booked: " + apt2); // APT2

        String apt3 = system.bookAppointment("P003", "D001", "2025-06-10");
        System.out.println("Booked: " + apt3); // APT3

        // Doctor D001 already has 3 on same date — should FAIL
        String apt4 = system.bookAppointment("P002", "D001", "2025-06-10");
        System.out.println("Booked (should be null): " + apt4);

        // Invalid patient
        String apt5 = system.bookAppointment("P999", "D001", "2025-06-11");
        System.out.println("Booked (should be null): " + apt5);

        // Invalid doctor
        String apt6 = system.bookAppointment("P001", "D999", "2025-06-11");
        System.out.println("Booked (should be null): " + apt6);

        // Valid new date — should succeed
        String apt7 = system.bookAppointment("P001", "D001", "2025-06-11");
        System.out.println("Booked: " + apt7); // APT4 (counter continues)

        System.out.println("\n========== COMPLETE & CANCEL ==========");
        system.completeAppointment(apt1);                    // valid
        system.cancelAppointment(apt2);                      // valid cancel
        system.completeAppointment(apt2);                    // should fail - already CANCELLED
        system.cancelAppointment("APT999");                  // invalid id
        system.completeAppointment(apt1);                    // should fail - already COMPLETED

        System.out.println("\n========== REVENUE BY DOCTOR ==========");
        // Assume consultation fee is set when booking (you can hardcode 500.0 per appointment)
        System.out.println("Revenue D001: " + system.getTotalRevenueByDoctor("D001"));
        System.out.println("Revenue D002: " + system.getTotalRevenueByDoctor("D002")); // 0

        System.out.println("\n========== SCHEDULED FOR DATE ==========");
        List<Appointment> scheduled = system.getScheduledAppointmentsForDate("2025-06-10");
        for (Appointment a : scheduled) {
            System.out.println(a.appointmentId + " | " + a.patient.name + " | " + a.status);
        }
        // Expected: Only APT3 (APT1=COMPLETED, APT2=CANCELLED)

        System.out.println("\n========== MOST BUSY DOCTOR ==========");
        Doctor busy = system.getMostBusyDoctor();
        System.out.println("Most busy: " + (busy != null ? busy.name : "None"));
        // Expected: Dr. Sharma (1 completed)

        System.out.println("\n========== FINAL CHECK ==========");
        System.out.println("If outputs match expectations → SYSTEM IS CORRECT");
        System.out.println("\n🎉 ALL TESTS COMPLETED 🎉");
    }
}
