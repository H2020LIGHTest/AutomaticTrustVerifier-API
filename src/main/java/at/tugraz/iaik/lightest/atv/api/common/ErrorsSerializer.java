package at.tugraz.iaik.lightest.atv.api.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import java.io.IOException;

/**
 * Created by gavinkim at 2018-12-06
 * 스프링에 등록 하게 되면 object mapper 는 해당 시리얼라이저를 사용하게 된다
 * Errors 객체를 serializer 할때 사용한다.
 */
@JsonComponent
public class ErrorsSerializer extends JsonSerializer<Errors> {


    @Override
    public void serialize(Errors errors, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartArray();
        errors.getFieldErrors().stream().forEach(e->{
            try {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("field",e.getField());
                jsonGenerator.writeStringField("code",e.getCode());
                jsonGenerator.writeStringField("objectName",e.getObjectName());
                jsonGenerator.writeStringField("defaultMessage",e.getDefaultMessage());

                Object rejectedValue = e.getRejectedValue();
                if(rejectedValue != null){
                    jsonGenerator.writeStringField("rejectedValue",rejectedValue.toString());
                }
                jsonGenerator.writeEndObject();

            } catch (IOException e1) {
                e1.printStackTrace();
            }

        });

        errors.getGlobalErrors().forEach(e->{
            try{
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("objectName",e.getObjectName());
                jsonGenerator.writeStringField("code",e.getCode());
                jsonGenerator.writeStringField("defaultMessage",e.getDefaultMessage());
                jsonGenerator.writeEndObject();
            }catch (IOException e2){
                e2.printStackTrace();
            }
        });
        jsonGenerator.writeEndArray();
    }
}
