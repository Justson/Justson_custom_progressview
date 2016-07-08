package com.qypt.just.just_study_app;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by Administrator on 2016/7/4.
 */
public class BitmapUtils {



    public static Bitmap[] divisionBitmap(Bitmap bitmap , int number){

        if(bitmap==null||number<=1){
            return null;
        }
        Bitmap bitmaps[]=new Bitmap[number];

        int bigWidth=bitmap.getWidth();
        int bitHeight=bitmap.getHeight();

        int smallWidth=bigWidth/number-15;
        int smallHeight=bitHeight/number-15;

        int height=smallHeight;
        Log.i("PuzzleViewGroup","切割");
        for(int i=0;i<number;i++){


            Log.i("PuzzleViewGroup","PuzzleViewGroup i :"+i);
            Log.i("PuzzleViewGroup","bitmap:width"+bitmap.getWidth()+"   lineWidth:"+(i+1)+smallWidth);
            Log.i("PuzzleViewGroup","bitmap:height"+bitmap.getHeight()+"   lineHeight:"+smallHeight);
            bitmaps[i]= Bitmap.createBitmap(bitmap,i*smallWidth,smallHeight,smallWidth,height);

            if((i+1)*smallWidth>=bigWidth-20){
                smallHeight+=height;
                smallWidth=0;
            }
        }
        return bitmaps;
    }


}
