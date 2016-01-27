package info.tranquy.justtalk.justtalk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import info.tranquy.justtalk.R;
import info.tranquy.justtalk.justtalk.model.MessListItem;
import info.tranquy.justtalk.justtalk.model.NavDrawerItem;

/**
 * Created by quyquy on 1/25/2016.
 */
public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MyViewHolder> {
    List<MessListItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    public MessageListAdapter(Context context, List<MessListItem> data) {
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
        View view = inflater.inflate(R.layout.message_list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MessListItem current = data.get(position);
        holder.profile.setImageResource(current.getProfileID());
        holder.name.setText(current.getName());
        holder.status.setText(current.getStatus());
        holder.state.setImageResource(current.getStateID());
        //holder.nav_icon_item.setImageResource(current.getPhotoID());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profile;
        TextView name;
        TextView status;
        ImageView state;

        public MyViewHolder(View itemView) {
            super(itemView);

            profile = (CircleImageView) itemView.findViewById(R.id.profile);
            name = (TextView) itemView.findViewById(R.id.name);
            status = (TextView) itemView.findViewById(R.id.status);
            state = (ImageView) itemView.findViewById(R.id.state);
        }
    }

}
