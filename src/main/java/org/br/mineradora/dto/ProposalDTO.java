package org.br.mineradora.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProposalDTO(long proposalId, String customer, BigDecimal priceTonne) {}
