package com.soomo

import com.soomo.dao.*
import com.soomo.domain.model.*
import zio.*
import zio.config.*
import zio.http.*
import zio.http.ChannelEvent.ChannelRead
import zio.http.socket.{WebSocketChannelEvent, WebSocketFrame}
import zio.json.*

import java.time.{Duration, Instant}
object DBHandler:
  def processCommand(
      commandTarget: String,
      commandAction: String,
      parameter: String
  ): ZIO[DepartmentDao with CommonDao[Department] with PositionDao with EmployeeDao with TimesheetDao with PayrollDao with TaxDeductionDao, Nothing, String] =
    commandTarget match
      case "Department"   => handleDepartment(commandAction, parameter)
      case "Position"     => handlePosition(commandAction, parameter)
      case "Employee"     => handleEmployee(commandAction, parameter)
      case "Timesheet"    => handleTimesheet(commandAction, parameter)
      case "Payroll"      => handlePayroll(commandAction, parameter)
      case "TaxDeduction" => handleTaxDeduction(commandAction, parameter)
      case _ =>
        ZIO.succeed(
          "Wrong command:\n" +
            "commandTarget: " + commandTarget + "\n" +
            "commandAction: " + commandAction + "\n" +
            "parameter: " + parameter
        )

private def handleDepartment(commandAction: String, parameter: String) =
  commandAction match
    case "addAll" =>
      (for {
        departments <- ZIO.fromEither(parameter.fromJson[Seq[Department]])
        _           <- DepartmentDao.addAll(departments)
      } yield "Added successfully")
        .catchAll(e => ZIO.succeed(e.toString))
        .logError("Workflow Error " + ("=" * 20))
    case "getAll" =>
      (for {
        departments <- DepartmentDao.getAll()
        departmentsJson = departments.map(_.toJson).mkString("[", ",", "]")
      } yield departmentsJson)
        .catchAll(e => ZIO.succeed(e.toString))
        .logError("Workflow Error " + ("=" * 20))
    case "getById" =>
      (for {
        department <- DepartmentDao.getById(parameter.toInt)
        departmentJson = department.toJson
      } yield departmentJson)
        .catchAll(e => ZIO.succeed(e.toString))
        .logError("Workflow Error " + ("=" * 20))
    case "deleteById" =>
      (for {
        deletedDepartmentNum <- DepartmentDao.deleteById(parameter.toInt)
      } yield s"Deleted successfully. Number of records affected: $deletedDepartmentNum")
        .catchAll(e => ZIO.succeed(e.toString))
        .logError("Workflow Error " + ("=" * 20))
    case _ =>
      ZIO.succeed(
        "Wrong command:\n" +
          "commandAction: " + commandAction + "\n" +
          "parameter: " + parameter
      )

private def handlePosition(commandAction: String, parameter: String) = commandAction match
  case "addAll" =>
    (for {
      positions <- ZIO.fromEither(parameter.fromJson[Seq[Position]])
      _         <- PositionDao.addAll(positions)
    } yield "Added successfully")
      .catchAll(e => ZIO.succeed(e.toString))
      .logError("Workflow Error " + ("=" * 20))
  case "getAll" =>
    (for {
      positions <- PositionDao.getAll()
      positionsJson = positions.map(_.toJson).mkString("[", ",", "]")
    } yield positionsJson)
      .catchAll(e => ZIO.succeed(e.toString))
      .logError("Workflow Error " + ("=" * 20))
  case "getById" =>
    (for {
      position <- PositionDao.getById(parameter.toInt)
      positionJson = position.toJson
    } yield positionJson)
      .catchAll(e => ZIO.succeed(e.toString))
      .logError("Workflow Error " + ("=" * 20))
  case "deleteById" =>
    (for {
      deletedPositionNum <- PositionDao.deleteById(parameter.toInt)
    } yield s"Deleted successfully. Number of records affected: $deletedPositionNum")
      .catchAll(e => ZIO.succeed(e.toString))
      .logError("Workflow Error " + ("=" * 20))
  case _ =>
    ZIO.succeed(
      "Wrong command:\n" +
        "commandAction: " + commandAction + "\n" +
        "parameter: " + parameter
    )
private def handleEmployee(commandAction: String, parameter: String) = commandAction match
  case "addAll" =>
    (for {
      employees <- ZIO.fromEither(parameter.fromJson[Seq[Employee]])
      _         <- EmployeeDao.addAll(employees)
    } yield "Added successfully")
      .catchAll(e => ZIO.succeed(e.toString))
      .logError("Workflow Error " + ("=" * 20))
  case "getAll" =>
    (for {
      employees <- EmployeeDao.getAll()
      employeesJson = employees.map(_.toJson).mkString("[", ",", "]")
    } yield employeesJson)
      .catchAll(e => ZIO.succeed(e.toString))
      .logError("Workflow Error " + ("=" * 20))
  case "getById" =>
    (for {
      employee <- EmployeeDao.getById(parameter.toInt)
      employeeJson = employee.toJson
    } yield employeeJson)
      .catchAll(e => ZIO.succeed(e.toString))
      .logError("Workflow Error " + ("=" * 20))
  case "deleteById" =>
    (for {
      deletedEmployeeNum <- EmployeeDao.deleteById(parameter.toInt)
    } yield s"Deleted successfully. Number of records affected: $deletedEmployeeNum")
      .catchAll(e => ZIO.succeed(e.toString))
      .logError("Workflow Error " + ("=" * 20))
  case _ =>
    ZIO.succeed(
      "Wrong command:\n" +
        "commandAction: " + commandAction + "\n" +
        "parameter: " + parameter
    )

private def handleTimesheet(commandAction: String, parameter: String) = commandAction match
  case "addAll" =>
    (for {
      timesheets <- ZIO.fromEither(parameter.fromJson[Seq[Timesheet]])
      _          <- TimesheetDao.addAll(timesheets)
    } yield " Added successfully")
      .catchAll(e => ZIO.succeed(e.toString))
      .logError("Workflow Error " + ("=" * 20))
  case "getAll" =>
    (for {
      timesheets <- TimesheetDao.getAll()
      timesheetsJson = timesheets.map(_.toJson).mkString("[", ",", "]")
    } yield timesheetsJson)
      .catchAll(e => ZIO.succeed(e.toString))
      .logError("Workflow Error " + ("=" * 20))
  case "getById" =>
    (for {
      timesheet <- TimesheetDao.getById(parameter.toInt)
      timesheetJson = timesheet.toJson
    } yield timesheetJson)
      .catchAll(e => ZIO.succeed(e.toString))
      .logError("Workflow Error " + ("=" * 20))
  case "deleteById" =>
    (for {
      deletedTimesheetNum <- TimesheetDao.deleteById(parameter.toInt)
    } yield s"Deleted successfully. Number of records affected: $deletedTimesheetNum")
      .catchAll(e => ZIO.succeed(e.toString))
      .logError("Workflow Error " + ("=" * 20))
  case _ =>
    ZIO.succeed(
      "Wrong command:\n" +
        "commandAction: " + commandAction + "\n" +
        "parameter: " + parameter
    )
private def handlePayroll(commandAction: String, parameter: String): ZIO[PayrollDao, Nothing, String] =
  commandAction match
    case "addAll" =>
      (for {
        payrolls <- ZIO.fromEither(parameter.fromJson[Seq[Payroll]])
        _        <- PayrollDao.addAll(payrolls)
      } yield "Added successfully")
        .catchAll(e => ZIO.succeed(e.toString))
        .logError("Workflow Error " + ("=" * 20))
    case "getAll" =>
      (for {
        payrolls <- PayrollDao.getAll()
        payrollsJson = payrolls.map(_.toJson).mkString("[", ",", "]")
      } yield payrollsJson)
        .catchAll(e => ZIO.succeed(e.toString))
        .logError("Workflow Error " + ("=" * 20))
    case "getById" =>
      (for {
        payroll <- PayrollDao.getById(parameter.toInt)
        payrollJson = payroll.toJson
      } yield payrollJson)
        .catchAll(e => ZIO.succeed(e.toString))
        .logError("Workflow Error " + ("=" * 20))
    case "deleteById" =>
      (for {
        deletedPayrollNum <- PayrollDao.deleteById(parameter.toInt)
      } yield s"Deleted successfully. Number of records affected: $deletedPayrollNum")
        .catchAll(e => ZIO.succeed(e.toString))
        .logError("Workflow Error " + ("=" * 20))
    case _ =>
      ZIO.succeed(
        "Wrong command:\n" +
          "commandAction: " + commandAction + "\n" +
          "parameter: " + parameter
      )

private def handleTaxDeduction(commandAction: String, parameter: String) = commandAction match
  case "addAll" =>
    (for {
      taxDeductions <- ZIO.fromEither(parameter.fromJson[Seq[TaxDeduction]])
      _             <- TaxDeductionDao.addAll(taxDeductions)
    } yield "Added successfully")
      .catchAll(e => ZIO.succeed(e.toString))
      .logError("Workflow Error " + ("=" * 20))
  case "getAll" =>
    (for {
      taxDeductions <- TaxDeductionDao.getAll()
      taxDeductionsJson = taxDeductions.map(_.toJson).mkString("[", ",", "]")
    } yield taxDeductionsJson)
      .catchAll(e => ZIO.succeed(e.toString))
      .logError("Workflow Error " + ("=" * 20))
  case "getById" =>
    (for {
      taxDeduction <- TaxDeductionDao.getById(parameter.toInt)
      taxDeductionJson = taxDeduction.toJson
    } yield taxDeductionJson)
      .catchAll(e => ZIO.succeed(e.toString))
      .logError("Workflow Error " + ("=" * 20))
  case "deleteById" =>
    (for {
      deletedTaxDeductionNum <- TaxDeductionDao.deleteById(parameter.toInt)
    } yield s"Deleted successfully. Number of records affected: $deletedTaxDeductionNum")
      .catchAll(e => ZIO.succeed(e.toString))
      .logError("Workflow Error " + ("=" * 20))
  case _ =>
    ZIO.succeed(
      "Wrong command:\n" +
        "commandAction: " + commandAction + "\n" +
        "parameter: " + parameter
    )