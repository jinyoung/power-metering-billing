package power.metering.billing.command;

import java.util.List;
import lombok.Data;
import lombok.ToString;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@ToString
@Data
public class CalculateCommand {

    private String id; // Please comment here if you want user to enter the id directly
    private String yearCode;
    private String monthCode;
    private String dayCode;
    private String subscriberId;
    private String platId;
    private Double generationAmount;
    private Double sep;
}