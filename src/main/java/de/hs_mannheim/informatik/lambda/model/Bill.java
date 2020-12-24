package de.hs_mannheim.informatik.lambda.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
@AllArgsConstructor
public class Bill {

    @Id
    private String id;
    private PaymentInfo paymentInfo;
    private List<ProductAmount> productAmounts;
    private double total;

}
