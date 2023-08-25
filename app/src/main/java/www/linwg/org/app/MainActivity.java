package www.linwg.org.app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import www.linwg.org.app.R;
import www.linwg.org.lib.LCardView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final LCardView cardCR = findViewById(R.id.cardCR);
        final LCardView cardBg = findViewById(R.id.cardBg);
        SeekBar sbLT = findViewById(R.id.sbLT);
        SeekBar sbRT = findViewById(R.id.sbRT);
        SeekBar sbRB = findViewById(R.id.sbRB);
        SeekBar sbLB = findViewById(R.id.sbLB);
        SeekBar sbAll = findViewById(R.id.sbAll);
        SeekBar sbSW = findViewById(R.id.sbSW);
        sbLT.setOnSeekBarChangeListener(new OnSeekBarChangeAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cardCR.setLeftTopCornerRadius(progress);
            }
        });
        sbRT.setOnSeekBarChangeListener(new OnSeekBarChangeAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cardCR.setRightTopCornerRadius(progress);
            }
        });
        sbLB.setOnSeekBarChangeListener(new OnSeekBarChangeAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cardCR.setLeftBottomCornerRadius(progress);
            }
        });
        sbRB.setOnSeekBarChangeListener(new OnSeekBarChangeAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cardCR.setRightBottomCornerRadius(progress);
            }
        });
        sbAll.setOnSeekBarChangeListener(new OnSeekBarChangeAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cardCR.setCornerRadius(progress);
            }
        });
        sbSW.setOnSeekBarChangeListener(new OnSeekBarChangeAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cardBg.setStrokeWidth(progress);
            }
        });
        CheckBox cbRes = findViewById(R.id.cbRes);
        cbRes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cardBg.setCardBackgroundDrawableRes(isChecked ? R.mipmap.girl : 0);
            }
        });
        CheckBox cbDrawable = findViewById(R.id.cbDrawable);
        cbDrawable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cardBg.setCardBackground(isChecked ? getDrawable(R.drawable.test) : null);
            }
        });

        TextView tvLabel = findViewById(R.id.tvLabel);
        RadioGroup rgGradientDirection = findViewById(R.id.rgGradientDirection);
        RadioGroup rgStart = findViewById(R.id.rgStart);
        RadioGroup rgCenter = findViewById(R.id.rgCenter);
        RadioGroup rgEnd = findViewById(R.id.rgEnd);
        View llColor = findViewById(R.id.llColor);
        CheckBox cbGradient = findViewById(R.id.cbGradient);
        CheckBox cbGradientSync = findViewById(R.id.cbGradientSync);
        cbGradient.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tvLabel.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                rgGradientDirection.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                llColor.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });
        cbGradientSync.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cardBg.setGradientSizeFollowView(isChecked);
            }
        });
        rgGradientDirection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbLTR:
                        cardBg.setGradientDirection(LCardView.LEFT_TO_RIGHT);
                        break;
                    case R.id.rbTTB:
                        cardBg.setGradientDirection(LCardView.TOP_TO_BOTTOM);
                        break;
                    case R.id.rbLTTRB:
                        cardBg.setGradientDirection(LCardView.LEFT_TOP_TO_RIGHT_BOTTOM);
                        break;
                    case R.id.rbLBTRT:
                        cardBg.setGradientDirection(LCardView.LEFT_BOTTOM_TO_RIGHT_TOP);
                        break;
                }
            }
        });
        rgStart.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int startId = checkedId;
                int endId = rgEnd.getCheckedRadioButtonId();
                int centerId = rgCenter.getCheckedRadioButtonId();
                int startColor = startId == R.id.rbRedStart ? Color.RED : startId == R.id.rbGreenStart ? Color.GREEN : Color.BLUE;
                int endColor = endId == R.id.rbRedEnd ? Color.WHITE : endId == R.id.rbGrayEnd ? Color.GRAY : Color.BLACK;
                int centerColor = centerId == R.id.rbRedCenter ? Color.YELLOW : centerId == R.id.rbGreenCenter ? Color.parseColor("#ff00ff") : Color.parseColor("#00ffff");
                cardBg.setGradientColors(startColor, centerColor, endColor);
            }
        });
        rgCenter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int centerId = checkedId;
                int endId = rgEnd.getCheckedRadioButtonId();
                int startId = rgStart.getCheckedRadioButtonId();
                int startColor = startId == R.id.rbRedStart ? Color.RED : startId == R.id.rbGreenStart ? Color.GREEN : Color.BLUE;
                int endColor = endId == R.id.rbRedEnd ? Color.WHITE : endId == R.id.rbGrayEnd ? Color.GRAY : Color.BLACK;
                int centerColor = centerId == R.id.rbRedCenter ? Color.YELLOW : centerId == R.id.rbGreenCenter ? Color.parseColor("#ff00ff") : Color.parseColor("#00ffff");
                cardBg.setGradientColors(startColor, centerColor, endColor);
            }
        });
        rgEnd.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int endId = checkedId;
                int centerId = rgCenter.getCheckedRadioButtonId();
                int startId = rgStart.getCheckedRadioButtonId();
                int startColor = startId == R.id.rbRedStart ? Color.RED : startId == R.id.rbGreenStart ? Color.GREEN : Color.BLUE;
                int endColor = endId == R.id.rbRedEnd ? Color.WHITE : endId == R.id.rbGrayEnd ? Color.GRAY : Color.BLACK;
                int centerColor = centerId == R.id.rbRedCenter ? Color.YELLOW : centerId == R.id.rbGreenCenter ? Color.parseColor("#ff00ff") : Color.parseColor("#00ffff");
                cardBg.setGradientColors(startColor, centerColor, endColor);
            }
        });


        final LCardView cardSS = findViewById(R.id.cardSS);
        final SeekBar sbAlpha = findViewById(R.id.sbAlpha);
        final SeekBar sbR = findViewById(R.id.sbR);
        final SeekBar sbG = findViewById(R.id.sbG);
        final SeekBar sbB = findViewById(R.id.sbB);
        final SeekBar sbSS = findViewById(R.id.sbSS);
        final RadioGroup rgShape = findViewById(R.id.rgShape);
        sbAlpha.setOnSeekBarChangeListener(new OnSeekBarChangeAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cardSS.setShadowAlpha(progress);
            }
        });
        sbR.setOnSeekBarChangeListener(new OnSeekBarChangeAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cardSS.setShadowColor(Color.argb(sbAlpha.getProgress(), sbR.getProgress(), sbG.getProgress(), sbB.getProgress()));
            }
        });
        sbG.setOnSeekBarChangeListener(new OnSeekBarChangeAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cardSS.setShadowColor(Color.argb(sbAlpha.getProgress(), sbR.getProgress(), sbG.getProgress(), sbB.getProgress()));
            }
        });
        sbB.setOnSeekBarChangeListener(new OnSeekBarChangeAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cardSS.setShadowColor(Color.argb(sbAlpha.getProgress(), sbR.getProgress(), sbG.getProgress(), sbB.getProgress()));
            }
        });
        sbSS.setOnSeekBarChangeListener(new OnSeekBarChangeAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cardSS.setShadowSize(progress);
            }
        });
        rgShape.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                cardSS.setShadowFluidShape(checkedId == R.id.rbOne ? LCardView.ADSORPTION : LCardView.LINEAR);
            }
        });

        final LCardView cardEle = findViewById(R.id.cardEle);
        final CheckBox cbAlpha = findViewById(R.id.cbAlpha);
        final CheckBox cbSize = findViewById(R.id.cbSize);
        final SeekBar sbEle = findViewById(R.id.sbEle);
        cbAlpha.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cardEle.setElevationAffectShadowColor(isChecked);
            }
        });
        cbSize.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cardEle.setElevationAffectShadowSize(isChecked);
            }
        });
        sbEle.setOnSeekBarChangeListener(new OnSeekBarChangeAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cardEle.setElevation(progress);
            }
        });

        final LCardView cardOS = findViewById(R.id.cardOS);
        final SeekBar sbLO = findViewById(R.id.sbLO);
        final SeekBar sbRO = findViewById(R.id.sbRO);
        final SeekBar sbTO = findViewById(R.id.sbTO);
        final SeekBar sbBO = findViewById(R.id.sbBO);
        final SeekBar sbFourO = findViewById(R.id.sbFourO);
        final int offset = 100 - cardOS.getShadowSize() / 2;
        sbLO.setOnSeekBarChangeListener(new OnSeekBarChangeAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cardOS.setLeftOffset(progress - offset);
            }
        });
        sbRO.setOnSeekBarChangeListener(new OnSeekBarChangeAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cardOS.setRightOffset(progress - offset);
            }
        });
        sbTO.setOnSeekBarChangeListener(new OnSeekBarChangeAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cardOS.setTopOffset(progress - offset);
            }
        });
        sbBO.setOnSeekBarChangeListener(new OnSeekBarChangeAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cardOS.setBottomOffset(progress - offset);
            }
        });
        sbFourO.setOnSeekBarChangeListener(new OnSeekBarChangeAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cardOS.setShadowOffsetCenter(progress - offset);
            }
        });

        final LCardView cardBook = findViewById(R.id.cardBook);
        final SeekBar sbAngle = findViewById(R.id.sbAngle);
        final CheckBox cbBook = findViewById(R.id.cbBook);
        final CheckBox cbBookShape = findViewById(R.id.cbBookShape);
        cbBook.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cardBook.setLinearBookEffect(isChecked);
            }
        });
        cbBookShape.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cardBook.setShadowFluidShape(isChecked ? LCardView.LINEAR : LCardView.ADSORPTION);
            }
        });
        sbAngle.setOnSeekBarChangeListener(new OnSeekBarChangeAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cardBook.setBookRadius(progress);
            }
        });

        final LCardView cardMesh = findViewById(R.id.cardMesh);
        final CheckBox cbMesh = findViewById(R.id.cbMesh);
        final CheckBox cbMeshShape = findViewById(R.id.cbMeshShape);
        final SeekBar sbCur = findViewById(R.id.sbCur);
        cbMesh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cardMesh.setCurveShadowEffect(isChecked);
                if (isChecked) {
                    cbMeshShape.setChecked(true);
                }
                cbMeshShape.setEnabled(!isChecked);
            }
        });
        cbMeshShape.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cardMesh.setShadowFluidShape(isChecked ? LCardView.LINEAR : LCardView.ADSORPTION);
            }
        });
        sbCur.setOnSeekBarChangeListener(new OnSeekBarChangeAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cardMesh.setCurvature(progress / 10f);
            }
        });

        final LCardView cardSync = findViewById(R.id.cardSync);
        final CheckBox cbSync = findViewById(R.id.cbSync);
        final SeekBar sbCardCN = findViewById(R.id.sbCardCN);
        final SeekBar sbShadowCN = findViewById(R.id.sbShadowCN);
        cbSync.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cardSync.setPaperSyncCorner(isChecked);
            }
        });
        sbCardCN.setOnSeekBarChangeListener(new OnSeekBarChangeAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cardSync.setPaperCorner(progress);
            }
        });
        sbShadowCN.setOnSeekBarChangeListener(new OnSeekBarChangeAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cardSync.setCornerRadius(progress);
            }
        });

        findViewById(R.id.cardLabel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CardListActivity.class));
            }
        });
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
