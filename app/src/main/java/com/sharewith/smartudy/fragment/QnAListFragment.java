package com.sharewith.smartudy.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharewith.smartudy.adapter.QnAListAdapter;
import com.sharewith.smartudy.dto.NotePadDto;
import com.sharewith.smartudy.smartudy.R;
import com.sharewith.smartudy.dao.Write_DBhelper;

import java.util.List;

public class QnAListFragment extends Fragment { //뷰페이저의 1페이지에 해당하는 리싸이클러뷰
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView mRecyclerView;
    private String mParam1;
    private String mParam2;
    Write_DBhelper DBhelper;

    public QnAListFragment() {
    }

    public static QnAListFragment newInstance(String param1, String param2) {
        QnAListFragment fragment = new QnAListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void setMember(View view){
        mRecyclerView = view.findViewById(R.id.qna_recycler_view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qna_list, container, false);
        setMember(view);
        DBhelper = new Write_DBhelper(view.getContext());
        List<NotePadDto> notepads = DBhelper.selectAllNotePad();
        QnAListAdapter listadapter = new QnAListAdapter(view.getContext(),notepads);
        mRecyclerView.setAdapter(listadapter);
        return view;
    }

    public void addNotePad(NotePadDto notepad){
        ((QnAListAdapter)mRecyclerView.getAdapter()).addNotePad(notepad);
    }

    public void scroll(int position){
        LinearLayoutManager llm = (LinearLayoutManager)mRecyclerView.getLayoutManager();
        if(position != 0)
            llm.scrollToPositionWithOffset(position,-65); //패딩20들어간거빼줌.
        else
            llm.scrollToPositionWithOffset(position,0);
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
