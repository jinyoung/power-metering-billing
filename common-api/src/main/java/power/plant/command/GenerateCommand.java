package power.plant.command;

import java.util.List;
import lombok.Data;
import lombok.ToString;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@ToString
@Data
public class GenerateCommand {

    private Long timestamp; // Please comment here if you want user to enter the id directly
    private String subscriberId;
    private String plantId;
    private Double generatedAmount;
    private String generatorType;
}
