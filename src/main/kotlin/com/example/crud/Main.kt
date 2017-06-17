package com.example.crud

/**
 * Deals with the basic database CRUD operations of a table of employees.
 * Table :
 *      CREATE TABLE employee(id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
 *                             first_name TEXT NOT NULL,
 *                             middle_name TEXT,
 *                             last_name TEXT NOT NULL,
 *                             department TEXT NOT NULL,
 *                             position TEXT NOT NULL,
 *                             salary NUMERIC NOT NULL);
 */

fun main(args: Array<String>) {

    stop@ while (true) {
        val company: CompanyDb = CompanyDb()

        when(askChoice()) {
            1 -> company.getAllEmployees()
            2 -> company.insertEmployee()
            3 -> company.updateEmployee()
            4 -> company.deleteEmployee()
            5 -> break@stop
        }
    }

}


fun askChoice(): Int {
    println("\n\nCompany Operations")
    println("1. Show all employees details")
    println("2. Enter new employee details")
    println("3. Update employee details")
    println("4. Remove an employee")
    println("5. Quit")

    print("\n\nEnter your choice: ")
    try {
        val choice: Int = readLine()?.toInt() ?: -1
        if (choice == -1) {
            return askChoice()
        }
        return choice
    } catch (exc: NumberFormatException) {
        println("\nTry Again...")
    }
    return askChoice()
}