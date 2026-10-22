import java.util.ArrayList;
import java.util.List;

public class BankAccount {
    private static final int MAX_FAILED_ATTEMPTS = 3;
    private static final List<BankAccount> ALL = new ArrayList<>();

    private final String number;
    private final String owner;
    private Balance balance;
    private boolean frozen;
    private int failedAttempts;

    public BankAccount(String number, String owner) {
        if (number == null || number.isBlank()) {
            throw new IllegalArgumentException("Номер счёта пустой");
        }
        if (owner == null || owner.isBlank()) {
            throw new IllegalArgumentException("Имя владельца пустое");
        }
        for (BankAccount account : ALL) {
            if (account.number.equals(number)) {
                throw new IllegalArgumentException("Номер уже существует: " + number);
            }
            if (account.owner.equals(owner)) {
                throw new IllegalArgumentException("Владелец уже существует: " + owner);
            }
        }
        this.number = number;
        this.owner = owner;
        this.balance = new Balance(0, Currency.RUB);
        this.frozen = false;
        this.failedAttempts = 0;
        ALL.add(this);
    }

    public String getNumber() {
        return number;
    }

    public String getOwner() {
        return owner;
    }

    public Balance getBalance() {
        return balance;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public void freeze() {
        frozen = true;
    }

    public void unfreeze() {
        frozen = false;
        failedAttempts = 0;
    }

    public void deposit(long amount) {
        checkNotFrozen();
        balance = balance.add(amount);
    }

    public void withdraw(long amount) {
        checkNotFrozen();
        if (amount > balance.getAmount()) {
            registerFailedAttempt();
            throw new IllegalArgumentException("Недостаточно средств");
        }
        balance = balance.subtract(amount);
        failedAttempts = 0;
    }

    public void transfer(BankAccount to, long amount) {
        if (to == null) {
            throw new IllegalArgumentException("Счёт-получатель не задан");
        }
        if (to == this) {
            throw new IllegalArgumentException("Нельзя переводить деньги на тот же счёт");
        }
        to.checkNotFrozen();
        withdraw(amount);
        to.deposit(amount);
    }

    public void transferByNumber(String toNumber, long amount) {
        transfer(findByNumber(toNumber), amount);
    }

    public void transferByOwner(String toOwner, long amount) {
        transfer(findByOwner(toOwner), amount);
    }

    public static BankAccount findByNumber(String number) {
        if (number == null) {
            throw new IllegalArgumentException("Номер не задан");
        }
        for (BankAccount account : ALL) {
            if (account.number.equals(number)) {
                return account;
            }
        }
        throw new IllegalArgumentException("Счёт не найден: " + number);
    }

    public static BankAccount findByOwner(String owner) {
        if (owner == null) {
            throw new IllegalArgumentException("Владелец не задан");
        }
        for (BankAccount account : ALL) {
            if (account.owner.equals(owner)) {
                return account;
            }
        }
        throw new IllegalArgumentException("Владелец не найден: " + owner);
    }

    private void registerFailedAttempt() {
        failedAttempts++;
        if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
            freeze();
        }
    }

    private void checkNotFrozen() {
        if (frozen) {
            throw new IllegalStateException("Счёт " + number + " заморожен");
        }
    }

    @Override
    public String toString() {
        return "#" + number + " " + owner + ": " + balance + (frozen ? " (заморожен)" : "");
    }
}