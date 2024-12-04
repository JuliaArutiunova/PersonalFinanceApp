package by.it_academy.jd2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;
@AllArgsConstructor
@Data
@Builder
public class RecalculationDTO {
    private double oldValue;
    private UUID oldCurrency;
    private double newValue;
    private UUID newCurrency;
    private double accountBalance;
    private UUID accountCurrency;
}
