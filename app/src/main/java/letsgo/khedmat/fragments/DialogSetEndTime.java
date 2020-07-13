package letsgo.khedmat.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import letsgo.khedmat.R;


public class DialogSetEndTime extends MyDialogFragment {


    private View view;
    private Listener listener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.dialog_set_end_time, container, false);

        AppCompatButton btnDone = view.findViewById(R.id.btnDone);
        AppCompatButton btnCancel = view.findViewById(R.id.btnCancel);
        final EditText edtMonth = view.findViewById(R.id.edtMonth);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String monthString = edtMonth.getText().toString();
                if (monthString != null) {
                    int month = Integer.parseInt(monthString);
                    if (month > 0) {
                        listener.onSelectMonth(month);
                        dismiss();
                    }
                } else {
                    Toast.makeText(getContext(), "عدد وارد شده صحیح نیست", Toast.LENGTH_LONG);
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onSelectMonth(int month);
    }
}