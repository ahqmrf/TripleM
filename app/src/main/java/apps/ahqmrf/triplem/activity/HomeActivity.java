package apps.ahqmrf.triplem.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import apps.ahqmrf.triplem.R;
import apps.ahqmrf.triplem.util.DatabaseHelper;

public class HomeActivity extends DrawerActivity {

    private ViewGroup layout;
    private FloatingActionButton mAddBtn;
    private DatabaseHelper helper;
    private TextView mTextView;
    private RecyclerView mRecyclerView;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_home, mContentFrame);

        mContext = getApplicationContext();
        layout = (ViewGroup) findViewById(R.id.frame_layout);
        helper = new DatabaseHelper(this, null, null, 1);

        if(helper.getAllMemories() == null) {
            mTextView = (TextView) getLayoutInflater().inflate(R.layout.no_memories, layout, false);
            layout.addView(mTextView, 0);
        }
        else {
            // TODO
            mRecyclerView = (RecyclerView) getLayoutInflater().inflate(R.layout.memories_home, layout, false);
            layout.addView(mRecyclerView, 0);
            setRecyclerView();
        }

        mAddBtn = (FloatingActionButton) findViewById(R.id.fbtn_add_memory);
        mAddBtn.setOnClickListener(mOnclickListener);
    }

    private void setRecyclerView() {

    }

    private View.OnClickListener mOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fbtn_add_memory:
                    startActivity(new Intent(mContext, CreateMemoryActivity.class));
                    break;
            }
        }
    };
}
