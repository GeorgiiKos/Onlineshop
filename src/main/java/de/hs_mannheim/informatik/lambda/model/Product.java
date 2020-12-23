package de.hs_mannheim.informatik.lambda.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Product {

    @Id
    private String id;
    private String name;
    private String price;
    private String description;
    private int inStock;
    private String manufacturer;
    private String imagePath;

}
