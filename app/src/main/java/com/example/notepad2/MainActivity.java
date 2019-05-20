package com.example.notepad2;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import java.util.ArrayList;



/*
 * 这个类主要包括五个点击事件，分别为
 * ListView的长按点击事件，用来AlertDialog来判断是否删除数据。
 * ListView的点击事件，跳转到第二个界面，用来修改数据
 * 新建便签按钮的点击事件，跳转到第二界面，用来新建便签
 * menu里的退出事件，用来退出程序
 * menu里的新建事件，用来新建便签
 */

public class MainActivity extends AppCompatActivity {

    FloatingActionButton imageButton;
    ListView lv;
    SearchView mSearchView;
    LayoutInflater inflater;
    ArrayList<Cuns> array;   //存储从listview中读取的数据
    MyDataBase mdb;
    MyAdapter adapter;
    String color="#FFFFFF";
    RelativeLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
           supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
           setContentView(R.layout.activity_main);
           Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
           setSupportActionBar(toolbar);
           init();
           getBColor();  //获取背景颜色
    }

    public void getBColor(){
        SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
        color=sp.getString("color",null);
        if(color==null) color="#FFFFFF";
            ll.setBackgroundColor(Color.parseColor(color));
    }
    public void setBColor(String color){
        SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
        SharedPreferences.Editor edit=sp.edit(); //获取编辑器
        edit.putString("color",color);
        edit.commit();
    }

    public void init(){
        ll = (RelativeLayout) findViewById(R.id.ll_main);
        lv=(ListView) findViewById(R.id.lv_bwlList);
        imageButton= (DragFloatActionButton)findViewById(R.id.fb);
        inflater=getLayoutInflater();
        mdb=new MyDataBase(this);
        array=mdb.getArray();  //得到ListView的数据，从数据库里查找后解析
        //MyAdapter adapter=new MyAdapter(inflater,array);
        MyAdapter adapter=new MyAdapter(inflater,array);
        lv.setAdapter(adapter);
        /*
         * 点击listView里面的item,用来修改日记
         */
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Intent intent=new Intent(getApplicationContext(),NewNote.class);
                intent.putExtra("ids",array.get(position).getIds() );
                startActivity(intent);
                MainActivity.this.finish();
            }
        });
        /*
         * 长按后来判断是否删除数据
         */
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           final int position, long id) {
                //AlertDialog,来判断是否删除日记。
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("删除")
                        .setMessage("是否删除笔记")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mdb.toDelete(array.get(position).getIds());
                                array=mdb.getArray();
                                MyAdapter adapter=new MyAdapter(inflater,array);
                                lv.setAdapter(adapter);
                            }
                        })
                        .create().show();
                return true;
            }
        });
        /*
         * 按钮点击事件，用来新建日记
         */
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),NewNote.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //引用menu文件
        getMenuInflater().inflate(R.menu.main, menu);
        //找到searchView并配置相关参数
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //搜索图标是否显示在搜索框内
        mSearchView.setIconifiedByDefault(true);
        //设置搜索框展开时是否显示提交按钮，可不显示
        mSearchView.setSubmitButtonEnabled(true);
        //让键盘的回车键设置成搜索
        mSearchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        //搜索框是否展开，false表示展开
        mSearchView.setIconified(false);
        //获取焦点
        mSearchView.setFocusable(true);
        mSearchView.requestFocusFromTouch();
        //设置提示词
        mSearchView.setQueryHint("请输入关键字");
        inflater=getLayoutInflater();
        mdb=new MyDataBase(this);
        array=mdb.getArray();  //得到ListView的数据，从数据库里查找后解析
        adapter = new MyAdapter(inflater,array, this, new FilterListener() {
            // 回调方法获取过滤后的数据
            public void getFilterData(ArrayList<Cuns> list) {
                // 这里可以拿到过滤后数据，所以在这里可以对搜索后的数据进行操作
                //  Log.e("TAG", "接口回调成功");
               // Log.e("TAG", list.toString());
            }
        });

        lv.setAdapter(adapter);
        lv.setTextFilterEnabled(true);  //用来过滤选项
        //设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //在文字改变的时候回调，query是改变之后的文字
                //mSearchFragment.setSearchStr(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                //文字提交的时候哦回调，newText是最后提交搜索的文字
                if (!TextUtils.isEmpty(newText)){
                    adapter.getFilter().filter(newText);
                }else{
                    adapter.getFilter().filter("");
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item1:
                Intent intent=new Intent(getApplicationContext(),NewNote.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.item2:
                this.finish();
                break;
            //设置背景颜色
            case R.id.lv:
                color="#009688";
                setBColor(color);
                ll.setBackgroundColor(Color.parseColor("#009688"));
                break;
            case R.id.zi:
                color="#673AB7";
                setBColor(color);
                ll.setBackgroundColor(Color.parseColor("#673AB7"));
                break;
            case R.id.lan:
                color="#2196F3";
                setBColor(color);
                ll.setBackgroundColor(Color.parseColor("#2196F3"));
                break;
            case R.id.bai:
                color="#FFFFFF";
                setBColor(color);
                ll.setBackgroundColor(Color.parseColor("#FFFFFF"));
                break;

            //按颜色分类筛选
            case R.id.blue:
                 adapter.getFilter1().filter("#00BCD4");
                 break;
            case R.id.green:
                adapter.getFilter1().filter("#8BC34A");
                break;
            case R.id.yellow:
                adapter.getFilter1().filter("#FFEB3B");
                break;
            case R.id.all:
                adapter.getFilter1().filter("");
                break;
            default:
                break;
        }
        return true;
    }
}




