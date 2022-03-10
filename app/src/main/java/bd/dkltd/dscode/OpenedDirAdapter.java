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
    private static OptionClickListener optionClickListener;
    private Boolean[] expanded;

    private ArrayList<File> sortedFileArray;

    public OpenedDirAdapter(Context cntx, ArrayList<SrcPaths> srcPathsAndName) {
        this.cntx = cntx;
        this.srcPathsAndName = srcPathsAndName;
        int boolArraySize = srcPathsAndName.size();
        expanded = new Boolean[boolArraySize];
        Arrays.fill(expanded, Boolean.FALSE);
    }

    public void setExpanded(boolean expanded, int position) {
        this.expanded[position] = expanded;
    }

    public boolean getExpanded(int position) {
        return expanded[position];
    }

    public static void setOnOptionClickListener(OptionClickListener optionClickListener) {
        OpenedDirAdapter.optionClickListener = optionClickListener;
    }

    @Override
    public OpenedDirAdapter.ODAHolder onCreateViewHolder(ViewGroup parent, int p2) {
        View newView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_dir_rcv, parent, false);
        sortedFileArray = new ArrayList<File>();
        return new ODAHolder(newView);
    }

    @Override
    public void onBindViewHolder(OpenedDirAdapter.ODAHolder viewHolder, final int position) {
        String valueOfPath = srcPathsAndName.get(position).getPathSource();
        File selectedFile = new File(valueOfPath);
        String nameOfFile = selectedFile.getName();
        viewHolder.getFolderName().setText(nameOfFile);
        //setup recyclerview
        File[] allFile = selectedFile.listFiles();
        FileSorter fs = new FileSorter(allFile);
        fs.sortFilesByDirectory();
        sortedFileArray = fs.getSortedFileArray();
        if (hasFile(sortedFileArray)) {
            setNoFileVisibility(viewHolder, false);
            dislayToRecyclerView(viewHolder);
        } else {
            setNoFileVisibility(viewHolder, true);
        }
        //Handle expand collapse
        final RelativeLayout rl = viewHolder.getRl();
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

    }

    private boolean hasFile(ArrayList<File> array) {
        if (array.size() > 0) {
            return true;
        } else {
            return false;    
        }
    }

    private void setNoFileVisibility(OpenedDirAdapter.ODAHolder viewHolder, boolean p1) {
        RecyclerView nrcv = viewHolder.getNestedRcv();
        TextView noFile = viewHolder.getNoFileTV();
        if (p1) {
            nrcv.setVisibility(View.GONE);
            noFile.setVisibility(View.VISIBLE);
        } else {
            noFile.setVisibility(View.GONE);
            nrcv.setVisibility(View.VISIBLE);
        }
    }

    private void dislayToRecyclerView(OpenedDirAdapter.ODAHolder viewHolder) {
        //Setup RecyclerView
        RecyclerView nrcv = viewHolder.getNestedRcv();
        nrcv.setHasFixedSize(true);
        nrcv.setLayoutManager(new LinearLayoutManager(cntx));
        NestedDirAdapter nda = new NestedDirAdapter(cntx, sortedFileArray);
        nrcv.setAdapter(nda);
    }

    @Override
    public int getItemCount() {
        return srcPathsAndName.size();
    }


    public class ODAHolder extends RecyclerView.ViewHolder {
        private ImageView addIcon,infoIcon,crossIcon;
        private final ImageView folderIcon,arrowIcon;
        private final TextView folderName,noFileTV;
        private final RelativeLayout rl;
        private final RecyclerView nestedRcv;
        public ODAHolder(View view) {
            super(view);  
            //listeners
            View.OnClickListener clickListenerForOptions = new View.OnClickListener() {

                @Override
                public void onClick(View v2) {
                    switch (v2.getId()) {
                        case R.id.dirRcvAddIcon:
                            try {
                                optionClickListener.onAddIconClick(getAdapterPosition(), v2);
                            } catch (NullPointerException e) {

                            }
                            break;
                        case R.id.dirRcvInfoIcon:
                            try {
                                optionClickListener.onInfoIconClick(getAdapterPosition(), v2);
                            } catch (NullPointerException e) {

                            }
                            break;
                        case R.id.dirRcvCrossIcon:
                            try {
                                optionClickListener.onCrossIconClick(getAdapterPosition(), v2);
                            } catch (NullPointerException e) {

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
            addIcon.setOnClickListener(clickListenerForOptions);
            infoIcon.setOnClickListener(clickListenerForOptions);
            crossIcon.setOnClickListener(clickListenerForOptions);
        }

        public RecyclerView getNestedRcv() {
            return nestedRcv;
        }

        public TextView getNoFileTV() {
            return noFileTV;
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

    public interface OptionClickListener {
        void onAddIconClick(int position, View v);
        void onInfoIconClick(int position, View v);
        void onCrossIconClick(int position, View v);
    }
}
