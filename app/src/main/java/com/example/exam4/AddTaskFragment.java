package com.example.exam4;

import android.os.Bundle;
import android.util.Log; // 1. استيراد مكتبة اللوج
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.concurrent.Executors; // لاستخدام الخلفية

public class AddTaskFragment extends Fragment {

    // كلمة السر للبحث عنها في اللوج كات
    private static final String TAG = "Exam4_Debug";

    private EditText etTitle;
    private EditText etDescription;
    private Button btnSave;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_task, container, false);

        // ربط العناصر (حسب الصور التي أرسلتها)
        etTitle = view.findViewById(R.id.et_title);
        etDescription = view.findViewById(R.id.et_description);
        btnSave = view.findViewById(R.id.btn_save);

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString();
            String desc = etDescription.getText().toString();

            // --- طباعة البيانات في اللوج كات عند الضغط ---
            Log.d(TAG, "------------------------------------");
            Log.d(TAG, "1. تم الضغط على زر الحفظ");
            Log.d(TAG, "2. العنوان المكتوب: " + title);
            Log.d(TAG, "3. الوصف المكتوب: " + desc);

            if (!title.isEmpty()) {
                saveTaskToDatabase(title, desc);
            } else {
                Log.e(TAG, "خطأ: العنوان فارغ، لن يتم الحفظ");
                Toast.makeText(getActivity(), "يرجى إدخال العنوان", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void saveTaskToDatabase(String title, String desc) {
        // التنفيذ في الخلفية كما هو في كودك الأصلي
        Executors.newSingleThreadExecutor().execute(() -> {

            Log.d(TAG, "4. جاري الاتصال بقاعدة البيانات...");

            try {
                // إنشاء كائن المهمة
                Task newTask = new Task(title, desc);

                // أمر الإدخال (Insert)
                AppDatabase.getInstance(getContext()).taskDao().insert(newTask);

                Log.d(TAG, "5. نجاح! تمت إضافة المهمة لقاعدة البيانات Room.");

            } catch (Exception e) {
                Log.e(TAG, "فشل الحفظ! المشكلة هي: " + e.getMessage());
            }
        });
    }
}