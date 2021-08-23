package com.ilyasov.sci_king.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.ilyasov.sci_king.BR
import com.ilyasov.sci_king.R
import com.ilyasov.sci_king.databinding.FragmentMainBinding
import com.ilyasov.sci_king.presentation.fragments.base.BaseFragment
import com.ilyasov.sci_king.util.setupWithNavController
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * Main fragment -- container for bottom navigation
 */
class MainFragment : BaseFragment(R.layout.fragment_main) {
    private lateinit var fragmentMainBinding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        fragmentMainBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        fragmentMainBinding.setVariable(BR.mainFragment, this)
        return fragmentMainBinding.root
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        setupBottomNavigationBar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
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
        bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = childFragmentManager,
            containerId = R.id.navHostFragmentForBottomNavigation,
            intent = requireActivity().intent
        )
    }

    fun onClickSettings() {
        Navigation.findNavController(
            requireActivity(),
            R.id.navHostFragmentActivityRoot
        ).navigate(R.id.action__MainFragment__to__ChengeTheme_Flow)
    }
}