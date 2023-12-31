package ru.netology.nework.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import ru.netology.nework.viewmodel.AuthViewModel
import ru.netology.nework.R
import ru.netology.nework.adapter.*
import ru.netology.nework.databinding.FragmentEventsBinding
import ru.netology.nework.dto.Event
import ru.netology.nework.utils.Companion.Companion.latitude
import ru.netology.nework.utils.Companion.Companion.longitude
import ru.netology.nework.utils.Companion.Companion.zoom
import ru.netology.nework.viewmodel.EventViewModel
import ru.netology.nework.viewmodel.UserViewModel

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class EventsFragment : Fragment() {

    private val eventViewModel by activityViewModels<EventViewModel>()
    private val authViewModel by activityViewModels<AuthViewModel>()
    private val userViewModel by activityViewModels<UserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentEventsBinding.inflate(
            inflater,
            container,
            false
        )

        val adapter = EventAdapter(object : OnEventInteractionListener {

            override fun onOpenEvent(event: Event) {}

            override fun onEditEvent(event: Event) {
                eventViewModel.edit(event)
                val bundle = Bundle().apply {
                    putString("content", event.content)
                    putString("dateTime", event.datetime)
                    event.coordinates?.lat?.let {
                        putDouble("lat", it)
                    }
                    event.coordinates?.long?.let {
                        putDouble("long", it)
                    }

                }
                findNavController()
                    .navigate(R.id.newEventFragment, bundle)
            }

            override fun onRemoveEvent(event: Event) {
                eventViewModel.removeById(event.id)
            }

            override fun onOpenSpeakers(event: Event) {
                userViewModel.getUsersIds(event.speakerIds)
                if (event.speakerIds.isEmpty()) {
                    Toast.makeText(context, R.string.no_speakers, Toast.LENGTH_SHORT)
                        .show()
                } else {
                    findNavController().navigate(R.id.bottomSheetFragment)
                }
            }

            override fun onOpenMap(event: Event) {
                if (event.coordinates?.lat != null) {
                    findNavController().navigate(
                        R.id.action_nav_events_to_mapFragment,
                        Bundle().apply {
                            latitude = event.coordinates?.lat ?: 59.945933
                            longitude = event.coordinates?.long ?: 30.320045
                            zoom = 17.0f
                        }
                    )
                }else{
                    Toast.makeText(activity, R.string.error_coordnate, Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onOpenImageAttachment(event: Event) {
                val bundle = Bundle().apply {
                    putString("url", event.attachment?.url)
                }
                findNavController().navigate(R.id.imageAttachmentFragment, bundle)
            }

            override fun onLikeEvent(event: Event) {
                if (authViewModel.authorized) {
                    if (!event.likedByMe)
                        eventViewModel.likeById(event.id)
                    else eventViewModel.unlikeById(event.id)
                } else {
                    Toast.makeText(activity, R.string.error_auth, Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onParticipateEvent(event: Event) {
                if (authViewModel.authorized) {
                    if (!event.participatedByMe)
                        eventViewModel.participate(event.id)
                    else eventViewModel.doNotParticipate(event.id)
                } else {
                    Toast.makeText(activity, R.string.error_auth, Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onShareEvent(event: Event) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, event.content)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(intent, "Share Event")
                startActivity(shareIntent)
            }

            override fun onOpenLikers(event: Event) {
                userViewModel.getUsersIds(event.likeOwnerIds)
                if (event.likeOwnerIds.isEmpty()) {
                    Toast.makeText(context, R.string.no_likers, Toast.LENGTH_SHORT)
                        .show()
                } else {
                    findNavController().navigate(R.id.bottomSheetFragment)
                }
            }

            override fun onOpenParticipants(event: Event) {
                userViewModel.getUsersIds(event.participantsIds)
                if (event.participantsIds.isEmpty()) {
                    Toast.makeText(context, R.string.no_participants, Toast.LENGTH_SHORT)
                        .show()
                } else {
                    findNavController().navigate(R.id.bottomSheetFragment)
                }
            }

            override fun onOpenProfile(event: Event) {
                val bundle = Bundle().apply {
                    userViewModel.getUserById(event.authorId)
                    putLong("id", event.authorId)
                }
                findNavController().apply {
                    this.popBackStack(R.id.nav_user, true)
                    this.navigate(R.id.profileFragment, bundle)
                }
            }
        })

        binding.recyclerViewContainerFragmentEvents.adapter = adapter

        lifecycleScope.launchWhenCreated {
            eventViewModel.data.collectLatest(adapter::submitData)
        }

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest { state ->
                binding.swipeRefreshFragmentEvents.isRefreshing =
                    state.refresh is LoadState.Loading
                binding.textViewEmptyTextFragmentEvents.isVisible =
                    adapter.itemCount < 1
            }
        }

        eventViewModel.dataState.observe(viewLifecycleOwner) {
            when {
                it.error -> {
                    Toast.makeText(context, R.string.error_loading, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        binding.swipeRefreshFragmentEvents.setOnRefreshListener(adapter::refresh)

        return binding.root
    }
}