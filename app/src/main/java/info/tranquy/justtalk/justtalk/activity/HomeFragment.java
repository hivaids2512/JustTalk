package info.tranquy.justtalk.justtalk.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

import info.tranquy.justtalk.R;
import info.tranquy.justtalk.justtalk.adapter.MessageListAdapter;
import info.tranquy.justtalk.justtalk.adapter.NavigationDrawerAdapter;
import info.tranquy.justtalk.justtalk.chat.NetworkManager;
import info.tranquy.justtalk.justtalk.chat.XmppChatManager;
import info.tranquy.justtalk.justtalk.decoration.DividerItemDecoration;
import info.tranquy.justtalk.justtalk.model.MessListItem;
import info.tranquy.justtalk.justtalk.model.NavDrawerItem;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private MessageListAdapter adapter;
    private XmppChatManager chatManager;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void createChat() {
        chatManager = XmppChatManager.getInstance();
        chatManager.setUsernameAndPassword("quy1", "quy1");
        //chatManager.newConnection();
        //chatManager.openConnection();
        chatManager.login();

        Chat chat = chatManager.chatWith("quy2@quyvupc");
        chatManager.sendMessage(chat, "quyquy");
        chatManager.chatWith("quy3@quyvupc");
        Chat chat2 = chatManager.getChatInstance("quy3@quyvupc");
        chatManager.sendMessage(chat2, "quyquy");

        Collection<RosterEntry> entries = chatManager.getRosterEntries();
        Log.e("e", String.valueOf(entries.size()));
        mapData(entries);

    }

    public List<MessListItem> mapData(Collection<RosterEntry> entries) {
        List<MessListItem> messList = new ArrayList<>();


        // preparing navigation drawer items
        for (RosterEntry entry : entries) {
            MessListItem messItem = new MessListItem();
            messItem.setProfileID(R.drawable.face);
            messItem.setName(entry.getName().toString());

            messItem.setStatus("I am ok now");
            messItem.setStateID(R.drawable.online);
            messList.add(messItem);
        }
        return messList;
    }

    public List<MessListItem>exData() {
        List<MessListItem> messList = new ArrayList<>();


        // preparing navigation drawer items
        for (int i =0; i< 10; i++) {
            MessListItem messItem = new MessListItem();
            messItem.setProfileID(R.drawable.face);
            messItem.setName("Quy Vu");

            messItem.setStatus("I am ok now");
            messItem.setStateID(R.drawable.online);
            messList.add(messItem);
        }
        return messList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        List<MessListItem> messList = new ArrayList<MessListItem>();

        recyclerView = (RecyclerView) rootView.findViewById(R.id.inbox_list);

        new Connection().execute();


        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new FragmentDrawer.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Context context = getActivity();

                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, String.valueOf(position), duration);
                toast.show();
                Intent intent = new Intent(getActivity(), SingleChatActivity.class);

                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private FragmentDrawer.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final FragmentDrawer.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }


    }

    public class Connection extends AsyncTask<Void, Void, List<MessListItem>> {

        @Override
        protected List<MessListItem> doInBackground(Void... params) {
            List<MessListItem> messListItems = new ArrayList<MessListItem>();
            chatManager = XmppChatManager.getInstance();
            chatManager.setUsernameAndPassword("quy1", "quy1");
            //chatManager.newConnection();
            //chatManager.openConnection();
            chatManager.login();

            Chat chat = chatManager.chatWith("quy2@quyvupc");
            chatManager.sendMessage(chat, "quy :v");

            Collection<RosterEntry> entries = chatManager.getRosterEntries();

                Log.e("e", String.valueOf(entries.size()));


            messListItems = mapData(entries);
            return messListItems;
        }

        @Override
        protected void onPostExecute(List<MessListItem> messListItems) {
            adapter = new MessageListAdapter(getActivity(),messListItems);
            recyclerView.setAdapter(adapter);
        }
    }



}

