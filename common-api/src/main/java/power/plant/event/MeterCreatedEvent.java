package power.plant.event;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MeterCreatedEvent {

    private String id;
    private String yearCode;
    private String monthCode;
    private String dayCode;
    private String subscriberId;
    private String platId;
    private Double generationAmount;
    private Double sep;
}
