package com.example.anyatoon;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.anyatoon.CanhanActivity;
import com.example.anyatoon.KenhActivity;
import com.example.anyatoon.R;
import com.example.anyatoon.SearchActivity;
import com.example.anyatoon.adapter.UnifiedAdapter;
import com.example.anyatoon.adapter.ViewPagerAdapter;
import com.example.anyatoon.model.Books;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private RecyclerView recyclerViewBTV, recyclerViewThich, recyclerViewHot;
    private UnifiedAdapter unifiedAdapter;
    private Handler handler;
    private Runnable runnable;
    private int currentPage = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Thiết lập ViewPager cho slideshow
        viewPager = findViewById(R.id.viewPager);
        List<Integer> imageList = Arrays.asList(
                R.drawable.anhsilde1, R.drawable.anhsilde2, R.drawable.anhslide3, R.drawable.anhslide4, R.drawable.slide5
        );
        viewPagerAdapter = new ViewPagerAdapter(this, imageList);
        viewPager.setAdapter(viewPagerAdapter);

        // Tự động chuyển đổi giữa các trang của ViewPager
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (viewPagerAdapter.getCount() > 0) {
                    currentPage = (currentPage + 1) % viewPagerAdapter.getCount();
                    viewPager.setCurrentItem(currentPage, true);
                }
                handler.postDelayed(this, 3000); // Đặt thời gian trượt là 3 giây
            }
        };
        handler.postDelayed(runnable, 3000);

        // Thiết lập nút tìm kiếm
        ImageView searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(v -> {
            // Chuyển đổi sang fragment_search
            Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        // Thiết lập RecyclerView cho các mục tiêu đề
        recyclerViewBTV = findViewById(R.id.recyclerViewBTV);
        recyclerViewThich = findViewById(R.id.recyclerViewThich);
        recyclerViewHot = findViewById(R.id.recyclerViewHot);

        // Lấy danh sách sách và sách hot
        List<Books> bookList = getBookList();
        List<Books> bookHotList = getBookHotList();

        // Sắp xếp sách hot
        bookHotList.sort((b1, b2) -> {
            if (b1.getPopularity() != b2.getPopularity()) {
                return b2.getPopularity() - b1.getPopularity(); // Sắp xếp theo độ hot giảm dần
            }
            return b2.getLikes() - b1.getLikes(); // Nếu độ hot bằng nhau, sắp xếp theo likes giảm dần
        });

        // Thiết lập Adapter cho RecyclerView và xử lý sự kiện nhấn vào sách
        unifiedAdapter = new UnifiedAdapter(this, bookList, book -> {
            Intent intent = new Intent(HomeActivity.this, BookDetailActivity.class);
            intent.putExtra("book", book);
            startActivity(intent);
        });
        recyclerViewBTV.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerViewThich.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerViewBTV.setAdapter(unifiedAdapter);
        recyclerViewThich.setAdapter(unifiedAdapter);

        UnifiedAdapter hotAdapter = new UnifiedAdapter(this, bookHotList, book -> {
            Intent intent = new Intent(HomeActivity.this, BookDetailActivity.class);
            intent.putExtra("book", book);
            startActivity(intent);
        });
        recyclerViewHot.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewHot.setAdapter(hotAdapter);

        // Thiết lập các nút điều hướng dưới cùng
        LinearLayout homeButton = findViewById(R.id.Home);
        LinearLayout kenhButton = findViewById(R.id.Kenh);
        LinearLayout canhanButton = findViewById(R.id.Canhan);

        homeButton.setOnClickListener(v -> {
            // Không cần xử lý vì đây là trang hiện tại
        });

        kenhButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, KenhActivity.class);
            startActivity(intent);
            finish();
        });

        canhanButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CanhanActivity.class);
            startActivity(intent);
            finish();
        });

    }

    // Phương thức giả sử để lấy danh sách sách
    private List<Books> getBookList() {
        List<Books> books = new ArrayList<>();
        // Thêm sách vào danh sách
        books.add(new Books("Book 1", "Author 1", "Genre 1", "Status 1", 100, 120, "https://example.com/cover1.jpg"));
        books.add(new Books("Book 2", "Author 2", "Genre 2", "Status 2", 90, 110, "https://example.com/cover2.jpg"));
        books.add(new Books("Book 3", "Author 3", "Genre 3", "Status 3", 80, 100, "https://example.com/cover3.jpg"));
        // Thêm các sách khác nếu cần
        return books;
    }

    // Phương thức giả sử để lấy danh sách sách hot
    private List<Books> getBookHotList() {
        List<Books> books = new ArrayList<>();
        // Thêm sách hot vào danh sách
        books.add(new Books("Hot Book 1", "Hot Author 1", "Hot Genre 1", "Hot Status 1", 200, 220, "https://example.com/hotcover1.jpg"));
        books.add(new Books("Hot Book 2", "Hot Author 2", "Hot Genre 2", "Hot Status 2", 190, 210, "https://example.com/hotcover2.jpg"));
        books.add(new Books("Hot Book 3", "Hot Author 3", "Hot Genre 3", "Hot Status 3", 180, 200, "https://example.com/hotcover3.jpg"));
        // Thêm các sách hot khác nếu cần
        return books;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable); // Ngừng tự động trượt khi activity bị hủy
    }
}
