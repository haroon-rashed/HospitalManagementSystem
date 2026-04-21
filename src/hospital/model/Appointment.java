package hospital.model;

/**
 * Appointment entity - ENCAPSULATION with private fields
 */
public class Appointment {

    private String appointmentId;
    private String patientId;
    private String patientName;
    private String doctorId;
    private String doctorName;
    private String date;
    private String time;
    private String reason;
    private String status;
    private String notes;

    // Full constructor - FUNCTION OVERLOADING
    public Appointment(String appointmentId, String patientId, String patientName,
                       String doctorId, String doctorName, String date, String time,
                       String reason, String status) {
        this.appointmentId = appointmentId;
        this.patientId     = patientId;
        this.patientName   = patientName;
        this.doctorId      = doctorId;
        this.doctorName    = doctorName;
        this.date          = date;
        this.time          = time;
        this.reason        = reason;
        this.status        = status;
        this.notes         = "";
    }

    // Minimal constructor - FUNCTION OVERLOADING
    public Appointment(String appointmentId, String patientId, String doctorId, String date) {
        this.appointmentId = appointmentId;
        this.patientId     = patientId;
        this.doctorId      = doctorId;
        this.date          = date;
        this.patientName   = "";
        this.doctorName    = "";
        this.time          = "09:00 AM";
        this.reason        = "General Checkup";
        this.status        = "Scheduled";
        this.notes         = "";
    }

    // Getters / Setters
    public String getAppointmentId()                        { return appointmentId; }
    public void   setAppointmentId(String appointmentId)    { this.appointmentId = appointmentId; }

    public String getPatientId()                    { return patientId; }
    public void   setPatientId(String patientId)    { this.patientId = patientId; }

    public String getPatientName()                      { return patientName; }
    public void   setPatientName(String patientName)    { this.patientName = patientName; }

    public String getDoctorId()                   { return doctorId; }
    public void   setDoctorId(String doctorId)    { this.doctorId = doctorId; }

    public String getDoctorName()                     { return doctorName; }
    public void   setDoctorName(String doctorName)    { this.doctorName = doctorName; }

    public String getDate()             { return date; }
    public void   setDate(String date)  { this.date = date; }

    public String getTime()             { return time; }
    public void   setTime(String time)  { this.time = time; }

    public String getReason()               { return reason; }
    public void   setReason(String reason)  { this.reason = reason; }

    public String getStatus()               { return status; }
    public void   setStatus(String status)  { this.status = status; }

    public String getNotes()              { return notes; }
    public void   setNotes(String notes)  { this.notes = notes; }

    @Override
    public String toString() {
        return "Appt#" + appointmentId + " | " + patientName + " -> Dr." +
               doctorName + " | " + date + " " + time + " | " + status;
    }

    public Object[] toTableRow() {
        return new Object[]{ appointmentId, patientName, doctorName, date, time, reason, status };
    }
}
