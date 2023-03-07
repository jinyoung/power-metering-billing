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
//import power.metering.billing.query.*;  //TODO

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
        MeasureCalculatedEvent event = new MeasureCalculatedEvent();
        BeanUtils.copyProperties(command, event);

        apply(event);
    }

    //<<< Etc / ID Generation
    private String createUUID() {
        return UUID.randomUUID().toString();
    }

    //>>> Etc / ID Generation

    //<<< EDA / Event Sourcing

    @EventSourcingHandler
    public void on(MeasureCalculatedEvent event) {
        //TODO: business logic here

    }
    //>>> EDA / Event Sourcing

}
