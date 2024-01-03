package com.soomo.dao

import com.soomo.domain.ServiceError
import com.soomo.domain.model.*
import zio.{IO, ZIO}

/**
 * Трейт `DepartmentDao` надає інтерфейс для доступу до даних відділів.
 *
 * Він містить методи для додавання, отримання, пошуку за ID та видалення даних відділів.
 */
trait DepartmentDao:
  def addAll(departments: Seq[Department]): IO[ServiceError, Unit]
  def getAll(): IO[ServiceError, Seq[Department]]
  def getById(id: Int): IO[ServiceError, Department]
  def deleteById(id: Int): IO[ServiceError, Long]

/**
 * Компаньйон-об'єкт для `trait DepartmentDao`.
 *
 * Надає методи для роботи з `DepartmentDao` в контексті ZIO.
 */
object DepartmentDao:
  def addAll(departments: Seq[Department]): ZIO[DepartmentDao, ServiceError, Unit] =
    ZIO.serviceWithZIO(_.addAll(departments))
  def getAll(): ZIO[DepartmentDao, ServiceError, Seq[Department]] =
    ZIO.serviceWithZIO(_.getAll())
  def getById(id: Int): ZIO[DepartmentDao, ServiceError, Department] =
    ZIO.serviceWithZIO(_.getById(id))
  def deleteById(id: Int): ZIO[DepartmentDao, ServiceError, Long] =
    ZIO.serviceWithZIO(_.deleteById(id))

trait PositionDao:
  def addAll(positions: Seq[Position]): IO[ServiceError, Unit]
  def getAll(): IO[ServiceError, Seq[Position]]
  def getById(id: Int): IO[ServiceError, Position]
  def deleteById(id: Int): IO[ServiceError, Long]

object PositionDao:
  def addAll(positions: Seq[Position]): ZIO[PositionDao, ServiceError, Unit] =
    ZIO.serviceWithZIO(_.addAll(positions))
  def getAll(): ZIO[PositionDao, ServiceError, Seq[Position]] =
    ZIO.serviceWithZIO(_.getAll())
  def getById(id: Int): ZIO[PositionDao, ServiceError, Position] =
    ZIO.serviceWithZIO(_.getById(id))
  def deleteById(id: Int): ZIO[PositionDao, ServiceError, Long] =
    ZIO.serviceWithZIO(_.deleteById(id))

trait EmployeeDao:
  def addAll(employees: Seq[Employee]): IO[ServiceError, Unit]
  def getAll(): IO[ServiceError, Seq[Employee]]
  def getById(id: Int): IO[ServiceError, Employee]
  def deleteById(id: Int): IO[ServiceError, Long]

object EmployeeDao:
  def addAll(employees: Seq[Employee]): ZIO[EmployeeDao, ServiceError, Unit] =
    ZIO.serviceWithZIO(_.addAll(employees))
  def getAll(): ZIO[EmployeeDao, ServiceError, Seq[Employee]] =
    ZIO.serviceWithZIO(_.getAll())
  def getById(id: Int): ZIO[EmployeeDao, ServiceError, Employee] =
    ZIO.serviceWithZIO(_.getById(id))
  def deleteById(id: Int): ZIO[EmployeeDao, ServiceError, Long] =
    ZIO.serviceWithZIO(_.deleteById(id))

trait TimesheetDao:
  def addAll(timesheets: Seq[Timesheet]): IO[ServiceError, Unit]
  def getAll(): IO[ServiceError, Seq[Timesheet]]
  def getById(id: Int): IO[ServiceError, Timesheet]
  def deleteById(id: Int): IO[ServiceError, Long]

object TimesheetDao:
  def addAll(timesheets: Seq[Timesheet]): ZIO[TimesheetDao, ServiceError, Unit] =
    ZIO.serviceWithZIO(_.addAll(timesheets))
  def getAll(): ZIO[TimesheetDao, ServiceError, Seq[Timesheet]] =
    ZIO.serviceWithZIO(_.getAll())
  def getById(id: Int): ZIO[TimesheetDao, ServiceError, Timesheet] =
    ZIO.serviceWithZIO(_.getById(id))
  def deleteById(id: Int): ZIO[TimesheetDao, ServiceError, Long] =
    ZIO.serviceWithZIO(_.deleteById(id))

trait PayrollDao:
  def addAll(payrolls: Seq[Payroll]): IO[ServiceError, Unit]
  def getAll(): IO[ServiceError, Seq[Payroll]]
  def getById(id: Int): IO[ServiceError, Payroll]
  def deleteById(id: Int): IO[ServiceError, Long]

object PayrollDao:
  def addAll(payrolls: Seq[Payroll]): ZIO[PayrollDao, ServiceError, Unit] =
    ZIO.serviceWithZIO(_.addAll(payrolls))
  def getAll(): ZIO[PayrollDao, ServiceError, Seq[Payroll]] =
    ZIO.serviceWithZIO(_.getAll())
  def getById(id: Int): ZIO[PayrollDao, ServiceError, Payroll] =
    ZIO.serviceWithZIO(_.getById(id))
  def deleteById(id: Int): ZIO[PayrollDao, ServiceError, Long] =
    ZIO.serviceWithZIO(_.deleteById(id))

trait TaxDeductionDao:
  def addAll(deductions: Seq[TaxDeduction]): IO[ServiceError, Unit]
  def getAll(): IO[ServiceError, Seq[TaxDeduction]]
  def getById(id: Int): IO[ServiceError, TaxDeduction]
  def deleteById(id: Int): IO[ServiceError, Long]

object TaxDeductionDao:
  def addAll(deductions: Seq[TaxDeduction]): ZIO[TaxDeductionDao, ServiceError, Unit] =
    ZIO.serviceWithZIO(_.addAll(deductions))
  def getAll(): ZIO[TaxDeductionDao, ServiceError, Seq[TaxDeduction]] =
    ZIO.serviceWithZIO(_.getAll())
  def getById(id: Int): ZIO[TaxDeductionDao, ServiceError, TaxDeduction] =
    ZIO.serviceWithZIO(_.getById(id))
  def deleteById(id: Int): ZIO[TaxDeductionDao, ServiceError, Long] =
    ZIO.serviceWithZIO(_.deleteById(id))

