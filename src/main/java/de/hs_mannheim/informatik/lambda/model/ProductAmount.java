package de.hs_mannheim.informatik.lambda.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductAmount {

    private Product product;
    private int amount;

}
