package power.metering.billing.event;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MeasureAddedEvent {

    private String id;
    private String yearCode;
    private String monthCode;
    private String dayCode;
    private String subscriberId;
    private String platId;
    private Double generationAmount;
    private Double sep;
}
