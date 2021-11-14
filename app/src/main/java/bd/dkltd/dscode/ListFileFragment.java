package bd.dkltd.dscode;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.widget.Toast;
import android.widget.LinearLayout;

public class ListFileFragment extends Fragment {

    private View fragmentView;
    private String rootPath;
    private RecyclerView rcv;
    private LinearLayout ll;
    private OnPathBackListener onPathBackListener;

    public void setOnPathBackListener(OnPathBackListener onPathBackListener) {
        this.onPathBackListener = onPathBackListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_listfile, container, false);
        init();
        return fragmentView;
    }

    private void init() {
        //Retrieve path
        rootPath = "storage/emulated/0/";
        String recievedPath = getArguments().getString("dirPath", rootPath);   
        try {
            onPathBackListener.onPathBack(recievedPath);
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        // File operation
        File rootDir = new File(recievedPath);
        File[] allFilesAndDir = rootDir.listFiles();
        //init
        rcv = fragmentView.findViewById(R.id.rcvListFile1);
        ll = fragmentView.findViewById(R.id.noFileLayId);
        //Check empty directory
        if (emptyChecker(allFilesAndDir)) {
            Arrays.sort(allFilesAndDir, new Comparator<File>() {
                    @Override
                    public int compare(File p1, File p2) {
                        return p1.getName().compareTo(p2.getName());
                    }
                });

            MyFileAdapter myFA = new MyFileAdapter(getActivity(), allFilesAndDir);
            myFA.setOnFileItemClicklistener(new MyFileAdapter.FileItemClickListener(){
                    @Override
                    public void onFileItemClick(File selectedFile) {
                        if (selectedFile.isDirectory()) {     
                            String dirPath =  selectedFile.getAbsolutePath();
                            Bundle bundle = new Bundle();
                            bundle.putString("dirPath", dirPath);
                            FragmentManager fm = getFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            ListFileFragment fragment = new ListFileFragment();
                            fragment.setOnPathBackListener(new ListFileFragment.OnPathBackListener(){

                                    @Override
                                    public void onPathBack(String recievedPath) {
                                        Toast.makeText(getActivity(), recievedPath, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            fragment.setArguments(bundle);
                            ft.replace(R.id.frLayId, fragment);
                            ft.addToBackStack(null);
                            ft.commit();

                        } else {
                            String filePath = selectedFile.getAbsolutePath();
                            Toast.makeText(getActivity(), filePath, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            // RecyclerView
            rcv.setLayoutManager(new LinearLayoutManager(getActivity()));
            rcv.setAdapter(myFA);
        } else {
            //Do something in here
            rcv.setVisibility(View.GONE);
            ll.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), "No Files Found", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean emptyChecker(File[] allFilesAndDir) {
        if (allFilesAndDir.length == 0) {
            return false;
        } else {
            return true;
        }

    }

    public interface OnPathBackListener {
        public void onPathBack(String recievedPath);
    }

}
