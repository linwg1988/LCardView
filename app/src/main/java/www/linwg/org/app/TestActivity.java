package www.linwg.org.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new Ad(this));
    }

    private static class Ad extends RecyclerView.Adapter<VH>{
        private Context context;

        private Ad(Context context) {
            this.context = context;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_test, parent, false);
            return new VH(view);
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            holder.imageView.setImageResource(R.drawable.ic_launcher_background);
            holder.testView.setText(""+holder.getLayoutPosition());
        }

        @Override
        public int getItemCount() {
            return 1000;
        }
    }

    private static class VH extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView testView;
        public VH(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            testView = itemView.findViewById(R.id.testView);
        }
    }
}
