package power.plant.event;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PowerGeneratedEvent {

    private Long timestamp;
    private String subscriberId;
    private String plantId;
    private Double generatedAmount;
    private String generatorType;
}
