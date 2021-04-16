package br.com.hartwig.raffle.jasper.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleDTO {

    private String title;
    private String description;
    private Double price;
    private Integer number;
}
