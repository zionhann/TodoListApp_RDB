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
				+ "카테고리를 입력하세요.");

		category = sc.nextLine();

		System.out.println("\n"
				+ "할 일을 입력하세요.");

		title = sc.nextLine();
		if (list.isDuplicate(title, list.getList()) && list.isDuplicateCate(category, list.getList())) {
			System.out.println("이미 존재하는 할 일입니다.\n");
			return;
		}

		System.out.println("\n" +
						"메모를 입력하세요.");
		desc = sc.nextLine();

		System.out.println("\n" +
				"마감일을 입력하세요.(YYYYMMDD)");
		due_date = sc.nextLine();
		if(!list.isInteger(due_date)) {
			System.out.println("숫자가 아닙니다.\n");
			return;
		}
		else if(due_date.length() != 8) {
			System.out.println("8자리가 아닙니다.\n");
			return;
		}

		TodoItem t = new TodoItem(title, desc, category, due_date, false);
		if(list.addItem(t)>0)
			System.out.println("추가되었습니다.");
	}

	public static void deleteItem(TodoList l) {

		Scanner sc = new Scanner(System.in);
		boolean isDeleted = false;

		System.out.println("\n"
				+ "========== 할 일 삭제\n"
				+ "삭제할 할 일의 번호를 입력하세요.");
		int num = sc.nextInt();
		String lineFeed = sc.nextLine();
		for (TodoItem item : l.getList()) {
			if (num == item.getID()) {
				System.out.println(item);
				System.out.println("위 항목을 삭제하시겠습니까? (y/n)\n");
				String input = sc.next().trim();
				if(Objects.equals(input, "y")) {
					if(l.deleteItem(item) > 0) {
						System.out.println("삭제되었습니다.\n");
						isDeleted = true;
					}
				}
				else return;
			break;
			}
		}
		if(!isDeleted) System.out.println("번호에 해당하는 할 일을 찾을 수 없습니다.\n");
	}


	public static void updateItem(TodoList l) {

		Scanner sc = new Scanner(System.in);

		System.out.println("\n"
				+ "========== 할 일 수정\n"
				+ "수정할 할 일의 번호를 입력하세요.");
		int num = sc.nextInt();
		String lineFeed = sc.nextLine();
		if(!l.isDuplicate(num, l.getList())) {
			System.out.println("번호에 해당하는 할 일을 찾을 수 없습니다.\n");
			return;
		}
		for(TodoItem item : l.getList()) {
			if(num == item.getID()) {
				System.out.println(item);
				break;
			}
		}

		System.out.println("\n새로운 카테고리를 입력하세요.");
		String new_category = sc.nextLine().trim();

		System.out.println("\n새로운 할 일을 입력하세요.");
		String new_title = sc.nextLine().trim();
		if (l.isDuplicate(new_title, l.getList()) && l.isDuplicateCate(new_category, l.getList())) {
			System.out.println("해당 카테고리에 이미 존재하는 할 일입니다.\n");
			return;
		}

		System.out.println("\n새로운 메모를 입력하세요.");
		String new_description = sc.nextLine().trim();

		System.out.println("\n새로운 마감일을 입력하세요(YYYYMMDD).");
		String new_due_date = sc.nextLine().trim();
		if(!l.isInteger(new_due_date)) {
			System.out.println("숫자가 아닙니다.\n");
			return;
		}
		else if(new_due_date.length() != 8) {
			System.out.println("8자리가 아닙니다.\n");
			return;
		}

		TodoItem t = new TodoItem(new_title, new_description, new_category, new_due_date, false);
		t.setID(num);
		if(l.editItem(t) > 0)
			System.out.println("할 일이 수정되었습니다.\n");
	}

	public static void completeItem(TodoList l, int index) {
		boolean isFound = false;

		for(TodoItem t : l.getList()) {
			if(t.getID() == index) {
				l.checkItem(index, t.isCompleted());
				if(t.isCompleted() > 0)
					System.out.println("취소 표시되었습니다.\n");
				else System.out.println("완료 표시되었습니다.\n");
				isFound = true;
				break;
			}
		}
		if(!isFound) System.out.println("번호에 해당하는 할 일을 찾을 수 없습니다.\n");
	}

	public static void listAll(TodoList l) {
		System.out.println(l.numberOf() + " items");
		for (TodoItem item : l.getList()) {
			System.out.println(item.toString());
		}
	}

	//@overload
	public static void listAll(TodoList l, String keyword, boolean desc) {
		System.out.println(l.numberOf() + " items");
		for (TodoItem item : l.orderBy(keyword, desc)) {
			System.out.println(item.toString());
		}
	}

	//@overload
	public static void listAll(TodoList l, boolean isCompleted) {
		System.out.println(l.numberOf(isCompleted) + " items");
		for (TodoItem item : l.getList(isCompleted)) {
			System.out.println(item.toString());
		}
	}

	public static void listCate(TodoList l) {
		ArrayList<String> cateList = l.getList_cateOnly();
		String e = String.join(" / ", cateList);
		System.out.println(e);
		System.out.println("총 " +cateList.size() + "개의 카테고리가 등록되어 있습니다.");
	}

//	public static void saveList(TodoList l, String filename) {
//		Writer w = null;
//		try {
//			w = new FileWriter(filename);
//			for (TodoItem item : l.getList()) {
//				w.write(item.toSaveString());
//			}
//			w.close();
//			System.out.println("데이터가 저장되었습니다.\n");
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}

//	public static void loadList(TodoList l, String filename) {
//		BufferedReader br = null;
//		try {
//			br = new BufferedReader((new FileReader(filename)));
//			String line;
//			int count = 0;
//			while ((line = br.readLine()) != null) {
//				StringTokenizer st = new StringTokenizer(line, "##");
//				String category = st.nextToken();
//				String title = st.nextToken();
//				String description = st.nextToken();
//				String due_date = st.nextToken();
//				String current_date = st.nextToken();
//				TodoItem item = new TodoItem(title, description, category, due_date);
//				item.setCurrent_date(current_date);
//				item.setDue_date(due_date);
//				l.addItem(item);
//				count++;
//			}
//			br.close();
//			System.out.println(count+"개의 할 일을 읽었습니다.\n");
//		}
//		catch (FileNotFoundException e) {
//			System.out.println(filename+" 파일이 없습니다.\n");
//			//e.printStackTrace();
//		}
//		catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	public static void findItem(TodoList l, String keyword) {
		boolean isFound = false;

		for(TodoItem item : l.getList(keyword)) {
				System.out.println(item);
				isFound = true;
		}
		if(!isFound) System.out.println("키워드에 해당하는 할 일이나 메모를 찾을 수 없습니다.");
	}

	public static void findItem_cate(TodoList l, String keyword) {
		boolean isFound = false;

		for(TodoItem item : l.getList_cate(keyword)) {
				System.out.println(item);
				isFound = true;
		}
		if(!isFound) System.out.println("키워드에 해당하는 카테고리를 찾을 수 없습니다.");
	}
}