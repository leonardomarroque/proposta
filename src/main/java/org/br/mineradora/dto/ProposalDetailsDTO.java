package org.br.mineradora.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProposalDetailsDTO(long proposalId, String customer, BigDecimal priceTonne, Integer tonnes, String country, Integer proposalValidityDays) {}
