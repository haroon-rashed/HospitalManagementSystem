package hospital.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Doctor - INHERITANCE from Person
 * FUNCTION OVERLOADING via multiple constructors and calculateFee()
 */
public class Doctor extends Person {

    private String specialization;
    private String qualification;
    private String department;
    private String availability;
    private double consultationFee;
    private List<String> assignedPatientIds;

    // Full constructor
    public Doctor(String id, String name, int age, String gender,
                  String phone, String email, String specialization,
                  String qualification, String department,
                  String availability, double consultationFee) {
        super(id, name, age, gender, phone, email);
        this.specialization    = specialization;
        this.qualification     = qualification;
        this.department        = department;
        this.availability      = availability;
        this.consultationFee   = consultationFee;
        this.assignedPatientIds = new ArrayList<>();
    }

    // Minimal constructor - FUNCTION OVERLOADING
    public Doctor(String id, String name, String specialization, String department) {
        super(id, name, 0, "N/A", "", "");
        this.specialization    = specialization;
        this.department        = department;
        this.qualification     = "";
        this.availability      = "Full-time";
        this.consultationFee   = 0;
        this.assignedPatientIds = new ArrayList<>();
    }

    // FUNCTION OVERRIDING
    @Override
    public String getRole() { return "DOCTOR"; }

    @Override
    public String getDisplayInfo() {
        return "Doctor ID      : " + getId()             + "\n" +
               "Name           : Dr. " + getName()       + "\n" +
               "Age / Gender   : " + getAge() + " / " + getGender() + "\n" +
               "Phone          : " + getPhone()          + "\n" +
               "Specialization : " + specialization      + "\n" +
               "Qualification  : " + qualification       + "\n" +
               "Department     : " + department          + "\n" +
               "Availability   : " + availability        + "\n" +
               "Fee (Rs.)      : " + consultationFee;
    }

    @Override
    public String toString() {
        return super.toString() + " | " + department + " | " + specialization +
               " | Rs." + consultationFee;
    }

    // FUNCTION OVERLOADING: calculateFee with 0, 1, 2 parameters
    public double calculateFee()                           { return consultationFee; }
    public double calculateFee(int visits)                 { return consultationFee * visits; }
    public double calculateFee(int visits, double discount){ return consultationFee * visits * (1 - discount / 100); }

    public void assignPatient(String patientId) {
        if (!assignedPatientIds.contains(patientId)) assignedPatientIds.add(patientId);
    }

    // Getters / Setters
    public String getSpecialization()                         { return specialization; }
    public void   setSpecialization(String specialization)    { this.specialization = specialization; }

    public String getQualification()                          { return qualification; }
    public void   setQualification(String qualification)      { this.qualification = qualification; }

    public String getDepartment()                             { return department; }
    public void   setDepartment(String department)            { this.department = department; }

    public String getAvailability()                           { return availability; }
    public void   setAvailability(String availability)        { this.availability = availability; }

    public double getConsultationFee()                              { return consultationFee; }
    public void   setConsultationFee(double consultationFee)        { this.consultationFee = consultationFee; }

    public List<String> getAssignedPatientIds()               { return assignedPatientIds; }

    public Object[] toTableRow() {
        return new Object[]{ getId(), getName(), getAge(), getGender(), getPhone(),
                specialization, qualification, department, availability,
                "Rs. " + (int) consultationFee };
    }
}
