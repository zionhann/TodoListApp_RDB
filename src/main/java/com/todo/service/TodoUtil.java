package com.todo.service;

import java.io.*;
import java.util.*;

import com.todo.dao.TodoItem;
import com.todo.dao.TodoList;

public class TodoUtil {

	public static void createItem(TodoList list) {

		String title, desc, category, due_date;
		Scanner sc = new Scanner(System.in);

		System.out.println("\n"
				+ "========== 할 일 추가\n"
				+ "카테고리를 입력하세요.\n");

		category = sc.nextLine();

		System.out.println("\n"
				+ "할 일을 입력하세요.\n");

		title = sc.nextLine();
		if (list.isDuplicate(title) && list.isDuplicateCate(category)) {
			System.out.printf("이미 존재하는 할 일입니다.");
			return;
		}

		System.out.println("\n" +
						"메모를 입력하세요.\n");
		desc = sc.nextLine();

		System.out.println("\n" +
				"마감일을 입력하세요.(YYYYMMDD\n");
		due_date = sc.nextLine();
		if(!list.isInteger(due_date)) {
			System.out.println("숫자가 아닙니다.");
			return;
		}
		else if(due_date.length() != 8) {
			System.out.println("8자리가 아닙니다.");
			return;
		}

		TodoItem t = new TodoItem(title, desc, category, due_date);
		list.addItem(t);
	}

	public static void deleteItem(TodoList l) {

		Scanner sc = new Scanner(System.in);
		boolean isDeleted = false;

		System.out.println("\n"
				+ "========== 할 일 삭제\n"
				+ "삭제할 할 일의 번호를 입력하세요.\n"
				+ "\n");
		int num = sc.nextInt();
		for (TodoItem item : l.getList()) {
			if (num == l.indexOf(item) + 1) {
				l.deleteItem(item);
				System.out.println("삭제되었습니다.");
				isDeleted = true;
				break;
			}
		}
		if(!isDeleted) System.out.println("번호에 해당하는 할 일을 찾을 수 없습니다.");
	}


	public static void updateItem(TodoList l) {

		Scanner sc = new Scanner(System.in);

		System.out.println("\n"
				+ "========== 할 일 수정\n"
				+ "수정할 할 일의 번호를 입력하세요.\n"
				+ "\n");
		int num = sc.nextInt();
		if(!l.isDuplicate(num)) {
			System.out.println("번호에 해당하는 할 일을 찾을 수 없습니다.");
		}
		for(TodoItem item : l.getList()) {
			if(num == l.indexOf(item) + 1) {
				System.out.println(num + "." + item.toString());
				break;
			}
		}

		System.out.println("\n새로운 카테고리를 입력하세요.");
		String new_category = sc.nextLine().trim();

		System.out.println("\n새로운 할 일을 입력하세요.");
		String new_title = sc.nextLine().trim();
		if (l.isDuplicate(new_title) && l.isDuplicateCate(new_category)) {
			System.out.println("해당 카테고리에 이미 존재하는 할 일입니다.");
			return;
		}

		System.out.println("새로운 메모를 입력하세요.");
		String new_description = sc.nextLine().trim();

		System.out.println("새로운 마감일을 입력하세요(YYYYMMDD).");
		String new_due_date = sc.nextLine().trim();
		if(!l.isInteger(new_due_date)) {
			System.out.println("숫자가 아닙니다.");
			return;
		}
		else if(new_due_date.length() != 8) {
			System.out.println("8자리가 아닙니다.");
			return;
		}

		for (TodoItem item : l.getList()) {
			if (num == l.indexOf(item) + 1) {
				l.deleteItem(item);
				TodoItem t = new TodoItem(new_title, new_description, new_category, new_due_date);
				l.addItem(t);
				System.out.println("할 일이 수정되었습니다.");
			}
		}

	}

	public static void listAll(TodoList l) {
		for (TodoItem item : l.getList()) {
			System.out.println("할 일: " + item.getTitle() + "  메모:  " + item.getDesc() + " - " + item.getCurrent_date());
		}
	}

	public static void saveList(TodoList l, String filename) {
		Writer w = null;
		try {
			w = new FileWriter(filename);
			for (TodoItem item : l.getList()) {
				w.write(item.toSaveString());
			}
			w.close();
			System.out.println("데이터가 저장되었습니다.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	public static void loadList(TodoList l, String filename) {
		BufferedReader br = null;
		try {
			br = new BufferedReader((new FileReader(filename)));
			String line;
			int count = 0;
			while ((line = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, "##");
				String category = st.nextToken();
				String title = st.nextToken();
				String description = st.nextToken();
				String due_date = st.nextToken();
				String current_date = st.nextToken();
				TodoItem item = new TodoItem(title, description, category, due_date);
				item.setCurrent_date(current_date);
				l.addItem(item);
				count++;
			}
			br.close();
			System.out.println(count+"개의 할 일을 읽었습니다.");
		}
		catch (FileNotFoundException e) {
			System.out.println(filename+" 파일이 없습니다.");
			//e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}