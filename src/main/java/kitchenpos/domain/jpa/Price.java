package kitchenpos.domain.jpa;

import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class Price {

    private final BigDecimal price;

    public Price(BigDecimal price) {
        validate(price);
        this.price = price;
    }

    private void validate(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public Price(long price) {
        this(BigDecimal.valueOf(price));
    }

    public Price() {
        price = BigDecimal.ZERO;
    }


    public BigDecimal getPrice() {
        return price;
    }
}
