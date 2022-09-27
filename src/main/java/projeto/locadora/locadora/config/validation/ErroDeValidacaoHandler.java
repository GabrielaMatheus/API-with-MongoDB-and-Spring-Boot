package projeto.locadora.locadora.config.validation;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import projeto.locadora.locadora.config.validation.exceptions.FieldsNotFilledsException;
import projeto.locadora.locadora.config.validation.exceptions.NotFoundException;

//essas classes tratam os erros do Bean Validation, personalizando o JSON pro cliente.

@RestControllerAdvice
public class ErroDeValidacaoHandler {

    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ErroDeFormularioDto> handle(MethodArgumentNotValidException exception) {
        List<ErroDeFormularioDto> dto = new ArrayList<>();

        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        fieldErrors.forEach(e -> {
            String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale());
            ErroDeFormularioDto erro = new ErroDeFormularioDto(e.getField(), mensagem);
            dto.add(erro);
        });

        return dto;
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<NotFoundDto> handle(NotFoundException notFoundException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new NotFoundDto(notFoundException.getMessage()));
    }

    @ExceptionHandler(FieldsNotFilledsException.class)
    public ResponseEntity<NotFoundDto> handle(FieldsNotFilledsException fieldsNotFilledsException) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new NotFoundDto(fieldsNotFilledsException.getMessage()));
    }
}
