package br.com.ilia.digital.folhadeponto.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ilia.digital.folhadeponto.persistence.model.Alocacao;
import br.com.ilia.digital.folhadeponto.persistence.model.Momento;
import br.com.ilia.digital.folhadeponto.persistence.model.Registro;
import br.com.ilia.digital.folhadeponto.persistence.repository.AlocacaoRepository;
import br.com.ilia.digital.folhadeponto.persistence.repository.MomentoRepository;
import br.com.ilia.digital.folhadeponto.persistence.repository.RegistroRepository;

@Service
public class FolhaDePontoServiceImpl implements FolhaDePontoService {

	@Autowired
	private AlocacaoRepository alocacaoRepository;
	
	@Autowired
	private MomentoRepository momentoRepository;
	
	@Autowired
	private RegistroRepository registroRepository;
	
	@Override
	public Alocacao getAlocacao(Long id) {
		return alocacaoRepository.getOne(id);
	}

	@Override
	public String retornoTeste() {
		return "Tá funcionando!";
	}

	@Override
	public Registro registraBatida(Momento momento) {
//		Example<Momento> example = Example.of(momento);
//		registroRepository.findById(momento.getId());
		
		momentoRepository.save(momento);
		
		Registro registro = new Registro();
		registro.setId(1L);
		registro.setDia(momento.getDataHora());
		registro.getHorarios().add(momento.getDataHora().substring(3));
		
		return new Registro();
	}
	
	@Override
	public List<Registro> getRegistros() {
		return registroRepository.findAll();
	}
	
	@Override
	public List<Momento> getMomentos() {
		return momentoRepository.findAll();
	}
}
