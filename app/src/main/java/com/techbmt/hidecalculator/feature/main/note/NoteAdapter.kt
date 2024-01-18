package com.techbmt.hidecalculator.feature.main.note

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.techbmt.hidecalculator.core.OnItemClickListener
import com.techbmt.hidecalculator.databinding.ItemNoteBinding
import com.techbmt.hidecalculator.db.entities.Note

class NoteAdapter(
    private val listener: OnItemClickListener,
    private val deleteListener: (Note, Int) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private var listNote = emptyList<Note>()

    inner class NoteViewHolder(private val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            binding.apply {
                tvTitle.text = note.title
                tvDetail.text = note.detail
                tvTime.text = note.time
                delete.setOnClickListener {
                    deleteListener.invoke(note, adapterPosition)
                }
            }
            itemView.setOnClickListener {
                listener.onItemClick(note)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = listNote.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(listNote[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Note>) {
        this.listNote = list
        notifyDataSetChanged()
    }

}