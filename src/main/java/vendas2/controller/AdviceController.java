package vendas2.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vendas2.exception.ApiErrors;
import vendas2.exception.RegraNegocioException;
import vendas2.modelo.dto.ErroFormDTO;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 *  intercepta qualquer exception que ocorra em qualquer metodo de qualquer controller
 */
@RestControllerAdvice
public class AdviceController {
    //	ajuda a pegar mensagens de erro em varios idiomas
    @Autowired
    private MessageSource messageSource;
    private static final Logger logger = LoggerFactory.getLogger(AdviceController.class);

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrors> handleConstraintViolationException(ConstraintViolationException ex){
        logger.error("[AdviceCTRL] Constraint Violation Exception respondeu", ex);
        System.out.println("[AdviceCTRL] Constraint Violation Exception respondeu");
        String msgErro = ex.getMessage();
        ApiErrors erro = new ApiErrors(
                "CPF já existe na base",
                HttpStatus.CONFLICT.value());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrors> handleRuntimeException( RuntimeException ex) {
        logger.error("[AdviceCTRL] RuntimeException respondeu", ex);
        System.out.println("[AdviceCTRL] RunTime Exception respondeu");
        ApiErrors erro = new ApiErrors(
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }

    /**
     * Manipulador para execoes geradas do tipo RegraNegocioException
     * @param regranegocioexception
     * @return apierrors
     */
    @ExceptionHandler(RegraNegocioException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleRegraNegocioException (RegraNegocioException ex) {
        logger.error("[AdviceCTRL] RegraNegocioException respondeu", ex);
        System.out.println("[AdviceCTRL] regranegocioexception respondeu");
        String msgErro = ex.getMessage();
        return new ApiErrors(msgErro);
    }


    /**
     * Manipulador para execoes do tipo MethodArgumentNotValidException que sao geradas
     * pelo parser json nas chamadas dos metodos
     *
     * no header da requisicao pode ser inserido a key "Accept-Language" com
     * valor "en-US" para informar o idioma que sera interpretado por
     * LocaleContextHolder.getLocale() e pegar mensagem no idioma correto
     *
     * @param methodargumentnotvalidexception
     * @return apierrors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ErroFormDTO> handleMethodNotValidException (MethodArgumentNotValidException ex) {
        logger.error("[AdviceCTRL] method argument not valid exception respondeu", ex);
        System.out.println("[AdviceCTRL] method argument not valid exception respondeu");
        List<ErroFormDTO> dto = new ArrayList<ErroFormDTO>();
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        for(FieldError fe : fieldErrors) {
//			LocaleContextHolder.getLocale() => para descobrir qual o locale atual e pegar mensagem no idioma correto
            String msg = messageSource.getMessage(fe, LocaleContextHolder.getLocale());
            ErroFormDTO erro = new ErroFormDTO(fe.getField(), msg);
            dto.add(erro);
        }
        return dto;
    }

//	ConstraintViolationException
    /**
     * Manipulador para execoes do tipo DataIntegrityViolationException que sao geradas
     * por duplicacao de indice no banco de dados
     *
     * @param DataIntegrityViolationException
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroFormDTO handleDataIntegrityViolationException (DataIntegrityViolationException ex) {
        logger.error("[AdviceCTRL] Data Integrity Violation Exception respondeu", ex);
        System.out.println("[AdviceCTRL] Data Integrity Violation Exception respondeu");
        String getMsg = ex.getMessage();
        System.out.println("getmsg: " + getMsg);
        String erroTxt = "";
        String msgTxt = "";
        int ini = getMsg.indexOf("constraint [") + 12;
        int fim = getMsg.indexOf("]", ini);
        if(ini > 0 && fim > ini) {
            erroTxt = getMsg.subSequence(ini, fim).toString();
            ini = getMsg.indexOf(";");
            msgTxt = getMsg.subSequence(0, ini).toString();
        }else{
            erroTxt = "fullMsg";
            msgTxt = getMsg;
        }

        ErroFormDTO dto = new ErroFormDTO(erroTxt, msgTxt);
        return dto;
    }

    /**
     * Manipulador para execoes do tipo IllegalArgumentException que sao geradas
     * devido a parametro indevido passado a um metodo
     * @param illegal argument exception
     * @return apierrors
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleIllegalArgumentException (IllegalArgumentException ex) {
        logger.error("[AdviceCTRL] Illegal Argument Exception respondeu", ex);
        System.out.println("[AdviceCTRL] illegal argument exception respondeu");
        String msgErro = ex.getMessage();
        return new ApiErrors(msgErro);
    }

    /**
     * Manipulador para execoes do tipo InvalidFormatException que e gerada
     * devido a formato de parametro invalido
     * @param invalid format exception
     * @return apierrors
     */
    @ExceptionHandler(InvalidFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleIllegalFormatException (InvalidFormatException ex) {
        logger.error("[AdviceCTRL] Invalid Format Exception respondeu", ex);
        System.out.println("[AdviceCTRL] invalid format exception respondeu");
        String msgErro = ex.getMessage();
        String erroTxt = "";
        logger.debug("invalid format exception:\n" + msgErro + "\n");
        System.out.println("invalid format exception:\n" + msgErro + "\n");
        if(msgErro.indexOf("EnumStatusPedido") > 0) {
//			tratamento da msg de erro para o enum EnumStatusPedido
            erroTxt = "O status informado para o pedido é inválido";
            int ini = msgErro.indexOf("\"") + 1;
            int fim = msgErro.indexOf("\":");
            if(ini > 0 && fim > ini) {
                erroTxt += ": " + msgErro.subSequence(ini, fim);
            }
        }else{
//			tratamento da msg de erro para outros campos
            int ini = msgErro.indexOf("[\"") + 2;
            int fim = msgErro.indexOf("\"]");
            if(ini > 0 && fim > ini) {
                erroTxt = "Formato inválido para o campo: " + msgErro.subSequence(ini, fim);
            }
        }
        return new ApiErrors(erroTxt);
    }

    /**
     * Manipulador para execoes do tipo JsonParseException que sao geradas
     * devido a json mal formado
     * @param Json Parse Exception
     * @return apierrors
     */
    @ExceptionHandler(JsonParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleJsonParseException (JsonParseException ex) {
        logger.error("[AdviceCTRL] JsonParseException respondeu", ex);
        System.out.println("[AdviceCTRL] Json Parse Exception respondeu");
        return new ApiErrors("Formato inválido para JSON");
    }


    /**
     * Manipulador para execoes do tipo UsernameNotFoundException que sao geradas
     * devido a dados de usuario invalido no login
     * @param Username Not Found Exception
     * @return apierrors
     */
//    @ExceptionHandler(UsernameNotFoundException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ApiErrors handleJsonParseException (UsernameNotFoundException ex) {
//        System.out.println("[CTRL] Username Not Found Exception respondeu");
//        return new ApiErrors("Dados de usuario invalido para login");
//    }
}
