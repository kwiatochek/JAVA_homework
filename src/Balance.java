public final class Balance {
    private final long amount;
    private final Currency currency;

    public Balance(long amount, Currency currency) {
        if (currency == null) {
            throw new IllegalArgumentException("Валюта не задана");
        }
        if (amount < 0) {
            throw new IllegalArgumentException("Сумма отрицательная");
        }
        this.amount = amount;
        this.currency = currency;
    }

    public long getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Balance add(long value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Сумма должна быть положительной");
        }
        return new Balance(Math.addExact(amount, value), currency);
    }

    public Balance subtract(long value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Сумма должна быть положительной");
        }
        if (value > amount) {
            throw new IllegalArgumentException("Недостаточно средств");
        }
        return new Balance(amount - value, currency);
    }

    public Balance convertTo(Currency target) {
        if (target == null) {
            throw new IllegalArgumentException("Целевая валюта не задана");
        }
        long converted = Math.multiplyExact(amount, currency.getPriceInRublesKopecks())
                / target.getPriceInRublesKopecks();
        return new Balance(converted, target);
    }

    @Override
    public String toString() {
        return String.format("%d.%02d %s", amount / 100, amount % 100, currency.getSymbol());
    }
}