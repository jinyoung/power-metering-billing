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
public class MeteringViewQueryController {

    private final QueryGateway queryGateway;

    private final ReactorQueryGateway reactorQueryGateway;

    public MeteringViewQueryController(
        QueryGateway queryGateway,
        ReactorQueryGateway reactorQueryGateway
    ) {
        this.queryGateway = queryGateway;
        this.reactorQueryGateway = reactorQueryGateway;
    }

    @GetMapping("/meterings")
    public CompletableFuture findAll(MeteringViewQuery query) {
        return queryGateway
            .query(
                query,
                ResponseTypes.multipleInstancesOf(MeteringReadModel.class)
            )
            .thenApply(resources -> {
                List modelList = new ArrayList<EntityModel<MeteringReadModel>>();

                resources
                    .stream()
                    .forEach(resource -> {
                        modelList.add(hateoas(resource));
                    });

                CollectionModel<MeteringReadModel> model = CollectionModel.of(
                    modelList
                );

                return new ResponseEntity<>(model, HttpStatus.OK);
            });
    }

    @GetMapping("/meterings/{id}")
    public CompletableFuture findById(@PathVariable("id") String id) {
        MeteringViewSingleQuery query = new MeteringViewSingleQuery();
        query.setId(id);

        return queryGateway
            .query(
                query,
                ResponseTypes.optionalInstanceOf(MeteringReadModel.class)
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

    EntityModel<MeteringReadModel> hateoas(MeteringReadModel resource) {
        EntityModel<MeteringReadModel> model = EntityModel.of(resource);

        model.add(Link.of("/meterings/" + resource.getId()).withSelfRel());

        model.add(
            Link
                .of("/meterings/" + resource.getId() + "/calculate")
                .withRel("calculate")
        );

        model.add(
            Link
                .of("/meterings/" + resource.getId() + "/events")
                .withRel("events")
        );

        return model;
    }

    @MessageMapping("meterings.all")
    public Flux<MeteringReadModel> subscribeAll() {
        return reactorQueryGateway.subscriptionQueryMany(
            new MeteringViewQuery(),
            MeteringReadModel.class
        );
    }

    @MessageMapping("meterings.{id}.get")
    public Flux<MeteringReadModel> subscribeSingle(
        @DestinationVariable String id
    ) {
        MeteringViewSingleQuery query = new MeteringViewSingleQuery();
        query.setId(id);

        return reactorQueryGateway.subscriptionQuery(
            query,
            MeteringReadModel.class
        );
    }
}
