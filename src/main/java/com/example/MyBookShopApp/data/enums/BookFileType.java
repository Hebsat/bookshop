package com.example.MyBookShopApp.data.enums;

public enum BookFileType {

    PDF(".pdf"), EPUB(".epub"), FB2(".fb2");

    private final String fileExtensionString;

    BookFileType(String fileExtensionString) {
        this.fileExtensionString = fileExtensionString;
    }

    public static String getExtensionString(BookFileType fileType) {
        return fileType.fileExtensionString;
    }
}
