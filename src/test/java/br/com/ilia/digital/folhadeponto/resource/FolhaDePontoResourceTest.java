package br.com.ilia.digital.folhadeponto.resource;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ilia.digital.folhadeponto.persistence.model.Momento;
import br.com.ilia.digital.folhadeponto.service.FolhaDePontoService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class FolhaDePontoResourceTest {
	
	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FolhaDePontoService service;
    
    private static Momento momento1;
    private static Momento momento2;
    private static Momento momento3;
    private static Momento momento4;
    private static Momento momento5;
    private static Momento momento6;
    private static Momento momentoDataInvalida;
    private static Momento momentoSemCampoObrigatorio;
    
    @BeforeAll
    private static void init() {
    	initValues();
    }

	@Test
	@Order(1)
    public void testRegistroBatida() throws JsonProcessingException, Exception {
    	mockMvc.perform(post("/v1/batidas")
    	        .contentType("application/json")
    	        .content(objectMapper.writeValueAsString(momento1)))
    	        .andExpect(status().isCreated()); 
    }
	
	@Test
	@Order(2)
	public void testRegistroBatidaMenos1HoraAlmoco() throws JsonProcessingException, Exception {
		mockMvc.perform(post("/v1/batidas")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(momento2)))
		.andExpect(status().isCreated());
		
		mockMvc.perform(post("/v1/batidas")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(momento3)))
		.andExpect(status().isForbidden()); 
	}
	
	@Test
	@Order(3)
	public void testRegistroBatidaDataInvalida() throws JsonProcessingException, Exception {
		mockMvc.perform(post("/v1/batidas")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(momentoDataInvalida)))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@Order(4)
	public void testRegistroBatidaSemCampoObrigatorio() throws JsonProcessingException, Exception {
		mockMvc.perform(post("/v1/batidas")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(momentoSemCampoObrigatorio)))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@Order(5)
	public void testRegistroBatidaMaisDe4Batidas() throws JsonProcessingException, Exception {
		mockMvc.perform(post("/v1/batidas")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(momento4)))
		.andExpect(status().isCreated());
		
		mockMvc.perform(post("/v1/batidas")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(momento5)))
		.andExpect(status().isCreated()); 
		
		mockMvc.perform(post("/v1/batidas")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(momento6)))
		.andExpect(status().isForbidden()); 
	}
	
	private static void initValues() {
		momento1 = new Momento("2018-08-22T08:00:00");
		momento2 = new Momento("2018-08-22T12:00:00");
		momento3 = new Momento("2018-08-22T12:30:00");
		momento4 = new Momento("2018-08-22T13:00:00");
		momento5 = new Momento("2018-08-22T18:00:00");
		momento6 = new Momento("2018-08-22T19:00:00");
		
		momentoDataInvalida = new Momento("2018-02-30T08:00:00");
		momentoSemCampoObrigatorio = new Momento();
	}
}
