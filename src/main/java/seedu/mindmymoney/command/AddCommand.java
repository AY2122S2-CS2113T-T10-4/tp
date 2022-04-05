package seedu.mindmymoney.command;

import seedu.mindmymoney.MindMyMoneyException;
import seedu.mindmymoney.data.CreditCardList;
import seedu.mindmymoney.data.ExpenditureList;
import seedu.mindmymoney.data.IncomeList;
import seedu.mindmymoney.userfinancial.Expenditure;
import seedu.mindmymoney.userfinancial.CreditCard;
import seedu.mindmymoney.userfinancial.Income;
import seedu.mindmymoney.userfinancial.User;

import static seedu.mindmymoney.constants.Flags.FLAG_END_VALUE;
import static seedu.mindmymoney.constants.Flags.FLAG_OF_AMOUNT;
import static seedu.mindmymoney.constants.Flags.FLAG_OF_CARD_BALANCE;
import static seedu.mindmymoney.constants.Flags.FLAG_OF_CARD_LIMIT;
import static seedu.mindmymoney.constants.Flags.FLAG_OF_CARD_NAME;
import static seedu.mindmymoney.constants.Flags.FLAG_OF_CASHBACK;
import static seedu.mindmymoney.constants.Flags.FLAG_OF_CATEGORY;
import static seedu.mindmymoney.constants.Flags.FLAG_OF_CREDIT_CARD;
import static seedu.mindmymoney.constants.Flags.FLAG_OF_DESCRIPTION;
import static seedu.mindmymoney.constants.Flags.FLAG_OF_INCOME;
import static seedu.mindmymoney.constants.Flags.FLAG_OF_PAYMENT_METHOD;
import static seedu.mindmymoney.constants.Flags.FLAG_OF_TIME;

import static seedu.mindmymoney.helper.AddCommandInputTests.isValidInput;
import static seedu.mindmymoney.helper.AddCommandInputTests.testCashbackAmount;
import static seedu.mindmymoney.helper.AddCommandInputTests.testCreditCardBalance;
import static seedu.mindmymoney.helper.AddCommandInputTests.testCreditCardLimit;
import static seedu.mindmymoney.helper.AddCommandInputTests.testDescription;
import static seedu.mindmymoney.helper.AddCommandInputTests.testExpenditureAmount;
import static seedu.mindmymoney.helper.AddCommandInputTests.testExpenditureCategory;
import static seedu.mindmymoney.helper.AddCommandInputTests.testIncomeAmount;
import static seedu.mindmymoney.helper.AddCommandInputTests.testIncomeCategory;
import static seedu.mindmymoney.helper.AddCommandInputTests.testPaymentMethod;
import static seedu.mindmymoney.helper.GeneralFunctions.capitalise;
import static seedu.mindmymoney.helper.GeneralFunctions.parseInputWithCommandFlag;
import static seedu.mindmymoney.helper.GeneralFunctions.formatFloat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents the Add command.
 */
public class AddCommand extends Command {
    private String addInput;
    public ExpenditureList expenditureList;
    public CreditCardList creditCardList;
    public IncomeList incomeList;

    public AddCommand(String addInput, User user) {
        this.addInput = addInput;
        this.expenditureList = user.getExpenditureListArray();
        this.creditCardList = user.getCreditCardListArray();
        this.incomeList = user.getIncomeListArray();
    }

    /**
     * Indicates whether the program should exit.
     *
     * @return true if the program should exit, false otherwise.
     */
    @Override
    public boolean isExit() {
        return false;
    }

    /**
     * Indicates whether the add command is to add a credit card by looking for the /cc flag.
     *
     * @return true if the /cc flag is present, false otherwise.
     */
    private boolean hasCreditCardFlag() {
        return addInput.contains(FLAG_OF_CREDIT_CARD);
    }

    /**
     * Updates the total expenditure field in the credit card specified in the expenditure item.
     *
     * @param cardName Name of credit card to be updated.
     * @param amount amount of new expenditure.
     * @throws MindMyMoneyException when the card is not found in user's credit card list.
     */
    private void updateCreditCardTotalExpenditure(String cardName, float amount) throws MindMyMoneyException {
        CreditCard creditCard = creditCardList.get(cardName);
        if (creditCard == null) {
            throw new MindMyMoneyException("Invalid Card Name!");
        }
        creditCard.addExpenditure(amount);
    }

    /**
     * Indicates whether the add command is to add an income by looking for the /i flag.
     *
     * @return true if the /i flag is present, false otherwise.
     */
    private boolean hasIncomeFlag() {
        return addInput.contains(FLAG_OF_INCOME);
    }

    /**
     * Inserts an Expenditure object into user's list of expenditure(s).
     *
     * @throws MindMyMoneyException when inputs are invalid or flags are missing.
     */
    public void addExpenditure() throws MindMyMoneyException {
        String paymentMethod = parseInputWithCommandFlag(addInput, FLAG_OF_PAYMENT_METHOD, FLAG_OF_CATEGORY);
        testPaymentMethod(paymentMethod, creditCardList);
        if (capitalise(paymentMethod).equals("Cash")) {
            paymentMethod = capitalise(paymentMethod);
        }

        String inputCategory = parseInputWithCommandFlag(addInput, FLAG_OF_CATEGORY, FLAG_OF_DESCRIPTION);
        testExpenditureCategory(inputCategory);

        String description = parseInputWithCommandFlag(addInput, FLAG_OF_DESCRIPTION, FLAG_OF_AMOUNT);
        testDescription(description);

        String amountAsString = parseInputWithCommandFlag(addInput, FLAG_OF_AMOUNT, FLAG_OF_TIME);
        testExpenditureAmount(amountAsString);

        String category = capitalise(inputCategory);

        float amountAsFloat = Float.parseFloat(amountAsString);
        float amountInt = formatFloat(amountAsFloat);

        String inputTime = parseInputWithCommandFlag(addInput, FLAG_OF_TIME, FLAG_END_VALUE);
        if (!isValidInput(inputTime)) {
            throw new MindMyMoneyException("Date has to be in this format \"dd/mm/yyyy\"");
        }
        LocalDate date = LocalDate.parse(inputTime, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String time = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        expenditureList.add(new Expenditure(paymentMethod, category, description, amountInt, time));

        if (!paymentMethod.equals("Cash")) {
            updateCreditCardTotalExpenditure(paymentMethod, amountInt);
        }

        System.out.println("Successfully added: \n\n"
            + "Description: " + description + "\n"
            + "Amount: $" + String.format("%.2f", amountInt) + "\n"
            + "Category: " + category + "\n"
            + "Payment method: " + paymentMethod + "\n"
            + "Date: " + time + "\n\n"
            + "into the account");
        System.out.print(System.lineSeparator());
    }

    /**
     * Inserts a CreditCard object into user's list of credit card(s).
     *
     * @throws MindMyMoneyException Exception thrown when input is invalid
     */
    public void addCreditCard() throws MindMyMoneyException {
        //Parse data from input
        final String cardName = parseInputWithCommandFlag(addInput, FLAG_OF_CARD_NAME,
                FLAG_OF_CASHBACK);

        final String cashBack = parseInputWithCommandFlag(addInput, FLAG_OF_CASHBACK,
                FLAG_OF_CARD_LIMIT);
        testCashbackAmount(cashBack);

        final String cardLimit = parseInputWithCommandFlag(addInput, FLAG_OF_CARD_LIMIT,
                FLAG_OF_CARD_BALANCE);
        testCreditCardLimit(cardLimit);

        final String cardBalance = parseInputWithCommandFlag(addInput, FLAG_OF_CARD_BALANCE,
                FLAG_END_VALUE);
        testCreditCardBalance(cardBalance);

        creditCardList.add(new CreditCard(cardName, Double.parseDouble(cashBack), Float.parseFloat(cardLimit),
                Float.parseFloat(cardBalance)));

        System.out.println("Successfully added: \n\n"
                + "Credit card: " + cardName + "\n"
                + "Cash back: " + cashBack + "%\n"
                + "Card limit: $" + cardLimit + "\n"
                + "Card balance: $" + cardBalance + "\n\n"
                + "into the account");
        System.out.print(System.lineSeparator());
    }

    /**
     * Inserts an Income object into user's list of income(s).
     *
     * @throws MindMyMoneyException when the input amount is not a number.
     */
    public void addIncome() throws MindMyMoneyException {
        String amountAsString = parseInputWithCommandFlag(addInput, FLAG_OF_AMOUNT, FLAG_OF_CATEGORY);

        try {
            int amountAsInt = Integer.parseInt(amountAsString);
            testIncomeAmount(amountAsInt);

            String inputCategory = parseInputWithCommandFlag(addInput, FLAG_OF_CATEGORY, FLAG_END_VALUE);
            testIncomeCategory(inputCategory);
            String category = capitalise(inputCategory);

            incomeList.add(new Income(amountAsInt, category));

            System.out.print("Successfully added: \n\n"
                    + "Amount: $" + amountAsInt + "\n"
                    + "Category: " + category + "\n\n"
                    + "into the account");
            System.out.println(System.lineSeparator());
        } catch (NumberFormatException e) {
            throw new MindMyMoneyException("Income must be a whole number!");
        }
    }

    /**
     * Inserts either an Expenditure, CreditCard or Income object into the user's list based on the input.
     *
     * @throws MindMyMoneyException when an invalid command is received, along with its corresponding error message.
     */
    @Override
    public void executeCommand() throws MindMyMoneyException {
        if (hasCreditCardFlag()) {
            addCreditCard();
        } else if (hasIncomeFlag()) {
            addIncome();
        } else {
            addExpenditure();
        }
    }
}
