package br.com.imobzi.batch.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.imobzi.batch.domain.ImmobileRequest;
import br.com.imobzi.batch.domain.ImmobileResponse;
import br.com.imobzi.batch.facade.OrchestratorService;

@RestController
@RequestMapping("/immobile")
@CrossOrigin
public class ImmobileController {

	@Autowired
	private OrchestratorService orchestratorService;
	
	private final String filePath = ("./temp.xlsx"); 

	@CrossOrigin
	@MessageMapping("/immobile")
	@SendTo("/topic/immobile")
	public ResponseEntity<List<ImmobileResponse>> processFiles(@RequestBody ImmobileRequest immobileRequest) throws Exception{
		
		File file = new File("./temp/temp.xlsx");
		List<ImmobileResponse> immobileResponse = this.orchestratorService.orchestrator(file, immobileRequest);
		
		file.delete();
		
		return new ResponseEntity<List<ImmobileResponse>>(immobileResponse, HttpStatus.CREATED);
	}
	
	
	
	@CrossOrigin
    @PostMapping(path = "/create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<?> processFile(@RequestParam(value = "files") MultipartFile[] files) 
			throws Exception {
		
		List<ImmobileResponse> immobileResponse = new ArrayList<>();
		for (final MultipartFile file : files) {
			file.transferTo(new File(filePath));
		}
		return new ResponseEntity(HttpStatus.CREATED);
	}
}
