package com.example.movielopp
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.example.movielopp.Fragments.ListFilmFragment
import com.example.movielopp.Fragments.LoginFragment
import com.example.movielopp.Fragments.RegisterFragment


class MainActivity : AppCompatActivity(),LoginFragment.OnButtonLoginPressedListener, LoginFragment.OnTextRegistredPressedListener, RegisterFragment.OnGoToLoginPressed, RegisterFragment.OnRegistrationConfirmPressed{

    override fun onLoginPressed() {
        val listFragmenfilm = ListFilmFragment()
        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container, listFragmenfilm).
            commit()
    }

    override fun onRegistrationConfirmPressed() {
        val loginFragment = LoginFragment()
        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container, loginFragment).
            commit()

    }

    override fun onGoToLoginPressed() {
        val loginFragment = LoginFragment()
        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container, loginFragment).
            commit()
    }

    override fun onRegisteredPressed(username: String) {
        val fragmentRegister = RegisterFragment.newInstance(username)
        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container, fragmentRegister).
            commit()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val fragmentListFilms = ListFilmFragment()
        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container, fragmentListFilms).
            commit()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.signInButton -> {
                val signInFragment = LoginFragment()
                supportFragmentManager.
                    beginTransaction().
                    replace(R.id.main_container, signInFragment).
                    commit()
                true

            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }
}
