package vendas2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@RestController
@RequestMapping("/api/mapping")
public class MappingController {

//	@Autowired
//	private RestauraBDOriginal rbd;
	
	/**
	 * Teste de responseentity para retornar texto e caso queira um json deve ser 
	 * ser criado metodo que converta um objeto em json (ver .docx). Isto pode ser usado em todos 
	 * os metodos para diversificar os tipos de respostas (objetos e texto). Isto tambem significa 
	 * que todos os metodos deverao ser refatorados para atender a nova facilidade.
	 * @param texto Um texto qualquer
	 * @return texto e httpstatus
	 */
	@GetMapping("/teste/{texto}")
	public ResponseEntity<String> teste (@PathVariable String texto) {
		ResponseEntity<String> re = new ResponseEntity<String>(texto, HttpStatus.OK);
		return re;
	}
	
	//Exemplo com injecao de dependencia
	@Autowired
	@Qualifier("appName") //permite que voce referencie pelo nome o objeto que esta sendo injetado.
	private String nomedaaplicacao;
	
	@GetMapping(value = "/api/restart") 
	public ModelAndView restartBD() {
		HttpHeaders responseHeaders = new HttpHeaders ();
		responseHeaders.set ("MyResponseHeader", "MyValue");
		responseHeaders.set("MyResponseStatus", "OK");
		responseHeaders.set("MyResponseTeste", "TESTADO");
		ModelAndView mav = new ModelAndView();
	    mav.setViewName("index");
	    mav.setStatus(HttpStatus.OK);
	    mav.addObject("MyHeaders", responseHeaders);
//	    mav.addObject("cliente", cli);
	    return mav;
	}

//	@GetMapping(value = "/iniciarestart")
//	public void restartIni() {
//		rbd.restauraBancoDados();
////		rbd.testeLog();
//		return;
//	}
	
//	@GetMapping(value = "/api/log", produces = "application/json; charset=utf-8")
//	public ModelAndView obtemLog() {
//		// Create ObjectMapper object.
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.enable(SerializationFeature.INDENT_OUTPUT);
//        String json = "";
//		try {
//			json = mapper.writeValueAsString(rbd.getLog());
//		} catch (JsonProcessingException e) {
//			throw new ResponseStatusException (HttpStatus.INTERNAL_SERVER_ERROR,
//					"Falha ao gerar JSON de retorno");
//		}
//
//		HttpHeaders responseHeaders = new HttpHeaders ();
//		responseHeaders.set ("MyResponseHeader", "MyJSONValue");
//		responseHeaders.set("MyResponseStatus", "OK");
//		responseHeaders.set("MyResponseTeste", "TESTADO");
//		ModelAndView mav = new ModelAndView();
////	    mav.setViewName("index");
//		mav.setViewName(json);
//	    mav.setStatus(HttpStatus.OK);
//	    mav.addObject("MyHeaders", responseHeaders);
//	    mav.addObject("lista", json);
//	    return mav;
//	}
	
	@GetMapping(value="/nome")
	public String nome(){
		return nomedaaplicacao;
	}

	//Exemplo usando chave de application.properties
	@Value("${aplicacao.nome}")
	private String nomeappproperties;
	
	@GetMapping(value="/nomeproperties")
	public String nome2(){
		return nomeappproperties;
	}
	
	@RequestMapping(value="/")
	@ResponseBody
	public String raiz(){
		return "este texto esta em MappingController.java";
	}
	
	@GetMapping(value="/hello")
	public String hello(){
		return "Ola mundo veio sem porteira !";
	}
}
