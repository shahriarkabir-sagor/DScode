package bd.dkltd.dscode;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import bd.dkltd.dscode.myfragments.MyDialogListViewFragment;
import android.content.DialogInterface;
import bd.dkltd.dscode.myfragments.MyDialogInputPath;

public class ListPathFragment extends Fragment {
    private View fView;
    private MyDbHelper storage_db;
    private Cursor rowList;
    private String internalPath;
    private ArrayList<SrcPaths> pathRcrd;
    private RecyclerView rcView;
    private FilePathAdapter sAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fView = inflater.inflate(R.layout.fragment_listpath, container, false);
        init();
        return fView;
    }

    private void init() {
        storage_db = new MyDbHelper(getActivity()); // pass context
        rowList = storage_db.fetchRowFrom("Storage_Path");
        pathRcrd = new ArrayList<SrcPaths>();
        if (checkPerms()) {
            pathInit();
        } else {
            Toast.makeText(getActivity(), "Need Storage permission", Toast.LENGTH_SHORT).show();
            getPermissionAndUpdate();
        }
        if (checkPerms()) {
            rcView = fView.findViewById(R.id.activity_RecyclerView);
            rcView.setHasFixedSize(true);
            rcView.setLayoutManager(new LinearLayoutManager(fView.getContext()));
            sAdapter = new FilePathAdapter(fView.getContext(), pathRcrd);
            rcView.setAdapter(sAdapter);
            sAdapter.setOnRecycleItemClickListener(new FilePathAdapter.ClickListener() {

                    @Override
                    public void onItemClick(int position, View v) {
                        String absPath = pathRcrd.get(position).getPathSource();
                        ListFileFragment lff = new ListFileFragment();
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.frLayId, lff.newInstance(absPath), "Opening Storage");
                        ft.addToBackStack("inside file browser");
                        ft.commit();
                    }

                    @Override
                    public void onItemLongClick(int position, View v) {
                        final String absPath = pathRcrd.get(position).getPathSource();
                        final String tempName = pathRcrd.get(position).getPathName();
                        MyDialogListViewFragment pathAction = new MyDialogListViewFragment();
                        pathAction.setDialogTitle("Choose Action:");
                        pathAction.setDialogItemId(R.array.path_action);
                        pathAction.setDialogItemClickListener(new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dInterface, int indexPosition) {
                                    switch (indexPosition) {
                                        case 0:
                                            //Modify the path
                                            MyDialogInputPath iPathChooser = new MyDialogInputPath();
                                            iPathChooser.setDialogTitle("Modify path");
                                            iPathChooser.setDialogEtText1(tempName);
                                            iPathChooser.setDialogEtText2(absPath);
                                            iPathChooser.setCancelable(false);
                                            iPathChooser.setOnPositiveListener(new MyDialogInputPath.OnPositiveListener() {

                                                    @Override
                                                    public void onClick(String pathName, String pathValue) {
                                                        
                                                    }
                                                });
                                            iPathChooser.show(getActivity().getFragmentManager(), "Modify path");
                                            break;
                                        case 1:
                                            //Delete the path
                                            int affectedRow =  storage_db.deleteRowFrom("Storage_Path","path_name",tempName);
                                            if (affectedRow > 0) {
                                                Toast.makeText(getActivity(),"Row successfully deleted",Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getActivity(),"Failed to delete",Toast.LENGTH_SHORT).show();
                                            }
                                            break;
                                        default:
                                            //Unknown action
                                            Toast.makeText(getActivity(), "Can't perform this action", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        pathAction.show(getActivity().getFragmentManager(), "show action");
                    }
                });
        }
    }

    private void getPermissionAndUpdate() {
    }

    private void pathInit() {
        if (rowList.getCount() == 0) {
            internalPath = "/storage/emulated/0";
            long rowId = storage_db.insertRow("Storage_Path", "Internal Storage", internalPath);
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
        rowList = storage_db.fetchRowFrom("Storage_Path");
        if (rowList.getCount() > 0) {
            while (rowList.moveToNext()) {          
                //Now Start reading property
                String pathName = rowList.getString(0);
                String pathValue = rowList.getString(1);
                SrcPaths eachPath = new SrcPaths(pathName, pathValue);
                pathRcrd.add(eachPath);
            }
        }
    }

    private boolean checkPerms() {
        int res = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (res == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    public static ListPathFragment newInstance() {
        return new ListPathFragment();
    }
}
