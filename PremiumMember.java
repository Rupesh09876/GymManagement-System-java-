import java.time.LocalDate;

public class PremiumMember extends GymMember {
    private static final double PREMIUM_CHARGE = 50000.0;
    private static final double DISCOUNT_PERCENTAGE = 0.10; // 10% discount
    
    private String personalTrainer;
    private boolean paymentComplete;
    private double paidAmount;
    private double discountAmount;
    
    // Constructor
    public PremiumMember(String id, String name, String phoneNumber, String email, 
                         String gender, LocalDate dateOfBirth, LocalDate membershipStartDate,
                         String personalTrainer) {
        super(id, name, phoneNumber, email, gender, dateOfBirth, membershipStartDate);
        this.personalTrainer = personalTrainer;
        this.paymentComplete = false;
        this.paidAmount = 0.0;
        this.discountAmount = 0.0;
    }
    
    // Mark attendance for premium member
    @Override
    public void markAttendance() {
        if (activeStatus) {
            attendanceCount++;
            loyaltyPoints += 10; // Premium members get 10 loyalty points per visit
            System.out.println("Attendance marked for " + name + ". Total attendance: " + attendanceCount);
        } else {
            System.out.println("Cannot mark attendance. Membership is not active for " + name);
        }
    }
    
    // Pay due amount
    public boolean payDueAmount(double amount) {
        if (amount <= 0) {
            System.out.println("Invalid payment amount. Amount must be greater than 0.");
            return false;
        }
        
        if (paymentComplete) {
            System.out.println("Payment is already complete for " + name);
            return false;
        }
        
        double remainingAmount = PREMIUM_CHARGE - paidAmount;
        
        if (amount > remainingAmount) {
            System.out.println("Payment amount exceeds the remaining due. Adjusting to " + remainingAmount);
            amount = remainingAmount;
        }
        
        paidAmount += amount;
        
        // Check if payment is complete
        if (paidAmount >= PREMIUM_CHARGE) {
            paymentComplete = true;
            System.out.println("Payment completed for " + name);
        } else {
            System.out.println("Payment of " + amount + " received. Remaining due: " + (PREMIUM_CHARGE - paidAmount));
        }
        
        return true;
    }
    
    // Calculate discount
    public double calculateDiscount() {
        if (paymentComplete) {
            discountAmount = PREMIUM_CHARGE * DISCOUNT_PERCENTAGE;
            System.out.println("Discount of " + discountAmount + " applied for " + name);
            return discountAmount;
        } else {
            System.out.println("Cannot apply discount. Payment is not complete for " + name);
            return 0.0;
        }
    }
    
    // Revert premium member
    public void revertPremiumMember() {
        resetMember();
        this.paymentComplete = false;
        this.paidAmount = 0.0;
        this.discountAmount = 0.0;
        System.out.println("Premium member reverted for " + name);
    }
    
    // Display premium member details
    @Override
    public void display() {
        super.display();
        System.out.println("Member Type: Premium");
        System.out.println("Premium Charge: " + PREMIUM_CHARGE);
        System.out.println("Personal Trainer: " + personalTrainer);
        System.out.println("Payment Status: " + (paymentComplete ? "Complete" : "Incomplete"));
        System.out.println("Paid Amount: " + paidAmount);
        System.out.println("Remaining Amount: " + (PREMIUM_CHARGE - paidAmount));
        if (discountAmount > 0) {
            System.out.println("Discount Amount: " + discountAmount);
            System.out.println("Final Amount After Discount: " + (PREMIUM_CHARGE - discountAmount));
        }
    }
    
    // Getters and Setters
    public String getPersonalTrainer() {
        return personalTrainer;
    }
    
    public boolean isPaymentComplete() {
        return paymentComplete;
    }
    
    public double getPaidAmount() {
        return paidAmount;
    }
    
    public double getDiscountAmount() {
        return discountAmount;
    }
    
    public static double getPremiumCharge() {
        return PREMIUM_CHARGE;
    }
    
    public void setPersonalTrainer(String personalTrainer) {
        this.personalTrainer = personalTrainer;
    }
    
    public void setPaymentComplete(boolean paymentComplete) {
        this.paymentComplete = paymentComplete;
    }
    
    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
        if (this.paidAmount >= PREMIUM_CHARGE) {
            this.paymentComplete = true;
        }
    }
    
    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }
    
    // Method to convert to string for file storage
    public String toFileString() {
        return "PREMIUM," + id + "," + name + "," + phoneNumber + "," + email + "," + gender + "," +
               dateOfBirth.format(DATE_FORMATTER) + "," + membershipStartDate.format(DATE_FORMATTER) + "," +
               attendanceCount + "," + loyaltyPoints + "," + activeStatus + "," +
               personalTrainer + "," + paymentComplete + "," + paidAmount + "," + discountAmount;
    }
}