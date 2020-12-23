package de.hs_mannheim.informatik.lambda.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Bill {

    @Id
    private String id;
    private User user;
    private Product product;
    private int amount;
    private String finalPrice;
    private String billAddress;
    private String payment;

}
