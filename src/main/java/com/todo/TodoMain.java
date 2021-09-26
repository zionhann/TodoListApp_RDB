package com.todo;

import java.util.Scanner;

import com.todo.dao.TodoList;
import com.todo.menu.Menu;
import com.todo.service.TodoUtil;

public class TodoMain {
	
	public static void start() {
	
		Scanner sc = new Scanner(System.in);
		TodoList l = new TodoList();
		boolean isList = false;
		boolean quit = false;
		TodoUtil.loadList(l, "todolist.txt");
		do {
			Menu.prompt();
			isList = false;
			String choice = sc.next();
			switch (choice) {

			case "add":
				TodoUtil.createItem(l);
				break;
			
			case "del":
				TodoUtil.deleteItem(l);
				break;
				
			case "edit":
				TodoUtil.updateItem(l);
				break;
				
			case "ls":
				TodoUtil.listAll(l);
				break;

			case "ls_name_asc":
				l.sortByName();
				isList = true;
				break;

			case "ls_name_desc":
				l.sortByName();
				l.reverseList();
				isList = true;
				break;
				
			case "ls_date":
				l.sortByDate();
				isList = true;
				break;

			case "find": {
				String keyword = sc.next().trim();
				TodoUtil.findItem(l, keyword);
				break;
			}

			case "find_cate": {
				String keyword = sc.next().trim();
				TodoUtil.findItem_cate(l, keyword);
				break;
			}

			case "exit":
				quit = true;
				TodoUtil.saveList(l,"todolist.txt");
				break;

			case "help":
				Menu.displaymenu();
				break;
			default:
				System.out.println("잘못된 명령어입니다. (명렁어 보기 -help)");
				break;
			}
			
			if(isList) l.listAll();
		} while (!quit);
	}
}
