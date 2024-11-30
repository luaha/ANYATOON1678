package com.example.anyatoon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;
import com.example.anyatoon.model.User;
import com.example.anyatoon.database.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity {

    private EditText username, email, newPassword, confirmPassword;
    private Button registerButton, backToLoginButton;
    private TextView errorMessage;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangky);

        db = new DatabaseHelper(this);
        username = findViewById(R.id.username);
        email = findViewById(R.id.editemail);
        newPassword = findViewById(R.id.newpassword);
        confirmPassword = findViewById(R.id.password);
        registerButton = findViewById(R.id.register);
        backToLoginButton = findViewById(R.id.buttontrolaidangnhap);
        errorMessage = findViewById(R.id.message_error);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = newPassword.getText().toString();
                String confirmPass = confirmPassword.getText().toString();

                if (!pass.equals(confirmPass)) {
                    errorMessage.setText("Mật khẩu xác nhận không khớp.");
                    errorMessage.setVisibility(View.VISIBLE);
                } else {
                    String userId = generateUserId();
                    User newUser = new User(0, user, pass, userId); // Sử dụng lớp User
                    boolean isInserted = db.insertUser(newUser);
                    if (isInserted) {
                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        // Giữ nguyên ở trang đăng ký
                    } else {
                        Toast.makeText(RegisterActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        backToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quay lại trang đăng nhập
                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(intent);
                finish(); // Đóng trang đăng ký
            }
        });
    }

    private String generateUserId() {
        Random random = new Random();
        int number = random.nextInt(999999);
        return String.format("%06d", number);
    }
}
