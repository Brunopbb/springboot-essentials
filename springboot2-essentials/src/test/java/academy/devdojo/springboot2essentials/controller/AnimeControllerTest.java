package academy.devdojo.springboot2essentials.controller;


import academy.devdojo.springboot2essentials.domain.Anime;
import academy.devdojo.springboot2essentials.requests.AnimePostRequestBody;
import academy.devdojo.springboot2essentials.requests.AnimePutRequestBody;
import academy.devdojo.springboot2essentials.service.AnimeService;
import academy.devdojo.springboot2essentials.util.AnimeCreator;
import academy.devdojo.springboot2essentials.util.AnimePostRequestBodyCreator;
import academy.devdojo.springboot2essentials.util.AnimePutRequestBodyCreator;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
@Log4j2
@ExtendWith(SpringExtension.class)
class AnimeControllerTest {

    @InjectMocks
    private AnimeController animeController;
    @Mock
    private AnimeService animeServiceMock;

    @BeforeEach
    void setUp(){

        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeServiceMock.listAll(ArgumentMatchers.any()))
                .thenReturn(animePage);

        BDDMockito.when(animeServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(AnimeCreator.createValidAnime());

        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeServiceMock.save(ArgumentMatchers.any(AnimePostRequestBody.class)))
                .thenReturn(AnimeCreator.createValidAnime());

        BDDMockito.doNothing().when(animeServiceMock).replace(ArgumentMatchers.any(AnimePutRequestBody.class));

        BDDMockito.doNothing().when(animeServiceMock).delete(ArgumentMatchers.anyLong());

    }

    @Test
    @DisplayName("list returns list of anime inside page object when successful")
    void list_ReturnsListOfAnimeInsidePageObject_WhenSuccessful(){

       String expectedName = AnimeCreator.createValidAnime().getName();

       Page<Anime> animePage = animeController.list(null).getBody();

        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList()).isNotEmpty();
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);


    }

    @Test
    @DisplayName("findById returns anime when successful")
    void findById_ReturnsAnime_WhenSuccessful(){

        Anime expectedAnime = AnimeCreator.createValidAnime();

        Anime anime = animeController.findById(1L).getBody();

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isEqualTo(expectedAnime.getId());
        Assertions.assertThat(anime.getName()).isEqualTo(expectedAnime.getName());


    }

    @Test
    @DisplayName("findByName returns a list anime when successful")
    void findByName_ReturnsListOfAnime_WhenSuccessful(){

        String expectedName = AnimeCreator.createValidAnime().getName();

        List<Anime> animes = animeController.findByName("anime").getBody();


        Assertions.assertThat(animes).isNotNull();
        Assertions.assertThat(animes).isNotEmpty();
        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);


    }

    @Test
    @DisplayName("findByName returns an empty list when anime is not found")
    void findByName_ReturnsEmptyListOfAnime_WhenAnimeIsNotFound(){

        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of());

        List<Anime> animes = animeController.findByName("anime").getBody();

        Assertions.assertThat(animes).isNotNull();
        Assertions.assertThat(animes).isEmpty();


    }


    @Test
    @DisplayName("save returns anime when successful")
    void save_ReturnsAnime_WhenSuccessful(){

        Anime anime = animeController.save(AnimePostRequestBodyCreator.createAnimePostRequestBody()).getBody();

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime).isEqualTo(AnimeCreator.createValidAnime());


    }

    @Test
    @DisplayName("replace update anime when successful")
    void replace_UpdateAnime_WhenSuccessful(){

        Assertions.assertThatCode(() -> animeController.replace(AnimePutRequestBodyCreator.createAnimePutRequestBody()))
                .doesNotThrowAnyException();


    }
    @Test
    @DisplayName("delete removes anime when successful")
    void delete_RemovesAnime_WhenSuccessful(){

        Assertions.assertThatCode(() -> animeController.delete(1L))
                .doesNotThrowAnyException();


    }




}