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

    @RequestMapping(
        value = "/powerGenerations/{id}/generate",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public CompletableFuture generate(
        @PathVariable("id") String id,
        @RequestBody GenerateCommand generateCommand
    ) throws Exception {
        System.out.println("##### /powerGeneration/generate  called #####");

        generateCommand.setId(id);
        // send command
        return commandGateway.send(generateCommand);
    }

    @RequestMapping(value = "/powerGenerations", method = RequestMethod.POST)
    public CompletableFuture 입찰(@RequestBody 입찰Command 입찰Command)
        throws Exception {
        System.out.println("##### /powerGeneration/입찰  called #####");

        // send command
        return commandGateway
            .send(입찰Command)
            .thenApply(id -> {
                PowerGenerationAggregate resource = new PowerGenerationAggregate();
                BeanUtils.copyProperties(입찰Command, resource);

                resource.setId((String) id);

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
            Link.of("/powerGenerations/" + resource.getId()).withSelfRel()
        );

        model.add(
            Link
                .of("/powerGenerations/" + resource.getId() + "/generate")
                .withRel("generate")
        );

        model.add(
            Link
                .of("/powerGenerations/" + resource.getId() + "/events")
                .withRel("events")
        );

        return model;
    }
}
