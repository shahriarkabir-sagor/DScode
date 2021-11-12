package bd.dkltd.dscode.myfragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import bd.dkltd.dscode.R;

public class MyDialogInputPath extends DialogFragment {

	private String dialogTitle, dialogPositiveText, dialogNegativeText;
	private EditText edt,edt2;
	private String dialogEtText1;
	private String dialogEtText2;

    public MyDialogInputPath() {
        dialogTitle = "Select Path";
        dialogPositiveText = "OK";
        dialogNegativeText = "cancel";
    }

    public void setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
    }
    public void setDialogPositiveText(String dialogPositiveText) {
        this.dialogPositiveText = dialogPositiveText;
    }
    public void setDialogNegativeText(String dialogNegativeText) {
        this.dialogNegativeText = dialogNegativeText;
    }

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		LayoutInflater inflater = getActivity().getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.layout_addpath, null);
		ImageButton btn;
		edt = dialogView.findViewById(R.id.layoutaddpathEditText1);
		edt2 = dialogView.findViewById(R.id.layoutaddpathEditText2);
		btn = dialogView.findViewById(R.id.layout_addpathImageButton);

		// Set clicklistener
		btn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View p1) {
					int requestcode = 1;
					Intent choosePath = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
					choosePath.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
					choosePath.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
					choosePath.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
					try {
						startActivityForResult(Intent.createChooser(choosePath, "Choose Directory"), requestcode);
					} catch (Exception e) {
						Toast.makeText(getActivity(),e.getMessage(), Toast.LENGTH_SHORT).show();
						//e.printStackTrace();
					}
				}
			});

		
		//build alert dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(dialogTitle);
		builder.setView(dialogView);
		builder.setPositiveButton(dialogPositiveText, null);
		builder.setNegativeButton(dialogNegativeText, null);
		edt.setText(dialogEtText1);
		edt2.setText(dialogEtText2);
		//edt.requestFocus(); 
		Dialog dialog = builder.create();
		return dialog;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == getActivity().RESULT_OK && data != null) {
			String treePath = data.getData().getPath();
			edt2.setText(treePath);
		}
	}
	
}
