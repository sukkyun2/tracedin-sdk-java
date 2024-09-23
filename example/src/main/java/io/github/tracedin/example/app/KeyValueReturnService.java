package io.github.tracedin.example.app;

import org.springframework.stereotype.Service;

@Service
public class KeyValueReturnService {
    public String returnKey(String key){
        return key;
    }
}
