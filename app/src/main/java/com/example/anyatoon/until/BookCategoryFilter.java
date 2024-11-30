package com.example.anyatoon.until;

import com.example.anyatoon.model.Books;
import java.util.ArrayList;
import java.util.List;

public class BookCategoryFilter {

    // Phương thức để lọc sách theo thể loại
    public static List<Books> filterByGenre(List<Books> books, String genre) {
        List<Books> filteredBooks = new ArrayList<>();
        for (Books book : books) {
            if (book.getGenre().equalsIgnoreCase(genre)) {
                filteredBooks.add(book);
            }
        }
        return filteredBooks;
    }

    // Phương thức để lấy toàn bộ sách
    public static List<Books> getAllBooks(List<Books> books) {
        return new ArrayList<>(books); // Trả về danh sách sao chép
    }
}
