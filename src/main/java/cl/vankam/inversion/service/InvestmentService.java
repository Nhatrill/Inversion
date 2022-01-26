package cl.vankam.inversion.service;

import cl.vankam.inversion.domain.Investment;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Investment}.
 */
public interface InvestmentService {
    /**
     * Save a investment.
     *
     * @param investment the entity to save.
     * @return the persisted entity.
     */
    Investment save(Investment investment);

    /**
     * Partially updates a investment.
     *
     * @param investment the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Investment> partialUpdate(Investment investment);

    /**
     * Get all the investments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Investment> findAll(Pageable pageable);

    /**
     * Get the "id" investment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Investment> findOne(UUID id);

    /**
     * Delete the "id" investment.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
