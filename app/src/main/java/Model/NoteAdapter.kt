package Model


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R


class NoteAdapter(
    private val notes: MutableList<Note>,
    private val onDeleteClick: (Note) -> Unit,
    private val onUpdateClick: (Note) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>(){


class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val noteTitle: TextView = itemView.findViewById(R.id.noteTitle)
    val noteDescription: TextView = itemView.findViewById(R.id.noteDescription)
    val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    val updateButton: Button = itemView.findViewById(R.id.updateButton)
}



override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.note_item, parent, false)
    return NoteViewHolder(view)
}

override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
    val note = notes[position]
    holder.noteTitle.text = note.title
    holder.noteDescription.text = note.description

    holder.deleteButton.setOnClickListener {
        onDeleteClick(note)
    }

    holder.updateButton.setOnClickListener {

        val updatedNote = Note(
            id = note.id,
            title = holder.noteTitle.text.toString(),
            description = holder.noteDescription.text.toString()
        )
        onUpdateClick(updatedNote)
    }
}


override fun getItemCount(): Int = notes.size

}











