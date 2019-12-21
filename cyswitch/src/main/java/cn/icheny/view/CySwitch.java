package cn.icheny.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
 *     @desc   : CySwitch , A custom Switch View.
 *     @version: 1.0.0
 * </pre>
 */
public class CySwitch extends View implements Checkable {
    private static final int WHITE = 0XFFFFFFFF;

    /**
     * The min width for SwitchView,When the SwitchView's MeasureSpec.
     * Mode which is applied in width is not be MeasureSpec.EXACTLY ,it will be set.
     */
    private static final float MIN_WIDTH = 28;

    /**
     * The min height for SwitchView,When the SwitchView's MeasureSpec.
     * Mode which is applied in height is not be MeasureSpec.EXACTLY ,it will be set.
     */
    private static final float MIN_HEIGHT = 16;

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

    // The color of border when it be checked.
    private int mBorderColorChecked = WHITE;

    // The color of border when it be unchecked.
    private int mBorderColorUnchecked = WHITE;

    /**
     * background
     */
    // The paint
    private Paint mBgPaint;

    // The background color when it be checked.
    private int mBgColorChecked = 0XFFFF7E00;

    // The background color when it be unchecked.
    private int mBgColorUnchecked = 0XFFE5E5E5;

    // The width of background.
    private float mBgWidth;

    // The height of background.
    private float mBgHeight;

    /**
     * slider
     */
    // The paint
    private Paint mSliderPaint;

    // The slider color of view when it be checked.
    private int mSliderColorChecked = WHITE;

    // The slider color of view when it be unchecked.
    private int mSliderColorUnchecked = WHITE;

    // The corner radius of slider.
    private float mSliderRadius;

    // The width of slider.
    private float mSliderWidth;

    // The height of slider.
    private float mSliderHeight;


    /**
     * text
     */
    // The paint
    private Paint mTextPaint;
    private String mCheckedText;
    private String mUncheckedText;
    // The text color  when it be checked.
    private int mTextColorChecked = 0XFFFF7E00;
    // The text color when it be unchecked.
    private int mTextColorUnchecked = WHITE;
    private float mTextSize;

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

    /**
     * Initialize attributes of Switch View.
     *
     * @param attrs attributes
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
        mSliderWidth = a.getDimension(R.styleable.CySwitch_sliderWidth, dp2px(MIN_WIDTH));
        mSliderHeight = a.getDimension(R.styleable.CySwitch_sliderHeight, dp2px(MIN_HEIGHT));
        mBgWidth = a.getDimension(R.styleable.CySwitch_bgWidth, dp2px(MIN_WIDTH));
        mBgHeight = a.getDimension(R.styleable.CySwitch_bgHeight, dp2px(MIN_HEIGHT));
        mUncheckedText = a.getString(R.styleable.CySwitch_textUnchecked);
        mCheckedText = a.getString(R.styleable.CySwitch_textChecked);
        mTextSize = a.getDimension(R.styleable.CySwitch_textSize, dp2px(MIN_HEIGHT / 2));
        mTextColorUnchecked = a.getColor(R.styleable.CySwitch_textColorUnchecked, mTextColorUnchecked);
        mTextColorChecked = a.getColor(R.styleable.CySwitch_textColorChecked, mTextColorChecked);
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

        /**
         * text
         */
        //paint
        mTextPaint = new Paint();
        mTextPaint.setStrokeWidth(0);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mTextSize);
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

        final int width = getWidth();
        final int height = getHeight();

        /**
         * Drawing order from bottom to top to prevent overwriting.
         * background ---> border ---> slider
         */
        //Drawing background
        final float bgOffsetWith = (width - mBgWidth) / 2;
        final float bgOffsetHeight = (height - mBgHeight) / 2;
        final RectF bgRect = new RectF(bgOffsetWith, bgOffsetHeight,
                bgOffsetWith + mBgWidth, bgOffsetHeight + mBgHeight);
        canvas.drawRoundRect(bgRect, mViewRadius, mViewRadius, mBgPaint);

        // Drawing border
        if (mBorderWidth > 0) {
            final RectF borderRect = new RectF(bgOffsetWith + mBorderWidth / 2, bgOffsetHeight + mBorderWidth / 2,
                    bgOffsetWith + mBgWidth - mBorderWidth / 2, bgOffsetHeight + mBgHeight - mBorderWidth / 2);
            canvas.drawRoundRect(borderRect, mViewRadius - mBorderWidth / 2, mViewRadius - mBorderWidth / 2, mBorderPaint);
        }

        // Drawing slider
        final RectF sliderRect = new RectF(mSlideCenter - mSliderWidth / 2 + mOffset, (height - mSliderHeight) / 2, mSlideCenter + mSliderWidth / 2 + mOffset, (mSliderHeight + height) / 2);
        canvas.drawRoundRect(sliderRect, mSliderRadius, mSliderRadius, mSliderPaint);

        // Drawing text
        final Paint.FontMetricsInt fm = mTextPaint.getFontMetricsInt();
        if (!TextUtils.isEmpty(mUncheckedText)) {
            mTextPaint.setColor(mChecked ? mTextColorUnchecked : mTextColorChecked);
            // Calculate the baseline location.
            final float baseLine = height * 1f / 2 + (fm.bottom - fm.top) * 1f / 2 - fm.bottom;
            // Calculate the unchecked text location.
            final float uncheckedTextLoc = width * 1f / 2 - mBgWidth * 1f / 4 - mTextPaint.measureText(mCheckedText) / 2;
            canvas.drawText(mUncheckedText, uncheckedTextLoc, baseLine, mTextPaint);
        }

        if (!TextUtils.isEmpty(mCheckedText)) {
            mTextPaint.setColor(mChecked ? mTextColorChecked : mTextColorUnchecked);
            // Calculate the baseline location.
            final float baseLine = height * 1f / 2 + (fm.bottom - fm.top) * 1f / 2 - fm.bottom;
            // Calculate the checked text location.
            final float checkedTextLoc = width * 1f / 2 + mBgWidth * 1f / 4 - mTextPaint.measureText(mUncheckedText) / 2;
            canvas.drawText(mCheckedText, checkedTextLoc, baseLine, mTextPaint);
        }
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
         * Prevent setting slider width or height in XML is bigger than View width or height value.
         */
        // Suggest slider range
        suggestSliderRange(width, height);
        // Suggest background range
        suggestBgRange(width, height);
        // set location of slider center
        setSlideCenter(mBgWidth);
        setMeasuredDimension(width, height);
    }

    // set location of slider center
    private void setSlideCenter(float width) {
        if (mChecked) {
            mSlideCenter = width * 3 / 4;
        } else {
            mSlideCenter = width / 4;
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

        mChecked = checked;

        int width = getWidth();
        startAnim(checked ? width * 1f / 2 : -width * 1f / 2);
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
            // Click sound
            playSoundEffect(SoundEffectConstants.CLICK);
        }
        return handled;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
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
        mValueAnimator.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                switchCheckStatus();
            }
        });
        mValueAnimator.start();
    }

    /**
     * Switch check status.
     */
    private void switchCheckStatus() {
        mOffset = 0;
        //set location of slider center
        setSlideCenter(mBgWidth);
        // reset paint color
        resetPaintColor();
        if (mOnCheckedListener != null) {
            mOnCheckedListener.onCheckedChanged(this, mChecked);
        }
    }

    /**
     * The suggest range for slider
     */
    private void suggestSliderRange(float width, float height) {
        final float halfWidth = width / 2;
        if (mSliderWidth > halfWidth) {
            mSliderWidth = halfWidth;
        }
        if (mSliderWidth < 1) {
            mSliderWidth = 1;
        }
        if (mSliderHeight > height) {
            mSliderHeight = height;
        }
        if (mSliderHeight < 1) {
            mSliderHeight = 1;
        }
    }

    /**
     * The suggest range for background
     */
    private void suggestBgRange(float width, float height) {
        if (mBgWidth > width) {
            mBgWidth = width;
        }
        if (mBgWidth < 1) {
            mBgWidth = width;
        }
        if (mBgHeight > height) {
            mBgHeight = height;
        }
        if (mBgHeight < 1) {
            mBgHeight = height;
        }
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
    public interface OnCheckedChangeListener {
        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param switchView The switch view whose state has changed.
         * @param isChecked  The new checked state of switchView.
         */
        void onCheckedChanged(CySwitch switchView, boolean isChecked);
    }
}
