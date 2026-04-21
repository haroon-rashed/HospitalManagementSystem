package hospital.service;

import hospital.model.Appointment;
import hospital.model.Doctor;
import hospital.model.Patient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * HospitalService - central data/business layer
 * FUNCTION OVERLOADING on all search methods
 * ENCAPSULATION: data stored privately, exposed via methods only
 */
public class HospitalService {

    private final List<Patient>     patients     = new ArrayList<>();
    private final List<Doctor>      doctors      = new ArrayList<>();
    private final List<Appointment> appointments = new ArrayList<>();

    private int patientCounter     = 1001;
    private int doctorCounter      = 2001;
    private int appointmentCounter = 3001;

    // Singleton
    private static HospitalService instance;
    private HospitalService() { loadSampleData(); }
    public static HospitalService getInstance() {
        if (instance == null) instance = new HospitalService();
        return instance;
    }

    // ═══════════════ PATIENT CRUD ═══════════════

    public String generatePatientId()       { return "P-" + (patientCounter++); }

    public boolean addPatient(Patient p) {
        if (findPatientById(p.getId()) != null) return false;
        patients.add(p);
        return true;
    }

    public boolean updatePatient(Patient updated) {
        for (int i = 0; i < patients.size(); i++) {
            if (patients.get(i).getId().equals(updated.getId())) {
                patients.set(i, updated);
                return true;
            }
        }
        return false;
    }

    public boolean deletePatient(String id) {
        return patients.removeIf(p -> p.getId().equals(id));
    }

    public Patient findPatientById(String id) {
        return patients.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }

    // FUNCTION OVERLOADING - search by name only
    public List<Patient> searchPatients(String name) {
        String q = name.toLowerCase();
        return patients.stream()
                .filter(p -> p.getName().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    // FUNCTION OVERLOADING - search by name + status
    public List<Patient> searchPatients(String name, String status) {
        return patients.stream()
                .filter(p -> p.getName().toLowerCase().contains(name.toLowerCase())
                          && p.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }

    // FUNCTION OVERLOADING - search by name + status + ward
    public List<Patient> searchPatients(String name, String status, String ward) {
        return patients.stream()
                .filter(p -> p.getName().toLowerCase().contains(name.toLowerCase())
                          && (status.isEmpty() || p.getStatus().equalsIgnoreCase(status))
                          && (ward.isEmpty()   || p.getWard().equalsIgnoreCase(ward)))
                .collect(Collectors.toList());
    }

    public List<Patient> getAllPatients() { return new ArrayList<>(patients); }

    // ═══════════════ DOCTOR CRUD ═══════════════

    public String generateDoctorId()        { return "D-" + (doctorCounter++); }

    public boolean addDoctor(Doctor d) {
        if (findDoctorById(d.getId()) != null) return false;
        doctors.add(d);
        return true;
    }

    public boolean updateDoctor(Doctor updated) {
        for (int i = 0; i < doctors.size(); i++) {
            if (doctors.get(i).getId().equals(updated.getId())) {
                doctors.set(i, updated);
                return true;
            }
        }
        return false;
    }

    public boolean deleteDoctor(String id) {
        return doctors.removeIf(d -> d.getId().equals(id));
    }

    public Doctor findDoctorById(String id) {
        return doctors.stream().filter(d -> d.getId().equals(id)).findFirst().orElse(null);
    }

    public List<Doctor> searchDoctors(String name) {
        String q = name.toLowerCase();
        return doctors.stream()
                .filter(d -> d.getName().toLowerCase().contains(q)
                          || d.getSpecialization().toLowerCase().contains(q)
                          || d.getDepartment().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    public List<Doctor> searchDoctors(String name, String department) {
        return doctors.stream()
                .filter(d -> d.getName().toLowerCase().contains(name.toLowerCase())
                          && (department.isEmpty() || d.getDepartment().equalsIgnoreCase(department)))
                .collect(Collectors.toList());
    }

    public List<Doctor> getAllDoctors() { return new ArrayList<>(doctors); }

    // ═══════════════ APPOINTMENT CRUD ═══════════════

    public String generateAppointmentId()   { return "A-" + (appointmentCounter++); }

    public boolean addAppointment(Appointment a) {
        appointments.add(a);
        return true;
    }

    public boolean updateAppointment(Appointment updated) {
        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getAppointmentId().equals(updated.getAppointmentId())) {
                appointments.set(i, updated);
                return true;
            }
        }
        return false;
    }

    public boolean deleteAppointment(String id) {
        return appointments.removeIf(a -> a.getAppointmentId().equals(id));
    }

    public Appointment findAppointmentById(String id) {
        return appointments.stream()
                .filter(a -> a.getAppointmentId().equals(id)).findFirst().orElse(null);
    }

    public List<Appointment> searchAppointments(String query) {
        String q = query.toLowerCase();
        return appointments.stream()
                .filter(a -> a.getPatientName().toLowerCase().contains(q)
                          || a.getDoctorName().toLowerCase().contains(q)
                          || a.getDate().contains(query))
                .collect(Collectors.toList());
    }

    public List<Appointment> getAllAppointments() { return new ArrayList<>(appointments); }

    // ═══════════════ STATS ═══════════════

    public int  getTotalPatients()     { return patients.size(); }
    public int  getTotalDoctors()      { return doctors.size(); }
    public int  getTotalAppointments() { return appointments.size(); }
    public long getAdmittedPatients()  { return patients.stream().filter(p -> p.getStatus().equals("Admitted")).count(); }
    public long getPendingAppts()      { return appointments.stream().filter(a -> a.getStatus().equals("Scheduled")).count(); }

    // ═══════════════ SAMPLE DATA ═══════════════

    private void loadSampleData() {
        doctors.add(new Doctor("D-2001","Ahmed Ali",45,"Male","0300-1234567","ahmed@hms.com",
                "Cardiology","MBBS, FCPS","Cardiology","Morning",2000));
        doctors.add(new Doctor("D-2002","Fatima Khan",38,"Female","0301-2345678","fatima@hms.com",
                "Neurology","MBBS, FCPS Neuro","Neurology","Evening",2500));
        doctors.add(new Doctor("D-2003","Usman Raza",50,"Male","0302-3456789","usman@hms.com",
                "Orthopedics","MBBS, MS Ortho","Orthopedics","Full-time",1800));
        doctors.add(new Doctor("D-2004","Sara Malik",35,"Female","0303-4567890","sara@hms.com",
                "Pediatrics","MBBS, DCH","Pediatrics","Morning",1500));
        doctors.add(new Doctor("D-2005","Bilal Hussain",42,"Male","0304-5678901","bilal@hms.com",
                "General Surgery","MBBS, FCPS Surgery","Surgery","Full-time",3000));

        patients.add(new Patient("P-1001","Muhammad Zain",34,"Male","0311-1111111","zain@email.com",
                "B+","Hypertension","2025-01-10","Ward A","D-2001","Admitted"));
        patients.add(new Patient("P-1002","Aisha Noor",28,"Female","0322-2222222","aisha@email.com",
                "O+","Migraine","2025-01-12","Ward B","D-2002","Admitted"));
        patients.add(new Patient("P-1003","Hamid Shah",60,"Male","0333-3333333","hamid@email.com",
                "A-","Knee Arthritis","2025-01-08","Ward C","D-2003","Discharged"));
        patients.add(new Patient("P-1004","Zara Ali",5,"Female","0344-4444444","zara@email.com",
                "AB+","Fever","2025-01-14","Pediatric","D-2004","OPD"));
        patients.add(new Patient("P-1005","Tariq Mehmood",45,"Male","0355-5555555","tariq@email.com",
                "O-","Appendicitis","2025-01-11","Surgical","D-2005","Admitted"));

        appointments.add(new Appointment("A-3001","P-1001","Muhammad Zain","D-2001","Dr. Ahmed Ali",
                "2025-01-20","10:00 AM","Follow-up","Scheduled"));
        appointments.add(new Appointment("A-3002","P-1002","Aisha Noor","D-2002","Dr. Fatima Khan",
                "2025-01-20","11:30 AM","MRI Review","Scheduled"));
        appointments.add(new Appointment("A-3003","P-1004","Zara Ali","D-2004","Dr. Sara Malik",
                "2025-01-21","09:00 AM","Vaccination","Completed"));
        appointments.add(new Appointment("A-3004","P-1005","Tariq Mehmood","D-2005","Dr. Bilal Hussain",
                "2025-01-22","08:00 AM","Pre-op Check","Scheduled"));
    }
}
