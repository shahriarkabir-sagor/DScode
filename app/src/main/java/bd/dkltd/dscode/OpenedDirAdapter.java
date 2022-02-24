package bd.dkltd.dscode;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import java.io.File;
import bd.dkltd.dscode.OpenedDirAdapter.ODAHolder;
import android.widget.RelativeLayout;
import android.widget.FrameLayout;
import android.widget.Toast;
import java.util.Arrays;
import androidx.recyclerview.widget.LinearLayoutManager;

public class OpenedDirAdapter extends RecyclerView.Adapter<OpenedDirAdapter.ODAHolder> {

    private Context cntx;
    private ArrayList<SrcPaths> srcPathsAndName;
    private static FolderClickListener folderClickListener;
    private static OptionClickListener optionClickListener;
    private Boolean[] expanded;

    public OpenedDirAdapter(Context cntx, ArrayList<SrcPaths> srcPathsAndName) {
        this.cntx = cntx;
        this.srcPathsAndName = srcPathsAndName;
        int boolArraySize = srcPathsAndName.size();
        expanded = new Boolean[boolArraySize];
        Arrays.fill(expanded,Boolean.FALSE);
    }

    public void setExpanded(boolean expanded, int position) {
        this.expanded[position] = expanded;
    }

    public boolean getExpanded(int position) {
        return expanded[position];
    }

    public static void setOnFolderClickListener(FolderClickListener folderClickListener) {
        OpenedDirAdapter.folderClickListener = folderClickListener;
    }

    public static void setOnOptionClickListener(OptionClickListener optionClickListener) {
        OpenedDirAdapter.optionClickListener = optionClickListener;
    }

    @Override
    public OpenedDirAdapter.ODAHolder onCreateViewHolder(ViewGroup parent, int p2) {
        View newView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_dir_rcv, parent, false);
        return new ODAHolder(newView);
    }

    @Override
    public void onBindViewHolder(OpenedDirAdapter.ODAHolder viewHolder, int position) {
        String valueOfPath = srcPathsAndName.get(position).getPathSource();
        File selectedFile = new File(valueOfPath);
        String nameOfFile = selectedFile.getName();
        viewHolder.getFolderName().setText(nameOfFile);
        fileIconManager(viewHolder, selectedFile);
    }

    private void fileIconManager(OpenedDirAdapter.ODAHolder viewHolder, File selectedFile) {
        if (selectedFile.isDirectory()) {
            viewHolder.getFolderIcon().setImageResource(R.drawable.ic_folder);
        } else {
            viewHolder.getFolderIcon().setImageResource(R.drawable.ic_file_document);
        }
    }

    @Override
    public int getItemCount() {
        return srcPathsAndName.size();
    }


    public class ODAHolder extends RecyclerView.ViewHolder {
        private ImageView addIcon,infoIcon,crossIcon;
        private final ImageView folderIcon,arrowIcon;
        private final TextView folderName;
        private final RelativeLayout rl;
        private ArrayList<File> sortedFileArray;
        private RecyclerView nestedRcv;
        private TextView noFileTV;
        private NestedDirAdapter nda;
        public ODAHolder(View view) {
            super(view);  
            //listeners
            View.OnClickListener clickListenerForView = new View.OnClickListener() {

                @Override
                public void onClick(View v1) {
                    //fetch all files here
                    String parentDirPath = srcPathsAndName.get(getAdapterPosition()).getPathSource();
                    File parentDir = new File(parentDirPath);
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
                    try {
                        ExpandList obj = new ExpandList(arrowIcon,rl,expanded[getAdapterPosition()]);
                        folderClickListener.onFolderClick(getAdapterPosition(),v1,obj);
                    } catch(NullPointerException e) {
                        Toast.makeText(cntx,"error " + e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

                private void setNoFileVisibility(boolean p0) {
                    if (p0) {
                        nestedRcv.setVisibility(View.GONE);
                        noFileTV.setVisibility(View.VISIBLE);
                    } else {
                        noFileTV.setVisibility(View.GONE);
                        nestedRcv.setVisibility(View.VISIBLE);
                    }
                }

                private void displayToRecyclerView() {
                    //manage recyclerview
                    nestedRcv.setHasFixedSize(true);
                    nestedRcv.setLayoutManager(new LinearLayoutManager(cntx));
                    nda = new NestedDirAdapter(cntx,sortedFileArray);
                    nestedRcv.setAdapter(nda);
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

                private boolean hasFile(ArrayList<File> array) {
                    if (array.size() > 0) {
                        return true;
                    } else {
                        return false;    
                    }
                }
            };
            View.OnLongClickListener longClickListenerForView = new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v1) {
                    try {
                        folderClickListener.onFolderLongClick(getAdapterPosition(),v1);
                    } catch(NullPointerException e) {
                        
                    }
                    return true;
                }
            };
            View.OnClickListener clickListenerForOptions = new View.OnClickListener() {

                @Override
                public void onClick(View v2) {
                    switch (v2.getId()) {
                        case R.id.dirRcvAddIcon:
                            try {
                                optionClickListener.onAddIconClick(getAdapterPosition(), v2);
                            } catch(NullPointerException e) {

                            }
                            break;
                        case R.id.dirRcvInfoIcon:
                            try {
                                optionClickListener.onInfoIconClick(getAdapterPosition(), v2);
                            } catch(NullPointerException e) {

                            }
                            break;
                        case R.id.dirRcvCrossIcon:
                            try {
                                optionClickListener.onCrossIconClick(getAdapterPosition(), v2);
                            } catch(NullPointerException e) {

                            }
                            break;
                        default: //do nothing for now
                    }
                }
            };

            //init
            rl = view.findViewById(R.id.dirRcvRelativeLayout1);
            nestedRcv = view.findViewById(R.id.dirRcvRecyclerView1);
            noFileTV = view.findViewById(R.id.dirRcvTextView1);
            addIcon = view.findViewById(R.id.dirRcvAddIcon);
            infoIcon = view.findViewById(R.id.dirRcvInfoIcon);
            crossIcon = view.findViewById(R.id.dirRcvCrossIcon);
            arrowIcon = view.findViewById(R.id.dirRcvArrowRightIcon);
            folderIcon = view.findViewById(R.id.dirRcvFolderIcon);
            folderName = view.findViewById(R.id.dirRcvFolderName);  

            //set listener
            view.setOnClickListener(clickListenerForView);
            view.setOnLongClickListener(longClickListenerForView);
            addIcon.setOnClickListener(clickListenerForOptions);
            infoIcon.setOnClickListener(clickListenerForOptions);
            crossIcon.setOnClickListener(clickListenerForOptions);
        }

        public ImageView getArrowIcon() {
            return arrowIcon;
        }

        public RelativeLayout getRl() {
            return rl;
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
    
    public interface OptionClickListener {
        void onAddIconClick(int position, View v);
        void onInfoIconClick(int position, View v);
        void onCrossIconClick(int position, View v);
    }
}
