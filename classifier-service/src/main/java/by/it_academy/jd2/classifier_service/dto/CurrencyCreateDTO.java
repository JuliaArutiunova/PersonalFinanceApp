package by.it_academy.jd2.classifier_service.dto;

import by.it_academy.jd2.classifier_service.storage.entity.CurrencyNames;
import by.it_academy.lib.validation.EnumValue;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
public class CurrencyCreateDTO {
    @NotBlank(message = "Поле не заполнено")
    @EnumValue(enumClass = CurrencyNames.class, message = "Валюты с таким названием не существует")
    private String title;

    @NotBlank(message = "Поле не заполнено")
    private String description;
}
