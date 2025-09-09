import java.time.LocalDate;

public class RegularMember extends GymMember {
    private static final int ATTENDANCE_LIMIT = 30;
    private boolean eligibleForUpgrade;
    private String membershipPlan; // Basic, Standard, Deluxe
    private double price;
    private String referralSource;
    private String removalReason;
    
    // Plan prices
    private static final double BASIC_PRICE = 6500.0;
    private static final double STANDARD_PRICE = 12500.0;
    private static final double DELUXE_PRICE = 18500.0;
    
    // Constructor
    public RegularMember(String id, String name, String phoneNumber, String email, 
                         String gender, LocalDate dateOfBirth, LocalDate membershipStartDate,
                         String membershipPlan, String referralSource) {
        super(id, name, phoneNumber, email, gender, dateOfBirth, membershipStartDate);
        this.membershipPlan = membershipPlan;
        this.referralSource = referralSource;
        this.eligibleForUpgrade = false;
        this.removalReason = "";
        
        // Set price based on membership plan
        setPriceBasedOnPlan();
    }
    
    // Set price based on membership plan
    private void setPriceBasedOnPlan() {
        switch (membershipPlan.toLowerCase()) {
            case "basic":
                this.price = BASIC_PRICE;
                break;
            case "standard":
                this.price = STANDARD_PRICE;
                break;
            case "deluxe":
                this.price = DELUXE_PRICE;
                break;
            default:
                this.price = BASIC_PRICE; // Default to basic if invalid plan
                this.membershipPlan = "Basic";
                break;
        }
    }
    
    // Mark attendance for regular member
    @Override
    public void markAttendance() {
        if (activeStatus) {
            attendanceCount++;
            loyaltyPoints += 5; // 5 loyalty points per visit
            
            // Check if attendance limit reached
            if (attendanceCount >= ATTENDANCE_LIMIT) {
                eligibleForUpgrade = true;
                System.out.println(name + " has reached the attendance limit and is eligible for an upgrade.");
            }
            
            System.out.println("Attendance marked for " + name + ". Total attendance: " + attendanceCount);
        } else {
            System.out.println("Cannot mark attendance. Membership is not active for " + name);
        }
    }
    
    // Upgrade plan
    public boolean upgradePlan(String newPlan) {
        String currentPlan = membershipPlan.toLowerCase();
        String upgradePlan = newPlan.toLowerCase();
        
        // Validate upgrade path (cannot downgrade)
        if ((currentPlan.equals("basic") && (upgradePlan.equals("standard") || upgradePlan.equals("deluxe"))) ||
            (currentPlan.equals("standard") && upgradePlan.equals("deluxe"))) {
            
            this.membershipPlan = newPlan;
            setPriceBasedOnPlan();
            System.out.println(name + "'s plan upgraded to " + newPlan);
            return true;
        } else {
            System.out.println("Invalid upgrade path. Cannot downgrade or upgrade to the same plan.");
            return false;
        }
    }
    
    // Revert regular member
    public void revertRegularMember(String reason) {
        this.removalReason = reason;
        resetMember();
        System.out.println("Regular member reverted. Reason: " + reason);
    }
    
    // Display regular member details
    @Override
    public void display() {
        super.display();
        System.out.println("Member Type: Regular");
        System.out.println("Membership Plan: " + membershipPlan);
        System.out.println("Price: " + price);
        System.out.println("Referral Source: " + referralSource);
        System.out.println("Eligible for Upgrade: " + (eligibleForUpgrade ? "Yes" : "No"));
        if (!removalReason.isEmpty()) {
            System.out.println("Removal Reason: " + removalReason);
        }
    }
    
    // Getters and Setters
    public String getMembershipPlan() {
        return membershipPlan;
    }
    
    public double getPrice() {
        return price;
    }
    
    public String getReferralSource() {
        return referralSource;
    }
    
    public boolean isEligibleForUpgrade() {
        return eligibleForUpgrade;
    }
    
    public String getRemovalReason() {
        return removalReason;
    }
    
    public void setMembershipPlan(String membershipPlan) {
        this.membershipPlan = membershipPlan;
        setPriceBasedOnPlan();
    }
    
    public void setReferralSource(String referralSource) {
        this.referralSource = referralSource;
    }
    
    public void setEligibleForUpgrade(boolean eligibleForUpgrade) {
        this.eligibleForUpgrade = eligibleForUpgrade;
    }
    
    public void setRemovalReason(String removalReason) {
        this.removalReason = removalReason;
    }
    
    // Method to convert to string for file storage
    public String toFileString() {
        return "REGULAR," + id + "," + name + "," + phoneNumber + "," + email + "," + gender + "," +
               dateOfBirth.format(DATE_FORMATTER) + "," + membershipStartDate.format(DATE_FORMATTER) + "," +
               attendanceCount + "," + loyaltyPoints + "," + activeStatus + "," +
               membershipPlan + "," + price + "," + referralSource + "," + eligibleForUpgrade + "," + removalReason;
    }
}