package br.com.ilia.digital.folhadeponto.resource;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ilia.digital.folhadeponto.persistence.model.Alocacao;
import br.com.ilia.digital.folhadeponto.persistence.model.Momento;
import br.com.ilia.digital.folhadeponto.util.TestUtils;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Duration;

import com.google.gson.JsonParser;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class FolhaDePontoResourceTest {
	

	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    private static Momento momento1;
    private static Momento momento2;
    private static Momento momento3;
    private static Momento momento4;
    private static Momento momento5;
    private static Momento momento6;
    private static Momento momento7;
    private static Momento momento8;
    private static Momento momento9;
    private static Momento momento10;
    private static Momento momentoDataInvalida;
    private static Momento momentoSemCampoObrigatorio;
    private static Momento momentoSabado;
    private static Momento momentoDomingo;
    
    private static Alocacao alocacao1;
    private static Alocacao alocacaoMaiorQueTempoTrabalhado;
    
    private static final String URL_BATIDA = "/v1/batidas";
    private static final String URL_ALOCACOES = "/v1/alocacoes";
    private static final String URL_RELATORIO = "/v1/folhas-de-ponto";
    private static final String ANO_RELATORIO = "/2018-08";
    private static final String ARQUIVO_RELATORIO_2018_08 = "response_relatorio_2018_08.json";
    
    @BeforeAll
    private static void init() {
    	initValues();
    }

	@Test
	@Order(1)
    public void testRegistroBatida() throws JsonProcessingException, Exception {
    	mockMvc.perform(post(URL_BATIDA)
    	        .contentType(MediaType.APPLICATION_JSON)
    	        .content(objectMapper.writeValueAsString(momento1)))
    	        .andExpect(status().isCreated()); 
    }
	
	@Test
	@Order(2)
	public void testRegistroBatidaMenos1HoraAlmoco() throws JsonProcessingException, Exception {
		mockMvc.perform(post(URL_BATIDA)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(momento2)))
		.andExpect(status().isCreated());
		
		mockMvc.perform(post(URL_BATIDA)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(momento3)))
		.andExpect(status().isForbidden()); 
	}
	
	@Test
	@Order(3)
	public void testRegistroBatidaDataInvalida() throws JsonProcessingException, Exception {
		mockMvc.perform(post(URL_BATIDA)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(momentoDataInvalida)))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@Order(4)
	public void testRegistroBatidaSemCampoObrigatorio() throws JsonProcessingException, Exception {
		mockMvc.perform(post(URL_BATIDA)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(momentoSemCampoObrigatorio)))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@Order(5)
	public void testRegistroBatidaMaisDe4Batidas() throws JsonProcessingException, Exception {
		mockMvc.perform(post(URL_BATIDA)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(momento4)))
		.andExpect(status().isCreated());
		
		mockMvc.perform(post(URL_BATIDA)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(momento5)))
		.andExpect(status().isCreated()); 
		
		mockMvc.perform(post(URL_BATIDA)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(momento6)))
		.andExpect(status().isForbidden()); 
	}
	
	@Test
	@Order(6)
	public void testRegistroBatidaSabadoDomingo() throws JsonProcessingException, Exception {
		mockMvc.perform(post(URL_BATIDA)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(momentoSabado)))
		.andExpect(status().isForbidden());
		
		mockMvc.perform(post(URL_BATIDA)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(momentoDomingo)))
		.andExpect(status().isForbidden()); 
	}
	
	@Test
	@Order(7)
	public void testRegistroBatidaJaRegistrada() throws JsonProcessingException, Exception {
		mockMvc.perform(post(URL_BATIDA)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(momento1)))
		.andExpect(status().isConflict()); 
	}
	
	@Test
	@Order(8)
	public void testRegistroAlocacao() throws JsonProcessingException, Exception {
		MvcResult result = mockMvc.perform(post(URL_ALOCACOES)
    	        .contentType(MediaType.APPLICATION_JSON)
    	        .content(objectMapper.writeValueAsString(alocacao1))).andReturn();
		Alocacao alocacaoInserida = TestUtils.jsonToObject(result.getResponse().getContentAsString(), Alocacao.class);
		assertNotNull(alocacaoInserida);
		assertEquals(Duration.parse(alocacaoInserida.getTempo()), Duration.parse("PT2H30M0S"));
	}
	
	@Test
	@Order(9)
	public void testRegistroAlocacaoMaiorQueTempoTrabalhado() throws JsonProcessingException, Exception {
		mockMvc.perform(post(URL_ALOCACOES)
    	        .contentType(MediaType.APPLICATION_JSON)
    	        .content(objectMapper.writeValueAsString(alocacaoMaiorQueTempoTrabalhado)))
    	        .andExpect(status().isBadRequest());
	}
	
	@Test
	@Order(10)
	public void testRegistroSegundaAlocacao() throws JsonProcessingException, Exception {
		mockMvc.perform(post(URL_ALOCACOES)
    	        .contentType(MediaType.APPLICATION_JSON)
    	        .content(objectMapper.writeValueAsString(alocacao1)))
				.andExpect(status().isCreated());
	}
	
	@Test
	@Order(11)
	public void testRelatorio() throws JsonProcessingException, Exception {
		MvcResult result = mockMvc.perform(get(URL_RELATORIO + ANO_RELATORIO)
				.contentType(MediaType.APPLICATION_JSON)).andReturn();
		
		String json1 = result.getResponse().getContentAsString();
		String json2 = TestUtils.readFileContent(ARQUIVO_RELATORIO_2018_08);
		assertTrue(JsonParser.parseString(json1).isJsonObject());
		assertTrue(JsonParser.parseString(json2).isJsonObject());
		
		assertEquals(JsonParser.parseString(json1), JsonParser.parseString(json2));
	}
	
	@Test
	@Order(12)
    public void testRelatorioRegistroBatidaSegundoDia() throws JsonProcessingException, Exception {
    	mockMvc.perform(post(URL_BATIDA)
    	        .contentType(MediaType.APPLICATION_JSON)
    	        .content(objectMapper.writeValueAsString(momento7)))
    	        .andExpect(status().isCreated()); 
    	mockMvc.perform(post(URL_BATIDA)
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(momento8)))
    	.andExpect(status().isCreated()); 
    	mockMvc.perform(post(URL_BATIDA)
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(momento9)))
    	.andExpect(status().isCreated()); 
    	mockMvc.perform(post(URL_BATIDA)
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(momento10)))
    	.andExpect(status().isCreated());
    }
	
	private static void initValues() {
		momento1 = new Momento("2018-08-22T08:00:00");
		momento2 = new Momento("2018-08-22T12:00:00");
		momento3 = new Momento("2018-08-22T12:30:00");
		momento4 = new Momento("2018-08-22T13:00:00");
		momento5 = new Momento("2018-08-22T18:00:00");
		momento6 = new Momento("2018-08-22T19:00:00");
		
		momento7 = new Momento("2018-08-23T08:00:00");
		momento8 = new Momento("2018-08-23T12:00:00");
		momento9 = new Momento("2018-08-23T14:00:00");
		momento10 = new Momento("2018-08-23T18:00:00");
		
		momentoDataInvalida = new Momento("2018-02-30T08:00:00");
		momentoSemCampoObrigatorio = new Momento();
		momentoSabado = new Momento("2020-12-05T08:00:00");
		momentoDomingo = new Momento("2020-12-06T08:00:00");
		
		alocacao1 = new Alocacao("2018-08-22", "PT2H30M0S", "ACME Corporation");
		alocacaoMaiorQueTempoTrabalhado = new Alocacao("2018-08-22", "PT8H30M0S", "ACME Corporation");
	}
}
