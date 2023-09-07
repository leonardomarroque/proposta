package org.br.mineradora.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.br.mineradora.dto.ProposalDTO;
import org.br.mineradora.dto.ProposalDetailsDTO;
import org.br.mineradora.entity.ProposalEntity;
import org.br.mineradora.message.KafkaEvent;
import org.br.mineradora.repository.ProposalRepository;

import java.util.Date;

@ApplicationScoped
public class ProposalServiceImpl implements ProposalService {

    @Inject
    ProposalRepository proposalRepository;

    @Inject
    KafkaEvent kafkaEvent;

    @Override
    public ProposalDetailsDTO findFullProposal(long id) {

        ProposalEntity proposal = proposalRepository.findById(id);

        return ProposalDetailsDTO.builder()
                .proposalId(proposal.getId())
                .customer(proposal.getCustomer())
                .priceTonne(proposal.getPriceTonne())
                .tonnes(proposal.getTonnes())
                .country(proposal.getCountry())
                .proposalValidityDays(proposal.getProposalValidityDays())
                .build();
    }

    @Override
    @Transactional
    public ProposalDetailsDTO createNewProposal(ProposalDetailsDTO proposalDetailsDTO) {

        ProposalDTO proposal = buildAndSaveNewProposal(proposalDetailsDTO);

        kafkaEvent.sendNewKafkaEvent(proposal);

        return ProposalDetailsDTO.builder()
                .proposalId(proposal.proposalId())
                .customer(proposalDetailsDTO.customer())
                .priceTonne(proposalDetailsDTO.priceTonne())
                .country(proposalDetailsDTO.country())
                .tonnes(proposalDetailsDTO.tonnes())
                .proposalValidityDays(proposalDetailsDTO.proposalValidityDays())
                .build();

    }

    @Override
    @Transactional
    public void removeProposal(long id) {
        proposalRepository.deleteById(proposalRepository.findById(id).getId());
    }

    @Transactional
    public ProposalDTO buildAndSaveNewProposal(ProposalDetailsDTO proposalDetailsDTO) {

        try {

            ProposalEntity proposal = new ProposalEntity();

            proposal.setCreated(new Date());
            proposal.setProposalValidityDays(proposalDetailsDTO.proposalValidityDays());
            proposal.setCountry(proposalDetailsDTO.country());
            proposal.setCustomer(proposalDetailsDTO.customer());
            proposal.setPriceTonne(proposalDetailsDTO.priceTonne());
            proposal.setTonnes(proposalDetailsDTO.tonnes());

            proposalRepository.persistAndFlush(proposal);

            return ProposalDTO.builder()
                    .proposalId(proposal.getId())
                    .customer(proposal.getCustomer())
                    .priceTonne(proposal.getPriceTonne())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException();
        }


    }

}
