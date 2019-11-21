package www.linwg.org.lib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import java.util.HashMap;
import java.util.Map;

import www.linwg.org.lcardview.R;

public class LCardView extends FrameLayout {
    public static final int ADSORPTION = 0;
    public static final int LINEAR = 1;
    private int shadowFluidShape = ADSORPTION;
    private final int defaultShadowSize = 12;
    private final int defaultShadowStartAlpha = 10;
    private int elevation = 0;
    private int shadowSize = defaultShadowSize;
    private int leftOffset, topOffset, rightOffset, bottomOffset;
    private int effectLeftOffset, effectTopOffset, effectRightOffset, effectBottomOffset;
    private int defaultShadowColor = Color.parseColor("#05000000");
    private int defaultCardBackgroundColor = 99999999;
    private int[] colors = new int[]{defaultShadowColor, defaultShadowColor, Color.parseColor("#00000000"), Color.parseColor("#00000000")};
    private int shadowColor = defaultShadowColor;
    private int cardBackgroundColor = 99999999;
    private int cornerRadius = 0;
    private boolean elevationAffectShadowColor = false;
    private boolean elevationAffectShadowSize = false;
    private int leftTopCornerRadius = 0;
    private int rightTopCornerRadius = 0;
    private int rightBottomCornerRadius = 0;
    private int leftBottomCornerRadius = 0;
    private int viewWidth = -3;
    private int viewHeight = -3;
    private Path mPath = new Path();
    private Path highVerPath = new Path();
    private Path mContentPath = new Path();
    private Path mShadowPath = new Path();
    private Paint paint = new Paint();
    private Paint bgColorPaint = new Paint();
    private Paint bgPaint = new Paint();
    private Paint pathPaint = new Paint();
    /**
     * Corner shadows will be draw on 4 corner.
     */
    private RadialGradient ltrg, rtrg, rbrg, lbrg;
    /**
     * Edge shadows will be draw on 4 edge.
     */
    private LinearGradient t, r, b, l;
    private int shadowAlpha = defaultShadowStartAlpha;
    float percent = 0.33f;
    /**
     * Region of 8 shadows.
     */
    private RectF ltRectF = new RectF();
    private RectF tRectF = new RectF();
    private RectF rtRectF = new RectF();
    private RectF rRectF = new RectF();
    private RectF rbRectF = new RectF();
    private RectF bRectF = new RectF();
    private RectF lbRectF = new RectF();
    private RectF lRectF = new RectF();

    public LCardView(@NonNull Context context) {
        this(context, null);
    }

    public LCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LCardView, defStyleAttr, 0);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = typedArray.getIndex(i);
            if (index == R.styleable.LCardView_leftShadowWidth) {
                //not work after version 1.5.0
            } else if (index == R.styleable.LCardView_topShadowHeight) {
                //not work after version 1.5.0
            } else if (index == R.styleable.LCardView_rightShadowWidth) {
                //not work after version 1.5.0
            } else if (index == R.styleable.LCardView_bottomShadowHeight) {
                //not work after version 1.5.0
            } else if (index == R.styleable.LCardView_shadowSize) {
                shadowSize = typedArray.getDimensionPixelSize(index, -1);
            } else if (index == R.styleable.LCardView_shadowColor) {
                shadowColor = typedArray.getColor(index, defaultShadowColor);
            } else if (index == R.styleable.LCardView_shadowStartAlpha) {
                shadowAlpha = typedArray.getInt(index, defaultShadowStartAlpha);
            } else if (index == R.styleable.LCardView_shadowFluidShape) {
                shadowFluidShape = typedArray.getInt(index, ADSORPTION);
            } else if (index == R.styleable.LCardView_cardBackgroundColor) {
                cardBackgroundColor = typedArray.getColor(index, defaultCardBackgroundColor);
            } else if (index == R.styleable.LCardView_cornerRadius) {
                cornerRadius = typedArray.getDimensionPixelSize(index, 0);
            } else if (index == R.styleable.LCardView_leftTopCornerRadius) {
                leftTopCornerRadius = typedArray.getDimensionPixelSize(index, 0);
            } else if (index == R.styleable.LCardView_leftBottomCornerRadius) {
                leftBottomCornerRadius = typedArray.getDimensionPixelSize(index, 0);
            } else if (index == R.styleable.LCardView_rightTopCornerRadius) {
                rightTopCornerRadius = typedArray.getDimensionPixelSize(index, 0);
            } else if (index == R.styleable.LCardView_rightBottomCornerRadius) {
                rightBottomCornerRadius = typedArray.getDimensionPixelSize(index, 0);
            } else if (index == R.styleable.LCardView_elevation) {
                elevation = typedArray.getDimensionPixelSize(index, 0);
            } else if (index == R.styleable.LCardView_elevationAffectShadowColor) {
                elevationAffectShadowColor = typedArray.getBoolean(index, false);
            } else if (index == R.styleable.LCardView_elevationAffectShadowSize) {
                elevationAffectShadowSize = typedArray.getBoolean(index, false);
            } else if (index == R.styleable.LCardView_xOffset) {
                //not work after version 1.5.0
            } else if (index == R.styleable.LCardView_yOffset) {
                //not work after version 1.5.0
            } else if (index == R.styleable.LCardView_leftOffset) {
                leftOffset = typedArray.getDimensionPixelSize(index, 0);
            } else if (index == R.styleable.LCardView_rightOffset) {
                rightOffset = typedArray.getDimensionPixelSize(index, 0);
            } else if (index == R.styleable.LCardView_topOffset) {
                topOffset = typedArray.getDimensionPixelSize(index, 0);
            } else if (index == R.styleable.LCardView_bottomOffset) {
                bottomOffset = typedArray.getDimensionPixelSize(index, 0);
            }
        }

        typedArray.recycle();

        paint.setAntiAlias(true);
        paint.setDither(true);
        bgPaint.setAntiAlias(true);
        bgPaint.setDither(true);
        bgColorPaint.setAntiAlias(true);
        bgColorPaint.setDither(true);

        pathPaint.setDither(true);
        pathPaint.setAntiAlias(true);
        pathPaint.setColor(Color.WHITE);

        initColors(shadowColor);

        if (elevationAffectShadowSize) {
            shadowSize = elevation + 12;
        }

        if (cornerRadius != 0) {
            leftTopCornerRadius = leftBottomCornerRadius = rightTopCornerRadius = rightBottomCornerRadius = cornerRadius;
        }
        initOffset();

        int leftPadding = shadowSize + leftOffset;
        leftPadding = Math.max(leftPadding, 0);

        int topPadding = shadowSize + topOffset;
        topPadding = Math.max(topPadding, 0);

        int rightPadding = shadowSize + rightOffset;
        rightPadding = Math.max(rightPadding, 0);

        int bottomPadding = shadowSize + bottomOffset;
        bottomPadding = Math.max(bottomPadding, 0);
        super.setPadding(leftPadding, topPadding, rightPadding, bottomPadding);
    }

    private void initOffset() {
        int maxOffset = shadowSize / 2;
        leftOffset = Math.min(maxOffset, leftOffset);
        topOffset = Math.min(maxOffset, topOffset);
        rightOffset = Math.min(maxOffset, rightOffset);
        bottomOffset = Math.min(maxOffset, bottomOffset);
        effectLeftOffset = leftOffset > 0 ? 0 : leftOffset;
        effectTopOffset = topOffset > 0 ? 0 : topOffset;
        effectRightOffset = rightOffset > 0 ? 0 : rightOffset;
        effectBottomOffset = bottomOffset > 0 ? 0 : bottomOffset;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode;
        switch (widthMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                heightMode = (int) Math.ceil((double) getMinWidth());
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(Math.max(heightMode, MeasureSpec.getSize(widthMeasureSpec)), widthMode);
            case 0:
            default:
                heightMode = MeasureSpec.getMode(heightMeasureSpec);
                switch (heightMode) {
                    case MeasureSpec.AT_MOST:
                    case MeasureSpec.EXACTLY:
                        int minHeight = (int) Math.ceil((double) getMinHeight());
                        heightMeasureSpec = MeasureSpec.makeMeasureSpec(Math.max(minHeight, MeasureSpec.getSize(heightMeasureSpec)), heightMode);
                    case 0:
                    default:
                        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                }
        }
        if (viewWidth == -3) {
            viewWidth = getMeasuredWidth();
            viewHeight = getMeasuredHeight();
            createDrawables();
        }
    }

    private int getMinHeight() {
        return Math.max(leftTopCornerRadius, rightTopCornerRadius) + Math.max(leftBottomCornerRadius, rightBottomCornerRadius);
    }

    private int getMinWidth() {
        return Math.max(leftTopCornerRadius, leftBottomCornerRadius) + Math.max(rightTopCornerRadius, rightBottomCornerRadius);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            viewWidth = w;
            viewHeight = h;
            createDrawables();
        }
    }

    /**
     * Setting corner radius and shadow size will resize edge of offsets.
     */
    private void judgeOffset() {
        int minLeftOffset = -(viewWidth / 2 - shadowSize - Math.max(leftTopCornerRadius, leftBottomCornerRadius));
        if (leftOffset < minLeftOffset) {
            leftOffset = minLeftOffset;
        }
        int minTopOffset = -(viewHeight / 2 - shadowSize - Math.max(leftTopCornerRadius, rightTopCornerRadius));
        if (topOffset < minTopOffset) {
            topOffset = minTopOffset;
        }
        int minRightOffset = -(viewWidth / 2 - shadowSize - Math.max(rightTopCornerRadius, rightBottomCornerRadius));
        if (rightOffset < minRightOffset) {
            rightOffset = minRightOffset;
        }
        int minBottomOffset = -(viewHeight / 2 - shadowSize - Math.max(leftBottomCornerRadius, rightBottomCornerRadius));
        if (bottomOffset < minBottomOffset) {
            bottomOffset = minBottomOffset;
        }
    }

    private void createDrawables() {
        if (viewHeight == -3 || viewWidth == -3) {
            // view is not measure ann not ready to draw
            return;
        }
        if (shadowSize > viewHeight / 4) {
            shadowSize = viewHeight / 4;
        }
        judgeOffset();

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        int ltRadius = shadowSize + leftTopCornerRadius;
        if (ltRadius == 0) {
            ltRectF.setEmpty();
            ltrg = null;
        } else {
            float start = leftTopCornerRadius / (float) ltRadius;
            float center = (1 - start) * percent + start;
            float center2 = (1 - center) / 2 + center;

            float centerX = paddingLeft > 0 ? ltRadius : ltRadius - effectLeftOffset;
            float centerY = paddingTop > 0 ? ltRadius : ltRadius - effectTopOffset;
            ltRectF.set(centerX - ltRadius, centerY - ltRadius, centerX + ltRadius, centerY + ltRadius);
            ltrg = new RadialGradient(centerX, centerY, ltRadius, colors, new float[]{start, center, center2, 1}, Shader.TileMode.CLAMP);
        }

        int rtRadius = shadowSize + rightTopCornerRadius;
        if (rtRadius == 0) {
            rtRectF.setEmpty();
            rtrg = null;
        } else {
            float start = rightTopCornerRadius / (float) rtRadius;
            float center = (1 - start) * percent + start;
            float center2 = (1 - center) / 2 + center;

            float centerX = paddingRight > 0 ? viewWidth - rtRadius : viewWidth - rtRadius + effectRightOffset;
            float centerY = paddingTop > 0 ? rtRadius : rtRadius - effectTopOffset;
            rtRectF.set(centerX - rtRadius, centerY - rtRadius, centerX + rtRadius, centerY + rtRadius);
            rtrg = new RadialGradient(centerX, centerY, rtRadius, colors, new float[]{start, center, center2, 1}, Shader.TileMode.CLAMP);
        }

        int rbRadius = shadowSize + rightBottomCornerRadius;
        if (rbRadius == 0) {
            rbrg = null;
            rbRectF.setEmpty();
        } else {
            float start = rightBottomCornerRadius / (float) rbRadius;
            float center = (1 - start) * percent + start;
            float center2 = (1 - center) / 2 + center;

            float centerX = paddingRight > 0 ? viewWidth - rbRadius : viewWidth - rbRadius + effectRightOffset;
            float centerY = paddingBottom > 0 ? viewHeight - rbRadius : viewHeight - rbRadius + effectBottomOffset;
            rbRectF.set(centerX - rbRadius, centerY - rbRadius, centerX + rbRadius, centerY + rbRadius);
            rbrg = new RadialGradient(centerX, centerY, rbRadius, colors, new float[]{start, center, center2, 1}, Shader.TileMode.CLAMP);
        }

        int lbRadius = shadowSize + leftBottomCornerRadius;
        if (lbRadius == 0) {
            lbrg = null;
            lbRectF.setEmpty();
        } else {
            float start = leftBottomCornerRadius / (float) lbRadius;
            float center = (1 - start) * percent + start;
            float center2 = (1 - center) / 2 + center;

            float centerX = paddingLeft > 0 ? lbRadius : lbRadius - effectLeftOffset;
            float centerY = paddingBottom > 0 ? viewHeight - lbRadius : viewHeight - lbRadius + effectBottomOffset;
            lbRectF.set(centerX - lbRadius, centerY - lbRadius, centerX + lbRadius, centerY + lbRadius);
            lbrg = new RadialGradient(centerX, centerY, lbRadius, colors, new float[]{start, center, center2, 1}, Shader.TileMode.CLAMP);
        }

        float left = paddingLeft > 0 ? ltRadius : ltRadius - effectLeftOffset;
        float right = paddingRight > 0 ? viewWidth - rtRadius : viewWidth - rtRadius + effectRightOffset;
        float top = paddingTop > 0 ? 0 : -effectTopOffset;
        float bottom = top + shadowSize;
        tRectF.set(left, top, right, bottom);
        t = new LinearGradient(tRectF.left, tRectF.bottom, tRectF.left, tRectF.top, colors, new float[]{0, percent, (1 - percent) / 2 + percent, 1}, Shader.TileMode.CLAMP);

        right = paddingRight > 0 ? viewWidth : viewWidth + effectRightOffset;
        left = right - shadowSize;
        top = paddingTop > 0 ? rtRadius : rtRadius - effectTopOffset;
        bottom = paddingBottom > 0 ? viewHeight - rbRadius : viewHeight - rbRadius + effectBottomOffset;
        rRectF.set(left, top, right, bottom);
        r = new LinearGradient(rRectF.left, rRectF.top, rRectF.right, rRectF.top, colors, new float[]{0, percent, (1 - percent) / 2 + percent, 1}, Shader.TileMode.CLAMP);

        left = paddingLeft > 0 ? lbRadius : lbRadius - effectLeftOffset;
        right = paddingRight > 0 ? viewWidth - rbRadius : viewWidth - rbRadius + effectRightOffset;
        top = paddingBottom > 0 ? viewHeight - shadowSize : viewHeight - shadowSize + effectBottomOffset;
        bottom = top + shadowSize;
        bRectF.set(left, top, right, bottom);
        b = new LinearGradient(bRectF.left, bRectF.top, bRectF.left, bRectF.bottom, colors, new float[]{0, percent, (1 - percent) / 2 + percent, 1}, Shader.TileMode.CLAMP);

        right = paddingLeft > 0 ? shadowSize : effectLeftOffset + shadowSize;
        left = right - shadowSize;
        top = paddingTop > 0 ? ltRadius : ltRadius - effectTopOffset;
        bottom = paddingBottom > 0 ? viewHeight - lbRadius : viewHeight - lbRadius + effectBottomOffset;
        lRectF.set(left, top, right, bottom);
        l = new LinearGradient(lRectF.right, lRectF.top, lRectF.left, lRectF.top, colors, new float[]{0, percent, (1 - percent) / 2 + percent, 1}, Shader.TileMode.CLAMP);
    }

    private void measureContentPath() {
        mShadowPath.reset();
        mShadowPath.moveTo(lRectF.right, lRectF.top);
        mShadowPath.arcTo(new RectF(lRectF.right, tRectF.bottom, lRectF.right + leftTopCornerRadius * 2, tRectF.bottom + leftTopCornerRadius * 2), 180, 90);
        mShadowPath.lineTo(tRectF.right, tRectF.bottom);
        mShadowPath.arcTo(new RectF(rRectF.left - rightTopCornerRadius * 2, rRectF.top - rightTopCornerRadius, rRectF.left, rRectF.top + rightTopCornerRadius), 270, 90);
        mShadowPath.lineTo(rRectF.left, rRectF.bottom);
        mShadowPath.arcTo(new RectF(rRectF.left - rightBottomCornerRadius * 2, rRectF.bottom - rightBottomCornerRadius, rRectF.left, rRectF.bottom + rightBottomCornerRadius), 0, 90);
        mShadowPath.lineTo(bRectF.left, bRectF.top);
        mShadowPath.arcTo(new RectF(lRectF.right, lRectF.bottom - leftBottomCornerRadius, lRectF.right + leftBottomCornerRadius * 2, lRectF.bottom + leftBottomCornerRadius), 90, 90);
        mShadowPath.close();

        mContentPath.reset();
        float startX = getPaddingLeft();
        float startY = getPaddingTop();
        float stopX = getPaddingRight();
        float stopY = getPaddingBottom();
        mContentPath.moveTo(startX, startY + leftTopCornerRadius);
        mContentPath.arcTo(new RectF(startX, startY, leftTopCornerRadius * 2 + startX, startY + leftTopCornerRadius * 2), 180, 90);
        mContentPath.lineTo(viewWidth - stopX - rightTopCornerRadius, startY);
        mContentPath.arcTo(new RectF(viewWidth - stopX - rightTopCornerRadius * 2, startY, viewWidth - stopX, startY + rightTopCornerRadius * 2), 270, 90);
        mContentPath.lineTo(viewWidth - stopX, viewHeight - stopY - rightBottomCornerRadius);
        mContentPath.arcTo(new RectF(viewWidth - stopX - rightBottomCornerRadius * 2, viewHeight - stopY - rightBottomCornerRadius * 2, viewWidth - stopX, viewHeight - stopY), 0, 90);
        mContentPath.lineTo(startX + leftBottomCornerRadius, viewHeight - stopY);
        mContentPath.arcTo(new RectF(startX, viewHeight - stopY - leftBottomCornerRadius * 2, startX + leftBottomCornerRadius * 2, viewHeight - stopY), 90, 90);
        mContentPath.close();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        int saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
            pathPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
            super.dispatchDraw(canvas);
            canvas.drawPath(mContentPath, pathPaint);
        } else {
            pathPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            super.dispatchDraw(canvas);
            highVerPath.reset();
            highVerPath.addRect(0, 0, getWidth(), getHeight(), Path.Direction.CW);
            highVerPath.op(mContentPath, Path.Op.DIFFERENCE);
            canvas.drawPath(highVerPath, pathPaint);
        }
//        canvas.restoreToCount(saveCount);
        canvas.restore();
        pathPaint.setXfermode(null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        measureContentPath();
        canvas.save();
        canvas.clipPath(mShadowPath);
        canvas.clipPath(mContentPath, Region.Op.DIFFERENCE);
        bgPaint.setColor(shadowColor);
        canvas.drawPath(mShadowPath, bgPaint);
        canvas.restore();
        if (cardBackgroundColor != defaultCardBackgroundColor) {
            bgColorPaint.setColor(cardBackgroundColor);
            canvas.drawPath(mContentPath, bgColorPaint);
        }

        canvas.save();
        canvas.clipPath(mContentPath, Region.Op.DIFFERENCE);

        //左上圆角
        drawCornerShadow(canvas, mPath, ltrg, ltRectF.left, ltRectF.top, ltRectF.centerX(), ltRectF.centerY(), ltRectF.centerX(), ltRectF.centerY(), leftTopCornerRadius, shadowSize + leftTopCornerRadius, paint);

        //顶部阴影
        drawLineShadow(canvas, t, tRectF, paint);

        //右上圆角
        drawCornerShadow(canvas, mPath, rtrg, rtRectF.centerX(), rtRectF.top, rtRectF.right, rtRectF.centerY(), rtRectF.centerX(), rtRectF.centerY(), rightTopCornerRadius, shadowSize + rightTopCornerRadius, paint);

        //右侧阴影
        drawLineShadow(canvas, r, rRectF, paint);

        //右下圆角阴影
        drawCornerShadow(canvas, mPath, rbrg, rbRectF.centerX(), rbRectF.centerY(), rbRectF.right, rbRectF.bottom, rbRectF.centerX(), rbRectF.centerY(), rightBottomCornerRadius, shadowSize + rightBottomCornerRadius, paint);

        //底部阴影
        drawLineShadow(canvas, b, bRectF, paint);

        //左下圆角阴影
        drawCornerShadow(canvas, mPath, lbrg, lbRectF.left, lbRectF.centerY(), lbRectF.centerX(), lbRectF.bottom, lbRectF.centerX(), lbRectF.centerY(), leftBottomCornerRadius, shadowSize + leftBottomCornerRadius, paint);

        //左侧阴影
        drawLineShadow(canvas, l, lRectF, paint);

        canvas.restore();
    }

    private void drawCornerShadow(Canvas canvas, Path path, Shader shader, float left, float top, float right, float bottom, float centerX, float centerY, float clipRadius, float radius, Paint paint) {
        if (radius == 0) {
            return;
        }
        canvas.save();
        canvas.clipRect(left, top, right, bottom);
        path.reset();
        path.addCircle(centerX, centerY, clipRadius, Path.Direction.CCW);
        canvas.clipPath(path, Region.Op.DIFFERENCE);
        paint.setShader(shader);
        canvas.drawCircle(centerX, centerY, radius, paint);
        canvas.restore();
    }

    private void drawLineShadow(Canvas canvas, Shader shader, RectF rectF, Paint paint) {
        canvas.save();
        paint.setShader(shader);
        canvas.drawRect(rectF.left, rectF.top, rectF.right, rectF.bottom, paint);
        canvas.restore();
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        //NO OP
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        //NO OP
    }

    public void setLeftTopCornerRadius(int leftTopCornerRadius) {
        if (this.leftTopCornerRadius == leftTopCornerRadius) {
            return;
        }
        this.leftTopCornerRadius = leftTopCornerRadius;
        this.leftTopCornerRadius = Math.min(this.leftTopCornerRadius, (viewWidth - getPaddingLeft() - getPaddingRight()) / 2);
        this.leftTopCornerRadius = Math.min(this.leftTopCornerRadius, (viewHeight - getPaddingTop() - getPaddingBottom()) / 2);
        createDrawables();
        invalidate();
    }


    public void setRightTopCornerRadius(int rightTopCornerRadius) {
        if (this.rightTopCornerRadius == rightTopCornerRadius) {
            return;
        }
        this.rightTopCornerRadius = rightTopCornerRadius;
        this.rightTopCornerRadius = Math.min(this.rightTopCornerRadius, (viewWidth - getPaddingLeft() - getPaddingRight()) / 2);
        this.rightTopCornerRadius = Math.min(this.rightTopCornerRadius, (viewHeight - getPaddingTop() - getPaddingBottom()) / 2);
        createDrawables();
        invalidate();
    }

    public void setRightBottomCornerRadius(int rightBottomCornerRadius) {
        if (this.rightBottomCornerRadius == rightBottomCornerRadius) {
            return;
        }
        this.rightBottomCornerRadius = rightBottomCornerRadius;
        this.rightBottomCornerRadius = Math.min(this.rightBottomCornerRadius, (viewWidth - getPaddingLeft() - getPaddingRight()) / 2);
        this.rightBottomCornerRadius = Math.min(this.rightBottomCornerRadius, (viewHeight - getPaddingTop() - getPaddingBottom()) / 2);
        createDrawables();
        invalidate();
    }

    public void setLeftBottomCornerRadius(int leftBottomCornerRadius) {
        if (this.leftBottomCornerRadius == leftBottomCornerRadius) {
            return;
        }
        this.leftBottomCornerRadius = leftBottomCornerRadius;
        this.leftBottomCornerRadius = Math.min(this.leftBottomCornerRadius, (viewWidth - getPaddingLeft() - getPaddingRight()) / 2);
        this.leftBottomCornerRadius = Math.min(this.leftBottomCornerRadius, (viewHeight - getPaddingTop() - getPaddingBottom()) / 2);
        createDrawables();
        invalidate();
    }

    @Deprecated
    public void setLeftShadowSize(int leftShadowSize) {
        //not work after version 1.5.0        
    }

    @Deprecated
    public void setRightShadowSize(int rightShadowSize) {
        //not work after version 1.5.0  
    }

    @Deprecated
    public void setTopShadowSize(int topShadowSize) {
        //not work after version 1.5.0  
    }

    @Deprecated
    public void setBottomShadowSize(int bottomShadowSize) {
        //not work after version 1.5.0  
    }

    public void setShadowColor(@ColorInt int color) {
        if (isSameRGB(color)) {
            return;
        }
        initColors(color);
        createDrawables();
        invalidate();
    }

    private boolean isSameRGB(int color) {
        if (shadowColor == color) {
            return true;
        }
        return Color.red(color) == Color.red(shadowColor) && Color.green(color) == Color.green(shadowColor) && Color.blue(color) == Color.blue(shadowColor);
    }

    private void initColors(@ColorInt int color) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        shadowColor = Color.argb(elevationAffectShadowColor ? (elevation + 10) : shadowAlpha, red, green, blue);
        if (shadowFluidShape == ADSORPTION) {
            colors[0] = shadowColor;
            colors[1] = Color.argb(Color.alpha(shadowColor) / 4, red, green, blue);
            colors[2] = Color.argb(Color.alpha(shadowColor) / 8, red, green, blue);
            colors[3] = Color.argb(0, red, green, blue);
        } else {
            colors[0] = shadowColor;
            colors[1] = Color.argb((int) (Color.alpha(shadowColor) * 0.67), red, green, blue);
            colors[2] = Color.argb((int) (Color.alpha(shadowColor) * 0.33), red, green, blue);
            colors[3] = Color.argb(0, red, green, blue);
        }
    }

    //0~ 255
    public void setShadowAlpha(int alpha) {
        if (this.shadowAlpha == alpha) {
            return;
        }
        this.shadowAlpha = alpha;
        initColors(shadowColor);
        createDrawables();
        invalidate();
    }

    public void setElevationAffectShadowColor(boolean elevationAffectShadowColor) {
        if (this.elevationAffectShadowColor != elevationAffectShadowColor) {
            this.elevationAffectShadowColor = elevationAffectShadowColor;
            initColors(shadowColor);
            createDrawables();
            invalidate();
        }
    }

    public void setElevationAffectShadowSize(boolean elevationAffectShadowSize) {
        if (this.elevationAffectShadowSize != elevationAffectShadowSize) {
            this.elevationAffectShadowSize = elevationAffectShadowSize;
            if (elevationAffectShadowSize) {
                int shadowSize = elevation + 12;
                if (this.shadowSize != shadowSize) {
                    this.shadowSize = shadowSize;
                    onShadowSizeChange();
                }
            }
            createDrawables();
            invalidate();
        }
    }

    public void setElevation(int elevation) {
        if (this.elevation == elevation) {
            return;
        }
        this.elevation = elevation;
        if (elevationAffectShadowColor) {
            initColors(shadowColor);
        }
        if (elevationAffectShadowSize) {
            int shadowSize = elevation + 12;
            if (this.shadowSize != shadowSize) {
                this.shadowSize = shadowSize;
                onShadowSizeChange();
            }
        }
        createDrawables();
        invalidate();
    }

    public void setCornerRadius(int radius) {
        if (this.cornerRadius == radius) {
            return;
        }
        this.cornerRadius = radius;
        leftTopCornerRadius = leftBottomCornerRadius = rightTopCornerRadius = rightBottomCornerRadius = cornerRadius;
        createDrawables();
        invalidate();
    }

    public void setShadowSize(int shadowSize) {
        if (elevationAffectShadowSize) {
            //This field make shadow size change with elevation.
            return;
        }
        if (this.shadowSize == shadowSize) {
            return;
        }
        this.shadowSize = shadowSize;
        onShadowSizeChange();

        createDrawables();
        invalidate();
    }

    private void onShadowSizeChange() {
        initOffset();

        int leftPadding = shadowSize + leftOffset;
        leftPadding = Math.max(leftPadding, 0);

        int topPadding = shadowSize + topOffset;
        topPadding = Math.max(topPadding, 0);

        int rightPadding = shadowSize + rightOffset;
        rightPadding = Math.max(rightPadding, 0);

        int bottomPadding = shadowSize + bottomOffset;
        bottomPadding = Math.max(bottomPadding, 0);
        super.setPadding(leftPadding, topPadding, rightPadding, bottomPadding);
    }

    public int getCardElevation() {
        return elevation;
    }

    public int getLeftShadowSize() {
        return shadowSize;
    }

    public int getTopShadowSize() {
        return shadowSize;
    }

    public int getRightShadowSize() {
        return shadowSize;
    }

    public int getBottomShadowSize() {
        return shadowSize;
    }

    public int getShadowColor() {
        return shadowColor;
    }

    public int getCardBackgroundColor() {
        return cardBackgroundColor;
    }

    public int getCornerRadius() {
        return cornerRadius;
    }

    public boolean isElevationAffectShadowColor() {
        return elevationAffectShadowColor;
    }

    public boolean isElevationAffectShadowSize() {
        return elevationAffectShadowSize;
    }

    public int getLeftTopCornerRadius() {
        return leftTopCornerRadius;
    }

    public int getRightTopCornerRadius() {
        return rightTopCornerRadius;
    }

    public int getRightBottomCornerRadius() {
        return rightBottomCornerRadius;
    }

    public int getLeftBottomCornerRadius() {
        return leftBottomCornerRadius;
    }

    public int getShadowAlpha() {
        return shadowAlpha;
    }

    public void setCardBackgroundColor(int cardBackgroundColor) {
        if (this.cardBackgroundColor == cardBackgroundColor) {
            return;
        }
        this.cardBackgroundColor = cardBackgroundColor;
        invalidate();
    }

    public void setShadowOffsetCenter(int offset) {
        int maxOffset = shadowSize / 2;
        int leftOffset = Math.min(maxOffset, offset);
        int rightOffset = Math.min(maxOffset, offset);
        int topOffset = Math.min(maxOffset, offset);
        int bottomOffset = Math.min(maxOffset, offset);
        if (this.leftOffset == leftOffset && this.rightOffset == rightOffset && this.topOffset == topOffset && this.bottomOffset == bottomOffset) {
            return;
        }
        this.leftOffset = leftOffset;
        this.rightOffset = rightOffset;
        this.topOffset = topOffset;
        this.bottomOffset = bottomOffset;
        onShadowSizeChange();
        createDrawables();
        invalidate();
    }

    @Deprecated
    public void setShadowOffset(int offset) {
        //No work fine.
    }


    public int getLeftOffset() {
        return leftOffset;
    }

    public void setLeftOffset(int leftOffset) {
        int maxOffset = shadowSize / 2;
        leftOffset = Math.min(maxOffset, leftOffset);
        if (this.leftOffset == leftOffset) {
            return;
        }
        this.leftOffset = leftOffset;
        int leftPadding = shadowSize + this.leftOffset;
        leftPadding = Math.max(leftPadding, 0);
        int paddingLeft = getPaddingLeft();
        if (paddingLeft != leftPadding) {
            super.setPadding(leftPadding, getPaddingTop(), getPaddingRight(), getPaddingBottom());
            createDrawables();
            invalidate();
        }
    }

    public int getTopOffset() {
        return topOffset;
    }

    public void setTopOffset(int topOffset) {
        int maxOffset = shadowSize / 2;
        topOffset = Math.min(maxOffset, topOffset);
        if (this.topOffset == topOffset) {
            return;
        }
        this.topOffset = topOffset;
        int topPadding = shadowSize + this.topOffset;
        topPadding = Math.max(topPadding, 0);
        int paddingTop = getPaddingTop();
        if (paddingTop != topPadding) {
            super.setPadding(getPaddingLeft(), topPadding, getPaddingRight(), getPaddingBottom());
            createDrawables();
            invalidate();
        }
    }

    public int getRightOffset() {
        return rightOffset;
    }

    public void setRightOffset(int rightOffset) {
        int maxOffset = shadowSize / 2;
        rightOffset = Math.min(maxOffset, rightOffset);
        if (this.rightOffset == rightOffset) {
            return;
        }
        this.rightOffset = rightOffset;
        int rightPadding = shadowSize + this.rightOffset;
        rightPadding = Math.max(rightPadding, 0);
        int paddingRight = getPaddingRight();
        if (paddingRight != rightPadding) {
            super.setPadding(getPaddingLeft(), getPaddingTop(), rightPadding, getPaddingBottom());
            createDrawables();
            invalidate();
        }
    }

    public int getBottomOffset() {
        return bottomOffset;
    }

    public void setBottomOffset(int bottomOffset) {
        int maxOffset = shadowSize / 2;
        bottomOffset = Math.min(maxOffset, bottomOffset);
        if (this.bottomOffset == bottomOffset) {
            return;
        }
        this.bottomOffset = bottomOffset;
        int bottomPadding = shadowSize + this.bottomOffset;
        bottomPadding = Math.max(bottomPadding, 0);
        int paddingBottom = getPaddingBottom();
        if (paddingBottom != bottomPadding) {
            super.setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), bottomPadding);
            createDrawables();
            invalidate();
        }
    }

    public void setShadowFluidShape(int shape) {
        if (shape != ADSORPTION && shape != LINEAR) {
            return;
        }
        if (this.shadowFluidShape == shape) {
            return;
        }
        this.shadowFluidShape = shape;
        initColors(shadowColor);
        createDrawables();
        postInvalidate();
    }

    /**
     * If multi properties is been set by java code,use this class to reduce drawable create times.
     public PropertyHolder properties() {
     return new PropertyHolder(this);
     }

     public static class PropertyHolder {
     private LCardView cardView;
     private Map<String, Object> map = new HashMap<>();

     private PropertyHolder(LCardView cardView) {
     this.cardView = cardView;
     }

     public PropertyHolder cornerRadius(int cornerRadius) {
     return this;
     }

     public void confirm() {

     }
     }
     */

}
