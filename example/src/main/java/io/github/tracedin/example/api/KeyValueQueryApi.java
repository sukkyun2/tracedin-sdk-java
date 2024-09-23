package io.github.tracedin.example.api;

import io.github.tracedin.example.app.KeyValueReturnService;
import io.github.tracedin.example.intra.KeyValueQueryClient;
import io.github.tracedin.example.intra.KeyValueQueryResponse;
//import io.github.tracedin.logging.CollectLoggingAspect;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KeyValueQueryApi {
    private final KeyValueQueryClient keyValueQueryClient;
    private final KeyValueReturnService keyValueReturnService;

    @GetMapping
    public KeyValueQueryResponse getKeyValueAPI(String key, String value){
        getKeyValueInternal(key);
        keyValueReturnService.returnKey(key);
        return keyValueQueryClient.getKeyValue();
    }

    public void getKeyValueInternal(String key){
        System.out.println("@@@");
    }
}
