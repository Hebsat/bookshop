package com.example.MyBookShopApp.services.mappers;

import com.example.MyBookShopApp.api.BookFileDto;
import com.example.MyBookShopApp.data.main.BookFile;
import org.springframework.stereotype.Service;

@Service
public class BookFileMapper {

    public BookFileDto convertBookFileToBookFileDto(BookFile bookFile) {
        return BookFileDto.builder()
                .hash(bookFile.getHash())
                .fileType(bookFile.getBookFileType().getExtensionStringByType())
                .description(bookFile.getBookFileType().getDescription())
                .build();
    }
}
