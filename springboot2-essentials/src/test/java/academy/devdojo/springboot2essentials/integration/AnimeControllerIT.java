package academy.devdojo.springboot2essentials.integration;

import academy.devdojo.springboot2essentials.domain.Anime;
import academy.devdojo.springboot2essentials.repository.AnimeRepository;
import academy.devdojo.springboot2essentials.requests.AnimePostRequestBody;
import academy.devdojo.springboot2essentials.util.AnimeCreator;
import academy.devdojo.springboot2essentials.util.AnimePostRequestBodyCreator;
import academy.devdojo.springboot2essentials.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class AnimeControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private AnimeRepository animeRepository;

    @Test
    @DisplayName("list returns list of anime inside page object when successful")
    void list_ReturnsListOfAnimeInsidePageObject_WhenSuccessful(){

        Anime animeSaved = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        String expectedName = animeSaved.getName();

        PageableResponse<Anime> animePage = testRestTemplate.exchange("/animes", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Anime>>() {}).getBody();


        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList()).isNotEmpty();
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);

    }


    @Test
    @DisplayName("findById returns anime when successful")
    void findById_ReturnsAnime_WhenSuccessful(){

        Anime animeSaved = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        Long expectedId = animeSaved.getId();

        Anime anime = testRestTemplate.getForObject("/animes/{id}", Anime.class, expectedId);

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId()).isEqualTo(expectedId);
        Assertions.assertThat(anime.getName()).isEqualTo(animeSaved.getName());


    }

    @Test
    @DisplayName("findByName returns a list anime when successful")
    void findByName_ReturnsListOfAnime_WhenSuccessful(){

        Anime animeSaved = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        String expectedName = animeSaved.getName();
        String url = String.format("/animes/find?name=%s", expectedName);

        List<Anime> animes = testRestTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {}).getBody();


        Assertions.assertThat(animes).isNotNull();
        Assertions.assertThat(animes).isNotEmpty();
        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);


    }

    @Test
    @DisplayName("findByName returns an empty list when anime is not found")
    void findByName_ReturnsEmptyListOfAnime_WhenAnimeIsNotFound(){


        List<Anime> animes = testRestTemplate.exchange("/animes/find?name=test", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {}).getBody();
        Assertions.assertThat(animes).isNotNull();
        Assertions.assertThat(animes).isEmpty();



    }
//
//
    @Test
    @DisplayName("save returns anime when successful")
    void save_ReturnsAnime_WhenSuccessful(){

        AnimePostRequestBody animeToBeSave = AnimePostRequestBodyCreator.createAnimePostRequestBody();

        ResponseEntity<Anime> animeResponseEntity = testRestTemplate.postForEntity("/animes/", animeToBeSave, Anime.class);

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(animeResponseEntity.getBody()).isNotNull();
        Assertions.assertThat(animeResponseEntity.getBody().getId()).isNotNull();




    }

    @Test
    @DisplayName("replace update anime when successful")
    void replace_UpdateAnime_WhenSuccessful(){

        Anime animeSaved = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        animeSaved.setName("new name");

        ResponseEntity<Void> animeResponseEntity = testRestTemplate.exchange("/animes/",
                HttpMethod.PUT, new HttpEntity<>(animeSaved), Void.class);

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);



    }
    @Test
    @DisplayName("delete removes anime when successful")
    void delete_RemovesAnime_WhenSuccessful(){

        Anime animeSaved = animeRepository.save(AnimeCreator.createAnimeToBeSaved());



        ResponseEntity<Void> animeResponseEntity = testRestTemplate.exchange("/animes/{id}",
                HttpMethod.DELETE, null, Void.class, animeSaved.getId());

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);


    }



}
