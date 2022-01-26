package cl.vankam.inversion.domain;

import static org.assertj.core.api.Assertions.assertThat;

import cl.vankam.inversion.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class InvestmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Investment.class);
        Investment investment1 = new Investment();
        investment1.setId(UUID.randomUUID());
        Investment investment2 = new Investment();
        investment2.setId(investment1.getId());
        assertThat(investment1).isEqualTo(investment2);
        investment2.setId(UUID.randomUUID());
        assertThat(investment1).isNotEqualTo(investment2);
        investment1.setId(null);
        assertThat(investment1).isNotEqualTo(investment2);
    }
}
