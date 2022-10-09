package org.techdive;

import org.techdive.dto.VideoRequest;
import org.techdive.model.Video;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestHelper {

    public static Video gerarVideo() {
        return new Video("id", "url", "assunto", "usuario");
    }

    public static VideoRequest gerarVideoRequest() {
        return new VideoRequest("url", "assunto", "usuario", 30);
    }

    public static List<Video> gerarVideos() {
        List<Video> videos = IntStream.rangeClosed(0, 9)
                .mapToObj(i -> new Video("id" + i, "url" + 1, "assunto" + i, "usuario" + i))
                .collect(Collectors.toList());
        return videos;
    }

}
