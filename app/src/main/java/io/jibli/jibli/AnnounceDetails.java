package io.jibli.jibli;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import static io.jibli.jibli.LoginActivity.announceCollection;
import static io.jibli.jibli.LoginActivity.journeyCollection;
import static io.jibli.jibli.LoginActivity.usercollection;

public class AnnounceDetails extends AppCompatActivity {
    String email,announce;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announce_details);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            email = extras.getString("email");
            announce = extras.getString("announce");
        }




        User user = usercollection.searchUser(email);
        Announce announce1 = announceCollection.announces.get(Integer.parseInt(announce));

        TextView product = (TextView) (findViewById(R.id.product));
        TextView price = (TextView) (findViewById(R.id.price));
        TextView profit = (TextView) (findViewById(R.id.profit));
        TextView location = (TextView) (findViewById(R.id.location));
        TextView comment = (TextView) (findViewById(R.id.comment));
        TextView name = (TextView) (findViewById(R.id.name));
        TextView phonenumber = (TextView) (findViewById(R.id.phonenumber));
        TextView email = (TextView) (findViewById(R.id.email));


        product.setText(announce1.getProduct());
        price.setText(Double.toString(announce1.getPrice()) + "Dhs");
        profit.setText(Double.toString(announce1.getProfit()) + "Dhs");
        location.setText(announce1.getLocation());
        comment.setText(announce1.getComment());
        name.setText(user.getFirstname() + " " +user.getLastname());
        phonenumber.setText(user.getPhoneNumber());
        email.setText(user.getEmail());


    }
}
