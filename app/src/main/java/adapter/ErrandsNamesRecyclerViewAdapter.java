package adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cs4330.utep.edu.errandsgo.R;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ErrandsNamesRecyclerViewAdapter extends RecyclerView.Adapter<ErrandsNamesRecyclerViewAdapter.ErrandNamesViewHolder> {

    List<String> mErrandTitles;
    private Context mContext;

    public ErrandsNamesRecyclerViewAdapter(Context context) {
        mContext = context;
        randomizeErrandNames();
    }

    public void randomizeErrandNames() {
        mErrandTitles = Arrays.asList(getErrandNamesResource());
        Collections.shuffle(mErrandTitles);
    }

    private String[] getErrandNamesResource() {
        return mContext.getResources().getStringArray(R.array.errand_names);
    }

    @Override
    public ErrandNamesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflatedView = LayoutInflater.from(mContext).inflate(R.layout.errands_title_view, viewGroup, false);
        return new ErrandNamesViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(ErrandNamesViewHolder viewHolder, int i) {
        String errandName = getItem(i);
        viewHolder.mCatNameTextView.setText(errandName);
    }

    public String getItem(int position) {
        return mErrandTitles.get(position);
    }

    @Override
    public int getItemCount() {
        return mErrandTitles.size();
    }

    public class ErrandNamesViewHolder extends RecyclerView.ViewHolder {
        TextView mCatNameTextView;

        public ErrandNamesViewHolder(View itemView) {
            super(itemView);
            mCatNameTextView = (TextView) itemView.findViewById(R.id.errand_name_textview);
        }
    }
}