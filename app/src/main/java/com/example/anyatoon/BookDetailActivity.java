package com.example.anyatoon;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.anyatoon.adapter.UnifiedAdapter;
import com.example.anyatoon.model.Books;
import com.example.anyatoon.model.Review;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class BookDetailActivity extends AppCompatActivity {

    private ImageView bookCoverImageView, backgroundImagebiatruyen, imgtrove, imgbachammenu, image_menubl;
    private TextView bookTitleTextView, bookGenreTextView, bookStatusTextView, bookAuthorTextView,
            bookLikesTextView, bookPopularityTextView, bookdanhgiaTextView, textgioithieutruyen, rating_text;
    private ImageButton send_icon, imgbuttontrove;
    private Button buttonchitiet, buttonchapter, btnbatdaudoc;
    private EditText edit_text_danhgia;
    private RatingBar rating_bar;
    private LinearLayout detailsLayout, chaptersLayout, danhgiasao;
    private RelativeLayout layoutchinhsuadanhgia, danhgiaLayout;
    private boolean isFollowing = false;
    private ArrayList<String> reviews = new ArrayList<>();
    private ListView danhgiaListView;
    private String currentReviewText;
    private float currentRating;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitiettruyen);

        // Liên kết các thành phần giao diện
        bookCoverImageView = findViewById(R.id.bookCoverImageView);
        backgroundImagebiatruyen = findViewById(R.id.backgroundImagebiatruyen);
        bookTitleTextView = findViewById(R.id.bookTitleTextView);
        bookGenreTextView = findViewById(R.id.bookGenreTextView);
        bookStatusTextView = findViewById(R.id.bookStatusTextView);
        bookAuthorTextView = findViewById(R.id.bookAuthorTextView);
        bookLikesTextView = findViewById(R.id.bookLikesTextView);
        bookPopularityTextView = findViewById(R.id.bookPopularityTextView);
        bookdanhgiaTextView = findViewById(R.id.bookdanhgiaTextView);
        textgioithieutruyen = findViewById(R.id.textgioithieutruyen);
        rating_text = findViewById(R.id.rating_text);
        imgtrove = findViewById(R.id.imgtrove);
        imgbachammenu = findViewById(R.id.imgbachammenu);
        send_icon = findViewById(R.id.send_icon);
        imgbuttontrove = findViewById(R.id.imgbuttontrove);
        buttonchitiet = findViewById(R.id.buttonchitiet);
        buttonchapter = findViewById(R.id.buttonchapter);
        btnbatdaudoc = findViewById(R.id.btnbatdaudoc);
        edit_text_danhgia = findViewById(R.id.edit_text_danhgia);
        rating_bar = findViewById(R.id.rating_bar);
        detailsLayout = findViewById(R.id.detailsLayout);
        chaptersLayout = findViewById(R.id.chaptersLayout);
        danhgiaLayout = findViewById(R.id.danhgiaLayout);
        layoutchinhsuadanhgia = findViewById(R.id.layoutchinhsuadanhgia);
        danhgiaListView = findViewById(R.id.danhgiaListView);
        image_menubl = findViewById(R.id.image_menubl);

        // Thiết lập dữ liệu tĩnh cho các thành phần giao diện
        setupStaticData();

        // Sự kiện nút trở về
        imgtrove.setOnClickListener(view -> finish()); // Trở về activity trước đó

        // Sự kiện nút menu con
        imgbachammenu.setOnClickListener(this::showPopupMenu);

        // Hiển thị chi tiết truyện khi nhấn nút chi tiết
        buttonchitiet.setOnClickListener(view -> {
            detailsLayout.setVisibility(View.VISIBLE);
            chaptersLayout.setVisibility(View.GONE);
        });

        // Hiển thị danh sách chương khi nhấn nút chương
        buttonchapter.setOnClickListener(view -> {
            detailsLayout.setVisibility(View.GONE);
            chaptersLayout.setVisibility(View.VISIBLE);
        });

        // Sự kiện nút bắt đầu đọc (giả lập chuyển Activity)
        btnbatdaudoc.setOnClickListener(view -> {
            Intent intent = new Intent(BookDetailActivity.this, ReadActivity.class);
            startActivity(intent);
        });

        // Sự kiện hiển thị layout đánh giá
        findViewById(R.id.danhgia).setOnClickListener(view -> danhgiaLayout.setVisibility(View.VISIBLE));

        // Sự kiện nút trở về trong layout đánh giá
        imgbuttontrove.setOnClickListener(view -> danhgiaLayout.setVisibility(View.GONE));

        // Sự kiện RatingBar
        rating_bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                currentRating = rating;
                switch ((int) rating) {
                    case 1:
                        rating_text.setText("Quá tệ");
                        break;
                    case 2:
                        rating_text.setText("Thất vọng");
                        break;
                    case 3:
                        rating_text.setText("Bình thường");
                        break;
                    case 4:
                        rating_text.setText("Tốt");
                        break;
                    case 5:
                        rating_text.setText("Rất tốt");
                        break;
                }
            }
        });

        // Sự kiện gửi đánh giá
        send_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String reviewText = edit_text_danhgia.getText().toString();
                if (!reviewText.isEmpty()) {
                    reviews.add(reviewText);
                    Toast.makeText(BookDetailActivity.this, "Gửi đánh giá: " + reviewText, Toast.LENGTH_SHORT).show();
                    updateReviewsList();
                    edit_text_danhgia.setText(""); // Xóa nội dung EditText
                }
            }
        });

        // Sự kiện click vào bình luận trong danh sách
        danhgiaListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                layoutchinhsuadanhgia.setVisibility(View.VISIBLE);
                currentReviewText = reviews.get(position);
                edit_text_danhgia.setText(currentReviewText);
                rating_bar.setRating(currentRating);
                rating_text.setText(getRatingText((int) currentRating));
            }
        });


        // Sự kiện click vào image_menubl
        image_menubl.setOnClickListener(view -> layoutchinhsuadanhgia.setVisibility(View.VISIBLE));

        // Sự kiện sửa đánh giá
        findViewById(R.id.report_danhgia).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                danhgiaLayout.setVisibility(View.VISIBLE);
                layoutchinhsuadanhgia.setVisibility(View.GONE);
            }
        });

        // Sự kiện xóa đánh giá
        findViewById(R.id.delete_danhgia).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reviews.remove(currentReviewText);
                updateReviewsList();
                layoutchinhsuadanhgia.setVisibility(View.GONE);
                Toast.makeText(BookDetailActivity.this, "Đã xóa đánh giá", Toast.LENGTH_SHORT).show();
            }
        });

        // Sự kiện huy đánh giá
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutchinhsuadanhgia.setVisibility(View.GONE);
            }
        });
    }

    private void setupStaticData() {
        // Thiết lập dữ liệu tĩnh
        bookTitleTextView.setText("Title Example");
        bookGenreTextView.setText("Genre Example");
        bookStatusTextView.setText("Status Example");
        bookAuthorTextView.setText("Author Example");
        bookLikesTextView.setText("123");
        bookPopularityTextView.setText("456");
        bookdanhgiaTextView.setText("789");
        textgioithieutruyen.setText("Description Example");

        // Tải hình ảnh không sử dụng Glide
        new LoadImageTask(bookCoverImageView).execute("https://example.com/image.jpg");
        new LoadImageTask(backgroundImagebiatruyen).execute("https://example.com/image.jpg");
    }

    private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;

        public LoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(urlDisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

    private void updateReviewsList() {
        // Cập nhật ListView với dữ liệu đánh giá
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, reviews);
        danhgiaListView.setAdapter(adapter);
    }

    private String getRatingText(int rating) {
        switch (rating) {
            case 1:
                return "Quá tệ";
            case 2:
                return "Thất vọng";
            case 3:
                return "Bình thường";
            case 4:
                return "Tốt";
            case 5:
                return "Rất tốt";
            default:
                return "";
        }
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_chitiettruyen, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            int id = menuItem.getItemId();
            if (id == R.id.action_download) {
                // Xử lý tải xuống
                downloadBook();
                return true;
            } else if (id == R.id.action_follow) {
                // Xử lý lưu sách
                followBook(menuItem);
                return true;
            } else if (id == R.id.action_unfollow) {
                // Xử lý hủy lưu sách
                unfollowBook(menuItem);
                return true;
            } else {
                return false;
            }
        });

        // Hiển thị menu
        popupMenu.show();
    }

    private void downloadBook() {
        // Xử lý logic tải xuống
        Toast.makeText(this, "Bạn đã tải truyện xuống thành công", Toast.LENGTH_SHORT).show();
    }

    private void followBook(MenuItem menuItem) {
        // Xử lý logic lưu sách
        isFollowing = true;
        Toast.makeText(this, "Theo dõi thành công", Toast.LENGTH_SHORT).show();
        menuItem.setVisible(false); // Ẩn nút Lưu
        findViewById(R.id.action_unfollow).setVisibility(View.VISIBLE); // Hiển thị nút Hủy lưu
    }

    private void unfollowBook(MenuItem menuItem) {
        // Xử lý logic hủy lưu sách
        isFollowing = false;
        Toast.makeText(this, "Đã xóa khỏi danh sách theo dõi", Toast.LENGTH_SHORT).show();
        menuItem.setVisible(false); // Ẩn nút Hủy lưu
        findViewById(R.id.action_follow).setVisibility(View.VISIBLE); // Hiển thị nút Lưu
    }
}
