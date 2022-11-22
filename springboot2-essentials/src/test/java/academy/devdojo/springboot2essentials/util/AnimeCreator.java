package academy.devdojo.springboot2essentials.util;

import academy.devdojo.springboot2essentials.domain.Anime;

public class AnimeCreator {

    public static Anime createAnimeToBeSaved(){
        return Anime
                .builder()
                .name("Naruto")
                .build();
    }

    public static Anime createValidAnime(){
        return Anime
                .builder()
                .id(1L)
                .name("Naruto")
                .build();
    }

    public static Anime createValidUpdateAnime(){
        return Anime
                .builder()
                .id(1L)
                .name("Naruto classico")
                .build();
    }

}
