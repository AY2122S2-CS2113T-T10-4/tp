package seedu.mindmymoney.command;

import seedu.mindmymoney.MindMyMoneyException;
import seedu.mindmymoney.data.CreditCardList;
import seedu.mindmymoney.data.ExpenditureList;
import seedu.mindmymoney.data.IncomeList;
import seedu.mindmymoney.userfinancial.CreditCard;
import seedu.mindmymoney.userfinancial.Expenditure;
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
import static seedu.mindmymoney.constants.Flags.FLAG_OF_INCOME;
import static seedu.mindmymoney.constants.Indexes.SPLIT_LIMIT;
import static seedu.mindmymoney.constants.Indexes.LIST_INDEX_CORRECTION;
import static seedu.mindmymoney.constants.Indexes.INDEX_OF_FIRST_ITEM;
import static seedu.mindmymoney.constants.Indexes.INDEX_OF_SECOND_ITEM;

import static seedu.mindmymoney.helper.AddCommandInputTests.testIncomeAmount;
import static seedu.mindmymoney.helper.AddCommandInputTests.testIncomeCategory;
import static seedu.mindmymoney.helper.GeneralFunctions.capitalise;
import static seedu.mindmymoney.helper.GeneralFunctions.parseInputWithCommandFlag;

/**
 * Represents the Update command.
 */
public class UpdateCommand extends Command {
    private final String updateInput;
    public ExpenditureList itemList;
    public CreditCardList creditCardList;
    public IncomeList incomeList;

    public UpdateCommand(String updateInput, User user) {
        this.updateInput = updateInput;
        this.itemList = user.getExpenditureListArray();
        this.creditCardList = user.getCreditCardListArray();
        this.incomeList = user.getIncomeListArray();
    }

    /**
     * Indicates whether the program should exit.
     *
     * @return Indication on whether the program should exit.
     */
    @Override
    public boolean isExit() {
        return false;
    }

    /**
     * Indicates whether the update command is to update a credit card by looking for the /cc flag.
     *
     * @return true if the /cc flag is present, false otherwise.
     */
    private boolean hasCreditCardFlag() {
        return updateInput.contains(FLAG_OF_CREDIT_CARD);
    }

    /**
     * Indicates whether the update command is to update an income by looking for the /i flag.
     *
     * @return true if the /i flag is present, false otherwise.
     */
    private boolean hasIncomeFlag() {
        return updateInput.contains(FLAG_OF_INCOME);
    }

    /**
     * Updates an Expenditure entry in user's expenditure list.
     *
     * @throws MindMyMoneyException when an invalid command is received.
     */
    public void updateExpenditure() throws MindMyMoneyException {
        try {
            String description;
            String category = null;
            String[] parseUpdateInput = updateInput.split(" ", SPLIT_LIMIT);

            // get the index to update, amount, description
            String indexString = parseUpdateInput[INDEX_OF_FIRST_ITEM];
            String expenditureDescription = parseUpdateInput[INDEX_OF_SECOND_ITEM];
            int divisionIndex = expenditureDescription.lastIndexOf(" ");
            String descriptionAndCategory = expenditureDescription.substring(INDEX_OF_FIRST_ITEM,
                    divisionIndex).strip();
            if (descriptionAndCategory.contains("-c ")) {
                descriptionAndCategory = descriptionAndCategory.replace("-c ", "");
                int divisionIndexForCategory = descriptionAndCategory.lastIndexOf(" ");
                description = descriptionAndCategory.substring(INDEX_OF_FIRST_ITEM,
                        divisionIndexForCategory).strip();
                category = descriptionAndCategory.substring(divisionIndexForCategory).strip();
            } else {
                description = expenditureDescription.substring(INDEX_OF_FIRST_ITEM,
                        divisionIndex).strip();
            }
            String amountString = expenditureDescription.substring(divisionIndex).strip();
            int indexToUpdate = Integer.parseInt(indexString) + LIST_INDEX_CORRECTION;

            //to edit to fit new add command
            Expenditure newExpenditure = new Expenditure("cash", category, description,
                    Integer.parseInt(amountString), "2022-02");
            itemList.set(indexToUpdate, newExpenditure);
            System.out.printf("Successfully set expenditure %d to %s\n" + System.lineSeparator(),
                    indexToUpdate - LIST_INDEX_CORRECTION, newExpenditure);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new MindMyMoneyException("Did you forget to input INDEX, DESCRIPTION or AMOUNT?");
        } catch (NumberFormatException e) {
            throw new MindMyMoneyException("AMOUNT and INDEX must be a number");
        } catch (IndexOutOfBoundsException e) {
            throw new MindMyMoneyException("Please input a valid index");
        }
    }

    /**
     * Updates a Credit Card entry in user's credit card list.
     *
     * @throws MindMyMoneyException when an invalid command is received.
     */
    public void updateCreditCard() throws MindMyMoneyException {
        try {
            String[] parseUpdateInput = updateInput.split(" ");

            //Get index to update
            String indexString = parseUpdateInput[INDEX_OF_SECOND_ITEM];

            //Parse data from input
            String newCardName = parseInputWithCommandFlag(updateInput, FLAG_OF_CARD_NAME,
                    FLAG_OF_CASHBACK);
            String newCashBack = parseInputWithCommandFlag(updateInput, FLAG_OF_CASHBACK,
                    FLAG_OF_CARD_LIMIT);
            String newCardLimit = parseInputWithCommandFlag(updateInput, FLAG_OF_CARD_LIMIT,
                    FLAG_OF_CARD_BALANCE);
            String newCardBalance = parseInputWithCommandFlag(updateInput, FLAG_OF_CARD_BALANCE,
                    FLAG_END_VALUE);
            int indexToUpdate = Integer.parseInt(indexString) + LIST_INDEX_CORRECTION;

            //to edit to fit new add command
            CreditCard newCreditCard = new CreditCard(newCardName, Double.parseDouble(newCashBack),
                    Float.parseFloat(newCardLimit), Float.parseFloat(newCardBalance));

            creditCardList.set(indexToUpdate, newCreditCard);
            System.out.printf("Successfully set credit card %d\n" + System.lineSeparator(),
                    indexToUpdate - LIST_INDEX_CORRECTION);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new MindMyMoneyException("Did you forget to input NAME, CASHBACK, CREDIT LIMIT or BALANCE?");
        } catch (NumberFormatException e) {
            throw new MindMyMoneyException("CASHBACK, CREDIT LIMIT and BALANCE must be a number");
        } catch (IndexOutOfBoundsException e) {
            throw new MindMyMoneyException("Please input a valid index");
        }
    }

    /**
     * Updates an Income entry in user's income list.
     *
     * @throws MindMyMoneyException when an invalid command is received.
     */
    public void updateIncome() throws MindMyMoneyException {
        try {
            String[] parseUpdateInput = updateInput.split(" ");

            //Get index to update
            String indexString = parseUpdateInput[INDEX_OF_SECOND_ITEM];
            int indexToUpdate = Integer.parseInt(indexString) + LIST_INDEX_CORRECTION;

            //Parse data from input
            String newAmountAsString = parseInputWithCommandFlag(updateInput, FLAG_OF_AMOUNT,
                    FLAG_OF_CATEGORY);
            int newAmountAsInt = Integer.parseInt(newAmountAsString);
            testIncomeAmount(newAmountAsInt);

            String inputCategory = parseInputWithCommandFlag(updateInput, FLAG_OF_CATEGORY,
                    FLAG_END_VALUE);
            testIncomeCategory(inputCategory);
            String newCategory = capitalise(inputCategory);

            Income newIncome = new Income(newAmountAsInt, newCategory);
            incomeList.set(indexToUpdate, newIncome);

            System.out.printf("Successfully set income %d\n" + System.lineSeparator(),
                    indexToUpdate - LIST_INDEX_CORRECTION);

        } catch (ArrayIndexOutOfBoundsException e) {
            throw new MindMyMoneyException("Did you forget to input AMOUNT or CATEGORY?");
        } catch (NumberFormatException e) {
            throw new MindMyMoneyException("AMOUNT must be a number");
        } catch (IndexOutOfBoundsException e) {
            throw new MindMyMoneyException("Please input a valid index");
        }
    }

    /**
     * Updates either an Expenditure, Credit Card or Income entry based on the user's input.
     *
     * @throws MindMyMoneyException when an invalid command is received, along with its corresponding error message.
     */
    @Override
    public void executeCommand() throws MindMyMoneyException {
        if (hasCreditCardFlag()) {
            updateCreditCard();
        } else if (hasIncomeFlag()) {
            updateIncome();
        } else {
            updateExpenditure();
        }
    }
}
