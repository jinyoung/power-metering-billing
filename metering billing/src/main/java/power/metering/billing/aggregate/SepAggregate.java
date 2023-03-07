package power.metering.billing.aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.*;

import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.ToString;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;
import power.metering.billing.command.*;
import power.metering.billing.event.*;
import power.metering.billing.query.*;

@Aggregate
@Data
@ToString
public class SepAggregate {

    @AggregateIdentifier
    private String id;

    private String yearCode;
    private String monthCode;
    private String dayCode;
    private String subscriberId;
    private String platId;
    private Double generationAmount;
    private Double sep;

    public SepAggregate() {}

    @CommandHandler
    public SepAggregate(CalculateCommand command) {}

    //<<< Etc / ID Generation
    private String createUUID() {
        return UUID.randomUUID().toString();
    }
    //>>> Etc / ID Generation

    //<<< EDA / Event Sourcing

    //>>> EDA / Event Sourcing

}
