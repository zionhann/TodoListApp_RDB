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
		String sql = "INSERT INTO list (Title, Memo, Current_date, Due_date, isCompleted, Priority, Category_id)"
					+ "VALUES (?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement ps;
		int categoryId = getCategoryId(t.getCategory());
		int isAdded = 0;
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, t.getTitle());
			ps.setString(2, t.getDesc());
			ps.setString(3, t.getCurrent_date());
			ps.setString(4, t.getDue_date());
			ps.setInt(5, t.getIsCompleted());
			ps.setInt(6, t.getPriority());
			ps.setInt(7, categoryId);
			isAdded = ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isAdded;
	}

	public int deleteItem(List<String> selectedNum) {
		Statement s;
		int isDeleted=0;
		String id = String.join(" OR ID= ", selectedNum);
		String sql = "DELETE FROM list WHERE ID=" + id + ";";
		try {
			s = con.createStatement();
			isDeleted = s.executeUpdate(sql);
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isDeleted;
	}

	public int editItem(TodoItem t) {
		String sql = "UPDATE list SET Title=?, Memo=?, Current_date=?, Due_date=?, Priority=?, Category_id=?"
					+ "WHERE ID=?;";
		PreparedStatement ps;
		int categoryId = getCategoryId(t.getCategory());
		int isAdded =0;
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, t.getTitle());
			ps.setString(2, t.getDesc());
			ps.setString(3, t.getCurrent_date());
			ps.setString(4, t.getDue_date());
			ps.setInt(5, t.getPriority());
			ps.setInt(6, categoryId);
			ps.setInt(7, t.getID());
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

	public void addCate(String keyword)  {
		String sql = "INSERT INTO category (Name) VALUES(?);";
		PreparedStatement p;
		try {
			p = con.prepareStatement(sql);
			p.setString(1, keyword);
			p.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<TodoItem> getList() {
		ArrayList<TodoItem> list = new ArrayList<>();
		Statement s;
		try {
			s = con.createStatement();
			String sql = "SELECT * FROM list INNER JOIN category ON list.Category_id=category.ID;";
			ResultSet r = s.executeQuery(sql);

			while(r.next()) {
				int id = r.getInt("ID");
				String category = r.getString("Name");
				String title = r.getString("Title");
				String desc = r.getString("Memo");
				String due_date = r.getString("Due_date");
				String current_date = r.getString("Current_date");
				int isCompleted = r.getInt("isCompleted");
				int priority = r.getInt("Priority");

				TodoItem item = new TodoItem(title, desc, category, due_date, isCompleted>0, priority);
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
		String sql = "SELECT * FROM list INNER JOIN category ON list.Category_id = category.ID WHERE Title LIKE ? OR Memo LIKE ?;";
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, "%" + keyword + "%");
			ps.setString(2, "%" + keyword + "%");
			ResultSet r = ps.executeQuery();
			while(r.next()) {
				int id = r.getInt("ID");
				String category = r.getString("Name");
				String title = r.getString("Title");
				String desc = r.getString("Memo");
				String due_date = r.getString("Due_date");
				String current_date = r.getString("Current_date");
				int isCompleted = r.getInt("isCompleted");
				int priority = r.getInt("Priority");

				TodoItem item = new TodoItem(title, desc, category, due_date, isCompleted>0, priority);
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
		String sql = "SELECT * FROM list INNER JOIN category ON list.Category_id = category.ID "
					+ "WHERE isCompleted =?;";
		try {
			PreparedStatement p = con.prepareStatement(sql);
			p.setInt(1, (isCompleted)?1:0);
			ResultSet r = p.executeQuery();

			while(r.next()){
				int id = r.getInt("ID");
				String category = r.getString("Name");
				String title = r.getString("Title");
				String desc = r.getString("Memo");
				String due_date = r.getString("Due_date");
				String current_date = r.getString("Current_date");
				int completed = r.getInt("isCompleted");
				int priority = r. getInt("Priority");

				TodoItem item = new TodoItem(title, desc, category, due_date, completed>0, priority);
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
		String sql = "SELECT list.ID, list.Title, list.Memo, list.Due_date, list.Current_date, list.isCompleted, list.Priority, category.Name"
					+ " FROM list INNER JOIN category ON list.Category_id=category.ID WHERE Name= ?;";
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, keyword);
			ResultSet r = ps.executeQuery();
			while(r.next()) {
				int id = r.getInt("ID");
				String category = r.getString("Name");
				String title = r.getString("Title");
				String desc = r.getString("Memo");
				String due_date = r.getString("Due_date");
				String current_date = r.getString("Current_date");
				int isCompleted = r.getInt("isCompleted");
				int priority = r.getInt("Priority");

				TodoItem item = new TodoItem(title, desc, category, due_date, isCompleted>0, priority);
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
		String sql = "SELECT * FROM category;";
		try {
			Statement s = con.createStatement();
			ResultSet rs = s.executeQuery(sql);

			while(rs.next()){
				String category = rs.getString("Name");
				list.add(category);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public int getCategoryId(String keyword) {
		String sql = "SELECT ID FROM category WHERE Name=?";
		int id=1;

		try {
			PreparedStatement p = con.prepareStatement(sql);
			p.setString(1, keyword);
			ResultSet r = p.executeQuery();

			while(r.next()) {
				id = r.getInt("ID");
			}
			p.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
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

	//@Overload
	public int numberOf(boolean isCompleted) {
		int num=0;
		int condition = (isCompleted) ? 1 : 0;
		String sql = "SELECT count(ID) FROM list WHERE isCompleted=?;";
		try {
			PreparedStatement p = con.prepareStatement(sql);
			p.setInt(1, condition);
			ResultSet r = p.executeQuery();
			r.next();
			num = r.getInt("count(ID)");
			p.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return num;
	}

	public ArrayList<TodoItem> orderBy(String keyword, boolean desc) {
		ArrayList<TodoItem> list = new ArrayList<>();
		String sql = "SELECT * FROM list INNER JOIN category ON list.Category_id = category.ID "
					+ "ORDER BY " + keyword;
		if(desc) sql += " DESC";
		try {
			Statement s = con.createStatement();
			ResultSet r = s.executeQuery(sql);

			while(r.next()) {
				int id = r.getInt("ID");
				String category = r.getString("Name");
				String title = r.getString("Title");
				String memo = r.getString("Memo");
				String due_date = r.getString("Due_date");
				String current_date = r.getString("Current_date");
				int isCompleted = r.getInt("isCompleted");
				int priority = r.getInt("Priority");

				TodoItem item = new TodoItem(title, memo, category, due_date, isCompleted>0, priority);
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

	//@Overload
	public Boolean isDuplicateCate(String category) {
		for (String s : getList_cateOnly()) {
			if (category.equals(s)) return true;
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