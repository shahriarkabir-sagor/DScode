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

public class MyFileAdapter extends RecyclerView.Adapter<MyFileAdapter.MyFileViewHolder> {

    private Context context;
	private File[] filesAndFolders;
    private FileItemClickListener onFileItemClicklistener;

	public MyFileAdapter(Context context, File[] filesAndFolders) {
		this.context = context;
		this.filesAndFolders = filesAndFolders;
	}

    public void setOnFileItemClicklistener(FileItemClickListener onFileItemClicklistener) {
        this.onFileItemClicklistener = onFileItemClicklistener;
    }
	
	@Override
	public MyFileAdapter.MyFileViewHolder onCreateViewHolder(ViewGroup p1, int p2) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.layout_path_rcv,p1,false);
		return new MyFileViewHolder(v);
	}

	@Override
	public void onBindViewHolder(MyFileAdapter.MyFileViewHolder p1, int position) {
		final File selectedFile = filesAndFolders[position];
		p1.tv1.setText(selectedFile.getName());
		if (selectedFile.isDirectory()){
			p1.img1.setImageResource(R.drawable.ic_folder);
		} else {
			p1.img1.setImageResource(R.drawable.ic_file_document);
		}
		p1.itemView.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1) {
                    onFileItemClicklistener.onFileItemClick(selectedFile);
				}
			});
	}

	@Override
	public int getItemCount() {
		return filesAndFolders.length;
	}
    public interface FileItemClickListener {
        public void onFileItemClick(File selectedFile);
    }
    class MyFileViewHolder extends RecyclerView.ViewHolder {
		
		private TextView tv1;
		private ImageView img1;
		
		public MyFileViewHolder(View view) {
			super(view);
			tv1 = view.findViewById(R.id.pathRcvTv1);
			img1 = view.findViewById(R.id.pathRcvImgv1);
		}
		
	}
}
