package ru.netology.nework.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.job
import okhttp3.internal.http.hasBody
import ru.netology.nework.R
import ru.netology.nework.adapter.UserProfileAdapter
import ru.netology.nework.api.UserApiService
import ru.netology.nework.databinding.FragmentProfileBinding
import ru.netology.nework.dto.Event
import ru.netology.nework.dto.Job
import ru.netology.nework.dto.Post
import ru.netology.nework.dto.User
import ru.netology.nework.utils.Utils
import ru.netology.nework.viewmodel.AuthViewModel
import ru.netology.nework.viewmodel.EventViewModel
import ru.netology.nework.viewmodel.JobViewModel
import ru.netology.nework.viewmodel.PostViewModel
import ru.netology.nework.viewmodel.UserViewModel

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val authViewModel by activityViewModels<AuthViewModel>()

    private val postViewModel by activityViewModels<PostViewModel>()

    private val eventViewModel by activityViewModels<EventViewModel>()

    private val jobViewModel by activityViewModels<JobViewModel>()

    private val userViewModel by activityViewModels<UserViewModel>()

    private var twist = false

      private val profileTitles = arrayOf(
        R.string.title_posts,
        R.string.title_events,
        R.string.title_jobs

    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentProfileBinding.inflate(
            inflater,
            container,
            false
        )
        //     var user:User? = null
        val viewPagerProfile = binding.viewPagerFragmentProfile
        val tabLayoutProfile = binding.tabLayoutFragmentProfile
        val id = arguments?.getLong("id")


        userViewModel.user.observe(viewLifecycleOwner) {

            val user = userViewModel.user.value
            val name = user?.name
            val avatar = user?.avatar

        (activity as AppCompatActivity).supportActionBar?.title = name

        viewPagerProfile.adapter = UserProfileAdapter(this)

        TabLayoutMediator(tabLayoutProfile, viewPagerProfile) { tab, position ->
            tab.text = getString(profileTitles[position])
        }.attach()

        with(binding) {
            textViewUserNameFragmentProfile.text = name

            imageViewUserAvatarFragmentProfile.setOnClickListener {
                val bundle = Bundle().apply {
                    putString("url", avatar)
                }
                findNavController().navigate(R.id.imageAttachmentFragment, bundle)
            }

            Glide.with(imageViewUserAvatarFragmentProfile)
                .load("$avatar")
                .transform(CircleCrop())
                .placeholder(R.drawable.ic_default_user_profile_image)
                .into(imageViewUserAvatarFragmentProfile)
        }
    }

        authViewModel.data.observe(viewLifecycleOwner) {
            if (authViewModel.authorized && id == it.id) {
                binding.fabAdd.visibility = View.VISIBLE
            }
        }

        Utils.hideFirstFab(binding.linearAddEvent)
        Utils.hideFirstFab(binding.linearAddPost)
        Utils.hideFirstFab(binding.linearAddJob)

        binding.fabAdd.setOnClickListener { v ->
            twist = Utils.twistFab(v, !twist)

            if (twist) {
                Utils.showFab(binding.linearAddEvent)
                Utils.showFab(binding.linearAddPost)
                Utils.showFab(binding.linearAddJob)
            } else {
                Utils.hideFab(binding.linearAddEvent)
                Utils.hideFab(binding.linearAddPost)
                Utils.hideFab(binding.linearAddJob)
            }
        }

        binding.fabAddPost.setOnClickListener {
            postViewModel.edit(Post.emptyPost)
            findNavController().navigate(R.id.action_profileFragment_to_newPostFragment)
            twist = false
        }

        binding.fabAddEvent.setOnClickListener {
            eventViewModel.edit(Event.emptyEvent)
            findNavController().navigate(R.id.action_profileFragment_to_newEventFragment)
            twist = false
        }

        binding.fabAddJob.setOnClickListener {
            jobViewModel.edit(Job.emptyJob)
            findNavController().navigate(R.id.action_profileFragment_to_newJobFragment)
            twist = false
        }

        return binding.root
    }
}