package bd.dkltd.dscode;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import android.content.Intent;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;
import android.widget.Toast;
import bd.dkltd.dscode.MyFileAdapter.MyFileViewHolder;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import java.util.ArrayList;

public class MyFileAdapter extends RecyclerView.Adapter<MyFileAdapter.MyFileViewHolder> {

    private Context context;
	private ArrayList<File> allFiles;
    private ClickListener clickListener;

    public MyFileAdapter(Context context, ArrayList<File> allFiles) {
        this.context = context;
        this.allFiles = allFiles;
    }

    public void setOnFileClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }
	
	@Override
	public MyFileAdapter.MyFileViewHolder onCreateViewHolder(ViewGroup parent, int p2) {		
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_path_rcv,parent,false);
		return new MyFileViewHolder(v);
	}

	@Override
	public void onBindViewHolder(MyFileAdapter.MyFileViewHolder viewHolder, int position) {
        String localPath = allFiles.get(position).getAbsolutePath();
		File selectedFile = new File(localPath);
        String nameOfFile = selectedFile.getName();
        viewHolder.getTv1().setText(nameOfFile);
        fileIconManager(viewHolder,selectedFile);
	}

    private void fileIconManager(MyFileAdapter.MyFileViewHolder viewHolder, File selectedFile) {
        if(selectedFile.isDirectory()) {
            viewHolder.getImg1().setImageResource(R.drawable.ic_folder);
        } else {
            viewHolder.getImg1().setImageResource(R.drawable.ic_file_document);
        }
    }
    

	@Override
	public int getItemCount() {
		return allFiles.size();
	}
    
    public class MyFileViewHolder extends RecyclerView.ViewHolder implements OnClickListener,OnLongClickListener {

        @Override
        public void onClick(View v1) {
            clickListener.onItemClick(getAdapterPosition(),v1);
        }

        @Override
        public boolean onLongClick(View v1) {
            clickListener.onItemLongClick(getAdapterPosition(),v1);
            return true;
        }

		
		private final TextView tv1;
		private final ImageView img1;
		
		public MyFileViewHolder(View eachView) {
			super(eachView);
			tv1 = eachView.findViewById(R.id.pathRcvTv1);
			img1 = eachView.findViewById(R.id.pathRcvImgv1);
            eachView.setOnClickListener(this);
            eachView.setOnLongClickListener(this);
		}

        public TextView getTv1() {
            return tv1;
        }

        public ImageView getImg1() {
            return img1;
        }
	}
    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }
}
