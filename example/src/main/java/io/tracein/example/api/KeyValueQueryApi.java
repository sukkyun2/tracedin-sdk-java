package io.tracein.example.api;

import io.tracein.example.intra.KeyValueQueryClient;
import io.tracein.example.intra.KeyValueQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KeyValueQueryApi {
    private final KeyValueQueryClient keyValueQueryClient;

    @GetMapping
    public KeyValueQueryResponse getKeyValue(){
        return keyValueQueryClient.getKeyValue();
    }
}
