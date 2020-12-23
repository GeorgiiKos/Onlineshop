package de.hs_mannheim.informatik.lambda.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@Table
@AllArgsConstructor
public class Clickstream {

    @Column
    @PrimaryKey
    private String timestamp;
    @Column
    private String ip;
    @Column
    private int mouseX;
    @Column
    private int mouseY;
    @Column
    private String action;
    @Column
    private String webpage;

}
