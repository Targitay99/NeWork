package ru.netology.nework.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.netology.nework.R
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.databinding.FragmentSignInBinding
import ru.netology.nework.utils.AndroidUtils.hideKeyboard
import ru.netology.nework.viewmodel.SignInViewModel
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : Fragment() {

    @Inject
    lateinit var appAuth: AppAuth

    private val viewModel by viewModels<SignInViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentSignInBinding.inflate(
            inflater,
            container,
            false
        )

        with(binding) {
            textFieldLogin.requestFocus()
            buttonSignIn.setOnClickListener {
                viewModel.authorizationUser(
                    textFieldLogin.editText?.text.toString(),
                    textFieldPassword.editText?.text.toString()
                )
                hideKeyboard(requireView())
            }
        }

        binding.textFieldPassword.setErrorIconOnClickListener {
            binding.textFieldPassword.error = null
        }

        viewModel.data.observe(viewLifecycleOwner) {
            appAuth.setAuth(it.id, it.token)
            findNavController().navigate(R.id.nav_posts)
        }

        binding.buttonSignUp.setOnClickListener {
            findNavController().navigate(R.id.signUpFragment)
        }

        viewModel.dataState.observe(viewLifecycleOwner) { state ->

           if (state.error) {
               Toast.makeText(context, R.string.error_loading, Toast.LENGTH_SHORT)
                .show()
        }
            binding.progressBarFragmentSignIn.isVisible = state.loading
        }

        viewModel.dataLoginState.observe(viewLifecycleOwner){state->
            if (state.loginError) binding.textFieldPassword.error = getString(R.string.error_login)
        }

        return binding.root
    }
}