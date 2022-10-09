package org.techdive.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.techdive.dto.VideoRequest;
import org.techdive.dto.VideoResponse;
import org.techdive.model.Video;

@Mapper
public interface VideoMapper {

    VideoMapper INSTANCE = Mappers.getMapper( VideoMapper.class );

    VideoResponse toResponse(Video model);

    Video toModel(VideoResponse response);

    VideoRequest toRequest(Video model);

    Video toModel(VideoRequest request);

}
