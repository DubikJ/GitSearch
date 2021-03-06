package com.gmail.vanyadubik.gitsearch.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gmail.vanyadubik.gitsearch.R;
import com.gmail.vanyadubik.gitsearch.activity.ReposActivity;
import com.gmail.vanyadubik.gitsearch.model.db.Owner;

import java.util.List;

import static com.gmail.vanyadubik.gitsearch.activity.ReposActivity.REPOS_ACTIVITY_PARAM_OWNER_ID;

public class OrgRecyclerAdapter extends RecyclerView.Adapter {

    private List<Owner> list;
    private LayoutInflater layoutInflater;
    private Context context;


    public static class OrgViewHolder extends RecyclerView.ViewHolder {

        TextView login, location, blog;
        ImageView mAvatar;

        public OrgViewHolder(View itemView) {
            super(itemView);
            this.login = (TextView) itemView.findViewById(R.id.login);
            this.location = (TextView) itemView.findViewById(R.id.location);
            this.blog = (TextView) itemView.findViewById(R.id.blog);
            this.mAvatar = (ImageView) itemView.findViewById(R.id.avatar);
        }
    }

    public OrgRecyclerAdapter(Context context, List<Owner> list) {
        this.list = list;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new OrgViewHolder(layoutInflater.inflate(R.layout.list_org_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        final Owner owner = getOwner(listPosition);
        if (owner != null) {
            ((OrgViewHolder) holder).login.setText(owner.getLogin());

            ((OrgViewHolder) holder).location.setText(owner.getLocation());

            String blog = owner.getBlog();
            ((OrgViewHolder) holder).blog.setText(!TextUtils.isEmpty(blog) ?
                    blog.substring(0, owner.getBlog().length() - 1)
                            .replace("http://","")
                            .replace("https://",""): "");

            Glide.with(context).
                    load(owner.getAvatar() == null ?
                            android.R.drawable.ic_menu_gallery : owner.getAvatar())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(((OrgViewHolder)holder).mAvatar);

            ((OrgViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ReposActivity.class);
                    intent.putExtra(REPOS_ACTIVITY_PARAM_OWNER_ID, owner.getId());
                    Activity activity = (Activity) context;
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private Owner getOwner(int position) {
        return (Owner) list.get(position);
    }
}
