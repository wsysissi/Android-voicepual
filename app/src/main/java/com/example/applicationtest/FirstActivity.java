package com.example.applicationtest;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.util.DisplayMetrics;
import android.widget.RelativeLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FirstActivity extends AppCompatActivity {
    public static ArrayList getPathFilesName(String filePath){
        File path = new File(filePath);
        File[] files = path.listFiles();
        ArrayList<String> filesName = new ArrayList<>();
        for (int i=0 ; i<files.length ; i++){
            String fileName = files[i].getName();
            String file = null;
            if (fileName.endsWith(".txt")){//波波存的文件是什么类型的文件就把“.txt”改为那个类型
                file = fileName.substring(0,fileName.lastIndexOf(".")).toString();
            }
            if (file != null){

            }
        }
        return filesName;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Log.d("FirstActivity",getClass().getSimpleName());
        ActivityCollector.addActivity(this);*/
        setContentView(R.layout.first_layout);
        //获取当前程序路径
        String FilePath = getApplicationContext().getFilesDir().getAbsolutePath() ;
        //调用函数得到所有所需类型文件名FileNames
        final ArrayList <String> FileNames = getPathFilesName(FilePath) ;
        //FileNum为一共有几个文件名
        int FileNum = FileNames.size();
        //获取屏幕大小，以合理设定按钮大小及位置
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        if (FileNum > 0){ //如果本地文件夹有文件
            //自定义layout组件
            RelativeLayout layout = new RelativeLayout(this);
            //这里创建对应文件个数个按钮，每行放置2个按钮
            Button Btn[] = new Button[FileNum];
            int j = -1;
            String ButtonName ;
            for  (int i=0; i<=FileNum-1; i++) {
                Btn[i]=new Button(this);
                Btn[i].setId(2000+i);
                ButtonName = FileNames.get(i);
                Btn[i].setText("按钮"+i);
                RelativeLayout.LayoutParams btParams = new RelativeLayout.LayoutParams ((width-50)/2,40);  //设置按钮的宽度和高度
                if (i%2 == 0) {
                    j++;
                }
                btParams.leftMargin = 10+ ((width-50)/2+10)*(i%2);   //横坐标定位
                btParams.topMargin = 20 + 55*j;   //纵坐标定位
                layout.addView(Btn[i],btParams);   //将按钮放入layout组件
            }
            this.setContentView(layout);
            //批量设置监听
            for (int k = 0; k <= Btn.length-1; k++) {
                //这里不需要findId，因为创建的时候已经确定哪个按钮对应哪个Id
                Btn[k].setTag(k);                //为按钮设置一个标记，来确认是按下了哪一个按钮
                final int _k = k ;
                Btn[k].setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int i = (Integer) v.getTag();   //这里的i不能在外部定义，因为内部类的关系，内部类好多繁琐的东西，要好好研究一番
                        Intent intent = new Intent();
                        intent.setClass(FirstActivity.this, PlayDocument.class);//第一个参数是上下文即所在活动，第二个参数是要进入的活动
                        Bundle bundle = new Bundle();//把文件名信息传递给要进入的活动
                        bundle.putString("FileName", FileNames.get(_k));
                        intent.putExtras(bundle);
                        startActivity(intent);
                        //Work_01.this.finish();
                    }
                });
            }
        }

        //Button button1 = (Button) findViewById(R.id.button_1);//显示已有文件按钮
        Button button2 = (Button) findViewById(R.id.button_2);//新建文件
        /*button1.setOnClickListener(new View.OnClickListener( ) {
            public void onClick(View v)
            {
                Toast.makeText(FirstActivity.this, "You click button1", Toast.LENGTH_SHORT).show();
            }
        });*/
        button2.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setClass(FirstActivity.this,NewDir.class);//NewDir为波波创建的新建文件活动
                startActivity(intent);
            }
        });
    }
/*    @Override
    protected void onDestory(){
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }*/
}

