package com.example.notepad2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

//新建 MyDataBase 类用来操作数据库
public class MyDataBase {

    Context context;
    MyOpenHelper myHelper;
    SQLiteDatabase myDatabase;
    /*
     * 别的类实例化这个类的同时，创建数据库
     */
    public MyDataBase(Context con){
        this.context=con;
        myHelper=new MyOpenHelper(context);
    }
    /*
     * 得到ListView的数据，从数据库里查找后解析
     */
    public ArrayList<Cuns> getArray(){
        ArrayList<Cuns> array = new ArrayList<Cuns>();
        ArrayList<Cuns> array1 = new ArrayList<Cuns>();
        myDatabase = myHelper.getWritableDatabase();
        Cursor cursor=myDatabase.rawQuery("select ids,title,color,times from mybook" , null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            int id = cursor.getInt(cursor.getColumnIndex("ids"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String color = cursor.getString(cursor.getColumnIndex("color"));

            String times = cursor.getString(cursor.getColumnIndex("times"));
            Cuns cun = new Cuns(id,title,color,times);
           // Cuns cun = new Cuns(id, title, times);
            array.add(cun);
            cursor.moveToNext();
        }
        myDatabase.close();
        for (int i = array.size(); i >0; i--) {
            array1.add(array.get(i-1));
        }
        return array1;
    }

    /*
     * 返回可能要修改的数据
     *//*
    public Cuns getTiandCon(int id){
        myDatabase = myHelper.getWritableDatabase();
        Cursor cursor=myDatabase.rawQuery("select title,content from mybook where ids='"+id+"'" , null);
        cursor.moveToFirst();
        String title=cursor.getString(cursor.getColumnIndex("title"));
        String content=cursor.getString(cursor.getColumnIndex("content"));
        Cuns cun=new Cuns(title,content);
        myDatabase.close();
        return cun;
    }*/

    public Cuns getTiandCon(int id){
        myDatabase = myHelper.getWritableDatabase();
        Cursor cursor=myDatabase.rawQuery("select title,content,color from mybook where ids='"+id+"'" , null);
        cursor.moveToFirst();
        String title=cursor.getString(cursor.getColumnIndex("title"));
        String content=cursor.getString(cursor.getColumnIndex("content"));

        String color = cursor.getString(cursor.getColumnIndex("color"));

        //Cuns cun=new Cuns(title,content);

        Cuns cun=new Cuns();
        cun.setTitle(title);
        cun.setContent(content);
        cun.setColor(color);


        myDatabase.close();
        return cun;
    }

    /*
     * 用来修改日记
     */
    public void toUpdate(Cuns cun){
        myDatabase = myHelper.getWritableDatabase();
        myDatabase.execSQL(
                "update mybook set title='"+ cun.getTitle()+
                        "',times='"+cun.getTimes()+
                        "',content='"+cun.getContent() +
                        "',color='"+cun.getColor() +
                        "' where ids='"+ cun.getIds()+"'");
        myDatabase.close();
    }

    /*
     * 用来增加新的日记
     */
   /* public void toInsert(Cuns cun){
        myDatabase = myHelper.getWritableDatabase();
        myDatabase.execSQL("insert into mybook(title,content,times)values('"
                + cun.getTitle()+"','"
                +cun.getContent()+"','"
                +cun.getTimes()
                +"')");
        myDatabase.close();
    }*/

    public void toInsert(Cuns cun){
        myDatabase = myHelper.getWritableDatabase();
        if(cun.getColor()==null){
            myDatabase.execSQL("insert into mybook(title,content,times)values('"
                    + cun.getTitle()+"','"
                    +cun.getContent()+"','"
                    +cun.getTimes()
                    +"')");
        }else{
            myDatabase.execSQL("insert into mybook(title,content,color,times)values('"
                    + cun.getTitle()+"','"
                    +cun.getContent()+"','"
                    +cun.getColor()+"','"
                    +cun.getTimes()
                    +"')");
        }
        myDatabase.close();
    }

    /*
     * 长按点击后选择删除日记
     */
    public void toDelete(int ids){
        myDatabase  = myHelper.getWritableDatabase();
        myDatabase.execSQL("delete from mybook where ids="+ids+"");
        myDatabase.close();
    }
}

