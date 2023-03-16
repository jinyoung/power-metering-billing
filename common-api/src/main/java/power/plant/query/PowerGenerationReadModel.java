package power.plant.query;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import org.springframework.hateoas.server.core.Relation;

@Entity
@Table(name = "PowerGeneration_table")
@Data
@Relation(collectionRelation = "powerGenerations")
public class PowerGenerationReadModel {

    @Id
    private String id;

    private String subscriberId;

    private String plantId;

    private Double generatedAmount;

    private String generatorType;
}
