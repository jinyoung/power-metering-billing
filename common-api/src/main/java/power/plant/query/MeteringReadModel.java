package power.plant.query;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import org.springframework.hateoas.server.core.Relation;

@Entity
@Table(name = "Metering_table")
@Data
@Relation(collectionRelation = "meterings")
public class MeteringReadModel {

    @Id
    private String id;

    private String yearCode;

    private String monthCode;

    private String dayCode;

    private String subscriberId;

    private String platId;

    private Double generationAmount;

    private Double mep;
}
