package com.chananya.tanachon;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.*;
import android.widget.AdapterView.*;
import android.view.*;
import android.graphics.*;
import java.io.*;

public class MainActivity extends Activity {
	//views
	private ListView listView;
	private TextView title;
	private Spinner spinner;
	private Button resizeB;

	//other
	private Book tehilim;
	private BookAdapter adapter;
	private LayoutInflater inflater;

	String[] fontsItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		connectViews();
		readFiles(1);
		initializeVars();
		setListeners();
	}

	private void connectViews() {
		listView = (ListView) findViewById(R.id.listView);
		spinner = (Spinner) findViewById(R.id.spinner);
		title = (TextView) findViewById(R.id.title);
		resizeB = (Button) findViewById(R.id.resizeB);
	}

	private void readFiles(int versionKind) {

	}

	private void initializeVars() {
		inflater = this.getLayoutInflater();

		fontsItems = new String[]{"KeterYG","TaameyAshkenaz", "TaameyDavidCLM", "TaameyFrankCLM"};

		tehilim = new Book(this, "psalms", "psalms_he_nikkud_taamei_hamikra.csv");

		spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, fontsItems));
		title.setText("תהילון");

		adapter = new BookAdapter(this, tehilim.getlines(""), TypeFaceId.KeterYG);
		listView.setAdapter(adapter);
		listView.setFastScrollEnabled(true);
		listView.setFastScrollAlwaysVisible(true);
	}

	private void setListeners() {
		spinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
			{
				adapter.setTypeFace(getTypeFaceId(position));
				adapter.notifyDataSetChanged();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parentView){}
		});

		resizeB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				toast("clicked");
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				View dialogView = inflater.inflate(R.layout.font_dialog, null);

				Spinner fonts = (Spinner) findViewById(R.id.fonts);
				SeekBar textSize = (SeekBar) findViewById(R.id.textSize);
				TextView example = (TextView) dialogView.findViewById(R.id.exampleText);

				fonts.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, fontsItems));

				example.setTypeface(getTypeFaceFromNum(0));//-----arbitrary!!!!!
				example.setTextSize(30);
				example.setText(readExampleFile());

				builder.setView(dialogView);
				AlertDialog alertDialog = builder.create();
				alertDialog.show();
			}
		});

    /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    	@Override
    	public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
    	{
    		toast("clicked");
    	}
    });*/
	}

	private TypeFaceId getTypeFaceId(int n)
	{
		switch(n)
		{
			case 0:	return TypeFaceId.KeterYG;
			case 1:	return TypeFaceId.TaameyAshkenaz;
			case 2:	return TypeFaceId.TaameyDavidCLM;
			case 3:	return TypeFaceId.TaameyFrankCLM;

			default:	return TypeFaceId.KeterYG;
		}
	}
	private Typeface getTypeFaceFromNum(int n)
	{
		switch(n)
		{
			case 0:	return Typeface.createFromAsset(this.getAssets(), "fonts/KeterYG-Medium.ttf");
			case 1:	return Typeface.createFromAsset(this.getAssets(), "fonts/TaameyAshkenaz-Medium.ttf");
			case 2:	return Typeface.createFromAsset(this.getAssets(), "fonts/TaameyDavidCLM-Medium.ttf");
			case 3:	return Typeface.createFromAsset(this.getAssets(), "fonts/TaameyFrankCLM-Medium.ttf");

			default:	return Typeface.createFromAsset(this.getAssets(), "fonts/KeterYG-Medium.ttf");
		}
	}

	private void toast(String s) {
		Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
	}
	private String readExampleFile()
	{
		try
		{
			InputStream in;
			in = this.getAssets().open("example.txt");
			int size = in.available();
			byte[] buffer = new byte[size];
			in.read(buffer);
			in.close();
			return buffer.toString();
		}
		catch (Exception e)
		{
			toast("Can't open example.txt file");
			return "";
		}
	}
	/*private void toast_assets_list() {
		try {
			for (int i = 0; i < getAssets().list("").length; ++i) toast(getAssets().list("")[i]);
		}
		catch (Exception e) {
		}
	}*/
}
