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
public class 수력MeteringAggregate extends MeteringAggregate{

   
    private Double 수력관련속성;

    public 수력MeteringAggregate() {}



    @CommandHandler
    public void handle(CalculateCommand command) {

        //command.getGeneratorType()

        CalculatedEvent event = new CalculatedEvent();
        BeanUtils.copyProperties(command, event);

        apply(event);
    }

    @CommandHandler
    public 수력MeteringAggregate(수력CreateMeterCommand command) {
        super(command);
    }


    @EventSourcingHandler
    public void on(CalculatedEvent event) {
        //TODO: business logic here
        
    }

}
