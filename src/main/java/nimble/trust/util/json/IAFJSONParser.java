package nimble.trust.util.json;

import java.io.IOException;
import java.io.StringWriter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;



public class IAFJSONParser {
	
	public static synchronized String toJson(Object o){

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		StringWriter stringEmp = new StringWriter();
        try {
			objectMapper.writeValue(stringEmp, o);
		} catch (IOException e) {
			e.printStackTrace();
		}
        return stringEmp.toString();
	}

}
