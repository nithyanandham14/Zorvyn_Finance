package com.bjsn.finance.Module;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;
@Entity
@Data
@NoArgsConstructor
public class Item {
            @Id
            private int jwlno;
            private String name;
            private String fathername;
            @Positive
            private int principalamt;
            private LocalDate stdate;
            private String address;
            private String itemtype;
            private Long number;
            private Boolean status;
            private LocalDate reminderDate;

            @NotNull
            @Positive
            private double loanAmount;


            private LocalDate currDate;
            private int months;

            private double simpleInterest;
            private double compoundInterest;


}
