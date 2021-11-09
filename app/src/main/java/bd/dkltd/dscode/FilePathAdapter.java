package bd.dkltd.dscode;

import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;
import android.view.View;
import android.annotation.NonNull;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.ImageView;
import java.util.ArrayList;
import bd.dkltd.dscode.FilePathAdapter.ClickListener;

public class FilePathAdapter extends RecyclerView.Adapter<FilePathAdapter.MyViewHolder> {
	
	private Context context;
	private int[] images;
	private ArrayList<Paths> pathRcrd;
	private static FilePathAdapter.ClickListener clickListener;

	public FilePathAdapter(Context context, int[] images, ArrayList<Paths> pathRcrd) {
		this.context = context;
		this.images = images;
		this.pathRcrd = pathRcrd;
	}

	

	@Override
	public FilePathAdapter.MyViewHolder onCreateViewHolder(ViewGroup p1, int p2) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.layout_path_rcv,p1,false);
		return new MyViewHolder(v);
	}

	@Override
	public void onBindViewHolder(FilePathAdapter.MyViewHolder p1, int i) {
		String pathName =  pathRcrd.get(i).getPathName();
		p1.tv1.setText(pathName);
		if (i == 0) {
			p1.img1.setImageResource(images[0]);
		} else {
			p1.img1.setImageResource(images[1]);
		}
	}

	@Override
	public int getItemCount() {
		return pathRcrd.size();
	}
	
	class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

		@Override
		public void onClick(View v1) {
			clickListener.onItemClick(getAdapterPosition(),v1);
		}

		@Override
		public boolean onLongClick(View v1) {
			clickListener.onItemLongClick(getAdapterPosition(),v1);
			return true;
		}

		
		private TextView tv1;
		private ImageView img1;

		public MyViewHolder(@NonNull View view) {
			super(view);
			tv1 = view.findViewById(R.id.pathRcvTv1);
			img1 = view.findViewById(R.id.pathRcvImgv1);
			view.setOnClickListener(this);
			view.setOnLongClickListener(this);
		}
	}
	public interface ClickListener{
		void onItemClick(int position, View v);
		void onItemLongClick(int position, View v);
	}
	public void setOnItemClickListener(ClickListener clickListener) {
		FilePathAdapter.clickListener = clickListener;
	}
}

