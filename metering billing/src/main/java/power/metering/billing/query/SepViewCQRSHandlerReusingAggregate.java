package power.metering.billing.query;

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
import power.metering.billing.aggregate.*;
import power.metering.billing.event.*;

@Service
@ProcessingGroup("sepView")
public class SepViewCQRSHandlerReusingAggregate {

    @Autowired
    private SepReadModelRepository repository;

    //<<< Etc / RSocket
    @Autowired
    private QueryUpdateEmitter queryUpdateEmitter;

    //>>> Etc / RSocket

    @QueryHandler
    public List<SepReadModel> handle(SepViewQuery query) {
        return repository.findAll();
    }

    @QueryHandler
    public Optional<SepReadModel> handle(SepViewSingleQuery query) {
        return repository.findById(query.getId());
    }

    @EventHandler
    public void whenMeasureCalculated_then_UPDATE(MeasureCalculatedEvent event)
        throws Exception {
        repository
            .findById(event.getId())
            .ifPresent(entity -> {
                SepAggregate aggregate = new SepAggregate();

                BeanUtils.copyProperties(entity, aggregate);
                aggregate.on(event);
                BeanUtils.copyProperties(aggregate, entity);

                repository.save(entity);

                //<<< Etc / RSocket
                queryUpdateEmitter.emit(
                    SepViewSingleQuery.class,
                    query -> query.getId().equals(event.getId()),
                    entity
                );
                //>>> Etc / RSocket

            });
    }
}
