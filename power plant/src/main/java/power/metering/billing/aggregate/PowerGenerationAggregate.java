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
public class PowerGenerationAggregate {

    @AggregateIdentifier
    private Long timestamp;

    private String subscriberId;
    private String plantId;
    private Double generatedAmount;
    private String generatorType;

    public PowerGenerationAggregate() {}

    @CommandHandler
    public PowerGenerationAggregate(GenerateCommand command) {
        PowerGeneratedEvent event = new PowerGeneratedEvent();
        BeanUtils.copyProperties(command, event);

        //TODO: check key generation is properly done
        if (event.getTimestamp() == null) event.setTimestamp(createUUID());

        apply(event);
    }

    //<<< Etc / ID Generation
    private String createUUID() {
        return UUID.randomUUID().toString();
    }

    //>>> Etc / ID Generation

    //<<< EDA / Event Sourcing

    @EventSourcingHandler
    public void on(PowerGeneratedEvent event) {
        BeanUtils.copyProperties(event, this);
        //TODO: business logic here

    }

    @EventSourcingHandler
    public void on(급전지시됨Event event) {
        //TODO: business logic here

    }
    //>>> EDA / Event Sourcing

}
