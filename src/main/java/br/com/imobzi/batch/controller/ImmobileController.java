package br.com.imobzi.batch.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.imobzi.batch.domain.ImmobileRequest;
import br.com.imobzi.batch.domain.ImmobileResponse;
import br.com.imobzi.batch.facade.OrchestratorService;

@RestController
@RequestMapping("/immobile")
@CrossOrigin
public class ImmobileController {

    @Autowired
    private OrchestratorService orchestratorService;

    @CrossOrigin
    @MessageMapping("/immobile")
    @SendTo("/topic/immobile")
//    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
//    produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<ImmobileResponse>> saveImmobile(
            @RequestParam(value = "files") MultipartFile[] files,
            String immobile) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ImmobileRequest immobileRequest = null;
        try {
            immobileRequest = mapper.readValue(immobile, ImmobileRequest.class);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(null);
        }
        List<ImmobileResponse> immobileResponse = new ArrayList<>();
        for (final MultipartFile file : files) {
            immobileResponse = this.orchestratorService
                    .orchestrator(file, immobileRequest);
        }
        return new ResponseEntity(immobileResponse, HttpStatus.CREATED);
    }
    
    @CrossOrigin
    @GetMapping
    public ResponseEntity<?> getOkServer(){
    	return new ResponseEntity<>(HttpStatus.OK);
    }
}
