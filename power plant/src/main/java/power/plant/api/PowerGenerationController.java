package power.plant.api;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import power.plant.aggregate.*;
import power.plant.command.*;

@RestController
public class PowerGenerationController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    public PowerGenerationController(
        CommandGateway commandGateway,
        QueryGateway queryGateway
    ) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @RequestMapping(value = "/powerGenerations", method = RequestMethod.POST)
    public CompletableFuture generate(
        @RequestBody GenerateCommand generateCommand
    ) throws Exception {
        System.out.println("##### /powerGeneration/generate  called #####");

        // send command
        return commandGateway
            .send(generateCommand)
            .thenApply(id -> {
                PowerGenerationAggregate resource = new PowerGenerationAggregate();
                BeanUtils.copyProperties(generateCommand, resource);

                resource.setTimestamp((Long) id);

                return new ResponseEntity<>(hateoas(resource), HttpStatus.OK);
            });
    }

    @Autowired
    EventStore eventStore;

    @GetMapping(value = "/powerGenerations/{id}/events")
    public ResponseEntity getEvents(@PathVariable("id") String id) {
        ArrayList resources = new ArrayList<PowerGenerationAggregate>();
        eventStore.readEvents(id).asStream().forEach(resources::add);

        CollectionModel<PowerGenerationAggregate> model = CollectionModel.of(
            resources
        );

        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    EntityModel<PowerGenerationAggregate> hateoas(
        PowerGenerationAggregate resource
    ) {
        EntityModel<PowerGenerationAggregate> model = EntityModel.of(resource);

        model.add(
            Link
                .of("/powerGenerations/" + resource.getTimestamp())
                .withSelfRel()
        );

        model.add(
            Link
                .of("/powerGenerations/" + resource.getTimestamp() + "/events")
                .withRel("events")
        );

        return model;
    }
}
