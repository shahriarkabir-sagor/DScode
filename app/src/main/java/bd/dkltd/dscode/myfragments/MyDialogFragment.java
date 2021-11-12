package bd.dkltd.dscode.myfragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import bd.dkltd.dscode.R;

public class MyDialogFragment extends DialogFragment {
	
	private String dialogTitle = "File Name: ";
	private String dialogPositiveText = "OK";
	private String dialogNegativeText = "cancel";
	private String dialogEtText = "title.txt";
	private DialogInterface.OnClickListener dialogPositiveListener,dialogNegativeListener;

	public void setDialogTitle(String dialogTitle) {
		this.dialogTitle = dialogTitle;
	}

	public String getDialogTitle() {
		return dialogTitle;
	}

	public void setDialogPositiveText(String dialogPositiveText) {
		this.dialogPositiveText = dialogPositiveText;
	}

	public String getDialogPositiveText() {
		return dialogPositiveText;
	}

	public void setDialogNegativeText(String dialogNegativeText) {
		this.dialogNegativeText = dialogNegativeText;
	}

	public String getDialogNegativeText() {
		return dialogNegativeText;
	}

	public void setDialogEtText(String dialogEtText) {
		this.dialogEtText = dialogEtText;
	}

	public String getDialogEtText() {
		return dialogEtText;
	}

	public void setDialogPositiveListener(DialogInterface.OnClickListener dialogPositiveListener) {
		this.dialogPositiveListener = dialogPositiveListener;
	}

	public DialogInterface.OnClickListener getDialogPositiveListener() {
		return dialogPositiveListener;
	}

	public void setDialogNegativeListener(DialogInterface.OnClickListener dialogNegativeListener) {
		this.dialogNegativeListener = dialogNegativeListener;
	}

	public DialogInterface.OnClickListener getDialogNegativeListener() {
		return dialogNegativeListener;
	}

	

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.layout_one_et_dialog,null);
		EditText edt = dialogView.findViewById(R.id.createFileNameId); 
		if (dialogPositiveListener == null) {
			dialogPositiveListener = new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2) {
					//Do nothing
					//It is just for avoid app crash
				}
			};
		}
		if (dialogNegativeListener == null) {
			dialogNegativeListener = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface p1, int p2) {
					//Do nothing
					//It is just for avoid app crash
				}
			};
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(dialogTitle);
		builder.setView(dialogView);
		builder.setPositiveButton(dialogPositiveText, dialogPositiveListener);
		builder.setNegativeButton(dialogNegativeText, dialogNegativeListener);
		edt.setText(dialogEtText);
		Dialog dialog = builder.create();
		return dialog;
	}
}
