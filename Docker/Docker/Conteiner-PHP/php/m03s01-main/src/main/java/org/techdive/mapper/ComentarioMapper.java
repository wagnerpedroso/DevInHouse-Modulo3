package org.techdive.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.techdive.dto.ComentarioRequest;
import org.techdive.dto.ComentarioResponse;
import org.techdive.model.Comentario;

@Mapper
public interface ComentarioMapper {

    ComentarioMapper INSTANCE = Mappers.getMapper( ComentarioMapper.class );

    ComentarioResponse toResponse(Comentario model);

    Comentario toModel(ComentarioResponse response);

    ComentarioRequest toRequest(Comentario model);

    Comentario toModel(ComentarioRequest request);

}
