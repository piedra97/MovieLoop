package com.example.movielopp.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.movielopp.R
import com.example.movielopp.fragments.LoginFragment
import com.example.movielopp.fragments.RegisterFragment

class LoginActivity : AppCompatActivity(), RegisterFragment.OnRegistrationConfirmPressed,  RegisterFragment.OnGoToLoginPressed, LoginFragment.OnTextRegistredPressedListener, LoginFragment.OnButtonLoginPressedListener{

    override fun onRegistrationConfirmPressed() {
        val loginFragment = LoginFragment()
        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container_login, loginFragment).
            commit()

    }

    override fun onGoToLoginPressed() {
        val loginFragment = LoginFragment()
        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container_login, loginFragment).
            commit()
    }

    override fun onRegisteredPressed(username: String) {
        val fragmentRegister = RegisterFragment.newInstance(username)
        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container_login, fragmentRegister).
            commit()
    }

    override fun onLoginPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val signInFragment = LoginFragment()
        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container_login, signInFragment).
            commit()
    }
}
