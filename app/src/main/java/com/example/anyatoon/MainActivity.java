package com.example.anyatoon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.anyatoon.database.DatabaseHelper;
import com.example.anyatoon.model.User;
import java.util.ArrayList; // Thêm import ArrayList
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private EditText username, password;
    private CheckBox rememberMe;
    private Button loginButton, registerButton;
    private DatabaseHelper db;
    private ImageView imageBiaApp;
    private RelativeLayout loginLayout;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo các đối tượng và lấy tham chiếu đến các view trong layout
        db = new DatabaseHelper(this);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        rememberMe = findViewById(R.id.remember_me);
        loginButton = findViewById(R.id.login);
        registerButton = findViewById(R.id.register);
        imageBiaApp = findViewById(R.id.Imagebiaapp);
        loginLayout = findViewById(R.id.login_layout);

        // Lấy SharedPreferences để lưu trữ và truy xuất thông tin
        preferences = getSharedPreferences("USER_PREF", MODE_PRIVATE);

        // Kiểm tra nếu cần hiển thị ảnh bìa app
        if (getIntent().getBooleanExtra("fromRegister", false)) {
            // Nếu từ trang đăng ký trở lại, không hiển thị ảnh bìa app
            imageBiaApp.setVisibility(View.GONE);
            loginLayout.setVisibility(View.VISIBLE);
        } else {
            // Hiển thị ảnh bìa trong 5 giây rồi chuyển sang giao diện đăng nhập
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    imageBiaApp.setVisibility(View.GONE);
                    loginLayout.setVisibility(View.VISIBLE);
                }
            }, 5000);
        }

        // Kiểm tra nếu đã chọn "Nhớ mật khẩu" trước đó
        if (preferences.getBoolean("remember", false)) {
            List<String> rememberedAccounts = getRememberedAccounts();
            if (!rememberedAccounts.isEmpty()) {
                // Điền tài khoản đầu tiên được nhớ vào EditText
                String savedUsername = rememberedAccounts.get(0);
                username.setText(savedUsername);
                password.setText(getPasswordForAccount(savedUsername));
                rememberMe.setChecked(true);
            }
        }

        // Xử lý sự kiện khi nhấn nút đăng nhập
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

                // Kiểm tra thông tin đăng nhập từ cơ sở dữ liệu
                User dbUser = db.getUser(user, pass);
                if (dbUser != null) {
                    Toast.makeText(MainActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                    // Lưu thông tin tài khoản nếu chọn "Nhớ mật khẩu"
                    if (rememberMe.isChecked()) {
                        saveRememberedAccount(user, pass);
                    }

                    // Chuyển đến Activity Home sau khi đăng nhập thành công
                    Intent intent = new Intent(MainActivity.this, com.example.anyatoon.HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Tên tài khoản hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xử lý sự kiện khi nhấn nút đăng ký
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                intent.putExtra("fromLogin", true);
                startActivity(intent);
            }
        });
    }

    // Lấy danh sách tài khoản đã ghi nhớ từ SharedPreferences
    private List<String> getRememberedAccounts() {
        Set<String> accountSet = preferences.getStringSet("accounts", new HashSet<>());
        return new ArrayList<>(accountSet).subList(0, Math.min(accountSet.size(), 2));
    }

    // Lấy mật khẩu đã lưu cho tài khoản từ SharedPreferences
    private String getPasswordForAccount(String username) {
        return preferences.getString("password_" + username, "");
    }

    // Lưu thông tin tài khoản và mật khẩu vào SharedPreferences
    private void saveRememberedAccount(String username, String password) {
        SharedPreferences.Editor editor = preferences.edit();
        Set<String> accountSet = preferences.getStringSet("accounts", new HashSet<>());
        if (!accountSet.contains(username)) {
            if (accountSet.size() >= 2) {
                // Xóa tài khoản cũ nhất nếu đã đạt đến giới hạn 2 tài khoản
                accountSet.remove(accountSet.iterator().next());
            }
            accountSet.add(username);
        }
        editor.putStringSet("accounts", accountSet);
        editor.putString("password_" + username, password);
        editor.apply();
    }
}
