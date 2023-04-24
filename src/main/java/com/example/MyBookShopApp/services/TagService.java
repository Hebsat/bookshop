package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.api.BooksListDto;
import com.example.MyBookShopApp.api.TagDto;
import com.example.MyBookShopApp.data.main.Book;
import com.example.MyBookShopApp.data.main.Tag;
import com.example.MyBookShopApp.errors.BookshopWrongParameterException;
import com.example.MyBookShopApp.services.mappers.BookMapper;
import com.example.MyBookShopApp.services.mappers.TagMapper;
import com.example.MyBookShopApp.repositories.BookRepository;
import com.example.MyBookShopApp.repositories.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    @Value("${bookshop.default.page}")
    private int defaultPage;
    @Value("${bookshop.default.size}")
    private int defaultSize;
    private static final String[] TAG_CLASSES = {"Tag Tag_xs", "Tag Tag_sm", "Tag", "Tag Tag_md", "Tag Tag_lg"};

    private final TagRepository tagRepository;
    private final BookRepository bookRepository;
    private final TagMapper tagMapper;
    private final BookMapper bookMapper;

    public List<TagDto> getTags() {
        Logger.getLogger(TagService.class.getName()).info("getTags");
        return setTagVolumes(tagRepository.findAll()).stream().map(tagMapper::convertTagToTagDto).collect(Collectors.toList());
    }

    public String getFirstPageOfBooksByTag(String slug, Model model) {
        Optional<Tag> optionalTag = tagRepository.findTagBySlug(slug);
        model.addAttribute("topBarIdentifier", "genres");
        model.addAttribute("pageTitle", "tag");
        if (optionalTag.isEmpty()) {
            model.addAttribute("error", "Тега с идентификатором " + slug + " не существует");
            return "/errors/404";
        }
        Page<Book> books = getPageOfBooksByTag(optionalTag.get(), defaultPage, defaultSize);
        if (books.getTotalPages() == 1) {
            model.addAttribute("lastPage", true);
        }
        model.addAttribute("pageTitlePart", optionalTag.get().getName());
        model.addAttribute("tag", tagMapper.convertTagToTagDto(optionalTag.get()));
        model.addAttribute("books", books.getContent().stream().map(bookMapper::convertBookToBookDtoLight).collect(Collectors.toList()));
        return "tags/index";
    }

    public BooksListDto getNextPageOfBooksByTag(String slug, int page, int size) throws BookshopWrongParameterException {
        Tag tag = tagRepository.findTagBySlug(slug)
                .orElseThrow(() -> new BookshopWrongParameterException("Тега с идентификатором " + slug + " не существует"));
        Page<Book> books = getPageOfBooksByTag(tag, page, size);
        return BooksListDto.builder()
                .totalPages(books.getTotalPages())
                .count((int) books.getTotalElements())
                .books(books.getContent().stream().map(bookMapper::convertBookToBookDtoLight).collect(Collectors.toList()))
                .build();
    }

    public BooksListDto getPageOfBooksByTagId(int id, int page, int size) throws BookshopWrongParameterException {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new BookshopWrongParameterException("Тега с идентификатором " + id + " не существует"));
        Page<Book> books = getPageOfBooksByTag(tag, page, size);
        return BooksListDto.builder()
                .totalPages(books.getTotalPages())
                .count((int) books.getTotalElements())
                .books(books.getContent().stream().map(bookMapper::convertBookToBookDtoLight).collect(Collectors.toList()))
                .build();
    }

    private Page<Book> getPageOfBooksByTag(Tag tag, int page, int size) {
        Logger.getLogger(TagService.class.getName()).info(
                "getPageOfBooksByTag " + tag.getName() + " with page " + page + " and size " + size);
        Pageable nextPage = PageRequest.of(page, size);
        return bookRepository.findBooksByTagListContains(tag, nextPage);
    }

    private List<Tag> setTagVolumes(List<Tag> tagList) {
        int maxBooksPerTag = tagList.stream().max(Comparator.comparing(tag -> tag.getBookList().size()))
                .get().getBookList().size();
        int minBooksPerTag = tagList.stream().min(Comparator.comparing(tag -> tag.getBookList().size()))
                .get().getBookList().size() - 1;
        int tagVolumeStep = (maxBooksPerTag - minBooksPerTag) / TAG_CLASSES.length;
        tagList.forEach(tag -> tag.setVolume(TAG_CLASSES[(tag.getBookList().size() - minBooksPerTag - 1) / tagVolumeStep]));
        return tagList;
    }
}
