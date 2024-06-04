package com.example.smak

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale


class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var preferences: SharedPreferences
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preferences, rootKey)
        initFirebaseUse()

        preferences = activity?.getSharedPreferences("settings", Context.MODE_PRIVATE)!!

        initThemePreference()
        initLogoutPreference()
        initIdiomas()
    }


    private fun initFirebaseUse() {
        val store = Locator.settingsPreferencesRepository

        mAuth = FirebaseAuth.getInstance()

        preferenceManager.preferenceDataStore = store
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }



    private fun initThemePreference() {
        val themeValue = preferences.getString("modo", "0")!!
        val listPreferenceTheme = findPreference<ListPreference>("modo")

        listPreferenceTheme?.summary = listPreferenceTheme?.entries?.get(themeValue.toInt())
        listPreferenceTheme?.value = themeValue

        listPreferenceTheme?.setOnPreferenceChangeListener { preference, newValue ->
            if (preference is ListPreference) {
                val newMode = newValue.toString().toInt()
                preferences.edit()?.putString("modo", newMode.toString())?.apply()

                when (newMode) {
                    0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }

                listPreferenceTheme.summary = listPreferenceTheme.entries?.get(newMode)
            }
            true
        }
    }

    private fun initIdiomas(){

        val langValue = preferences!!.getString(
            getString(R.string.key_lang),
            getString(R.string.def_value_lang))

        val listPreferenceLang = findPreference<ListPreference>(getString(R.string.key_lang))
        listPreferenceLang?.summary = listPreferenceLang?.entries?.get(langValue!!.toInt())
        listPreferenceLang?.value = langValue
        listPreferenceLang?.setOnPreferenceChangeListener { preference, newValue ->
            if (preference is ListPreference) {
                val newLang = newValue.toString()
                preferences.edit()?.putString(getString(R.string.key_lang), newLang)?.apply()

                when(newLang.toInt()){
                    0 -> setLocale(Locale.getDefault().language)
                    1 -> setLocale("es")
                    2 -> setLocale("en")
                }
                requireActivity().recreate()

                listPreferenceLang.summary = listPreferenceLang.entries?.get(newLang.toInt())
            }
            true
        }
    }

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        //Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun initLogoutPreference() {
        val preferenceLogout = findPreference<Preference>("logout")

        preferenceLogout!!.setOnPreferenceClickListener {
            signOutAndStartSignInActivity()
            true
        }
    }

    private fun signOutAndStartSignInActivity() {
        mAuth.signOut()

        mGoogleSignInClient.signOut().addOnCompleteListener(requireActivity()) {
            findNavController().navigate(R.id.action_settingsFragment_to_welcomeFragment)
        }
    }
}