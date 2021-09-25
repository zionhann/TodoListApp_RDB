package com.todo.service;

import java.io.*;
import java.util.*;

import com.todo.dao.TodoItem;
import com.todo.dao.TodoList;

public class TodoUtil {

	public static void createItem(TodoList list) {

		String title, desc;
		Scanner sc = new Scanner(System.in);

		System.out.println("\n"
				+ "========== 할 일 추가\n"
				+ "할 일 입력하세요.\n");

		title = sc.nextLine();
		if (list.isDuplicate(title)) {
			System.out.printf("이미 존재하는 할 일입니다.");
			return;
		}

		System.out.println("메모 입력");
		desc = sc.nextLine();

		TodoItem t = new TodoItem(title, desc);
		list.addItem(t);
	}

	public static void deleteItem(TodoList l) {

		Scanner sc = new Scanner(System.in);

		System.out.println("\n"
				+ "========== 할 일 삭제\n"
				+ "삭제할 할 일을 입력하세요.\n"
				+ "\n");
		String title = sc.next();
		for (TodoItem item : l.getList()) {
			if (title.equals(item.getTitle())) {
				l.deleteItem(item);
				break;
			}
		}
	}


	public static void updateItem(TodoList l) {

		Scanner sc = new Scanner(System.in);

		System.out.println("\n"
				+ "========== 할 일 수정\n"
				+ "수정할 할 일을 입력하세요.\n"
				+ "\n");
		String title = sc.nextLine().trim();
		if (!l.isDuplicate(title)) {
			System.out.println("해당 할 일이 존재하지 않습니다.");
			return;
		}

		System.out.println("새로운 할 일을 입력하세요.");
		String new_title = sc.nextLine().trim();
		if (l.isDuplicate(new_title)) {
			System.out.println("이미 존재하는 할 일입니다.");
			return;
		}

		System.out.println("새로운 메모를 입력하세요.");
		String new_description = sc.nextLine().trim();
		for (TodoItem item : l.getList()) {
			if (item.getTitle().equals(title)) {
				l.deleteItem(item);
				TodoItem t = new TodoItem(new_title, new_description);
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
				String title = st.nextToken();
				String description = st.nextToken();
				String current_date = st.nextToken();
				TodoItem item = new TodoItem(title, description);
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