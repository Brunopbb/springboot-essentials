package academy.devdojo.springboot2essentials.client;

import academy.devdojo.springboot2essentials.domain.Anime;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Locale;


@Log4j2
public class SpringClient {

    public static void main(String[] args) {

        //GET

        //ResponseEntity<Anime> entity = new RestTemplate().getForEntity("http://localhost:8080/animes/", Anime.class);

        ResponseEntity<List<Anime>> exchange = new RestTemplate().exchange("http://localhost:8080/animes/", HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Anime>>() {
                });

        log.info(exchange.getBody());

        //POST
//        Anime samuraiChamploo = Anime.builder()
//                .name("Samurai Champloo")
//                .build();
//
//        ResponseEntity<Anime> samuraiSaved = new RestTemplate().exchange("http://localhost:8080/animes/", HttpMethod.POST,
//                new HttpEntity<>(samuraiChamploo), Anime.class);

        //PUT

        Anime animeUpdate = Anime.builder()
                .id(13L)
                .name("Samurai Champloo".toUpperCase())
                .build();

        new RestTemplate().exchange("http://localhost:8080/animes/", HttpMethod.PUT,
                new HttpEntity<>(animeUpdate), Anime.class);

        //DELETE

        new RestTemplate().exchange("http://localhost:8080/animes/{id}",
                HttpMethod.DELETE,
                null,
                Void.class, 13);





    }

}
