package bd.dkltd.dscode;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import bd.dkltd.dscode.myfragments.EditNameDFragment;
import bd.dkltd.dscode.myfragments.MyDialogListViewFragment;
import java.io.File;
import java.util.ArrayList;
import android.os.Looper;

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
    private ArrayList<File> newFileList;
    private FileSorter fs;
    private File[] allFilesAndDirs;
    private boolean wasEmpty;
    private MenuItem searchItem;

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
        pathFromArgs = getArguments().getString("dirPath", "/emulated/0");
        try {
            onPathReceivedCallback.onPathReceived(pathFromArgs);
        } catch(NullPointerException e) {
            
        }
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
        newFileList = new ArrayList<File>();
        fs = new FileSorter();
        fm = getActivity().getSupportFragmentManager();
        // File operation
        parentDir = new File(pathFromArgs);
        refreshData();

        //add path to Button
        initBtn(parentDir);
    }
    
    private void refreshData() {
        allFilesAndDirs = parentDir.listFiles();
        boolean shouldShowRecyclerView = fileOperations(allFilesAndDirs);
        if (shouldShowRecyclerView) {
            //init recyclerView and show
            rcView.setHasFixedSize(true);
            rcView.setLayoutManager(new LinearLayoutManager(fView.getContext()));
            fAdapter = new MyFileAdapter(fView.getContext(), newFileList);
            rcView.setAdapter(fAdapter);
            //handle recyclerView callback (listener)
            recycleCallBackHandler(newFileList);
        } else {
            //Here means empty files
            wasEmpty = true;
        }
    }

    private boolean fileOperations(File[] allFilesAndDir) {
        boolean result = false;
        if (!emptyChecker(allFilesAndDir)) {
            //Differentiate folders and Files and sort them
            fs.setListOfFiles(allFilesAndDir);
            fs.filterHiddenDirs();
            fs.sortFilesByNameAscIgnoreCase();
            fs.sortFilesByDirectory();
            newFileList = fs.getSortedFileArray();
            // check sorted fileArry is empty or not
            if (newFileList.size() > 0) {
                noFileMethod(false);
                result = true;
            } else {
                noFileMethod(true);
            }

        } else {
            noFileMethod(true);
        }
        return result;
    }
    
    private boolean emptyChecker(File[] allFilesAndDir) {
        if (allFilesAndDir.length == 0) {
            return true;
        } else {
            return false;
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

    private void recycleCallBackHandler(ArrayList<File> argList) {
        final ArrayList<File> fList = argList;
        fAdapter.setOnFileClickListener(new MyFileAdapter.ClickListener() {

                @Override
                public void onItemClick(int position, View v) {
                    String absPath = fList.get(position).getAbsolutePath();
                    File clickedFile = new File(absPath);
                    String dirName = fList.get(position).getName();
                    if (checkFile(clickedFile)) {
                        ListFileFragment lff = new ListFileFragment();
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.frLayId, lff.newInstance(absPath), "Opening directoy " + dirName);
                        ft.addToBackStack("inside file browser");
                        ft.commit();
                        //check searchview and close
                        if(searchItem.isActionViewExpanded()) {
                            searchItem.collapseActionView();
                        }
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
                    String absPath = fList.get(position).getAbsolutePath();
                    final File clickedFile = new File(absPath);
                    final String dirName = fList.get(position).getName();

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
                                        //Delete the File
                                        boolean isDeleted = clickedFile.delete();
                                        if (isDeleted) {
                                            Toast.makeText(getActivity(), dirName + " successfully deleted ", Toast.LENGTH_SHORT).show();
                                            fList.remove(position);
                                            fAdapter.notifyItemRemoved(position);
                                            //now also check if the directory became empty or not
                                            if (fList.size() == 0) {
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
    
    private void subRefresh() {
        if(wasEmpty) {
            //RecyclerView is not used b'cz it file was empty
            // So set method to it now
            rcView.setHasFixedSize(true);
            rcView.setLayoutManager(new LinearLayoutManager(fView.getContext()));
            fAdapter = new MyFileAdapter(fView.getContext(), newFileList);
            rcView.setAdapter(fAdapter);
            //handle recyclerView callback (listener)
            recycleCallBackHandler(newFileList);
            // visible the recyclerView
            noFileMethod(false);
            //We have added data, so set wasEmpty to false
            wasEmpty = false;
        } else if(newFileList.size() > 0) {
            noFileMethod(false);
        }
    }
    
    private void manageHidden(File modifiedFolder) {
        if(modifiedFolder.isHidden()) {
            int position = newFileList.indexOf(modifiedFolder);
            newFileList.remove(position);
            fAdapter.notifyItemRemoved(position);
            Toast.makeText(getActivity(),"Folder hidden",Toast.LENGTH_SHORT).show();
            if(newFileList.size() == 0) {
                noFileMethod(true);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //clear the menu that comes from FilesActivity
        menu.clear();
        // inflate a new menu
        inflater.inflate(R.menu.fmanager_menu, menu);
        //set search
        searchItem = menu.findItem(R.id.fMagnifyIcon);
        SearchView sv = (SearchView) searchItem.getActionView();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String p1) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String p1) {
                    if (fAdapter != null) {
                        fAdapter.getFilter().filter(p1);
                    }
                    return false;
                }
            });
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fNewFile:
                EditNameDFragment endf1 = EditNameDFragment.newInstance("Enter File Name: ", "Create", "Cancel", null);
                endf1.checkExistance(parentDir.getAbsolutePath());
                endf1.setForFileCreation();
                //set onSuccess listener here
                endf1.setOnSuccessListener(new EditNameDFragment.OnSuccessListener() {

                        @Override
                        public void onSuccess(boolean result, File createdFile) {
                            //check file is created or not
                            if (result && createdFile.exists()) {
                                int indPos;
                                if (newFileList.size() > 0) {
                                    //determine in which position the file should be added
                                    FileSorter fs2 = new FileSorter();
                                    ArrayList<File> tempArray = new ArrayList<File>(newFileList);
                                    tempArray.add(createdFile);
                                    fs2.setArrayListOfFiles(tempArray);
                                    fs2.filterHiddenDirs();
                                    fs2.sortFilesByNameAscIgnoreCase();
                                    fs2.sortFilesByDirectory();
                                    tempArray = fs2.getSortedFileArray();
                                    indPos = tempArray.indexOf(createdFile);
                                    //add it to array
                                    newFileList.add(indPos,createdFile);
                                } else {
                                    newFileList.add(createdFile);
                                    indPos = newFileList.indexOf(createdFile);
                                }
                                //call subRefresh
                                subRefresh();
                                //notify adapter
                                fAdapter.notifyItemInserted(indPos);
                                //scroll to position
                                rcView.scrollToPosition(indPos);
                            }
                        }
                    });
                endf1.show(fm, "Create file");
                return true;
            case R.id.fNewFolder:
                EditNameDFragment endf2 = EditNameDFragment.newInstance("Enter Folder Name: ", "Create", "Cancel", null);
                endf2.checkExistance(parentDir.getAbsolutePath());
                endf2.setForFolderCreation();
                //set onSuccessListener
                endf2.setOnSuccessListener(new EditNameDFragment.OnSuccessListener() {

                        @Override
                        public void onSuccess(boolean result, final File createdFolder) {
                            // check folder is created or not
                            if (result && createdFolder.exists()) {
                                int indPos;
                                if(newFileList.size() > 0) {
                                    //determine in which position the file should be added
                                    FileSorter fs2 = new FileSorter();
                                    ArrayList<File> tempArray = new ArrayList<File>(newFileList);
                                    tempArray.add(createdFolder);
                                    fs2.setArrayListOfFiles(tempArray);
                                    fs2.filterHiddenDirs();
                                    fs2.sortFilesByNameAscIgnoreCase();
                                    fs2.sortFilesByDirectory();
                                    tempArray = fs2.getSortedFileArray();
                                    indPos = tempArray.indexOf(createdFolder);
                                    //add it to arraylist
                                    newFileList.add(indPos, createdFolder);
                                } else {
                                    //add it to arraylist
                                    newFileList.add(createdFolder);
                                    indPos = newFileList.indexOf(createdFolder);
                                }
                                //call subrefresh
                                subRefresh();
                                //notify adapter
                                fAdapter.notifyItemInserted(indPos);
                                //scroll to position
                                rcView.scrollToPosition(indPos);
                                //check created folder is a hidden directory or not
                                //and take action according to it
                                Handler handler = new Handler(Looper.getMainLooper());
                                handler.postDelayed(new Runnable() {

                                        @Override
                                        public void run() {
                                            manageHidden(createdFolder);
                                        }
                                    }, 1100);
                            }
                        }
                    });
                endf2.show(fm, "Create folder");
                return true;
            case R.id.fNewProj:
                return true;
            case R.id.fReload:
                refreshData();
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
        } catch (ClassCastException e) {
            Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public interface OnPathReceivedCallback {
        void onPathReceived(String currentPath);
    }
}
