package operation;

import customer.CommonConstants;
import customer.Customer;
import customer.LoanDetails;
import customer.PersonalInformation;
import util.LoanConstants;
import util.StageConstants;
import util.Utility;

import java.util.ArrayList;

import static util.Utility.*;

public class LOSProcess implements StageConstants, CommonConstants {

    //private Customer customers[] = new Customer[100];
    private final ArrayList<Customer> customers = new ArrayList<>();

    public void sourcing(){
        Customer customer = new Customer();
        customer.setId(serialCounter);
        customer.setStage(SOURCING);

        System.out.println("Enter the first Name");
        String firstName = scanner.next();
        System.out.println("Enter the last Name");
        String lastName = scanner.next();
        System.out.println("Enter the Age");
        int age  = scanner.nextInt();
        System.out.println("Enter the Loan Type HL,AL,PL ");
        String type = scanner.next();
        System.out.println("Enter the Amount");
        double amount = scanner.nextDouble();
        System.out.println("Duration of the loan");
        int duration = scanner.nextInt();

        PersonalInformation personalInformation = new PersonalInformation();
        personalInformation.setFirstName(firstName);
        personalInformation.setLastName(lastName);
        personalInformation.setAge(age);
        customer.setPersonalInformation(personalInformation);

        LoanDetails loanDetails = new LoanDetails();
        loanDetails.setType(type);
        loanDetails.setAmount(amount);
        loanDetails.setDuration(duration);
        customer.setLoanDetails(loanDetails);

        customers.add(customer);
        serialCounter++;
        System.out.println("Sourcing Done...");
    
    }

    public void checkStage(int applicationNumber){
        boolean isStageFound = false;

        if(customers != null && customers.size()>0){
        for(Customer customer: customers){
            if (customer.getId() == applicationNumber){
                System.out.println("You are on: "+ Utility.getStageName(customer.getStage()));
                isStageFound = true;
                moveToNextStage(customer);
                break;
            }
        }
        }
        if (!isStageFound){
            System.out.println("Invalid Application Number");
        }

    }

    public void qde(Customer customer){
        System.out.println("Application Number: "+customer.getId());
        System.out.println(" First Name: "+customer.getPersonalInformation().getFirstName()+" Last Name: "+customer.getPersonalInformation().getLastName());
        System.out.println("You applied for a: "+ customer.getLoanDetails().getType()+" Duration: "+customer.getLoanDetails().getDuration()+" Amount: "+customer.getLoanDetails().getAmount());
        System.out.println("Enter the PANCard Number: ");
        String panCard = scanner.next();
        System.out.println("Enter your VoterID: ");
        String voterId = scanner.next();
        System.out.println("Enter the Income: ");
        double income = scanner.nextDouble();
        System.out.println("Enter the liability: ");
        double liability = scanner.nextDouble();
        System.out.println("Enter Your PhoneNumber: ");
        String phone = scanner.next();
        System.out.println("Enter Your Email: ");
        String email = scanner.next();

        customer.getPersonalInformation().setPanCard(panCard);
        customer.getPersonalInformation().setVoterId(voterId);
        customer.getPersonalInformation().setPhone(phone);
        customer.getPersonalInformation().setEmail(email);
        customer.setIncome(income);
        customer.setLiability(liability);
        customer.setStage(QDE);
    }

    public void moveToNextStage(Customer customer) {
        while (true) {
            if (customer.getStage() == SOURCING) {

                System.out.println("Want to move to the QDE stage Y/N");
                char choice = scanner.next().toUpperCase().charAt(0);

                if (choice == YES) {
                    qde(customer);
                } else {
                    return;
                }
            }

            else if (customer.getStage() == QDE) {
                System.out.println("Want to move to the DeDupe Stage Y/N: ");
                char choice = scanner.next().toUpperCase().charAt(0);

                if (choice == YES) {
                    deDupe(customer);
                } else {
                    return;
                }
            }

            else if (customer.getStage() == DEDUPE) {
                System.out.println("Want to move to the Scoring Stage Y/N: ");
                char choice = scanner.next().toUpperCase().charAt(0);

                if (choice == YES) {
                    scoring(customer);
                } else {
                    return;
                }
            }

            else if (customer.getStage() == SCORING){
                System.out.println("Want to move to the Approval Stage Y/N: ");
                char choice = scanner.next().toUpperCase().charAt(0);

                if (choice == YES) {
                    approval(customer);
                } else {
                    return;
                }
            }
        }
    }

    public void deDupe(Customer customer) {

        customer.setStage(DEDUPE);


        boolean isNegativeFound = false;
        for (Customer negativeCustomer : DB.getNegativeCustomer()) {
            int negativeScore = isNegative(customer, negativeCustomer);
            if (negativeScore > 0) {
                System.out.println("Customer Record found in DeDupe and Score is: " + negativeScore);
                isNegativeFound = true;
                break;
            }
        }
        if (isNegativeFound) {
            System.out.println("Do you want to proceed this loan: "+customer.getId());
            char choice = scanner.next().toUpperCase().charAt(0);

            if (choice == NO){
                customer.setRemarks("Loan is rejected due to High Score in DeDupe Check");
                customer.setStage(REJECT);
                return;
            }
        }
    }
    private int isNegative(Customer customer, Customer negative){
        int percentageMatch = 0;

        if (customer.getPersonalInformation().getPhone().equals(negative.getPersonalInformation().getPhone())){
            percentageMatch += 20;
        }

        if (customer.getPersonalInformation().getEmail().equals(negative.getPersonalInformation().getEmail())){
            percentageMatch += 20;
        }

        if (customer.getPersonalInformation().getVoterId().equals(negative.getPersonalInformation().getVoterId())){
            percentageMatch += 20;
        }

        if (customer.getPersonalInformation().getPanCard().equals(negative.getPersonalInformation().getPanCard())){
            percentageMatch += 20;
        }
        if (customer.getPersonalInformation().getAge() == negative.getPersonalInformation().getAge() && customer.getPersonalInformation().getFirstName().equals(negative.getPersonalInformation().getFirstName())){
            percentageMatch += 20;
        }

        return percentageMatch;
    }

    public void scoring(Customer customer){
        customer.setStage(SCORING);
        int score=0;
        double totalIncome = customer.getIncome()  - customer.getLiability();
        if (customer.getPersonalInformation().getAge()>=21 && customer.getPersonalInformation().getAge()<=35){
            score+=50;
            System.out.println("50 points because of age");
        }

        if (totalIncome>=200000){
            score += 50;
        }
        System.out.println("Final Score "+score);
        customer.getLoanDetails().setScore(score);
    }

    public void approval(Customer customer) {
        customer.setStage(APPROVAL);

        int score = customer.getLoanDetails().getScore();
        System.out.println("id " + customer.getId());
        System.out.println("Name is: " + customer.getPersonalInformation().getFirstName() + " " + customer.getPersonalInformation().getLastName());
        System.out.println("Score is: "+customer.getLoanDetails().getScore());
        System.out.println("Loan is: " + customer.getLoanDetails().getType() + " Amount:" + customer.getLoanDetails().getAmount() + " Duration: " + customer.getLoanDetails().getDuration());

        double approveAmount = customer.getLoanDetails().getAmount() * (score / 100);

        System.out.println("Loan Amount Approved is: " + approveAmount);
        System.out.println("Do you want to bring this loan or not");
        char choice = scanner.next().toUpperCase().charAt(0);

        if (choice == NO) {
            customer.setStage(REJECT);
            customer.setRemarks("Customer Denied the approval Amount " + approveAmount);
            return;
        } else {
            showEMI(customer);
        }
    }
private void showEMI(Customer customer){

    if (customer.getLoanDetails().getType().equals(LoanConstants.HOME_LOAN)){
customer.getLoanDetails().setRoi(LoanConstants.HOME_LOAN_ROI);
    }
    if (customer.getLoanDetails().getType().equals(LoanConstants.AUTO_LOAN)){
        customer.getLoanDetails().setRoi(LoanConstants.AUTO_LOAN_ROI);
    }
    if (customer.getLoanDetails().getType().equals(LoanConstants.PERSONAL_LOAN)){
        customer.getLoanDetails().setRoi(LoanConstants.PERSONAL_LOAN_ROI);
    }
    double perMonthPrinciple = customer.getLoanDetails().getAmount() / customer.getLoanDetails().getDuration();
    double interest = perMonthPrinciple*customer.getLoanDetails().getRoi();
    double totalEMI = perMonthPrinciple*interest;

    System.out.println("Your EMI is "+totalEMI);
    return;
    }
}
