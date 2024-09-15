package io.github.tracedin.example.intra;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "key-value", url = "${example.api.url}")
public interface KeyValueQueryClient {

    @GetMapping("/key/value/one/two")
    KeyValueQueryResponse getKeyValue();
}
