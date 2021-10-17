package com.todo.menu;
public class Menu {

    public static void displaymenu()
    {
        System.out.println();
        System.out.println("01. 할 일 추가 ( add )");
        System.out.println("02. 할 일 삭제 ( del )");
        System.out.println("03. 할 일 수정  ( edit )");
        System.out.println("04. 할 일 완료  ( comp <index> )");
        System.out.println("05. 할 일 고정 ( pin <index> )");
        System.out.println("06. 할 일 목록 표시 ( ls )");
        System.out.println("07. 완료된 할 일 목록 표시 ( ls_comp )");
        System.out.println("08. 추가된 카테고리 목록 표시 ( ls_cate )");
        System.out.println("09. 오름차순 정렬 ( ls_name_asc )");
        System.out.println("10. 내림차순 정렬 ( ls_name_desc )");
        System.out.println("11. 오래된 생성 날짜순 정렬 ( ls_date )");
        System.out.println("12. 최근 생성 날짜순 정렬 ( ls_date_desc )");
        System.out.println("13. 키워드 검색 ( find <keyword> )");
        System.out.println("14. 카테고리 키워드 검색 ( find_cate <keyword> )");
        System.out.println("15. 종료 ( exit )");
    }

    public static void prompt() {
        System.out.print("명령어 입력 > ");
    }
}
