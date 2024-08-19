package www.linwg.org.app;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import www.linwg.org.lib.LCardView;

public class TestActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test3, parent, false);
                return new RecyclerView.ViewHolder(itemView) {
                };
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                View tv = holder.itemView.findViewById(R.id.tv);
                View iv = holder.itemView.findViewById(R.id.iv);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iv.setVisibility(iv.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                    }
                });
                LCardView card = holder.itemView.findViewById(R.id.card);
                if (holder.getLayoutPosition() == 0) {
                    float bottomShadowDecrement = card.getBottomShadowDecrement();
                    float topShadowDecrement = card.getTopShadowDecrement();
                    float leftShadowDecrement = card.getLeftShadowDecrement();
                    float rightShadowDecrement = card.getRightShadowDecrement();
                    float startDecrement = card.getShadowSize() * 0.8f;
                    card.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {

                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                card.properties()
                                        .shadowAlpha(120)
                                        .leftShadowDecrement(startDecrement)
                                        .bottomShadowDecrement(startDecrement)
                                        .rightShadowDecrement(startDecrement)
                                        .topShadowDecrement(startDecrement);
                            }
                            if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) {
                                card.properties()
                                        .shadowAlpha(80)
                                        .leftShadowDecrement(leftShadowDecrement)
                                        .bottomShadowDecrement(bottomShadowDecrement)
                                        .rightShadowDecrement(rightShadowDecrement)
                                        .topShadowDecrement(topShadowDecrement);
                            }
                            return true;
                        }
                    });
                    card.setOnClickListener(null);
                } else {
                    card.setOnTouchListener(null);
                    card.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            float bottomShadowDecrement = card.getBottomShadowDecrement();
                            float startDecrement = card.getShadowSize() * 0.5f;
                            ValueAnimator animator = ValueAnimator.ofFloat(startDecrement, bottomShadowDecrement);
                            animator.setDuration(200);
                            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                                    card.properties().bottomShadowDecrement((Float) animator.getAnimatedValue());
                                }
                            });
                            animator.start();
                        }
                    });
                }
            }

            @Override
            public int getItemCount() {
                return 100;
            }
        });
    }
}
