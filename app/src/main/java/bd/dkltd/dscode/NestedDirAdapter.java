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

public class NestedDirAdapter extends RecyclerView.Adapter<NestedDirAdapter.NestedViewHolder> {

    private Context cntx;
    private ArrayList<File> fileArray;
    private Boolean[] expanded;
    private NestedDirAdapter.FolderClickListener folderClickListener;

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

    public void setOnFolderClickListener(NestedDirAdapter.FolderClickListener folderClickListener) {
        this.folderClickListener = folderClickListener;
    }

    @Override
    public NestedDirAdapter.NestedViewHolder onCreateViewHolder(ViewGroup parent, int p2) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_nested_dir_rcv, parent, false);
        return new NestedViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(NestedDirAdapter.NestedViewHolder viewHoler, int position) {
        String name = fileArray.get(position).getName();
        viewHoler.getFolderName().setText(name);
        File selectedFile = new File(fileArray.get(position).getAbsolutePath());
        if (!selectedFile.isDirectory()) {
            viewHoler.getFolderIcon().setImageResource(R.drawable.ic_file_document);
            viewHoler.getArrowIcon().setVisibility(View.INVISIBLE);
        }
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
            View.OnClickListener clickListenerForView = new View.OnClickListener() {

                private ArrayList<File> sortedFileArray;

                private NestedDirAdapter nda;

                //click listener for view
                @Override
                public void onClick(View v1) {
                    int position = getAdapterPosition();
                    String parentDirPath = fileArray.get(position).getAbsolutePath();
                    File parentDir = new File(parentDirPath);
                    // check file is a dir or not
                    if (parentDir.isDirectory()) {
                        // directory, so proceed
                        File[] allFile = parentDir.listFiles();
                        FileSorter fs = new FileSorter(allFile);
                        fs.sortFilesByDirectory();
                        sortedFileArray = fs.getSortedFileArray();
                        if (hasFile(sortedFileArray)) {
                            setNoFileVisibility(false);
                            displayToRecyclerView();
                        } else {
                            setNoFileVisibility(true);
                        }
                        //This will control collapse and expand
                        try {
                            ExpandList el = new ExpandList(arrowIcon,rl,expanded[position]);
                            folderClickListener.onFolderClick(position, v1, el);
                        } catch (NullPointerException e) {
                            Toast.makeText(cntx, "error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        //file, so open it inside editor
                    }
                }

                private boolean hasFile(ArrayList<File> array) {
                    if (array.size() > 0) {
                        return true;
                    } else {
                        return false;
                    }
                }

                private void setNoFileVisibility(boolean p0) {
                    if(p0) {
                        recyclerView.setVisibility(View.GONE);
                        noFile.setVisibility(View.VISIBLE);
                    } else {
                        noFile.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                }

                private void displayToRecyclerView() {
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(cntx));
                    nda = new NestedDirAdapter(cntx,sortedFileArray);
                    recyclerView.setAdapter(nda);
                    //add listener
                    nda.setOnFolderClickListener(new NestedDirAdapter.FolderClickListener() {

                            @Override
                            public void onFolderClick(int position, View v, ExpandList obj) {
                                // don't need to set recyclerView just control expand collapse
                                if(obj.isExpanded()) {
                                    obj.getRelativeLayout().setVisibility(View.GONE);
                                    nda.setExpanded(false, position);
                                    obj.getDropdownIcon().setImageResource(R.drawable.ic_menu_right);
                                } else {
                                    obj.getRelativeLayout().setVisibility(View.VISIBLE);
                                    nda.setExpanded(true, position);
                                    obj.getDropdownIcon().setImageResource(R.drawable.ic_menu_down);
                                }
                            }

                            @Override
                            public boolean onFolderLongClick(int position, View v) {
                                return false;
                            }
                        });
                }
            };
            View.OnLongClickListener longClickListenerForView = new View.OnLongClickListener() {

                //Long click listener
                @Override
                public boolean onLongClick(View p1) {
                    return false;
                }
            };
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
            newView.setOnClickListener(clickListenerForView);
            newView.setOnLongClickListener(longClickListenerForView);
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
    public interface FolderClickListener {
        void onFolderClick(int position, View v, ExpandList obj);
        boolean onFolderLongClick(int position, View v);
    }
}
