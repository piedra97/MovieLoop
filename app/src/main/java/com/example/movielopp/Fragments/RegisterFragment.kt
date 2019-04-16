package com.example.movielopp.Fragments


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import com.example.movielopp.Interfaces.OnGetRequestTokenCallback
import com.example.movielopp.Model.User
import com.example.movielopp.R
import com.example.movielopp.RequestTokenRepository
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_register.*
import java.util.regex.Pattern


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class RegisterFragment : Fragment() {


    interface OnRegistrationConfirmPressed {
        fun onRegistrationConfirmPressed()
    }

    interface OnGoToLoginPressed {
        fun onGoToLoginPressed()
    }

    private lateinit var buttonRegisteredListener: OnRegistrationConfirmPressed
    private lateinit var textGoToLoginPressed: OnGoToLoginPressed
    private var fieldsOk = false
    private lateinit var progressBar:ProgressBar
    private lateinit var database:FirebaseDatabase
    private lateinit var auth:FirebaseAuth
    private var authCreated = false
    var requestToken:String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        progressBar= ProgressBar(context)

        FirebaseApp.initializeApp(context!!)
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()


        val requestTokenRepository = RequestTokenRepository.instance
        requestTokenRepository.getRequestToken(object: OnGetRequestTokenCallback {

            override fun onSuccess(request_token: String) {
                requestToken = request_token
            }

            override fun onError() {
                Toast.makeText(context, "La llamada del Request Token ha fallado", Toast.LENGTH_LONG).show()
            }

        })


        signUp.setOnClickListener {
            fieldsOk = true
            checkFields()
            if (fieldsOk) {
                createNewAccount()

            }
        }

        goToLogin.setOnClickListener {
            textGoToLoginPressed.onGoToLoginPressed()
        }


    }


    private fun createNewAccount() {
        val mail = email.text.toString()
        val password = password1.text.toString()

        auth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener { task ->

            if (task.isComplete) {
                saveUserInDB(password, requestToken)

            } else {
                email.error = "Ya hay una cuenta registrada con este email."
            }
        }
    }



    private fun saveUserInDB(password:String, requestToken:String?) {
        val name = username.text.toString()
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, name, password, requestToken!!, " ")

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Finally we saved the user to Firebasa Database")
                buttonRegisteredListener.onRegistrationConfirmPressed()
            }
    }


    override fun onStart() {
        super.onStart()
        updateText()
    }
    private fun updateText() {
        val usernameToRegister = arguments!!.getSerializable("username")
        username.text = Editable.Factory.getInstance().newEditable(usernameToRegister.toString())
    }


    private fun checkFields() {
        checkUserName()
        checkEmail()
        checkPassword()
        checkRepeatPassword()
    }


    private fun checkRepeatPassword() {
        if (password2.text.toString() != password1.text.toString()) {
            password1.error = "Passwords doesn't match"
            fieldsOk = false
        }
    }

    private fun checkPassword(){
        val password = password1.text.toString()
        if (password.isEmpty() || password.length < 8) {
            password1.error = "The password cannot be empty or have a length of less then 8 characters"
            fieldsOk = false
        }
    }

    private fun checkUserName() {
        val user = username.text.toString()
        if (!Pattern.compile("^[a-zA-Z0-9]+$").matcher(user).matches()) {
            username.error = "Username not valid"
            fieldsOk = false
        }
    }

    private fun checkEmail() {
        val mail = email.text.toString()
        if (!Pattern.compile(".+\\@.+\\..+").matcher(mail).matches()) {
            email.error = "Mail no válido"
            fieldsOk = false
        }
    }

    companion object {
        fun newInstance(username: String):RegisterFragment{
            val fragmentRegister = RegisterFragment()
            val args = Bundle()

            args.putSerializable("username", username)
            fragmentRegister.arguments = args

            return fragmentRegister
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        buttonRegisteredListener = context as OnRegistrationConfirmPressed
        textGoToLoginPressed = context as OnGoToLoginPressed
    }




}
