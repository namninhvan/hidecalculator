package com.techbmt.hidecalculator.feature.main.note

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.techbmt.hidecalculator.R
import com.techbmt.hidecalculator.core.BaseActivity
import com.techbmt.hidecalculator.core.OnItemClickListener
import com.techbmt.hidecalculator.databinding.ActivityNoteBinding
import com.techbmt.hidecalculator.db.DBViewModel
import com.techbmt.hidecalculator.db.entities.Note

class NoteActivity : BaseActivity<ActivityNoteBinding>() {

    companion object {
        const val NOTE_TYPE = "note_type"
        const val NOTE = "note"
    }

    private lateinit var dbViewModel: DBViewModel
    private lateinit var adapter: NoteAdapter
    private var noteType = 0

    override fun initView() {

        dbViewModel = ViewModelProvider(this)[DBViewModel::class.java]

        binding.apply {

            back.setOnClickListener {
                finish()
            }

            ivAdd.setOnClickListener {
                noteType = 1
                val intent = Intent(this@NoteActivity, DetailNoteActivity::class.java)
                intent.putExtra(NOTE_TYPE, noteType)
                startActivity(intent)
            }

            val onItemClickListener = object : OnItemClickListener {
                override fun onItemClick(data: Any) {
                    noteType = 2
                    val note = data as Note
                    val bundle = Bundle()
                    bundle.putSerializable(NOTE, note)
                    bundle.putInt(NOTE_TYPE, noteType)
                    val intent = Intent(this@NoteActivity, DetailNoteActivity::class.java)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }
            }

            val handleDeleteNote: (Note, Int) -> Unit = { note, pos ->
                val dialog = AlertDialog.Builder(this@NoteActivity)
                dialog.apply {
                    setTitle(getString(R.string.ask_delete_note))
                    setMessage(getString(R.string.msg_delete_note))
                    setPositiveButton(getString(R.string.delete)) { _, _ ->
                        Handler(mainLooper).postDelayed({
                            dbViewModel.deleteNote(note.id)
                            Toast.makeText(this@NoteActivity, getString(R.string.deleted), Toast.LENGTH_SHORT).show()
                            adapter.notifyItemRemoved(pos)
                        }, 500)
                    }
                    setNegativeButton(getString(R.string.cancel)) { _, _ ->
                        // do nothing
                    }
                    show()
                }
            }

            adapter = NoteAdapter(onItemClickListener, handleDeleteNote)
            rcvNote.adapter = adapter

            dbViewModel.readAllNote.observe(this@NoteActivity) {
                adapter.setData(it)
            }

        }
    }

    override fun getLayout(): ActivityNoteBinding =
        ActivityNoteBinding.inflate(layoutInflater)
}