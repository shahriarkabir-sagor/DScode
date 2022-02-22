package bd.dkltd.dscode;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;
import androidx.recyclerview.widget.LinearLayoutManager;

public class NestedDirFragment extends Fragment {

    private View fView;
    private RecyclerView rcv;
    private TextView tv;
    private String pathFromArgs;
    private File parentDir;
    private NestedDirAdapter nda;

    private ArrayList<File> sortedFileArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Retrieve path from bundle
        pathFromArgs = getArguments().getString("dirPath2", null);
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fView = inflater.inflate(R.layout.fragment_nested_dir, container, false);
        init();
        return fView;
    }

    private void init() {
        //initiate views
        rcv = fView.findViewById(R.id.nestedDirRecyclerView1);
        tv = fView.findViewById(R.id.nestedDirTextView1);
        // File operation
        parentDir = new File(pathFromArgs);
        File[] nestedFilesAndDirs = parentDir.listFiles();
        FileSorter fs = new FileSorter(nestedFilesAndDirs);
        fs.filterHiddenDirs();
        fs.sortFilesByDirectory();
        sortedFileArray = fs.getSortedFileArray();
        //check if directory has files
        if (hasFile(sortedFileArray)) {
            setNoFileVisibilty(false);
            displayToRecyclerView();
            
        } else {
            setNoFileVisibilty(true);
        }
    }
    private boolean hasFile(ArrayList<File> array) {
        if (array.size() > 0) {
            return true;
        } else {
            return false;    
        }
    }
    
    private void setNoFileVisibilty(boolean p0) {
        if (p0) {
            rcv.setVisibility(View.GONE);
            tv.setVisibility(View.VISIBLE);
        } else {
            tv.setVisibility(View.GONE);
            rcv.setVisibility(View.VISIBLE);
        }
    }
    
    private void displayToRecyclerView() {
        rcv.setHasFixedSize(true);
        rcv.setLayoutManager(new LinearLayoutManager(fView.getContext()));
        nda = new NestedDirAdapter(fView.getContext(),sortedFileArray);
        rcv.setAdapter(nda);
        nda.setOnViewClickListener(new NestedDirAdapter.FolderClickListener() {

                @Override
                public void onFolderClick(int poition, View v1) {
                    
                }

                @Override
                public boolean onFolderLongClick(int poition, View v1) {
                    return false;
                }
            });
        nda.setOnOptionClickListener(new NestedDirAdapter.OptionsClickListener() {

                @Override
                public void onInfoIconClick(int poition, View v1) {
                }

                @Override
                public void onMenuIconClick(int poition, View v1) {
                }
            });
    }
    
    public static NestedDirFragment newInstance(String absPath) {
        NestedDirFragment ndf = new NestedDirFragment();
        Bundle args = new Bundle();
        args.putString("dirPath2",absPath);
        ndf.setArguments(args);
        return ndf;
    }
}
