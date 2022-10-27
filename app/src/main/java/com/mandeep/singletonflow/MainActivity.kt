package com.mandeep.singletonflow

import android.content.res.Configuration
import android.graphics.Insets
import android.hardware.display.DisplayManager
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.widget.Toast
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.window.layout.WindowMetricsCalculator
import com.mandeep.singletonflow.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.delay as delay1

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val windowInsetsController = ViewCompat.getWindowInsetsController(window.decorView)

        windowInsetsController?.isAppearanceLightNavigationBars = true

        /**   starting window sizing operation*/
        // ...

        // Replace with a known container that you can safely add a
        // view to where it won't affect the layout and the view
        // won't be replaced.
        val container: ViewGroup = binding.container

        // Add a utility view to the container to hook into
        // View.onConfigurationChanged. This is required for all
        // activities, even those that don't handle configuration
        // changes. We also can't use Activity.onConfigurationChanged,
        // since there are situations where that won't be called when
        // the configuration changes. View.onConfigurationChanged is
        // called in those scenarios.
        container.addView(object : View(this) {
            override fun onConfigurationChanged(newConfig: Configuration?) {
                super.onConfigurationChanged(newConfig)
                computeWindowSizeClasses()
            }
        })

        computeWindowSizeClasses()

/**   finishing window sizing operation*/

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        CoroutineScope(Dispatchers.Main).launch {
            GFG()
        }

        CoroutineScope(Dispatchers.Main).launch {
            Singleton.update.collect{
                Log.d("efuidgndf",Singleton.toString())
                Log.d("efuidgndf",Singleton.toString())
                Toast.makeText(this@MainActivity,it.toString(),Toast.LENGTH_SHORT).show()
            }
        }




        setSupportActionBar(binding.toolbar)



        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener(object : NavController.OnDestinationChangedListener{
            override fun onDestinationChanged( controller: NavController, destination: NavDestination, arguments: Bundle?) {
               Log.d("gorngrg",destination.displayName.toString())
                      binding.toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_launcher_foreground,null)
            }
        })




        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.fab)
                .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                Toast.makeText(this,"Settings activity is not created",Toast.LENGTH_SHORT).show()

                false}
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    // kotlin program for demonstration of async
   suspend fun GFG()
    {
        Log.i("Async", "Before")
        val resultOne = CoroutineScope(Dispatchers.IO).async { function1() }
        val resultTwo =  CoroutineScope(Dispatchers.IO).async { function2() }
        Log.i("Async", "After")
        val resultText = resultOne.await() + resultTwo.await()
        Log.i("Async", resultText)
    }

    suspend fun function1(): String
    {
        delay1(1000L)
        val message = "function1"
        Log.i("Async", message)
        return message
    }

    suspend fun function2(): String
    {
        delay1(100L)
        val message = "function2"
        Log.i("Async", message)
        return message
    }

    private fun computeWindowSizeClasses() {
        val metrics = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(this)

        val widthDp = metrics.bounds.width() / resources.displayMetrics.density
        val heightDp = metrics.bounds.height() / resources.displayMetrics.density

        val widthWindowSizeClass = when {
            widthDp < 600f -> WindowSizeClass.COMPACT
            widthDp < 840f -> WindowSizeClass.MEDIUM
            else -> WindowSizeClass.EXPANDED
        }

        val heightWindowSizeClass = when {
            heightDp < 480f -> WindowSizeClass.COMPACT
            heightDp < 900f -> WindowSizeClass.MEDIUM
            else -> WindowSizeClass.EXPANDED
        }

        Log.d("fodinfdf","$widthWindowSizeClass  ______ $heightWindowSizeClass")

        // Use widthWindowSizeClass and heightWindowSizeClass.
    }

}
enum class WindowSizeClass { COMPACT, MEDIUM, EXPANDED }
