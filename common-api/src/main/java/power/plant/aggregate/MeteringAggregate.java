package power.plant.aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.*;

import java.io.File;
import java.net.URLClassLoader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.ToString;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;
import power.plant.command.*;
import power.plant.event.*;
import power.plant.query.*;

@Aggregate
@Data
@ToString
public class MeteringAggregate {

    @AggregateIdentifier
    private String id;

    private String yearCode;
    private String monthCode;
    private String dayCode;
    private String subscriberId;
    private String plantId;
    private String generatorType;
    private Double generationAmount;
    private Double mep;

    private List<시간별측정> 시간별측정량;

    public MeteringAggregate() {}



    @CommandHandler
    public void handle(CalculateCommand command) {

        CalculatedEvent event = new CalculatedEvent();
        BeanUtils.copyProperties(command, event);
        event.setGeneratedPower(command.getGeneratedAmount());

        시간별측정 측정치 = new 시간별측정();
        측정치.setHourCode(event.getHourCode());
        측정치.setPower(event.getGeneratedPower());
        측정치.setMarketPrice(event.getMarketPrice());
        get시간별측정량().add(측정치);

        event.setGenerationAmount(calculateMEP());

        apply(event);
    }

    protected Double calculateMEP() {

        if(getGeneratorType()!=null)
        try{

            System.out.println(new File("..").getAbsoluteFile().getPath());
            File jarFile = new File("version1/target/metering-billing-logic-0.0.1-SNAPSHOT.jar");

            if(!jarFile.exists())
                throw new IllegalStateException("jar file is not found");

            ClassLoader classLoaderForSpecificVersion = new URLClassLoader(new URL[]{jarFile.toURL()}, Thread.currentThread().getContextClassLoader());
            Class generatorClass = classLoaderForSpecificVersion.loadClass("power.plant.aggregate."+ getGeneratorType());
            

            //Class generatorClass = Class.forName("power.plant.aggregate."+ getGeneratorType());

            if(!MeteringAggregate.class.isAssignableFrom(generatorClass))
                throw new IllegalStateException(getGeneratorType() + " is not a subtype of MeteringAggregate");

                
            MeteringAggregate generatorLogic = (MeteringAggregate) generatorClass.newInstance();
            BeanUtils.copyProperties(this, generatorLogic);
            
            Double mep = generatorLogic.calculateMEP();

            return mep;
        }catch(Exception e){
            throw new RuntimeException(e);
        }

        Double mep = get시간별측정량()
            .stream()
            .map(측정량 -> 측정량.marketPrice * 측정량.power)
            .reduce(0.0, (합계, 개별) -> 합계 + 개별);

        return mep;
    }

    @CommandHandler
    public MeteringAggregate(CreateMeterCommand command) {
        MeterCreatedEvent event = new MeterCreatedEvent();
        BeanUtils.copyProperties(command, event);

        //TODO: check key generation is properly done
        if (event.getId() == null) event.setId(createUUID());

        apply(event);
    }

    private String createUUID() {
        return UUID.randomUUID().toString();
    }

    @EventSourcingHandler
    public void on(CalculatedEvent event) {
        시간별측정 측정치 = new 시간별측정();
        측정치.setHourCode(event.getHourCode());
        측정치.setPower(event.getGeneratedPower());
        측정치.setMarketPrice(event.getMarketPrice());
        get시간별측정량().add(측정치);

        setGenerationAmount(event.getGenerationAmount());

    }

    @EventSourcingHandler
    public void on(MeterCreatedEvent event) {
        BeanUtils.copyProperties(event, this);
        set시간별측정량(new ArrayList<시간별측정>());

    }
}