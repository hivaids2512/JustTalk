package info.tranquy.justtalk.justtalk.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import org.jivesoftware.smack.chat.Chat;

import java.util.ArrayList;
import java.util.List;

import info.tranquy.justtalk.R;
import info.tranquy.justtalk.justtalk.adapter.ChatMessageAdapter;
import info.tranquy.justtalk.justtalk.adapter.MessageListAdapter;
import info.tranquy.justtalk.justtalk.decoration.DividerItemDecoration;
import info.tranquy.justtalk.justtalk.model.ChatMessage;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SingleChatFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SingleChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private ChatMessageAdapter adapter;
    List<ChatMessage> messages;
    public SingleChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_single_chat, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.message_list);
        messages = getData();
        Log.e("e", String.valueOf(getData().size()));
        adapter = new ChatMessageAdapter(getActivity(), messages);
        recyclerView.setAdapter(adapter);
        //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.scrollToPosition(messages.size()-1);


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new FragmentDrawer.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ChatMessage ChatMessage = new ChatMessage();
                ChatMessage.setProfileID(R.drawable.face);

                ChatMessage.setTime("07:29");
                ChatMessage.setMine(false);
                ChatMessage.setMessage("Tổng thầu, theo góp ý của người dân và Bộ GTVT. ");
                messages.add(ChatMessage);
                adapter.notifyItemInserted(messages.size() - 1);
                recyclerView.scrollToPosition(messages.size()-1);
                Context context = getActivity();

                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, String.valueOf(ChatMessage.getMine()), duration);
                toast.show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();

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
