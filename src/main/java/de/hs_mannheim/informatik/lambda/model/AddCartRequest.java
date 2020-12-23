package de.hs_mannheim.informatik.lambda.model;

import lombok.Data;

@Data
public class AddCartRequest {

    private String stockId;
    private int amount;
}
