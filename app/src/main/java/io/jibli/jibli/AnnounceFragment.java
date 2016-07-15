package io.jibli.jibli;

/**
 * Created by Ali on 7/14/2016.
 */



        import android.content.Intent;
        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.util.ArrayList;

        import static io.jibli.jibli.LoginActivity.journeyCollection;
        import static io.jibli.jibli.LoginActivity.announceCollection;
        import static io.jibli.jibli.LoginActivity.usercollection;

public class AnnounceFragment extends Fragment {
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
        announceCollection.UpdateAnnounces();
        ArrayList<String> values = new ArrayList<>();

        for (Announce announce: announceCollection.announces ){
            String value = "Product: " + announce.getProduct() + " Location: "+ announce.getLocation()+ " ("+announce.getPrice()+" Dhs)";
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
                Intent i=new Intent(getActivity(),AnnounceDetails.class);
                i.putExtra("email", usercollection.searchUser(announceCollection.announces.get(position).getBuyerId()).getEmail());
                i.putExtra("announce", id2);


                startActivity(i);

            }

        });

        return v;
    }
}