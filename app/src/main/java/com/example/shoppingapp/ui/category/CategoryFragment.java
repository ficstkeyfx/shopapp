package com.example.shoppingapp.ui.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.R;
import com.example.shoppingapp.adapters.NavCategoryAdapters;
import com.example.shoppingapp.models.NavCategoryModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {

    RecyclerView rcNavCategory ;
    NavCategoryAdapters navcategoryAdapter;
    List<NavCategoryModel> navCategoryList ;
    FirebaseFirestore db ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_category,container,false);
        rcNavCategory = root.findViewById(R.id.rc_cat_nav);
        navCategoryList = new ArrayList<>() ;
        navcategoryAdapter = new NavCategoryAdapters(getContext(),navCategoryList);
        rcNavCategory.setLayoutManager(new LinearLayoutManager(getActivity() ,LinearLayoutManager.VERTICAL,false));
        rcNavCategory.setAdapter(navcategoryAdapter);
        db =FirebaseFirestore.getInstance();
        db.collection("NavCategory").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                navCategoryList.clear();
                if (task.isComplete()){
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        NavCategoryModel navCategory =documentSnapshot.toObject(NavCategoryModel.class);
                        navCategoryList.add(navCategory);
                    }
                    navcategoryAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(getActivity(), "Lỗi", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

}