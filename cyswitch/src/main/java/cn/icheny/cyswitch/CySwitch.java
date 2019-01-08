package cn.icheny.cyswitch;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Checkable;

/**
 * <pre>
 *     @author : www.icheny.cn
 *     @e-mail : ausboyue@gmail.com
 *     @time   : 2019.01.08
 *     @desc   : CySwitch
 *     @version: 1.0.0
 * </pre>
 */
public class CySwitch extends View implements Checkable {
    private static final int WHITE = 0XFFFFFFFF;

    // The min width for SwitchView,When the SwitchView's MeasureSpec.
    // Mode which is applied in width is not be MeasureSpec.EXACTLY ,it will be set.
    private static final float MIN_WIDTH = 20;

    // The min height for SwitchView,When the SwitchView's MeasureSpec.
    // Mode which is applied in height is not be MeasureSpec.EXACTLY ,it will be set.
    private static final float MIN_HEIGHT = 12;

    // The check status
    private boolean mChecked;

    // The value which decide SwitchView can be checked.Default is true.
    private boolean mSwitchable = true;

    // The corner radius of SwitchView.
    private float mViewRadius;

    // The location of slider center，X coordinates, for horizontal direction.
    private float mSlideCenter;

    /**
     * out of border
     */
    // The paint
    private Paint mBorderPaint;

    // The width of border
    private float mBorderWidth;

    // The color of border when it be cheked.
    private int mBorderColorChecked = WHITE;

    // The color of border when it be uncheked.
    private int mBorderColorUnchecked = WHITE;

    /**
     * background
     */
    // The paint
    private Paint mBgPaint;

    // The background color of view when it be cheked.
    private int mBgColorChecked = 0XFFFF7E00;

    // The background color of view when it be uncheked.
    private int mBgColorUnchecked = 0XFFE5E5E5;


    /**
     * slider
     */
    // The paint
    private Paint mSliderPaint;

    // The slider color of view when it be cheked.
    private int mSliderColorChecked = WHITE;

    // The slider color of view when it be uncheked.
    private int mSliderColorUnchecked = WHITE;

    // The corner radius of slider.
    private float mSliderRadius;

    // The width of slider.
    private float mSliderWidth;

    // The heigth of slider.
    private float mSliderHeight;


    /**
     * animation
     */
    // Animation interval.Default is 180 milliseconds
    private long mDuration = 180;

    // Attribute animation control object
    private ValueAnimator mValueAnimator;

    // the offset
    private float mOffset;

    // Listener
    private OnCheckedChangeListener mOnCheckedListener;

    /**
     * Initialize attributes of Switch View.
     *
     * @param attrs
     */
    private void initAttr(AttributeSet attrs) {
        if (null == attrs) {
            return;
        }
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CySwitch);
        mChecked = a.getBoolean(R.styleable.CySwitch_isChecked, false);
        mSwitchable = a.getBoolean(R.styleable.CySwitch_switchable, true);
        mDuration = a.getInteger(R.styleable.CySwitch_duration, 180);
        mViewRadius = a.getDimension(R.styleable.CySwitch_viewRadius, 0f);
        mBorderWidth = a.getDimension(R.styleable.CySwitch_borderWidth, 0f);
        mBorderColorChecked = a.getColor(R.styleable.CySwitch_borderColorChecked, WHITE);
        mBorderColorUnchecked = a.getColor(R.styleable.CySwitch_borderColorUnchecked, WHITE);
        mBgColorChecked = a.getColor(R.styleable.CySwitch_bgColorChecked, mBgColorChecked);
        mBgColorUnchecked = a.getColor(R.styleable.CySwitch_bgColorUnchecked, mBgColorUnchecked);
        mSliderColorChecked = a.getColor(R.styleable.CySwitch_sliderColorChecked, WHITE);
        mSliderColorUnchecked = a.getColor(R.styleable.CySwitch_sliderColorUnchecked, WHITE);
        mSliderRadius = a.getDimension(R.styleable.CySwitch_sliderRadius, 0f);
        mSliderWidth = a.getDimension(R.styleable.CySwitch_sliderWidth, dp2px(sgtWidth(MIN_WIDTH)));
        mSliderHeight = a.getDimension(R.styleable.CySwitch_sliderHeight, dp2px(sgtHeight(MIN_HEIGHT)));
        a.recycle();
    }

    /**
     * Initialize paints.
     */
    private void initPaint() {
        /**
         * out of border
         */
        //paint
        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStrokeWidth(mBorderWidth);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setColor(mChecked ? mBorderColorChecked : mBorderColorUnchecked);

        /**
         * background
         */
        //paint
        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setColor(mChecked ? mBgColorChecked : mBgColorUnchecked);


        /**
         * slider
         */
        //paint
        mSliderPaint = new Paint();
        mSliderPaint.setAntiAlias(true);
        mSliderPaint.setStyle(Paint.Style.FILL);

        resetPaintColor();
    }

    /**
     * reset paint color
     */
    private void resetPaintColor() {
        mBorderPaint.setColor(mChecked ? mBorderColorChecked : mBorderColorUnchecked);
        mBgPaint.setColor(mChecked ? mBgColorChecked : mBgColorUnchecked);
        mSliderPaint.setColor(mChecked ? mSliderColorChecked : mSliderColorUnchecked);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        final int width = canvas.getWidth();
        final int height = canvas.getHeight();

        /**
         * Drawing order from bottom to top to prevent overwriting.
         * background ---> border ---> slider
         */
        //Drawing background
        final RectF bgRect = new RectF(0, 0, width, height);
        canvas.drawRoundRect(bgRect, mViewRadius, mViewRadius, mBgPaint);

        // Drawing border
        if (mBorderWidth > 0) {
            final RectF borderRect = new RectF(mBorderWidth / 2, mBorderWidth / 2, width - mBorderWidth / 2, height - mBorderWidth / 2);
            canvas.drawRoundRect(borderRect, mViewRadius - mBorderWidth / 2, mViewRadius - mBorderWidth / 2, mBorderPaint);
        }

        // Drawing slider
        final RectF sliderRect = new RectF(mSlideCenter - mSliderWidth / 2 + mOffset, (height - mSliderHeight) / 2, mSlideCenter + mSliderWidth / 2 + mOffset, (mSliderHeight + height) / 2);
        canvas.drawRoundRect(sliderRect, mSliderRadius, mSliderRadius, mSliderPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * width
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode != MeasureSpec.EXACTLY) {
            width = dp2px(MIN_WIDTH);
        }

        /**
         *  height
         */
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode != MeasureSpec.EXACTLY) {
            height = dp2px(MIN_HEIGHT);
        }


        /**
         * Prevent setting slider width or height in XML is bigger than View width or height recommended value. UI is ugly!
         */
        // Suggest Width
        float sgtWidth = sgtWidth(width);
        if (mSliderWidth > sgtWidth) {
            mSliderWidth = sgtWidth;
        }
        // Suggest Height
        float sgtHeight = sgtHeight(height);
        if (mSliderHeight > sgtHeight) {
            mSliderHeight = sgtHeight;
        }

        // set location of slider center
        setSlideCenter(width);

        setMeasuredDimension(width, height);
    }

    // set location of slider center
    private void setSlideCenter(int width) {
        if (mChecked) {
            mSlideCenter = width * 3.0f / 4;
        } else {
            mSlideCenter = width * 1.0f / 4;
        }
    }


    @Override
    public void setChecked(boolean checked) {
        if (!mSwitchable) {
            return;
        }

        if (mChecked == checked) {
            return;
        }
        if (isAnimRunning()) {
            return;
        }

        int width = getWidth();
        startAnim(checked ? width / 2 : -width / 2);
    }

    private boolean isAnimRunning() {
        return mValueAnimator != null && mValueAnimator.isRunning();
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public boolean performClick() {
        toggle();
        final boolean handled = super.performClick();
        if (!handled) {
            //点击音效
            playSoundEffect(SoundEffectConstants.CLICK);
        }
        return handled;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }


    public CySwitch(Context context) {
        this(context, null);
    }

    public CySwitch(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CySwitch(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(attrs);
        initPaint();
        Switchable(mSwitchable);
    }

    private void startAnim(float delta) {

        mValueAnimator = ValueAnimator.ofFloat(0, delta);
        mValueAnimator.setDuration(mDuration);
        mValueAnimator.setInterpolator(new AccelerateInterpolator());
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mOffset = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        mValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                switchCheckStatus();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        mValueAnimator.start();

    }

    /**
     * Switch check status.
     */
    private void switchCheckStatus() {
        mChecked = !mChecked;
        mOffset = 0;
        //set location of slider center
        setSlideCenter(getWidth());
        // reset paint color
        resetPaintColor();
        if (mOnCheckedListener != null) {
            mOnCheckedListener.onCheckedChanged(this, mChecked);
        }
    }

    /**
     * The suggest width for slider
     *
     * @param width
     * @return
     */
    private float sgtWidth(float width) {
        return width * 9.0f / 20;
    }

    /**
     * The suggest height for slider
     *
     * @param height
     * @return
     */
    private float sgtHeight(float height) {
        return height * 4.0f / 5;
    }


    public void Switchable(boolean switchable) {
        mSwitchable = switchable;
        setEnabled(switchable);
        setClickable(switchable);
    }

    /**
     * set view radius by dp value
     *
     * @param radius
     */
    public void setViewRadius(float radius) {
        if (radius < 0) {
            radius = 0;
        }
        mViewRadius = dp2px(radius);
        invalidate();
    }

    /**
     * set slider radius by dp value
     *
     * @param radius
     */
    public void setSliderRadius(float radius) {

        if (radius < 0) {
            radius = 0;
        }
        mSliderRadius = dp2px(radius);
        invalidate();
    }

    /**
     * set border width by dp value
     *
     * @param width
     */
    public void setBorderWidth(float width) {
        if (width < 0) {
            width = 0;
        }
        mBorderWidth = dp2px(width);
        mBorderPaint.setStrokeWidth(mBorderWidth);
        invalidate();
    }

    /**
     * DP to PX
     *
     * @param dpValue
     * @return
     */
    private int dp2px(float dpValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * Register a callback to be invoked when the checked state of this button
     * changes.
     *
     * @param listener the callback to call on checked state change
     */
    public void setOnCheckedChangeListener(@Nullable OnCheckedChangeListener listener) {
        mOnCheckedListener = listener;
    }

    /**
     * Interface definition for a callback to be invoked when the checked state
     * of a compound button changed.
     */
    public static interface OnCheckedChangeListener {
        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param switchView The switch view whose state has changed.
         * @param isChecked  The new checked state of switchView.
         */
        void onCheckedChanged(CySwitch switchView, boolean isChecked);
    }
}
