package com.mile.mile_navigation_engine.Navigation.activities.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mile.mile_navigation_engine.R
import com.mile.mile_navigation_engine.utils.OfflineRouteManager


class TestFragment : Fragment(), OnMapReadyCallback {

    companion object {
        fun newInstance(/*activity: FlutterFragmentActivity*/): TestFragment {
            var frag = TestFragment()
            //frag.mActivity = activity
            return frag
        }
    }

    lateinit var mapView: MapView
    var rootView: View? = null

    /*override fun onCreate(savedInstanceState: Bundle?) {
        // For styling the InstructionView
        setTheme(R.style.CustomInstructionView)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_fragment)


        OfflineRouteManager.fetchTileVersions()

        mapView = findViewById(R.id.mapView)

        mapView.onCreate(savedInstanceState)
        //startNavigationFab.show()

        // Will call onMapReady
        mapView.getMapAsync(this)

    }*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Mapbox.getInstance(activity!!.applicationContext, getString(R.string.access_token))
        super.onCreateView(inflater, container, savedInstanceState)
        rootView = inflater.inflate(R.layout.test_fragment, container, false)

        mapView = rootView!!.findViewById(R.id.mapView)

        mapView.onCreate(savedInstanceState)
        
        mapView.getMapAsync(this)


        return rootView
    }

    override fun onMapReady(mapboxMap: MapboxMap) {

    }
}