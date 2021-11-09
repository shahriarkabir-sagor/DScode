package bd.dkltd.dscode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.io.File;
import android.content.SharedPreferences;
import android.content.Context;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class ListFile extends AppCompatActivity 
{
	private String rootPath;
	private TextView noTxtTv;
	private RecyclerView rcv;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listfile);
        init();
    }

	private void init() {
		// get path from intent
		rootPath = getIntent().getStringExtra("rootPath");

		//TextView
		noTxtTv = findViewById(R.id.noFileTv1);
		
		// Files
		File rootDir = new File(rootPath);
		File[] filesAndFolders = rootDir.listFiles();
		//ArrayList<File> fArray = new ArrayList<>();
        Arrays.sort(filesAndFolders,new Comparator<File>() {

                @Override
                public int compare(File p1, File p2) {
                    return p1.getName().compareTo(p2.getName());
                }
            });
		listFilesMethod(filesAndFolders);
		
		//Adapter
		MyFileAdapter myFa = new MyFileAdapter(this,filesAndFolders);
		
		// RecyclerView
		rcv = findViewById(R.id.rcvListFile1);
		rcv.setLayoutManager(new LinearLayoutManager(this));
		rcv.setAdapter(myFa);
		
	}

	private void listFilesMethod(File[] filesAndFolders) {
		if (filesAndFolders == null || filesAndFolders.length == 0) {
			noTxtTv.setText("No File Found");
			
		} else {
			noTxtTv.setText("Files Found");
			
		}
	}
}
