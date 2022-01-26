package cl.vankam.inversion.web.rest;

import cl.vankam.inversion.domain.Investment;
import cl.vankam.inversion.repository.InvestmentRepository;
import cl.vankam.inversion.service.InvestmentService;
import cl.vankam.inversion.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link cl.vankam.inversion.domain.Investment}.
 */
@RestController
@RequestMapping("/api")
public class InvestmentResource {

    private final Logger log = LoggerFactory.getLogger(InvestmentResource.class);

    private static final String ENTITY_NAME = "investment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvestmentService investmentService;

    private final InvestmentRepository investmentRepository;

    public InvestmentResource(InvestmentService investmentService, InvestmentRepository investmentRepository) {
        this.investmentService = investmentService;
        this.investmentRepository = investmentRepository;
    }

    /**
     * {@code POST  /investments} : Create a new investment.
     *
     * @param investment the investment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new investment, or with status {@code 400 (Bad Request)} if the investment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/investments")
    public ResponseEntity<Investment> createInvestment(@RequestBody Investment investment) throws URISyntaxException {
        log.debug("REST request to save Investment : {}", investment);
        if (investment.getId() != null) {
            throw new BadRequestAlertException("A new investment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Investment result = investmentService.save(investment);
        return ResponseEntity
            .created(new URI("/api/investments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /investments/:id} : Updates an existing investment.
     *
     * @param id the id of the investment to save.
     * @param investment the investment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated investment,
     * or with status {@code 400 (Bad Request)} if the investment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the investment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/investments/{id}")
    public ResponseEntity<Investment> updateInvestment(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody Investment investment
    ) throws URISyntaxException {
        log.debug("REST request to update Investment : {}, {}", id, investment);
        if (investment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, investment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!investmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Investment result = investmentService.save(investment);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, investment.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /investments/:id} : Partial updates given fields of an existing investment, field will ignore if it is null
     *
     * @param id the id of the investment to save.
     * @param investment the investment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated investment,
     * or with status {@code 400 (Bad Request)} if the investment is not valid,
     * or with status {@code 404 (Not Found)} if the investment is not found,
     * or with status {@code 500 (Internal Server Error)} if the investment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/investments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Investment> partialUpdateInvestment(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody Investment investment
    ) throws URISyntaxException {
        log.debug("REST request to partial update Investment partially : {}, {}", id, investment);
        if (investment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, investment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!investmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Investment> result = investmentService.partialUpdate(investment);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, investment.getId().toString())
        );
    }

    /**
     * {@code GET  /investments} : get all the investments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of investments in body.
     */
    @GetMapping("/investments")
    public ResponseEntity<List<Investment>> getAllInvestments(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Investments");
        Page<Investment> page = investmentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /investments/:id} : get the "id" investment.
     *
     * @param id the id of the investment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the investment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/investments/{id}")
    public ResponseEntity<Investment> getInvestment(@PathVariable UUID id) {
        log.debug("REST request to get Investment : {}", id);
        Optional<Investment> investment = investmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(investment);
    }

    /**
     * {@code DELETE  /investments/:id} : delete the "id" investment.
     *
     * @param id the id of the investment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/investments/{id}")
    public ResponseEntity<Void> deleteInvestment(@PathVariable UUID id) {
        log.debug("REST request to delete Investment : {}", id);
        investmentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
