package com.ilyasov.sci_king.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.ilyasov.sci_king.R
import com.ilyasov.sci_king.util.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * Main fragment -- container for bottom navigation
 */
@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {
    private lateinit var buttonSettings: AppCompatImageButton

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        setupBottomNavigationBar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
        buttonSettings = view.findViewById(R.id.img_settings)
        buttonSettings.setOnClickListener {
            Navigation.findNavController(
                requireActivity(),
                R.id.activity_root__fragment__nav_host
            ).navigate(R.id.action__MainFragment__to__ChengeTheme_Flow)
        }
    }

    private fun setupBottomNavigationBar() {
        val navGraphIds = listOf(
            R.navigation.search_nav_graph,
            R.navigation.saved_articles_nav_graph,
            R.navigation.notes_nav_graph,
            R.navigation.profile_nav_graph
        )

        // Setup the bottom navigation view with a list of navigation graphs
        fragment_main_bottom_navigation.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = childFragmentManager,
            containerId = R.id.fragment_main_nav_host_container,
            intent = requireActivity().intent
        )
    }
}