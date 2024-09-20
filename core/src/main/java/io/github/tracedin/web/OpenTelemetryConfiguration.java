package io.github.tracedin.web;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import io.opentelemetry.sdk.trace.export.SpanExporter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OpenTelemetryConfiguration {
    private static final String OTLP_ENDPOINT = System.getenv("OTLP_ENDPOINT");

    public static OpenTelemetry initOpenTelemetry() {
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
