package info.tranquy.justtalk.justtalk.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import info.tranquy.justtalk.R;
import info.tranquy.justtalk.justtalk.activity.MainActivity;
import info.tranquy.justtalk.justtalk.model.ChatMessage;

/**
 * Created by quyquy on 1/27/2016.
 */
public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.MyViewHolder> {
    List<ChatMessage> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    public ChatMessageAdapter(Context context, List<ChatMessage> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.chat_message_single, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    public int getDPI(int size, DisplayMetrics metrics){
        return (size * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ChatMessage current = data.get(position);
        holder.profile.setImageResource(current.getProfileID());
        holder.message.setText(current.getMessage());
        holder.time.setText(current.getTime());


        if(current.getMine()){
            LayoutParams messageParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);

            messageParams.addRule(RelativeLayout.LEFT_OF, R.id.profile_container);
            messageParams.addRule(RelativeLayout.ALIGN_TOP, R.id.profile_container);
            messageParams.setMargins(20, 3, 30, 20);
            holder.message.setLayoutParams(messageParams);

            LayoutParams profileParams = new LayoutParams(144,
                    144);

            profileParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            profileParams.setMargins(0, 24, 30, 24);
            profileParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            //profileParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.profile_container.setLayoutParams(profileParams);

            //holder.message_container.setGravity(Gravity.RIGHT);
            holder.message.setBackgroundResource(R.color.singleChatTextBackground);
        }else{

            LayoutParams messageParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);

            messageParams.addRule(RelativeLayout.RIGHT_OF, R.id.profile_container);
            messageParams.addRule(RelativeLayout.ALIGN_TOP, R.id.profile_container);
            messageParams.setMargins(30, 3, 20, 20);
            holder.message.setLayoutParams(messageParams);

            LayoutParams profileParams = new LayoutParams(144,
                    144);

            profileParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            profileParams.setMargins(30, 24, 0, 24);
            profileParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            //profileParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.profile_container.setLayoutParams(profileParams);

            //holder.message_container.setGravity(Gravity.LEFT);
            holder.message.setBackgroundResource(R.color.singleChatTextBackgroundOther);
           // holder.message.setBackgroundResource(R.drawable.navy);
        }

        //holder.nav_icon_item.setImageResource(current.getPhotoID());
    }



    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profile;
        TextView message;
        TextView time;
        RelativeLayout message_container;
        RelativeLayout profile_container;

        public MyViewHolder(View itemView) {
            super(itemView);

            profile = (CircleImageView) itemView.findViewById(R.id.profile);
            message = (TextView) itemView.findViewById(R.id.message);
            time = (TextView) itemView.findViewById(R.id.time);
            message_container = (RelativeLayout) itemView.findViewById(R.id.message_container);
            profile_container = (RelativeLayout) itemView.findViewById(R.id.profile_container);
        }
    }

}

