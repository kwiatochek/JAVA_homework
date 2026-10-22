public class Main {
    public static void main(String[] args) {
        BankAccount a = new BankAccount("1000", "Иван");
        BankAccount b = new BankAccount("1001", "Пётр");
        BankAccount c = new BankAccount("1002", "Анна");

        a.deposit(100_000);
        System.out.println(a);
        System.out.println(b);
        System.out.println(c);

        a.transferByOwner("Пётр", 30_000);
        System.out.println(a);
        System.out.println(b);

        b.transferByNumber("1002", 10_000);
        System.out.println(b);
        System.out.println(c);

        System.out.println(a.getBalance().convertTo(Currency.USD));
        System.out.println(a.getBalance().convertTo(Currency.EUR));

        try {
            a.deposit(-5);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

        try {
            a.transferByOwner("Никто", 100);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

        try {
            a.transferByNumber("9999", 100);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

        try {
            b.transferByOwner("Иван", 1_000_000);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

        try {
            new BankAccount("2000", "Иван");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

        try {
            new BankAccount("1000", "Олег");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

        c.freeze();
        try {
            a.transferByOwner("Анна", 100);
        } catch (IllegalStateException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
        System.out.println(c);

        try {
            a.transferByNumber("1000", 100);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

        BankAccount d = new BankAccount("1003", "Олег");
        d.deposit(1_000);
        for (int i = 1; i <= 4; i++) {
            try {
                d.withdraw(5_000);
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Попытка " + i + ": " + e.getMessage());
            }
        }
        System.out.println(d);

        d.unfreeze();
        d.withdraw(500);
        System.out.println(d);
    }
}