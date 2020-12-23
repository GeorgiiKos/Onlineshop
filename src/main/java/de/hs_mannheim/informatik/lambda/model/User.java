package de.hs_mannheim.informatik.lambda.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class User {

    @Id
    private String id;
    private String name;
    private String email;
    private String address;
    private String dateOfBirth;
    private String gender;
}
