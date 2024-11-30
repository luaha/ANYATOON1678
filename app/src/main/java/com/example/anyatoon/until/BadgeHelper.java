package com.example.anyatoon.until;

import android.widget.ImageView;
import com.example.anyatoon.R;

public class BadgeHelper {

    // Phương thức để gắn huy chương số
    public static void applyBadge(ImageView imageView, int rank) {
        switch (rank) {
            case 1:
                imageView.setImageResource(R.drawable.top1);
                break;
            case 2:
                imageView.setImageResource(R.drawable.top2);
                break;
            case 3:
                imageView.setImageResource(R.drawable.top3);
                break;
            default:
                imageView.setImageResource(0); // Không gắn huy chương cho các thứ hạng khác
                break;
        }
    }
}
