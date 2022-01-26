package cl.vankam.inversion.service.impl;

import cl.vankam.inversion.domain.Investment;
import cl.vankam.inversion.repository.InvestmentRepository;
import cl.vankam.inversion.service.InvestmentService;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Investment}.
 */
@Service
@Transactional
public class InvestmentServiceImpl implements InvestmentService {

    private final Logger log = LoggerFactory.getLogger(InvestmentServiceImpl.class);

    private final InvestmentRepository investmentRepository;

    public InvestmentServiceImpl(InvestmentRepository investmentRepository) {
        this.investmentRepository = investmentRepository;
    }

    @Override
    public Investment save(Investment investment) {
        log.debug("Request to save Investment : {}", investment);
        return investmentRepository.save(investment);
    }

    @Override
    public Optional<Investment> partialUpdate(Investment investment) {
        log.debug("Request to partially update Investment : {}", investment);

        return investmentRepository
            .findById(investment.getId())
            .map(existingInvestment -> {
                if (investment.getRut() != null) {
                    existingInvestment.setRut(investment.getRut());
                }
                if (investment.getStartDate() != null) {
                    existingInvestment.setStartDate(investment.getStartDate());
                }
                if (investment.getUpdateDate() != null) {
                    existingInvestment.setUpdateDate(investment.getUpdateDate());
                }
                if (investment.getTotalAmount() != null) {
                    existingInvestment.setTotalAmount(investment.getTotalAmount());
                }

                return existingInvestment;
            })
            .map(investmentRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Investment> findAll(Pageable pageable) {
        log.debug("Request to get all Investments");
        return investmentRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Investment> findOne(UUID id) {
        log.debug("Request to get Investment : {}", id);
        return investmentRepository.findById(id);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Investment : {}", id);
        investmentRepository.deleteById(id);
    }
}
