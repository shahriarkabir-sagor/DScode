package bd.dkltd.dscode;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import bd.dkltd.dscode.myfragments.EditNameDFragment;
import bd.dkltd.dscode.myfragments.MyDialogListViewFragment;
import java.io.File;
import java.util.ArrayList;
import android.content.Context;

public class ListFileFragment extends Fragment {

    private View fView;
    private String pathFromArgs;
    private Button selectDirBtn;
    private File parentDir;
    private LinearLayout ll;
    private RecyclerView rcView;
    private MyFileAdapter fAdapter;
    private FragmentManager fm;
    private OnPathReceivedCallback onPathReceivedCallback;

    public void setOnPathReceived(OnPathReceivedCallback onPathReceived) {
        this.onPathReceivedCallback = onPathReceived;
    }

    public static ListFileFragment newInstance(String absolutePath) {
        ListFileFragment lff = new ListFileFragment();
        Bundle args = new Bundle();
        args.putString("dirPath", absolutePath);
        lff.setArguments(args);
        return lff;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //Retrieve path from bundle
        pathFromArgs = getArguments().getString("dirPath", null);
        onPathReceivedCallback.onPathReceived(pathFromArgs);
    } 

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fView = inflater.inflate(R.layout.fragment_listfile, container, false);
        init();
        return fView;
    }

    private void init() {
        //initialize views
        ll = fView.findViewById(R.id.emptyLl);
        rcView = fView.findViewById(R.id.rcvListFile1);
        selectDirBtn = fView.findViewById(R.id.selDirBtn);
        fm = getActivity().getSupportFragmentManager();
        // File operation
        parentDir = new File(pathFromArgs);
        File[] allFilesAndDirs = parentDir.listFiles();
        fileOperations(allFilesAndDirs);
        //add path to Button
        initBtn(parentDir);
    }

    private void initBtn(File currentDirectory) {
        if (currentDirectory.exists()) {
            final String path = currentDirectory.getAbsolutePath();
            final String dirName = currentDirectory.getName();
            //Now set the path to Button
            selectDirBtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View p1) {
                        MyDbHelper db_helper = new MyDbHelper(getActivity());
                        long rowId = db_helper.insertRow("Opened_directory", dirName, path);
                        if (rowId == -1) {
                            Toast.makeText(getActivity(), "Failed to open directory " + dirName, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Directory " + dirName + " successfully opened", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        }/** else {
         Toast.makeText(getActivity(),"Can't open this folder",Toast.LENGTH_LONG).show();
         } */
    }

    private void fileOperations(File[] allFilesAndDir) {
        if (!emptyChecker(allFilesAndDir)) {
            //Differentiate folders and Files and sort them
            FileSorter fs = new FileSorter();
            fs.setListOfFiles(allFilesAndDir);
            fs.filterHiddenDirs();
            fs.sortFilesByNameAscIgnoreCase();
            fs.sortFilesByDirectory();
            ArrayList<File> newFileList = fs.getSortedFileArray();
            // check sorted fileArry is empty or not
            if (newFileList.size() > 0) {
                noFileMethod(false);
                //Then pass the sorted file List to recycleHandler
                recycleHandler(newFileList);
            } else {
                noFileMethod(true);
            }

        } else {
            noFileMethod(true);
        }
    }

    private void noFileMethod(boolean p0) {
        if (p0) {
            rcView.setVisibility(View.GONE);
            ll.setVisibility(View.VISIBLE);
        } else {
            ll.setVisibility(View.GONE);
            rcView.setVisibility(View.VISIBLE);
        }
    }



    private void recycleHandler(final ArrayList<File> newFileList) {
        rcView.setHasFixedSize(true);
        rcView.setLayoutManager(new LinearLayoutManager(fView.getContext()));
        fAdapter = new MyFileAdapter(fView.getContext(), newFileList);
        rcView.setAdapter(fAdapter);
        fAdapter.setOnFileClickListener(new MyFileAdapter.ClickListener() {

                @Override
                public void onItemClick(int position, View v) {
                    String absPath = newFileList.get(position).getAbsolutePath();
                    File clickedFile = new File(absPath);
                    String dirName = newFileList.get(position).getName();
                    if (checkFile(clickedFile)) {
                        ListFileFragment lff = new ListFileFragment();
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.frLayId, lff.newInstance(absPath), "Opening directoy " + dirName);
                        ft.addToBackStack("inside file browser");
                        ft.commit();
                    } else {
                        //Here means it is a file
                        Toast.makeText(getActivity(), "Can't open file: " + dirName, Toast.LENGTH_SHORT).show();
                    }
                }

                private boolean checkFile(File clickedFile) {
                    if (clickedFile.isDirectory()) {
                        return true;  
                    } else {
                        return false;
                    }
                }

                @Override
                public void onItemLongClick(final int position, View v) {
                    String absPath = newFileList.get(position).getAbsolutePath();
                    final File clickedFile = new File(absPath);
                    final String dirName = newFileList.get(position).getName();

                    MyDialogListViewFragment fileAction = new MyDialogListViewFragment();
                    fileAction.setDialogTitle("Choose Action:");
                    fileAction.setDialogItemId(R.array.file_action);
                    fileAction.setDialogItemClickListener(new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dInterface, int indexPosition) {
                                switch (indexPosition) {
                                    case 0:
                                        //Rename the file

                                        break;
                                    case 1:
                                        //store the parent directory before deleting it
                                        String localParentDirPath = clickedFile.getParent();
                                        File localParentDirectory = new File(localParentDirPath);
                                        //Delete the File
                                        boolean isDeleted = clickedFile.delete();
                                        if (isDeleted) {
                                            Toast.makeText(getActivity(), dirName + " successfully deleted ", Toast.LENGTH_SHORT).show();
                                            newFileList.remove(position);
                                            fAdapter.notifyItemRemoved(position);
                                            //now also check if the directory became empty or not
                                            File[] listParent = localParentDirectory.listFiles();
                                            if (emptyChecker(listParent)) {
                                                noFileMethod(true);
                                            }
                                        } else {
                                            Toast.makeText(getActivity(), "Can't delete " + dirName, Toast.LENGTH_SHORT).show();
                                        }
                                        break;
                                    default:
                                        //Unknown action
                                        Toast.makeText(getActivity(), "Can't perform this action", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    fileAction.show(getActivity().getFragmentManager(), "show file action");
                }
            });
    }

    private boolean emptyChecker(File[] allFilesAndDir) {
        if (allFilesAndDir.length == 0 || allFilesAndDir == null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //clear the menu that comes from FilesActivity
        menu.clear();
        // inflate a new menu
        inflater.inflate(R.menu.fmanager_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fNewFile:
                EditNameDFragment endf1 = EditNameDFragment.newInstance("Enter File Name: ","Create","Cancel",null);
                endf1.checkExistance(parentDir.getAbsolutePath());
                endf1.setForFileCreation();
                endf1.show(fm,"Create file");
                return true;
            case R.id.fNewFolder:
                EditNameDFragment endf2 = EditNameDFragment.newInstance("Enter Folder Name: ","Create","Cancel",null);
                endf2.checkExistance(parentDir.getAbsolutePath());
                endf2.setForFolderCreation();
                endf2.show(fm,"Create folder");
                return true;
            case R.id.fNewProj:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
        onPathReceivedCallback = (ListFileFragment.OnPathReceivedCallback) context;
        } catch(ClassCastException e) {
            Toast.makeText(getActivity(),"Error: " + e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    
    public interface OnPathReceivedCallback {
        void onPathReceived(String currentPath);
    }
}
