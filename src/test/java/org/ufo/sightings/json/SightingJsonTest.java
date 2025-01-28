package org.ufo.sightings.json;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.ufo.sightings.entities.Sighting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class SightingJsonTest {

    @Autowired
    private JacksonTester<Sighting> json;

    @Autowired
    private JacksonTester<Sighting[]> jsonList;

    private Sighting[] sightings;

    @BeforeEach
    void setUp() {
        sightings = new Sighting[]{
                createSightingWithValues(1, LocalDateTime.parse("2025-01-27T21:42:59"), "Dublin", "Dublin", "Ireland",
                        "circular", 300, "5 minutes",
                        "Spotted hovering above the Five Lamps on Amiens St", LocalDateTime.parse("2025-01-27T21:42:59"),
                        53.349804F, -6.26031F),
                createSightingWithValues(100, LocalDateTime.parse("2025-01-30T21:42:59"), "Castleblayney", "Monaghan",
                        "Ireland", "oval", 300, "5 minutes",
                        "Flickering over the Spectrum on Main St at dusk", LocalDateTime.parse("2025-01-30T21:43:59"),
                        54.119804F, -6.733380F)
        };
    }

    @Test
    void sightingSerializationTest() throws IOException {
        Sighting sighting = sightings[0];
        assertThat(json.write(sighting)).isStrictlyEqualToJson("single.json");
        assertThat(json.write(sighting)).hasJsonPathNumberValue("@.id");
        assertThat(json.write(sighting)).extractingJsonPathNumberValue("@.id")
                .isEqualTo(1);
        assertThat(json.write(sighting)).hasJsonPathStringValue("@.city");
        assertThat(json.write(sighting)).extractingJsonPathStringValue("@.city")
                .isEqualTo("Dublin");
        assertThat(json.write(sighting)).hasJsonPathStringValue("@.shape");
        assertThat(json.write(sighting)).extractingJsonPathStringValue("@.shape")
                .isEqualTo("circular");
        assertThat(json.write(sighting)).hasJsonPathStringValue("@.comments");
        assertThat(json.write(sighting)).extractingJsonPathStringValue("@.comments")
                .isEqualTo("Spotted hovering above the Five Lamps on Amiens St");
    }

    @Test
    void sightingDeserializationTest() throws IOException {
        String content = Files.readString(Paths.get("src/test/resources/org/ufo/sightings/json/single.json"));
        assertThat(json.parseObject(content).getId()).isEqualTo(1);
        assertThat(json.parseObject(content).getDatetime()).isEqualTo(LocalDateTime.parse("2025-01-27T21:42:59"));
        assertThat(json.parseObject(content).getCity()).isEqualTo("Dublin");
        assertThat(json.parseObject(content).getShape()).isEqualTo("circular");
        assertThat(json.parseObject(content).getDurationSeconds()).isEqualTo(300);
        assertThat(json.parseObject(content).getComments()).isEqualTo("Spotted hovering above the Five Lamps on Amiens St");
        assertThat(json.parseObject(content).getLatitude()).isEqualTo(53.349804F);
    }

    @Test
    void sightingListSerializationTest() throws IOException {
        assertThat(jsonList.write(sightings)).isStrictlyEqualToJson("list.json");
    }

    @Test
    void sightingListDeserializationTest() throws IOException {
        String content = Files.readString(Paths.get("src/test/resources/org/ufo/sightings/json/list.json"));
        assertThat(jsonList.parse(content).getObject().length).isEqualTo(sightings.length);
    }

    private Sighting createSightingWithValues(int id, LocalDateTime datetime, String city, String state, String country,
                                              String shape, int durationSeconds, String durationHoursMins, String comments,
                                              LocalDateTime datePosted, float latitude, float longitude) {
        Sighting newSighting = new Sighting();
        newSighting.setId(id);
        newSighting.setDatetime(datetime);
        newSighting.setCity(city);
        newSighting.setState(state);
        newSighting.setCountry(country);
        newSighting.setShape(shape);
        newSighting.setDurationSeconds(durationSeconds);
        newSighting.setDurationHoursMins(durationHoursMins);
        newSighting.setComments(comments);
        newSighting.setDatePosted(datePosted);
        newSighting.setLatitude(latitude);
        newSighting.setLongitude(longitude);

        return newSighting;
    }
}
