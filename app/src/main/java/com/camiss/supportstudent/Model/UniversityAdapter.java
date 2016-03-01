package com.camiss.supportstudent.Model;

import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.camiss.supportstudent.ActivityListRecord;
import com.camiss.supportstudent.ActivityRecordContent;
import com.camiss.supportstudent.R;
import com.camiss.supportstudent.Utils.ApiUtils;
import com.camiss.supportstudent.Utils.Constant;
import com.camiss.supportstudent.Utils.DataParseUtils;
import com.camiss.supportstudent.Utils.PrefUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UniversityAdapter extends RecyclerView.Adapter<UniversityAdapter.UniViewHolder> {

    private List<University> mVisibleList;
    private List<University> mAllObject;
    final String abc = "";
    public Context mContext;

    public UniversityAdapter(List<University> list, Context context) {
        this.mVisibleList = list;
        mAllObject = list;
        mContext = context;
    }

    public static class UniViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener {
        CardView cv;
        TextView universityName;
        ImageView universityLogo;
        CardView yourCustomView;
        private int originalHeight = 0;
        private int expandHeight = 0;
        private boolean mIsViewExpanded = false;
        private Context mInsideContext;
        private LinearLayout lnBrief;
        private LinearLayout lnPhone;
        private LinearLayout lnWebsite;
        private LinearLayout lnContent;
        private TextView tvPhone;
        private TextView tvWebsite;
        private String uniID;


        UniViewHolder(View itemView, Context context) {
            super(itemView);
            mInsideContext = context;
            cv = (CardView) itemView.findViewById(R.id.cvUniversity);
            universityName = (TextView) itemView.findViewById(R.id.tvUniversityName);
            universityLogo = (ImageView) itemView.findViewById(R.id.imgUniversityLogo);
            yourCustomView = (CardView) itemView.findViewById(R.id.testVisible);
            lnBrief = (LinearLayout) itemView.findViewById(R.id.lnBrief);
            lnPhone = (LinearLayout) itemView.findViewById(R.id.lnPhone);
            lnWebsite = (LinearLayout) itemView.findViewById(R.id.lnWebsite);
            lnContent = (LinearLayout) itemView.findViewById(R.id.lnContent);
            tvPhone = (TextView) itemView.findViewById(R.id.tvPhoneNumber);
            tvWebsite = (TextView) itemView.findViewById(R.id.tvWebsite);
            lnBrief.setOnClickListener(this);
            lnWebsite.setOnClickListener(this);
            lnPhone.setOnClickListener(this);
            lnContent.setOnClickListener(this);
            if (mIsViewExpanded == false) {
                // Set Views to View.GONE and .setEnabled(false)
                yourCustomView.setVisibility(View.GONE);
                yourCustomView.setEnabled(false);
            }
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.lnContent:
                    final View view = v;
                    if (originalHeight == 0) {
                        originalHeight = view.getHeight();
                        expandHeight = ((int) mInsideContext.getResources().getDimension(R.dimen.icon_size)) * 3
                                + ((int) mInsideContext.getResources().getDimension(R.dimen.linear_padding_top)) * 2
                                + ((int) mInsideContext.getResources().getDimension(R.dimen.linear_padding_top_bottom) * 5); //5 is magic number :

                    }

                    // Declare a ValueAnimator object
                    ValueAnimator valueAnimator;
                    if (!mIsViewExpanded) {
                        yourCustomView.setVisibility(View.VISIBLE);
                        yourCustomView.setEnabled(true);
                        mIsViewExpanded = true;
                        valueAnimator = ValueAnimator.ofInt(originalHeight, originalHeight + expandHeight); // These values in this method can be changed to expand however much you like
                    } else {
                        mIsViewExpanded = false;
                        valueAnimator = ValueAnimator.ofInt(originalHeight + expandHeight, originalHeight);

                        Animation a = new AlphaAnimation(1.00f, 0.00f); // Fade out

                        a.setDuration(200);
                        // Set a listener to the animation and configure onAnimationEnd
                        a.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                yourCustomView.setVisibility(View.INVISIBLE);
                                yourCustomView.setEnabled(false);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });

                        // Set the animation on the custom view
                        yourCustomView.startAnimation(a);
                    }
                    valueAnimator.setDuration(200);
                    valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        public void onAnimationUpdate(ValueAnimator animation) {
                            Integer value = (Integer) animation.getAnimatedValue();
                            view.getLayoutParams().height = value.intValue();
                            view.requestLayout();
                        }
                    });


                    valueAnimator.start();
                    break;
                case R.id.lnBrief:
                    new GetRecordFromSystem().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uniID);
                    break;
                case R.id.lnPhone:
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tvPhone.getText()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mInsideContext.startActivity(intent);
                    break;
                case R.id.lnWebsite:
                    String url = tvWebsite.getText().toString();
                    if (!url.startsWith("http://") && !url.startsWith("https://"))
                        url = "http://" + url;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mInsideContext.startActivity(browserIntent);
                    break;

                default:
                    break;
            }

        }

        class GetRecordFromSystem extends AsyncTask<String, String, List<Record>> {

            private final static String TAG = "LoginToSystem";

            protected ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Log.d(TAG, "onPreExecute");
                progressDialog = new ProgressDialog(mInsideContext,
                        ProgressDialog.STYLE_SPINNER);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage(mInsideContext.getString(R.string.message_authenticating));
                progressDialog.show();
            }

            @Override
            protected List<Record> doInBackground(String... params) {
                String result = ApiUtils.getRecord(params[0]);
                return DataParseUtils.parseRecord(result);
            }

            @Override
            protected void onPostExecute(List<Record> s) {
                super.onPostExecute(s);
                Log.d(TAG, "return message: " + s);
                if (s != null) {
                    PrefUtils.saveInt(mInsideContext,PrefUtils.KEY_UNIVERSITY_ID, Integer.parseInt(uniID));
                    PrefUtils.saveString(mInsideContext, PrefUtils.KEY_UNIVERSITY_NAME, universityName.getText().toString());
                    ActivityRecordContent.sUniversityName = universityName.getText().toString();
                    Intent intent = new Intent(mInsideContext, ActivityListRecord.class);
                    ActivityListRecord.listData = s;
                    mInsideContext.startActivity(intent);
                } else {
                    Toast.makeText(mInsideContext, "Can't get any information for this university!", Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }
        }
    }
    @Override
    public int getItemCount() {
        return mVisibleList.size();
    }

    @Override
    public void onBindViewHolder(UniViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public UniViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.university_list_item, parent, false);
        UniViewHolder pvh = new UniViewHolder(v, mContext);
        return pvh;
    }

    @Override
    public void onBindViewHolder(UniViewHolder holder, int position) {
        holder.tvPhone.setText(mVisibleList.get(position).getUniPhoneNumber());
        holder.tvWebsite.setText(mVisibleList.get(position).getUniWebsite());
        holder.universityName.setText(mVisibleList.get(position).getUniName());
        holder.uniID = Constant.EMPTY + mVisibleList.get(position).getIdUniversity();
        Picasso.with(mContext).load(mVisibleList.get(position).getUniLogoURL()).into(holder.universityLogo);
    }

    public void setFilter(String queryText) {
        mVisibleList = new ArrayList<>();
        queryText = queryText.toString().toLowerCase();
        for (University item : mAllObject) {
            if (item.getUniName().toLowerCase().contains(queryText))
                mVisibleList.add(item);
        }
        notifyDataSetChanged();
    }

    public void flushFilter() {
        mVisibleList = new ArrayList<>();
        mVisibleList.addAll(mAllObject);
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public University removeItem(int position) {
        final University model = mVisibleList.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, University model) {
        mVisibleList.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final University model = mVisibleList.remove(fromPosition);
        mVisibleList.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }
}
