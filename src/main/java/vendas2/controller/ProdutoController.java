package vendas2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import vendas2.dao.ProdutoDAO;
import vendas2.modelo.Produto;

@RestController
@RequestMapping("/api/pro")
public class ProdutoController {
	
	@Autowired
	ProdutoDAO produtodao;

	@GetMapping(value = "/con/id/{id}") 
	public ResponseEntity<Produto> getProdutoById(@PathVariable Integer id) {
		try{
			Produto pro = (Produto) produtodao.buscarPorId(id);
			if(pro != null) {
				return ResponseEntity.ok(pro);
			} else{
				return ResponseEntity.notFound().build();
			}
		}catch(Exception e){
			throw new ResponseStatusException (HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao localizar registro");
		}
	}
	
	@GetMapping(value = "/con/todos") 
	public ResponseEntity<List<Produto>> getAllProduto() {
		try{
			List<Produto> pro = produtodao.obterTodos();
			if(pro.size() > 0) {
				return ResponseEntity.ok(pro);
			} else{
				return ResponseEntity.notFound().build();
			}
		}catch(Exception e){
			throw new ResponseStatusException (HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao localizar registro");
		}
	}
	 
	@PostMapping(value = "/ins") 
	public ResponseEntity<Integer> salvarProduto(@RequestBody Produto produto) {
		try{
			Integer produtosalvo = produtodao.inserirProduto(produto);
			 if(produtosalvo != null) {
				 return ResponseEntity.ok(produtosalvo);
			 }else{
				 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			 }
		}catch(Exception e){
			throw new ResponseStatusException (HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao gravar registro");
		}
	 }
	
	@DeleteMapping("/del/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT) //Este status vai junto no return
	public ResponseEntity<Produto> removeProdutoById(@PathVariable Integer id) {
		Boolean chfind = false;
		String msg = "";
		try{
			Produto pro = produtodao.buscarPorId(id);
			chfind = true;
			if(pro != null) {
				Boolean emuso = produtodao.existePedidoQueTenhaProduto(pro);
				if(!emuso) {
					produtodao.remover(pro);
					return ResponseEntity.ok(pro);
				}else{
					return ResponseEntity.noContent().build();
				}
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			msg = "Falha ao remover registro";
			if(!chfind){
				msg = "Produto nao localizado";
			}
			throw new ResponseStatusException (HttpStatus.INTERNAL_SERVER_ERROR, msg);
		}	
	}	
	
	@PutMapping("/atu")
	public ResponseEntity<Produto> atualizarProdutoById (@RequestBody Produto produto) {
		Boolean chfind = false;
		String msg = "";
		try{
			Produto pro = produtodao.buscarPorId(produto.getId());
			chfind = true;
			if(pro != null) {
				pro.setDescricao(produto.getDescricao());
				pro.setPreco(produto.getPreco());
				
				produtodao.atualizar(pro);
				return ResponseEntity.ok(pro);
			} else{
				return ResponseEntity.notFound().build();
			}
		} catch(Exception e){
			msg = "Falha ao atualizar registro";
			if(!chfind){
				msg = "Produto nao localizado";
			}
			throw new ResponseStatusException (HttpStatus.INTERNAL_SERVER_ERROR, msg);
		}
	}
		
	@GetMapping("/con/filtro/descricao/{filtroNome}")
	@ExceptionHandler(NumberFormatException.class)
	public ResponseEntity<List<Produto>> find ( @PathVariable String filtroNome) {
		try{
			List<Produto> lista = produtodao.buscarPorDescricaoLike(filtroNome);
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
	public ResponseEntity<List<Produto>> buscaComPedido() {
		try{
			List<Produto> lista = produtodao.buscaProdutosComPedido();
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
	public ResponseEntity<List<Produto>> buscaSemPedido() {
		try{
			List<Produto> lista = produtodao.buscaProdutosSemPedido();
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