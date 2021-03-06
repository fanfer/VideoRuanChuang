package com.zhaoss.weixinrecorded.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.projectUtil.BeCutSubtitleSpan;
import com.zhaoss.weixinrecorded.adpter.TextAdapter;
import com.zhaoss.weixinrecorded.R;

import java.util.ArrayList;
import java.util.List;

public class TextActivity extends AppCompatActivity {
    private List<BeCutSubtitleSpan> mylist;
    private ListView listView;
    //public static Boolean[] isDelete;
    //public static Boolean isInit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        listView=(ListView) findViewById(R.id.list_Main);

        initText();//初始化字幕数据,需要到内存中去寻找
        listView.setAdapter(new TextAdapter(TextActivity.this,R.layout.video_item,mylist));

//        if(!isInit){
//            isDelete = new Boolean[mylist.size()];
//            for(int i=0; i<isDelete.length; i++){
//                isDelete[i] = false;
//            }
//            isInit = true;
//        }else {
//            while (listView.getChildCount()==0 && mylist.size()!=0) {
//                try {
//                    Thread.sleep(50);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            for(int i=0; i<isDelete.length; i++){
//                if(isDelete[i]){
//                    TextView textView = (TextView) listView.getChildAt(i);
//                    textView.setTextColor(Color.rgb(255, 0, 0));
//                }
//            }
//        }

        // 设置ListView的单击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            /**
             * @param parent
             *            ListView
             * @param view
             *            所点击的item视图，也就是TextView
             * @param position
             *            所点击item的位置
             * @param id
             *            所点击item的id
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (view instanceof View) {
                    TextView textView=view.findViewById(R.id.video_name);
                    String content = textView.getText().toString();

                    Toast.makeText(TextActivity.this, "跳转到 " + content,
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TextActivity.this,EditVideoActivity.class);
                    BeCutSubtitleSpan myBeSubtitleSpan=mylist.get(position);
                    Long startTime=myBeSubtitleSpan.startTime;
                    intent.putExtra("startTime",startTime);
                    startActivity(intent);
                }
            }
        });

        // 设置ListView的长按事件，后期可以做成单击跳转，长按修改文本内容这样的
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            /**
             * @param parent
             *            ListView
             * @param view
             *            所点击的item视图，也就是TextView
             * @param position
             *            所点击item的位置
             * @param id
             *            所点击item的id
             */
            @SuppressLint("ResourceAsColor")
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                if (view instanceof View) {
                    TextView textView=view.findViewById(R.id.video_name);
                    String content = textView.getText().toString();

                    BeCutSubtitleSpan myBeSubtitleSpan=mylist.get(position);

                    if( myBeSubtitleSpan.isChecked==false){
                        Toast.makeText(TextActivity.this, "选中了 " + content,
                                Toast.LENGTH_SHORT).show();
                        EditVideoActivity.addBeCutVideoSpan(myBeSubtitleSpan);
                        textView.setTextColor(Color.rgb(255, 0, 0));
                        myBeSubtitleSpan.isChecked=true;
                        //isDelete[position] = true;
                    }else {
                        EditVideoActivity.removeBeCutVideoSpan(myBeSubtitleSpan);
                        Toast.makeText(TextActivity.this, "取消选中选中了 " + content,
                                Toast.LENGTH_SHORT).show();
                        textView.setTextColor(Color.rgb(0, 0, 0));
                        myBeSubtitleSpan.isChecked=false;
                        //isDelete[position] = false;
                    }
                }
                // 返回true，表示将单击事件进行拦截
                return true;
            }
        });

    }

    //TODO:将内存的数据读入到mylist中
    private void initText(){
        //EditVideoActivity editVideoActivity=new EditVideoActivity();
        //editVideoActivity.refreshAllClips();
        mylist=EditVideoActivity.allClips;
    }
}