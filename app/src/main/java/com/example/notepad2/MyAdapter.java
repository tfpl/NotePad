package com.example.notepad2;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
public class MyAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private MyFilter filter = null;// 创建MyFilter对象
    private MyFilter1 filter1 = null;// 创建MyFilter对象
    private FilterListener listener = null;// 接口对象
    LayoutInflater inflater;
    ArrayList<Cuns> list;

    public MyAdapter(LayoutInflater inf, ArrayList<Cuns> list){
        this.inflater=inf;
        this.list = list;
    }
    public MyAdapter(LayoutInflater inf, ArrayList<Cuns> list, Context context, FilterListener filterListener) {
        this.list = list;
        this.context = context;
        this.listener = filterListener;
        this.inflater=inf;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    @SuppressLint("ResourceType")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        String cl;
        if(convertView==null){
            vh=new ViewHolder();
            convertView=inflater.inflate(R.layout.list_item, null);//注意导包，别导系统包
            vh.tv1=(TextView) convertView.findViewById(R.id.textView3);
            vh.tv2=(TextView) convertView.findViewById(R.id.textView4);
            vh.linearLayoutProductNameList = convertView.findViewById(R.id.linearLayoutProductNameList);
            convertView.setTag(vh);
        }
        vh=(ViewHolder) convertView.getTag();
        vh.tv1.setText(list.get(position).getTitle());
        vh.tv2.setText(list.get(position).getTimes());
        //设置背景色
        vh.linearLayoutProductNameList.setBackgroundColor(Color.parseColor(list.get(position).getColor()));

        return convertView;
    }
    class ViewHolder{
        TextView tv1,tv2;
        LinearLayout linearLayoutProductNameList;
    }

    /**
     * 自定义MyAdapter类实现了Filterable接口，重写了该方法
     * 用于实现搜索
     */
    @Override
    public Filter getFilter() {
        // 如果MyFilter对象为空，那么重写创建一个
        if (filter == null) {
            filter = new MyFilter(list);
        }
        return filter;
    }

    /**
     * 创建内部类MyFilter继承Filter类，并重写相关方法，实现数据的过滤
     */
    class MyFilter extends Filter {
        // 创建集合保存原始数据
        private ArrayList<Cuns> original = new ArrayList<Cuns>();

        public MyFilter(ArrayList<Cuns> list) {
            this.original = list;
        }
        /**
         * 该方法返回搜索过滤后的数据
         */
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // 创建FilterResults对象
            FilterResults results = new FilterResults();

            /**
             * 没有搜索内容的话就还是给results赋值原始数据的值和大小
             * 执行了搜索的话，根据搜索的规则过滤即可，最后把过滤后的数据的值和大小赋值给results
             *
             */

            if(TextUtils.isEmpty(constraint)){
                results.values = original;
                results.count = original.size();
            }else {
                // 创建集合保存过滤后的数据
                ArrayList<Cuns> mList = new ArrayList<Cuns>();
                // 遍历原始数据集合，根据搜索的规则过滤数据
                for(Cuns s: original){
                    // 这里就是过滤规则的具体实现【规则有很多，大家可以自己决定怎么实现】
                    if(s.getTitle().trim().toLowerCase().contains(constraint.toString().trim().toLowerCase())){
                        // 规则匹配的话就往集合中添加该数据
                        mList.add(s);
                    }
                }
                results.values = mList;
                results.count = mList.size();
            }
            // 返回FilterResults对象
            return results;
        }
        /**
         * 该方法用来刷新用户界面，根据过滤后的数据重新展示列表
         */
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            // 获取过滤后的数据
            list = (ArrayList<Cuns>) results.values;
            // 如果接口对象不为空，那么调用接口中的方法获取过滤后的数据，具体的实现在new这个接口的时候重写的方法里执行
            if(listener != null){
                listener.getFilterData(list);
            }
            // 刷新数据源显示
            notifyDataSetChanged();
        }
    }


    /**
     *  用于实现按颜色分类
    */
    public Filter getFilter1() {
        // 如果MyFilter对象为空，那么重写创建一个
        if (filter1 == null) {
            filter1 = new MyFilter1(list);
        }
        return filter1;
    }

    /**
     * 创建内部类MyFilter继承Filter类，并重写相关方法，实现数据的过滤
     */
    class MyFilter1 extends Filter {
        // 创建集合保存原始数据
        private ArrayList<Cuns> original = new ArrayList<Cuns>();
        public MyFilter1(ArrayList<Cuns> list) {
            this.original = list;
        }
        /**
         * 该方法返回搜索过滤后的数据
         */
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // 创建FilterResults对象
            FilterResults results = new FilterResults();

            /**
             * 没有搜索内容的话就还是给results赋值原始数据的值和大小
             * 执行了搜索的话，根据搜索的规则过滤即可，最后把过滤后的数据的值和大小赋值给results
             *
             */
            if(TextUtils.isEmpty(constraint)){
                results.values = original;
                results.count = original.size();
            }else {
                // 创建集合保存过滤后的数据
                ArrayList<Cuns> mList = new ArrayList<Cuns>();
                // 遍历原始数据集合，根据搜索的规则过滤数据
                for(Cuns s: original){
                    // 这里就是过滤规则的具体实现【规则有很多，大家可以自己决定怎么实现】
                    if(s.getColor().trim().toLowerCase().contains(constraint.toString().trim().toLowerCase())){
                        // 规则匹配的话就往集合中添加该数据
                        mList.add(s);
                    }
                }
                results.values = mList;
                results.count = mList.size();
            }
            // 返回FilterResults对象
            return results;
        }
        /**
         * 该方法用来刷新用户界面，根据过滤后的数据重新展示列表
         */
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            // 获取过滤后的数据
            list = (ArrayList<Cuns>) results.values;
            // 如果接口对象不为空，那么调用接口中的方法获取过滤后的数据，具体的实现在new这个接口的时候重写的方法里执行
            if(listener != null){
                listener.getFilterData(list);
            }
            // 刷新数据源显示
            notifyDataSetChanged();
        }
    }

}
