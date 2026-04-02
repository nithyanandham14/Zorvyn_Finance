package com.bjsn.finance.Module;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data// getters Setters and toString methods
@Entity//in the jpa for table and used within the Databases
@NoArgsConstructor// Constructors
@AllArgsConstructor//constructors with arguments and without arguments
public class Users {
    @Id
    private int id;
    private String username;
    private String Password;
    private String role;

}
//Demo account demo1->abc
