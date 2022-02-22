package bd.dkltd.dscode;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;
import android.view.LayoutInflater;
import bd.dkltd.dscode.NestedDirAdapter.NestedViewHolder;
import android.widget.Toast;

public class NestedDirAdapter extends RecyclerView.Adapter<NestedDirAdapter.NestedViewHolder> {
    
    private Context cntx;
    private ArrayList<File> fileArray;

    public NestedDirAdapter(Context cntx, ArrayList<File> fileArray) {
        this.cntx = cntx;
        this.fileArray = fileArray;
    }

    @Override
    public NestedDirAdapter.NestedViewHolder onCreateViewHolder(ViewGroup parent, int p2) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_nested_dir_rcv,parent,false);
        return new NestedViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(NestedDirAdapter.NestedViewHolder viewHoler, final int position) {
        String name = fileArray.get(position).getName();
        viewHoler.getFolderName().setText(name);
        setIcon(viewHoler,position);
    }

    private void setIcon(NestedDirAdapter.NestedViewHolder viewHoler, int position) {
        File selectedFile = new File(fileArray.get(position).getAbsolutePath());
        if (!selectedFile.isDirectory()) {
            viewHoler.getFolderIcon().setImageResource(R.drawable.ic_file_document);
        }
    }

    @Override
    public int getItemCount() {
        return fileArray.size();
    }

    private static FolderClickListener viewClickListener;
    private static OptionsClickListener optionClickListener;

    public static void setOnViewClickListener(FolderClickListener viewClickListener) {
        NestedDirAdapter.viewClickListener = viewClickListener;
    }    

    public static void setOnOptionClickListener(OptionsClickListener optionClickListener) {
        NestedDirAdapter.optionClickListener = optionClickListener;
    }

    public class NestedViewHolder extends RecyclerView.ViewHolder {
        private ImageView infoIcon,menuIcon;
        private final ImageView arrowIcon,folderIcon;
        private final TextView folderName;
        private final RelativeLayout rl;
        private final FrameLayout fl;
        public NestedViewHolder(View newView) {
            super(newView);

            // listners
            View.OnClickListener clickListenerForView = new View.OnClickListener() {
                
                @Override
                public void onClick(View v1) {
                    try {
                        viewClickListener.onFolderClick(getAdapterPosition(), v1);
                    } catch (NullPointerException e) {
                        Toast.makeText(cntx,"error " + e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            };
            View.OnLongClickListener longClickListenerForView = new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v1) {
                    try {
                        viewClickListener.onFolderLongClick(getAdapterPosition(),v1);
                    } catch (NullPointerException e) {
                        Toast.makeText(cntx,"error " + e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            };
            View.OnClickListener clickListenerForOptions = new View.OnClickListener() {

                @Override
                public void onClick(View v2) {
                    switch (v2.getId()) {
                        case R.id.nestedSVInfoIcon:
                            try {
                                optionClickListener.onInfoIconClick(getAdapterPosition(), v2);
                            } catch (NullPointerException e) {
                                Toast.makeText(cntx,"error " + e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case R.id.nestedSVMenuIcon:
                            try {
                                optionClickListener.onMenuIconClick(getAdapterPosition(),v2);
                            } catch (NullPointerException e) {
                                Toast.makeText(cntx,"error " + e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                            break;
                        default: //do nothing
                    }
                }
            };

            //init everything
            infoIcon = newView.findViewById(R.id.nestedSVInfoIcon);
            menuIcon = newView.findViewById(R.id.nestedSVMenuIcon);
            arrowIcon = newView.findViewById(R.id.nestedSVDropdownIcon);
            folderIcon = newView.findViewById(R.id.nestedSVfolderIcon);
            folderName = newView.findViewById(R.id.nestedSVFolderName);
            rl = newView.findViewById(R.id.nestedSVRelativeLayout1);
            fl = newView.findViewById(R.id.nestedSVFrameLayout1);

            //set listner
            newView.setOnClickListener(clickListenerForView);
            newView.setOnLongClickListener(longClickListenerForView);
            infoIcon.setOnClickListener(clickListenerForOptions);
            menuIcon.setOnClickListener(clickListenerForOptions);
        }

        public RelativeLayout getRl() {
            return rl;
        }

        public FrameLayout getFl() {
            return fl;
        }

        public ImageView getArrowIcon() {
            return arrowIcon;
        }

        public ImageView getFolderIcon() {
            return folderIcon;
        }

        public TextView getFolderName() {
            return folderName;
        }
    }

    public interface FolderClickListener {
        void onFolderClick(int poition, View v1);
        boolean onFolderLongClick(int poition, View v1);
    }

    public interface OptionsClickListener {
        void onInfoIconClick(int poition, View v1);
        void onMenuIconClick(int poition, View v1);
    }
}
