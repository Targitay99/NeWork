package ru.netology.nework.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.LocationServices
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
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.user_location.UserLocationLayer
import ru.netology.nework.utils.Companion.Companion.latitudeNew
import ru.netology.nework.utils.Companion.Companion.longitudeNew



class MapsFragment : Fragment() , LocationListener, InputListener {

    private lateinit var binding: FragmentMapBinding
    private lateinit var userLocationLayer: UserLocationLayer
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                binding.mapview.apply {
                    userLocationLayer.isVisible = true
                    userLocationLayer.isHeadingEnabled = false
                }
            } else {
                Toast.makeText(context, R.string.no_location, Toast.LENGTH_LONG)
                    .show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(layoutInflater)
        checkPermission()
        val map = binding.mapview.map
        val latitudeMap = arguments?.latitude ?: 59.945933
        val longitudeMap = arguments?.longitude ?: 30.320045
        val zoomMap = arguments?.zoom ?: 8.0f
        map.move(
            CameraPosition(Point(latitudeMap, longitudeMap), zoomMap, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 5F),
            null
        )
        userLocationLayer = MapKitFactory.getInstance().createUserLocationLayer(binding.mapview.mapWindow)

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

            binding.buttonLocation.setOnClickListener {
                try {
                    moveCamera(userLocationLayer.cameraPosition()?.target!!)
                } catch (e: Exception) {
                    Toast.makeText(context, R.string.retry, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }


        return binding.root


    }

    private fun moveCamera(point: Point) {
        binding.mapview.map.move(CameraPosition(point, 10F, 0F, 0F))
    }

    private fun checkPermission() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            -> {
                binding.mapview.apply {
                    userLocationLayer.isVisible = true
                    userLocationLayer.isHeadingEnabled = false
                }

                val fusedLocationProviderClient = LocationServices
                    .getFusedLocationProviderClient(requireActivity())

                fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                    println(it)
                }
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
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

    override fun onLocationChanged(location: Location) {
        TODO("Not yet implemented")
    }

    override fun onMapTap(p0: Map, p1: Point) {
        TODO("Not yet implemented")
    }

    override fun onMapLongTap(p0: Map, p1: Point) {
        TODO("Not yet implemented")
    }

}


//open = arguments?.getString("open")
//position =
//if (open == "newEvent") {
//    Point(59.945933, 30.320045)
//} else Point(lat, long)