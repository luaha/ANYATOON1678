package com.example.anyatoon.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.RatingBar;
import com.example.anyatoon.BookDetailActivity;
import com.example.anyatoon.R;
import com.example.anyatoon.model.Books;
import java.util.List;

public class UnifiedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_BOOK = 1;
    private static final int VIEW_TYPE_BOOK_HOT = 2;

    private Context context;
    private List<Books> itemList;
    private OnItemClickListener onItemClickListener;

    // Constructor
    public UnifiedAdapter(Context context, List<Books> itemList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.itemList = itemList;
        this.onItemClickListener = onItemClickListener;
    }

    // Interface để xử lý sự kiện nhấn vào sách
    public interface OnItemClickListener {
        void onItemClick(Books book);
    }

    // Xác định loại view dựa trên độ hot của sách
    @Override
    public int getItemViewType(int position) {
        Books book = itemList.get(position);
        if (book.getPopularity() > 100) {
            return VIEW_TYPE_BOOK_HOT;
        } else {
            return VIEW_TYPE_BOOK;
        }
    }

    // Tạo các ViewHolder thích hợp dựa trên loại view
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_BOOK) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
            return new BookViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_bookhot, parent, false);
            return new BookHotViewHolder(view);
        }
    }

    // Liên kết dữ liệu với các ViewHolder
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Books book = itemList.get(position);
        if (holder instanceof BookViewHolder) {
            ((BookViewHolder) holder).bind(book, onItemClickListener);
        } else if (holder instanceof BookHotViewHolder) {
            ((BookHotViewHolder) holder).bind(book, onItemClickListener);
        }
    }

    // Trả về số lượng sách trong danh sách
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // Cập nhật danh sách sách và thông báo cho RecyclerView để làm mới giao diện
    public void updateBooks(List<Books> newBookList) {
        itemList = newBookList;
        notifyDataSetChanged();
    }

    // ViewHolder cho loại view thông thường
    public class BookViewHolder extends RecyclerView.ViewHolder {
        TextView bookTitle;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            bookTitle = itemView.findViewById(R.id.bookTitle);
        }

        // Liên kết dữ liệu với giao diện và xử lý sự kiện nhấn vào sách
        public void bind(Books book, OnItemClickListener onItemClickListener) {
            bookTitle.setText(book.getTitle());
            itemView.setOnClickListener(v -> onItemClickListener.onItemClick(book));
        }
    }

    // ViewHolder cho loại view "hot"
    public class BookHotViewHolder extends RecyclerView.ViewHolder {
        TextView bookTitleTextView, bookGenreTextView, bookStatusTextView, bookAuthorTextView, bookLikesTextView, bookPopularityTextView;

        public BookHotViewHolder(@NonNull View itemView) {
            super(itemView);
            bookTitleTextView = itemView.findViewById(R.id.bookTitleTextView);
            bookGenreTextView = itemView.findViewById(R.id.bookGenreTextView);
            bookStatusTextView = itemView.findViewById(R.id.bookStatusTextView);
            bookAuthorTextView = itemView.findViewById(R.id.bookAuthorTextView);
            bookLikesTextView = itemView.findViewById(R.id.bookLikesTextView);
            bookPopularityTextView = itemView.findViewById(R.id.bookPopularityTextView);
        }

        // Liên kết dữ liệu với giao diện và xử lý sự kiện nhấn vào sách
        public void bind(Books book, OnItemClickListener onItemClickListener) {
            bookTitleTextView.setText(book.getTitle());
            bookGenreTextView.setText(book.getGenre());
            bookStatusTextView.setText(book.getStatus());
            bookAuthorTextView.setText(book.getAuthor());
            bookLikesTextView.setText("Likes: " + book.getLikes());
            bookPopularityTextView.setText("Hot: " + book.getPopularity());
            itemView.setOnClickListener(v -> onItemClickListener.onItemClick(book));
        }
    }
}
