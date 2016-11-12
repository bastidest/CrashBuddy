package hackatum.de.checkcrash.design;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.Button;

import hackatum.de.checkcrash.R;

public class BreadcrumbView extends Button {

    private int width;
    private int height;

    public BreadcrumbView(Context context) {
        super(context);
    }

    public BreadcrumbView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BreadcrumbView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = MeasureSpec.getSize(getMeasuredWidth()) + 80;
        height = MeasureSpec.getSize(getMeasuredHeight());

        setMeasuredDimension(width, height);
        setPadding(50, 10, 50, 10);
    }

    @Override
    protected void onDraw(Canvas canvas) {

    }

    @Override
    public void draw(Canvas canvas) {

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        paint.setAntiAlias(true);


        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(0, 0);
        path.lineTo(width - 40, 0);
        path.lineTo(width, height / 2);
        path.lineTo(width - 40, height);
        path.lineTo(0, height);
        path.lineTo(40, height / 2);
        path.close();

        canvas.drawPath(path, paint);

        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorBreadcrumbs));
        textPaint.setTextSize(getTextSize());
        textPaint.setTextAlign(Paint.Align.CENTER);

        int yPos = (int) ((height / 2) - ((textPaint.descent() + textPaint.ascent()) / 2));

        canvas.drawText(getText().toString().toUpperCase(), width / 2, yPos, textPaint);
    }

    @Override
    public void setShadowLayer(float radius, float dx, float dy, int color) {

    }
}
