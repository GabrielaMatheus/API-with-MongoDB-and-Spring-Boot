package projeto.locadora.locadora.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomJsonDateDeserializer extends JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        try {
            return new SimpleDateFormat("dd-MM-yyyy").parse(parser.getText());
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
