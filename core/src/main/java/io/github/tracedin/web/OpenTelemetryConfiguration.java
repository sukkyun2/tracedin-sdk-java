package io.github.tracedin.web;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import io.opentelemetry.sdk.trace.export.SpanExporter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Slf4j
@Configuration
public class OpenTelemetryConfiguration {
    private static final String OTLP_ENDPOINT = System.getenv("OTLP_ENDPOINT");

    @Bean
    public Tracer tracer() {
        String tracerName = System.getenv("TRACER_NAME");
        log.info(String.format("Initializing Tracer name [%s]", tracerName));

        OpenTelemetry openTelemetry = initOpenTelemetry();
        return openTelemetry.getTracer(tracerName);
    }

    public OpenTelemetry initOpenTelemetry() {
        log.info(String.format("Initializing OTLP Collector [%s]", OTLP_ENDPOINT));

        SpanExporter spanExporter = OtlpHttpSpanExporter.builder()
                .setEndpoint(OTLP_ENDPOINT)
                .build();

        SdkTracerProvider tracerProvider = SdkTracerProvider.builder()
                .addSpanProcessor(SimpleSpanProcessor.create(spanExporter))
                .build();

        return OpenTelemetrySdk.builder()
                .setTracerProvider(tracerProvider)
                .buildAndRegisterGlobal();
    }
}
