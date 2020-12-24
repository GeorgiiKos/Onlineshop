package de.hs_mannheim.informatik.lambda.model;

import lombok.Data;

@Data
public class PaymentInfo {

    private String firstName;
    private String lastName;
    private String gender;
    private String dateOfBirth;
    private String email;
    private String address;
    private String payment;

}
