package org.ufo.sightings;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.ufo.sightings.entities.Sighting;
import org.ufo.sightings.repositories.SightingRepository;

import java.net.URI;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
class UfoSightingsApplicationTests {

    @Autowired
    SightingRepository sightingRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void testDatabaseConnection() {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM sightings", Integer.class);
        assertThat(count).isEqualTo(5);
        assertThat(sightingRepository.findAll().spliterator().getExactSizeIfKnown()).isEqualTo(5);
    }

    @Test
    void shouldReturnASightingWhenIdExists() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("mulder", "abc123")
                .getForEntity("/ufo-sightings/100", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number id = documentContext.read("$.id");
        assertThat(id).isEqualTo(100);

		String city = documentContext.read("$.city");
		assertThat(city).isEqualTo("Los Angeles");
    }

    @Test
    void shouldNotReturnASightingWithAnUnknownId() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("mulder", "abc123")
                .getForEntity("/ufo-sightings/10000", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isBlank();
    }

    @Test
    @DirtiesContext
    void shouldCreateANewSighting() {
		Sighting newSighting = createNewSighting();
		ResponseEntity<Void> createResponse = restTemplate
				.withBasicAuth("mulder", "abc123")
				.postForEntity("/ufo-sightings", newSighting, Void.class);
		assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		URI locationOfNewSighting = createResponse.getHeaders().getLocation();
		ResponseEntity<String> getResponse = restTemplate
				.withBasicAuth("mulder", "abc123")
				.getForEntity(locationOfNewSighting, String.class);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
		Number id = documentContext.read("$.id");
		String city = documentContext.read("$.city");
        String shape = documentContext.read("$.shape");

		assertThat(id).isNotNull();
		assertThat(city).isEqualTo("Boise");
        assertThat(shape).isEqualTo("diamond");
    }

    @Test
    void shouldReturnAllSightingsAssignedToAgent() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("mulder", "abc123")
                .getForEntity("/ufo-sightings", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int sightingsCount = documentContext.read("$.length()");
        assertThat(sightingsCount).isEqualTo(3);

		JSONArray ids = documentContext.read("$..id");
		assertThat(ids).containsExactlyInAnyOrder(100, 101, 102);

		JSONArray cities = documentContext.read("$..city");
		assertThat(cities).containsExactlyInAnyOrder("Los Angeles", "Phoenix", "Seattle");
    }

    @Test
    void shouldReturnASortedPageOfSightingsWithCustomParameters() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("mulder", "abc123")
                .getForEntity("/ufo-sightings?page=0&size=2&sort=datePosted,desc", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray page = documentContext.read("$[*]");
        assertThat(page.size()).isEqualTo(2);

        JSONArray ids = documentContext.read("$..id");
        assertThat(ids).containsExactly(102, 101);

        JSONArray cities = documentContext.read("$..city");
        assertThat(cities).containsExactly("Seattle", "Phoenix");
    }

    @Test
    void shouldReturnAPageOfSightings() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("mulder", "abc123")
                .getForEntity("/ufo-sightings?page=0&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray page = documentContext.read("$[*]");
        assertThat(page.size()).isEqualTo(1);

        String datePosted = documentContext.read("$[0].datePosted");
        assertThat(datePosted).isEqualTo("2025-01-02T10:00:00");
    }

    @Test
    void shouldNotReturnASightingWhenUsingBadCredentials() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("BAD-USER", "abc123")
                .getForEntity("/ufo-sightings/100", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        response = restTemplate
                .withBasicAuth("mulder", "BAD-PASSWORD")
                .getForEntity("/ufo-sightings/100", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldRejectUsersWhoAreNotAgents() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("norm", "789fed")
                .getForEntity("/ufo-sightings/100", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void shouldNotAllowAccessToSightingsTheyDoNotOwn() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("mulder", "abc123")
                .getForEntity("/ufo-sightings/104", String.class); // scully's data
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DirtiesContext
    void shouldUpdateAnExistingSighting() {
        Sighting sightingUpdate = createNewSighting();
        HttpEntity<Sighting> request = new HttpEntity<>(sightingUpdate);
        ResponseEntity<Void> response = restTemplate
                .withBasicAuth("mulder", "abc123")
                .exchange("/ufo-sightings/100", HttpMethod.PUT, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<String> getResponse = restTemplate
                .withBasicAuth("mulder", "abc123")
                .getForEntity("/ufo-sightings/100", String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
		Number id = documentContext.read("$.id");
		String city = documentContext.read("$.city");

		assertThat(id).isEqualTo(100);
		assertThat(city).isEqualTo("Boise");
    }

    @Test
    void shouldNotUpdateASightingThatDoesNotExist() {
		Sighting unknownSighting = createNewSighting();
		HttpEntity<Sighting> request = new HttpEntity<>(unknownSighting);
		ResponseEntity<Void> response = restTemplate
				.withBasicAuth("mulder", "abc123")
				.exchange("/ufo-sightings/99999", HttpMethod.PUT, request, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldNotUpdateASightingThatIsAssignedToAnotherAgent() {
		Sighting sighting = createNewSighting();
        sighting.setAgent("scully");
		HttpEntity<Sighting> request = new HttpEntity<>(sighting);
		ResponseEntity<Void> response = restTemplate
				.withBasicAuth("mulder", "abc123")
				.exchange("/ufo-sightings/104", HttpMethod.PUT, request, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DirtiesContext
    void shouldDeleteAnExistingSighting() {
        ResponseEntity<Void> response = restTemplate
                .withBasicAuth("mulder", "abc123")
                .exchange("/ufo-sightings/100", HttpMethod.DELETE, null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<String> getResponse = restTemplate
                .withBasicAuth("mulder", "abc123")
                .getForEntity("/ufo-sightings/100", String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldNotDeleteASightingThatDoesNotExist() {
        ResponseEntity<Void> deleteResponse = restTemplate
                .withBasicAuth("mulder", "abc123")
                .exchange("/ufo-sightings/99999", HttpMethod.DELETE, null, Void.class);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldNotAllowDeletionOfSightingsAssignedToAnotherAgent() {
        ResponseEntity<Void> deleteResponse = restTemplate
                .withBasicAuth("mulder", "abc123")
                .exchange("/ufo-sightings/104", HttpMethod.DELETE, null, Void.class);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        ResponseEntity<String> getResponse = restTemplate
                .withBasicAuth("scully", "xyz456")
                .getForEntity("/ufo-sightings/104", String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private Sighting createNewSighting() {
        Sighting newSighting = new Sighting();
        newSighting.setDatetime(LocalDateTime.parse("2018-10-23T17:19:33"));
        newSighting.setCity("Boise");
        newSighting.setState("ID");
        newSighting.setCountry("USA");
        newSighting.setShape("diamond");
        newSighting.setDurationSeconds(120);
        newSighting.setDurationHoursMins("2 minutes");
        newSighting.setComments("Strange object sparkling over Main St");
        newSighting.setDatePosted(LocalDateTime.parse("2018-10-23T17:19:33"));
        newSighting.setLatitude(43.6150F);
        newSighting.setLongitude(116.2023F);
        return newSighting;
    }
}
