package database;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import tvz.projekt.rma.R;

public class AdsList extends ArrayAdapter<Ad> {

    private Activity context;
    private List<Ad> adsList;

    public AdsList(Activity context, List<Ad> adsList){
        super(context, R.layout.advertisement, adsList);
        this.context = context;
        this.adsList = adsList;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        @SuppressLint({"ViewHolder", "InflateParams"})
        View listViewItem = inflater.inflate(R.layout.advertisement, null, true);

        TextView textViewName = listViewItem.findViewById(R.id.helperName);
        TextView textViewSubject = listViewItem.findViewById(R.id.helperSubject);
        TextView textViewLocation = listViewItem.findViewById(R.id.helperLocation);
        TextView textViewPrice = listViewItem.findViewById(R.id.helperPrice);
        TextView textViewEmail = listViewItem.findViewById(R.id.helperEmail);
        TextView textViewPhone = listViewItem.findViewById(R.id.helperPhone);
        TextView textViewArrival = listViewItem.findViewById(R.id.helperArrival);
        TextView textViewDate = listViewItem.findViewById(R.id.date);
        RatingBar ratingBar = listViewItem.findViewById(R.id.ratingBar2);

        Ad ad = adsList.get(position);

        StringBuilder sb = new StringBuilder();
        sb.append(ad.getPrice().toString());
        sb.append(" kn/h");

        textViewName.setText(ad.getName());
        textViewSubject.setText(ad.getSubject());
        textViewLocation.setText(ad.getLocation());
        textViewPrice.setText(sb);
        textViewEmail.setText(ad.getEmail());
        textViewPhone.setText(ad.getPhone());
        textViewArrival.setText(ad.getArrival());
        textViewDate.setText(ad.getDate());
        ratingBar.setRating(ad.getRating());

        return listViewItem;
    }
}
