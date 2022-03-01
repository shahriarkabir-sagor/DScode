package bd.dkltd.dscode;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;

public class MyFileAdapter extends RecyclerView.Adapter<MyFileAdapter.MyFileViewHolder> implements Filterable {


    private Context context;
	private ArrayList<File> allFiles;
    private ArrayList<File> searchableFile;
    private ClickListener clickListener;

    public MyFileAdapter(Context context, ArrayList<File> allFiles) {
        this.context = context;
        this.allFiles = allFiles;
        this.searchableFile = new ArrayList<File>(allFiles);
    }

    public void setOnFileClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

	@Override
	public MyFileAdapter.MyFileViewHolder onCreateViewHolder(ViewGroup parent, int p2) {		
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_path_rcv, parent, false);
		return new MyFileViewHolder(v);
	}

	@Override
	public void onBindViewHolder(MyFileAdapter.MyFileViewHolder viewHolder, int position) {
        String localPath = allFiles.get(position).getAbsolutePath();
		File selectedFile = new File(localPath);
        String nameOfFile = selectedFile.getName();
        viewHolder.getTv1().setText(nameOfFile);
        fileIconManager(viewHolder, selectedFile);
	}

    private void fileIconManager(MyFileAdapter.MyFileViewHolder viewHolder, File selectedFile) {
        if (selectedFile.isDirectory()) {
            viewHolder.getImg1().setImageResource(R.drawable.ic_folder);
        } else {
            viewHolder.getImg1().setImageResource(R.drawable.ic_file_document);
        }
    }


	@Override
	public int getItemCount() {
		return allFiles.size();
	}

    @Override
    public Filter getFilter() {
        return filter;
    }

    // custom filter
    Filter filter = new Filter() {

        @Override
        protected Filter.FilterResults performFiltering(CharSequence keyword) {
            ArrayList<File> searchedArray = new ArrayList<File>();
            if (
            keyword == null ||
            keyword.length() == 0 ||
            keyword.toString().isEmpty()
                ) {
                searchedArray.addAll(allFiles);
            } else {
                for (File singleFile : searchableFile) {
                    if (singleFile.getName().toString().toLowerCase().contains(keyword.toString().toLowerCase())) {
                        searchedArray.add(singleFile);
                    }
                }
            }
            FilterResults fr = new FilterResults();
            fr.values = searchedArray;
            return fr;
        }

        @Override
        protected void publishResults(CharSequence p1, Filter.FilterResults filteredResult) {
            allFiles.clear();
            allFiles.addAll((ArrayList<File>) filteredResult.values);
            notifyDataSetChanged();
        }
    };

    // viewholder class
    public class MyFileViewHolder extends RecyclerView.ViewHolder implements OnClickListener,OnLongClickListener {

		private final TextView tv1;
		private final ImageView img1;

        @Override
        public void onClick(View v1) {
            clickListener.onItemClick(getAdapterPosition(), v1);
        }

        @Override
        public boolean onLongClick(View v1) {
            clickListener.onItemLongClick(getAdapterPosition(), v1);
            return true;
        }

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
