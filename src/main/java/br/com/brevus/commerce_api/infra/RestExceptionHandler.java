package br.com.brevus.commerce_api.infra;

import br.com.brevus.commerce_api.exceptions.DuplicateRecordException;
import br.com.brevus.commerce_api.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    private ResponseEntity<RestErrorMessage> resourceNotFound(ResourceNotFoundException exception, HttpServletRequest request){
        String path = request.getRequestURI();
        int statusCode = HttpStatus.NOT_FOUND.value();
        RestErrorMessage restErrorMessage = new RestErrorMessage(LocalDateTime.now(), statusCode, "Recurso não encontrado!", exception.getMessage(), path);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(restErrorMessage);
    }

    @ExceptionHandler(BadCredentialsException.class)
    private ResponseEntity<RestErrorMessage> badCredentials(BadCredentialsException exception, HttpServletRequest request){
        String path = request.getRequestURI();
        int statusCode = HttpStatus.UNAUTHORIZED.value();
        RestErrorMessage restErrorMessage = new RestErrorMessage(LocalDateTime.now(), statusCode, "Credenciais inválidas!", exception.getMessage(),path);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(restErrorMessage);
    }

    @ExceptionHandler(DuplicateRecordException.class)
    public ResponseEntity<RestErrorMessage> duplicateException(DuplicateRecordException exception, HttpServletRequest request){
        String path = request.getRequestURI();
        int statusCode = HttpStatus.CONFLICT.value();
        RestErrorMessage restErrorMessage = new RestErrorMessage(LocalDateTime.now(), statusCode, "Registro duplicado!", exception.getMessage(), path);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(restErrorMessage);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        String errorDescription = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        int statusCode = HttpStatus.BAD_REQUEST.value();
        RestErrorMessage restErrorMessage = new RestErrorMessage(
                LocalDateTime.now(),
                statusCode,
                "Erro de validação nos campos!",
                errorDescription,
                path
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restErrorMessage);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        String message = "O corpo da requisição (JSON) está inválido ou malformado. Verifique a sintaxe.";
        int statusCode = HttpStatus.BAD_REQUEST.value();
        RestErrorMessage restErrorMessage = new RestErrorMessage(
                LocalDateTime.now(),
                statusCode,
                "Erro na leitura do JSON!",
                message,
                path
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restErrorMessage);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<RestErrorMessage> handleAccessDenied(AccessDeniedException exception, HttpServletRequest request) {
        String path = request.getRequestURI();
        int statusCode = HttpStatus.FORBIDDEN.value();
        RestErrorMessage restErrorMessage = new RestErrorMessage(
                LocalDateTime.now(),
                statusCode,
                "Acesso negado!",
                "Você não tem permissão para acessar este recurso.",
                path
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(restErrorMessage);
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<RestErrorMessage> genericError(Exception exception, HttpServletRequest request){
        String path = request.getRequestURI();
        int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        RestErrorMessage restErrorMessage = new RestErrorMessage(LocalDateTime.now(), statusCode, "Erro interno no servidor!", "Ocorreu um erro inesperado. Tente novamente mais tarde.", path);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(restErrorMessage);
    }

}
