package power.plant.aggregate;

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
import power.plant.command.*;
import power.plant.event.*;
import power.plant.query.*;

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
    public void handle(GenerateCommand command) {
        PowerGeneratedEvent event = new PowerGeneratedEvent();
        BeanUtils.copyProperties(command, event);

        apply(event);
    }

    @CommandHandler
    public PowerGenerationAggregate(입찰Command command) {
        입찰됨Event event = new 입찰됨Event();
        BeanUtils.copyProperties(command, event);

        //TODO: check key generation is properly done
        if (event.getTimestamp() == null) event.setTimestamp(System.currentTimeMillis());

        apply(event);
    }

    private String createUUID() {
        return UUID.randomUUID().toString();
    }

    @EventSourcingHandler
    public void on(PowerGeneratedEvent event) {
        //TODO: business logic here

    }

    @EventSourcingHandler
    public void on(급전지시됨Event event) {
        //TODO: business logic here

    }

    @EventSourcingHandler
    public void on(입찰됨Event event) {
        BeanUtils.copyProperties(event, this);
        //TODO: business logic here

    }
}
