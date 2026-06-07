package vendas2.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import vendas2.dao.ClienteDAO;
import vendas2.exception.ApiErrors;
import vendas2.exception.RegraNegocioException;
import vendas2.modelo.Cliente;
import vendas2.modelo.dto.ClienteCreatedResponseDTO;

@RestController
@RequestMapping("/api/cli")
public class ClienteController {
	/*
	static → o logger pertence à classe, não a cada instância.
	Isso evita criar um novo logger toda vez que a classe é instanciada.
	Economiza recursos e mantém consistência.

	final → impede que a referência ao logger seja alterada.
	Garante que sempre se use o mesmo logger dentro da classe.
	Evita erros de reatribuição acidental.
	 */

	private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);
	@Autowired
	private ClienteDAO clientedao;

	/**
	 * gera exception e trata com try...catch ao inves de inserir cliente na base
	 * @param cliente
	 * @return responseentity<integer>
	 */
	@PostMapping(value = "advice/ins")
	public ResponseEntity<Integer> testesalvarClienteTratado(@RequestBody @Valid Cliente cliente) {
		try{
			logger.debug("[controller] chamando testeinserirClienteTratado...");
			System.out.println("[controller] chamando testeinserirClienteTratado...");
			Integer clientesalvo = clientedao.testeinserirClienteTratado(cliente);
			logger.info("[controller] retornando de testeinserirClienteTratado...");
			System.out.println("[controller] retornando de testeinserirClienteTratado...");
			if(clientesalvo != null) {
				return ResponseEntity.status(HttpStatus.CREATED).body(clientesalvo);
//				ResponseEntity.ok(clientesalvo);
			}else{
				return ResponseEntity.badRequest().build();
			}
		}catch(Exception e){
			logger.error("[controller] listando erro em testesalvarClienteTratado");
			System.out.println("[controller] listando erro em testesalvarClienteTratado");
			throw new RegraNegocioException(e.getMessage(), e);
//			throw new ResponseStatusException (HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao gravar registro");
		}
	}

	/**
	 * gera exception sem tratamento ao inves de inserir cliente na base
	 * @param cliente
	 * @return responseentity<integer>
	 */
	@PostMapping(value = "advice/ins2")
	public ResponseEntity<Integer> testesalvarClienteNaoTratado(@RequestBody @Valid Cliente cliente) {
			logger.debug("[controller] chamando testeinserirClienteNaoTratado...");
			System.out.println("[controller] chamando testeinserirClienteNaoTratado...");
			Integer clientesalvo = clientedao.testeinserirClienteNaoTratado(cliente);
			logger.debug("[controller] retornando de testeinserirClienteNaoTratado...");
			System.out.println("[controller] retornando de testeinserirClienteNaoTratado...");
			if(clientesalvo != null) {
				return ResponseEntity.status(HttpStatus.CREATED).body(clientesalvo);
			}else{
				return ResponseEntity.badRequest().build();
			}
	}


	/**
	 * busca cliente pelo seu Id
	 * @param id numero identificador do cliente
	 * @return responseentity<cliente>
	 */
	@GetMapping(value = "/con/id/{id}")
	public ResponseEntity<Cliente> getClienteById(@PathVariable Integer id) {
		try{
			Cliente cli = clientedao.obtemClienteByClienteId(id);
			if(cli != null) {
				return ResponseEntity.ok(cli);
			} else{
				return ResponseEntity.notFound().build();
			}
		}catch(Exception e){
			throw new ResponseStatusException (HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao localizar registro");
		}
	}
	
	/**
	 * busca todos os clientes cadastrados
	 * @return responseentity<list<cliente>>
	 */
	@GetMapping(value = "/con/todos") 
	public ResponseEntity<List<Cliente>> getAllCliente() {
		try{
			List<Cliente> cli = clientedao.obtemTodosClientes();
			if(cli.size() > 0) {
				return ResponseEntity.ok(cli);
			} else{
				return ResponseEntity.notFound().build();
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new RegraNegocioException(e.getMessage(), e);
//			throw new ResponseStatusException (HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao localizar registro");
		}
	}
	 

	
	/**
	 * Insere cliente na base de dados
	 * @param cliente Objeto Cliente
	 * @return responseentity<cliente>
	 */
	@PostMapping(value = "/ins")
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	// insere UriComponentsBuilder no metodo que o spring injeta o conteudo
	public ResponseEntity<ClienteCreatedResponseDTO> salvarCliente(
			@RequestBody @Valid Cliente cliente,
				HttpServletRequest request) {
		ClienteCreatedResponseDTO corpo=null;
		try {
			Integer clientesalvo = clientedao.inserirCliente(cliente);
			// Apenas a URL base
			String urlBase = request.getScheme() + "://" + request.getServerName()
					+ ":" + request.getServerPort();

			URI location = UriComponentsBuilder
					.fromPath(urlBase + "/api/cli/con/id/{id}")
					.buildAndExpand(clientesalvo)
					.toUri();

			corpo = new ClienteCreatedResponseDTO(
				clientesalvo,
				"Cliente criado com sucesso",
				LocalDate.now(),
				location.toString()
			);

			return ResponseEntity.created(location).body(corpo);
		}catch(RuntimeException e){
			System.out.println("[CTRL] Erro de runtime ao inserir cliente: "+e.getMessage());
			ResponseEntity.unprocessableEntity().body(corpo);
			throw new RegraNegocioException(e.getMessage(), e);
		}
	 }

	/**
	 * Insere lote de cliente na base de dados
	 * @param listacliente lista de cliente
	 * @return responseentity<cliente> - quantidade de clientes inseridos na base
	 */
	@PostMapping(value = "/lote/ins")
	public ResponseEntity<Integer> salvarLoteCliente(@RequestBody @Valid List<Cliente> listacliente) {
		try{
			Integer clientesalvo = clientedao.inserirLote(listacliente);
			 if(clientesalvo == listacliente.size()) {
				 return ResponseEntity.ok(clientesalvo);
			 }else{
				 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			 }
		}catch(Exception e){
			throw new ResponseStatusException (HttpStatus.INTERNAL_SERVER_ERROR, 
					"Falha ao gravar lista de clientes");
		}
	 }
	
	/**
	 * Remove um cliente da base pelo seu identificador
	 * @param id
	 * @return ResponseEntity Cliente
	 */
	@DeleteMapping("/del/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT) //Este status vai junto no return
	public ResponseEntity<Cliente> removeClienteById(@PathVariable Integer id) {
		Boolean chfind = false;
		String msg = "";
		try{
			Cliente cli = clientedao.obtemClienteByClienteId(id);
			chfind = true;
			if(cli != null) {
				clientedao.excluirClienteByCliente(cli);
				return ResponseEntity.ok(cli);
			}else{
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			msg = "Falha ao remover registro";
			if(!chfind){
				msg = "Cliente nao localizado";
			}
			throw new ResponseStatusException (HttpStatus.INTERNAL_SERVER_ERROR, msg);
		}
		
	}	
	
	@PutMapping("/atu")
	public ResponseEntity<Cliente> atualizarClienteById (@RequestBody @Valid Cliente cliente) {
		Boolean chfind = false;
		String msg = "";
		try{
			Cliente cli = clientedao.obtemClienteByClienteId(cliente.getId());
			chfind = true;
			if(cli != null) {
				cli.setNome(cliente.getNome());
				cli.setNascimento(cliente.getNascimento());
				cli.setCpf(cliente.getCpf());
				
				clientedao.atualizarClienteByCliente(cli);
				return ResponseEntity.ok(cli);
			} else{
				return ResponseEntity.notFound().build();
			}
		} catch(Exception e){
			msg = "Falha ao atualizar registro";
			if(!chfind){
				msg = "Cliente nao localizado";
			}
			throw new ResponseStatusException (HttpStatus.INTERNAL_SERVER_ERROR, msg);
		}
	}
		
	@GetMapping("/con/filtronome/{filtroNome}") 
	public ResponseEntity<List<Cliente>> findByNomePart ( @PathVariable String filtroNome) {
		try{
			List<Cliente> lista = clientedao.obtemClienteByNomeLike(filtroNome);
			if(lista.size() > 0) {
				return ResponseEntity.ok(lista);
			}else{
				return ResponseEntity.notFound().build();
			}
		}catch(Exception e){
			throw new ResponseStatusException (
					HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao localizar registro");
		}
	}
	
	@GetMapping("/con/compedido")
	public ResponseEntity<List<Cliente>> buscaComPedido() {
		try{
			List<Cliente> lista = clientedao.obtemClientesComPedido();
			if(lista.size() > 0) {
				return ResponseEntity.ok(lista);
			}else{
				return ResponseEntity.notFound().build();
			}
		}catch(Exception e){
			throw new ResponseStatusException (
					HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao localizar registro");
		}
	}
	
	@GetMapping("/con/sempedido")
	public ResponseEntity<List<Cliente>> buscaSemPedido() {
		try{
			List<Cliente> lista = clientedao.obtemClientesSemPedido();
			if(lista.size() > 0) {
				return ResponseEntity.ok(lista);
			}else{
				return ResponseEntity.notFound().build();
			}
		}catch(Exception e){
			throw new ResponseStatusException (
					HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao localizar registro");
		}
	}
	
	@GetMapping("/con/compedido/filtronome/{nome}") 
	public ResponseEntity<List<Cliente>> buscaComPedidoPeloNome(@PathVariable String nome) {
		try{
			List<Cliente> lista = clientedao.obtemClientesComPedidoNomeLike(nome);
			if(lista.size() > 0) {
				return ResponseEntity.ok(lista);
			}else{
				return ResponseEntity.notFound().build();
			}
		}catch(Exception e){
			throw new ResponseStatusException (
					HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao localizar registro");
		}
	}
	
	@GetMapping("/con/sempedido/filtronome/{nome}")
	public ResponseEntity<List<Cliente>> buscaSemPedidoPeloNome(@PathVariable String nome) {
		try{
			List<Cliente> lista = clientedao.obtemClientesSemPedidoNomeLike(nome);
			if(lista.size() > 0) {
				return ResponseEntity.ok(lista);
			}else{
				return ResponseEntity.notFound().build();
			}
		}catch(Exception e){
			throw new ResponseStatusException (
					HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao localizar registro");
		}
	}
		
}
