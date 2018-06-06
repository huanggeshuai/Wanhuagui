package com.example.huang.wanhuagui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by huang on 2018/6/6.
 */

public class MyView extends View {
    private Paint paint;
    private Path path;
    Random random;
    Canvas canvas;
    float formatX,formatY;
    private int width,heigh;
    private Clicklisten clicklisten;
    public MyView(Context context) {
        super(context);
        paint=new Paint();
        path=new Path();
        random=new Random();
        canvas=new Canvas();
    }
   public void setlisten(Clicklisten clicklisten){
       this.clicklisten=clicklisten;
   }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        heigh = this.getHeight()/2;
        width = this.getWidth()/2;


        //int c=Color.rgb(random.nextInt(256),random.nextInt(256),random.nextInt(256));
        paint.setColor(Color.rgb(random.nextInt(256),random.nextInt(256),random.nextInt(256)));
        paint.setStrokeWidth(10f);
        paint.setStyle(Paint.Style.STROKE);
        int rows=width;

        path.addCircle(width,heigh,rows,Path.Direction.CW);
        // path.addCircle(width,heigh,rows/2,Path.Direction.CW);
        // path.addCircle(width,heigh,rows/4*3,Path.Direction.CW);
        //path.addCircle();
        int edge=4; //修改内切圆的个数
        float[] ptx=new float[edge];
        float[] pty=new float[edge];
        for(int i=0;i<edge;i++){
            ptx[i]=width+rows/2*1*(float)Math.cos(i*2*Math.PI/edge-0.5*2*Math.PI); //修改内切圆的半径
            pty[i]=heigh+rows/2*1*(float)Math.sin(i*2*Math.PI/edge-0.5*2*Math.PI);
        }
        for(int i=0;i<edge;i++){
            path.addCircle(ptx[i],pty[i],rows/2*1,Path.Direction.CW);  //修改小圆的半径
        }
        //canvas.drawPath(path,paint);
        canvas.drawCircle(width,heigh,rows,paint);
        updateView(canvas);

    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x,y;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                x=event.getX();
                y=event.getY();

                if(clicklisten!=null){
                    clicklisten.clic(canvas);
                }
                if(isCircle(x,y)){
                    invalidate();
                }else {
                    Toast.makeText(getContext(),"不在园内",Toast.LENGTH_SHORT).show();
                }

                break;
            case MotionEvent.ACTION_UP:
                invalidate();
                break;

        }
        return super.onTouchEvent(event);
    }
    void updateView(Canvas canvas){
        int oldcolour=0;
        int x0=width;
        int y0=heigh;
        int r0=random.nextInt(100)+200;
        int r1= (int) (random.nextInt((int) ( r0*0.7))+r0*0.2);
        // int r0=300;
        // int r1=180;
        int x1=x0+r0-r1;
        int y1=y0;

        int r=200;
        int xx0=x1+r,yy0=y0;
        for(int i=1;i<3600*4;i++){
            int xx1,yy1;
            double angle=Math.PI/180*i;
            double Angle=angle*r1/r0;
            xx1= (int) (x0+(r0-r1)*Math.cos(-Angle)+r*Math.cos(angle));
            yy1=(int) (y0+(r0-r1)*Math.sin(-Angle)+r*Math.sin(angle));
            if(i/360!=oldcolour){
                paint.setColor(Color.rgb(random.nextInt(256),random.nextInt(256),random.nextInt(256)));
                oldcolour=i/360;
            }
            canvas.drawLine(xx0,yy0,xx1,yy1,paint);
            xx0=xx1;
            yy0=yy1;
        }

    }
    public interface Clicklisten{
        public void clic(Canvas canvas);
    }
    private   boolean isCircle(float x,float y){
        int disx=Math.abs(width-(int) x);
        int disy=Math.abs(heigh-(int) y);
         int row=(int) Math.sqrt(Math.pow(disx,2)+Math.pow(disy,2)); //计算触摸点距离圆心的坐标
        if (row>width)return false;    //如果两点距离大于半径返回false
        else return true;

    }
}

