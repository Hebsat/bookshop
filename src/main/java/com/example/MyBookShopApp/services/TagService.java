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
import java.util.logging.Logger;

@Service
public class TagService {

    @Value("${bookshop.default.page}")
    private int defaultPage;
    @Value("${bookshop.default.size}")
    private int defaultSize;
    private static final String[] TAG_CLASSES = {"Tag Tag_xs", "Tag Tag_sm", "Tag", "Tag Tag_md", "Tag Tag_lg"};

    private final TagRepository tagRepository;
    private final BookRepository bookRepository;

    @Autowired
    public TagService(TagRepository tagRepository, BookRepository bookRepository) {
        this.tagRepository = tagRepository;
        this.bookRepository = bookRepository;
    }

    public List<Tag> getTags() {
        Logger.getLogger(TagService.class.getName()).info("getTags");
        return setTagVolumes(tagRepository.findAll());
    }

    public Tag getTagBySlug(String slug) {
        Logger.getLogger(TagService.class.getName()).info("getTagBySlug " + slug);
        return tagRepository.findTagBySlug(slug);
    }

    public Page<Book> getPageOfBooksByTag(String slug, int page, int size) {
        Logger.getLogger(TagService.class.getName()).info(
                "getPageOfBooksByTag " + slug + " with page " + page + " and size " + size);
        Pageable nextPage = PageRequest.of(page, size);
        Tag tag = tagRepository.findTagBySlug(slug);
        return bookRepository.findBooksByTagListContains(tag, nextPage);
    }

    public Page<Book> getPageOfBooksByTag(String slug) {
        Logger.getLogger(TagService.class.getName()).info("getPageOfBooksByTag " + slug);
        return getPageOfBooksByTag(slug, defaultPage, defaultSize);
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
