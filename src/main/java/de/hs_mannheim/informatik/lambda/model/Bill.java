package de.hs_mannheim.informatik.lambda.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class Bill {

    @Id
    private String id;
    private User user;
    private List<Product> products;
    private int amount;
    private String finalPrice;
    private String billAddress;
    private String payment;

}
