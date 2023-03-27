package power.plant.aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.*;

import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.ToString;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;


@Aggregate
@Data
@ToString
public class 수력 extends MeteringAggregate{

   
    private Double 수력관련속성 = 0.9;

    public 수력() {}
    
    @Override
    protected Double calculateMEP() {

System.out.println("version 15 is used");

        Double mep = get시간별측정량()
            .stream()
            .map(측정량 -> 측정량.getMarketPrice() * 측정량.getPower() * 수력관련속성)
            .reduce(0.0, (합계, 개별) -> 합계 + 개별 );

        return mep;
    }


}
