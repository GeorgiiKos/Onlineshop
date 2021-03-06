package de.hs_mannheim.informatik.lambda.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
@AllArgsConstructor
public class ShoppingCart {

    @Id
    private String sessionId;
    private List<ProductAmount> productAmounts;

    public double getTotalCost() {
        return productAmounts.stream().mapToDouble(item -> item.getProduct().getPrice() * item.getAmount()).sum();
    }

}
