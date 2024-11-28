package by.it_academy.lib.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CurrencyNamesDTO {
    private String operationCurrencyName;
    private String accountCurrencyName;
}
