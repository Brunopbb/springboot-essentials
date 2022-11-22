package academy.devdojo.springboot2essentials.service;

import academy.devdojo.springboot2essentials.controller.AnimeController;
import academy.devdojo.springboot2essentials.domain.Anime;
import academy.devdojo.springboot2essentials.exception.BadRequestException;
import academy.devdojo.springboot2essentials.repository.AnimeRepository;
import academy.devdojo.springboot2essentials.requests.AnimePostRequestBody;
import academy.devdojo.springboot2essentials.requests.AnimePutRequestBody;
import academy.devdojo.springboot2essentials.util.AnimeCreator;
import academy.devdojo.springboot2essentials.util.AnimePostRequestBodyCreator;
import academy.devdojo.springboot2essentials.util.AnimePutRequestBodyCreator;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class AnimeServiceTest {

    @InjectMocks
    private AnimeService animeService;
    @Mock
    private AnimeRepository animeRepositoryMock;

    @BeforeEach
    void setUp(){

        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(animePage);

        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeRepositoryMock.save(ArgumentMatchers.any(Anime.class)))
                .thenReturn(AnimeCreator.createValidAnime());


        BDDMockito.doNothing().when(animeRepositoryMock).delete(ArgumentMatchers.any(Anime.class));

    }

    @Test
    @DisplayName("listAll returns list of anime inside page object when successful")
    void listAll_ReturnsListOfAnimeInsidePageObject_WhenSuccessful(){

        String expectedName = AnimeCreator.createValidAnime().getName();

        Page<Anime> animePage = animeService.listAll(PageRequest.of(1, 1));

        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList()).isNotEmpty();
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);


    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException returns anime when successful")
    void findByIdOrThrowBadRequestException_ReturnsAnime_WhenSuccessful(){

        Anime expectedAnime = AnimeCreator.createValidAnime();

        Anime anime = animeService.findByIdOrThrowBadRequestException(1L);

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isEqualTo(expectedAnime.getId());
        Assertions.assertThat(anime.getName()).isEqualTo(expectedAnime.getName());


    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException throws BadRequestException when anime is not found")
    void findByIdOrThrowBadRequestException_ThrowsBadRequestException_WhenAnimeIsNotFound(){

        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());


        Assertions.assertThatThrownBy(() -> animeService.findByIdOrThrowBadRequestException(2L))
                        .isInstanceOf(BadRequestException.class);




    }

    @Test
    @DisplayName("findByName returns a list anime when successful")
    void findByName_ReturnsListOfAnime_WhenSuccessful(){

        String expectedName = AnimeCreator.createValidAnime().getName();

        List<Anime> animes = animeService.findByName("anime");


        Assertions.assertThat(animes).isNotNull();
        Assertions.assertThat(animes).isNotEmpty();
        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);


    }

    @Test
    @DisplayName("findByName returns an empty list when anime is not found")
    void findByName_ReturnsEmptyListOfAnime_WhenAnimeIsNotFound(){

        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of());

        List<Anime> animes = animeService.findByName("anime");

        Assertions.assertThat(animes).isNotNull();
        Assertions.assertThat(animes).isEmpty();


    }


    @Test
    @DisplayName("save returns anime when successful")
    void save_ReturnsAnime_WhenSuccessful(){

        Anime anime = animeService.save(AnimePostRequestBodyCreator.createAnimePostRequestBody());

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime).isEqualTo(AnimeCreator.createValidAnime());


    }

    @Test
    @DisplayName("replace update anime when successful")
    void replace_UpdateAnime_WhenSuccessful(){

        Assertions.assertThatCode(() -> animeService.replace(AnimePutRequestBodyCreator.createAnimePutRequestBody()))
                .doesNotThrowAnyException();


    }
    @Test
    @DisplayName("delete removes anime when successful")
    void delete_RemovesAnime_WhenSuccessful(){

        Assertions.assertThatCode(() -> animeService.delete(1L))
                .doesNotThrowAnyException();


    }



}