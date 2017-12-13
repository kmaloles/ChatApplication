package com.kmaloles.mymessagingapp.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kmaloles.mymessagingapp.R;

public class AdminFragment extends Fragment {
    // the fragment initialization parameters
    private static final String MODE = "mode";

    // TODO: Rename and change types of parameters
    private String mMode;

    public AdminFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mode Determines whether the view is used as either:
     *               -public messaging
     *               -direct messaging to admin
     * @return A new instance of fragment AdminFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminFragment newInstance(String mode) {
        AdminFragment fragment = new AdminFragment();
        Bundle args = new Bundle();
        args.putString(MODE, mode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMode = getArguments().getString(MODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messenger, container, false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
