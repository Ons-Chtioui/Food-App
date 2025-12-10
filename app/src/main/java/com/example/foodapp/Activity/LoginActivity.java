package com.example.foodapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class LoginActivity extends BaseActivity {
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setVariable();}
        private void setVariable() {
     binding.loginBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String email=binding.userEdt.getText().toString();
        String password=binding.passEdt.getText().toString();
        if(!email.isEmpty() && !password.isEmpty()){
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>(){
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                    else{

                        Toast.makeText(LoginActivity.this,"Authentication failed",Toast.LENGTH_SHORT ).show();
                    }
                }
            });
        }else{
            Toast.makeText(LoginActivity.this,"Please fill username and password",Toast.LENGTH_SHORT ).show();
        }
    }});
     binding.signupBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                }
            });


}


    }
