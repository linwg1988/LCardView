package www.linwg.org.app;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;

import www.linwg.org.lib.LCardView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final LCardView cardView = findViewById(R.id.cardView);
        SeekBar sbLeftTop = findViewById(R.id.sbLeftTop);
        SeekBar sbRightTop = findViewById(R.id.sbRightTop);
        SeekBar sbRightBottom = findViewById(R.id.sbRightBottom);
        SeekBar sbLeftBottom = findViewById(R.id.sbLeftBottom);
        SeekBar sbShadowAlpha = findViewById(R.id.sbShadowAlpha);
        SeekBar sbZIndex = findViewById(R.id.sbZIndex);
        SeekBar sbShadowSize = findViewById(R.id.sbShadowSize);
        SeekBar sbCorner = findViewById(R.id.sbCorner);
        SeekBar sbLeftOffset = findViewById(R.id.sbLeftOffset);
        SeekBar sbTopOffset = findViewById(R.id.sbTopOffset);
        SeekBar sbRightOffset = findViewById(R.id.sbRightOffset);
        SeekBar sbBottomOffset = findViewById(R.id.sbBottomOffset);
        CheckBox cbSize = findViewById(R.id.cbSize);
        CheckBox cbShowPic = findViewById(R.id.cbShowPic);
        final CheckBox cbColor = findViewById(R.id.cbColor);
        final CheckBox cbMode = findViewById(R.id.cbMode);
        final EditText etColor = findViewById(R.id.etColor);
        final Button btnSure = findViewById(R.id.btnSure);
        final ImageView ivGirl = findViewById(R.id.ivGirl);
        final SeekBar sbOffset = findViewById(R.id.sbOffset);
        final SeekBar sbCenterOffset = findViewById(R.id.sbCenterOffset);
        initSeekBar(sbLeftOffset);
        initSeekBar(sbTopOffset);
        initSeekBar(sbRightOffset);
        initSeekBar(sbRightOffset);
        initSeekBar(sbOffset);
        initSeekBar(sbCenterOffset);
        sbOffset.setOnSeekBarChangeListener(new OnSeekBarChangeAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cardView.setShadowOffset(progress - 100);
            }
        });
        sbCenterOffset.setOnSeekBarChangeListener(new OnSeekBarChangeAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cardView.setShadowOffsetCenter(progress - 100);
            }
        });
        sbLeftOffset.setOnSeekBarChangeListener(new OnSeekBarChangeAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cardView.setLeftOffset(progress - 100);
            }
        });
        sbTopOffset.setOnSeekBarChangeListener(new OnSeekBarChangeAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cardView.setTopOffset(progress - 100);
            }
        });
        sbRightOffset.setOnSeekBarChangeListener(new OnSeekBarChangeAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cardView.setRightOffset(progress - 100);
            }
        });
        sbBottomOffset.setOnSeekBarChangeListener(new OnSeekBarChangeAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cardView.setBottomOffset(progress - 100);
            }
        });
        sbLeftTop.setOnSeekBarChangeListener(new OnSeekBarChangeAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cardView.setLeftTopCornerRadius(progress);
            }
        });
        sbRightTop.setOnSeekBarChangeListener(new OnSeekBarChangeAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cardView.setRightTopCornerRadius(progress);
            }
        });
        sbRightBottom.setOnSeekBarChangeListener(new OnSeekBarChangeAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cardView.setRightBottomCornerRadius(progress);
            }
        });
        sbLeftBottom.setOnSeekBarChangeListener(new OnSeekBarChangeAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cardView.setLeftBottomCornerRadius(progress);
            }
        });
        sbShadowAlpha.setOnSeekBarChangeListener(new OnSeekBarChangeAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cardView.setShadowAlpha(progress);
            }
        });
        sbShadowSize.setOnSeekBarChangeListener(new OnSeekBarChangeAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cardView.setShadowSize(progress);
            }
        });
        sbCorner.setOnSeekBarChangeListener(new OnSeekBarChangeAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cardView.setCornerRadius(progress);
            }
        });
        cbSize.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cardView.setElevationAffectShadowSize(isChecked);
            }
        });
        cbColor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cardView.setElevationAffectShadowColor(isChecked);
            }
        });
        cbMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cardView.setShadowFluidShape(isChecked ? LCardView.LINEAR : LCardView.ADSORPTION);
            }
        });
        cbShowPic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ivGirl.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
            }
        });
        sbZIndex.setOnSeekBarChangeListener(new OnSeekBarChangeAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cardView.setElevation(progress);
            }
        });
        etColor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                btnSure.setEnabled(s.length() == 6);
            }
        });
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int parseColor = Color.parseColor("#" + etColor.getText());
                    etColor.setTextColor(parseColor);
                    cardView.setShadowColor(parseColor);
                } catch (Exception e) {

                }
            }
        });
    }

    private void initSeekBar(SeekBar seekBar) {
        seekBar.setMax(200);
        seekBar.setProgress(100);
    }

    private static abstract class OnSeekBarChangeAdapter implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }
}
