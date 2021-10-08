package com.todo.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import com.todo.service.TodoSortByDate;
import com.todo.service.TodoSortByName;
import com.todo.db.DBConnect;

public class TodoList {
	private List<TodoItem> list; // this will be deleted
	private Connection con;

	public TodoList() {
		this.list = new ArrayList<TodoItem>(); // this will be deleted
		this.con = DBConnect.getConnection();
	}

	public void addItem(TodoItem t) {
		list.add(t);
	}

	public void deleteItem(TodoItem t) {
		list.remove(t);
	}

	void editItem(TodoItem t, TodoItem updated) {
		int index = list.indexOf(t);
		list.remove(index);
		list.add(updated);
	}

	public ArrayList<TodoItem> getList() {
		return new ArrayList<TodoItem>(list);
	}

	public void sortByName() {
		Collections.sort(list, new TodoSortByName());

	}

	public void listAll() {

		for (TodoItem item : list) {
			System.out.println(list.indexOf(item)+1 + "." + item.toString());
		}
	}
	
	public void reverseList() {
		Collections.reverse(list);
	}

	public void sortByDate() {
		Collections.sort(list, new TodoSortByDate());
	}

	public int indexOf(TodoItem t) {
		return list.indexOf(t);
	}

	public Boolean isDuplicate(String title) {
		for (TodoItem item : list) {
			if (title.equals(item.getTitle())) return true;
		}
		return false;
	}

	public Boolean isDuplicate(int num) {
		for (TodoItem item : list) {
			if (num == list.indexOf(item) + 1) return true;
		}
		return false;
	}

	public Boolean isDuplicateCate(String category) {
		for (TodoItem item : list) {
			if (category.equals(item.getCategory())) return true;
		}
		return false;
	}

	public Boolean isInteger(String due_date) {
		try { Integer.parseInt(due_date); }
		catch(NumberFormatException e) { return false; }
		return true;
	}

	public void importData(String filename) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line;
			String sql = "INSERT INTO list (Title, Memo, Category, Current_date, Due_date)"
					+ "VALUES (?, ?, ?, ?, ?);";
			int records = 0;
			while((line = br.readLine()) != null){
				StringTokenizer st = new StringTokenizer(line, "##");
				String category = st.nextToken();
				String title = st.nextToken();
				String memo = st.nextToken();
				String due_date = st.nextToken();
				String current_date = st.nextToken();

				PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1, title);
				ps.setString(2, memo);
				ps.setString(3, category);
				ps.setString(4, current_date);
				ps.setString(5, due_date);
				int count = ps.executeUpdate();
				if(count > 0) records++;
				ps.close();
			}
			System.out.println(records + " 레코드가 추가되었습니다.");
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}
}