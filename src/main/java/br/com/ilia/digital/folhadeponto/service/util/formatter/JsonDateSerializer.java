package br.com.ilia.digital.folhadeponto.service.util.formatter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@Component
public class JsonDateSerializer extends JsonSerializer<Date>{
    // ISO 8601
    private static final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Override
    public void serialize(Date date, JsonGenerator gen, SerializerProvider provider)
            throws IOException, JsonProcessingException
    {
        String formattedDate = dateFormat.format(date);
        gen.writeString(formattedDate);
    }
}
