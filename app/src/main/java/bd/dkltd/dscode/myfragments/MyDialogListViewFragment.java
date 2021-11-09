package bd.dkltd.dscode.myfragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import bd.dkltd.dscode.R;

public class MyDialogListViewFragment extends DialogFragment {

	private String dialogTitle = "Choose an Item: ";
	private DialogInterface.OnClickListener dialogItemClickListener;
	private int dialogItemId = R.array.app_font_name_array,arrayIndexPos;

	public void setArrayIndexPos(int arrayIndexPos) {
		this.arrayIndexPos = arrayIndexPos;
	}

	public int getArrayIndexPos() {
		return arrayIndexPos;
	}

	public void setDialogItemClickListener(DialogInterface.OnClickListener dialogItemClickListener) {
		this.dialogItemClickListener = dialogItemClickListener;
	}

	public DialogInterface.OnClickListener getDialogItemClickListener() {
		return dialogItemClickListener;
	}

	public void setDialogTitle(String dialogTitle) {
		this.dialogTitle = dialogTitle;
	}

	public String getDialogTitle() {
		return dialogTitle;
	}

	public void setDialogItemId(int intResId) {
		this.dialogItemId = intResId;
	}

	public int getDialogItemId() {
		return dialogItemId;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		if (dialogItemClickListener == null) {
			dialogItemClickListener = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface p1, int indexPosition) {
					setArrayIndexPos(indexPosition);
				}
			};
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(dialogTitle);
		builder.setItems(dialogItemId, dialogItemClickListener);
		Dialog dialog = builder.create();
		return dialog;
	}
    
    
    
}
