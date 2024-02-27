package com.example.notesapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


public class AddNotesFragment extends Fragment {
    private EditText titleEditText;
    private EditText contentEditText;
    private ImageView saveBtn;
    private DbHelper dbHelper;


    public AddNotesFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_notes, container, false);

        titleEditText = view.findViewById(R.id.titleEditText);
        contentEditText = view.findViewById(R.id.contentEditText);
        saveBtn = view.findViewById(R.id.saveBtn);

        dbHelper = new DbHelper(getContext());

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        return view;
    }

    private void saveNote() {
        String title = titleEditText.getText().toString().trim();
        String content = contentEditText.getText().toString().trim();

        if (!title.isEmpty() && !content.isEmpty()) {
            // Create a new Note object
            Notes note = new Notes();
            note.setTitle(title);
            note.setContent(content);

            // Insert the note into the database
            long id = dbHelper.addNote(note);

            if (id != -1) {
                  Toast.makeText(getContext(), "Note added successfully", Toast.LENGTH_SHORT).show();
                requireActivity().getSupportFragmentManager().popBackStack();
            } else {
                // Failed to add note
                Toast.makeText(getContext(), "Failed to add note", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Show error message if title or content is empty
            Toast.makeText(getContext(), "Please enter title and content", Toast.LENGTH_SHORT).show();
        }
    }
}