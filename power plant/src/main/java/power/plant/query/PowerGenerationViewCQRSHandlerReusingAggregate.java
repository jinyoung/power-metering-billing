package power.plant.query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import power.plant.aggregate.*;
import power.plant.event.*;

@Service
@ProcessingGroup("powerGenerationView")
public class PowerGenerationViewCQRSHandlerReusingAggregate {

    @Autowired
    private PowerGenerationReadModelRepository repository;

    @Autowired
    private QueryUpdateEmitter queryUpdateEmitter;

    @QueryHandler
    public List<PowerGenerationReadModel> handle(
        PowerGenerationViewQuery query
    ) {
        return repository.findAll();
    }

    @QueryHandler
    public Optional<PowerGenerationReadModel> handle(
        PowerGenerationViewSingleQuery query
    ) {
        return repository.findById(query.getId());
    }

    @EventHandler
    public void whenPowerGenerated_then_UPDATE(PowerGeneratedEvent event)
        throws Exception {
        repository
            .findById(event.getId())
            .ifPresent(entity -> {
                PowerGenerationAggregate aggregate = new PowerGenerationAggregate();

                BeanUtils.copyProperties(entity, aggregate);
                aggregate.on(event);
                BeanUtils.copyProperties(aggregate, entity);

                repository.save(entity);

                queryUpdateEmitter.emit(
                    PowerGenerationViewSingleQuery.class,
                    query -> query.getId().equals(event.getId()),
                    entity
                );
            });
    }

    @EventHandler
    public void when급전지시됨_then_UPDATE(급전지시됨Event event)
        throws Exception {
        repository
            .findById(event.getId())
            .ifPresent(entity -> {
                PowerGenerationAggregate aggregate = new PowerGenerationAggregate();

                BeanUtils.copyProperties(entity, aggregate);
                aggregate.on(event);
                BeanUtils.copyProperties(aggregate, entity);

                repository.save(entity);

                queryUpdateEmitter.emit(
                    PowerGenerationViewSingleQuery.class,
                    query -> query.getId().equals(event.getId()),
                    entity
                );
            });
    }

    @EventHandler
    public void when입찰됨_then_CREATE(입찰됨Event event) throws Exception {
        PowerGenerationReadModel entity = new PowerGenerationReadModel();
        PowerGenerationAggregate aggregate = new PowerGenerationAggregate();
        aggregate.on(event);

        BeanUtils.copyProperties(aggregate, entity);

        repository.save(entity);

        queryUpdateEmitter.emit(
            PowerGenerationViewQuery.class,
            query -> true,
            entity
        );
    }
}
