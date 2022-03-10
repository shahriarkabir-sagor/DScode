package bd.dkltd.dscode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

public class FilePathAdapter extends RecyclerView.Adapter<FilePathAdapter.Holder> {

    private Context cntx;
    private ArrayList<SrcPaths> srcPathsAndName;
    private static ClickListener clickListener;

    public FilePathAdapter(Context cntx, ArrayList<SrcPaths> srcPathsAndName) {
        this.cntx = cntx;
        this.srcPathsAndName = srcPathsAndName;
    }

    public static void setOnRecycleItemClickListener(ClickListener clickListener) {
        FilePathAdapter.clickListener = clickListener;
    }

    @Override
    public FilePathAdapter.Holder onCreateViewHolder(ViewGroup parent, int p2) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_path_rcv,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(FilePathAdapter.Holder viewHolder, int position) {
        String nameOfPath = srcPathsAndName.get(position).getPathName();
        viewHolder.getTv().setText(nameOfPath);
    }

    @Override
    public int getItemCount() {
        return srcPathsAndName.size();
    }


    public class Holder extends RecyclerView.ViewHolder implements OnClickListener,OnLongClickListener {

        private final ImageView imgView;
        private final TextView tv;

        @Override
        public void onClick(View v1) {
            clickListener.onItemClick(getAdapterPosition(),v1);
        }

        @Override
        public boolean onLongClick(View v1) {
            clickListener.onItemLongClick(getAdapterPosition(),v1);
            return true;
        }

        public Holder(View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.pathRcvImgv1);
            tv = itemView.findViewById(R.id.pathRcvTv1);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public TextView getTv() {
            return tv;
        }
    }
    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }
}

