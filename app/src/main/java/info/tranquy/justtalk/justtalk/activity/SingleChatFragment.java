package info.tranquy.justtalk.justtalk.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import info.tranquy.justtalk.R;
import info.tranquy.justtalk.justtalk.adapter.ChatMessageAdapter;
import info.tranquy.justtalk.justtalk.adapter.MessageListAdapter;
import info.tranquy.justtalk.justtalk.chat.XmppChatManager;
import info.tranquy.justtalk.justtalk.decoration.DividerItemDecoration;
import info.tranquy.justtalk.justtalk.model.ChatMessage;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SingleChatFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SingleChatFragment extends Fragment {

    private Activity mActivity;

    private RecyclerView recyclerView;
    private ChatMessageAdapter adapter;
    private XmppChatManager chatManager;
    List<ChatMessage> messages;
    private Button button;
    private EditText editText;
    private String User;
    private Chat newChat;

    public SingleChatFragment() {
        chatManager = XmppChatManager.getInstance();
        messages = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_single_chat, container, false);
        if(chatManager.isOnlineMode()) {
            User = getActivity().getIntent().getStringExtra("User");
            if(chatManager.getConnection().isConnected()) {
                newChat = ChatManager.getInstanceFor(chatManager.getConnection()).createChat(User);
            }
        }

        recyclerView = (RecyclerView) rootView.findViewById(R.id.message_list);
        button = (Button) rootView.findViewById(R.id.Button);
        editText = (EditText) rootView.findViewById(R.id.EditText);
        //messages = getData();
        //Log.e("e", String.valueOf(getData().size()));
        adapter = new ChatMessageAdapter(getActivity(), messages);
        recyclerView.setAdapter(adapter);
        //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.scrollToPosition(messages.size() - 1);
        Log.e("onlineMode", String.valueOf(chatManager.isOnlineMode()));
        if(chatManager.isOnlineMode()) {
            //chatManager.initChatStateManager();
            ChatManager.getInstanceFor(chatManager.getConnection()).addChatListener(new ChatManagerListener() {
                @Override
                public void chatCreated(Chat chat, boolean createdLocally) {

                    chat.addMessageListener(new ChatMessageListener() {
                        @Override
                        public void processMessage(Chat chat, Message message) {
                            if (message.getType() == Message.Type.chat || message.getType() == Message.Type.normal) {
                                if (message.getBody() != null) {
                                    ChatMessage newChatMessage = bindMessage(message.getBody().toString(), false);
                                    //updateList(newChatMessage);
                                    //if(getActivity()!=null){
                                    getActivity().runOnUiThread(new Runnable() {
                                        ChatMessage ChatMessage;

                                        @Override
                                        public void run() {

                                            updateList(ChatMessage);
                                        }

                                        public Runnable init(ChatMessage ChatMessage) {
                                            this.ChatMessage = ChatMessage;
                                            return (this);
                                        }
                                    }.init(newChatMessage));
                                    //}
                                }
                            }
                        }
                    });
                }
            });
        }

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new FragmentDrawer.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editText.getText().toString();
                if(message == null || message.length() == 0){
                    Toast toast = Toast.makeText(getActivity(), String.valueOf(getActivity().getString(R.string.noMessageAlert)), Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    if(chatManager.isOnlineMode()) {
                        ChatMessage ChatMessage = bindMessage(message, true);
                        updateList(ChatMessage);
                        try {
                            newChat.sendMessage(message);
                            //chat.getParticipant();
                        } catch (SmackException.NotConnectedException e) {
                            e.printStackTrace();
                        }

                    }else{
                        ChatMessage ChatMessage = bindMessage(message, true);
                        updateList(ChatMessage);
                        //add offline mode queue
                    }
                }
                editText.getText().clear();

            }
        });

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
            //if (context instanceof Activity){
            mActivity=(Activity) context;
        //}
    }

    public static List<ChatMessage> getData() {
        List<ChatMessage> data = new ArrayList<>();


        // preparing navigation drawer items
        for (int i = 1; i < 10; i++) {
            ChatMessage ChatMessage = new ChatMessage();
            ChatMessage.setProfileID(R.drawable.face);

            ChatMessage.setTime("07:29");
            if(i%2 !=0){
                ChatMessage.setMine(true);
                ChatMessage.setMessage("Hello there!");
            }else{
                ChatMessage.setMine(false);
                ChatMessage.setMessage("Từ Đại hội thành lập Đảng năm 1930 đến nay Đảng Cộng sản Việt Nam đã trải qua 12 kỳ đại hội với các dấu mốc quan trọng.");
            }
            data.add(ChatMessage);
        }
        return data;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    private ChatMessage bindMessage(String message, boolean mine){
        ChatMessage ChatMessage = new ChatMessage();
        ChatMessage.setProfileID(R.drawable.face);

        ChatMessage.setTime(getCurrentTime());
        ChatMessage.setMine(mine);
        ChatMessage.setMessage(message);

        return ChatMessage;
    }

    private String getCurrentTime(){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime()); //2014/08/06 16:00:22
    }

    private void updateList(ChatMessage ChatMessage){
        messages.add(ChatMessage);
        adapter.notifyItemInserted(messages.size() - 1);
        recyclerView.scrollToPosition(messages.size() - 1);

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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

}
