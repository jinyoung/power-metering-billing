package power.plant.policy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.DisallowReplay;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import power.plant.aggregate.*;
import power.plant.command.*;
import power.plant.event.*;

@Service
@ProcessingGroup("meteringBilling")
public class PolicyHandler {

    @Autowired
    CommandGateway commandGateway;

    @EventHandler
    //@DisallowReplay
    public void wheneverPowerGenerated_Calculate(
        PowerGeneratedEvent powerGenerated
    ) {
        System.out.println(powerGenerated.toString());

        CalculateCommand command = new CalculateCommand();
        //TODO: mapping attributes (anti-corruption)
        commandGateway.send(command);
    }

    @EventHandler
    //@DisallowReplay
    public void whenever입찰됨_CreateMeter(입찰됨Event 입찰됨) {
        System.out.println(입찰됨.toString());

        CreateMeterCommand command = new CreateMeterCommand();
        //TODO: mapping attributes (anti-corruption)
        commandGateway.send(command);
    }
}
