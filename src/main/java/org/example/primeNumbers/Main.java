package org.example.primeNumbers;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;

/**
 * @author Michael Schmid
 */
public class Main {

    private final static int COLUMN_B = 1;          //column identifier - prerequisites say t
    private final static int CERTAINTY = 10;        //Certainty used for Rabin-Miller primality test
    private final static int MISSING_ARGUMENT_ERROR_CODE = 1;
    private final static int EXPECTED_NUMBER_OF_ARGUMENTS = 1;
    private final static int SHEET_INDEX = 0;

    public static void main(String[] args) {

        checkArguments(args);

        try (FileInputStream fileInputStream = new FileInputStream(new File(args[0]))) {        //Initiate input stream

            XSSFWorkbook wb = new XSSFWorkbook(fileInputStream);        //load workbook
            XSSFSheet sheet = wb.getSheetAt(SHEET_INDEX);               //load sheet

            printPrimeNumbers(sheet);

        } catch (FileNotFoundException e) {
            System.err.println("Specified file could not be found");
        } catch (IOException e) {
            System.err.println("Could not load workbook from file");
        }
    }

    private static void printPrimeNumbers(Sheet sheet) {
        for (Row row : sheet) {         //iterate over all rows

            BigInteger number;
            try {
                number = new BigInteger(row.getCell(COLUMN_B).getStringCellValue());        //Try to convert string value to BigInteger - this also provides the format check
            } catch (NumberFormatException | IllegalStateException e) {
                continue;       //Wrong format, try next
            }

            if (number.isProbablePrime(CERTAINTY)) {        //Test the number
                System.out.println(number);
            }
        }
    }

    private static void checkArguments(String[] args) {
        if (args.length < EXPECTED_NUMBER_OF_ARGUMENTS) {
            System.err.println("Missing argument - argument should be path to xlsx file");
            System.exit(MISSING_ARGUMENT_ERROR_CODE);
        }
    }
}