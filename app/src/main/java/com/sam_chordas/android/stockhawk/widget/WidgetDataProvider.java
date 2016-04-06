package com.sam_chordas.android.stockhawk.widget;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.StocksModel;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by admin on 4/3/2016.
 */
public class WidgetDataProvider implements QuoteWidgetRemoteViewsService.RemoteViewsFactory{

    private static final String TAG = "WidgetDataProvider";

    Set<StocksModel> list = new HashSet<>();
    ArrayList<StocksModel> arrayList;
    Context mContext = null;

    public WidgetDataProvider(Context context, Intent intent) {

        mContext = context;
    }

    @Override
    public void onCreate() {

        getAllData(mContext);
    }

    @Override
    public void onDataSetChanged() {
        getAllData(mContext);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews view = new RemoteViews(mContext.getPackageName(), R.layout.widget_collection_item);

        view.setContentDescription(R.id.stock_symbol, arrayList.get(position).getName());

        view.setTextViewText(R.id.stock_symbol, arrayList.get(position).getSymbol());
        view.setTextViewText(R.id.stock_bid_price, arrayList.get(position).getBidPrice());
        view.setTextViewText(R.id.change, arrayList.get(position).getPercentChange());

        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void getAllData(Context context){
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(QuoteProvider.Quotes.CONTENT_URI, null, null, null, null);

        if(cursor != null){
            cursor.moveToFirst();
            do{

                StocksModel model = new StocksModel();
                model.setSymbol(cursor.getString(cursor.getColumnIndex("symbol")));
                model.setBidPrice(cursor.getString(cursor.getColumnIndex("bid_price")));
                model.setPercentChange(cursor.getString(cursor.getColumnIndex("percent_change")));
                model.setName(cursor.getString(cursor.getColumnIndex("name")));

                list.add(model);

            }while (cursor.moveToNext());

            arrayList = new ArrayList<>(list);
            cursor.close();
        }
    }
}
