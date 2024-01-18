package com.techbmt.hidecalculator.feature.calculator

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.ScrollView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.techbmt.hidecalculator.R
import com.techbmt.hidecalculator.core.BaseActivity
import com.techbmt.hidecalculator.core.MyPreference
import com.techbmt.hidecalculator.databinding.ActivityCalculatorBinding
import com.techbmt.hidecalculator.feature.main.MainActivity
import com.techbmt.hidecalculator.feature.user.setting.SettingActivity
import com.techbmt.hidecalculator.feature.user.setting.language.TypeOfLanguage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale


class CalculatorActivity : BaseActivity<ActivityCalculatorBinding>() {

    private val decimalSymbol =
        DecimalFormatSymbols.getInstance(Locale.ROOT).decimalSeparator.toString()
    private val groupSymbol =
        DecimalFormatSymbols.getInstance(Locale.ROOT).groupingSeparator.toString()
    private var calculationResult = BigDecimal.ZERO
    private var isEqualLastAction = false
    private var oldErrorStatus = false

    override fun initView() {


        binding.apply {
            tvResult.text = "0"
            one.setOnClickListener {
                digitClick(it)
            }
            two.setOnClickListener {
                digitClick(it)
            }
            three.setOnClickListener {
                digitClick(it)
            }
            four.setOnClickListener {
                digitClick(it)
            }
            five.setOnClickListener {
                digitClick(it)
            }
            six.setOnClickListener {
                digitClick(it)
            }
            seven.setOnClickListener {
                digitClick(it)
            }
            eight.setOnClickListener {
                digitClick(it)
            }
            nine.setOnClickListener {
                digitClick(it)
            }
            zero.setOnClickListener {
                digitClick(it)
            }
            add.setOnClickListener {
                inputSymbol(it, "+")
            }
            minus.setOnClickListener {
                inputSymbol(it, "-")
            }
            multiple.setOnClickListener {
                inputSymbol(it, "*")
            }
            divide.setOnClickListener {
                inputSymbol(it, "÷")
            }
            percent.setOnClickListener {
                inputSymbol(it, "%")
            }
            roundBrackets.setOnClickListener {
                parentThesisButton(it)
            }
            clear.setOnClickListener {
                clearDisplay()
            }
            equal.setOnClickListener {
                equalButton()
            }
            semicolon.setOnClickListener {
                inputSymbol(it, decimalSymbol)
            }
            delete.setOnClickListener {
                backSpaceButton(it)
            }
            edtInput.addTextChangedListener(object : TextWatcher {
                private var beforeTextLength = 0
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    beforeTextLength = p0?.length ?: 0
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    resultAction()
                }

                override fun afterTextChanged(p0: Editable?) {
                    // do nothing
                }

            })
        }
    }

    private fun setError(status: Boolean) {
        if (status != oldErrorStatus) {
            // Set error color
            if (status) {
                binding.edtInput.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.red_equal_button
                    )
                )
                binding.tvResult.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.red_equal_button
                    )
                )
            }
            // Clear error color
            else {
                binding.edtInput.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.tvResult.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.grey
                    )
                )
            }
            oldErrorStatus = status
        }
    }

    @SuppressLint("SetTextI18n")
    private fun resultAction() {
        lifecycleScope.launch(Dispatchers.Default) {
            // Reset text color
            setError(false)

            val calculation = binding.edtInput.text.toString()

            if (calculation != "") {
                division_by_0 = false
                domain_error = false
                syntax_error = false
                is_infinity = false
                require_real_number = false

                val calculationTmp = Expression().getCleanExpression(
                    binding.edtInput.text.toString(),
                    decimalSymbol,
                    groupSymbol
                )
                calculationResult =
                    Calculator(10).evaluate(
                        calculationTmp,
                        true
                    )

                // If result is a number and it is finite
                if (!(division_by_0 || domain_error || syntax_error || is_infinity || require_real_number)) {

                    // Round
                    calculationResult = roundResult(calculationResult)
                    var formattedResult = NumberFormatter.format(
                        calculationResult.toString().replace(".", decimalSymbol),
                        decimalSymbol,
                        groupSymbol
                    )

                    // Remove zeros at the end of the results (after point)
                    if (!(calculationResult >= BigDecimal(
                            999999999
                        ) || calculationResult <= BigDecimal(0.1))
                    ) {
                        val resultSplited = calculationResult.toString().split('.')
                        if (resultSplited.size > 1) {
                            val resultPartAfterDecimalSeparator = resultSplited[1].trimEnd('0')
                            var resultWithoutZeros = resultSplited[0]
                            if (resultPartAfterDecimalSeparator != "") {
                                resultWithoutZeros =
                                    resultSplited[0] + "." + resultPartAfterDecimalSeparator
                            }
                            formattedResult = NumberFormatter.format(
                                resultWithoutZeros.replace(
                                    ".",
                                    decimalSymbol
                                ), decimalSymbol, groupSymbol
                            )
                        }
                    }


                    withContext(Dispatchers.Main) {
                        if (formattedResult != calculation) {
                            binding.tvResult.text = formattedResult
                        } else {
                            binding.tvResult.text = ""
                        }
                    }

                } else withContext(Dispatchers.Main) {
                    if (is_infinity && !division_by_0 && !domain_error && !require_real_number) {
                        if (calculationResult < BigDecimal.ZERO) binding.tvResult.text =
                            "-" + getString(
                                R.string.infinity
                            )
                        else binding.tvResult.text = getString(R.string.value_too_large)
                    } else {
                        withContext(Dispatchers.Main) {
                            binding.tvResult.text = ""
                        }
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    binding.tvResult.text = ""
                }
            }
        }
    }


    private fun equalButton() {
        lifecycleScope.launch(Dispatchers.Default) {

            val calculation = binding.edtInput.text.toString()
            if (calculation != "") {

                val password = calculation.replace(",", "")
                if (password == MyPreference(this@CalculatorActivity).getUserPassword()) {
                    startActivity(Intent(this@CalculatorActivity, MainActivity::class.java))
                    finish()
                }

                val resultString = calculationResult.toString()
                var formattedResult = NumberFormatter.format(
                    resultString.replace(".", decimalSymbol),
                    decimalSymbol,
                    groupSymbol
                )

                // If result is a number and it is finite
                if (!(division_by_0 || domain_error || syntax_error || is_infinity || require_real_number)) {

                    // Remove zeros at the end of the results (after point)
                    val resultSplited = resultString.split('.')
                    if (resultSplited.size > 1) {
                        val resultPartAfterDecimalSeparator = resultSplited[1].trimEnd('0')
                        var resultWithoutZeros = resultSplited[0]
                        if (resultPartAfterDecimalSeparator != "") {
                            resultWithoutZeros =
                                resultSplited[0] + "." + resultPartAfterDecimalSeparator
                        }
                        formattedResult = NumberFormatter.format(
                            resultWithoutZeros.replace(
                                ".",
                                decimalSymbol
                            ), decimalSymbol, groupSymbol
                        )
                    }

                    // Hide the cursor before updating binding.input to avoid weird cursor movement
                    withContext(Dispatchers.Main) {
                        binding.edtInput.isCursorVisible = false
                    }

                    // Display result
                    withContext(Dispatchers.Main) { binding.edtInput.setText(formattedResult) }

                    // Set cursor
                    withContext(Dispatchers.Main) {
                        // Scroll to the end
                        binding.edtInput.setSelection(binding.edtInput.length())

                        // Hide the cursor (do not remove this, it's not a duplicate)
                        binding.edtInput.isCursorVisible = false

                        // Clear resultDisplay
                        binding.tvResult.text = ""
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        if (syntax_error) {
                            setError(true)
                            binding.tvResult.text = getString(R.string.syntax_error)
                        } else if (domain_error) {
                            setError(true)
                            binding.tvResult.text = getString(R.string.domain_error)
                        } else if (require_real_number) {
                            setError(true)
                            binding.tvResult.text = getString(R.string.require_real_number)
                        } else if (division_by_0) {
                            setError(true)
                            binding.tvResult.text = getString(R.string.division_by_0)
                        } else if (is_infinity) {
                            if (calculationResult < BigDecimal.ZERO) binding.tvResult.text =
                                "-" + getString(
                                    R.string.infinity
                                )
                            else binding.tvResult.text = getString(R.string.value_too_large)
                            //} else if (result.isNaN()) {
                            //    setErrorColor(true)
                            //    binding.resultDisplay.setText(getString(R.string.math_error))
                        } else {
                            binding.tvResult.text = formattedResult
                            isEqualLastAction =
                                true // Do not clear the calculation (if you click into a number) if there is an error
                        }
                    }
                }
            } else {
                withContext(Dispatchers.Main) { binding.tvResult.text = "" }
            }
        }
    }

    private fun roundResult(result: BigDecimal): BigDecimal {

        var newResult = result.setScale(10, RoundingMode.HALF_EVEN)
        if ((newResult >= BigDecimal(999999999) || newResult <= BigDecimal(
                0.1
            ))
        ) {
            val scientificString = String.format(Locale.ENGLISH, "%.4g", result)
            newResult = BigDecimal(scientificString)
        }

        // Fix how is displayed 0 with BigDecimal
        val tempResult = newResult.toString().replace("E-", "").replace("E", "")
        val allCharsEqualToZero = tempResult.all { it == '0' }
        if (
            allCharsEqualToZero
            || newResult.toString().startsWith("0E")
        ) {
            return BigDecimal.ZERO
        }

        return newResult
    }

    private fun clearDisplay() {
        binding.apply {
            edtInput.setText("")
            tvResult.text = ""
        }
    }

    private fun parentThesisButton(view: View) {
        binding.apply {
            val cursorPos = edtInput.selectionStart
            val textLength = edtInput.text.length
            var openParentheses = 0
            var closeParentheses = 0

            val text = edtInput.text.toString()

            for (i in 0 until cursorPos) {
                if (text[i] == '(') {
                    openParentheses += 1
                }
                if (text[i] == ')') {
                    closeParentheses += 1
                }
            }

            if (
                !(textLength > cursorPos && edtInput.text.toString()[cursorPos] in "×÷+-^")
                && (
                        openParentheses == closeParentheses
                                || edtInput.text.toString()[cursorPos - 1] == '('
                                || edtInput.text.toString()[cursorPos - 1] in "×÷+-^"
                        )
            ) {
                updateInput(view, "(")
            } else {
                updateInput(view, ")")
            }
        }
    }

    private fun digitClick(view: View) {
        updateInput(view, (view as Button).text.toString())
    }

    private fun updateInput(view: View, value: String) {
        val valueNoSeparators = value.replace(groupSymbol, "")
        val isValueInt = valueNoSeparators.toIntOrNull() != null

        // Reset input with current number if following "equal"
        if (isEqualLastAction) {
            if (isValueInt || value == decimalSymbol) {
                binding.edtInput.setText("")
            } else {
                binding.edtInput.setSelection(binding.edtInput.text.length)
                binding.scrollInput.fullScroll(HorizontalScrollView.FOCUS_RIGHT)
            }
            isEqualLastAction = false
        }

        if (!binding.edtInput.isCursorVisible) {
            binding.edtInput.isCursorVisible = true
        }

        lifecycleScope.launch(Dispatchers.Default) {
            val formerValue = binding.edtInput.text.toString()
            val cursorPosition = binding.edtInput.selectionStart
            val leftValue = formerValue.subSequence(0, cursorPosition).toString()
            val leftValueFormatted =
                NumberFormatter.format(leftValue, decimalSymbol, groupSymbol)
            val rightValue = formerValue.subSequence(cursorPosition, formerValue.length).toString()

            val newValue = leftValue + value + rightValue

            var newValueFormatted =
                NumberFormatter.format(newValue, decimalSymbol, groupSymbol)

            withContext(Dispatchers.Main) {
                // Avoid two decimalSeparator in the same number
                // when you click on the decimalSeparator button
                if (value == decimalSymbol && decimalSymbol in binding.edtInput.text.toString()) {
                    if (binding.edtInput.text.toString().isNotEmpty()) {
                        var lastNumberBefore = ""
                        if (cursorPosition > 0 && binding.edtInput.text.toString()
                                .substring(0, cursorPosition)
                                .last() in "0123456789\\$decimalSymbol"
                        ) {
                            lastNumberBefore = NumberFormatter.extractNumbers(
                                binding.edtInput.text.toString().substring(0, cursorPosition),
                                decimalSymbol
                            ).last()
                        }
                        var firstNumberAfter = ""
                        if (cursorPosition < binding.edtInput.text.length - 1) {
                            firstNumberAfter = NumberFormatter.extractNumbers(
                                binding.edtInput.text.toString()
                                    .substring(cursorPosition, binding.edtInput.text.length),
                                decimalSymbol
                            ).first()
                        }
                        if (decimalSymbol in lastNumberBefore || decimalSymbol in firstNumberAfter) {
                            return@withContext
                        }
                    }
                }

                // Update Display
                binding.edtInput.setText(newValueFormatted)

                // Set cursor position
                if (isValueInt) {
                    val cursorOffset = newValueFormatted.length - newValue.length
                    binding.edtInput.setSelection(cursorPosition + value.length + cursorOffset)
                } else {
                    binding.edtInput.setSelection(leftValueFormatted.length + value.length)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun inputSymbol(view: View, symbol: String) {
        binding.apply {
            val textLength = edtInput.text.length
            if (textLength > 0) {
                val cursorPos = edtInput.selectionStart
                Log.i("pos", cursorPos.toString())
                val nextChar =
                    if (textLength - cursorPos > 0) edtInput.text[cursorPos].toString() else "0"
                val previousChar =
                    if (cursorPos > 0) edtInput.text[cursorPos - 1].toString() else "0"
                if (symbol != previousChar && symbol != nextChar && previousChar != decimalSymbol
                    && (previousChar != "(" || symbol != "-")
                ) {
                    if (previousChar.matches("[+\\-÷×^]".toRegex())) {
                        val leftValue =
                            edtInput.text.subSequence(0, cursorPos - 1).toString()
                        val rightValue =
                            edtInput.text.subSequence(cursorPos, textLength).toString()
                        if (symbol == "-") {
                            if (previousChar in "+-") {
                                edtInput.setText(leftValue + symbol + rightValue)
                                edtInput.setSelection(cursorPos)
                            } else {
                                edtInput.setText(leftValue + previousChar + symbol + rightValue)
                                edtInput.setSelection(cursorPos + 1)
                            }
                        } else if (cursorPos > 1 && edtInput.text[cursorPos - 2] != '(') {
                            edtInput.setText(leftValue + symbol + rightValue)
                            edtInput.setSelection(cursorPos)
                        } else if (symbol == "+") {
                            edtInput.setText(leftValue + rightValue)
                            edtInput.setSelection(cursorPos - 1)
                        }
                    } else if (nextChar.matches("[+\\-÷×^%!]".toRegex()) && symbol != "%") {
                        val leftValue = edtInput.text.subSequence(0, cursorPos).toString()
                        val rightValue =
                            edtInput.text.subSequence(cursorPos + 1, textLength).toString()
                        if (cursorPos > 0 && previousChar != "(") {
                            edtInput.setText(leftValue + symbol + rightValue)
                            edtInput.setSelection(cursorPos + 1)
                        } else if (symbol == "+") edtInput.setText(leftValue + rightValue)
                    } else if (cursorPos > 0 || nextChar != "0" && symbol == "-") {
                        updateInput(view, symbol)
                    }
                }
            } else {
                if (symbol == "-") updateInput(view, symbol)
            }
        }
    }

    override fun getLayout(): ActivityCalculatorBinding {
        return ActivityCalculatorBinding.inflate(layoutInflater)
    }

    private fun backSpaceButton(view: View) {
        var cursorPosition = binding.edtInput.selectionStart
        val textLength = binding.edtInput.text.length
        var newValue = ""
        var isFunction = false
        var isDecimal = false
        var functionLength = 0

        if (isEqualLastAction) {
            cursorPosition = textLength
        }

        if (cursorPosition != 0 && textLength != 0) {
            // Check if it is a function to delete
            val functionsList =
                listOf("cos⁻¹(", "sin⁻¹(", "tan⁻¹(", "cos(", "sin(", "tan(", "ln(", "log(", "exp(")
            for (function in functionsList) {
                val leftPart = binding.edtInput.text.subSequence(0, cursorPosition).toString()
                if (leftPart.endsWith(function)) {
                    newValue =
                        binding.edtInput.text.subSequence(0, cursorPosition - function.length)
                            .toString() +
                                binding.edtInput.text.subSequence(cursorPosition, textLength)
                                    .toString()
                    isFunction = true
                    functionLength = function.length - 1
                    break
                }
            }
            // Else
            if (!isFunction) {
                // remove the grouping separator
                val leftPart = binding.edtInput.text.subSequence(0, cursorPosition).toString()
                val leftPartWithoutSpaces = leftPart.replace(groupSymbol, "")
                functionLength = leftPart.length - leftPartWithoutSpaces.length

                newValue = leftPartWithoutSpaces.subSequence(0, leftPartWithoutSpaces.length - 1)
                    .toString() +
                        binding.edtInput.text.subSequence(cursorPosition, textLength).toString()

                isDecimal = binding.edtInput.text[cursorPosition - 1] == decimalSymbol[0]
            }

            // Handle decimal deletion as a special case when finding cursor position
            var rightSideCommas = 0
            if (isDecimal) {
                val oldString = binding.edtInput.text
                var immediateRightDigits = 0
                var index = cursorPosition
                // Find number of digits that were previously to the right of the decimal
                while (index < textLength && oldString[index].isDigit()) {
                    index++
                    immediateRightDigits++
                }
                // Determine how many thousands separators that gives us to our right
                if (immediateRightDigits > 3)
                    rightSideCommas = immediateRightDigits / 3
            }

            val newValueFormatted =
                NumberFormatter.format(newValue, decimalSymbol, groupSymbol)
            var cursorOffset = newValueFormatted.length - newValue.length - rightSideCommas
            if (cursorOffset < 0) cursorOffset = 0

            binding.edtInput.setText(newValueFormatted)
            binding.edtInput.setSelection((cursorPosition - 1 + cursorOffset - functionLength).takeIf { it > 0 }
                ?: 0)
        }
    }


}