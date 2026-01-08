package com.example.exam4;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import java.util.concurrent.Executors;

public class AddTaskFragment extends Fragment {

    private EditText etTitle, etDescription;
    private Button btnSave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_task, container, false);

        etTitle = view.findViewById(R.id.et_title);
        etDescription = view.findViewById(R.id.et_description);
        btnSave = view.findViewById(R.id.btn_save);

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString();
            String desc = etDescription.getText().toString();

            if (!title.isEmpty()) {
                saveTaskToDatabase(title, desc);
            } else {
                Toast.makeText(getActivity(), "يرجى إدخال العنوان", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void saveTaskToDatabase(String title, String desc) {
        // تنفيذ الحفظ في خيط خلفي لأن Room لا تسمح بالعمل على الخيط الرئيسي
        Executors.newSingleThreadExecutor().execute(() -> {
            Task newTask = new Task(title, desc);
            AppDatabase.getInstance(getContext()).taskDao().insert(newTask);

            getActivity().runOnUiThread(() -> {
                Toast.makeText(getActivity(), "تم حفظ المهمة بنجاح!", Toast.LENGTH_SHORT).show();
                etTitle.setText("");
                etDescription.setText("");
            });
        });
    }
}