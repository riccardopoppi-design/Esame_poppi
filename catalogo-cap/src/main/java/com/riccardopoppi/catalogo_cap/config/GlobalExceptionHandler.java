package com.riccardopoppi.catalogo_cap.config;

import com.riccardopoppi.catalogo_cap.dto.response.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // VINCOLO: Validazione Real-Time. Intercetta gli errori di validazione (es. @NotBlank, @Min, @Max)
    // e restituisce un 400 Bad Request con la mappa dettagliata campo-messaggio.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(APIResponse.fail(errors));
    }

    // VINCOLO: Isolamento delle eccezioni. Gestisce il caso di risorsa non trovata (404)
    // senza che ci siano blocchi di formattazione o eccezioni web nel Controller.
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<APIResponse<String>> handleNotFoundException(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error(ex.getMessage()));
    }

    // Intercettatore generico per qualsiasi altro errore imprevisto (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<Void>> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIResponse.error("Si è verificato un errore interno al server."));
    }
}