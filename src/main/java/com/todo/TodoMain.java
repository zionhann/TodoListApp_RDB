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
		boolean isList = false;
		boolean quit = false;
		//TodoUtil.loadList(l, "todolist.txt");
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

			case "comp": {
				String multiIndex = sc.nextLine().trim();
				TodoUtil.completeItem(l, multiIndex);
				break;
			}
				
			case "ls":
				TodoUtil.listAll(l);
				break;

			case "ls_comp":
				TodoUtil.listAll(l, true);
				break;

			case "ls_cate":
				TodoUtil.listCate(l);
				break;

			case "ls_name_asc":
				TodoUtil.listAll(l, "Title", false);
				isList = true;
				break;

			case "ls_name_desc":
				TodoUtil.listAll(l, "Title", true);
				isList = true;
				break;
				
			case "ls_date":
				TodoUtil.listAll(l, "Due_date", false);
				isList = true;
				break;

			case "ls_date_desc":
				TodoUtil.listAll(l, "Due_date", true);
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
				//TodoUtil.saveList(l,"todolist.txt");
				l.setConClose();
				break;

			case "help":
				Menu.displaymenu();
				break;
			default:
				System.out.println("잘못된 명령어입니다. (명렁어 보기 -help)");
				break;
			}

		} while (!quit);
	}
}