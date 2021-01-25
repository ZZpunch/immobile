package br.com.imobzi.batch.facade;

import java.io.File;
import java.util.List;

import br.com.imobzi.batch.domain.ImmobileRequest;
import br.com.imobzi.batch.domain.ImmobileResponse;

public interface OrchestratorService {
     List<ImmobileResponse> orchestrator(final File inputStream,
                                               ImmobileRequest immobileRequest )throws Exception;
}
