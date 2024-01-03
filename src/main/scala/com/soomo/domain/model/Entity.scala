package com.soomo.domain.model

import zio.json.{DeriveJsonCodec, JsonCodec}

case class Department(id: Int = 0, name: String, manager: String)

object Department:
  given JsonCodec[Department] = DeriveJsonCodec.gen[Department]

case class Position(id: Int = 0, title: String, salary: BigDecimal)

object Position:
  given JsonCodec[Position] = DeriveJsonCodec.gen[Position]

case class Employee(id: Int = 0, firstName: String, lastName: String, departmentId: Int, positionId: Int)

object Employee:
  given JsonCodec[Employee] = DeriveJsonCodec.gen[Employee]

case class Timesheet(id: Int = 0, date: String, hours: Int, employeeId: Int)

object Timesheet:
  given JsonCodec[Timesheet] = DeriveJsonCodec.gen[Timesheet]

case class Payroll(id: Int = 0, amount: BigDecimal, paymentDate: String, employeeId: Int)

object Payroll:
  given JsonCodec[Payroll] = DeriveJsonCodec.gen[Payroll]

case class TaxDeduction(id: Int = 0, deductionType: String, rate: BigDecimal, payrollId: Int)

object TaxDeduction:
  given JsonCodec[TaxDeduction] = DeriveJsonCodec.gen[TaxDeduction]

