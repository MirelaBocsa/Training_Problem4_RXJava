package com.example.mirela.rxjava

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.ViewAnimator
import com.jakewharton.rxbinding.widget.RxTextView
import kotlinx.android.synthetic.main.activity_main.*
import rx.Observable
import rx.functions.Action1


class MainActivity : AppCompatActivity() {
    private lateinit var emailError: TextView
    private lateinit var passError: TextView
    private lateinit var nrError: TextView

    private lateinit var emailTextField: EditText
    private lateinit var passTextField: EditText
    private lateinit var nrTextField: EditText

    private lateinit var submitBtn: Button

    private val regexEmail = Regex("^[A-Za-z0-9][A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}")
    private val regexPass = Regex("[a-zA-Z0-9]{6,}")
    private val regexNr = Regex("[0-9]{10}")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeFields()

        val emailObservable: Observable<Boolean> = RxTextView.textChanges(emailTextField)
            .map { inputText -> inputText.toString().matches(regexEmail) }
            .distinctUntilChanged()
        emailObservable.subscribe { isValid ->
            if (!isValid) errorEmail.visibility = View.VISIBLE else errorEmail.visibility = View.INVISIBLE
        }

        val passObservable: Observable<Boolean> = RxTextView.textChanges(passTextField)
            .map { inputText -> inputText.toString().matches(regexPass) }
            .distinctUntilChanged()
        passObservable.subscribe { isValid ->
            if (!isValid) errorPass.visibility = View.VISIBLE else errorPass.visibility = View.INVISIBLE
        }

        val nrObservable: Observable<Boolean> = RxTextView.textChanges(nrTextField)
            .map { inputText -> inputText.toString().matches(regexNr) }
            .distinctUntilChanged()
        nrObservable.subscribe { isValid ->
            if (!isValid) errorNr.visibility = View.VISIBLE else errorNr.visibility = View.INVISIBLE
        }

        Observable.combineLatest(
            emailObservable,
            passObservable,
            nrObservable
        ) { emailValid: Boolean, passValid: Boolean, nrValid: Boolean -> emailValid && passValid && nrValid }
            .distinctUntilChanged()
            .subscribe { valid -> btnSubmit.isEnabled = valid }
    }

    private fun initializeFields() {
        emailError = findViewById(R.id.errorEmail)
        passError = findViewById(R.id.errorPass)
        nrError = findViewById(R.id.errorNr)

        emailTextField = findViewById(R.id.emailEditText)
        passTextField = findViewById(R.id.passEditText)
        nrTextField = findViewById(R.id.numberEditText)

        submitBtn = findViewById(R.id.btnSubmit)

    }
}
