package com.example.app

import Model.Note
import Model.NoteAdapter
import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app.ui.theme.AppTheme

class MainActivity : AppCompatActivity() {


    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NoteAdapter
    private val noteList = mutableListOf<Note>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        recyclerView = findViewById(R.id.recyclerView)
        adapter = NoteAdapter(noteList,
            onDeleteClick = { note ->
                deleteNoteFromContentProvider(note.id)
            },
            onUpdateClick = { note ->
                updateNoteInContentProvider(note)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        loadNotesFromOtherApp()
    }

    private fun loadNotesFromOtherApp() {
        val uri = Uri.parse("content://com.example.praktineuzduotis.provider/notes")
        val cursor = contentResolver.query(uri, null, null, null, null)

        noteList.clear()

        if (cursor == null) {
            Log.e("MainActivity", "Cursor is null — data not loaded")
        } else {
            Log.d("MainActivity", "Cursor count: ${cursor.count}")
        }
        cursor?.use {

            val titleIndex = it.getColumnIndex("title")
            val contentIndex = it.getColumnIndex("content")
            val idIndex = it.getColumnIndex("_id")
            while (it.moveToNext()) {
                val id = it.getInt(idIndex)
                val title = it.getString(titleIndex)
                val content = it.getString(contentIndex)
                noteList.add(Note(id,title, content))
            }
        }

        adapter.notifyDataSetChanged()
    }

    private fun deleteNoteFromContentProvider(noteId: Int) {
        val uri = Uri.parse("content://com.example.praktineuzduotis.provider/notes/$noteId")
        val deletedRows = contentResolver.delete(uri, null, null)
        if (deletedRows > 0) {
            Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show()
            loadNotesFromOtherApp()  // Perkraunam sąrašą po ištrynimo
        } else {
            Toast.makeText(this, "Failed to delete note", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateNoteInContentProvider(note: Note) {
        val uri = Uri.parse("content://com.example.praktineuzduotis.provider/notes/${note.id}")

        val values = ContentValues().apply {
            put("title", note.title)
            put("content", note.description)
        }

        val rowsUpdated = contentResolver.update(uri, values, null, null)

        if (rowsUpdated > 0) {
            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show()
            loadNotesFromOtherApp() // atnaujink sąrašą
        } else {
            Toast.makeText(this, "Failed to update note", Toast.LENGTH_SHORT).show()
        }
    }



}