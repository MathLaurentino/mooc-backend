package ifpr.edu.br.mooc.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class JacksonConfig {

    @Bean
    public Module stringTrimModule() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(String.class, new StringTrimmerDeserializer());
        return module;
    }

    public static class StringTrimmerDeserializer extends JsonDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String value = StringDeserializer.instance.deserialize(p, ctxt);
            return value != null ? value.trim() : null;
        }
    }
}