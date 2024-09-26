package com.example.shashank.login;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    EditText username, Password, Name ;
    Button Register;
    String NameHolder, usernameHolder, PasswordHolder;
    Boolean EditTextEmptyHolder;
    SQLiteDatabase sqLiteDatabaseObj;
    String SQLiteDataBaseQueryHolder ;
    SQLiteHelper sqLiteHelper;
    Cursor cursor;
    String F_Result = "Not_Found";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Register = (Button)findViewById(R.id.buttonRegister);
        username = (EditText)findViewById(R.id.editEmail);
        Password = (EditText)findViewById(R.id.editPassword);
        Name = (EditText)findViewById(R.id.editName);
        sqLiteHelper = new SQLiteHelper(this);

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDataBaseBuild();
                SQLiteTableBuild();
                CheckEditTextStatus();
                CheckingusernameAlreadyExistsOrNot();
                EmptyEditTextAfterDataInsert();
            }
        });

    }

    public void SQLiteDataBaseBuild(){
        sqLiteDatabaseObj = openOrCreateDatabase(SQLiteHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);
    }

    public void SQLiteTableBuild() {
        sqLiteDatabaseObj.execSQL("CREATE TABLE IF NOT EXISTS " + SQLiteHelper.TABLE_NAME + "(" + SQLiteHelper.Table_Column_ID + " PRIMARY KEY AUTOINCREMENT NOT NULL, " + SQLiteHelper.Table_Column_1_Name + " VARCHAR, " + SQLiteHelper.Table_Column_2_username + " VARCHAR, " + SQLiteHelper.Table_Column_3_Password + " VARCHAR);");
    }

    public void InsertDataIntoSQLiteDatabase(){
        if(EditTextEmptyHolder == true)
        {
            SQLiteDataBaseQueryHolder = "INSERT INTO "+SQLiteHelper.TABLE_NAME+" (name,username,password) VALUES('"+NameHolder+"', '"+usernameHolder+"', '"+PasswordHolder+"');";
            sqLiteDatabaseObj.execSQL(SQLiteDataBaseQueryHolder);
            sqLiteDatabaseObj.close();
            Toast.makeText(RegisterActivity.this,"Register Sukses", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(RegisterActivity.this,"Text  harus diisi semua !", Toast.LENGTH_LONG).show();
        }

    }


    public void EmptyEditTextAfterDataInsert(){
        Name.getText().clear();
        username.getText().clear();
        Password.getText().clear();
    }

    public void CheckEditTextStatus(){

        NameHolder = Name.getText().toString() ;
        usernameHolder = username.getText().toString();
        PasswordHolder = Password.getText().toString();

        if(TextUtils.isEmpty(NameHolder) || TextUtils.isEmpty(usernameHolder) || TextUtils.isEmpty(PasswordHolder)){
            EditTextEmptyHolder = false ;
        }
        else {
            EditTextEmptyHolder = true ;
        }
    }

    public void CheckingusernameAlreadyExistsOrNot(){
        sqLiteDatabaseObj = sqLiteHelper.getWritableDatabase();
        cursor = sqLiteDatabaseObj.query(SQLiteHelper.TABLE_NAME, null, " " + SQLiteHelper.Table_Column_2_username + "=?", new String[]{usernameHolder}, null, null, null);
        while (cursor.moveToNext()) {
            if (cursor.isFirst()) {
                cursor.moveToFirst();
                F_Result = "username Found";
                cursor.close();
            }
        }
        CheckFinalResult();
    }


    public void CheckFinalResult(){
        if(F_Result.equalsIgnoreCase("username Found"))
        {
            Toast.makeText(RegisterActivity.this,"username Already Exists",Toast.LENGTH_LONG).show();
        }
        else {
            InsertDataIntoSQLiteDatabase();
        }
        F_Result = "Not_Found" ;
    }
}