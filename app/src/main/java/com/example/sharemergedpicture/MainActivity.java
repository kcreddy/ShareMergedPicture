package com.example.sharemergedpicture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.graphics.Canvas;

import java.util.ArrayList;



public class MainActivity extends ActionBarActivity {

    ListView list;
    boolean isSelectPicture = true;
    Integer[] imageId = {
            R.mipmap.image1,
            R.mipmap.image2,
            R.mipmap.image3,
            R.mipmap.image4
    };
    Integer[] frameId = {
            R.mipmap.frame1,
            R.mipmap.frame2
    };

    SelectedItem seletedItem ;
    ArrayList<SelectedItem> listSelectedImage = new ArrayList<SelectedItem>();
    ArrayList<SelectedItem> listSelectedFrame = new ArrayList<SelectedItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isSelectPicture = true;
        InitListView(false);

        final Button btChooseFrame = (Button)findViewById(R.id.btChooseFrame);
        btChooseFrame.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(isSelectPicture) {
                    InitListView(true);
                    btChooseFrame.setText("Merge");
                    isSelectPicture = false;
                }
                else {
                    setContentView(R.layout.merged_picture);
                    ImageView img = (ImageView)findViewById(R.id.mergedImg);

                    //merge image
                    img.setImageDrawable(new BitmapDrawable(getResources(), getMergeBitmap()));
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void InitListView(boolean isChooseFrame) {
        CustomList adapter;
        if(!isChooseFrame) {
            for(int i = 0; i<imageId.length; i++)
            {
                seletedItem = new SelectedItem(imageId[i], false);
                listSelectedImage.add(seletedItem);
            }
            adapter = new CustomList(this, imageId, listSelectedImage);
        }
        else
        {
            for(int i = 0; i<frameId.length; i++) {
                seletedItem = new SelectedItem(frameId[i], false);
                listSelectedFrame.add(seletedItem);
            }
            adapter = new CustomList(this, frameId, listSelectedFrame);
        }

        list = (ListView)findViewById(R.id.list);
        list.setAdapter(adapter);

        
    }


    private Bitmap getImageBitmap() {
        Bitmap imgBitmap = Bitmap.createBitmap(350, 350, Bitmap.Config.ARGB_8888);

        for(int i = 0; i<listSelectedImage.size(); i++)
        {
            if(listSelectedImage.get(i).selected)
                imgBitmap = BitmapFactory.decodeResource(getResources(), listSelectedImage.get(i).imageID);
        }
        return  imgBitmap;
    }

    private Bitmap getFrameBitmap()
    {
        Bitmap frmBitmap = Bitmap.createBitmap(350, 350, Bitmap.Config.ARGB_8888);
        for(int i = 0; i<listSelectedFrame.size(); i++)
        {
            if(listSelectedFrame.get(i).selected)
                frmBitmap = BitmapFactory.decodeResource(getResources(), listSelectedFrame.get(i).imageID);
        }
        return frmBitmap;
    }

    private Bitmap getMergeBitmap()
    {
        int height = 0;
        int width = 0;

        Bitmap merged = null;
        Bitmap frmBitmap = getFrameBitmap();
        Bitmap imgBitmap = getImageBitmap();


        if(frmBitmap.getWidth() > imgBitmap.getHeight()) {
            width = frmBitmap.getWidth();
            height = frmBitmap.getHeight() + imgBitmap.getHeight();
        }
        else
        {
            width = imgBitmap.getWidth();
            height = frmBitmap.getHeight() + imgBitmap.getHeight();
        }
        merged = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas cv = new Canvas(merged);

        cv.drawBitmap(imgBitmap, new Matrix(), null);
        cv.drawBitmap(frmBitmap, new Matrix(), null);
        return merged;
    }
}
