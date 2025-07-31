package com.challengeAlura.ChallengeLiteratura.model;

import java.util.List;

public record DatosRespuesta(
        int count,
        String next,
        String previous,
        List<DatosLibro> results
) {}