package com.example.kasper.beacon.SupportClasses;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import com.example.kasper.beacon.R;


/**
 * Created by kasper on 4/2/2015.
 */
public class Radar extends View {


    private final Drawable drawable;

    public Radar(Context context, AttributeSet attrs) {
        super(context, attrs);
        drawable = context.getResources().getDrawable(R.drawable.fundoinicio);
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = drawable.getIntrinsicWidth() * canvas.getHeight() / drawable.getIntrinsicHeight();
        int deltaX = (width - canvas.getWidth()) / 2;
        drawable.setBounds(-deltaX, 0, width - deltaX, canvas.getHeight());
        drawable.draw(canvas);
    }
}



