package com.challengeAlura.ChallengeLiteratura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro(
        int id,
        @JsonAlias("title") String title,
        List<Autor> authors,
        List<String> summaries,
        List<String> subjects,
        List<String> languages,
        boolean copyright,
        @JsonAlias("media_type") String mediaType,
        Map<String, String> formats,
        @JsonAlias("download_count") int downloadCount
) {}