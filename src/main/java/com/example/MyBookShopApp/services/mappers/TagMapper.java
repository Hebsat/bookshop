package com.example.MyBookShopApp.services.mappers;

import com.example.MyBookShopApp.api.TagDto;
import com.example.MyBookShopApp.data.main.Tag;
import org.springframework.stereotype.Service;

@Service
public class TagMapper {

    public TagDto convertTagToTagDto(Tag tag) {
        return TagDto.builder()
                .id(tag.getId())
                .name(tag.getName())
                .slug(tag.getSlug())
                .volume(tag.getVolume())
                .build();
    }
}
