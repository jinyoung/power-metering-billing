package power.metering.billing.api;

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
import power.metering.billing.aggregate.*;
import power.metering.billing.command.*;

@RestController
public class SepController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    public SepController(
        CommandGateway commandGateway,
        QueryGateway queryGateway
    ) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @RequestMapping(
        value = "/seps/{id}/calculate",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public CompletableFuture calculate(
        @PathVariable("id") String id,
        @RequestBody CalculateCommand calculateCommand
    ) throws Exception {
        System.out.println("##### /sep/calculate  called #####");

        calculateCommand.setId(id);
        // send command
        return commandGateway.send(calculateCommand);
    }

    @RequestMapping(
        value = "/seps/{id}/createmeasure",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public CompletableFuture createMeasure(@PathVariable("id") String id)
        throws Exception {
        System.out.println("##### /sep/createMeasure  called #####");
        CreateMeasureCommand createMeasureCommand = new CreateMeasureCommand();
        createMeasureCommand.setId(id);
        // send command
        return commandGateway.send(createMeasureCommand);
    }

    @Autowired
    EventStore eventStore;

    @GetMapping(value = "/seps/{id}/events")
    public ResponseEntity getEvents(@PathVariable("id") String id) {
        ArrayList resources = new ArrayList<SepAggregate>();
        eventStore.readEvents(id).asStream().forEach(resources::add);

        CollectionModel<SepAggregate> model = CollectionModel.of(resources);

        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    EntityModel<SepAggregate> hateoas(SepAggregate resource) {
        EntityModel<SepAggregate> model = EntityModel.of(resource);

        model.add(Link.of("/seps/" + resource.getId()).withSelfRel());

        model.add(
            Link
                .of("/seps/" + resource.getId() + "/calculate")
                .withRel("calculate")
        );

        model.add(
            Link
                .of("/seps/" + resource.getId() + "/createmeasure")
                .withRel("createmeasure")
        );

        model.add(
            Link.of("/seps/" + resource.getId() + "/events").withRel("events")
        );

        return model;
    }
}
