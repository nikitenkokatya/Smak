package com.example.smak

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.preference.ListPreference
import com.example.smak.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener{
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var topLevelDestinations: Set<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        topLevelDestinations = setOf(R.id.welcomeFragment, R.id.smakFragment,
            R.id.buscadorFragment2, R.id.createFragment2, R.id.comprasFragment2, R.id.profileFragment2)

        val navController = findNavController(R.id.nav_host_fragment_content_main)

        navController.addOnDestinationChangedListener(this)

        // Configurar el primer destino
        navController.navigate(R.id.welcomeFragment)

        appBarConfiguration = AppBarConfiguration.Builder(topLevelDestinations)
            .build()

        setupActionBarWithNavController(navController, appBarConfiguration)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottnav)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> navController.navigate(R.id.smakFragment)
                R.id.nav_buscador -> navController.navigate(R.id.buscadorFragment2)
                R.id.nav_crear -> navController.navigate(R.id.createFragment2)
                R.id.nav_compras -> navController.navigate(R.id.comprasFragment2)
                R.id.nav_profile -> navController.navigate(R.id.profileFragment2)
            }
            true
        }

        setTheme()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        if (destination.id in topLevelDestinations) {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        } else {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setTheme() {
        val modoValue =
            getSharedPreferences("settings", Context.MODE_PRIVATE)!!.getString("modo", "0")

        when (modoValue!!.toInt()) {
            0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    fun setAppBarGone() {
        supportActionBar?.hide()
    }

    fun setBottomNavGone() {
        binding.bottnav.visibility = View.INVISIBLE
    }

    fun setBottomNavVisible() {
        binding.bottnav.visibility = View.VISIBLE
    }

    /* fun setAppBarVisible() {
         supportActionBar!!.show()
         binding.appBarLayout.visibility = View.VISIBLE
     }*/
    fun setAppBarVisible() {
        supportActionBar?.show()
        binding.appBarLayout.visibility = View.VISIBLE
    }

    fun updateAppBar(title: String?) {
        setAppBarVisible()
        supportActionBar!!.title = title
    }

    fun setDefaultHighlight() {
        val menuItem = binding.bottnav.menu.findItem(R.id.nav_home)
        menuItem?.isChecked = true
    }

    companion object {
        const val CHANNEL_ID = "dummy channel"
    }

    fun createNotificationChannel() {
        val notificationManager: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            CHANNEL_ID, "Important Notification Channel",
            NotificationManager.IMPORTANCE_HIGH,
        ).apply {
            description = "This notification contains important announcement, etc."
        }
        notificationManager.createNotificationChannel(channel)
    }
}


/*private lateinit var appBarConfiguration: AppBarConfiguration
   private lateinit var binding: ActivityMainBinding
   var cont :Int = 0

   override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)

       binding = ActivityMainBinding.inflate(layoutInflater)
       setContentView(binding.root)

       setSupportActionBar(binding.toolbar)

       val navController = findNavController(R.id.nav_host_fragment_content_main)
       appBarConfiguration = AppBarConfiguration(navController.graph)
       setupActionBarWithNavController(navController, appBarConfiguration)

       val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottnav)
       bottomNavigationView.setOnItemSelectedListener { item ->
           when(item.itemId) {
               R.id.nav_home -> {
                   replaceFragment(SmakFragment())
                   cont = 0
                   true
               }
               R.id.nav_buscador -> {
                   replaceFragment(BuscadorFragment())
                   cont = 1
                   true
               }
               R.id.nav_crear -> {
                   replaceFragment(CreateFragment())
                   cont = 2
                   true
               }
               R.id.nav_compras -> {
                   replaceFragment(ComprasFragment())
                   cont = 3
                   true
               }
               R.id.nav_profile -> {
                   replaceFragment(ProfileFragment())
                   cont = 4
                   true
               }
               else -> false
           }
       }

       // Verificar si hay una sesión iniciada
       if (isLoggedIn()) {
           // Si hay una sesión iniciada, muestra la pantalla principal
           replaceFragment(SmakFragment())
           bottomNavigationView.visibility = View.VISIBLE
       } else {
           // Si no hay sesión iniciada, muestra el fragmento de inicio de sesión
           replaceFragment(FirstFragment())
           bottomNavigationView.visibility = View.GONE
       }
   }

   private fun isLoggedIn(): Boolean {
       val currentUser = FirebaseAuth.getInstance().currentUser
       return currentUser != null
   }

   private fun replaceFragment(fragment: Fragment) {
       supportFragmentManager.beginTransaction()
           .replace(R.id.include, fragment)
           .commit()
   }



   override fun onCreateOptionsMenu(menu: Menu): Boolean {
       menuInflater.inflate(R.menu.menu_main, menu)
       return true
   }

   override fun onOptionsItemSelected(item: MenuItem): Boolean {
       return when (item.itemId) {
           R.id.action_settings -> true
           else -> super.onOptionsItemSelected(item)
       }
   }

   override fun onSupportNavigateUp(): Boolean {
       val navController = findNavController(R.id.nav_host_fragment_content_main)
       return navController.navigateUp(appBarConfiguration)
               || super.onSupportNavigateUp()
   }*/