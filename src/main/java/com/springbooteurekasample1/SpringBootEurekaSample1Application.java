package com.springbooteurekasample1;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableDiscoveryClient
@EnableFeignClients
public class SpringBootEurekaSample1Application {

	private static List<Race> races = new ArrayList<Race>();

	@Autowired
	private ParticipantsClient participantsClient;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootEurekaSample1Application.class, args);
	}
	
	@RequestMapping("/")
	public List<Race> getRaces() {
		races.add(new Race("Spartan Beast", "123", "MA", "Boston"));
		races.add(new Race("Tough Mudder RI", "456", "RI", "Providence"));
		return races;
	}
	
	@RequestMapping("/participants")
	public List<RaceWithParticipants> getRacesWithParticipants() {
		List<RaceWithParticipants> returnRaces = new ArrayList<RaceWithParticipants>();
		for(Race r : races) {
			returnRaces.add(new RaceWithParticipants(r, participantsClient.getParticipants(r.getId())));
		}
		return returnRaces;
	}
}

@FeignClient("participants")
interface ParticipantsClient {
	
	@RequestMapping(method = RequestMethod.GET, value="/races/{raceId}")
	List<Participant> getParticipants(@PathVariable("raceId") String raceId);
	
}

class Race {
	private String name;
	private String id;
	private String state;
	private String city;
	
	public Race(String name, String id, String state, String city) {
		super();
		this.name = name;
		this.id = id;
		this.state = state;
		this.city = city;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
}

class Participant {
	private String firstName;
	private String lastName;
	private String homeState;
	private String shirtSize;
	private List<String> races;
	public Participant(String firstName, String lastName, String homeState,
			String shirtSize, List<String> races) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.homeState = homeState;
		this.shirtSize = shirtSize;
		this.races = races;
	}
	
	public Participant(){}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getHomeState() {
		return homeState;
	}
	public void setHomeState(String homeState) {
		this.homeState = homeState;
	}
	public String getShirtSize() {
		return shirtSize;
	}
	public void setShirtSize(String shirtSize) {
		this.shirtSize = shirtSize;
	}
	public List<String> getRaces() {
		return races;
	}
	public void setRaces(List<String> races) {
		this.races = races;
	}
}

class RaceWithParticipants extends Race {
	private List<Participant> participants;

	public RaceWithParticipants(Race r, List<Participant> participants) {
		super(r.getName(), r.getId(), r.getState(), r.getCity());
		this.participants = participants;
	}

	public List<Participant> getParticipants() {
		return participants;
	}

	public void setParticipants(List<Participant> participants) {
		this.participants = participants;
	}
}
