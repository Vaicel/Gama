package samsung.ru.gama;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by samsung on 22.01.2016.
 */
public class GameView extends View {
    private final int timerInterval=30;
    private Sprite playerBird;
    private int viewWidth;
    private int viewHeight;
    private int points =0;
    private Sprite enemyBird;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawARGB(250,127,199,255);
        Paint p= new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.WHITE);
        p.setTextSize(55.0f);
        canvas.drawText(points + "", viewHeight - 100, 70, p);
        playerBird.draw(canvas);
        enemyBird.draw(canvas);

    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        int eventAction = event.getAction();
        if (eventAction ==MotionEvent.ACTION_DOWN){
            if(event.getY()<playerBird.getBoundingBoxRect().top){
                playerBird.setVelocityY(-100);
            }
            if(event.getX()>playerBird.getBoundingBoxRect().top){
                playerBird.setVelocityY(100);
            }
        }
        return true;
    }
    private void teleportEnemy () {
        enemyBird.setX(viewWidth + Math.random() * 500);
        enemyBird.setY(Math.random() * (viewHeight - enemyBird.getFrameHeight()));
    }
    protected void update(){
        enemyBird.update(timerInterval);
        playerBird.update(timerInterval);
        if(playerBird.getY()+playerBird.getFrameHeight()>viewHeight){
                playerBird.setY(viewHeight-playerBird.getFrameHeight());
                playerBird.setVelocityY(-playerBird.getVelocityY());
        }
        else if(playerBird.getY()<0){
            playerBird.setY(0);
            playerBird.setVelocityY(-playerBird.getVelocityY());
        }
        if (enemyBird.getX() < - enemyBird.getFrameWidth()) {
            teleportEnemy ();
            points +=10;
        }
        if (enemyBird.intersect(playerBird)) {
            teleportEnemy ();
            points -= 40;
        }
        invalidate();
    }
    class Timer extends CountDownTimer{
        public Timer(){
            super(Integer.MAX_VALUE,timerInterval);
        }
        @Override
        public void onTick(long millisUntilFinished) {
            update();
        }

        @Override
        public void onFinish() {

        }


    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth=w;
        viewHeight=h;
    }
    public GameView(Context context){
        super(context);
        Bitmap b= BitmapFactory.decodeResource(getResources(), R.drawable.player);
        int w = b.getWidth()/5;
        int h= b.getHeight()/3;
        Rect firstFrame = new Rect(0,0,w,h);
        playerBird = new Sprite(10,0,0,100,firstFrame,b);
        Timer t= new Timer();
        t.start();
        for(int i =0; i<3; i++){
            for (int j = 0; j<4;j++){
                if(i==0&&j==0){
                    continue;
                }
                if (i==2&&j==3){
                    continue;
                }
                playerBird.addFrame(new Rect(j*w,i*h,j*w+w, i*h+h));
            }
        }
        b = BitmapFactory.decodeResource(getResources(), R.drawable.enemy);
        w = b.getWidth()/5;
        h = b.getHeight()/3;
        firstFrame = new Rect(4*w, 0, 5*w, h);
        enemyBird = new Sprite(2000, 250, -300, 0, firstFrame, b);
        for (int i = 0; i < 3; i++) {
            for (int j = 4; j >= 0; j--) {
                if (i ==0 && j == 4) {
                    continue;
                }
                if (i ==2 && j == 0) {
                    continue;
                }
                enemyBird.addFrame(new Rect(j*w, i*h, j*w+w, i*w+w));
            }
        }
    }
}
