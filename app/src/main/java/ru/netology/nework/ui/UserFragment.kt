package ru.netology.nework.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.netology.nework.R
import ru.netology.nework.adapter.OnUserInteractionListener
import ru.netology.nework.adapter.UserAdapter
import ru.netology.nework.databinding.FragmentUserBinding
import ru.netology.nework.dto.User
import ru.netology.nework.entity.toUserEntity
import ru.netology.nework.viewmodel.EventViewModel
import ru.netology.nework.viewmodel.PostViewModel
import ru.netology.nework.viewmodel.UserViewModel

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class UserFragment : Fragment() {

    private val userViewModel by activityViewModels<UserViewModel>()
    private val eventViewModel by activityViewModels<EventViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentUserBinding.inflate(
            inflater,
            container,
            false
        )

        val open = arguments?.getString("open")

        val adapter = UserAdapter(object : OnUserInteractionListener {
            override fun openProfile(user: User) {
                when (open) {
                    "speaker" -> {
                        eventViewModel.setSpeaker(user.id)
                        findNavController().navigateUp()
                    }
                    else -> {
                        userViewModel.getUserById(user.id)
                        val bundle = Bundle().apply {
                            putLong("id", user.id)

                        }
                        findNavController().apply {
                            this.popBackStack(R.id.nav_user, true)
                            this.navigate(R.id.profileFragment, bundle)
                        }
                    }
                }
            }
        })

        binding.fragmentListUsers.adapter = adapter

        userViewModel.data.observe(viewLifecycleOwner) {
            adapter.setData(it.toUserEntity())
        }

        userViewModel.dataState.observe(viewLifecycleOwner) {
            when {
                it.error -> {
                    Toast.makeText(context, R.string.error_loading, Toast.LENGTH_SHORT)
                        .show()
                }
            }
            binding.progressBarFragmentUsers.isVisible = it.loading
        }

        val search = binding.searchView

        search.isSubmitButtonEnabled = true

        search.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = true

                override fun onQueryTextChange(query: String?): Boolean {
                    if (query != null) {
                        searchDatabase(query, adapter)
                    }
                    return true
                }
            }
        )

        return binding.root
    }

    private fun searchDatabase(query: String, adapter: UserAdapter) {
        val searchQuery = "%$query%"

        userViewModel.searchDatabase(searchQuery).observe(this) { list ->
            list.let {
                adapter.setData(it)
            }
        }
    }
}