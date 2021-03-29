package com.msoft.garbagemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.msoft.garbagemanagement.dao.User;

public class UserRegistration extends AppCompatActivity {
    private static final String TAG = "UserRegistration";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSave();
            }
        });
    }

    @Override
    public void onBackPressed() {    }

    User readInputs(){
        User u=new User();
        u.setName(((TextInputEditText)findViewById(R.id.txt_name)).getText().toString());
        u.setCity(((TextInputEditText)findViewById(R.id.txt_city)).getText().toString());
        u.setMobile(((TextInputEditText)findViewById(R.id.txt_mobile)).getText().toString());
        switch( ((ChipGroup)findViewById(R.id.chip_type)).getCheckedChipId()){
            case R.id.chip_consumer:
                u.setConsumer(true);
                break;
            case R.id.chip_user:
                u.setConsumer(false);
        }
        return u;
    }

    private void handleSave() {
        User user=readInputs();
        if(user.isValid()){
            user.setUserid(FirebaseAuth.getInstance().getCurrentUser().getUid());
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(user.getUserid()).set(user);
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
        else {
            Snackbar.make(findViewById(R.id.root), R.string.not_valid_input, Snackbar.LENGTH_SHORT).show();
        }
    }
}