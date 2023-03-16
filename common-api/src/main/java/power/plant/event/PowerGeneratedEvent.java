package power.plant.event;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PowerGeneratedEvent {

    private String id;
    private String subscriberId;
    private Double generatedAmount;
    private String generatorType;
    private String plantId;
}
