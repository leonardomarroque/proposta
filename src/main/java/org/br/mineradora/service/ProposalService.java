package org.br.mineradora.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.br.mineradora.dto.ProposalDetailsDTO;

@ApplicationScoped
public interface ProposalService {

    ProposalDetailsDTO findFullProposal(long id);

    ProposalDetailsDTO createNewProposal(ProposalDetailsDTO proposalDetailsDTO);

    void removeProposal(long id);
}
