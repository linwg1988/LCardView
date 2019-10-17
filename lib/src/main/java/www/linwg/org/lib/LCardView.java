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
import android.widget.FrameLayout;

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
    private int defaultShadowColor = Color.parseColor("#05000000");
    private int defaultCardBackgroundColor = Color.WHITE;
    private int[] colors = new int[]{defaultShadowColor, defaultShadowColor, Color.parseColor("#00000000"), Color.parseColor("#00000000")};
    private int shadowColor = defaultShadowColor;
    private int cardBackgroundColor = defaultCardBackgroundColor;
    private int cornerRadius = 0;
    private boolean elevationAffectShadowColor = false;
    private boolean elevationAffectShadowSize = false;
    private int leftTopCornerRadius = 0;
    private int rightTopCornerRadius = 0;
    private int rightBottomCornerRadius = 0;
    private int leftBottomCornerRadius = 0;
    private int viewWidth;
    private int viewHeight;
    private Path mPath = new Path();
    private Path highVerPath = new Path();
    private Path mContentPath = new Path();
    private Path mShadowPath = new Path();
    private Paint paint = new Paint();
    private Paint bgColorPaint = new Paint();
    private Paint bgPaint = new Paint();
    private Paint linePaint = new Paint();
    private Paint erasePaint = new Paint();
    private Paint pathPaint = new Paint();
    RadialGradient ltrg;
    RadialGradient rtrg;
    RadialGradient rbrg;
    RadialGradient lbrg;
    LinearGradient t, r, b, l;
    private int shadowAlpha = defaultShadowStartAlpha;
    float percent = 0.33f;

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
        linePaint.setAntiAlias(true);
        linePaint.setDither(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(1);
        erasePaint.setStrokeWidth(1);
        erasePaint.setStyle(Paint.Style.STROKE);
        erasePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

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
        if (leftOffset > shadowSize) {
            leftOffset = shadowSize;
        }
        if (topOffset > shadowSize) {
            topOffset = shadowSize;
        }
        if (rightOffset > shadowSize) {
            rightOffset = shadowSize;
        }
        if (bottomOffset > shadowSize) {
            bottomOffset = shadowSize;
        }
        super.setPadding(shadowSize, shadowSize, shadowSize, shadowSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = getMeasuredWidth();
        viewHeight = getMeasuredHeight();
        createDrawables();
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

    private void judgeEdge() {
        if (shadowSize > viewWidth / 4) {
            shadowSize = viewWidth / 4;
        }
        if (shadowSize > viewWidth / 4) {
            shadowSize = viewWidth / 4;
        }
        if (shadowSize > viewHeight / 4) {
            shadowSize = viewHeight / 4;
        }
        if (shadowSize > viewHeight / 4) {
            shadowSize = viewHeight / 4;
        }
    }

    private void createDrawables() {
//        judgeOffset();

        int min = shadowSize + leftTopCornerRadius;
        if (min == 0) {
            ltrg = null;
        } else {
            float start = leftTopCornerRadius / (float) min;
            float center = (1 - start) * percent + start;
            float center2 = (1 - center) / 2 + center;
            ltrg = new RadialGradient(shadowSize + leftTopCornerRadius + leftOffset, shadowSize + leftTopCornerRadius + topOffset, min, colors, new float[]{start, center, center2, 1}, Shader.TileMode.CLAMP);
        }

        int min2 = shadowSize + rightTopCornerRadius;
        if (min2 == 0) {
            rtrg = null;
        } else {
            float start = rightTopCornerRadius / (float) min2;
            float center = (1 - start) * percent + start;
            float center2 = (1 - center) / 2 + center;
            rtrg = new RadialGradient(viewWidth - shadowSize - rightTopCornerRadius + rightOffset, shadowSize + rightTopCornerRadius + topOffset, min2, colors, new float[]{start, center, center2, 1}, Shader.TileMode.CLAMP);
        }

        int min3 = shadowSize + rightBottomCornerRadius;
        if (min3 == 0) {
            rbrg = null;
        } else {
            float start = rightBottomCornerRadius / (float) min3;
            float center = (1 - start) * percent + start;
            float center2 = (1 - center) / 2 + center;
            rbrg = new RadialGradient(viewWidth - shadowSize - rightBottomCornerRadius + rightOffset, viewHeight - shadowSize - rightBottomCornerRadius + bottomOffset, min3, colors, new float[]{start, center, center2, 1}, Shader.TileMode.CLAMP);
        }

        int min4 = shadowSize + leftBottomCornerRadius;
        if (min4 == 0) {
            lbrg = null;
        } else {
            float start = leftBottomCornerRadius / (float) min4;
            float center = (1 - start) * percent + start;
            float center2 = (1 - center) / 2 + center;
            lbrg = new RadialGradient(shadowSize + leftBottomCornerRadius + leftOffset, viewHeight - shadowSize - leftBottomCornerRadius + bottomOffset, min4, colors, new float[]{start, center, center2, 1}, Shader.TileMode.CLAMP);
        }

        t = new LinearGradient(shadowSize + leftTopCornerRadius, shadowSize + topOffset, shadowSize + leftTopCornerRadius, +topOffset, colors, new float[]{0, percent, (1 - percent) / 2 + percent, 1}, Shader.TileMode.CLAMP);

        r = new LinearGradient(viewWidth - shadowSize + rightOffset, shadowSize + rightTopCornerRadius, viewWidth + rightOffset, shadowSize + rightTopCornerRadius, colors, new float[]{0, percent, (1 - percent) / 2 + percent, 1}, Shader.TileMode.CLAMP);

        b = new LinearGradient(shadowSize + leftBottomCornerRadius, viewHeight - shadowSize + bottomOffset, shadowSize + leftBottomCornerRadius, viewHeight + bottomOffset, colors, new float[]{0, percent, (1 - percent) / 2 + percent, 1}, Shader.TileMode.CLAMP);

        l = new LinearGradient(shadowSize + leftOffset, shadowSize + leftTopCornerRadius, leftOffset, shadowSize + leftTopCornerRadius, colors, new float[]{0, percent, (1 - percent) / 2 + percent, 1}, Shader.TileMode.CLAMP);
    }

    private void measureContentPath() {
        mShadowPath.reset();
        float startX = shadowSize + leftOffset;
        float startY = shadowSize + leftTopCornerRadius + topOffset;
        mShadowPath.moveTo(startX, startY);
        mShadowPath.arcTo(new RectF(startX, shadowSize + topOffset, leftTopCornerRadius * 2 + startX, shadowSize + leftTopCornerRadius * 2 + topOffset), 180, 90);
        mShadowPath.lineTo(viewWidth - shadowSize - rightTopCornerRadius + rightOffset, shadowSize + topOffset);
        mShadowPath.arcTo(new RectF(viewWidth - shadowSize - rightTopCornerRadius * 2 + rightOffset, shadowSize + topOffset, viewWidth - shadowSize + rightOffset, shadowSize + rightTopCornerRadius * 2 + topOffset), 270, 90);
        mShadowPath.lineTo(viewWidth - shadowSize + rightOffset, viewHeight - shadowSize - rightBottomCornerRadius + bottomOffset);
        mShadowPath.arcTo(new RectF(viewWidth - shadowSize - rightBottomCornerRadius * 2 + rightOffset, viewHeight - shadowSize - rightBottomCornerRadius * 2 + bottomOffset, viewWidth - shadowSize + rightOffset, viewHeight - shadowSize + bottomOffset), 0, 90);
        mShadowPath.lineTo(shadowSize + leftBottomCornerRadius + leftOffset, viewHeight - shadowSize + bottomOffset);
        mShadowPath.arcTo(new RectF(shadowSize + leftOffset, viewHeight - shadowSize - leftBottomCornerRadius * 2 + bottomOffset, shadowSize + leftBottomCornerRadius * 2 + leftOffset, viewHeight - shadowSize + bottomOffset), 90, 90);
        mShadowPath.close();

        mContentPath.reset();
        startX = shadowSize;
        startY = shadowSize + leftTopCornerRadius;
        mContentPath.moveTo(startX, startY);
        mContentPath.arcTo(new RectF(startX, shadowSize, leftTopCornerRadius * 2 + startX, shadowSize + leftTopCornerRadius * 2), 180, 90);
        mContentPath.lineTo(viewWidth - shadowSize - rightTopCornerRadius, shadowSize);
        mContentPath.arcTo(new RectF(viewWidth - shadowSize - rightTopCornerRadius * 2, shadowSize, viewWidth - shadowSize, shadowSize + rightTopCornerRadius * 2), 270, 90);
        mContentPath.lineTo(viewWidth - shadowSize, viewHeight - shadowSize - rightBottomCornerRadius);
        mContentPath.arcTo(new RectF(viewWidth - shadowSize - rightBottomCornerRadius * 2, viewHeight - shadowSize - rightBottomCornerRadius * 2, viewWidth - shadowSize, viewHeight - shadowSize), 0, 90);
        mContentPath.lineTo(shadowSize + leftBottomCornerRadius, viewHeight - shadowSize);
        mContentPath.arcTo(new RectF(shadowSize, viewHeight - shadowSize - leftBottomCornerRadius * 2, shadowSize + leftBottomCornerRadius * 2, viewHeight - shadowSize), 90, 90);
        mContentPath.close();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
//        canvas.save();
//        int saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
//        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
//            pathPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
//            super.dispatchDraw(canvas);
//            canvas.drawPath(mContentPath, pathPaint);
//        } else {
//            pathPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
//            super.dispatchDraw(canvas);
//            highVerPath.reset();
//            highVerPath.addRect(0, 0, getWidth(), getHeight(), Path.Direction.CW);
//            highVerPath.op(mContentPath, Path.Op.DIFFERENCE);
//            canvas.drawPath(highVerPath, pathPaint);
//        }
////        canvas.restoreToCount(saveCount);
//        canvas.restore();
//        pathPaint.setXfermode(null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        measureContentPath();
        bgPaint.setColor(shadowColor);
        canvas.drawPath(mShadowPath, bgPaint);
        bgColorPaint.setColor(cardBackgroundColor);
        canvas.drawPath(mContentPath, bgColorPaint);

        //左上圆角
        int radius = shadowSize + leftTopCornerRadius;
        if (radius != 0) {
            canvas.save();
            canvas.clipRect(leftOffset, topOffset, radius + leftOffset, radius + topOffset);
            mPath.reset();
            mPath.addCircle(leftOffset + radius, topOffset + radius, leftTopCornerRadius, Path.Direction.CCW);
            canvas.clipPath(mPath, Region.Op.DIFFERENCE);
            paint.setShader(ltrg);
            canvas.drawCircle(radius + leftOffset, radius + topOffset, radius, paint);
            canvas.restore();
        }

        //顶部阴影
        canvas.save();
        paint.setShader(t);
        canvas.drawRect(radius + leftOffset, topOffset, viewWidth - shadowSize - rightTopCornerRadius + rightOffset, shadowSize + topOffset, paint);
        canvas.restore();

        //右上圆角
        radius = shadowSize + rightTopCornerRadius;
        if (radius != 0) {
            canvas.save();
            canvas.clipRect(viewWidth - radius + rightOffset, topOffset, viewWidth + rightOffset, radius + topOffset);
            mPath.reset();
            mPath.addCircle(viewWidth - radius + rightOffset, radius + topOffset, rightTopCornerRadius, Path.Direction.CCW);
            canvas.clipPath(mPath, Region.Op.DIFFERENCE);
            paint.setShader(rtrg);
            canvas.drawCircle(viewWidth - radius + rightOffset, radius + topOffset, radius, paint);
            canvas.restore();
        }

        //右侧阴影
        canvas.save();
        paint.setShader(r);
        canvas.drawRect(viewWidth - shadowSize + rightOffset, shadowSize + rightTopCornerRadius + topOffset, viewWidth + rightOffset, viewHeight - shadowSize - rightBottomCornerRadius + bottomOffset, paint);
        canvas.restore();

        //右下圆角阴影
        radius = shadowSize + rightBottomCornerRadius;
        if (radius != 0) {
            canvas.save();
            canvas.clipRect(viewWidth - radius + rightOffset, viewHeight - radius + bottomOffset, viewWidth + rightOffset, viewHeight + bottomOffset);
            mPath.reset();
            mPath.addCircle(viewWidth - radius + rightOffset, viewHeight - radius + bottomOffset, rightBottomCornerRadius, Path.Direction.CCW);
            canvas.clipPath(mPath, Region.Op.DIFFERENCE);
            paint.setShader(rbrg);
            canvas.drawCircle(viewWidth - radius + rightOffset, viewHeight - radius + bottomOffset, radius, paint);
            canvas.restore();
        }

        //底部阴影
        canvas.save();
        paint.setShader(b);
        canvas.drawRect(shadowSize + leftBottomCornerRadius + leftOffset, viewHeight - shadowSize + bottomOffset, viewWidth - shadowSize - rightBottomCornerRadius + rightOffset, viewHeight + bottomOffset, paint);
        canvas.restore();

        //左下圆角阴影
        radius = shadowSize + leftBottomCornerRadius;
        if (radius != 0) {
            canvas.save();
            canvas.clipRect(leftOffset, viewHeight - radius + bottomOffset, radius + leftOffset, viewHeight + bottomOffset);
            mPath.reset();
            mPath.addCircle(radius + leftOffset, viewHeight - radius + bottomOffset, leftBottomCornerRadius, Path.Direction.CCW);
            canvas.clipPath(mPath, Region.Op.DIFFERENCE);
            paint.setShader(lbrg);
            canvas.drawCircle(radius + leftOffset, viewHeight - radius + bottomOffset, radius, paint);
            canvas.restore();
        }

        //左侧阴影
        canvas.save();
        paint.setShader(l);
        canvas.drawRect(leftOffset, shadowSize + leftTopCornerRadius + topOffset, shadowSize + leftOffset, viewHeight - shadowSize - leftBottomCornerRadius + bottomOffset, paint);
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

    private boolean adjustEdgeSize() {

        return false;
    }

    public void setLeftTopCornerRadius(int leftTopCornerRadius) {
        this.leftTopCornerRadius = leftTopCornerRadius;
        this.leftTopCornerRadius = Math.min(this.leftTopCornerRadius, (viewWidth - shadowSize - shadowSize) / 2);
        this.leftTopCornerRadius = Math.min(this.leftTopCornerRadius, (viewHeight - shadowSize - shadowSize) / 2);
//        if (adjustEdgeSize()) {
//            super.setPadding(shadowSize ,
//                    shadowSize ,
//                    shadowSize - xOffset,
//                    shadowSize - yOffset);
//        }
        createDrawables();
        invalidate();
    }


    public void setRightTopCornerRadius(int rightTopCornerRadius) {
        this.rightTopCornerRadius = rightTopCornerRadius;
        this.rightTopCornerRadius = Math.min(this.rightTopCornerRadius, (viewWidth - shadowSize - shadowSize) / 2);
        this.rightTopCornerRadius = Math.min(this.rightTopCornerRadius, (viewHeight - shadowSize - shadowSize) / 2);
//        if (adjustEdgeSize()) {
//            super.setPadding(shadowSize ,
//                    shadowSize ,
//                    shadowSize - xOffset,
//                    shadowSize - yOffset);
//        }
        createDrawables();
        invalidate();
    }

    public void setRightBottomCornerRadius(int rightBottomCornerRadius) {
        this.rightBottomCornerRadius = rightBottomCornerRadius;
        this.rightBottomCornerRadius = Math.min(this.rightBottomCornerRadius, (viewWidth - shadowSize - shadowSize) / 2);
        this.rightBottomCornerRadius = Math.min(this.rightBottomCornerRadius, (viewHeight - shadowSize - shadowSize) / 2);
//        if (adjustEdgeSize()) {
//            super.setPadding(shadowSize ,
//                    shadowSize ,
//                    shadowSize - xOffset,
//                    shadowSize - yOffset);
//        }
        createDrawables();
        invalidate();
    }

    public void setLeftBottomCornerRadius(int leftBottomCornerRadius) {
        this.leftBottomCornerRadius = leftBottomCornerRadius;
        this.leftBottomCornerRadius = Math.min(this.leftBottomCornerRadius, (viewWidth - shadowSize - shadowSize) / 2);
        this.leftBottomCornerRadius = Math.min(this.leftBottomCornerRadius, (viewHeight - shadowSize - shadowSize) / 2);
//        if (adjustEdgeSize()) {
//            super.setPadding(shadowSize ,
//                    shadowSize ,
//                    shadowSize - xOffset,
//                    shadowSize - yOffset);
//        }
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
        initColors(color);
        createDrawables();
        invalidate();
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
        this.shadowAlpha = alpha;
        setShadowColor(shadowColor);
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
//            if (elevationAffectShadowSize) {
//                shadowSize = elevation + 12;
//                super.setPadding(shadowSize ,
//                        shadowSize ,
//                        shadowSize - xOffset,
//                        shadowSize - yOffset);
//            }
            createDrawables();
            invalidate();
        }
    }

    public void setElevation(int elevation) {
        this.elevation = elevation;
        if (elevationAffectShadowColor) {
            initColors(shadowColor);
        }
//        if (elevationAffectShadowSize) {
//            shadowSize = elevation + 12;
//            judgeEdge();
//            super.setPadding(shadowSize ,
//                    shadowSize ,
//                    shadowSize - xOffset,
//                    shadowSize - yOffset);
//        }
        createDrawables();
        invalidate();
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        leftTopCornerRadius = leftBottomCornerRadius = rightTopCornerRadius = rightBottomCornerRadius = cornerRadius;
//        if (adjustEdgeSize()) {
//            super.setPadding(shadowSize ,
//                    shadowSize ,
//                    shadowSize - xOffset,
//                    shadowSize - yOffset);
//        }
        createDrawables();
        invalidate();
    }

    public void setShadowSize(int shadowSize) {
        if (elevationAffectShadowSize) {
            //This field make shadow size change with elevation.
            return;
        }
        this.shadowSize = shadowSize;
//        if (xOffset > shadowSize) {
//            xOffset = shadowSize;
//        }
//        if (xOffset < -shadowSize) {
//            xOffset = -shadowSize;
//        }
//        if (yOffset > shadowSize) {
//            yOffset = shadowSize;
//        }
//        if (yOffset < -shadowSize) {
//            yOffset = -shadowSize;
//        }
//        super.setPadding(shadowSize ,
//                shadowSize ,
//                shadowSize - xOffset,
//                shadowSize - yOffset);
        createDrawables();
        invalidate();
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
        this.cardBackgroundColor = cardBackgroundColor;
        invalidate();
    }

    public void setShadowFluidShape(int shape) {
        if (shape != ADSORPTION && shape != LINEAR) {
            return;
        }
        this.shadowFluidShape = shape;
        initColors(shadowColor);
        createDrawables();
        postInvalidate();
    }
}
