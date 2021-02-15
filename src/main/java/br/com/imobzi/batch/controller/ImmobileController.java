package br.com.imobzi.batch.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.imobzi.batch.domain.ImmobileRequest;
import br.com.imobzi.batch.domain.ImmobileResponse;
import br.com.imobzi.batch.domain.ResponseBodyDefaut;
import br.com.imobzi.batch.facade.OrchestratorService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/immobile")
@CrossOrigin
@Slf4j
public class ImmobileController {

	@Autowired
	private OrchestratorService orchestratorService;

	private final String filePath = ("/tmp/temp.xlsx");

	@CrossOrigin
	@MessageMapping("/immobile")
	@SendTo("/topic/immobile")
	public ResponseEntity<ResponseBodyDefaut> processFiles(@RequestBody ImmobileRequest immobileRequest)
			throws Exception {

		log.info("Buscando arquivo temp salvo.");
		File file = new File(filePath);

		log.info("Arquivo " + file.getName() + " encontrado com sucesso!");

		List<ImmobileResponse> immobileResponse = this.orchestratorService.orchestrator(file, immobileRequest);

		log.info("Deletando arquivo temporario.");
		file.delete();
		log.info("Arquivo temporario deletado com sucesso!");

		return new ResponseEntity<ResponseBodyDefaut>(
				ResponseBodyDefaut.builder().status(true).message("Dados inseridos com sucesso!").build(),
				HttpStatus.CREATED);
	}
	
	@PostMapping("/test")
	public ResponseEntity<ResponseBodyDefaut> teste(@RequestBody ImmobileRequest immobileRequest)
			throws Exception {

		log.info("Buscando arquivo temp salvo.");
		File file = new File(filePath);

		log.info("Arquivo " + file.getName() + " encontrado com sucesso!");

		List<ImmobileResponse> immobileResponse = this.orchestratorService.orchestrator(file, immobileRequest);

		log.info("Deletando arquivo temporario.");
		file.delete();
		log.info("Arquivo temporario deletado com sucesso!");

		return new ResponseEntity<ResponseBodyDefaut>(
				ResponseBodyDefaut.builder().status(true).message("Dados inseridos com sucesso!").build(),
				HttpStatus.CREATED);
	}
	

	@CrossOrigin
	@PostMapping(path = "/create", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<?> processFile(@RequestParam(value = "files") MultipartFile[] files) throws Exception {

		log.info("Arquivo recebido com sucesso.");

		List<ImmobileResponse> immobileResponse = new ArrayList<>();
		for (final MultipartFile file : files) {
			log.info("Salvando o arquivo recebido.");
			file.transferTo(new File(filePath));
			log.info("Arquivo salvo com sucesso.");
		}
		return new ResponseEntity(HttpStatus.CREATED);
	}
}
