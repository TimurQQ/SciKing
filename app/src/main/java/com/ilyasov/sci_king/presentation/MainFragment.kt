package com.ilyasov.sci_king.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ilyasov.sci_king.R
import com.ilyasov.sci_king.util.setupWithNavController
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * Main fragment -- container for bottom navigation
 */
class MainFragment : Fragment(R.layout.fragment_main) {

    private var currentNavController: LiveData<NavController>? = null

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
    }

    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {
        val navGraphIds = listOf(
            R.navigation.search_nav_graph,
            R.navigation.saved_articles_nav_graph,
            R.navigation.notes_nav_graph,
            R.navigation.profile_nav_graph
        )

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = fragment_main_bottom_navigation.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = requireActivity().supportFragmentManager,
            containerId = R.id.fragment_main_nav_host_container,
            intent = requireActivity().intent
        )

        currentNavController = controller

        // Fix for crash on app folding (will be crashed in onDestroyView of NavHostFragment, if you use 'Don't keep activities' mode).
        //
        // Caused by: java.lang.IllegalStateException:
        // View androidx.fragment.app.FragmentContainerView{1595b6 V.E...... ......ID 0,0-1080,1584 #7f080099 app:id/fragment_main__nav_host_container}
        // does not have a NavController set
        currentNavController?.observe(viewLifecycleOwner, { liveDataController ->
            Navigation.setViewNavController(requireView(), liveDataController)
        })
    }
}