package xyz.tangram.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import xyz.tangram.demo.base.BaseAdapter;
import xyz.tangram.demo.view.SimpleListView;

/**
 * Created by linyongsheng on 16/6/9.
 */
public class RefreshActivity extends Activity implements SimpleListView.OnLoadListener, AdapterView.OnItemClickListener {
    private SimpleListView mContentRlv;
    private View mEmptyView;
    private RefreshListAdapter mAdapter;
    private int mPage = 1;
    private int mIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refresh_activity);
        mContentRlv = (SimpleListView) findViewById(R.id.content_rlv);
        mEmptyView = findViewById(R.id.empty_rl);
        mContentRlv.setEmptyView(mEmptyView);
        mAdapter = new RefreshListAdapter(this);
        mContentRlv.setAdapter(mAdapter);
        mContentRlv.setOnLoadListener(this);
        mContentRlv.setOnItemClickListener(this);
    }

    private void getData(final int page) {
        Toast.makeText(this, "开始加载数据:" + page, Toast.LENGTH_SHORT).show();
        mContentRlv.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RefreshActivity.this, "结束加载数据:" + page, Toast.LENGTH_SHORT).show();
                List<RefreshListItem> data = new LinkedList<>();
                RefreshListItem item;
                for (int i = 0; i <15; i++) {
                    item = new RefreshListItem();
                    item.title = "this is title:" + mIndex;
                    mIndex++;
                    data.add(item);
                }
                mAdapter.setData(data, page == 1 ? true : false);
                mContentRlv.finishLoad(page == 5 ? true : false); //最多加载五页数据
            }
        }, 2000);
    }

    @Override
    public void onLoad(boolean isRefresh) {
        if (isRefresh) {
            mPage = 1;
        } else {
            mPage++;
        }
        getData(mPage);
    }

    private static class RefreshListItem {
        public String title;
    }


    private static class RefreshListAdapter extends BaseAdapter<RefreshListItem> {

        public RefreshListAdapter(Activity context) {
            super(context);
        }

        @Override
        protected int getItemLayoutId(int itemViewType) {
            return R.layout.refresh_list_item;
        }

        @Override
        protected void handleItem(int itemViewType, int position, RefreshListItem item, ViewHolder holder, boolean reused) {
            holder.get(R.id.title, TextView.class).setText(item.title);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "点击了" + position + " ", Toast.LENGTH_SHORT).show();
    }
}
