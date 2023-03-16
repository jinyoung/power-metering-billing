package power.plant.api;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import power.plant.query.*;
import reactor.core.publisher.Flux;

@RestController
public class PowerGenerationViewQueryController {

    private final QueryGateway queryGateway;

    private final ReactorQueryGateway reactorQueryGateway;

    public PowerGenerationViewQueryController(
        QueryGateway queryGateway,
        ReactorQueryGateway reactorQueryGateway
    ) {
        this.queryGateway = queryGateway;
        this.reactorQueryGateway = reactorQueryGateway;
    }

    @GetMapping("/powerGenerations")
    public CompletableFuture findAll(PowerGenerationViewQuery query) {
        return queryGateway
            .query(
                query,
                ResponseTypes.multipleInstancesOf(
                    PowerGenerationReadModel.class
                )
            )
            .thenApply(resources -> {
                List modelList = new ArrayList<EntityModel<PowerGenerationReadModel>>();

                resources
                    .stream()
                    .forEach(resource -> {
                        modelList.add(hateoas(resource));
                    });

                CollectionModel<PowerGenerationReadModel> model = CollectionModel.of(
                    modelList
                );

                return new ResponseEntity<>(model, HttpStatus.OK);
            });
    }

    @GetMapping("/powerGenerations/{id}")
    public CompletableFuture findById(@PathVariable("id") String id) {
        PowerGenerationViewSingleQuery query = new PowerGenerationViewSingleQuery();
        query.setId(id);

        return queryGateway
            .query(
                query,
                ResponseTypes.optionalInstanceOf(PowerGenerationReadModel.class)
            )
            .thenApply(resource -> {
                if (!resource.isPresent()) {
                    return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
                }

                return new ResponseEntity<>(
                    hateoas(resource.get()),
                    HttpStatus.OK
                );
            })
            .exceptionally(ex -> {
                throw new RuntimeException(ex);
            });
    }

    EntityModel<PowerGenerationReadModel> hateoas(
        PowerGenerationReadModel resource
    ) {
        EntityModel<PowerGenerationReadModel> model = EntityModel.of(resource);

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

    @MessageMapping("powerGenerations.all")
    public Flux<PowerGenerationReadModel> subscribeAll() {
        return reactorQueryGateway.subscriptionQueryMany(
            new PowerGenerationViewQuery(),
            PowerGenerationReadModel.class
        );
    }

    @MessageMapping("powerGenerations.{id}.get")
    public Flux<PowerGenerationReadModel> subscribeSingle(
        @DestinationVariable String id
    ) {
        PowerGenerationViewSingleQuery query = new PowerGenerationViewSingleQuery();
        query.setId(id);

        return reactorQueryGateway.subscriptionQuery(
            query,
            PowerGenerationReadModel.class
        );
    }
}
