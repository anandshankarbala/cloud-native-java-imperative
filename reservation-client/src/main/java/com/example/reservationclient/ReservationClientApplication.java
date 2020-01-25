package com.example.reservationclient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.stream.Collectors;

@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
public class ReservationClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservationClientApplication.class, args);
	}

}

@RestController
@RequestMapping("")
class ClientRestController {

	private final ReservationReader reader;

	@Autowired
	ClientRestController(ReservationReader reader) {
		this.reader = reader;
	}

	@GetMapping
	public Collection<String> getNames(){
		return reader.read()
				.getContent()
				.stream()
				.map(r -> r.toString())
				.collect(Collectors.toList());
	}
}

@FeignClient("reservation-service")
interface ReservationReader {

	@GetMapping("/reservations")
	public CollectionModel<Reservation> read();
}

@AllArgsConstructor
@NoArgsConstructor
@Data
class Reservation {
	private String reservationName;
}
