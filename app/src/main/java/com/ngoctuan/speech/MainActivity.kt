package com.ngoctuan.speech

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ngoctuan.speech.databinding.ActivityMainBinding
import com.ngoctuan.speech.db.Account
import com.ngoctuan.speech.db.AccountViewModel
import com.ngoctuan.speech.payment.SubBuyAct
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var textToSpeech: TextToSpeech

    private lateinit var mainBinding: ActivityMainBinding
    private val accountViewModel : AccountViewModel by lazy {
        ViewModelProvider(this, AccountViewModel.AccountViewModelFactory(this.application))[AccountViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        textToSpeech = TextToSpeech(this@MainActivity) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech.setLanguage(Locale("en"))

                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(
                        this@MainActivity,
                        "This language is not supported.",
                        Toast.LENGTH_SHORT
                    ).show()
                    mainBinding.speakButton.isEnabled = false
                } else {
                    mainBinding.speakButton.isEnabled = true
                }
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "Can not initate Text-to-Speech.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        with(mainBinding) {
            this.tvClean.setOnClickListener {
                this.editText.setText("")
              //  this.speakButton.isEnabled = false
            }
            this.speakButton.setOnClickListener {
                var acc = accountViewModel.getAccountByID(1)
                    var count: Int = 0
                    if (acc == null) {
                        accountViewModel.insertAccount(Account(1, 10000))
                        count = 10000
                    } else
                        count = acc.count
                    if (count == 0 || getTextLength() > count) {
                        val intent = Intent(this@MainActivity, SubBuyAct::class.java)
                        startActivity(intent)
                    } else {
                        speak()
                        if(count < 1000000)
                            accountViewModel.updateAccount(Account(1,count - getTextLength()))
                    }
        }
          }
    }
    private fun getTextLength() : Int{
        return mainBinding.editText.text.toString().length
    }
    private fun speak() {
        val text = mainBinding.editText.text.toString()
        if (text.isNotEmpty()) {
                // Phát âm từ văn bản
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "speak")
            }
        else {
            Toast.makeText(this, "Please type your text before speak.", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        if (::textToSpeech.isInitialized) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
    }
}