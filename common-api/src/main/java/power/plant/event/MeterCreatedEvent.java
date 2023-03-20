package power.plant.event;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MeterCreatedEvent {

    private String id;
    private Integer yearCode;
    private Integer monthCode;
    private Integer dayCode;
    private String subscriberId;
    private String platId;
    private String generatorType;
    private Double generationAmount;
    private Double sep;
}
