package ru.netology.nework.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
class UserFragment : Fragment(), androidx.appcompat.widget.SearchView.OnQueryTextListener{

    private val userViewModel by viewModels<UserViewModel>()
    private val adapter: UserAdapter by lazy { UserAdapter(object : OnUserInteractionListener {
        override fun openProfile(user: User) {
            userViewModel.getUserById(user.id)
            val bundle = Bundle().apply {
                putLong("id", user.id)
                putString("avatar", user.avatar)
                putString("name", user.name)
            }
            findNavController().apply {
                this.popBackStack(R.id.nav_user, true)
                this.navigate(R.id.profileFragment, bundle)
            }}
    })}


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


        binding.fragmentListUsers.adapter = adapter

        userViewModel.data.observe(viewLifecycleOwner)
        {
            adapter.setData(it.toUserEntity())
        }

        userViewModel.dataState.observe(viewLifecycleOwner)
        {
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
        search.setOnQueryTextListener(this)

        return binding.root
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true

    }

    override fun onQueryTextChange(query: String?): Boolean {
        if(query != null){
            searchDatabase(query)
        }
        return true
    }

    private fun searchDatabase(query: String){
        val searchQuery = "%$query%"

        userViewModel.searchDatabase(searchQuery).observe(this, { list ->
            list.let {
                adapter.setData(it)
            }
        })

    }

}