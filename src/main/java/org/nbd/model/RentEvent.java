package org.nbd.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentEvent implements Serializable {
    private Rent rent;
    private String rentalAgencyName;
    private long timestamp;
}