package cl.vankam.inversion.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * The Investment entity.
 */
@Schema(description = "The Investment entity.")
@Entity
@Table(name = "investment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Investment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    @Column(name = "id", length = 36)
    private UUID id;

    @Column(name = "rut")
    private String rut;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "update_date")
    private Instant updateDate;

    @Column(name = "total_amount")
    private String totalAmount;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Investment id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRut() {
        return this.rut;
    }

    public Investment rut(String rut) {
        this.setRut(rut);
        return this;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public Investment startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getUpdateDate() {
        return this.updateDate;
    }

    public Investment updateDate(Instant updateDate) {
        this.setUpdateDate(updateDate);
        return this;
    }

    public void setUpdateDate(Instant updateDate) {
        this.updateDate = updateDate;
    }

    public String getTotalAmount() {
        return this.totalAmount;
    }

    public Investment totalAmount(String totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Investment)) {
            return false;
        }
        return id != null && id.equals(((Investment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Investment{" +
            "id=" + getId() +
            ", rut='" + getRut() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", updateDate='" + getUpdateDate() + "'" +
            ", totalAmount='" + getTotalAmount() + "'" +
            "}";
    }
}
