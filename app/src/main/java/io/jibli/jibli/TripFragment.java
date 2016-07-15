package io.jibli.jibli;



        import android.content.Intent;
        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.util.ArrayList;

        import static io.jibli.jibli.LoginActivity.announceCollection;
        import static io.jibli.jibli.LoginActivity.journeyCollection;
        import static io.jibli.jibli.LoginActivity.usercollection;


public class TripFragment extends Fragment {
    ListView listView;
    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.tripfragment, container, false);
        listView = (ListView) v.findViewById(R.id.list);

        // Defined Array values to show in ListView
/*
        String[] values = new String[] { "Android List View",
                "Adapter implementation",
                "Simple List View In Android",
                "Create List View Android",
                "Android Example",
                "List View Source Code",
                "List View Array Adapter",
                "Android Example List View"
        };
*/

        journeyCollection.UpdateJourneys();
        ArrayList<String> values = new ArrayList<>();

        for (Journey journey: journeyCollection.journeys ){
            String value = "From: " + journey.getDepar() + " to: "+ journey.getDest()+ " ("+journey.getDate()+")";
            System.out.println(value);
            values.add(value);
        }

        String[] valueStrings = new String[values.size()];
        valueStrings = values.toArray(valueStrings);
        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                R.layout.mytextview, valueStrings);


        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) listView.getItemAtPosition(position);

                System.out.println("position " + Integer.toString(position));
                String id2  =  Integer.toString(position);
                System.out.println("the id test " + id2);
                Intent i=new Intent(getActivity(),TripDetails.class);
                i.putExtra("trip", id2 );
                i.putExtra("email", usercollection.searchUser(journeyCollection.journeys.get(position).getBringerId()).getEmail());


                startActivity(i);

            }

        });

        return v;

    }

}