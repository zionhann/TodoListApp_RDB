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
		String sql = "INSERT INTO list (Title, Memo, Category, Current_date, Due_date, isCompleted)"
					+ "VALUES (?, ?, ?, ?, ?, ?);";
		PreparedStatement ps;
		int isAdded = 0;
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, t.getTitle());
			ps.setString(2, t.getDesc());
			ps.setString(3, t.getCategory());
			ps.setString(4, t.getCurrent_date());
			ps.setString(5, t.getDue_date());
			ps.setInt(6, t.isCompleted());
			isAdded = ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isAdded;
	}

	public int deleteItem(TodoItem t) {
		String sql = "DELETE FROM list WHERE ID=?;";
		PreparedStatement ps;
		int isDeleted=0;
		try {
			ps = con.prepareStatement(sql);
			ps.setInt(1, t.getID());
			isDeleted = ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isDeleted;
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

	public void checkItem(int index, int isCompleted) {
		String sql = "UPDATE list SET isCompleted=?"
					+ "WHERE ID=?;";
		try {
			PreparedStatement p = con.prepareStatement(sql);
			p.setInt(1, (isCompleted>0)?0:1);
			p.setInt(2, index);
			p.executeUpdate();
			p.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
				int isCompleted = r.getInt("isCompleted");

				TodoItem item = new TodoItem(title, desc, category, due_date, isCompleted>0);
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

	//@overload
	public ArrayList<TodoItem> getList(String keyword) {
		ArrayList<TodoItem> list = new ArrayList<>();
		String sql = "SELECT * FROM list WHERE Title LIKE ? OR Memo LIKE ?;";
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, "%" + keyword + "%");
			ps.setString(2, "%" + keyword + "%");
			ResultSet r = ps.executeQuery();
			while(r.next()) {
				int id = r.getInt("ID");
				String category = r.getString("Category");
				String title = r.getString("Title");
				String desc = r.getString("Memo");
				String due_date = r.getString("Due_date");
				String current_date = r.getString("Current_date");
				int isCompleted = r.getInt("isCompleted");

				TodoItem item = new TodoItem(title, desc, category, due_date, isCompleted>0);
				item.setID(id);
				item.setCurrent_date(current_date);
				item.setDue_date(due_date);
				list.add(item);
			}
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	//@Overload
	public ArrayList<TodoItem> getList(boolean isCompleted) {
		ArrayList<TodoItem> list = new ArrayList<>();
		String sql = "SELECT * FROM list WHERE isCompleted =?;";
		try {
			PreparedStatement p = con.prepareStatement(sql);
			p.setInt(1, (isCompleted)?1:0);
			ResultSet r = p.executeQuery();

			while(r.next()){
				int id = r.getInt("ID");
				String category = r.getString("Category");
				String title = r.getString("Title");
				String desc = r.getString("Memo");
				String due_date = r.getString("Due_date");
				String current_date = r.getString("Current_date");
				int completed = r.getInt("isCompleted");

				TodoItem item = new TodoItem(title, desc, category, due_date, completed>0);
				item.setID(id);
				item.setCurrent_date(current_date);
				item.setDue_date(due_date);
				list.add(item);
			}
			p.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<TodoItem> getList_cate(String keyword) {
		ArrayList<TodoItem> list = new ArrayList<>();
		String sql = "SELECT * FROM list WHERE Category= ?;";
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, keyword);
			ResultSet r = ps.executeQuery();
			while(r.next()) {
				int id = r.getInt("ID");
				String category = r.getString("Category");
				String title = r.getString("Title");
				String desc = r.getString("Memo");
				String due_date = r.getString("Due_date");
				String current_date = r.getString("Current_date");
				int isCompleted = r.getInt("isCompleted");

				TodoItem item = new TodoItem(title, desc, category, due_date, isCompleted>0);
				item.setID(id);
				item.setCurrent_date(current_date);
				item.setDue_date(due_date);
				list.add(item);
			}
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<String> getList_cateOnly() {
		ArrayList<String> list = new ArrayList<>();
		String sql = "SELECT DISTINCT Category FROM list;";
		try {
			Statement s = con.createStatement();
			ResultSet rs = s.executeQuery(sql);

			while(rs.next()){
				String category = rs.getString("Category");
				list.add(category);
			}
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

	public ArrayList<TodoItem> orderBy(String keyword, boolean desc) {
		ArrayList<TodoItem> list = new ArrayList<>();
		String sql = "SELECT * FROM list ORDER BY " + keyword;
		if(desc) sql += " DESC";
		try {
			Statement s = con.createStatement();
			ResultSet r = s.executeQuery(sql);

			while(r.next()) {
				int id = r.getInt("ID");
				String category = r.getString("Category");
				String title = r.getString("Title");
				String memo = r.getString("Memo");
				String due_date = r.getString("Due_date");
				String current_date = r.getString("Current_date");
				int isCompleted = r.getInt("isCompleted");

				TodoItem item = new TodoItem(title, memo, category, due_date, isCompleted>0);
				item.setID(id);
				item.setCurrent_date(current_date);
				item.setDue_date(due_date);
				list.add(item);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
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

	public Boolean isDuplicate(String title, ArrayList<TodoItem> l) {
		for (TodoItem item : l) {
			if (title.equals(item.getTitle())) return true;
		}
		return false;
	}
	//@Overload
	public Boolean isDuplicate(int num, ArrayList<TodoItem> l) {
		for (TodoItem item : l) {
			if (num == item.getID()) return true;
		}
		return false;
	}

	public Boolean isDuplicateCate(String category, ArrayList<TodoItem> l) {
		for (TodoItem item : l) {
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