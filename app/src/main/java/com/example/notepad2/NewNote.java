package com.example.notepad2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NewNote extends AppCompatActivity {

    EditText ed1,ed2;
    Button imageButton;

    MyDataBase myDatabase;
    Cuns cun;
    int ids;
    String color="#FFFFFF";
    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        ll = (LinearLayout) findViewById(R.id.ll);
        ed1=(EditText) findViewById(R.id.editText1);
        ed2=(EditText) findViewById(R.id.editText2);
        imageButton=(Button) findViewById(R.id.saveButton);
        myDatabase=new MyDataBase(this);

        Intent intent=this.getIntent();
        ids=intent.getIntExtra("ids", 0);
        //默认为0，不为0,则为修改数据时跳转过来的
        if(ids!=0){
            cun=myDatabase.getTiandCon(ids);
            ed1.setText(cun.getTitle());
            ed2.setText(cun.getContent());
            color=cun.getColor();
        }

        ll.setBackgroundColor(Color.parseColor(color));
        //保存按钮的点击事件，他和返回按钮是一样的功能，所以都调用isSave()方法；
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isSave();
            }
        });
    }

    /*
     * 返回按钮调用的方法。
     */
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd  HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String times = formatter.format(curDate);
        String title=ed1.getText().toString();
        String content=ed2.getText().toString();
        //是要修改数据
        if(ids!=0){
            cun=new Cuns(title,ids, content, times);
            cun.setColor(color);//?
            myDatabase.toUpdate(cun);
            Intent intent=new Intent(NewNote.this,MainActivity.class);
            startActivity(intent);
            NewNote.this.finish();
        }
        //新建日记
        else{
            if(title.equals("")&&content.equals("")){
                Intent intent=new Intent(NewNote.this,MainActivity.class);
                startActivity(intent);
                NewNote.this.finish();
            }
            else{
                cun=new Cuns(title,content,times);
                cun.setColor(color);
                myDatabase.toInsert(cun);
                Intent intent=new Intent(NewNote.this,MainActivity.class);
                startActivity(intent);
                NewNote.this.finish();
            }
        }
    }
    private void isSave(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd  HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String times = formatter.format(curDate);
        String title=ed1.getText().toString();
        String content=ed2.getText().toString();
        //是要修改数据
        if(ids!=0){
            cun=new Cuns(title,ids, content, times);
            cun.setColor(color);
            myDatabase.toUpdate(cun);
            Intent intent=new Intent(NewNote.this,MainActivity.class);
            startActivity(intent);
            NewNote.this.finish();
        }
        //新建日记
        else{
            cun=new Cuns(title,content,times);
            cun.setColor(color);
            myDatabase.toInsert(cun);
            Intent intent=new Intent(NewNote.this,MainActivity.class);
            startActivity(intent);
            NewNote.this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.second, menu);
        return true;
    }
    //分享功能
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,
                        "标题："+ed1.getText().toString()+"    " +
                                "内容："+ed2.getText().toString());
                startActivity(intent);
                break;

            case R.id.blue:
                color="#00BCD4";
                ll.setBackgroundColor(Color.parseColor(color));
                break;
            case R.id.green:
                color="#8BC34A";
                ll.setBackgroundColor(Color.parseColor(color));
                break;
            case R.id.yellow:
                color="#FFEB3B";
                ll.setBackgroundColor(Color.parseColor(color));
                break;
            default:
                break;
        }
        return false;
    }
}
