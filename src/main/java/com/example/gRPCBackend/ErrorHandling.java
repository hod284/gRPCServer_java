package com.example.gRPCBackend;


import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
// HttpEntity 클래스를 상속받아 구현한 클래스가 RequestEntity, ResponseEntity 클래스이다. ResponseEntity는 사용자의 HttpRequest에 대한 응답 데이터를 포함하는 클래스이다. 따라서 HttpStatus, HttpHeaders, HttpBody를 포함한다.
// resetcontolleradvice는 전역컨트롤러에 적용되는 aop개념으로 예외처리 바인딩 처리 공동 응답처리를 할수 있다
@RestControllerAdvice
public class ErrorHandling {
    //각각의 자바에서 처리하는 예외처리 클리들이 있는데 에외 처리를 쓸경우 이클래스에 제이슨형태로 만들어 보여준다
    // 즉 예외처리를 전역적으로 처리하는 클래스이며 특정 예외가 발생했을때 해당 메서드가 호출된다.
    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    public ResponseEntity<Map<String,Object>> handleNotFoundException(ChangeSetPersister.NotFoundException ex) {
        return ResponseEntity.status(404).body(Map.of(
                "timestamp", System.currentTimeMillis(),
                "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(String.format("An error occurred: %s", ex.getMessage()
        ));
    }
}
