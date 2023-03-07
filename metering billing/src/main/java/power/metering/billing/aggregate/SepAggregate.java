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
    public void handle(CalculateCommand command) {
        MeasureAddedEvent event = new MeasureAddedEvent();
        BeanUtils.copyProperties(command, event);

        apply(event);
    }

    @CommandHandler
    public SepAggregate(CreateMeasureCommand command) {
        MeasureCreatedEvent event = new MeasureCreatedEvent();
        BeanUtils.copyProperties(command, event);

        //TODO: check key generation is properly done
        if (event.getId() == null) event.setId(createUUID());

        apply(event);
    }

    //<<< Etc / ID Generation
    private String createUUID() {
        return UUID.randomUUID().toString();
    }

    //>>> Etc / ID Generation

    //<<< EDA / Event Sourcing

    @EventSourcingHandler
    public void on(MeasureAddedEvent event) {
        //TODO: business logic here

    }

    @EventSourcingHandler
    public void on(MeasureCreatedEvent event) {
        BeanUtils.copyProperties(event, this);
        //TODO: business logic here

    }
    //>>> EDA / Event Sourcing

}
