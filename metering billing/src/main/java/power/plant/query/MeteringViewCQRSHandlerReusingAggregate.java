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
@ProcessingGroup("meteringView")
public class MeteringViewCQRSHandlerReusingAggregate {

    @Autowired
    private MeteringReadModelRepository repository;

    @Autowired
    private QueryUpdateEmitter queryUpdateEmitter;

    @QueryHandler
    public List<MeteringReadModel> handle(MeteringViewQuery query) {
        return repository.findAll();
    }

    @QueryHandler
    public Optional<MeteringReadModel> handle(MeteringViewSingleQuery query) {
        return repository.findById(query.getId());
    }

    @EventHandler
    public void whenCalculated_then_UPDATE(CalculatedEvent event)
        throws Exception {
        repository
            .findById(event.getId())
            .ifPresent(entity -> {
                MeteringAggregate aggregate = new MeteringAggregate();

                BeanUtils.copyProperties(entity, aggregate);
                aggregate.set시간별측정량(new ArrayList<>());
                aggregate.on(event);
                BeanUtils.copyProperties(aggregate, entity);

                repository.save(entity);

                queryUpdateEmitter.emit(
                    MeteringViewSingleQuery.class,
                    query -> query.getId().equals(event.getId()),
                    entity
                );
            });
    }

    @EventHandler
    public void whenMeterCreated_then_CREATE(MeterCreatedEvent event)
        throws Exception {
        MeteringReadModel entity = new MeteringReadModel();
        MeteringAggregate aggregate = new MeteringAggregate();
        aggregate.on(event);

        BeanUtils.copyProperties(aggregate, entity);

        repository.save(entity);

        queryUpdateEmitter.emit(MeteringViewQuery.class, query -> true, entity);
    }
}
