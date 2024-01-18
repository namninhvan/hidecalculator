package com.techbmt.hidecalculator.feature.main.note

import android.os.Build
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.techbmt.hidecalculator.R
import com.techbmt.hidecalculator.core.BaseActivity
import com.techbmt.hidecalculator.core.getCurrentDateTime
import com.techbmt.hidecalculator.databinding.ActivityDetailNoteBinding
import com.techbmt.hidecalculator.db.DBViewModel
import com.techbmt.hidecalculator.db.entities.Note

class DetailNoteActivity : BaseActivity<ActivityDetailNoteBinding>() {

    private lateinit var dbViewModel: DBViewModel

    override fun initView() {

        dbViewModel = ViewModelProvider(this)[DBViewModel::class.java]

        val noteType = intent.extras!!.getInt(NoteActivity.NOTE_TYPE)
        Log.i("type", noteType.toString())

        binding.apply {

            back.setOnClickListener {
                finish()
            }

            if (noteType == 1) {
                save.setOnClickListener {
                    if (edtTitle.text.toString().isEmpty()) {
                        layoutTitle.error = getString(R.string.no_empty_title)
                    } else if (edtDetail.text.toString().isEmpty()) {
                        tvErrorDetail.text = getString(R.string.no_empty_detail)
                    } else if (edtTitle.text.toString().length > 40) {
                        layoutTitle.error = getString(R.string.counter_title_length)
                    } else {
                        dbViewModel.addNote(
                            Note(
                                id = 0,
                                title = edtTitle.text.toString(),
                                detail = edtDetail.text.toString(),
                                time = getCurrentDateTime()
                            )
                        )
                        Handler(mainLooper).postDelayed({
                            Toast.makeText(
                                this@DetailNoteActivity,
                                getString(R.string.save_note_successfully),
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }, 500)
                    }
                }
            } else if (noteType == 2) {
                val note = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.extras!!.getSerializable(NoteActivity.NOTE, Note::class.java)!!
                } else {
                    intent.extras!!.getSerializable(NoteActivity.NOTE) as Note
                }
                edtTitle.setText(note.title)
                edtDetail.setText(note.detail)
                save.setOnClickListener {
                    dbViewModel.updateNote(
                        Note(
                            id = note.id,
                            title = edtTitle.text.toString(),
                            detail = edtDetail.text.toString(),
                            time = getCurrentDateTime()
                        )
                    )
                    Handler(mainLooper).postDelayed({
                        Toast.makeText(
                            this@DetailNoteActivity,
                            getString(R.string.update_note_successfully),
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }, 500)
                }
            }

        }

    }

    override fun getLayout(): ActivityDetailNoteBinding {
        return ActivityDetailNoteBinding.inflate(layoutInflater)
    }


}