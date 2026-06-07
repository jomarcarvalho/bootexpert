package vendas2.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vendas2.dao.PedidoDtoDAO;
import vendas2.exception.RegraNegocioException;
import vendas2.modelo.Cliente;
import vendas2.modelo.dto.PScreenDTO;
import vendas2.modelo.dto.PedidoDTO;

@RestController
@RequestMapping("/api/peddto")
public class PedidoDTOController {
	
	@Autowired
	PedidoDtoDAO pedidodtodao;
	
//	teste ok
	@PostMapping(value = "/ins")
	public ResponseEntity<Integer> inserirPedido(@RequestBody @Valid PedidoDTO pdto) {
		try{
			Integer pedidoinsert = pedidodtodao.inserirPedidoDTO(pdto);
			 if(pedidoinsert != null) {
				 return ResponseEntity.ok(pedidoinsert);
			 }else{
				 return ResponseEntity.noContent().build();
			 }
		}catch(Exception e){
			System.out.println("Falha ao inserir pedido na base");
			e.printStackTrace();
			throw new RegraNegocioException(e.getMessage(), e);
		}
	 }
	
	
//	teste ok
	@GetMapping("/con/pedido/cliente")
	public ResponseEntity<List<PScreenDTO>> buscaListaPedidoPeloCliente( @RequestBody Cliente cliente) {
		try{
			List<PScreenDTO> listappscreenDTO = pedidodtodao.listarPedidosByCliente(cliente);
			if(listappscreenDTO != null) {
				return ResponseEntity.ok(listappscreenDTO);
			} else {
				return ResponseEntity.notFound().build();
			}
		}catch(Exception e){
			System.out.println("Falha ao localizar lista de pedidos do cliente");
			e.printStackTrace();
			throw new RegraNegocioException(e.getMessage(), e);
		}
	}
	
//	teste ok
	@GetMapping("/con/pedido/cliente/cpf/{txtcpf}")
	public ResponseEntity<List<PScreenDTO>> buscaPedidoClientePeloCpf ( @PathVariable String txtcpf) {
		try{
			List<PScreenDTO> listaDTO = pedidodtodao.listarPedidosByClienteCPF(txtcpf);
			if(listaDTO != null) {
				return ResponseEntity.ok(listaDTO);
			}else{
				return ResponseEntity.notFound().build();
			}
		}catch(Exception e){
			System.out.println("Falha ao localizar lista de pedidos pelo cpf do cliente");
			e.printStackTrace();
			throw new RegraNegocioException(e.getMessage(), e);
		}
	}
	
	
//	teste ok
	@GetMapping("/con/pedido/id/{pid}")
	public ResponseEntity<PScreenDTO> buscaPedidoPeloId ( @PathVariable Integer pid) {
		try{
			PScreenDTO ppscreenDTO = pedidodtodao.listarPedidoByPedidoId(pid);
			if(ppscreenDTO != null) {
				return ResponseEntity.ok(ppscreenDTO);
			}else{
				return ResponseEntity.notFound().build();
			}
		}catch(Exception e){
			System.out.println("Falha ao localizar pedido");
			e.printStackTrace();
			throw new RegraNegocioException(e.getMessage(), e);
		}
	}
	
	
//	teste ok
	@GetMapping("/con/pedido/tem/produto/{ppid}")
	public ResponseEntity<List<PScreenDTO>> listarPedidosQueTenhamProduto ( @PathVariable Integer ppid) {
		try{
			List<PScreenDTO> prodpedDTO = pedidodtodao.listarTop10PedidosQueContenhamUmCertoProduto(ppid);
			if(prodpedDTO != null) {
				return ResponseEntity.ok(prodpedDTO);
			}else{
				return ResponseEntity.notFound().build();
			}
		}catch(Exception e){
			System.out.println("Falha ao localizar pedidos com o produto");
			e.printStackTrace();
			throw new RegraNegocioException(e.getMessage(), e);
		}
	}
	
//	teste ok
	@PutMapping("/atu/item/pedido/{ppid}")
	public ResponseEntity<PScreenDTO> atualizaItensDoPedido(@PathVariable Integer ppid,
			@RequestBody @Valid PedidoDTO pedto) {
		try{
			PScreenDTO ppsdto = pedidodtodao.atualizarItensDoPedido(ppid, pedto);
			if(ppsdto != null) {
				return ResponseEntity.ok(ppsdto);
			}else{
				return ResponseEntity.notFound().build();
			}
		}catch(Exception e){
			System.out.println("listando erro em atualizaItensDoPedido");
			e.printStackTrace();
			throw new RegraNegocioException(e.getMessage(), e);
		}
	}
	
	
//	teste ok
	@PutMapping("/atu/status/pedido/{ppid}")
	public ResponseEntity<PScreenDTO> atualizaStatusDoPedido(@PathVariable Integer ppid,
			@RequestBody @Valid PedidoDTO pedto) {
		try{
			PScreenDTO ppsdto = pedidodtodao.atualizarStatusPedido(ppid, pedto);
			if(ppsdto != null) {
				return ResponseEntity.ok(ppsdto);
			}else{
				return ResponseEntity.notFound().build();
			}
		}catch(Exception e){
			System.out.println("Falha ao atualizar status do pedido");
			e.printStackTrace();
			throw new RegraNegocioException(e.getMessage(), e);
		}
	}
	
//	teste 
	@DeleteMapping("/del/pedido/{ppid}")
	public ResponseEntity<PScreenDTO> removerPedidoById(@PathVariable Integer ppid) {
		try{
			PScreenDTO p = pedidodtodao.excluirPedidoByPedidoId(ppid);
			if(p != null) {
				return ResponseEntity.ok(p);
			}else{
				return ResponseEntity.notFound().build();
			}
		}catch(Exception e){
			System.out.println("Falha ao remover o pedido");
			e.printStackTrace();
			throw new RegraNegocioException(e.getMessage(), e);
		}
	}
	
}