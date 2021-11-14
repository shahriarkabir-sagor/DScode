package bd.dkltd.dscode;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import android.database.Cursor;
import android.content.SharedPreferences;
import android.widget.Toast;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ListPathFragment extends Fragment {
    
    private View fragmentView;
    private String iPath;
    private MyDbHelper db_helper;
    private ArrayList<Paths> pathRcrd;
    private Cursor rowlist;
	private RecyclerView rcv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_listpath, container, false);
        init();
        return fragmentView;
    }

    private void init() {
        // Get internal path
        iPath = getArguments().getString("internalPath");
        
        // Database
        db_helper = new MyDbHelper(getActivity());
        db_helper.setInternalPath(iPath);
		rowlist = db_helper.fetchRow();
        
        //path record
		pathRcrd = new ArrayList<Paths>();
        //Check Permission
        if (checkPerms()) {
            pathInit();
        } else{
            updateRcrd();
		}
        //Array of images
        int[] img = {R.drawable.ic_folder_home,R.drawable.ic_folder};
        //initialize filepath adapter
        FilePathAdapter fpa = new FilePathAdapter(getActivity(),img,pathRcrd);
        fpa.setOnItemClickListener(new FilePathAdapter.ClickListener() {

                @Override
                public void onItemClick(int position, View v) {
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putString("dirPath",iPath);
                    ListFileFragment fragment = new ListFileFragment();
                    fragment.setOnPathBackListener(new ListFileFragment.OnPathBackListener(){

                            @Override
                            public void onPathBack(String recievedPath) {
                                Toast.makeText(getActivity(),recievedPath,Toast.LENGTH_SHORT).show();
                            }
                        });
                    fragment.setArguments(bundle);
                    ft.replace(R.id.frLayId, fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }

                @Override
                public void onItemLongClick(int position, View v) {
                    
                }
            });
        //initialize recyclerview
        rcv = fragmentView.findViewById(R.id.activity_RecyclerView);
        rcv.setAdapter(fpa);
		rcv.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
    
    private boolean checkPerms() {
        int res = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (res == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    
    private void pathInit() {
        if (rowlist.getCount() == 0) {
            long rowId = db_helper.insertRow("Internal",iPath);
            if (rowId == -1) {
                Toast.makeText(getActivity(), "Row insertion failed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Row successfully inserted", Toast.LENGTH_SHORT).show();
                updateRcrd();
            }
        } else {
            //db has data
            updateRcrd();
        }
    }

    private void updateRcrd() {
        rowlist = db_helper.fetchRow();
        if (rowlist.getCount() > 0) {
            while(rowlist.moveToNext()) {
                String pathName = rowlist.getString(1);
                String pathValue = rowlist.getString(2);
                Paths eachPath = new Paths(pathName,pathValue);
                pathRcrd.add(eachPath);
            }
		}
    }
}
