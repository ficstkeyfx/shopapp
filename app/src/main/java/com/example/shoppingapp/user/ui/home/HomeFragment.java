package com.example.shoppingapp.user.ui.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.R;
import com.example.shoppingapp.user.adapters.CategoryAdapters;
import com.example.shoppingapp.user.adapters.PopularAdapters;

import com.example.shoppingapp.user.adapters.RecommendAdapters;
import com.example.shoppingapp.user.adapters.ViewAllAdapters;
import com.example.shoppingapp.user.models.HomeCategoryModel;
import com.example.shoppingapp.user.models.PopularModel;
import com.example.shoppingapp.user.models.RecommendModel;
import com.example.shoppingapp.user.models.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    ScrollView scrollView;
    ProgressBar progressBar;
    RecyclerView popularRec,homeCatRec,recRec;
    // popular items
    List<ViewAllModel> popularModelList;
    PopularAdapters popularAdapters;

    /// Search View
    EditText searchBox;
    private List<ViewAllModel> viewAllModelList;
    private RecyclerView recyclerViewSearch;
    private ViewAllAdapters viewAllAdapters;


    // category
    List<HomeCategoryModel> homeCategoryModelList;
    CategoryAdapters categoryAdapters;

    // recommend
    static List<ViewAllModel> recommendModelList;
    static RecommendAdapters recommendAdapters;

    static String type;

    public static void updateView(ViewAllModel viewAllModel){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        type = viewAllModel.getType();
        recommendModelList.clear();
        if(type != null && type.equalsIgnoreCase("adidas")){
            firebaseFirestore.collection("AllProducts").whereEqualTo("type","adidas").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    recommendModelList.clear();
                    for(DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                        recommendModelList.add(viewAllModel);
                        recommendAdapters.notifyDataSetChanged();
                    }
                }
            });
        }
        if(type != null && type.equalsIgnoreCase("nike")){
            firebaseFirestore.collection("AllProducts").whereEqualTo("type","nike").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    recommendModelList.clear();
                    for(DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                        recommendModelList.add(viewAllModel);
                        recommendAdapters.notifyDataSetChanged();
                    }
                }
            });
        }
        if(type != null && type.equalsIgnoreCase("converse")){
            firebaseFirestore.collection("AllProducts").whereEqualTo("type","converse").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    recommendModelList.clear();
                    for(DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                        recommendModelList.add(viewAllModel);
                        recommendAdapters.notifyDataSetChanged();
                    }
                }

            });
        }
        if(type != null && type.equalsIgnoreCase("new balance")){
            recommendModelList.clear();
            firebaseFirestore.collection("AllProducts").whereEqualTo("type","new balance").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for(DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                        recommendModelList.add(viewAllModel);
                        recommendAdapters.notifyDataSetChanged();
                    }
                }
            });
        }
        if(type != null && type.equalsIgnoreCase("gucci")){
            recommendModelList.clear();
            firebaseFirestore.collection("AllProducts").whereEqualTo("type","gucci").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for(DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                        recommendModelList.add(viewAllModel);
                        recommendAdapters.notifyDataSetChanged();
                    }
                    int n = task.getResult().getDocuments().size();
                    System.out.println(n);
                }

            });
        }
    }

    FirebaseFirestore db;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home,container,false);

        db = FirebaseFirestore.getInstance();

        // progressbar
        scrollView = root.findViewById(R.id.scroll_view);
        progressBar = root.findViewById(R.id.progressbar);

        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);
        //

        popularRec = root.findViewById(R.id.pop_rec);
        homeCatRec = root.findViewById(R.id.cat_rec);
        recRec = root.findViewById(R.id.recommend_rec);

        // popular
        popularRec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        popularModelList = new ArrayList<>();
        popularAdapters = new PopularAdapters(getActivity(),popularModelList);
        popularRec.setAdapter(popularAdapters);

        // category
        homeCatRec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        homeCategoryModelList = new ArrayList<>();
        categoryAdapters = new CategoryAdapters(getActivity(),homeCategoryModelList);
        homeCatRec.setAdapter(categoryAdapters);

        // recommend
        recRec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        recommendModelList = new ArrayList<>();
        recommendAdapters = new RecommendAdapters(getActivity(),recommendModelList);
        recRec.setAdapter(recommendAdapters);

        // popular
//         Cloud Firestore
        db.collection("AllProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document: task.getResult()){
                                if(Float.parseFloat(document.get("rating").toString()) >= 4.8){
                                    ViewAllModel recommendModel = document.toObject(ViewAllModel.class);
                                    popularModelList.add(recommendModel);
                                    popularAdapters.notifyDataSetChanged();
                                    progressBar.setVisibility(View.GONE);
                                    scrollView.setVisibility(View.VISIBLE);
                                }
                            }
                        }else {
                            Toast.makeText(getActivity(), "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        //category
//         Cloud Firestore
        db.collection("HomeCategory")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document: task.getResult()){
                                HomeCategoryModel homeCategoryModel = document.toObject(HomeCategoryModel.class);
                                homeCategoryModelList.add(homeCategoryModel);
                                categoryAdapters.notifyDataSetChanged();
                            }
                        }else {
                            Toast.makeText(getActivity(), "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // recommend
        if(type==null)
            db.collection("AllProducts")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot document: task.getResult()){

                                    if(document.get("status")!=null&&((String)document.get("status")).equals("new")){
                                        ViewAllModel recommendModel = document.toObject(ViewAllModel.class);
                                        recommendModelList.add(recommendModel);
                                        recommendAdapters.notifyDataSetChanged();
                                        progressBar.setVisibility(View.GONE);
                                        scrollView.setVisibility(View.VISIBLE);
                                    }
                                }
                            }else {
                                Toast.makeText(getActivity(), "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


        /// Search View
        searchBox = root.findViewById(R.id.search_box);
        recyclerViewSearch = root.findViewById(R.id.search_rec);

        viewAllModelList = new ArrayList<>();
        viewAllAdapters = new ViewAllAdapters(getContext(),viewAllModelList);
        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSearch.setAdapter(viewAllAdapters);
        recyclerViewSearch.setHasFixedSize(true);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty()){
                    viewAllModelList.clear();
                    viewAllAdapters.notifyDataSetChanged();
                }else{
                    searchProducts(s.toString());
                }
            }
        });
        return root;
    }

    private void searchProducts(String type) {
        if(!type.isEmpty()){
            db.collection("AllProducts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    viewAllModelList.clear();
                    viewAllAdapters.notifyDataSetChanged();
                    for (DocumentSnapshot doc: task.getResult())
                    {
                        String name = doc.getString("name");
                        String[] q = type.split(" ");
                        boolean check = true;

                        for(String value:q){
                            if(!name.toLowerCase().contains(value.toLowerCase())) check = false;
                        }
                        if (check)
                        {
                            System.out.println(type);
                            ViewAllModel viewAllModel = doc.toObject(ViewAllModel.class);
                            viewAllModelList.add(viewAllModel);
                            viewAllAdapters.notifyDataSetChanged();
                        }
                    }
                }
            });
//            db.collection("AllProducts").whereEqualTo("type",type.toLowerCase()).get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if(task.isSuccessful()&&task.getResult()!=null){
//                                viewAllModelList.clear();
//                                viewAllAdapters.notifyDataSetChanged();
//                                for(DocumentSnapshot documentSnapshot: task.getResult().getDocuments()){
//                                    ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
//                                    viewAllModelList.add(viewAllModel);
//                                    viewAllAdapters.notifyDataSetChanged();
//                                }
//                            }
//                        }
//                    });
        }
    }

}