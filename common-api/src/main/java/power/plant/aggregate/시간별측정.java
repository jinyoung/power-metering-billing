package power.plant.aggregate;

import lombok.*;

@Data
public class 시간별측정 {
    Long hourCode;
    Double power;
    Double marketPrice;
}
