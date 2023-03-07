package power.metering.billing.api;

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
import power.metering.billing.query.*;
import reactor.core.publisher.Flux;

@RestController
public class SepViewQueryController {

    private final QueryGateway queryGateway;

    private final ReactorQueryGateway reactorQueryGateway;

    public SepViewQueryController(
        QueryGateway queryGateway,
        ReactorQueryGateway reactorQueryGateway
    ) {
        this.queryGateway = queryGateway;
        this.reactorQueryGateway = reactorQueryGateway;
    }

    @GetMapping("/seps")
    public CompletableFuture findAll(SepViewQuery query) {
        return queryGateway
            .query(query, ResponseTypes.multipleInstancesOf(SepReadModel.class))
            .thenApply(resources -> {
                List modelList = new ArrayList<EntityModel<SepReadModel>>();

                resources
                    .stream()
                    .forEach(resource -> {
                        modelList.add(hateoas(resource));
                    });

                CollectionModel<SepReadModel> model = CollectionModel.of(
                    modelList
                );

                return new ResponseEntity<>(model, HttpStatus.OK);
            });
    }

    @GetMapping("/seps/{id}")
    public CompletableFuture findById(@PathVariable("id") String id) {
        SepViewSingleQuery query = new SepViewSingleQuery();
        query.setId(id);

        return queryGateway
            .query(query, ResponseTypes.optionalInstanceOf(SepReadModel.class))
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

    EntityModel<SepReadModel> hateoas(SepReadModel resource) {
        EntityModel<SepReadModel> model = EntityModel.of(resource);

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

    //<<< Etc / RSocket
    @MessageMapping("seps.all")
    public Flux<SepReadModel> subscribeAll() {
        return reactorQueryGateway.subscriptionQueryMany(
            new SepViewQuery(),
            SepReadModel.class
        );
    }

    @MessageMapping("seps.{id}.get")
    public Flux<SepReadModel> subscribeSingle(@DestinationVariable String id) {
        SepViewSingleQuery query = new SepViewSingleQuery();
        query.setId(id);

        return reactorQueryGateway.subscriptionQuery(query, SepReadModel.class);
    }
    //>>> Etc / RSocket

}
