package hospital.model;

/**
 * Patient - INHERITANCE from Person
 * Demonstrates FUNCTION OVERLOADING (multiple constructors)
 * Demonstrates FUNCTION OVERRIDING (getRole, getDisplayInfo, toString)
 */
public class Patient extends Person {

    private String bloodGroup;
    private String diagnosis;
    private String admissionDate;
    private String ward;
    private String assignedDoctorId;
    private String status;

    // Full constructor - FUNCTION OVERLOADING
    public Patient(String id, String name, int age, String gender,
                   String phone, String email, String bloodGroup,
                   String diagnosis, String admissionDate, String ward,
                   String assignedDoctorId, String status) {
        super(id, name, age, gender, phone, email);
        this.bloodGroup      = bloodGroup;
        this.diagnosis       = diagnosis;
        this.admissionDate   = admissionDate;
        this.ward            = ward;
        this.assignedDoctorId = assignedDoctorId;
        this.status          = status;
    }

    // Partial constructor - FUNCTION OVERLOADING
    public Patient(String id, String name, int age, String gender, String phone) {
        super(id, name, age, gender, phone, "");
        this.bloodGroup      = "Unknown";
        this.diagnosis       = "Pending";
        this.admissionDate   = "";
        this.ward            = "OPD";
        this.assignedDoctorId = "";
        this.status          = "OPD";
    }

    // FUNCTION OVERRIDING
    @Override
    public String getRole() { return "PATIENT"; }

    @Override
    public String getDisplayInfo() {
        return "Patient ID   : " + getId()          + "\n" +
               "Name         : " + getName()         + "\n" +
               "Age / Gender : " + getAge() + " / " + getGender() + "\n" +
               "Phone        : " + getPhone()        + "\n" +
               "Blood Group  : " + bloodGroup        + "\n" +
               "Diagnosis    : " + diagnosis         + "\n" +
               "Admission    : " + admissionDate     + "\n" +
               "Ward         : " + ward              + "\n" +
               "Status       : " + status;
    }

    @Override
    public String toString() {
        return super.toString() + " | Blood:" + bloodGroup + " | Ward:" + ward + " | " + status;
    }

    // FUNCTION OVERLOADING: updateStatus with different params
    public void updateStatus(String status) {
        this.status = status;
    }
    public void updateStatus(String status, String ward) {
        this.status = status;
        this.ward   = ward;
    }

    // Getters / Setters
    public String getBloodGroup()                   { return bloodGroup; }
    public void   setBloodGroup(String bloodGroup)  { this.bloodGroup = bloodGroup; }

    public String getDiagnosis()                    { return diagnosis; }
    public void   setDiagnosis(String diagnosis)    { this.diagnosis = diagnosis; }

    public String getAdmissionDate()                      { return admissionDate; }
    public void   setAdmissionDate(String admissionDate)  { this.admissionDate = admissionDate; }

    public String getWard()             { return ward; }
    public void   setWard(String ward)  { this.ward = ward; }

    public String getAssignedDoctorId()                         { return assignedDoctorId; }
    public void   setAssignedDoctorId(String assignedDoctorId)  { this.assignedDoctorId = assignedDoctorId; }

    public String getStatus()               { return status; }
    public void   setStatus(String status)  { this.status = status; }

    // For JTable row
    public Object[] toTableRow() {
        return new Object[]{ getId(), getName(), getAge(), getGender(),
                getPhone(), bloodGroup, diagnosis, ward, status };
    }
}
