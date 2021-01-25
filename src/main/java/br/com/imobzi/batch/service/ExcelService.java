package br.com.imobzi.batch.service;

import java.io.File;
import java.util.List;

import br.com.imobzi.batch.domain.Excel;
import br.com.imobzi.batch.domain.ImmobileRequest;

public interface ExcelService {
    List<Excel> readList(final File inputStream, ImmobileRequest immobileRequest) throws Exception;
}
