package info.tranquy.justtalk.justtalk.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import info.tranquy.justtalk.R;
import info.tranquy.justtalk.justtalk.adapter.MessageListAdapter;
import info.tranquy.justtalk.justtalk.chat.XmppChatManager;
import info.tranquy.justtalk.justtalk.decoration.DividerItemDecoration;
import info.tranquy.justtalk.justtalk.model.ChatMessage;
import info.tranquy.justtalk.justtalk.model.MessListItem;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private MessageListAdapter adapter;
    private XmppChatManager chatManager;
    List<MessListItem> messList;
    private ProgressDialog progress;
    private Roster roster;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatManager = XmppChatManager.getInstance();
        roster = Roster.getInstanceFor(chatManager.getConnection());
    }

    /*
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
    */

    public List<MessListItem> mapData(Collection<RosterEntry> entries) {
        List<MessListItem> messList = new ArrayList<>();
        for (RosterEntry entry : entries) {
            Roster roster = Roster.getInstanceFor(chatManager.getConnection());
            MessListItem messItem = new MessListItem();
            messItem.setProfileID(R.drawable.face);
            messItem.setName(entry.getName().toString());
            messItem.setUser(entry.getUser().toString());
            Presence presence = roster.getPresence(entry.getUser().toString());

            messItem.setStatus(presence.getStatus());
            if(presence.getType().equals(Presence.Type.available)){
                messItem.setStateID(R.drawable.availablemode);
            }else if(presence.getType().equals(Presence.Type.unavailable)){
                //messItem.setStateID(R.drawable.donotdistubmode);
            }
            messList.add(messItem);
        }
        return messList;
    }

    public int mapDataChanged(List<MessListItem> messList, Presence presence) {
        int index = -1;
        for (MessListItem item : messList) {
            Log.e("userr", item.getUser());
            String user = presence.getFrom().split("/")[0];
            Log.e("userr2", user);
            if (item.getUser().equals(user)){
                index = messList.indexOf(item);
                MessListItem newItem = messList.get(index);

                if(presence.getType().equals(Presence.Type.available)){
                    newItem.setStateID(R.drawable.availablemode);
                    newItem.setStatus(presence.getStatus());
                }else if(presence.getType().equals(Presence.Type.unavailable)){
                    newItem.setStateID(R.drawable.donotdistubmode);
                    newItem.setStatus(presence.getStatus());
                }
                messList.set(index, newItem);
            }
        }
        return index;
    }
    /*
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
    */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        messList = new ArrayList<MessListItem>();

        recyclerView = (RecyclerView) rootView.findViewById(R.id.inbox_list);




        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new FragmentDrawer.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                MessListItem MessListItem = messList.get(position);
                //Log.e("user", MessListItem.getUser().toString());
                Intent intent = new Intent(getActivity(), SingleChatActivity.class);
                intent.putExtra("User", MessListItem.getUser().toString());
                intent.putExtra("Name", MessListItem.getName().toString());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));




        new Connection().execute();

        // Inflate the layout for this fragment
        return rootView;
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
        protected void onPreExecute() {
            progress = ProgressDialog.show(getActivity(), null,
                    "Getting your friends...", true);
        }

        @Override
        protected List<MessListItem> doInBackground(Void... params) {
            List<MessListItem> messListItems = new ArrayList<MessListItem>();

            if(chatManager.isConnected()){
                Roster roster = Roster.getInstanceFor(chatManager.getConnection());
                if(!roster.isLoaded()){
                    try {
                        roster.reloadAndWait();

                    } catch (SmackException.NotLoggedInException e) {
                        e.printStackTrace();
                    } catch (SmackException.NotConnectedException e) {
                        e.printStackTrace();
                    }
                }
                Collection<RosterEntry> entries = roster.getEntries();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.e("e", String.valueOf(entries.size()));
                messListItems = mapData(entries);
            }else{
                Log.e("error", "NetWork error");
            }
            /*
            Collection<RosterEntry> entries = chatManager.getRosterEntries();
                Log.e("e", String.valueOf(entries.size()));
            messListItems = mapData(entries);
            */
            return messListItems;
        }

        @Override
        protected void onPostExecute(List<MessListItem> messListItems) {
            messList = messListItems;
            Log.e("e", String.valueOf(messListItems.size()));
            adapter = new MessageListAdapter(getActivity(), messListItems);

            recyclerView.setAdapter(adapter);
            adapter.notifyItemInserted(messListItems.size() - 1);
            //Context context = getActivity().getApplicationContext();
            roster.addRosterListener(new RosterListener() {

                @Override
                public void entriesAdded(Collection<String> addresses) {

                }

                @Override
                public void entriesUpdated(Collection<String> addresses) {

                }

                @Override
                public void entriesDeleted(Collection<String> addresses) {

                }

                @Override
                public void presenceChanged(Presence presence) {
                    Log.e("presence changed", presence.getFrom() + " " + presence.getStatus()+" "+ presence.getType());

                    int index = mapDataChanged(messList,presence);
                    Log.e("index", String.valueOf(index));
                    //adapter.notifyItemChanged(index);
                    getActivity().runOnUiThread(new Runnable() {
                        int index;

                        @Override
                        public void run() {

                            adapter.notifyItemChanged(index);
                        }

                        public Runnable init(int index) {
                            this.index = index;
                            return (this);
                        }
                    }.init(index));
                }
            });
            progress.dismiss();
        }
    }



}

