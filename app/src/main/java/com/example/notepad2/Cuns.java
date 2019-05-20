package com.example.notepad2;

public class Cuns {

    private String title;   //标题
    private String content; //内容
    private String times;   //时间
    private int ids;        //编号
    private String color;   //背景颜色


    public Cuns(){}

   // Cuns cun = new Cuns(id,title,color,times);
    public Cuns(int id,String title ,String color,String time){
        this.ids=id;
        this.title=title;
        this.times=time;
        this.color=color;
    }

    public Cuns(String ti,int id,String con ,String time){
        this.ids=id;
        this.title=ti;
        this.content=con;
        this.times=time;
    }

    public Cuns(String ti,String con,String time){
        this.title=ti;
        this.content=con;
        this.times=time;
    }

    public Cuns(int i,String ti,String time){
        this.ids=i;
        this.title=ti;
        this.times=time;
    }

    public Cuns(String ti,String con){
        this.title=ti;
        this.content=con;
    }

    public int getIds() {
        return ids;
    }
    public String getTitle() {
        return title;
    }
    public String getContent() {
        return content;
    }
    public String getTimes() {
        return times;
    }
    public String getColor(){return color;}

    public void setColor(String color){
        this.color=color;
    }
    public void setIds (int ids){
        this.ids=ids;
    }
    public void setTitle(String title){
        this.title=title;
    }
    public void setContent(String content){
        this.content=content;
    }
    public void setTimes(String times){
        this.times=times;
    }
}