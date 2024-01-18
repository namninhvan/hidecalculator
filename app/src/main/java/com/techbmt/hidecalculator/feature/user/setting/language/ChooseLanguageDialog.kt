package com.techbmt.hidecalculator.feature.user.setting.language

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.techbmt.hidecalculator.core.MyPreference
import com.techbmt.hidecalculator.databinding.DialogChooseLanguageBinding
import com.techbmt.hidecalculator.feature.calculator.CalculatorActivity
import com.techbmt.hidecalculator.feature.user.setting.SettingActivity

class ChooseLanguageDialog : DialogFragment() {

    private lateinit var binding: DialogChooseLanguageBinding
    private var listLanguage: ArrayList<Language> = arrayListOf()
    private lateinit var adapter: LanguageAdapter
    private var languageType = TypeOfLanguage.ENGLISH

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, com.google.android.material.R.style.ThemeOverlay_MaterialComponents)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogChooseLanguageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            listLanguage.add(Language(TypeOfLanguage.VIETNAM, false))
            listLanguage.add(Language(TypeOfLanguage.ENGLISH, false))
            val chosenListener: (Language, Int) -> Unit = { language, pos ->
                languageType = language.type!!
            }
            adapter = LanguageAdapter(requireActivity(), listLanguage, chosenListener)
            rcvLanguage.adapter = adapter

            save.setOnClickListener {
                refresh()
            }
        }
    }

    private fun refresh() {
        MyPreference(requireActivity()).saveLocale(languageType.toString())
        Handler((requireActivity() as SettingActivity).mainLooper).postDelayed({
            val intent = Intent(requireActivity(), CalculatorActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent)
            requireActivity().finish()
        },500)
    }


}