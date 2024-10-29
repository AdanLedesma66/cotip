package py.com.cotip;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Locale;
import java.util.TimeZone;

@SpringBootApplication
public class CotipApplication {

	public static void main(String[] args) {
		SpringApplication.run(CotipApplication.class, args);
	}

	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("America/Asuncion"));
		Locale.setDefault(Locale.forLanguageTag("es_PY"));
	}

}
