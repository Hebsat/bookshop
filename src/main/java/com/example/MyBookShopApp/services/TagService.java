package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.Tag;
import com.example.MyBookShopApp.repositories.BookRepository;
import com.example.MyBookShopApp.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class TagService {

    @Value("${bookshop.default.offset}")
    private int defaultOffset;
    @Value("${bookshop.default.limit}")
    private int defaultLimit;
    private static final String[] TAG_CLASSES = {"Tag Tag_xs", "Tag Tag_sm", "Tag", "Tag Tag_md", "Tag Tag_lg"};

    private final TagRepository tagRepository;
    private final BookRepository bookRepository;

    @Autowired
    public TagService(TagRepository tagRepository, BookRepository bookRepository) {
        this.tagRepository = tagRepository;
        this.bookRepository = bookRepository;
    }

    public List<Tag> getTags() {
        return setTagVolumes(tagRepository.findAll());
    }

    public Tag getTagBySlug(String tagSlug) {
        return tagRepository.findTagBySlug(tagSlug);
    }

    public Page<Book> getPageOfBooksByTag(String slug, int page, int size) {
        Pageable nextPage = PageRequest.of(page, size);
        Tag tag = tagRepository.findTagBySlug(slug);
        return bookRepository.findBooksByTagListContains(tag, nextPage);
    }

    public Page<Book> getPageOfBooksByTag(String slug) {
        return getPageOfBooksByTag(slug, defaultOffset, defaultLimit);
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
