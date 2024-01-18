package com.techbmt.hidecalculator.feature.user.password

import android.content.Intent
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.techbmt.hidecalculator.R
import com.techbmt.hidecalculator.core.BaseActivity
import com.techbmt.hidecalculator.core.MyPreference
import com.techbmt.hidecalculator.databinding.ActivitySetPasswordBinding
import com.techbmt.hidecalculator.feature.main.MainActivity

class SetPasswordActivity : BaseActivity<ActivitySetPasswordBinding>() {

    private var userPassword = ""
    private var pos = 0
    private var arrEditText: ArrayList<EditText> = arrayListOf()

    override fun initView() {

        arrEditText.add(binding.edt1)
        arrEditText.add(binding.edt2)
        arrEditText.add(binding.edt3)
        arrEditText.add(binding.edt4)
        arrEditText.add(binding.edt5)
        arrEditText.add(binding.edt6)

        binding.apply {
            one.setOnClickListener {
                updateUserPassword((it as Button).text.toString())
            }
            two.setOnClickListener {
                updateUserPassword((it as Button).text.toString())
            }
            three.setOnClickListener {
                updateUserPassword((it as Button).text.toString())
            }
            four.setOnClickListener {
                updateUserPassword((it as Button).text.toString())
            }
            five.setOnClickListener {
                updateUserPassword((it as Button).text.toString())
            }
            six.setOnClickListener {
                updateUserPassword((it as Button).text.toString())
            }
            seven.setOnClickListener {
                updateUserPassword((it as Button).text.toString())
            }
            eight.setOnClickListener {
                updateUserPassword((it as Button).text.toString())
            }
            nine.setOnClickListener {
                updateUserPassword((it as Button).text.toString())
            }
            zero.setOnClickListener {
                updateUserPassword((it as Button).text.toString())
            }
            delete.setOnClickListener {
                deleteValue()
            }
            equal.setOnClickListener {
                binding.apply {
                    if (!isEmptyEdt(edt1) && !isEmptyEdt(edt2) && !isEmptyEdt(edt3) && !isEmptyEdt(
                            edt4
                        ) && !isEmptyEdt(edt5) && !isEmptyEdt(edt6)
                    ) {
                        userPassword =
                            edt1.text.toString() + edt2.text.toString() + edt3.text.toString() + edt4.text.toString() + edt5.text.toString() + edt6.text.toString()

                        MyPreference(this@SetPasswordActivity).savePassword(userPassword)
                        Toast.makeText(
                            this@SetPasswordActivity,
                            getString(R.string.set_password_successfully),
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this@SetPasswordActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            this@SetPasswordActivity,
                            getString(R.string.fill_full_password),
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setOnClickListener
                    }
                }
            }
        }
    }

    private fun hideKeyboard(edt: EditText) {
        edt.setRawInputType(InputType.TYPE_CLASS_TEXT);
        edt.setTextIsSelectable(true);
    }

    private fun deleteValue() {
        binding.apply {
            if (edt1.isFocused) {
                edt1.setText("")
            } else if (edt2.isFocused) {
                edt2.setText("")
            } else if (edt3.isFocused) {
                edt3.setText("")
            } else if (edt4.isFocused) {
                edt4.setText("")
            } else if (edt5.isFocused) {
                edt5.setText("")
            } else if (edt6.isFocused) {
                edt6.setText("")
            } else {
                when (pos) {
                    6 -> {
                        edt6.setText("")
                    }

                    5 -> {
                        edt5.setText("")
                    }

                    4 -> {
                        edt4.setText("")
                    }

                    3 -> {
                        edt3.setText("")
                    }

                    2 -> {
                        edt2.setText("")
                    }

                    1 -> {
                        edt1.setText("")
                    }
                }
            }
        }
        pos--
    }

    private fun updateUserPassword(value: String) {
        pos++
        binding.apply {
            if (!edt1.isFocused && !edt2.isFocused && !edt3.isFocused && !edt4.isFocused && !edt5.isFocused && !edt6.isFocused) {
                if (isEmptyEdt(edt1)) {
                    edt1.setText(value)
                } else if (isEmptyEdt(edt2)) {
                    edt2.setText(value)
                } else if (isEmptyEdt(edt3)) {
                    edt3.setText(value)
                } else if (isEmptyEdt(edt4)) {
                    edt4.setText(value)
                } else if (isEmptyEdt(edt5)) {
                    edt5.setText(value)
                } else if (isEmptyEdt(edt6)) {
                    edt6.setText(value)
                }
            } else {
                if (edt1.isFocused) {
                    edt1.setText(value)
                } else if (edt2.isFocused) {
                    edt2.setText(value)
                } else if (edt3.isFocused) {
                    edt3.setText(value)
                } else if (edt4.isFocused) {
                    edt4.setText(value)
                } else if (edt5.isFocused) {
                    edt5.setText(value)
                } else if (edt6.isFocused) {
                    edt6.setText(value)
                }
            }
        }
    }

    private fun isEmptyEdt(edt: EditText): Boolean {
        return edt.text.toString().isEmpty()
    }

    override fun getLayout(): ActivitySetPasswordBinding {
        return ActivitySetPasswordBinding.inflate(layoutInflater)
    }
}