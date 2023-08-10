package ru.netology.nework.ui

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.netology.nework.R
import ru.netology.nework.databinding.FragmentMapBinding
import ru.netology.nework.utils.Companion.Companion.idPoint
import ru.netology.nework.utils.Companion.Companion.latitude
import ru.netology.nework.utils.Companion.Companion.longitude
import ru.netology.nework.utils.Companion.Companion.zoom
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.utils.Companion.Companion.latitudeNew
import ru.netology.nework.utils.Companion.Companion.longitudeNew


@AndroidEntryPoint
class MapsFragment : Fragment() {

    private lateinit var binding: FragmentMapBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(layoutInflater)
        val map = binding.mapview.map
        val latitudeMap = arguments?.latitude ?: 59.945933
        val longitudeMap = arguments?.longitude ?: 30.320045
        val zoomMap = arguments?.zoom ?: 8.0f
        map.move(
            CameraPosition(Point(latitudeMap, longitudeMap), zoomMap, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 5F),
            null
        )

        val open = arguments?.getString("open")
        if (open == "newEvent") {
            binding.buttonSetPoint.visibility = View.VISIBLE
        } else binding.buttonSetPoint.visibility = View.GONE

        map.addCameraListener { _, cameraPosition, _, _ ->

            binding.buttonSetPoint.setOnClickListener {
                val point = cameraPosition.target
                findNavController().navigate(
                    R.id.action_mapFragment_to_newEventFragment,
                    Bundle().apply {
                        latitudeNew = point.latitude
                        longitudeNew = point.longitude
                    }
                )
                true
            }
        }
        return binding.root


    }


    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapview.onStart()
    }

    override fun onStop() {
        binding.mapview.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

}


//open = arguments?.getString("open")
//position =
//if (open == "newEvent") {
//    Point(59.945933, 30.320045)
//} else Point(lat, long)