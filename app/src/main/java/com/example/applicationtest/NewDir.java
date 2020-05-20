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
import android.support.v7.widget.LinearLayoutCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.LinearLayout;
import java.util.List;

public class NewDir extends AppCompatActivity implements View.OnClickListener {

    private ImageView picture;
    private EditText editText;
    private EditText docname;
    private ImageView imageView1;
    private String filePath;

    public static final int CHOOSE_PHOTO = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dir);
        //设置新建文件名称有关实例
        Button button1 = (Button)findViewById(R.id.sbmitname) ;
        docname = (EditText) findViewById(R.id.docname);
        button1.setOnClickListener(NewDir.this);
        //设置选择有几步有关实例
        Button button2 = (Button) findViewById(R.id.sbmit) ;
        editText = (EditText) findViewById(R.id.steps) ;//获得输入实例
        button2.setOnClickListener(NewDir.this);
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
                    RelativeLayout.LayoutParams layoutParamsroot = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.FILL_PARENT);
                    RelativeLayout layoutroot = new RelativeLayout(NewDir.this);
                    layoutroot.setLayoutParams(layoutParamsroot);
                    //这里创建对应文件个数个按钮，每行放置2个按钮
                    RelativeLayout layoutbutton = new RelativeLayout(NewDir.this);
                    Button Btn[] = new Button[steps];
                    int j = -1;
                    String ButtonName;
                    for (int i = 0; i <= steps - 1; i++) {
                        //FilePathList[i] = "hhh" ;//初始化文件数目个文件名数组位置
                        Btn[i] = new Button(this);//按钮
                        //imageMain[i] = new ImageView(this);//图片
                        Btn[i].setId(2000 + i);//按钮
                        //imageMain[i].setId(3000 + i);//图片
                        ButtonName = "click to add";
                        Btn[i].setText(ButtonName);
                        RelativeLayout.LayoutParams btParams = new RelativeLayout.LayoutParams((width - 50) / 2 , 200);  //设置按钮的宽度和高度
                        if (i % 2 == 0) {
                            j++;
                        }
                        btParams.leftMargin = 10 + ((width - 50) / 2 + 10) * (i % 2);   //横坐标定位
                        btParams.topMargin = 20 + 205 * j;   //纵坐标定位
                        layoutbutton.addView(Btn[i], btParams);   //将按钮放入layout组件
                    }
                    //设置button布局和image布局再跟布局中的位置
                    //按钮布局在大布局种得位置
                    RelativeLayout.LayoutParams layoutParamsButton = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.FILL_PARENT);
                    layoutParamsButton.topMargin = 0;
                    layoutParamsButton.leftMargin = 0;
                    layoutroot.addView(layoutbutton , layoutParamsButton);
                    //图片布局
                    int im_topmargin = 20 + 205 * j;//记录图片布局开始的纵坐标
                    RelativeLayout.LayoutParams layoutParamsImageMain = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.FILL_PARENT);
                    layoutParamsImageMain.topMargin = im_topmargin;
                    layoutParamsImageMain.leftMargin = 10;
                    imageView1 = new ImageView(NewDir.this);
                    layoutroot.addView(imageView1,layoutParamsImageMain);

                    NewDir.this.setContentView(layoutroot);
                    //批量设置监听
                    for (int k = 0; k <= Btn.length - 1; k++) {
                        //这里不需要findId，因为创建的时候已经确定哪个按钮对应哪个Id
                        Btn[k].setTag(k);                //为按钮设置一个标记，来确认是按下了哪一个按钮
                        final int _k = k;
                        Btn[k].setOnClickListener(new Button.OnClickListener() {  //这里跟你原来写的被我注释了的监听是一个东西，要改的话记得在这里改。
                            @Override
                            public void onClick(View v) {
                                Intent intent1 = new Intent(Intent.ACTION_PICK , android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intent1.setType("image/*");
                                startActivityForResult(intent1,111);
                            }
                        });
                    }
                }
                break;
            default:
                break;
        }
    }
/*
    public final void UI(){

    }*/
    private void openAlbum(){
        /*Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);*/
        Intent intent1 = new Intent(Intent.ACTION_PICK , android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent1.setType("image/*");
        startActivityForResult(intent1,111);
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
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode)
        {
            //复写onActivityResult读取选择图片的uri
            case 111:
                if (resultCode == RESULT_CANCELED){
                    Toast.makeText(getApplication(),"click to cancel choicing",Toast.LENGTH_SHORT).show();
                    return;
                }
                try{
                    Uri uri = data.getData();//获取系统返回照片的Uri
                    Log.e("TAG",uri.toString());
                    filePath = getRealPathFromURI(uri);
                    Bitmap bitmap1 = getresizePhoto(filePath);
                    //Bitmap bmp = ImageTools.decodeSampledBitmapFromResource(filePath, 100, 100);
                    Toast.makeText(NewDir.this,filePath,Toast.LENGTH_SHORT).show();
                    imageView1.setImageBitmap(bitmap1);
                    /*LinearLayout showimage = new LinearLayout(NewDir.this) ;
                    ImageView imageView1 = new ImageView(NewDir.this);
                    RelativeLayout.LayoutParams layoutParamsImage = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.FILL_PARENT);
                    layoutParamsImage.topMargin = 10;
                    layoutParamsImage.leftMargin = 10;
                    showimage.addView(imageView1,layoutParamsImage);
                    NewDir.this.setContentView(showimage);*/
                    if (bitmap1 != null){
                        Log.e("aa","bitmap1 not null");
                    }else{
                        Log.e("aa","bitmap1 is null");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
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
    //从URI获取String类型的文件路径
    public String getRealPathFromURI (Uri contentUri){
        Cursor cursor = null;
        try{
            String []proj={MediaStore.Images.Media.DATA};
            //由context.getContentResolver()获取contnetProvider再获取curso(游标)拥有表获取文件路径返回
            cursor = getContentResolver().query(contentUri,proj,null,null,null);//从系统表中查询指定Uri对应的照片
            cursor.moveToFirst();
            int column_indenx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            return cursor.getString(column_indenx);//返回照片路径
        }finally {
            if(cursor != null){
                cursor.close();
            }
        }
    }
    //根据文件路径调整图片大小防止oom并且返回bitmap
    private Bitmap getresizePhoto(String ImagePath){
        if (ImagePath != null){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true ;
            BitmapFactory.decodeFile(ImagePath,options);
            double ratio = Math.max(options.outWidth*1.0d/1024f,options.outHeight*1.0d/1024);
            options.inSampleSize = (int) Math.ceil(ratio);
            options.inJustDecodeBounds = false ;
            Bitmap bitmap = BitmapFactory.decodeFile(ImagePath,options);
            return bitmap;
        }
        return null;
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
