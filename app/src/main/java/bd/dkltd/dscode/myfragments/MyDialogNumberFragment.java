package bd.dkltd.dscode.myfragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import bd.dkltd.dscode.R;

public class MyDialogNumberFragment extends DialogFragment {
	private Context Context;
	private int RequestCode;
	private String DialogTitle,DialogPositiveButton,DialogNegativeButton;
	private int minDialogValue,maxDialogValue,placeHolderValue;
	private boolean dialogZero;
	private View dialogView;
	private EditText edt;
	private DialogPositiveListener dialogPositiveListener;
	
	public MyDialogNumberFragment() {
		RequestCode = -1;
		DialogTitle = "Enter number: ";
		DialogPositiveButton = "OK";
		DialogNegativeButton = "cancel";
		minDialogValue = 1;
		maxDialogValue = 10;
		placeHolderValue = 00;
		dialogZero = false;
	}

	public MyDialogNumberFragment(Context context, int requestCode) {
		Context = context;
		RequestCode = requestCode;
		DialogTitle = "Enter number: ";
		DialogPositiveButton = "OK";
		DialogNegativeButton = "cancel";
		minDialogValue = 1;
		maxDialogValue = 10;
		placeHolderValue = 00;
		dialogZero = false;
	}

	public void setDialogZero(boolean dialogZero) {
		this.dialogZero = dialogZero;
	}

	public void setDialogPositiveListener(DialogPositiveListener dialogPositiveListener) {
		this.dialogPositiveListener = dialogPositiveListener;
	}

	public void setDialogTitle(String dialogTitle) {
		DialogTitle = dialogTitle;
	}

	public String getDialogTitle() {
		return DialogTitle;
	}

	public void setDialogPositiveButton(String dialogPositiveButton) {
		DialogPositiveButton = dialogPositiveButton;
	}

	public String getDialogPositiveButton() {
		return DialogPositiveButton;
	}

	public void setDialogNegativeButton(String dialogNegativeButton) {
		DialogNegativeButton = dialogNegativeButton;
	}

	public String getDialogNegativeButton() {
		return DialogNegativeButton;
	}
	
	public void setMinDialogValue(int minDialogValue) {
		this.minDialogValue = minDialogValue;
	}

	public int getMinDialogValue() {
		return minDialogValue;
	}

	public void setMaxDialogValue(int maxDialogValue) {
		this.maxDialogValue = maxDialogValue;
	}

	public int getMaxDialogValue() {
		return maxDialogValue;
	}

	public void setPlaceHolderValue(int placeHolderValue) {
		this.placeHolderValue = placeHolderValue;
	}

	public int getPlaceHolderValue() {
		return placeHolderValue;
	}

	public void setContext(Context context) {
		Context = context;
	}

	public Context getContext() {
		return Context;
	}

	public void setRequestCode(int requestCode) {
		RequestCode = requestCode;
	}

	public int getRequestCode() {
		return RequestCode;
	}

	//Create Dialog
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		dialogView = inflater.inflate(R.layout.layout_one_et_nmbr,null);
		edt = dialogView.findViewById(R.id.oneEtnmbrId);
		edt.setText(String.valueOf(placeHolderValue));
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(DialogTitle);
		builder.setView(dialogView);
		builder.setPositiveButton(DialogPositiveButton, null);
		builder.setNegativeButton(DialogNegativeButton, null);
		Dialog dialog = builder.create();
		return dialog;
	}
	public interface DialogPositiveListener{
		void onPositiveListen(int requestCode, int value);
	}

	@Override
	public void onResume() {
		super.onResume();
		final AlertDialog d = (AlertDialog) getDialog();
		if (d != null) {
			Button positiveButton = d.getButton(Dialog.BUTTON_POSITIVE);
			positiveButton.setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View p1) {
                        String strValue = edt.getText().toString();
                        if (!strValue.isEmpty()) {
                            int inputValue = Integer.parseInt(strValue);
                            //Check if zero value is allowed to accept
                            if (dialogZero) {
                                if (inputValue == 0) {
                                    try {
                                        dialogPositiveListener.onPositiveListen(RequestCode,inputValue);
                                    } catch (Exception e) {
                                        Toast.makeText(getActivity(),"Listener is not set yet",Toast.LENGTH_LONG).show();
                                    }
                                    d.dismiss();
                                } else if (inputValue >= minDialogValue && inputValue <= maxDialogValue) {
                                    try {
                                        dialogPositiveListener.onPositiveListen(RequestCode,inputValue);
                                    } catch (Exception e) {
                                        Toast.makeText(getActivity(),"Listener is not set yet",Toast.LENGTH_LONG).show();
                                    }
                                    d.dismiss();
                                } else {
                                    edt.setError("Invalid value");
                                    edt.requestFocus();
                                }
                            } else {
                                if (inputValue >= minDialogValue && inputValue <= maxDialogValue) {
                                    try {
                                        dialogPositiveListener.onPositiveListen(RequestCode,inputValue);
                                    } catch (Exception e) {
                                        Toast.makeText(getActivity(),"Listener is not set yet",Toast.LENGTH_LONG).show();
                                    }
                                    d.dismiss();
                                } else {
                                    edt.setError("Invalid value");
                                    edt.requestFocus();
                                }
                            }
                        } else {
                            // value is empty
                            edt.setError("This Field can't be empty");
                            edt.requestFocus();
                        }
						
					}
				});
		}
	}
}
