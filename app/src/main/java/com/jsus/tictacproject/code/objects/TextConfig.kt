package com.jsus.tictacproject.code.objects

import android.content.Context
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.jsus.tictacproject.R

class TextConfig(private val context: Context) {

    private fun getItString(editText: EditText, layout: TextInputLayout): String?{
        layout.error = null
        return if (editText.text.toString().isEmpty()) null
        else editText.text.toString().trim()
    }

    fun getInfo(editText: EditText, layout: TextInputLayout, op: Int): String?{
        return when(op){
            1 -> { //Get Name
                val name = getItString(editText, layout)
                if (!isValid(name, op)) showError(layout, R.string.error_name, context)
                else name
            }
            2 -> { //Get Description
                val desc = getItString(editText, layout)
                if (!isValid(desc, op) && desc != null) {
                    showError(layout, R.string.error_desc, context)
                    "-1"
                }
                else desc
            }
            else -> {
                showMe(context, context.getString(R.string.error))
                null
            }
        }
    }

    fun showMe(context: Context, string: String){
        Toast.makeText(context, string, Toast.LENGTH_LONG).show()
    }

    fun isValid(string: String?, op: Int): Boolean{
        val validName = Regex("[A-Za-z0-9\\s]{1,50}")
        val validDesc = Regex("[A-Za-z0-9\\s]{1,100}")
        val validUser = Regex("[A-Za-z0-9\\S]{4,25}")
        val validPass = Regex("^(?=(?:[^a-zA-Z]*[a-zA-Z]){3})" +
                "(?=(?:[^\\p{Punct}]*\\p{Punct}){3})(?=(?:[^0-9]*[0-9]){3}).{9,32}$")
        val validNameProject = Regex("[A-Za-z0-9\\s]{5,25}")

        return if(string != null){
            when (op){
                1 -> validName.matches(string)
                2 -> validDesc.matches(string)
                /*2 -> validUser.matches(string)
                4 -> Patterns.EMAIL_ADDRESS.matcher(string).matches()
                5 -> validPass.matches(string)
                6 -> validNameProject.matches(string)*/
                else -> false
            }
        } else false
    }

    fun showErrorNClean(layout: TextInputLayout, editText: EditText, error: Int, context: Context): String?{
        clearText(editText)
        return showError(layout, error, context)
    }

    fun showError(layout: TextInputLayout, error: Int, context: Context): String?{
        layout.error = context.getString(error)
        return null
    }

    fun clearText(editText: EditText){
        editText.setText("")
    }

}