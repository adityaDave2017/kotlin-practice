package com.example.crud

import java.math.BigDecimal
import java.sql.*
import java.text.NumberFormat
import java.util.*


class CompanyDb {

    private val database: String = "./company.sqlite"

    private val createEmployeeTable: String = """
      CREATE TABLE IF NOT EXISTS employee(
          employee_id INTEGER PRIMARY KEY AUTOINCREMENT,
          first_name TEXT NOT NULL,
          middle_name TEXT,
          last_name TEXT NOT NULL,
          department TEXT NOT NULL,
          position TEXT NOT NULL,
          salary NUMERIC NOT NULL)
    """

    private val selectAllEmployees: String = """
    SELECT * FROM employee
    """

    private val insertEmployee: String = """
    INSERT INTO employee(first_name, middle_name, last_name, department, position, salary) VALUES(?, ?, ?, ?, ?, ?)
    """

    private val updateEmployeeDept: String = """
    UPDATE employee SET department = ? WHERE employee_id = ?
    """

    private val updateEmployeePosition: String = """
    UPDATE employee SET position = ? WHERE employee_id = ?
    """

    private val updateEmployeeSalary: String = """
    UPDATE employee SET Salary = ? WHERE employee_id = ?
    """

    private val deleteEmployee: String = """
    DELETE FROM employee WHERE employee_id = ?
    """

    init {
        createDatabase()
    }

    fun getInput(): MutableMap<String, String?> {
        val inputMap: MutableMap<String, String?> = mutableMapOf()

        print("\nEnter employee's first name: ")
        inputMap.put("first_name", readLine())

        print("Enter employee's middle name: ")
        inputMap.put("middle_name", readLine())

        print("Enter employee's last name: ")
        inputMap.put("last_name", readLine())

        print("Enter employee's department: ")
        inputMap.put("department", readLine())

        print("Enter employee's position: ")
        inputMap.put("position", readLine())

        print("Enter employee's salary: ")
        inputMap.put("salary", readLine())

        return inputMap
    }


    private fun getConnection(): Connection = DriverManager.getConnection("jdbc:sqlite:$database")


    private fun createDatabase(): Unit = getConnection().createStatement().use { stmt -> stmt.execute(createEmployeeTable) }


    fun getAllEmployees(): Unit {
        val connection: Connection = getConnection()
        connection.use {
            val stmt: Statement = connection.createStatement()
            stmt.use {
                val resultSet: ResultSet = stmt.executeQuery(selectAllEmployees)
                resultSet.use {
                    val formatter: Formatter = Formatter()
                    formatter.format("\n%-10s %-15s %-15s %-15s %-15s %-15s %-15s\n",
                            "id", "first_name", "middle_name", "last_name", "department", "position", "salary")
                    while (resultSet.next()) {
                        formatter.flush()
                        formatter.format("%-10d %-15s %-15s %-15s %-15s %-15s %-15s\n",
                                resultSet.getInt("employee_id"),
                                resultSet.getString("first_name"),
                                resultSet.getString("middle_name"),
                                resultSet.getString("last_name"),
                                resultSet.getString("department"),
                                resultSet.getString("position"),
                                NumberFormat.getCurrencyInstance(Locale("en", "IN"))
                                        .format(BigDecimal(resultSet.getString("salary"))))
                    }
                    print(formatter)
                    formatter.close()
                }
            }
        }
    }


    fun insertEmployee(): Unit {
        val input: MutableMap<String, String?> = getInput()
        val connection: Connection = getConnection()
        connection.use {
            val preparedStmt: PreparedStatement = connection.prepareStatement(insertEmployee)
            preparedStmt.use {
                preparedStmt.setString(1, input["first_name"])
                preparedStmt.setString(2, input["middle_name"])
                preparedStmt.setString(3, input["last_name"])
                preparedStmt.setString(4, input["department"])
                preparedStmt.setString(5, input["position"])
                preparedStmt.setString(6, input["salary"])
                preparedStmt.executeUpdate()
            }
        }
    }


    fun updateEmployee() {

        stop@ while (true) {
            println("\nMain Menu --> Update Options...\n")
            println("1. Update Department")
            println("2. Update Position")
            println("3. Update Salary")
            println("4. return")

            print("\nEnter your choice: ")
            val choice: Int = readLine()?.toInt() ?: -1

            print("\nEnter id of the employee: ")

            when (choice) {
                -1 -> continue@stop
                1 -> updateEmployeeDepartment(readLine()?.toInt() ?: -1)
                2 -> updateEmployeePosition(readLine()?.toInt() ?: -1)
                3 -> updateEmployeeSalary(readLine()?.toInt() ?: -1)
                4 -> break@stop
            }

        }

    }


    private fun updateEmployeeDepartment(employeeId: Int): Unit {
        print("\nEnter new department: ")
        val department: String = readLine() ?: ""
        val connection: Connection = getConnection()
        connection.use {
            val preparedStmt = connection.prepareStatement(updateEmployeeDept)
            preparedStmt.use {
                    preparedStmt.setString(1, department)
                    preparedStmt.setInt(2, employeeId)
                    preparedStmt.executeUpdate()
            }
        }
    }


    private fun updateEmployeePosition(employeeId: Int): Unit {

        print("\nEnter new position: ")
        val position: String = readLine() ?: ""
        val connection: Connection = getConnection()
        connection.use {
            val preparedStmt: PreparedStatement = connection.prepareStatement(updateEmployeePosition)
            preparedStmt.use {
                    preparedStmt.setString(1, position)
                    preparedStmt.setInt(2, employeeId)
                    preparedStmt.executeUpdate()
            }
        }
    }


    private fun updateEmployeeSalary(employeeId: Int): Unit {

        print("\nEnter new salary: ")
        val salary: String = readLine() ?: ""
        val connection: Connection = getConnection()
        connection.use {
            val preparedStmt: PreparedStatement = connection.prepareStatement(updateEmployeeSalary)
            preparedStmt.use {
                preparedStmt.setString(1, salary)
                preparedStmt.setInt(2, employeeId)
                preparedStmt.executeUpdate()
            }
        }
    }


    fun deleteEmployee(): Unit {
        print("\nEnter employee's id to be removed: ")
        val employeeId: Int = readLine()?.toInt() ?: -1

        print("\nDo you really want to remove the employee(y/n): ")
        val surety: Char = readLine()?.get(0) ?: '#'

        if (surety == 'Y' || surety == 'y') {
            val connection: Connection = getConnection()
            connection.use {
                val preparedStmt: PreparedStatement = connection.prepareStatement(deleteEmployee)
                preparedStmt.use {
                    preparedStmt.setInt(1, employeeId)
                    preparedStmt.executeUpdate()
                }
            }
        } else {
            println("\n\nNo action will be taken")
        }
    }

}
