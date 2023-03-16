package power.plant.event;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class 입찰됨Event {

    private String id;
    private String subscriberId;
    private String plantId;
    private Double generatedAmount;
    private String generatorType;
}
