package power.plant.command;

import java.util.List;
import lombok.Data;
import lombok.ToString;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@ToString
@Data
public class GenerateCommand {

    @TargetAggregateIdentifier
    private String id;

    private Double generatedAmount;
}
