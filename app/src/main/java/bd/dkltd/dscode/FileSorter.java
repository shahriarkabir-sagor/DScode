package bd.dkltd.dscode;

import java.io.File;
import java.util.ArrayList;

public class FileSorter {
    
    private File[] listOfFiles;
    private ArrayList<File> listArray;
    
    //Empty Constructor
    public FileSorter() {
        super();
    }
    
    //Files accepting constructor
    public FileSorter(File[] listOfFiles) {
        this.listOfFiles = listOfFiles;
        //There need to be checked if listOfFile is not empty
        
        ArrayList<File> setArray = new ArrayList<File>();
        for (File singleFile : listOfFiles) {
            setArray.add(singleFile);
        }
        setListArray(setArray);
    }

    //Setters
    public void setListOfFiles(File[] listOfFiles) {
        this.listOfFiles = listOfFiles;
        ArrayList<File> setArray = new ArrayList<File>();
        for (File singleFile : listOfFiles) {
            setArray.add(singleFile);
        }
        setListArray(setArray);
    }
    
    private void setListArray(ArrayList<File> listArray) {
        this.listArray = listArray;
    }

    //Getters
    public ArrayList<File> getSortedFileArray() {
        return listArray;
    }
    
    /** ----------------------------
        All public methods starts here
        -------------------------------
      */
    
    //sort by name in Ascending order
    public void sortFilesByNameAsc() {
        
    }
    
    //sort by name in Descending order
    public void sortFilesByNameDesc() {
        
    }
    
    /**
         --- sort by directory or file
     */
    public void sortFilesByDirectory() {
        //Create a local object of ArrayList
        ArrayList<File> dirByFile = new ArrayList<File>();
        //run loop to get directories only
        for (File singleFile : listArray) {
            if (singleFile.isDirectory()){
                dirByFile.add(singleFile);
            }
        }
        //run another loop to get files only
        for (File singleFile : listArray) {
            if (!singleFile.isDirectory()) {
                dirByFile.add(singleFile);
            }
        }
        //finally set it to Final output point
        setListArray(dirByFile);
    }
    
    /** 
        --- remove hidden directory
     */
    public void filterHiddenDirs() {
        //Create a local object of ArrayList
        ArrayList<File> nonHidden = new ArrayList<File>();
        //run loop to get non-hidden fike
        for (File singleFile : listArray) {
            if (singleFile.isDirectory() && !singleFile.isHidden()) {
                nonHidden.add(singleFile);
            } else if (!singleFile.isDirectory()) {
                nonHidden.add(singleFile);
            }
        }
        //finally set it to Final output point
        setListArray(nonHidden);
    }
    
    //sort by lastModified date
    public void sortFilesByLastModifiedDate() {
        
    }
    
    //sort by size low2high
    public void sortFilesBySizeAsc() {
        
    }
    
    //sort by size high2low
    public void sortFileBySizeDesc() {
        
    }
}
