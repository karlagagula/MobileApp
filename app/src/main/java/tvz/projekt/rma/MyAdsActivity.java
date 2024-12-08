package tvz.projekt.rma;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import database.Ad;

public class MyAdsActivity extends AppCompatActivity {

    private static final String TAG = "MyAdsActivity";
    private List<Ad> myAdsList;
    private ListView listViewMyAds;
    private MyAdsList adapter = null;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_ads);
        listViewMyAds = findViewById(R.id.listViewMyAds);
        myAdsList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        TextView currentUser = findViewById(R.id.currentUser);
        currentUser.setText(mAuth.getCurrentUser().getDisplayName());


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("ads").whereEqualTo("email", email).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    myAdsList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        String subject = Objects.requireNonNull(doc.get("subject")).toString();
                        int id = Integer.parseInt(Objects.requireNonNull(doc.get("id")).toString());
                        String location = Objects.requireNonNull(doc.get("location")).toString();
                        String phone = Objects.requireNonNull(doc.get("phone")).toString();
                        String arrival = Objects.requireNonNull(doc.get("arrival")).toString();
                        int price = Integer.parseInt(Objects.requireNonNull(doc.get("price").toString()));
                        String date = Objects.requireNonNull(doc.get("date")).toString();

                        Ad ad = new Ad(subject, id, location, phone, arrival, price, date);
                        myAdsList.add(ad);
                        adapter = new MyAdsList(MyAdsActivity.this, myAdsList);
                        listViewMyAds.setAdapter(adapter);
                    }
                });

    }

    public class MyAdsList extends ArrayAdapter<Ad> {

        private static final String TAG = "MyAdsList";
        private Activity context;
        private List<Ad> myAdsList;

        public MyAdsList(Activity context, List<Ad> myAdsList){
            super(context, R.layout.my_ad, myAdsList);
            this.context = context;
            this.myAdsList = myAdsList;
        }

        @SuppressLint("SetTextI18n")
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            FirebaseFirestore db = FirebaseFirestore.getInstance();


            @SuppressLint({"ViewHolder", "InflateParams"})
            View listViewItem = inflater.inflate(R.layout.my_ad, null, true);

            TextView textViewSubject = listViewItem.findViewById(R.id.subjectShow);
            TextView textViewLocation = listViewItem.findViewById(R.id.locationShow);
            TextView textViewPrice = listViewItem.findViewById(R.id.priceShow);
            TextView textViewPhone = listViewItem.findViewById(R.id.numberShow);
            TextView textViewArrival = listViewItem.findViewById(R.id.arriveShow);
            TextView textViewDate = listViewItem.findViewById(R.id.dateShow);
            TextView textViewId = listViewItem.findViewById(R.id.idShow);
            Button delete = listViewItem.findViewById(R.id.deleteAd);

            Ad ad = myAdsList.get(position);

            textViewSubject.setText(ad.getSubject());
            textViewLocation.setText(ad.getLocation());
            textViewPrice.setText(ad.getPrice().toString());
            textViewPhone.setText(ad.getPhone());
            textViewArrival.setText(ad.getArrival());
            textViewDate.setText(ad.getDate());
            textViewId.setText(ad.getId().toString());

            delete.setOnClickListener(v -> {
                CollectionReference itemsRef = db.collection("ads");
                Query query = itemsRef.whereEqualTo("id", ad.getId());
                query.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            itemsRef.document(document.getId()).delete();
                        }
                        myAdsList.remove(position);
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
            });

            return listViewItem;
        }
    }


}
