package ru.netology.nework

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.databinding.ActivityAppBinding
import ru.netology.nework.utils.MAIN
import ru.netology.nework.viewmodel.AuthViewModel
import ru.netology.nework.viewmodel.UserViewModel
import javax.inject.Inject

@AndroidEntryPoint
class AppActivity : AppCompatActivity() {

    @Inject
    lateinit var appAuth: AppAuth


    private val authViewModel by viewModels<AuthViewModel>()

    private val userViewModel by viewModels<UserViewModel>()

    private lateinit var binding: ActivityAppBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        MAIN = this
        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        navView.itemIconTintList = null

        val navController = findNavController(R.id.nav_host_fragment_activity_app)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_posts,
                R.id.nav_user,
                R.id.nav_events,
                -> {
                    navView.visibility = View.VISIBLE
                }
                else -> {
                    navView.visibility = View.GONE
                }
            }
        }

      val appBarConfiguration = AppBarConfiguration(
          setOf(
              R.id.nav_posts,
              R.id.nav_user,
              R.id.nav_events,
          )
      )
       setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)

        val itemIcon = navView.menu.findItem(R.id.profileFragment)

        authViewModel.data.observe(this) { auth ->
            invalidateOptionsMenu()
            if (auth.id == 0L) {
                itemIcon.setIcon(R.drawable.ic_avatar)
            } else {
                userViewModel.getUserById(auth.id)
                itemIcon.setIcon(R.drawable.ic_default_user_profile_image)
            }
            navView.menu.findItem(R.id.profileFragment).setOnMenuItemClickListener {
                if (!authViewModel.authorized) {
                    findNavController(R.id.nav_host_fragment_activity_app)
                        .navigate(R.id.signInFragment)
                    true
                } else {
                    userViewModel.getUserById(auth.id)
                    val bundle = Bundle().apply {
                            putLong("id", auth.id)
                    }

                    findNavController(R.id.nav_host_fragment_activity_app).popBackStack()

                    findNavController(R.id.nav_host_fragment_activity_app)
                        .navigate(R.id.profileFragment, bundle)
                    true
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        menu.let {
            it.setGroupVisible(R.id.unauthentificated, !authViewModel.authorized)
            it.setGroupVisible(R.id.authentificated, authViewModel.authorized)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                   findNavController(R.id.nav_host_fragment_activity_app)
                       .navigate(R.id.nav_posts)
                true
            }
            R.id.sign_in -> {
                findNavController(R.id.nav_host_fragment_activity_app)
                    .navigate(R.id.signInFragment)
                true
            }
            R.id.sign_up -> {
                findNavController(R.id.nav_host_fragment_activity_app)
                   .navigate(R.id.signUpFragment)
                true
            }
            R.id.sign_out -> {
                appAuth.removeAuth()
                findNavController(R.id.nav_host_fragment_activity_app)
                    .navigate(R.id.nav_posts)
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        }
    }
}