package power.metering.billing.query;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import org.springframework.hateoas.server.core.Relation;

@Entity
@Table(name = "Sep_table")
@Data
@Relation(collectionRelation = "seps")
public class SepReadModel {

    @Id
    private String id;

    private String yearCode;

    private String monthCode;

    private String dayCode;

    private String subscriberId;

    private String platId;

    private Double generationAmount;

    private Double sep;
}
