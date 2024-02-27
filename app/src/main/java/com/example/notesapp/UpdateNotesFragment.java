package com.example.notesapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;


public class UpdateNotesFragment extends Fragment {

    EditText updateTitleEditText, updateContentEditText;
    ImageView updateBtn;
    Notes noteToUpdate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_notes, container, false);

        updateTitleEditText = view.findViewById(R.id.updateTitleEditText);
        updateContentEditText = view.findViewById(R.id.updateContentEditText);
        updateBtn = view.findViewById(R.id.updateBtn);

        // Get the note data from the bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            noteToUpdate = (Notes) bundle.getSerializable("note");
            if (noteToUpdate != null) {
                // Set the note data to the EditText fields
                updateTitleEditText.setText(noteToUpdate.getTitle());
                updateContentEditText.setText(noteToUpdate.getContent());
            }
        }

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNote();
            }
        });

        return view;
    }

    private void updateNote() {
        String updatedTitle = updateTitleEditText.getText().toString().trim();
        String updatedContent = updateContentEditText.getText().toString().trim();

        // Create a new Note object with updated data
        Notes updatedNote = new Notes();
        updatedNote.setId(noteToUpdate.getId()); // Set the ID of the original note
        updatedNote.setTitle(updatedTitle);
        updatedNote.setContent(updatedContent);

        // Update the note in the database
        DbHelper dbHelper = new DbHelper(requireContext());
        dbHelper.updateNote(updatedNote);

        // Close the fragment
        getParentFragmentManager().popBackStack();
    }
}
