package cl.vankam.inversion.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cl.vankam.inversion.IntegrationTest;
import cl.vankam.inversion.domain.Investment;
import cl.vankam.inversion.repository.InvestmentRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link InvestmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InvestmentResourceIT {

    private static final String DEFAULT_RUT = "AAAAAAAAAA";
    private static final String UPDATED_RUT = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_TOTAL_AMOUNT = "AAAAAAAAAA";
    private static final String UPDATED_TOTAL_AMOUNT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/investments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private InvestmentRepository investmentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvestmentMockMvc;

    private Investment investment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Investment createEntity(EntityManager em) {
        Investment investment = new Investment()
            .rut(DEFAULT_RUT)
            .startDate(DEFAULT_START_DATE)
            .updateDate(DEFAULT_UPDATE_DATE)
            .totalAmount(DEFAULT_TOTAL_AMOUNT);
        return investment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Investment createUpdatedEntity(EntityManager em) {
        Investment investment = new Investment()
            .rut(UPDATED_RUT)
            .startDate(UPDATED_START_DATE)
            .updateDate(UPDATED_UPDATE_DATE)
            .totalAmount(UPDATED_TOTAL_AMOUNT);
        return investment;
    }

    @BeforeEach
    public void initTest() {
        investment = createEntity(em);
    }

    @Test
    @Transactional
    void createInvestment() throws Exception {
        int databaseSizeBeforeCreate = investmentRepository.findAll().size();
        // Create the Investment
        restInvestmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(investment)))
            .andExpect(status().isCreated());

        // Validate the Investment in the database
        List<Investment> investmentList = investmentRepository.findAll();
        assertThat(investmentList).hasSize(databaseSizeBeforeCreate + 1);
        Investment testInvestment = investmentList.get(investmentList.size() - 1);
        assertThat(testInvestment.getRut()).isEqualTo(DEFAULT_RUT);
        assertThat(testInvestment.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testInvestment.getUpdateDate()).isEqualTo(DEFAULT_UPDATE_DATE);
        assertThat(testInvestment.getTotalAmount()).isEqualTo(DEFAULT_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void createInvestmentWithExistingId() throws Exception {
        // Create the Investment with an existing ID
        investmentRepository.saveAndFlush(investment);

        int databaseSizeBeforeCreate = investmentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvestmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(investment)))
            .andExpect(status().isBadRequest());

        // Validate the Investment in the database
        List<Investment> investmentList = investmentRepository.findAll();
        assertThat(investmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInvestments() throws Exception {
        // Initialize the database
        investmentRepository.saveAndFlush(investment);

        // Get all the investmentList
        restInvestmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(investment.getId().toString())))
            .andExpect(jsonPath("$.[*].rut").value(hasItem(DEFAULT_RUT)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].updateDate").value(hasItem(DEFAULT_UPDATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(DEFAULT_TOTAL_AMOUNT)));
    }

    @Test
    @Transactional
    void getInvestment() throws Exception {
        // Initialize the database
        investmentRepository.saveAndFlush(investment);

        // Get the investment
        restInvestmentMockMvc
            .perform(get(ENTITY_API_URL_ID, investment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(investment.getId().toString()))
            .andExpect(jsonPath("$.rut").value(DEFAULT_RUT))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.updateDate").value(DEFAULT_UPDATE_DATE.toString()))
            .andExpect(jsonPath("$.totalAmount").value(DEFAULT_TOTAL_AMOUNT));
    }

    @Test
    @Transactional
    void getNonExistingInvestment() throws Exception {
        // Get the investment
        restInvestmentMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInvestment() throws Exception {
        // Initialize the database
        investmentRepository.saveAndFlush(investment);

        int databaseSizeBeforeUpdate = investmentRepository.findAll().size();

        // Update the investment
        Investment updatedInvestment = investmentRepository.findById(investment.getId()).get();
        // Disconnect from session so that the updates on updatedInvestment are not directly saved in db
        em.detach(updatedInvestment);
        updatedInvestment.rut(UPDATED_RUT).startDate(UPDATED_START_DATE).updateDate(UPDATED_UPDATE_DATE).totalAmount(UPDATED_TOTAL_AMOUNT);

        restInvestmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInvestment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedInvestment))
            )
            .andExpect(status().isOk());

        // Validate the Investment in the database
        List<Investment> investmentList = investmentRepository.findAll();
        assertThat(investmentList).hasSize(databaseSizeBeforeUpdate);
        Investment testInvestment = investmentList.get(investmentList.size() - 1);
        assertThat(testInvestment.getRut()).isEqualTo(UPDATED_RUT);
        assertThat(testInvestment.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testInvestment.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
        assertThat(testInvestment.getTotalAmount()).isEqualTo(UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void putNonExistingInvestment() throws Exception {
        int databaseSizeBeforeUpdate = investmentRepository.findAll().size();
        investment.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvestmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, investment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(investment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Investment in the database
        List<Investment> investmentList = investmentRepository.findAll();
        assertThat(investmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvestment() throws Exception {
        int databaseSizeBeforeUpdate = investmentRepository.findAll().size();
        investment.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvestmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(investment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Investment in the database
        List<Investment> investmentList = investmentRepository.findAll();
        assertThat(investmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvestment() throws Exception {
        int databaseSizeBeforeUpdate = investmentRepository.findAll().size();
        investment.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvestmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(investment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Investment in the database
        List<Investment> investmentList = investmentRepository.findAll();
        assertThat(investmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvestmentWithPatch() throws Exception {
        // Initialize the database
        investmentRepository.saveAndFlush(investment);

        int databaseSizeBeforeUpdate = investmentRepository.findAll().size();

        // Update the investment using partial update
        Investment partialUpdatedInvestment = new Investment();
        partialUpdatedInvestment.setId(investment.getId());

        partialUpdatedInvestment.totalAmount(UPDATED_TOTAL_AMOUNT);

        restInvestmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvestment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvestment))
            )
            .andExpect(status().isOk());

        // Validate the Investment in the database
        List<Investment> investmentList = investmentRepository.findAll();
        assertThat(investmentList).hasSize(databaseSizeBeforeUpdate);
        Investment testInvestment = investmentList.get(investmentList.size() - 1);
        assertThat(testInvestment.getRut()).isEqualTo(DEFAULT_RUT);
        assertThat(testInvestment.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testInvestment.getUpdateDate()).isEqualTo(DEFAULT_UPDATE_DATE);
        assertThat(testInvestment.getTotalAmount()).isEqualTo(UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void fullUpdateInvestmentWithPatch() throws Exception {
        // Initialize the database
        investmentRepository.saveAndFlush(investment);

        int databaseSizeBeforeUpdate = investmentRepository.findAll().size();

        // Update the investment using partial update
        Investment partialUpdatedInvestment = new Investment();
        partialUpdatedInvestment.setId(investment.getId());

        partialUpdatedInvestment
            .rut(UPDATED_RUT)
            .startDate(UPDATED_START_DATE)
            .updateDate(UPDATED_UPDATE_DATE)
            .totalAmount(UPDATED_TOTAL_AMOUNT);

        restInvestmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvestment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvestment))
            )
            .andExpect(status().isOk());

        // Validate the Investment in the database
        List<Investment> investmentList = investmentRepository.findAll();
        assertThat(investmentList).hasSize(databaseSizeBeforeUpdate);
        Investment testInvestment = investmentList.get(investmentList.size() - 1);
        assertThat(testInvestment.getRut()).isEqualTo(UPDATED_RUT);
        assertThat(testInvestment.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testInvestment.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
        assertThat(testInvestment.getTotalAmount()).isEqualTo(UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void patchNonExistingInvestment() throws Exception {
        int databaseSizeBeforeUpdate = investmentRepository.findAll().size();
        investment.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvestmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, investment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(investment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Investment in the database
        List<Investment> investmentList = investmentRepository.findAll();
        assertThat(investmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvestment() throws Exception {
        int databaseSizeBeforeUpdate = investmentRepository.findAll().size();
        investment.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvestmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(investment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Investment in the database
        List<Investment> investmentList = investmentRepository.findAll();
        assertThat(investmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvestment() throws Exception {
        int databaseSizeBeforeUpdate = investmentRepository.findAll().size();
        investment.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvestmentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(investment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Investment in the database
        List<Investment> investmentList = investmentRepository.findAll();
        assertThat(investmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvestment() throws Exception {
        // Initialize the database
        investmentRepository.saveAndFlush(investment);

        int databaseSizeBeforeDelete = investmentRepository.findAll().size();

        // Delete the investment
        restInvestmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, investment.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Investment> investmentList = investmentRepository.findAll();
        assertThat(investmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
