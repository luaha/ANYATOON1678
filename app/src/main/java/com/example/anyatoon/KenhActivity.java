package com.example.anyatoon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.anyatoon.R;
import com.example.anyatoon.adapter.UnifiedAdapter;
import com.example.anyatoon.model.Books;
import com.example.anyatoon.until.BookCategoryFilter;

import java.util.ArrayList;
import java.util.List;

public class KenhActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UnifiedAdapter adapter;
    private List<Books> bookList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kenh);

        // Khởi tạo danh sách sách (Dữ liệu giả để minh họa)
        bookList = new ArrayList<>();
        bookList.add(new Books("Book 1", "Author 1", "Nữ cường", "Đang phát hành", 150, 200, "https://example.com/cover1.jpg"));
        bookList.add(new Books("Book 2", "Author 2", "Kinh dị", "Hoàn thành", 100, 300, "https://example.com/cover2.jpg"));
        // Thêm các cuốn sách khác vào đây

        // Thiết lập RecyclerView
        recyclerView = findViewById(R.id.recyclerHTtruyen);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UnifiedAdapter(this, bookList, book -> {
            // Xử lý sự kiện nhấn vào sách
            Intent intent = new Intent(KenhActivity.this, BookDetailActivity.class);
            intent.putExtra("book", book);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        // Hiển thị toàn bộ sách khi mở trang kênh
        adapter.updateBooks(BookCategoryFilter.getAllBooks(bookList));

        // Xử lý sự kiện nhấn vào nút tìm kiếm
        ImageView searchButton = findViewById(R.id.buttontimkiem);
        searchButton.setOnClickListener(v -> {
            Intent intent = new Intent(KenhActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        // Xử lý sự kiện nhấn vào các nút thể loại
        Button btnToanBo = findViewById(R.id.buttonToanbo);
        btnToanBo.setOnClickListener(v -> filterBooks("Toàn bộ"));

        Button btnNuCuong = findViewById(R.id.buttonNucuong);
        btnNuCuong.setOnClickListener(v -> filterBooks("Nữ cường"));

        Button btnKinhDi = findViewById(R.id.buttonKinhdi);
        btnKinhDi.setOnClickListener(v -> filterBooks("Kinh dị"));

        Button btnHuyenHuyen = findViewById(R.id.buttonHuyenhuyen);
        btnHuyenHuyen.setOnClickListener(v -> filterBooks("Huyền huyễn"));

        // Thiết lập các nút điều hướng dưới cùng
        LinearLayout homeButton = findViewById(R.id.Home);
        LinearLayout kenhButton = findViewById(R.id.Kenh);
        LinearLayout canhanButton = findViewById(R.id.Canhan);

        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(KenhActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });

        kenhButton.setOnClickListener(v -> {
            // Đây là trang hiện tại, không cần điều hướng
        });

        canhanButton.setOnClickListener(v -> {
            Intent intent = new Intent(KenhActivity.this, CanhanActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // Phương thức lọc sách theo thể loại
    private void filterBooks(String genre) {
        List<Books> filteredBooks;
        if (genre.equals("Toàn bộ")) {
            filteredBooks = BookCategoryFilter.getAllBooks(bookList);
        } else {
            filteredBooks = BookCategoryFilter.filterByGenre(bookList, genre);
        }
        adapter.updateBooks(filteredBooks);
    }
}
