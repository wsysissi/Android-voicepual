package com.example.applicationtest;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class NewDir extends AppCompatActivity implements View.OnClickListener {

    private ImageView picture;
    private EditText editText;
    private EditText docname;
    public static final int CHOOSE_PHOTO = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dir);
        //Button takePhoto = (Button) findViewById(R.id.take_photo);
        //设置新建文件名称有关实例
        Button button1 = (Button)findViewById(R.id.sbmitname) ;
        docname = (EditText) findViewById(R.id.docname);
        button1.setOnClickListener(NewDir.this);
        //设置选择有几步有关实例
        Button button2 = (Button) findViewById(R.id.sbmit) ;
        editText = (EditText) findViewById(R.id.steps) ;//获得输入实例
        button2.setOnClickListener(NewDir.this);
        /*
        Button chooseFromAlbum = (Button) findViewById(R.id.choose_from_album);
        chooseFromAlbum.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                if (ContextCompat.checkSelfPermission(NewDir.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(NewDir.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
            }
        });*/
    }
    @Override
    public void onClick(View v){
        switch (v.getId()) {
            //获得新建文件名
            case R.id.sbmitname:
                String inputname = docname.getText().toString();
                Toast.makeText(NewDir.this,inputname,Toast.LENGTH_SHORT).show();
                break;
            //识别输入内容是否为数字，不是即提示用户重新输入（暂未实现）
            //获取输入内容，并生成用户所需的按钮数目
            case R.id.sbmit:
                String inputsteps = editText.getText().toString();
                Toast.makeText(NewDir.this , inputsteps,Toast.LENGTH_SHORT).show();
                int steps = Integer.parseInt(inputsteps);
                //获取屏幕大小，以合理设定按钮大小及位置
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                int width = dm.widthPixels;
                int height = dm.heightPixels;
                if (steps > 0) {
                    //自定义layout组件
                    RelativeLayout layout = new RelativeLayout(this);
                    //这里创建对应文件个数个按钮，每行放置2个按钮
                    Button Btn[] = new Button[steps];
                    int j = -1;
                    String ButtonName;
                    for (int i = 0; i <= steps - 1; i++) {
                        Btn[i] = new Button(this);
                        Btn[i].setId(2000 + i);
                        ButtonName = "click to add";
                        Btn[i].setText(ButtonName);
                        RelativeLayout.LayoutParams btParams = new RelativeLayout.LayoutParams((width - 50) / 2 , 200);  //设置按钮的宽度和高度
                        if (i % 2 == 0) {
                            j++;
                        }
                        btParams.leftMargin = 10 + ((width - 50) / 2 + 10) * (i % 2);   //横坐标定位
                        btParams.topMargin = 20 + 205 * j;   //纵坐标定位
                        layout.addView(Btn[i], btParams);   //将按钮放入layout组件
                    }
                    this.setContentView(layout);
                    //批量设置监听
                    for (int k = 0; k <= Btn.length - 1; k++) {
                        //这里不需要findId，因为创建的时候已经确定哪个按钮对应哪个Id
                        Btn[k].setTag(k);                //为按钮设置一个标记，来确认是按下了哪一个按钮
                        final int _k = k;
                        Btn[k].setOnClickListener(new Button.OnClickListener() {  //这里跟你原来写的被我注释了的监听是一个东西，要改的话记得在这里改。
                            @Override
                            public void onClick(View v) {
                                if (ContextCompat.checkSelfPermission(NewDir.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(NewDir.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                                } else {
                                    openAlbum();
                                }
                            }
                        });
                    }
                }
                break;
            default:
                break;
        }
    }


    private void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults)
    {
        switch (requestCode)
        {
            case 1:
                if(grantResults.length> 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    openAlbum();
                }
                else
                {
                    Toast.makeText(this, "You deniey the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case CHOOSE_PHOTO:
                if (resultCode ==RESULT_OK)
                {
                    if (Build.VERSION.SDK_INT>= 19)
                    {
                        handleImageOnkitKat(data);
                    }
                    else
                    {
                        handleImageBeforekitkat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnkitKat(Intent data)
    {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri))
        {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority()))
            {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID+"="+id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }
            else if ("com.android.providers.downloads.documents".equals(uri.getAuthority()))
            {
                Uri contentURi = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentURi,null);
            }
        }
        else if("content".equalsIgnoreCase(uri.getScheme()))
        {
            imagePath = getImagePath(uri,null);
        }
        else if ("file".equalsIgnoreCase(uri.getScheme()))
        {
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }
    private void handleImageBeforekitkat(Intent data)
    {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }
    private String getImagePath(Uri uri, String selection)
    {
        String path = null;
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if (cursor != null)
        {
            if(cursor.moveToFirst())
            {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath)
    {
        if(imagePath != null)
        {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        }
        else
        {
            Toast.makeText(this,"failed to get image",Toast.LENGTH_SHORT).show();
        }
    }

}
