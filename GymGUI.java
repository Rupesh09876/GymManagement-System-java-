import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;
import javax.swing.ListSelectionModel;
import javax.swing.Box;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 * Fitness Club Management System GUI Application
 * 
 * This class provides a comprehensive graphical user interface for managing
 * gym members, including regular and premium memberships. The system supports
 * member registration, attendance tracking, membership management, and file
 * operations for data persistence.
 */
public class GymGUI {
    private static ArrayList<GymMember> members = new ArrayList<>();
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    private static final String MEMBERS_FILE = "members.txt";
    
    private static JFrame mainFrame;
    
    private static JFrame displayFrame;
    
    private static JTabbedPane tabbedPane;
    
    private static JLabel lblId, lblName, lblLocation, lblPhone, lblEmail, lblGender, lblDob;
    private static JLabel lblStartDate, lblMembershipPlan, lblPrice, lblReferralSource;
    private static JLabel lblTrainerName, lblPremiumCharge, lblPaidAmount, lblDiscountAmount;
    private static JLabel lblRemovalReason, lblStatus;
    
    private static JTextField txtId, txtName, txtLocation, txtPhone, txtEmail;
    private static JTextField txtReferralSource, txtPaidAmount, txtRemovalReason, txtTrainerName;
    private static JTextField txtPrice, txtPremiumCharge, txtDiscountAmount;
    
    private static JComboBox<String> dobYearComboBox, dobMonthComboBox, dobDayComboBox;
    private static JComboBox<String> msYearComboBox, msMonthComboBox, msDayComboBox;
    private static JComboBox<String> cbMembershipPlan;
    private static JComboBox<String> cbMemberSelect;
    
    private static JRadioButton rbMale, rbFemale;
    
    private static ButtonGroup bgGender;
    
    private static JButton btnAddRegular, btnAddPremium, btnActivate, btnDeactivate;
    private static JButton btnMarkAttendance, btnUpgradePlan, btnRevertRegular, btnRevertPremium;
    private static JButton btnPayDue, btnCalculateDiscount, btnDisplay, btnSaveToFile, btnReadFromFile, btnClear;
    
    private static JTable tblMembers;
    
    private static DefaultTableModel tableModel;
    
    private static JTextArea txtAreaDisplay;
    
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private static final Color WARNING_COLOR = new Color(241, 196, 15);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color LIGHT_BG_COLOR = new Color(236, 240, 241);
    private static final Color DARK_TEXT_COLOR = new Color(44, 62, 80);

    /**
     * Main method - Entry point of the application
     * 
     * Sets the system look and feel and initializes the GUI on the Event Dispatch Thread
     */
    public static void main(String[] args) {
    try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    /**
     * Creates and displays the main GUI window
     * 
     * Initializes all components, sets up the layout, and makes the window visible.
     * This method is called on the Event Dispatch Thread to ensure thread safety.
     */
    private static void createAndShowGUI() {
        mainFrame = new JFrame("Fitness Club Management System");
        mainFrame.setSize(1100, 800);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());
        
        createHeaderPanel();
        
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        tabbedPane.setBackground(LIGHT_BG_COLOR);
        tabbedPane.setForeground(DARK_TEXT_COLOR);
        
        createMemberManagementTab();
        createMemberListTab();
        createReportsTab();
        
        mainFrame.add(tabbedPane, BorderLayout.CENTER);
        
        createStatusBar();
        
        updateMemberDropdown();
        
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }
    
    /**
     * Creates the header panel with application title and help button
     * 
     * The header panel contains the application title on the left and a help button
     * on the right, styled with the primary color scheme.
     */
    private static void createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(1100, 60));
        
        JLabel titleLabel = new JLabel("Fitness Club Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 0));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(PRIMARY_COLOR);
        
        JButton helpButton = new JButton("Help");
        styleButton(helpButton, SECONDARY_COLOR);
        helpButton.setForeground(Color.WHITE);
        helpButton.addActionListener(e -> showHelp());
        
        rightPanel.add(helpButton);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        mainFrame.add(headerPanel, BorderLayout.NORTH);
    }
    
    /**
     * Creates the member management tab
     * 
     * This tab contains forms for adding regular and premium members,
     * along with various member management actions.
     */
    private static void createMemberManagementTab() {
        JPanel memberPanel = new JPanel(new BorderLayout());
        memberPanel.setBackground(LIGHT_BG_COLOR);
        memberPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBackground(LIGHT_BG_COLOR);
        
        JPanel memberInfoPanel = createMemberInfoPanel();
        formPanel.add(memberInfoPanel, BorderLayout.NORTH);
        
        JPanel membershipPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        membershipPanel.setBackground(LIGHT_BG_COLOR);
        
        JPanel regularPanel = createRegularMembershipPanel();
        membershipPanel.add(regularPanel);
        
        JPanel premiumPanel = createPremiumMembershipPanel();
        membershipPanel.add(premiumPanel);
        
        formPanel.add(membershipPanel, BorderLayout.CENTER);
        
        JPanel actionsPanel = createActionsPanel();
        formPanel.add(actionsPanel, BorderLayout.SOUTH);
        
        memberPanel.add(formPanel, BorderLayout.CENTER);
        
        tabbedPane.addTab("Member Management", null, memberPanel, "Manage gym members");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
    }
    
    /**
     * Creates the member information panel
     * 
     * This panel contains all the basic member information fields including
     * member selection dropdown, personal details, and dates.
     * 
     * @return JPanel containing member information form fields
     */
    private static JPanel createMemberInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
                "Member Information",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                PRIMARY_COLOR
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JLabel lblMemberSelect = new JLabel("Select Member:");
        lblMemberSelect.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        panel.add(lblMemberSelect, gbc);
        
        cbMemberSelect = new JComboBox<>();
        cbMemberSelect.setFont(new Font("Arial", Font.PLAIN, 12));
        cbMemberSelect.addActionListener(e -> handleMemberSelection());
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        panel.add(cbMemberSelect, gbc);
        
        lblId = new JLabel("Member ID* (numbers only):");
        lblId.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(lblId, gbc);
        
        txtId = new JTextField();
        txtId.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(txtId, gbc);
        
        lblName = new JLabel("Name*:");
        lblName.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(lblName, gbc);
        
        txtName = new JTextField();
        txtName.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(txtName, gbc);
        
        lblLocation = new JLabel("Location*:");
        lblLocation.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(lblLocation, gbc);
        
        txtLocation = new JTextField();
        txtLocation.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(txtLocation, gbc);
        
        lblPhone = new JLabel("Phone* (numbers only):");
        lblPhone.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(lblPhone, gbc);
        
        txtPhone = new JTextField();
        txtPhone.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(txtPhone, gbc);
        
        lblEmail = new JLabel("Email* (will add @gmail.com):");
        lblEmail.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panel.add(lblEmail, gbc);
        
        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panel.add(txtEmail, gbc);
        
        lblGender = new JLabel("Gender*:");
        lblGender.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panel.add(lblGender, gbc);
        
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        genderPanel.setBackground(Color.WHITE);
        
        rbMale = new JRadioButton("Male");
        rbMale.setFont(new Font("Arial", Font.PLAIN, 12));
        rbMale.setBackground(Color.WHITE);
        rbFemale = new JRadioButton("Female");
        rbFemale.setFont(new Font("Arial", Font.PLAIN, 12));
        rbFemale.setBackground(Color.WHITE);
        
        bgGender = new ButtonGroup();
        bgGender.add(rbMale);
        bgGender.add(rbFemale);
        rbMale.setSelected(true);
        
        genderPanel.add(rbMale);
        genderPanel.add(Box.createHorizontalStrut(10));
        genderPanel.add(rbFemale);
        
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panel.add(genderPanel, gbc);
        
        lblDob = new JLabel("Date of Birth*:");
        lblDob.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        panel.add(lblDob, gbc);
        
        JPanel dobPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        dobPanel.setBackground(Color.WHITE);
        
        String[] years = getYears(1950, LocalDate.now().getYear());
        dobYearComboBox = new JComboBox<>(years);
        dobYearComboBox.setFont(new Font("Arial", Font.PLAIN, 12));
        
        String[] months = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        dobMonthComboBox = new JComboBox<>(months);
        dobMonthComboBox.setFont(new Font("Arial", Font.PLAIN, 12));
        
        String[] days = getDays(31);
        dobDayComboBox = new JComboBox<>(days);
        dobDayComboBox.setFont(new Font("Arial", Font.PLAIN, 12));
        
        dobPanel.add(dobYearComboBox);
        dobPanel.add(new JLabel("-"));
        dobPanel.add(dobMonthComboBox);
        dobPanel.add(new JLabel("-"));
        dobPanel.add(dobDayComboBox);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        panel.add(dobPanel, gbc);
        
        lblStartDate = new JLabel("Start Date*:");
        lblStartDate.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        panel.add(lblStartDate, gbc);
        
        JPanel startDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        startDatePanel.setBackground(Color.WHITE);
        
        msYearComboBox = new JComboBox<>(years);
        msYearComboBox.setFont(new Font("Arial", Font.PLAIN, 12));
        msYearComboBox.setSelectedItem(String.valueOf(LocalDate.now().getYear()));
        
        msMonthComboBox = new JComboBox<>(months);
        msMonthComboBox.setFont(new Font("Arial", Font.PLAIN, 12));
        msMonthComboBox.setSelectedItem(String.format("%02d", LocalDate.now().getMonthValue()));
        
        msDayComboBox = new JComboBox<>(days);
        msDayComboBox.setFont(new Font("Arial", Font.PLAIN, 12));
        msDayComboBox.setSelectedItem(String.format("%02d", LocalDate.now().getDayOfMonth()));
        
        startDatePanel.add(msYearComboBox);
        startDatePanel.add(new JLabel("-"));
        startDatePanel.add(msMonthComboBox);
        startDatePanel.add(new JLabel("-"));
        startDatePanel.add(msDayComboBox);
        
        gbc.gridx = 3;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        panel.add(startDatePanel, gbc);
        
        lblRemovalReason = new JLabel("Removal Reason:");
        lblRemovalReason.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        panel.add(lblRemovalReason, gbc);
        
        txtRemovalReason = new JTextField();
        txtRemovalReason.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        panel.add(txtRemovalReason, gbc);
        
        return panel;
    }
    
    /**
     * Creates the regular membership panel
     * 
     * This panel contains fields specific to regular membership including
     * membership plan selection, pricing, and referral source.
     * 
     * @return JPanel containing regular membership form fields
     */
    private static JPanel createRegularMembershipPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(SECONDARY_COLOR, 1),
                "Regular Membership",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                SECONDARY_COLOR
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        lblMembershipPlan = new JLabel("Membership Plan*:");
        lblMembershipPlan.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        panel.add(lblMembershipPlan, gbc);
        
        String[] plans = {"Basic", "Standard", "Deluxe"};
        cbMembershipPlan = new JComboBox<>(plans);
        cbMembershipPlan.setFont(new Font("Arial", Font.PLAIN, 12));
        cbMembershipPlan.addActionListener(e -> updatePriceField());
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        panel.add(cbMembershipPlan, gbc);
        
        lblPrice = new JLabel("Price:");
        lblPrice.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(lblPrice, gbc);
        
        txtPrice = new JTextField("6500");
        txtPrice.setFont(new Font("Arial", Font.PLAIN, 12));
        txtPrice.setEditable(false);
        txtPrice.setBackground(new Color(240, 240, 240));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(txtPrice, gbc);
        
        lblReferralSource = new JLabel("Referral Source*:");
        lblReferralSource.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(lblReferralSource, gbc);
        
        txtReferralSource = new JTextField();
        txtReferralSource.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(txtReferralSource, gbc);
        
        btnAddRegular = new JButton("Add Regular Member");
        btnAddRegular.setFont(new Font("Arial", Font.BOLD, 12));
        styleButton(btnAddRegular, SUCCESS_COLOR);
        btnAddRegular.addActionListener(e -> addRegularMember());
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnAddRegular, gbc);
        
        return panel;
    }
    
    /**
     * Creates the premium membership panel
     * 
     * This panel contains fields specific to premium membership including
     * personal trainer assignment, premium charges, and payment information.
     * 
     * @return JPanel containing premium membership form fields
     */
    private static JPanel createPremiumMembershipPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(SECONDARY_COLOR, 1),
                "Premium Membership",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                SECONDARY_COLOR
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        lblTrainerName = new JLabel("Personal Trainer*:");
        lblTrainerName.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        panel.add(lblTrainerName, gbc);
        
        txtTrainerName = new JTextField();
        txtTrainerName.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        panel.add(txtTrainerName, gbc);
        
        lblPremiumCharge = new JLabel("Premium Charge:");
        lblPremiumCharge.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(lblPremiumCharge, gbc);
        
        txtPremiumCharge = new JTextField("50000");
        txtPremiumCharge.setFont(new Font("Arial", Font.PLAIN, 12));
        txtPremiumCharge.setEditable(false);
        txtPremiumCharge.setBackground(new Color(240, 240, 240));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(txtPremiumCharge, gbc);
        
        lblPaidAmount = new JLabel("Payment Amount:");
        lblPaidAmount.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(lblPaidAmount, gbc);
        
        txtPaidAmount = new JTextField();
        txtPaidAmount.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(txtPaidAmount, gbc);
        
        lblDiscountAmount = new JLabel("Discount Amount:");
        lblDiscountAmount.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panel.add(lblDiscountAmount, gbc);
        
        txtDiscountAmount = new JTextField();
        txtDiscountAmount.setFont(new Font("Arial", Font.PLAIN, 12));
        txtDiscountAmount.setEditable(false);
        txtDiscountAmount.setBackground(new Color(240, 240, 240));
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panel.add(txtDiscountAmount, gbc);
        
        btnAddPremium = new JButton("Add Premium Member");
        btnAddPremium.setFont(new Font("Arial", Font.BOLD, 12));
        styleButton(btnAddPremium, SUCCESS_COLOR);
        btnAddPremium.addActionListener(e -> addPremiumMember());
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnAddPremium, gbc);
        
        return panel;
    }
    
    /**
     * Creates the member actions panel
     * 
     * This panel contains buttons for various member management operations
     * such as activation, deactivation, attendance marking, and plan upgrades.
     * 
     * @return JPanel containing action buttons
     */
    private static JPanel createActionsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 5, 10, 10));
        panel.setBackground(LIGHT_BG_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
                "Member Actions",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                PRIMARY_COLOR
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        btnActivate = new JButton("Activate Membership");
        btnActivate.setFont(new Font("Arial", Font.BOLD, 12));
        styleButton(btnActivate, SUCCESS_COLOR);
        btnActivate.addActionListener(e -> activateMembership());
        panel.add(btnActivate);
        
        btnDeactivate = new JButton("Deactivate Membership");
        btnDeactivate.setFont(new Font("Arial", Font.BOLD, 12));
        styleButton(btnDeactivate, DANGER_COLOR);
        btnDeactivate.addActionListener(e -> deactivateMembership());
        panel.add(btnDeactivate);
        
        btnMarkAttendance = new JButton("Mark Attendance");
        btnMarkAttendance.setFont(new Font("Arial", Font.BOLD, 12));
        styleButton(btnMarkAttendance, SUCCESS_COLOR);
        btnMarkAttendance.addActionListener(e -> markAttendance());
        panel.add(btnMarkAttendance);
        
        btnUpgradePlan = new JButton("Upgrade Plan");
        btnUpgradePlan.setFont(new Font("Arial", Font.BOLD, 12));
        styleButton(btnUpgradePlan, SECONDARY_COLOR);
        btnUpgradePlan.addActionListener(e -> upgradePlan());
        panel.add(btnUpgradePlan);
        
        btnClear = new JButton("Clear Fields");
        btnClear.setFont(new Font("Arial", Font.BOLD, 12));
        styleButton(btnClear, Color.LIGHT_GRAY);
        btnClear.setForeground(DARK_TEXT_COLOR);
        btnClear.addActionListener(e -> clearFields());
        panel.add(btnClear);
        
        btnRevertRegular = new JButton("Revert Regular Member");
        btnRevertRegular.setFont(new Font("Arial", Font.BOLD, 12));
        styleButton(btnRevertRegular, DANGER_COLOR);
        btnRevertRegular.addActionListener(e -> revertRegularMember());
        panel.add(btnRevertRegular);
        
        btnRevertPremium = new JButton("Revert Premium Member");
        btnRevertPremium.setFont(new Font("Arial", Font.BOLD, 12));
        styleButton(btnRevertPremium, DANGER_COLOR);
        btnRevertPremium.addActionListener(e -> revertPremiumMember());
        panel.add(btnRevertPremium);
        
        btnPayDue = new JButton("Pay Due Amount");
        btnPayDue.setFont(new Font("Arial", Font.BOLD, 12));
        styleButton(btnPayDue, SECONDARY_COLOR);
        btnPayDue.addActionListener(e -> payDueAmount());
        panel.add(btnPayDue);
        
        btnCalculateDiscount = new JButton("Calculate Discount");
        btnCalculateDiscount.setFont(new Font("Arial", Font.BOLD, 12));
        styleButton(btnCalculateDiscount, SECONDARY_COLOR);
        btnCalculateDiscount.addActionListener(e -> calculateDiscount());
        panel.add(btnCalculateDiscount);
        
        btnDisplay = new JButton("Display Members");
        btnDisplay.setFont(new Font("Arial", Font.BOLD, 12));
        styleButton(btnDisplay, WARNING_COLOR);
        btnDisplay.addActionListener(e -> displayAllMembers());
        panel.add(btnDisplay);
        
        return panel;
    }
    
    /**
     * Creates the member list tab
     * 
     * This tab displays all members in a table format and provides
     * file operations for saving and loading member data.
     */
    private static void createMemberListTab() {
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBackground(LIGHT_BG_COLOR);
        listPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] columnNames = {"ID", "Name", "Type", "Plan/Trainer", "Status", "Attendance", "Loyalty Points"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblMembers = new JTable(tableModel);
        tblMembers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblMembers.getTableHeader().setReorderingAllowed(false);
        tblMembers.setFont(new Font("Arial", Font.PLAIN, 12));
        tblMembers.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tblMembers.setRowHeight(25);
        
        JScrollPane scrollPaneTable = new JScrollPane(tblMembers);
        scrollPaneTable.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
                "Member List",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                PRIMARY_COLOR
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        listPanel.add(scrollPaneTable, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(LIGHT_BG_COLOR);
        
        btnSaveToFile = new JButton("Save to File");
        btnSaveToFile.setFont(new Font("Arial", Font.BOLD, 12));
        styleButton(btnSaveToFile, WARNING_COLOR);
        btnSaveToFile.addActionListener(e -> saveToFile());
        
        btnReadFromFile = new JButton("Read from File");
        btnReadFromFile.setFont(new Font("Arial", Font.BOLD, 12));
        styleButton(btnReadFromFile, WARNING_COLOR);
        btnReadFromFile.addActionListener(e -> readFromFile());
        
        buttonPanel.add(btnSaveToFile);
        buttonPanel.add(btnReadFromFile);
        
        listPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        tabbedPane.addTab("Member List", null, listPanel, "View all members");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
    }
    
    /**
     * Creates the reports tab
     * 
     * This tab provides a text area for displaying detailed member reports
     * and includes a button to generate comprehensive member information.
     */
    private static void createReportsTab() {
        JPanel reportsPanel = new JPanel(new BorderLayout());
        reportsPanel.setBackground(LIGHT_BG_COLOR);
        reportsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        txtAreaDisplay = new JTextArea();
        txtAreaDisplay.setEditable(false);
        txtAreaDisplay.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtAreaDisplay.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(txtAreaDisplay);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
                "Member Reports",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                PRIMARY_COLOR
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        reportsPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(LIGHT_BG_COLOR);
        
        JButton generateReportBtn = new JButton("Generate Report");
        generateReportBtn.setFont(new Font("Arial", Font.BOLD, 12));
        styleButton(generateReportBtn, WARNING_COLOR);
        generateReportBtn.addActionListener(e -> displayAllMembers());
        
        buttonPanel.add(generateReportBtn);
        
        reportsPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        tabbedPane.addTab("Reports", null, reportsPanel, "View member reports");
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
    }
    
    /**
     * Creates the status bar at the bottom of the application
     * 
     * The status bar displays current application status and version information.
     */
    private static void createStatusBar() {
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        statusPanel.setPreferredSize(new Dimension(1100, 25));
        
        lblStatus = new JLabel("Ready");
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 12));
        lblStatus.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        statusPanel.add(lblStatus, BorderLayout.WEST);
        
        JLabel versionLabel = new JLabel("v2.0.0");
        versionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        versionLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        statusPanel.add(versionLabel, BorderLayout.EAST);
        
        mainFrame.add(statusPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Applies consistent styling to buttons
     * 
     * This method sets the background color, removes focus painting and borders,
     * and adds hover effects to buttons for a consistent look and feel.
     */
    private static void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
    }
    
    /**
     * Displays the help dialog with application usage information
     * 
     * Shows a modal dialog containing comprehensive help information
     * about how to use the various features of the application.
     */
    private static void showHelp() {
        JDialog helpDialog = new JDialog(mainFrame, "Help", true);
        helpDialog.setSize(600, 400);
        helpDialog.setLocationRelativeTo(mainFrame);
        
        JPanel helpPanel = new JPanel(new BorderLayout());
        helpPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextArea helpText = new JTextArea();
        helpText.setEditable(false);
        helpText.setLineWrap(true);
        helpText.setWrapStyleWord(true);
        helpText.setFont(new Font("Arial", Font.PLAIN, 12));
        
        helpText.setText("Fitness Club Management System Help\n\n" +
                "Member Management Tab:\n" +
                "- Add new regular or premium members\n" +
                "- All fields marked with * are required\n" +
                "- Member ID must be numbers only\n" +
                "- Phone number must be numbers only\n" +
                "- Email will automatically add @gmail.com\n" +
                "- Activate or deactivate memberships\n" +
                "- Mark attendance for members\n" +
                "- Upgrade membership plans\n" +
                "- Process payments and calculate discounts\n\n" +
                "Member List Tab:\n" +
                "- View all members in a table format\n" +
                "- Save member data to a text file\n" +
                "- Load member data from a text file\n\n" +
                "Reports Tab:\n" +
                "- Generate detailed reports of all members\n\n" +
                "For more information, please contact support.");
        
        JScrollPane scrollPane = new JScrollPane(helpText);
        helpPanel.add(scrollPane, BorderLayout.CENTER);
        
        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.BOLD, 12));
        styleButton(closeButton, PRIMARY_COLOR);
        closeButton.addActionListener(e -> helpDialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeButton);
        helpPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        helpDialog.add(helpPanel);
        helpDialog.setVisible(true);
    }

    /**
     * Generates an array of year strings for combo box population
     * 
     * @param startYear The starting year (inclusive)
     * @param endYear The ending year (inclusive)
     * @return String array containing years from startYear to endYear
     */
    private static String[] getYears(int startYear, int endYear) {
        String[] years = new String[endYear - startYear + 1];
        for (int i = 0; i <= endYear - startYear; i++) {
            years[i] = String.valueOf(startYear + i);
        }
        return years;
    }

    /**
     * Generates an array of day strings for combo box population
     * 
     * @param maxDays The maximum number of days to generate
     * @return String array containing formatted day numbers (01, 02, etc.)
     */
    private static String[] getDays(int maxDays) {
        String[] days = new String[maxDays];
        for (int i = 1; i <= maxDays; i++) {
            days[i-1] = String.format("%02d", i);
        }
        return days;
    }

    /**
     * Updates the price field based on selected membership plan
     * 
     * This method is called when the membership plan combo box selection changes
     * and automatically updates the price field with the corresponding amount.
     */
    private static void updatePriceField() {
        String selectedPlan = (String) cbMembershipPlan.getSelectedItem();
        switch (selectedPlan) {
            case "Basic":
                txtPrice.setText("6500");
                break;
            case "Standard":
                txtPrice.setText("12500");
                break;
            case "Deluxe":
                txtPrice.setText("18500");
                break;
        }
    }

    /**
     * Handles member selection from the dropdown
     * 
     * When a member is selected from the dropdown, this method populates
     * all form fields with the selected member's information.
     */
    private static void handleMemberSelection() {
        int selectedIndex = cbMemberSelect.getSelectedIndex();
        if (selectedIndex > 0) {
            String selectedItem = (String) cbMemberSelect.getSelectedItem();
            String memberId = selectedItem.split(" - ")[0];
            GymMember selectedMember = findMemberById(memberId);
            
            if (selectedMember != null) {
                populateFieldsFromMember(selectedMember);
            }
        }
    }

    /**
     * Populates form fields with data from a selected member
     * 
     * This method fills all relevant form fields with information from
     * the provided member object, handling both regular and premium members.
     * 
     * @param member The GymMember object to populate fields from
     */
    private static void populateFieldsFromMember(GymMember member) {
        txtId.setText(member.getId());
        txtName.setText(member.getName());
        txtLocation.setText("");
        txtPhone.setText(member.getPhoneNumber());
        txtEmail.setText(member.getEmail().replace("@gmail.com", ""));
        
        if (member.getGender().equals("Male")) {
            rbMale.setSelected(true);
        } else {
            rbFemale.setSelected(true);
        }
        
        LocalDate dob = member.getDateOfBirth();
        dobYearComboBox.setSelectedItem(String.valueOf(dob.getYear()));
        dobMonthComboBox.setSelectedItem(String.format("%02d", dob.getMonthValue()));
        dobDayComboBox.setSelectedItem(String.format("%02d", dob.getDayOfMonth()));
        
        LocalDate startDate = member.getMembershipStartDate();
        msYearComboBox.setSelectedItem(String.valueOf(startDate.getYear()));
        msMonthComboBox.setSelectedItem(String.format("%02d", startDate.getMonthValue()));
        msDayComboBox.setSelectedItem(String.format("%02d", startDate.getDayOfMonth()));
        
        if (member instanceof RegularMember) {
            RegularMember regularMember = (RegularMember) member;
            cbMembershipPlan.setSelectedItem(regularMember.getMembershipPlan());
            txtPrice.setText(String.valueOf(regularMember.getPrice()));
            txtReferralSource.setText(regularMember.getReferralSource());
            txtRemovalReason.setText(regularMember.getRemovalReason());
            
            txtTrainerName.setText("");
            txtPaidAmount.setText("");
            txtDiscountAmount.setText("");
            
        } else if (member instanceof PremiumMember) {
            PremiumMember premiumMember = (PremiumMember) member;
            txtTrainerName.setText(premiumMember.getPersonalTrainer());
            txtPaidAmount.setText(String.valueOf(premiumMember.getPaidAmount()));
            txtDiscountAmount.setText(String.valueOf(premiumMember.getDiscountAmount()));
            
            txtReferralSource.setText("");
        }
    }

    /**
     * Updates the member selection dropdown with current members
     * 
     * Refreshes the dropdown list to include all currently registered members
     * with their ID, name, and membership type.
     */
    private static void updateMemberDropdown() {
        cbMemberSelect.removeAllItems();
        cbMemberSelect.addItem("-- Select Member --");
        
        for (GymMember member : members) {
            String memberType = member instanceof RegularMember ? "Regular" : "Premium";
            cbMemberSelect.addItem(member.getId() + " - " + member.getName() + " (" + memberType + ")");
        }
    }

    /**
     * Refreshes the member table with current data
     * 
     * Clears the table and repopulates it with all current members,
     * displaying their key information in a tabular format.
     */
    private static void refreshMemberTable() {
        tableModel.setRowCount(0);
        
        for (GymMember member : members) {
            String memberType = member instanceof RegularMember ? "Regular" : "Premium";
            String planOrTrainer;
            
            if (member instanceof RegularMember) {
                planOrTrainer = ((RegularMember) member).getMembershipPlan();
            } else {
                planOrTrainer = ((PremiumMember) member).getPersonalTrainer();
            }
            
            String status = member.isActiveStatus() ? "Active" : "Inactive";
            
            Object[] row = {
                member.getId(),
                member.getName(),
                memberType,
                planOrTrainer,
                status,
                member.getAttendanceCount(),
                member.getLoyaltyPoints()
            };
            
            tableModel.addRow(row);
        }
        
        updateStatus("Member table refreshed. Total members: " + members.size());
    }

    /**
     * Finds a member by their ID
     * 
     * Searches through the members list to find a member with the specified ID.
     * 
     * @param id The member ID to search for
     * @return The GymMember object if found, null otherwise
     */
    private static GymMember findMemberById(String id) {
        for (GymMember member : members) {
            if (member.getId().equals(id)) {
                return member;
            }
        }
        return null;
    }

    /**
     * Clears all form fields and resets them to default values
     * 
     * This method resets all input fields, combo boxes, and radio buttons
     * to their initial state, preparing the form for new member entry.
     */
    private static void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtLocation.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        rbMale.setSelected(true);
        
        LocalDate now = LocalDate.now();
        dobYearComboBox.setSelectedIndex(0);
        dobMonthComboBox.setSelectedIndex(0);
        dobDayComboBox.setSelectedIndex(0);
        
        msYearComboBox.setSelectedItem(String.valueOf(now.getYear()));
        msMonthComboBox.setSelectedItem(String.format("%02d", now.getMonthValue()));
        msDayComboBox.setSelectedItem(String.format("%02d", now.getDayOfMonth()));
        
        
        updateStatus("Fields cleared");
    }

    /**
     * Creates a LocalDate from combo box selections
     * 
     * @param yearBox The year combo box
     * @param monthBox The month combo box
     * @param dayBox The day combo box
     * @return LocalDate object created from the selected values
     */
    private static LocalDate getDateFromComboBoxes(JComboBox<String> yearBox, JComboBox<String> monthBox, JComboBox<String> dayBox) {
        int year = Integer.parseInt((String) yearBox.getSelectedItem());
        int month = Integer.parseInt((String) monthBox.getSelectedItem());
        int day = Integer.parseInt((String) dayBox.getSelectedItem());
        return LocalDate.of(year, month, day);
    }

    /**
     * Updates the status bar with a message
     * 
     * @param message The message to display in the status bar
     */
    private static void updateStatus(String message) {
        lblStatus.setText(message);
    }

    /**
     * Highlights a success message in the status bar
     * 
     * @param message The success message to display
     */
    private static void highlightSuccess(String message) {
        updateStatus(message);
    }

    /**
     * Highlights an error message in the status bar
     * 
     * @param message The error message to display
     */
    private static void highlightError(String message) {
        updateStatus(message);
    }

    /**
     * Validates all required fields for member registration
     * 
     * This method performs comprehensive validation of all required fields
     * including data type validation for ID and phone number, and email format checking.
     * 
     * @param isRegular true if validating for regular member, false for premium
     * @return true if all validations pass, false otherwise
     */
    private static boolean validateRequiredFields(boolean isRegular) {
        StringBuilder errors = new StringBuilder();
        
        String id = txtId.getText().trim();
        if (id.isEmpty()) {
            errors.append("- Member ID is required\n");
        } else {
            try {
                Integer.parseInt(id);
            } catch (NumberFormatException e) {
                errors.append("- Member ID must be a valid number\n");
            }
        }
        
        if (txtName.getText().trim().isEmpty()) {
            errors.append("- Name is required\n");
        }
        
        if (txtLocation.getText().trim().isEmpty()) {
            errors.append("- Location is required\n");
        }
        
        String phone = txtPhone.getText().trim();
        if (phone.isEmpty()) {
            errors.append("- Phone number is required\n");
        } else {
            try {
                Long.parseLong(phone);
            } catch (NumberFormatException e) {
                errors.append("- Phone number must contain only numbers\n");
            }
        }
        
        String email = txtEmail.getText().trim();
        if (email.isEmpty()) {
            errors.append("- Email is required\n");
        } else if (email.contains("@")) {
            errors.append("- Email should not contain @, we'll add @gmail.com automatically\n");
        }
        
        if (isRegular) {
            if (txtReferralSource.getText().trim().isEmpty()) {
                errors.append("- Referral Source is required for regular members\n");
            }
        } else {
            if (txtTrainerName.getText().trim().isEmpty()) {
                errors.append("- Personal Trainer name is required for premium members\n");
            }
        }
        
        if (errors.length() > 0) {
            JOptionPane.showMessageDialog(mainFrame, 
                "Please fix the following errors:\n\n" + errors.toString(), 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }

    /**
     * Adds a new regular member to the system
     * 
     * This method validates all required fields, creates a new RegularMember object,
     * adds it to the members list, and updates the UI components.
     */
    private static void addRegularMember() {
        try {
            if (!validateRequiredFields(true)) {
                return;
            }
            
            String id = txtId.getText().trim();
            
            if (findMemberById(id) != null) {
                JOptionPane.showMessageDialog(mainFrame, "Member ID already exists", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String name = txtName.getText().trim();
            String phone = txtPhone.getText().trim();
            
            String emailInput = txtEmail.getText().trim().toLowerCase();
            String email = emailInput + "@gmail.com";
            
            String gender = rbMale.isSelected() ? "Male" : "Female";
            
            LocalDate dob = getDateFromComboBoxes(dobYearComboBox, dobMonthComboBox, dobDayComboBox);
            LocalDate startDate = getDateFromComboBoxes(msYearComboBox, msMonthComboBox, msDayComboBox);
            
            String plan = (String) cbMembershipPlan.getSelectedItem();
            String referralSource = txtReferralSource.getText().trim();
            
            RegularMember member = new RegularMember(id, name, phone, email, gender, dob, startDate, plan, referralSource);
            members.add(member);
            
            updateMemberDropdown();
            refreshMemberTable();
            
            JOptionPane.showMessageDialog(mainFrame, "Regular member added successfully!\nEmail: " + email, "Success", JOptionPane.INFORMATION_MESSAGE);
            
            updateStatus("Regular member added: " + name);
            
            clearFields();
            
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(mainFrame, "Invalid date format", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainFrame, "Error adding member: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Adds a new premium member to the system
     * 
     * This method validates all required fields, creates a new PremiumMember object,
     * adds it to the members list, and updates the UI components.
     */
    private static void addPremiumMember() {
        try {
            if (!validateRequiredFields(false)) {
                return;
            }
            
            String id = txtId.getText().trim();
            
            if (findMemberById(id) != null) {
                JOptionPane.showMessageDialog(mainFrame, "Member ID already exists", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String name = txtName.getText().trim();
            String phone = txtPhone.getText().trim();
            
            String emailInput = txtEmail.getText().trim().toLowerCase();
            String email = emailInput + "@gmail.com";
            
            String gender = rbMale.isSelected() ? "Male" : "Female";
            
            LocalDate dob = getDateFromComboBoxes(dobYearComboBox, dobMonthComboBox, dobDayComboBox);
            LocalDate startDate = getDateFromComboBoxes(msYearComboBox, msMonthComboBox, msDayComboBox);
            
            String trainer = txtTrainerName.getText().trim();
            
            PremiumMember member = new PremiumMember(id, name, phone, email, gender, dob, startDate, trainer);
            members.add(member);
            
            updateMemberDropdown();
            refreshMemberTable();
            
            JOptionPane.showMessageDialog(mainFrame, "Premium member added successfully!\nEmail: " + email, "Success", JOptionPane.INFORMATION_MESSAGE);
            
            updateStatus("Premium member added: " + name);
            
            clearFields();
            
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(mainFrame, "Invalid date format", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainFrame, "Error adding member: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Activates membership for the selected member
     * 
     * Sets the selected member's status to active and updates the display.
     */
    private static void activateMembership() {
        int selectedIndex = cbMemberSelect.getSelectedIndex();
        if (selectedIndex > 0) {
            String selectedItem = (String) cbMemberSelect.getSelectedItem();
            String memberId = selectedItem.split(" - ")[0];
            GymMember selectedMember = findMemberById(memberId);
            
            if (selectedMember != null) {
                selectedMember.activateMembership();
                JOptionPane.showMessageDialog(mainFrame, "Membership activated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshMemberTable();
                
                updateStatus("Membership activated for: " + selectedMember.getName());
            }
        } else {
            JOptionPane.showMessageDialog(mainFrame, "Please select a member", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Deactivates membership for the selected member
     * 
     * Sets the selected member's status to inactive and updates the display.
     */
    private static void deactivateMembership() {
        int selectedIndex = cbMemberSelect.getSelectedIndex();
        if (selectedIndex > 0) {
            String selectedItem = (String) cbMemberSelect.getSelectedItem();
            String memberId = selectedItem.split(" - ")[0];
            GymMember selectedMember = findMemberById(memberId);
            
            if (selectedMember != null) {
                selectedMember.deactivateMembership();
                JOptionPane.showMessageDialog(mainFrame, "Membership deactivated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshMemberTable();
                
                updateStatus("Membership deactivated for: " + selectedMember.getName());
            }
        } else {
            JOptionPane.showMessageDialog(mainFrame, "Please select a member", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Marks attendance for the selected member
     * 
     * Increments the attendance count for the selected member if their membership is active.
     * Only active members can have their attendance marked.
     */
    private static void markAttendance() {
        int selectedIndex = cbMemberSelect.getSelectedIndex();
        if (selectedIndex > 0) {
            String selectedItem = (String) cbMemberSelect.getSelectedItem();
            String memberId = selectedItem.split(" - ")[0];
            GymMember selectedMember = findMemberById(memberId);
            
            if (selectedMember != null) {
                if (selectedMember.isActiveStatus()) {
                    selectedMember.markAttendance();
                    JOptionPane.showMessageDialog(mainFrame, "Attendance marked successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshMemberTable();
                    
                    updateStatus("Attendance marked for: " + selectedMember.getName());
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "Cannot mark attendance. Membership is not active", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(mainFrame, "Please select a member", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Upgrades the membership plan for a regular member
     * 
     * Allows upgrading a regular member's plan to Standard or Deluxe.
     * Only regular members can have their plans upgraded.
     */
    private static void upgradePlan() {
        int selectedIndex = cbMemberSelect.getSelectedIndex();
        if (selectedIndex > 0) {
            String selectedItem = (String) cbMemberSelect.getSelectedItem();
            String memberId = selectedItem.split(" - ")[0];
            GymMember selectedMember = findMemberById(memberId);
            
            if (selectedMember != null && selectedMember instanceof RegularMember) {
                RegularMember regularMember = (RegularMember) selectedMember;
                
                String[] options = {"Standard", "Deluxe"};
                String newPlan = (String) JOptionPane.showInputDialog(mainFrame,
                     "Select new plan:", "Upgrade Plan",
                     JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                
                if (newPlan != null) {
                    boolean upgraded = regularMember.upgradePlan(newPlan);
                    if (upgraded) {
                        JOptionPane.showMessageDialog(mainFrame, "Plan upgraded successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                        refreshMemberTable();
                        
                        updateStatus("Plan upgraded for: " + regularMember.getName());
                    } else {
                        JOptionPane.showMessageDialog(mainFrame, "Failed to upgrade plan", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Please select a regular member", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(mainFrame, "Please select a member", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Reverts a regular member from the system
     * 
     * Removes a regular member with a specified reason.
     * Requires a removal reason to be entered in the form.
     */
    private static void revertRegularMember() {
        int selectedIndex = cbMemberSelect.getSelectedIndex();
        if (selectedIndex > 0) {
            String selectedItem = (String) cbMemberSelect.getSelectedItem();
            String memberId = selectedItem.split(" - ")[0];
            GymMember selectedMember = findMemberById(memberId);
            
            if (selectedMember != null && selectedMember instanceof RegularMember) {
                RegularMember regularMember = (RegularMember) selectedMember;
                
                String reason = txtRemovalReason.getText().trim();
                if (reason.isEmpty()) {
                    JOptionPane.showMessageDialog(mainFrame, "Please enter a removal reason", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                regularMember.revertRegularMember(reason);
                JOptionPane.showMessageDialog(mainFrame, "Regular member reverted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshMemberTable();
                
                updateStatus("Regular member reverted: " + regularMember.getName());
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Please select a regular member", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(mainFrame, "Please select a member", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Reverts a premium member from the system
     * 
     * Removes a premium member from the system.
     * Only premium members can be reverted using this method.
     */
    private static void revertPremiumMember() {
        int selectedIndex = cbMemberSelect.getSelectedIndex();
        if (selectedIndex > 0) {
            String selectedItem = (String) cbMemberSelect.getSelectedItem();
            String memberId = selectedItem.split(" - ")[0];
            GymMember selectedMember = findMemberById(memberId);
            
            if (selectedMember != null && selectedMember instanceof PremiumMember) {
                PremiumMember premiumMember = (PremiumMember) selectedMember;
                
                premiumMember.revertPremiumMember();
                JOptionPane.showMessageDialog(mainFrame, "Premium member reverted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshMemberTable();
                
                updateStatus("Premium member reverted: " + premiumMember.getName());
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Please select a premium member", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(mainFrame, "Please select a member", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Processes payment for a premium member
     * 
     * Records a payment amount for the selected premium member.
     * Only premium members can make payments through this method.
     */
    private static void payDueAmount() {
        int selectedIndex = cbMemberSelect.getSelectedIndex();
        if (selectedIndex > 0) {
            String selectedItem = (String) cbMemberSelect.getSelectedItem();
            String memberId = selectedItem.split(" - ")[0];
            GymMember selectedMember = findMemberById(memberId);
            
            if (selectedMember != null && selectedMember instanceof PremiumMember) {
                PremiumMember premiumMember = (PremiumMember) selectedMember;
                
                try {
                    String amountStr = txtPaidAmount.getText().trim();
                    if (amountStr.isEmpty()) {
                        JOptionPane.showMessageDialog(mainFrame, "Please enter payment amount", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    double amount = Double.parseDouble(amountStr);
                    boolean paid = premiumMember.payDueAmount(amount);
                    
                    if (paid) {
                        JOptionPane.showMessageDialog(mainFrame, "Payment processed successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                        refreshMemberTable();
                        
                        updateStatus("Payment processed for: " + premiumMember.getName());
                    } else {
                        JOptionPane.showMessageDialog(mainFrame, "Payment failed", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(mainFrame, "Invalid amount format", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Please select a premium member", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(mainFrame, "Please select a member", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Calculates discount for a premium member
     * 
     * Computes and displays the discount amount for the selected premium member
     * based on their payment status and other criteria.
     */
    private static void calculateDiscount() {
        int selectedIndex = cbMemberSelect.getSelectedIndex();
        if (selectedIndex > 0) {
            String selectedItem = (String) cbMemberSelect.getSelectedItem();
            String memberId = selectedItem.split(" - ")[0];
            GymMember selectedMember = findMemberById(memberId);
            
            if (selectedMember != null && selectedMember instanceof PremiumMember) {
                PremiumMember premiumMember = (PremiumMember) selectedMember;
                
                double discount = premiumMember.calculateDiscount();
                if (discount > 0) {
                    txtDiscountAmount.setText(String.valueOf(discount));
                    JOptionPane.showMessageDialog(mainFrame, "Discount calculated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    
                    updateStatus("Discount calculated for: " + premiumMember.getName());
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "Cannot calculate discount. Payment is not complete", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Please select a premium member", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(mainFrame, "Please select a member", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Displays all members in a detailed format
     * 
     * Shows comprehensive information about all registered members.
     * If called from the Reports tab, displays in the text area.
     * Otherwise, opens a new window with the member information.
     */
    private static void displayAllMembers() {
        if (members.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "No members to display", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if (tabbedPane.getSelectedIndex() == 2) {
            StringBuilder sb = new StringBuilder();
            sb.append("=== ALL GYM MEMBERS ===\n\n");
            
            for (GymMember member : members) {
                sb.append("ID: ").append(member.getId()).append("\n");
                sb.append("Name: ").append(member.getName()).append("\n");
                sb.append("Phone: ").append(member.getPhoneNumber()).append("\n");
                sb.append("Email: ").append(member.getEmail()).append("\n");
                sb.append("Gender: ").append(member.getGender()).append("\n");
                sb.append("Date of Birth: ").append(member.getDateOfBirth().format(DATE_FORMATTER)).append("\n");
                sb.append("Membership Start Date: ").append(member.getMembershipStartDate().format(DATE_FORMATTER)).append("\n");
                sb.append("Attendance Count: ").append(member.getAttendanceCount()).append("\n");
                sb.append("Loyalty Points: ").append(member.getLoyaltyPoints()).append("\n");
                sb.append("Active Status: ").append(member.isActiveStatus() ? "Active" : "Inactive").append("\n");
                
                if (member instanceof RegularMember) {
                    RegularMember regularMember = (RegularMember) member;
                    sb.append("Member Type: Regular\n");
                    sb.append("Membership Plan: ").append(regularMember.getMembershipPlan()).append("\n");
                    sb.append("Price: ").append(regularMember.getPrice()).append("\n");
                    sb.append("Referral Source: ").append(regularMember.getReferralSource()).append("\n");
                    sb.append("Eligible for Upgrade: ").append(regularMember.isEligibleForUpgrade() ? "Yes" : "No").append("\n");
                    if (!regularMember.getRemovalReason().isEmpty()) {
                        sb.append("Removal Reason: ").append(regularMember.getRemovalReason()).append("\n");
                    }
                } else if (member instanceof PremiumMember) {
                    PremiumMember premiumMember = (PremiumMember) member;
                    sb.append("Member Type: Premium\n");
                    sb.append("Premium Charge: ").append(PremiumMember.getPremiumCharge()).append("\n");
                    sb.append("Personal Trainer: ").append(premiumMember.getPersonalTrainer()).append("\n");
                    sb.append("Payment Status: ").append(premiumMember.isPaymentComplete() ? "Complete" : "Incomplete").append("\n");
                    sb.append("Paid Amount: ").append(premiumMember.getPaidAmount()).append("\n");
                    sb.append("Remaining Amount: ").append(PremiumMember.getPremiumCharge() - premiumMember.getPaidAmount()).append("\n");
                    if (premiumMember.getDiscountAmount() > 0) {
                        sb.append("Discount Amount: ").append(premiumMember.getDiscountAmount()).append("\n");
                        sb.append("Final Amount After Discount: ").append(PremiumMember.getPremiumCharge() - premiumMember.getDiscountAmount()).append("\n");
                    }
                }
                
                sb.append("\n-----------------------------------------\n\n");
            }
            
            txtAreaDisplay.setText(sb.toString());
            txtAreaDisplay.setCaretPosition(0);
            
            updateStatus("Displaying all members in report. Total: " + members.size());
            
            return;
        }
        
        if (displayFrame != null && displayFrame.isVisible()) {
            displayFrame.dispose();
        }
        
        displayFrame = new JFrame("All Gym Members");
        displayFrame.setSize(800, 600);
        displayFrame.setLayout(new BorderLayout());
        
        JTextArea displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        StringBuilder sb = new StringBuilder();
        sb.append("=== ALL GYM MEMBERS ===\n\n");
        
        for (GymMember member : members) {
            sb.append("ID: ").append(member.getId()).append("\n");
            sb.append("Name: ").append(member.getName()).append("\n");
            sb.append("Phone: ").append(member.getPhoneNumber()).append("\n");
            sb.append("Email: ").append(member.getEmail()).append("\n");
            sb.append("Gender: ").append(member.getGender()).append("\n");
            sb.append("Date of Birth: ").append(member.getDateOfBirth().format(DATE_FORMATTER)).append("\n");
            sb.append("Membership Start Date: ").append(member.getMembershipStartDate().format(DATE_FORMATTER)).append("\n");
            sb.append("Attendance Count: ").append(member.getAttendanceCount()).append("\n");
            sb.append("Loyalty Points: ").append(member.getLoyaltyPoints()).append("\n");
            sb.append("Active Status: ").append(member.isActiveStatus() ? "Active" : "Inactive").append("\n");
            
            if (member instanceof RegularMember) {
                RegularMember regularMember = (RegularMember) member;
                sb.append("Member Type: Regular\n");
                sb.append("Membership Plan: ").append(regularMember.getMembershipPlan()).append("\n");
                sb.append("Price: ").append(regularMember.getPrice()).append("\n");
                sb.append("Referral Source: ").append(regularMember.getReferralSource()).append("\n");
                sb.append("Eligible for Upgrade: ").append(regularMember.isEligibleForUpgrade() ? "Yes" : "No").append("\n");
                if (!regularMember.getRemovalReason().isEmpty()) {
                    sb.append("Removal Reason: ").append(regularMember.getRemovalReason()).append("\n");
                }
            } else if (member instanceof PremiumMember) {
                PremiumMember premiumMember = (PremiumMember) member;
                sb.append("Member Type: Premium\n");
                sb.append("Premium Charge: ").append(PremiumMember.getPremiumCharge()).append("\n");
                sb.append("Personal Trainer: ").append(premiumMember.getPersonalTrainer()).append("\n");
                sb.append("Payment Status: ").append(premiumMember.isPaymentComplete() ? "Complete" : "Incomplete").append("\n");
                sb.append("Paid Amount: ").append(premiumMember.getPaidAmount()).append("\n");
                sb.append("Remaining Amount: ").append(PremiumMember.getPremiumCharge() - premiumMember.getPaidAmount()).append("\n");
                if (premiumMember.getDiscountAmount() > 0) {
                    sb.append("Discount Amount: ").append(premiumMember.getDiscountAmount()).append("\n");
                    sb.append("Final Amount After Discount: ").append(PremiumMember.getPremiumCharge() - premiumMember.getDiscountAmount()).append("\n");
                }
            }
            
            sb.append("\n-----------------------------------------\n\n");
        }
        
        displayArea.setText(sb.toString());
        
        JScrollPane scrollPane = new JScrollPane(displayArea);
        displayFrame.add(scrollPane, BorderLayout.CENTER);
        
        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.BOLD, 12));
        styleButton(closeButton, PRIMARY_COLOR);
        closeButton.addActionListener(e -> displayFrame.dispose());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        displayFrame.add(buttonPanel, BorderLayout.SOUTH);
        
        displayFrame.setLocationRelativeTo(mainFrame);
        displayFrame.setVisible(true);
        
        updateStatus("Displaying all members. Total: " + members.size());
    }

    /**
     * Saves all member data to a text file
     * 
     * Creates a formatted text file containing all member information
     * in a tabular format with headers and borders for easy reading.
     */
    private static void saveToFile() {
        try {
            if (members.isEmpty()) {
                JOptionPane.showMessageDialog(mainFrame, "No members to save",
                                              "Empty List", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            File file = new File(MEMBERS_FILE);
            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
                writer.println("+---------+--------------------+--------------------+---------------+-------------------------+------------+------------+--------+------------+---------------+----------+------------+--------------------+---------------+");
                writer.println("| ID      | Name               | Location/Type      | Phone         | Email                   | Start Date | Plan       | Gender | Attendance | Loyalty Points| Status   | DOB        | Trainer            | Paid Amount   |");
                writer.println("+---------+--------------------+--------------------+---------------+-------------------------+------------+------------+--------+------------+---------------+----------+------------+--------------------+---------------+");
                
                for (GymMember member : members) {
                    StringBuilder line = new StringBuilder();
                    line.append(String.format("| %-7s | %-18s | ", member.getId(), member.getName()));
                    
                    if (member instanceof RegularMember) {
                        RegularMember regularMember = (RegularMember) member;
                        line.append(String.format("%-18s | %-13s | %-23s | %-10s | %-10s | %-6s | %-10d | %-13d | %-8s | %-10s | %-18s | %-13s |",
                            txtLocation.getText().isEmpty() ? regularMember.getMembershipPlan() : txtLocation.getText(),
                            member.getPhoneNumber(),
                            member.getEmail(),
                            member.getMembershipStartDate().format(dateFormatter),
                            regularMember.getMembershipPlan(),
                            member.getGender(),
                            member.getAttendanceCount(),
                            regularMember.getLoyaltyPoints(),
                            member.isActiveStatus() ? "Active" : "Inactive",
                            member.getDateOfBirth().format(dateFormatter),
                            "N/A",
                            "N/A"
                        ));
                    } else if (member instanceof PremiumMember) {
                        PremiumMember premiumMember = (PremiumMember) member;
                        line.append(String.format("%-18s | %-13s | %-23s | %-10s | %-10s | %-6s | %-10d | %-13d | %-8s | %-10s | %-18s | %-13.2f |",
                            "Premium",
                            member.getPhoneNumber(),
                            member.getEmail(),
                            member.getMembershipStartDate().format(dateFormatter),
                            "Premium",
                            member.getGender(),
                            member.getAttendanceCount(),
                            premiumMember.getLoyaltyPoints(),
                            member.isActiveStatus() ? "Active" : "Inactive",
                            member.getDateOfBirth().format(dateFormatter),
                            premiumMember.getPersonalTrainer(),
                            premiumMember.getPaidAmount()
                        ));
                    }
                    
                    writer.println(line.toString());
                }
                
                writer.println("+---------+--------------------+--------------------+---------------+-------------------------+------------+------------+--------+------------+---------------+----------+------------+--------------------+---------------+");
            }
            
            JOptionPane.showMessageDialog(mainFrame, members.size() + " members saved to file successfully",
                                          "Success", JOptionPane.INFORMATION_MESSAGE);
            highlightSuccess(members.size() + " members saved to file successfully");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(mainFrame, "Error saving to file: " + e.getMessage(),
                                          "Error", JOptionPane.ERROR_MESSAGE);
            highlightError("Error saving to file: " + e.getMessage());
        }
    }

    /**
     * Reads member data from a selected text file
     * 
     * Opens a file chooser dialog to select a file and loads member data
     * from the selected file, parsing both regular and premium member information.
     */
    private static void readFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Open Members Text File");
        
        int userSelection = fileChooser.showOpenDialog(mainFrame);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToOpen = fileChooser.getSelectedFile();
            
            try (BufferedReader reader = new BufferedReader(new FileReader(fileToOpen))) {
                members.clear();
                
                String line;
                int count = 0;
                
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    
                    if (parts.length > 0) {
                        String memberType = parts[0];
                        
                        if (memberType.equals("REGULAR") && parts.length >= 15) {
                            String id = parts[1];
                            String name = parts[2];
                            String phone = parts[3];
                            String email = parts[4];
                            String gender = parts[5];
                            LocalDate dob = LocalDate.parse(parts[6], DATE_FORMATTER);
                            LocalDate startDate = LocalDate.parse(parts[7], DATE_FORMATTER);
                            int attendance = Integer.parseInt(parts[8]);
                            int loyalty = Integer.parseInt(parts[9]);
                            boolean active = Boolean.parseBoolean(parts[10]);
                            String plan = parts[11];
                            String referral = parts[13];
                            boolean eligible = Boolean.parseBoolean(parts[14]);
                            
                            RegularMember member = new RegularMember(id, name, phone, email, gender, dob, startDate, plan, referral);
                            member.setAttendanceCount(attendance);
                            member.setLoyaltyPoints(loyalty);
                            member.setActiveStatus(active);
                            member.setEligibleForUpgrade(eligible);
                            
                            if (parts.length > 15) {
                                member.setRemovalReason(parts[15]);
                            }
                            
                            members.add(member);
                            count++;
                            
                        } else if (memberType.equals("PREMIUM") && parts.length >= 15) {
                            String id = parts[1];
                            String name = parts[2];
                            String phone = parts[3];
                            String email = parts[4];
                            String gender = parts[5];
                            LocalDate dob = LocalDate.parse(parts[6], DATE_FORMATTER);
                            LocalDate startDate = LocalDate.parse(parts[7], DATE_FORMATTER);
                            int attendance = Integer.parseInt(parts[8]);
                            int loyalty = Integer.parseInt(parts[9]);
                            boolean active = Boolean.parseBoolean(parts[10]);
                            String trainer = parts[11];
                            boolean paymentComplete = Boolean.parseBoolean(parts[12]);
                            double paidAmount = Double.parseDouble(parts[13]);
                            double discountAmount = Double.parseDouble(parts[14]);
                            
                            PremiumMember member = new PremiumMember(id, name, phone, email, gender, dob, startDate, trainer);
                            member.setAttendanceCount(attendance);
                            member.setLoyaltyPoints(loyalty);
                            member.setActiveStatus(active);
                            member.setPaymentComplete(paymentComplete);
                            member.setPaidAmount(paidAmount);
                            member.setDiscountAmount(discountAmount);
                            
                            members.add(member);
                            count++;
                        }
                    }
                }
                
                updateMemberDropdown();
                refreshMemberTable();
                
                JOptionPane.showMessageDialog(mainFrame, count + " members loaded successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                
                updateStatus("Members loaded from file: " + fileToOpen.getName() + ". Total: " + count);
                
            } catch (IOException | DateTimeParseException | NumberFormatException e) {
                JOptionPane.showMessageDialog(mainFrame, "Error reading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}