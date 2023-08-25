package www.linwg.org.app;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import www.linwg.org.app.R;
import www.linwg.org.lib.LCardView;

public class CardListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new Ad(this));
    }

    private static class Ad extends RecyclerView.Adapter<VH> {
        private Context context;
        private String[] arr = {
                "属性说明:1.useShadowPool为true时，列表中的着色器以及Bitmap将储存于缓存池中一起使用",
                "如果一个列表中的卡片样式都一致的话，建议开启此功能",
                "2.开启缓存池后，建议同时设置bindLifeCircle为true",
                "bindLifeCircle属性将自动帮助卡片绑定生命周期，避免共用缓存池对象导致内存泄漏",
                "3.如果没有设置bindLifeCircle也没有关系，每张卡片在attach或detach的时候会自动清除缓存池对象的引用（只在useShadowPool为true时有效）",
                "个人估计，理论上这样会增加一点点点点点的查询开销.",
                "其他属性说明:fixedContentWidth/fixedContentHeight",
                "启用这两个属性时卡片的measure结果将会被改变，当卡片与卡片父布局的尺寸都为wrap_content时，使用fixedContentWidth，卡片的尺寸由子view的内容宽高计算获得，否则为0（原计算结果）",
                "卡片properties()方法用于一次性设置多个属性，避免重复创建着色器或Bitmap."
        };

        private Ad(Context context) {
            this.context = context;
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new VH(LayoutInflater.from(context).inflate(R.layout.item_test, parent, false));
        }

        @Override
        public void onBindViewHolder(final VH holder, int position) {
            //ignore
            holder.testView.setText(arr[position% arr.length ]);
        }

        @Override
        public int getItemCount() {
            return 1000;
        }
    }

    private static class VH extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView testView;
        LCardView cardView;

        public VH(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            testView = itemView.findViewById(R.id.testView);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
