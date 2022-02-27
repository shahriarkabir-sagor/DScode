package bd.dkltd.dscode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import bd.dkltd.dscode.NestedDirAdapter.NestedViewHolder;

public class NestedDirAdapter extends RecyclerView.Adapter<NestedDirAdapter.NestedViewHolder> {

    private Context cntx;
    private ArrayList<File> fileArray;
    private Boolean[] expanded;
    private ArrayList<File> sortedFileArray;

    private NestedDirAdapter nda;

    public NestedDirAdapter(Context cntx, ArrayList<File> fileArray) {
        this.cntx = cntx;
        this.fileArray = fileArray;
        int boolArraySize = fileArray.size();
        expanded = new Boolean[boolArraySize];
        Arrays.fill(expanded,Boolean.FALSE);
    }
    
    public void setExpanded(boolean expanded, int position) {
        this.expanded[position] = expanded;
    }

    public boolean getExpanded(int position) {
        return expanded[position];
    }

    @Override
    public NestedDirAdapter.NestedViewHolder onCreateViewHolder(ViewGroup parent, int p2) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_nested_dir_rcv, parent, false);
        sortedFileArray = new ArrayList<File>();
        return new NestedViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(NestedDirAdapter.NestedViewHolder viewHolder, final int position) {
        String name = fileArray.get(position).getName();
        viewHolder.getFolderName().setText(name);
        File selectedFile = new File(fileArray.get(position).getAbsolutePath());
        boolean isDirectory = selectedFile.isDirectory();
        if (isDirectory) {
            File[] allFile = selectedFile.listFiles();
            FileSorter fs = new FileSorter(allFile);
            fs.sortFilesByDirectory();
            sortedFileArray = fs.getSortedFileArray();
            if (hasFile(sortedFileArray)) {
                setNoFileVisibility(viewHolder, false);
                diplayToRecyclerView(viewHolder);
            } else {
                setNoFileVisibility(viewHolder, true);
            }
            //Handle expand and collapse
            final RelativeLayout rl = viewHolder.getRelativeLayout();
            final ImageView arrowIcon = viewHolder.getArrowIcon();
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View p1) {
                        boolean isExpanded = getExpanded(position);
                        if (isExpanded) {
                            rl.setVisibility(View.GONE);
                            arrowIcon.setImageResource(R.drawable.ic_menu_right);
                            setExpanded(false, position);
                        } else {
                            rl.setVisibility(View.VISIBLE);
                            arrowIcon.setImageResource(R.drawable.ic_menu_down);
                            setExpanded(true, position);
                        }
                    }
                });
        }else if (!isDirectory) {
            viewHolder.getFolderIcon().setImageResource(R.drawable.ic_file_document);
            viewHolder.getArrowIcon().setVisibility(View.INVISIBLE);
        }
    }

    private boolean hasFile(ArrayList<File> array) {
        if (array.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private void setNoFileVisibility(NestedDirAdapter.NestedViewHolder viewHoler, boolean p1) {
        RecyclerView nrcv = viewHoler.getRecyclerView();
        TextView noFile = viewHoler.getNoFile();
        if (p1) {
            nrcv.setVisibility(View.GONE);
            noFile.setVisibility(View.VISIBLE);
        } else {
            noFile.setVisibility(View.GONE);
            nrcv.setVisibility(View.VISIBLE);
        }
    }

    private void diplayToRecyclerView(NestedDirAdapter.NestedViewHolder viewHoler) {
        RecyclerView nestedRcv = viewHoler.getRecyclerView();
        nestedRcv.setHasFixedSize(true);
        nestedRcv.setLayoutManager(new LinearLayoutManager(cntx));
        nda = new NestedDirAdapter(cntx,sortedFileArray);
        nestedRcv.setAdapter(nda);
    }

    @Override
    public int getItemCount() {
        return fileArray.size();
    }

    public class NestedViewHolder extends RecyclerView.ViewHolder {
        
        private final ImageView infoIcon,menuIcon,arrowIcon,folderIcon;
        private final TextView folderName,noFile;
        private final RelativeLayout rl;
        private final RecyclerView recyclerView;
        
        public NestedViewHolder(View newView) {
            super(newView);
            
            // ---------------------------
            //listeners object
            // ---------------------------
            View.OnClickListener clickListenerForOptions = new View.OnClickListener() {

                //option click listener
                @Override
                public void onClick(View p1) {
                    
                }
            };

            // ---------------
            //init everything
            infoIcon = newView.findViewById(R.id.nestedSVInfoIcon);
            menuIcon = newView.findViewById(R.id.nestedSVMenuIcon);
            arrowIcon = newView.findViewById(R.id.nestedSVDropdownIcon);
            folderIcon = newView.findViewById(R.id.nestedSVfolderIcon);
            folderName = newView.findViewById(R.id.nestedSVFolderName);
            rl = newView.findViewById(R.id.nestedSVRelativeLayout1);
            recyclerView = newView.findViewById(R.id.nestedDirRcvRecyclerView1);
            noFile = newView.findViewById(R.id.nestedDirRcvTextView1);
            
            //set listeners
            infoIcon.setOnClickListener(clickListenerForOptions);
            menuIcon.setOnClickListener(clickListenerForOptions);
        }

        public RecyclerView getRecyclerView() {
            return recyclerView;
        }

        public TextView getNoFile() {
            return noFile;
        }

        public ImageView getInfoIcon() {
            return infoIcon;
        }

        public ImageView getMenuIcon() {
            return menuIcon;
        }

        public RelativeLayout getRelativeLayout() {
            return rl;
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
}
