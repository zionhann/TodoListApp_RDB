package com.todo;

import java.util.Scanner;

import com.todo.dao.TodoList;
import com.todo.menu.Menu;
import com.todo.service.TodoUtil;

public class TodoMain {
	
	public static void start() {
	
		Scanner sc = new Scanner(System.in);
		TodoList l = new TodoList();
		//l.importData("todolist.txt");
		boolean quit = false;
		//TodoUtil.loadList(l, "todolist.txt");
		do {
			Menu.prompt();
			String choice = sc.next();
			switch (choice) {
				case "add" -> TodoUtil.createItem(l);
				case "del" -> TodoUtil.deleteItem(l);
				case "edit" -> TodoUtil.updateItem(l);
				case "comp" -> {
					String multiIndex = sc.nextLine().trim();
					TodoUtil.completeItem(l, multiIndex);
				}
				case "pin" -> {
					int input = sc.nextInt();
					TodoUtil.pinItem(l, input);
				}
				case "ls" -> TodoUtil.listAll(l);
				case "ls_comp" -> TodoUtil.listAll(l, true, "isCompleted");
				case "ls_cate" -> TodoUtil.listCate(l);
				case "ls_name_asc" -> TodoUtil.listAll(l, "Title", false);
				case "ls_name_desc" -> TodoUtil.listAll(l, "Title", true);
				case "ls_date" -> TodoUtil.listAll(l, "Due_date", false);
				case "ls_date_desc" -> TodoUtil.listAll(l, "Due_date", true);
				case "find" -> {
					String keyword = sc.next().trim();
					TodoUtil.findItem(l, keyword);
				}
				case "find_cate" -> {
					String keyword = sc.next().trim();
					TodoUtil.findItem_cate(l, keyword);
				}
				case "exit" -> {
					quit = true;
					//TodoUtil.saveList(l,"todolist.txt");
					l.setConClose();
				}
				case "help" -> Menu.displaymenu();
				default -> System.out.println("잘못된 명령어입니다. (명렁어 보기 -help)");
			}
		} while (!quit);
	}
}