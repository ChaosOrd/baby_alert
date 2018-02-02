package com.baby_alert;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GatherReadingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GatherReadingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GatherReadingsFragment extends Fragment {
    private ReadingsGatheredCallback readingsCallback;
    private static final String DEVICE_HOST = "HostKey";
    private static final String TAG = "GatherReadingsFragment";
    private GatherReadingsTask gatherReadingsTask;
    private String deviceHost;

    public GatherReadingsFragment() {
        // Required empty public constructor
    }

    public static GatherReadingsFragment newInstance(FragmentManager fragmentManager, String host) {
        GatherReadingsFragment fragment = (GatherReadingsFragment) fragmentManager
                .findFragmentByTag(GatherReadingsFragment.TAG);
        if (fragment == null) {
            fragment = new GatherReadingsFragment();
            Bundle args = new Bundle();
            args.putString(DEVICE_HOST, host);
            fragment.setArguments(args);
            fragmentManager.beginTransaction().add(fragment, TAG).commit();
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deviceHost = getArguments().getString(DEVICE_HOST);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gather_readings, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        readingsCallback = (ReadingsGatheredCallback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        readingsCallback = null;
    }

    public void startGatheringReadings() {
        gatherReadingsTask = new GatherReadingsTask(readingsCallback);
        gatherReadingsTask.execute(deviceHost);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
