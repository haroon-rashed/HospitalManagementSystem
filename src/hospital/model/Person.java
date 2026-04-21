package hospital.model;

/**
 * Abstract base class - demonstrates ABSTRACTION and ENCAPSULATION
 */
public abstract class Person {

    // ENCAPSULATION: private fields with getters/setters
    private String id;
    private String name;
    private int age;
    private String gender;
    private String phone;
    private String email;

    public Person(String id, String name, int age, String gender, String phone, String email) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
    }

    public Person() {}

    // ABSTRACTION: abstract methods subclasses must implement
    public abstract String getRole();
    public abstract String getDisplayInfo();

    // Getters and Setters (ENCAPSULATION)
    public String getId()              { return id; }
    public void   setId(String id)     { this.id = id; }

    public String getName()            { return name; }
    public void   setName(String name) { this.name = name; }

    public int    getAge()             { return age; }
    public void   setAge(int age)      { this.age = age; }

    public String getGender()              { return gender; }
    public void   setGender(String gender) { this.gender = gender; }

    public String getPhone()             { return phone; }
    public void   setPhone(String phone) { this.phone = phone; }

    public String getEmail()             { return email; }
    public void   setEmail(String email) { this.email = email; }

    // POLYMORPHISM: overridden in subclasses
    @Override
    public String toString() {
        return String.format("[%s] ID:%s | Name:%s | Age:%d | Gender:%s | Phone:%s",
                getRole(), id, name, age, gender, phone);
    }
}
