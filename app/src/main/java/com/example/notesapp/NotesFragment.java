package com.example.notesapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
import android.content.SharedPreferences;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class NotesFragment extends Fragment implements NotesAdapter.OnNoteDeleteListener {

    private RecyclerView recyclerView;
    private NotesAdapter notesAdapter;
    private List<Notes> notesList;
    private DbHelper dbHelper;
    private SharedPreferences sharedPreferences;
    private ProgressBar progressBar;


    public NotesFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

        setHasOptionsMenu(true);

        recyclerView = view.findViewById(R.id.recycler_view_notes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notesAdapter = new NotesAdapter(getContext(), notesList);
        recyclerView.setAdapter(notesAdapter);
        progressBar = view.findViewById(R.id.progress_bar);

        dbHelper = new DbHelper(getContext());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());

        notesList = dbHelper.getAllNotes();
        notesAdapter.setNotes(notesList);
        notesAdapter.notifyDataSetChanged();

        FloatingActionButton fab = view.findViewById(R.id.addNotesBtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddNotesFragment();
            }
        });

        notesAdapter.setOnNoteDeleteListener(this);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_notes, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
         if (sharedPreferences != null) {
            // Show progress bar
            progressBar.setVisibility(View.VISIBLE);

            // Clear user login status from SharedPreferences
            sharedPreferences.edit().putBoolean("isLoggedIn", false).apply();

            // Return to the login screen after a delay (for demonstration)
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((MainActivity) requireActivity()).showSignInFragment();
                }
            }, 2000);
        }
    }

    public void onNoteDelete(Notes note) {
        // Delete the note from the database
        dbHelper.deleteNote(note);

        // Update the RecyclerView
        notesList.remove(note);
        notesAdapter.notifyDataSetChanged();
    }

    private void openAddNotesFragment() {
        AddNotesFragment addNotesFragment = new AddNotesFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, addNotesFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
