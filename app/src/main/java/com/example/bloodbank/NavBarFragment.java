package com.example.bloodbank;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavBarFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class NavBarFragment extends Fragment {

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment NavBarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NavBarFragment newInstance() {
        NavBarFragment fragment = new NavBarFragment();
        return fragment;
    }

    public NavBarFragment() {
        super(R.layout.fragment_nav_bar);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nav_bar, container, false);
    }

}