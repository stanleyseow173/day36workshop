package ibf2022.batch3.csf.day36.weatherserver.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import ibf2022.batch3.csf.day36.weatherserver.models.WeatherInfo;
import ibf2022.batch3.csf.day36.weatherserver.services.WeatherException;
import ibf2022.batch3.csf.day36.weatherserver.services.WeatherService;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

@Controller
@RequestMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public class WeatherController {

	@Autowired
	private WeatherService weatherSvc;

	@GetMapping(path = "/api/weather")
	@ResponseBody
	public ResponseEntity<String> getWeather(@RequestParam String city,
			@RequestParam(defaultValue = "metric") String units) {

		try {

			JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
			weatherSvc.getWeather(city, units).stream()
					.map(d -> Json.createObjectBuilder()
							.add("main", d.main())
							.add("description", d.description())
							.add("icon", d.icon())
							.build())
					.forEach(arrBuilder::add);
			return ResponseEntity.ok(arrBuilder.build().toString());

		} catch (WeatherException ex) {
			return ResponseEntity.status(400)
					.body(
							Json.createObjectBuilder()
									.add("error", ex.getMessage())
									.build().toString());
		}
	}

	@GetMapping(path = "/weather/**")
	public RedirectView handleWeatherRequest() {
		return new RedirectView("/");
	}

	@GetMapping(path = "/api/cities")
	@ResponseBody
	public ResponseEntity<String> getCities() {
		JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
		weatherSvc.getCities().stream()
				.forEach(arrBuilder::add);
		return ResponseEntity.ok(arrBuilder.build().toString());

	}

	@PostMapping(path = "/api/addCity")
	@ResponseBody
	public ResponseEntity<String> addCity(@RequestParam String city) {
		System.out.println("went through controller here");
		JsonObjectBuilder objBuilder = Json.createObjectBuilder();
		Boolean canAdd = weatherSvc.addCity(city);
		JsonObject returnJson = objBuilder.add("canAdd", canAdd).build();
		return ResponseEntity.ok(returnJson.toString());
	}

}
