package power.plant.command;

import java.util.List;
import lombok.Data;
import lombok.ToString;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@ToString
@Data
public class CalculateCommand {

    @TargetAggregateIdentifier
    private String id;

    private String subscriberId;
    private String plantId;
    private String generatorType;
    private Double generatedAmount;
}
