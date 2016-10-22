package ru.domain.java3.lesson2;

import ru.domain.java3.lesson2.DAO.Facade;
import ru.domain.java3.lesson2.entity.Author;
import ru.domain.java3.lesson2.entity.Book;
import ru.domain.java3.lesson2.exceptions.EntityDeleteError;
import ru.domain.java3.lesson2.exceptions.EntityInsertError;
import ru.domain.java3.lesson2.exceptions.EntityUpdateError;
import ru.domain.java3.lesson2.exceptions.MenuItemOutOfRange;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;


public class DBEditor {
    private boolean isExit = false;

    public void menuInit() {
        while (!isExit) {

            printMainMenu();
            String action;
            int menuItem;
            Scanner scanner = new Scanner(System.in);
            try {
                action = scanner.next();
                if (checkExitAction(action)) break;
                //else throw new InputMismatchException();
                try {
                    menuItem = Integer.parseInt(action);
                } catch (NumberFormatException ex) {
                    throw new InputMismatchException();
                }
                if (menuItem < 1 || menuItem > 9 || menuItem == 0) throw new MenuItemOutOfRange();
                switch (menuItem) {
                    case 1: {
                        addAuthorMenu();
                        break;
                    }
                    case 2: {
                        editAuthorMenu();
                        break;
                    }
                    case 3: {
                        deleteAuthorMenu();
                        break;
                    }
                    case 4: {
                        printAllAuthors();
                        break;
                    }
                    case 5: {
                        addBookMenu();
                        break;
                    }
                    case 6: {
                        editBookMenu();
                        break;
                    }
                    case 7: {
                        deleteBookMenu();
                        break;
                    }
                    case 8: {
                        printAllBooks();
                        break;
                    }
                    case 9: {
                        viewLibrary();
                    }

                }


            } catch (InputMismatchException ex) {
                System.out.println("Введено неверное значение");
            } catch (MenuItemOutOfRange ex) {
                System.out.println("Выбран не верный пункт меню. Выберите пункт меню в допустимых пределах");
            }
        }
    }

    private void viewLibrary() {
        ArrayList<Book> books = (ArrayList<Book>)Facade.getBookDAO().selectAll();
        ArrayList<Author> authors = (ArrayList<Author>)Facade.getAuthorDAO().selectAll();
        for (int i = 0; i < books.size(); i++) {
            Author bookAuthor = getAuthorById(authors,books.get(i).getAuthorId());
            String authorInfo;
            if (bookAuthor.getId()>0){
                authorInfo = " Автор книги: "+bookAuthor.getLastName()+" "+bookAuthor.getFirstName()+" "+bookAuthor.getPatronymic();
            }
            else authorInfo = " Автор книги неизвестен";
            System.out.println("Книга "+books.get(i).getName()+"   ID: "+books.get(i).getId() + " "+authorInfo);
        }

    }
    private Author getAuthorById(ArrayList<Author> authors,int id){
        Author author=null;
        for (int i = 0; i < authors.size(); i++) {
            if(authors.get(i).getId()==id){
                author=authors.get(i);
            }
        }
        if (author==null){
            author=new Author(-1,"","","");
        }
        return author;
    }
    private void deleteBookMenu() {
        System.out.println("Выберите кингу для удаления. Введите 0 для возврата");
        printAllBooks();
        while (!isExit) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("ID: ");
            try {
                int id = scanner.nextInt();
                if (id == 0) break;
                if (getConfirm(scanner,"Вы уверены,что хотите удалить книгу?")) {
                    Facade.getBookDAO().delete(id);
                    break;
                }
                else break;
            } catch (InputMismatchException ex) {
                System.out.println("Введено не верное значение.");
            } catch (EntityDeleteError e) {
                System.out.println("Ошибка удаления. Подробности в логе");
                AppLogger.writeLog("Book delete Error. Probably wrong id ? ", AppLogger.INFO);
                break;
            }
        }
    }


    private void editBookMenu() {
        printAllBooks();
        if (!Facade.getBookDAO().selectAll().isEmpty()) {
            editBook();
        }
    }

    private void editBook() {
        Scanner scanner = new Scanner(System.in);
        Book editableBook;
        int id = -1;
        String name;
        int bookAuthorId;

        System.out.println("Выберите книгу для редактирования. Введите 0 для возврата");
        while (!isExit) {

            try {

                id = getIntFromConsole(scanner, "Введите ID книги: ");
                if (id == 0) break;
                name = getStringFromConsole(scanner, "Введите название книги: ");
                bookAuthorId = getIntFromConsole(scanner, "Введите ID автора книги: ");

                if(getConfirm(scanner, "Вы уверены,что хотите обновить книгу?  ")) {
                    editableBook = new Book(id, name, bookAuthorId);
                    Facade.getBookDAO().update(editableBook);
                    System.out.println("Изменения успешно сохранены");
                    break;
                }else {
                    System.out.println("Изменения отменены");
                    break;
                }

            } catch (InputMismatchException ex) {
                System.out.println("Введено не верное значение");
                id = -1;
            } catch (EntityUpdateError ex) {
                System.out.println("Ошибка обновления автора. Подробности в логе");
                AppLogger.writeLog("Book update Error. Probably wrong id ? ", AppLogger.INFO);
                break;
            }
        }

    }

    private void addBookMenu() {
        Scanner scanner = new Scanner(System.in);
        String name = null;
        int authorId = -1;
        name = getStringFromConsole(scanner, "Введите название книги: ");
        authorId = getIntFromConsole(scanner, "Введите ID автора книги: ");
        if(getConfirm(scanner,"Вы уверены,что хотите добавить книгу ? ")){
        if (name != null && authorId > 0) {
            Book addableBook = new Book(0, name, authorId);
            if (Facade.getBookDAO().select(addableBook).isEmpty()) {
                try {
                    Facade.getBookDAO().insert(addableBook);
                    int newBookID = ((ArrayList<Book>) Facade.getBookDAO().select(addableBook)).get(0).getId();
                    System.out.println(" Книга добавлена. ID в базе = " + newBookID);
                } catch (EntityInsertError ex) {
                    System.out.println("Ошибка добавления в базу данных. Дополнительная информация в логе.");
                    AppLogger.writeLog("Book insert Error. Probably wrong id ? ", AppLogger.INFO);
                }
            } else {
                System.out.println("Данная книга уже есть в базе");
            }
        }}

    }


    private void deleteAuthorMenu() {
        System.out.println("Выберите автора для удаления. Введите 0 для возврата");
        printAllAuthors();
        while (!isExit) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("ID: ");
            try {
                int id = scanner.nextInt();
                if (id == 0) break;
                if (getConfirm(scanner,"Вы уверены , что хотите удалить автора ? ")) {
                    Facade.getAuthorDAO().delete(id);
                    break;
                }
                else break;


            } catch (InputMismatchException ex) {
                System.out.println("Введено не верное значение.");
            } catch (EntityDeleteError e) {
                System.out.println("Ошибка удаления. Подробности в логе");
                AppLogger.writeLog("Author delete Error. Probably wrong id ? ", AppLogger.INFO);
                break;
            }
        }
    }

    private void editAuthorMenu() {

        printAllAuthors();
        if (!Facade.getAuthorDAO().selectAll().isEmpty()) {
            editAuthor();
        }
    }

    private void editAuthor() {
        Scanner scanner = new Scanner(System.in);
        Author editableAuthor;
        int id = -1;
        String firstName = "";
        String secondName = "";
        String patronymic = "";

        System.out.println("Выберите автора для редактирования. Введите 0 для возврата");
        while (!isExit) {

            try {

                id = getIntFromConsole(scanner, "Введите ID автора: ");
                if (id == 0) break;
                firstName = getStringFromConsole(scanner, "Введите имя автора: ");
                secondName = getStringFromConsole(scanner, "Введите фамилию автора: ");
                patronymic = getStringFromConsole(scanner, "Введите отчество автора: ");
                if(getConfirm(scanner, "Вы уверены,что хотите обновить автора? ")) {
                    editableAuthor = new Author(id, firstName, secondName, patronymic);
                    Facade.getAuthorDAO().update(editableAuthor);
                    System.out.println("Изменения успешно сохранены");
                    break;
                }else {
                    System.out.println("Изменения отменены");
                    break;
                }

            } catch (InputMismatchException ex) {
                System.out.println("Введено не верное значение");
                id = -1;
            } catch (EntityUpdateError ex) {
                System.out.println("Ошибка обновления автора. Подробности в логе");
                AppLogger.writeLog("Author update Error. Probably wrong id ? ", AppLogger.INFO);
                break;
            }
        }
    }

    private void printAllAuthors() {
        ArrayList<Author> authors = (ArrayList<Author>) Facade.getAuthorDAO().selectAll();
        if (authors.isEmpty()) {
            System.out.println("Авторов не найдено.");
        } else {
            for (int i = 0; i < authors.size(); i++) {
                System.out.println(authors.get(i));
            }
        }
    }
    private void printAllBooks() {
        ArrayList<Book> books = (ArrayList<Book>) Facade.getBookDAO().selectAll();
        if (books.isEmpty()) {
            System.out.println("Книг не найдено.");
        } else {
            for (int i = 0; i < books.size(); i++) {
                System.out.println(books.get(i));
            }
        }
    }

    private void addAuthorMenu() {
        Scanner scanner = new Scanner(System.in);
        String name = null, lastName = null, patronymic = null;
        name = getStringFromConsole(scanner, "Введите имя автора: ");
        lastName = getStringFromConsole(scanner, "Введите фамилию автора: ");
        patronymic = getStringFromConsole(scanner, "Введите отчество автора: ");
        if(getConfirm(scanner,"Вы уверены,что хотите добавить автора?")){
        if (name != null && lastName != null && patronymic != null) {
            Author addableAuthor = new Author(0, name, lastName, patronymic);
            if (Facade.getAuthorDAO().select(addableAuthor).isEmpty()) {
                try {
                    Facade.getAuthorDAO().insert(addableAuthor);
                    int newAuthorID = ((ArrayList<Author>) Facade.getAuthorDAO().select(addableAuthor)).get(0).getId();
                    System.out.println(" Автор добавлен. ID в базе = " + newAuthorID);
                } catch (EntityInsertError ex) {
                    System.out.println("Ошибка добавления в базу данных. Дополнительная информация в логе.");
                    AppLogger.writeLog("Author insert Error. Probably wrong id ? ", AppLogger.INFO);
                }
            } else {
                System.out.println("Данный автор уже есть в базе");
            }
        }}


    }


    private String getStringFromConsole(Scanner source, String dialogMessage) {
        String buffer="";
        while (!isExit) {
            System.out.println(dialogMessage);
            String inputString = source.next();
            if (inputString.trim().equalsIgnoreCase("exit")) {
                isExit = true;
                break;
            }
            if (!inputString.trim().isEmpty()) {
                buffer = inputString.trim();
                break;
            }
            System.out.println("Необходимо ввести верную фамилию!");
        }
        return buffer;
    }

    private int getIntFromConsole(Scanner source, String dialog) {
        int buffer=-1;
        while (!isExit) {
            System.out.println(dialog);
            String inputString = source.next();
            if (inputString.trim().equalsIgnoreCase("exit")) {
                isExit = true;
                break;
            }
            if (!inputString.trim().isEmpty()) {
                try {
                    buffer = Integer.parseInt(inputString.trim());
                    break;
                } catch (NumberFormatException e) {
                }

            }
            System.out.println("Необходимо ввести верный ID!");
        }
        return buffer;
    }
   private boolean getConfirm(Scanner scanner,String dialog){
        String consoleMessage="";
        while (!isExit){
            consoleMessage=getStringFromConsole(scanner, dialog+"(Y/N)").trim();
            if (checkExitAction(consoleMessage)) break;
            if (consoleMessage.equalsIgnoreCase("y")||consoleMessage.equalsIgnoreCase("Y")) return true;
            if(consoleMessage.equalsIgnoreCase("n")||consoleMessage.equalsIgnoreCase("N")) return false;

            System.out.println("Выберите верный вариант!");
        }
        return false;
    }

    private void printMainMenu() {
        System.out.println("1) Добавить автора");
        System.out.println("2) Обновить автора");
        System.out.println("3) Удалить автора");
        System.out.println("4) Посмотреть всех авторов");
        System.out.println("5) Добавить книгу");
        System.out.println("6) Обновить книгу");
        System.out.println("7) Удалить книгу");
        System.out.println("8) Посмотреть все книги");
        System.out.println("9) Посмотреть все книги и соответствующих авторов");

    }

   private boolean checkExitAction(String inputedMessage) {
        if (inputedMessage.trim().equalsIgnoreCase("exit")) {
            System.out.println("Вы ввели команду выхода. Приложение будет закрыто");
            isExit = true;
            return true;
        }
        return false;
    }



}
