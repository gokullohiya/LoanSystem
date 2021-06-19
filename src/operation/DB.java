package operation;

import customer.Customer;
import customer.PersonalInformation;

import java.util.ArrayList;

public interface DB {
     static ArrayList<Customer> getNegativeCustomer() {

        ArrayList<Customer> negativeCustomer = new ArrayList<>();

        Customer customer = new Customer();
        customer.setId(1010);
        PersonalInformation personalInformation = new PersonalInformation();
        personalInformation.setFirstName("Tim");
        personalInformation.setLastName("Cook");
        personalInformation.setPhone("22222");
        personalInformation.setPanCard("232");
        personalInformation.setVoterId("21212");
        personalInformation.setEmail("femkm@gmail.com");
        customer.setPersonalInformation(personalInformation);
        negativeCustomer.add(customer);


        customer = new Customer();
        customer.setId(1010);
        personalInformation.setFirstName("Tim");
        personalInformation.setLastName("Cook");
        personalInformation.setPhone("22222");
        personalInformation.setPanCard("232");
        personalInformation.setVoterId("21212");
        personalInformation.setEmail("femkm@gmail.com");
        customer.setPersonalInformation(personalInformation);
        negativeCustomer.add(customer);

        return negativeCustomer;
    }

}
