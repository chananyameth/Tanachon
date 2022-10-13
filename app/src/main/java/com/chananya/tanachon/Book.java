package com.chananya.tanachon;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class Book
{
	private String name;
	private String filePath;    //csv file

	private String[] lines;
	private int levels; //how many commas in a line

	public Book(Context _con, String _name, String _filePath)
	{
		name = _name;
		filePath = _filePath;

		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(
					_con.getAssets().open(_filePath), "UTF-8"));

			String str;
			StringBuilder sb = new StringBuilder();
			while ((str = br.readLine()) != null) {
				sb.append(str + "\n");
			}
			if (br != null)
				br.close();
			lines = sb.toString().split("\n");
		}
		catch (Exception e)
		{
			Log.e("Book", "Can't open the file");
		}

		//checks for the validation of the file,
		//and sets the 'levels' attribute
		checkFileValidation();
	}

	public String getName() {
		return name;
	}
	public int getLevelsCount() {
		return levels;
	}

	private void checkFileValidation()
	{
		levels = 0;

		for (char c : lines[0].toCharArray()) {
			if(c==',')
				levels++;
		}

		for (String s : lines) {
			int i = 0;
			for (char c : s.toCharArray()) {
				if (c == ',')
					i++;
			}
			if(i != levels)
				Log.e("Book","invalid line: " + s);
		}
	}
	public String[] getlines(String levelsPath)
	{
		int emptyPath = (levelsPath == "") ? -1 : 0;

		LinkedList<String> selected = new LinkedList<>();
		for (String s: lines)
		{
			if(s.startsWith(levelsPath))
				selected.add(s.substring(levelsPath.length() + 1 + emptyPath));
		}

		String[] ret = new String[selected.size()];
		int i=0;
		for (String s: selected)
		{
			ret[i++] = s;
		}

		return ret;
	}
	public String[] getKeysAtLevel(String levelsPath)
	{
		int emptyPath = (levelsPath == "") ? -1 : 0;

		LinkedList<String> selected = new LinkedList<>();
		for (String s: lines)
		{
			if(s.startsWith(levelsPath))
			{
				String end = s.substring(levelsPath.length() + 1 + emptyPath);
				if(end.contains(","))
					selected.add(end.substring(0, end.indexOf(",")));
				else
					selected.add(end);
			}
		}

		String[] ret = new String[selected.size()];
		int i=0;
		for (String s: selected)
		{
			ret[i++] = s;
		}

		return ret;
	}
}
