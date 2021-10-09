package com.todo.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;

import com.todo.TodoMain;
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

	public int addItem(TodoItem t) {
		String sql = "INSERT INTO list (Title, Memo, Category, Current_date, Due_date)"
					+ "VALUES (?, ?, ?, ?, ?);";
		PreparedStatement ps;
		int isAdded = 0;
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, t.getTitle());
			ps.setString(2, t.getDesc());
			ps.setString(3, t.getCategory());
			ps.setString(4, t.getCurrent_date());
			ps.setString(5, t.getDue_date());
			isAdded = ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isAdded;
	}

	public void deleteItem(TodoItem t) {
		list.remove(t);
	}

	public int editItem(TodoItem t) {
		String sql = "UPDATE list SET Title=?, Memo=?, Category=?, Current_date=?, Due_date=?"
					+ "WHERE ID=?;";
		PreparedStatement ps;
		int isAdded =0;
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, t.getTitle());
			ps.setString(2, t.getDesc());
			ps.setString(3, t.getCategory());
			ps.setString(4, t.getCurrent_date());
			ps.setString(5, t.getDue_date());
			ps.setInt(6, t.getID());
			isAdded = ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isAdded;
	}

	public ArrayList<TodoItem> getList() {
		ArrayList<TodoItem> list = new ArrayList<>();
		Statement s;
		try {
			s = con.createStatement();
			String sql = "SELECT * FROM list;";
			ResultSet r = s.executeQuery(sql);

			while(r.next()) {
				int id = r.getInt("ID");
				String category = r.getString("Category");
				String title = r.getString("Title");
				String desc = r.getString("Memo");
				String due_date = r.getString("Due_date");
				String current_date = r.getString("Current_date");
				TodoItem item = new TodoItem(title, desc, category, due_date);
				item.setID(id);
				item.setCurrent_date(current_date);
				item.setDue_date(due_date);
				list.add(item);
			}
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public int numberOf() {
		Statement s;
		int num=0;
		try {
			s = con.createStatement();
			String sql = "SELECT count(ID) FROM list;";
			ResultSet r = s.executeQuery(sql);
			r.next();
			num = r.getInt("count(ID)");
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return num;
	}

	public void sortByName() {
		Collections.sort(list, new TodoSortByName());

	}

	public void listAll() {

		for (TodoItem item : list) {
			System.out.println(item.toString());
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

	public void setConClose() {
		DBConnect.closeConnection();
	}
}