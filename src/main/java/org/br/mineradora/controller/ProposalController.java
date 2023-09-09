package org.br.mineradora.controller;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import org.br.mineradora.dto.ProposalDetailsDTO;
import org.br.mineradora.service.ProposalService;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/api/proposal")
@Authenticated
public class ProposalController {

    private final Logger LOG = LoggerFactory.getLogger(ProposalController.class);

    @Inject
    JsonWebToken jsonWebToken;

    @Inject
    ProposalService proposalService;

    @GET()
    @Path("/{id}")
    @RolesAllowed({"user", "manager"})
    public Response findProposalDetails(@PathParam("id") long id) {
        try {
            return Response.ok(proposalService.findFullProposal(id)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Resource not found for id: " + id).build();
        }
    }

    @POST
    @RolesAllowed("proposal-customer")
    public Response createProposal (ProposalDetailsDTO proposalDetails, @Context UriInfo uriInfo) {

        LOG.info("-- Receiving purchase proposal --");

        try {

            ProposalDetailsDTO proposal = proposalService.createNewProposal(proposalDetails);

            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
            uriBuilder.path(Long.toString(proposal.proposalId()));

            return Response.created(uriBuilder.build()).entity(proposal).build();

        } catch (Exception e) {

            return Response.serverError().build();
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("manager")
    public Response removeProposal(@PathParam("id") long id) {
        try {

            proposalService.removeProposal(id);
            return Response.noContent().build();

        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Resource not found for id: " + id).build();
        }
    }

}
