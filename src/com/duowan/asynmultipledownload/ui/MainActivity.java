package com.duowan.asynmultipledownload.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.duowan.asynmultipledownload.AppControler;
import com.duowan.asynmultipledownload.R;

public class MainActivity extends Activity {

	private ListView mListView;
	private DownLoadBaseAdapter mBaseAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// AppControler.getInstance().init();
		setContentView(R.layout.activity_main);
		mListView = (ListView) findViewById(R.id.listView1);
		mBaseAdapter = new DownLoadBaseAdapter(this);
		mListView.setAdapter(mBaseAdapter);
		addData();
	}

	private void addData() {
		ArrayList<DownLoadParcel> list = new ArrayList<DownLoadParcel>();
		DownLoadParcel parcel1 = new DownLoadParcel();
		parcel1.setId(1);
		parcel1.setUrl(DownLoadUrlUtil.URL_ONE);
		parcel1.setFilePath(DownLoadUrlUtil
				.getFilePath(DownLoadUrlUtil.URL_ONE));
		list.add(parcel1);

		DownLoadParcel parcel2 = new DownLoadParcel();
		parcel2.setId(2);
		parcel2.setUrl(DownLoadUrlUtil.URL_TWO);
		parcel2.setFilePath(DownLoadUrlUtil
				.getFilePath(DownLoadUrlUtil.URL_TWO));
		list.add(parcel2);

		DownLoadParcel parcel3 = new DownLoadParcel();
		parcel3.setId(3);
		parcel3.setUrl(DownLoadUrlUtil.URL_THREE);
		parcel3.setFilePath(DownLoadUrlUtil
				.getFilePath(DownLoadUrlUtil.URL_THREE));
		list.add(parcel3);

		DownLoadParcel parcel4 = new DownLoadParcel();
		parcel4.setId(4);
		parcel4.setUrl(DownLoadUrlUtil.URL_FOUR);
		parcel4.setFilePath(DownLoadUrlUtil
				.getFilePath(DownLoadUrlUtil.URL_FOUR));
		list.add(parcel4);

		DownLoadParcel parcel5 = new DownLoadParcel();
		parcel5.setId(5);
		parcel5.setUrl(DownLoadUrlUtil.URL_FIVE);
		parcel5.setFilePath(DownLoadUrlUtil
				.getFilePath(DownLoadUrlUtil.URL_FIVE));
		list.add(parcel5);

		DownLoadParcel parcel6 = new DownLoadParcel();
		parcel6.setId(6);
		parcel6.setUrl(DownLoadUrlUtil.URL_SIX);
		parcel6.setFilePath(DownLoadUrlUtil
				.getFilePath(DownLoadUrlUtil.URL_SIX));
		list.add(parcel6);

		DownLoadParcel parcel7 = new DownLoadParcel();
		parcel7.setId(7);
		parcel7.setUrl(DownLoadUrlUtil.URL_SEVEN);
		parcel7.setFilePath(DownLoadUrlUtil
				.getFilePath(DownLoadUrlUtil.URL_SEVEN));
		list.add(parcel7);

		DownLoadParcel parcel8 = new DownLoadParcel();
		parcel8.setId(8);
		parcel8.setUrl(DownLoadUrlUtil.URL_EIGHT);
		parcel8.setFilePath(DownLoadUrlUtil
				.getFilePath(DownLoadUrlUtil.URL_EIGHT));
		list.add(parcel8);

		DownLoadParcel parcel9 = new DownLoadParcel();
		parcel9.setId(9);
		parcel9.setUrl(DownLoadUrlUtil.URL_NINE);
		parcel9.setFilePath(DownLoadUrlUtil
				.getFilePath(DownLoadUrlUtil.URL_NINE));
		list.add(parcel9);

		DownLoadParcel parcel10 = new DownLoadParcel();
		parcel10.setId(10);
		parcel10.setUrl(DownLoadUrlUtil.URL_TEN);
		parcel10.setFilePath(DownLoadUrlUtil
				.getFilePath(DownLoadUrlUtil.URL_TEN));
		list.add(parcel10);

		DownLoadParcel parcel11 = new DownLoadParcel();
		parcel11.setId(11);
		parcel11.setUrl(DownLoadUrlUtil.URL_ELEVENT);
		parcel11.setFilePath(DownLoadUrlUtil
				.getFilePath(DownLoadUrlUtil.URL_ELEVENT));
		list.add(parcel11);

		DownLoadParcel parcel12 = new DownLoadParcel();
		parcel12.setId(12);
		parcel12.setUrl(DownLoadUrlUtil.URL_TWELVE);
		parcel12.setFilePath(DownLoadUrlUtil
				.getFilePath(DownLoadUrlUtil.URL_TWELVE));
		list.add(parcel12);

		DownLoadParcel parcel13 = new DownLoadParcel();
		parcel13.setId(13);
		parcel13.setUrl(DownLoadUrlUtil.URL_THIRTEEN);
		parcel13.setFilePath(DownLoadUrlUtil
				.getFilePath(DownLoadUrlUtil.URL_THIRTEEN));
		list.add(parcel13);

		DownLoadParcel parcel14 = new DownLoadParcel();
		parcel14.setId(14);
		parcel14.setUrl(DownLoadUrlUtil.URL_FORTEEN);
		parcel14.setFilePath(DownLoadUrlUtil
				.getFilePath(DownLoadUrlUtil.URL_FORTEEN));
		list.add(parcel14);

		DownLoadParcel parcel15 = new DownLoadParcel();
		parcel15.setId(15);
		parcel15.setUrl(DownLoadUrlUtil.URL_FIFTEEN);
		parcel15.setFilePath(DownLoadUrlUtil
				.getFilePath(DownLoadUrlUtil.URL_FIFTEEN));
		list.add(parcel15);
		mBaseAdapter.setData(list);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppControler.getInstance().exit();
	}
}
