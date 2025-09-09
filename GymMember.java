import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Abstract base class representing a gym member in the Fitness Club Management System
 * 
 * This class provides the common functionality and attributes shared by all types
 * of gym members. It serves as the foundation for specific member types such as
 * RegularMember and PremiumMember.
 * 
 * The class handles basic member information including personal details, membership
 * status, attendance tracking, and loyalty points management. It provides methods
     * for membership activation/deactivation and member data management.
 */
public abstract class GymMember {
    
    protected String id;
    
    protected String name;
    
    protected String phoneNumber;
    
    protected String email;
    
    protected String gender;
    
    protected LocalDate dateOfBirth;
    protected LocalDate membershipStartDate;
    
    protected int attendanceCount;
    
    protected int loyaltyPoints;
    
    protected boolean activeStatus;
    
    protected static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    /**
     * Constructor for creating a new gym member
     * 
     * Initializes a new gym member with the provided personal information.
     * Sets default values for attendance count (0), loyalty points (0),
     * and membership status (inactive).
     */
    public GymMember(String id, String name, String phoneNumber, String email,
                     String gender, LocalDate dateOfBirth, LocalDate membershipStartDate) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.membershipStartDate = membershipStartDate;
        this.attendanceCount = 0;
        this.loyaltyPoints = 0;
        this.activeStatus = false;
    }
    
    /**
     * Abstract method for marking attendance
     * 
     * This method must be implemented by all subclasses to define
     * specific attendance marking behavior for different member types.
     * Each member type may have different rules for attendance tracking
     * and loyalty point calculation.
     */
    public abstract void markAttendance();
    
    /**
     * Activates the membership for this member
     * 
     * Sets the membership status to active, allowing the member to
     * access gym facilities and services. Prints a confirmation message
     * to the console.
     */
    public void activateMembership() {
        this.activeStatus = true;
        System.out.println("Membership activated for " + name);
    }
    
    
    public void deactivateMembership() {
        if (this.activeStatus) {
            this.activeStatus = false;
            System.out.println("Membership deactivated for " + name);
        } else {
            System.out.println("Membership is already inactive for " + name);
        }
    }
    
    
    public void resetMember() {
        this.attendanceCount = 0;
        this.loyaltyPoints = 0;
        this.activeStatus = false;
        System.out.println("Member details reset for " + name);
    }
    
    public void display() {
        System.out.println("Member ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Phone Number: " + phoneNumber);
        System.out.println("Email: " + email);
        System.out.println("Gender: " + gender);
        System.out.println("Date of Birth: " + dateOfBirth.format(DATE_FORMATTER));
        System.out.println("Membership Start Date: " + membershipStartDate.format(DATE_FORMATTER));
        System.out.println("Attendance Count: " + attendanceCount);
        System.out.println("Loyalty Points: " + loyaltyPoints);
        System.out.println("Active Status: " + (activeStatus ? "Active" : "Inactive"));
    }
    
   
    public String getId() {
        return id;
    }
    
    
    public String getName() {
        return name;
    }
    
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getGender() {
        return gender;
    }
    
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    
    
    public LocalDate getMembershipStartDate() {
        return membershipStartDate;
    }
    
    
    public int getAttendanceCount() {
        return attendanceCount;
    }
    
    
    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }
    
    
    public boolean isActiveStatus() {
        return activeStatus;
    }
    
    
    public void setId(String id) {
        this.id = id;
    }
    
    
    public void setName(String name) {
        this.name = name;
    }
    
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    

    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    

    public void setMembershipStartDate(LocalDate membershipStartDate) {
        this.membershipStartDate = membershipStartDate;
    }
    
    public void setAttendanceCount(int attendanceCount) {
        this.attendanceCount = attendanceCount;
    }
    
    
    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }
    
    /**
     * Sets the membership active status
     * 
     * This method allows direct setting of the membership status,
     * typically used when loading data from files or programmatically
     * changing status without console output.
     */
    public void setActiveStatus(boolean activeStatus) {
        this.activeStatus = activeStatus;
    }
}