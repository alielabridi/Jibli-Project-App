package io.jibli.jibli;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import static io.jibli.jibli.LoginActivity.journeyCollection;
import static io.jibli.jibli.LoginActivity.announceCollection;
import static io.jibli.jibli.LoginActivity.usercollection;

public class TripDetails extends AppCompatActivity {
    String email,trip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            email = extras.getString("email");
            trip = extras.getString("trip");
        }
        System.out.println("the id is " + trip);
        System.out.println("the id is " + email);
        Journey journey = journeyCollection.journeys.get(Integer.parseInt(trip));
        User user = usercollection.searchUser(email);
        TextView departure = (TextView) (findViewById(R.id.tripdeparture));
        TextView destination = (TextView) (findViewById(R.id.tripdestination));
        TextView date = (TextView) (findViewById(R.id.tripdate));
        TextView name = (TextView) (findViewById(R.id.tripname));
        TextView phonenumber = (TextView) (findViewById(R.id.tripphonenumber));
        TextView email = (TextView) (findViewById(R.id.tripemail));



        departure.setText(journey.getDepar());
        destination.setText(journey.getDest());
        date.setText(journey.getDate());
        name.setText(user.getFirstname() + " " +user.getLastname());
        phonenumber.setText(user.getPhoneNumber());
        email.setText(user.getEmail());
    }
}
