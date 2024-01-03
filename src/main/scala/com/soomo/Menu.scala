package com.soomo

import com.soomo.dao.*
import com.soomo.domain.ServiceError
import com.soomo.domain.ServiceError.*
import com.soomo.flyway.{Flyway, FlywayProvider}
import zio.*

final case class Menu() {
  private def display(): UIO[Unit] =
    ZIO.succeed {
      println("Виберіть дію:")
      println("1. Додати запис")
      println("2. Отримати всі записи")
      println("3. Отримати запис за ID")
      println("4. Видалити запис")
      println("0. Вийти")
    }

  private def selectRecordType(): UIO[Int] = {
    ZIO.succeed {
      println("Виберіть тип запису:")
      println("1. Відділ")
      println("2. Посада")
      println("3. Працівник")
      println("4. Табель")
      println("5. Заробітна плата")
      println("6. Податкові відрахування")
      println("7. Повернутися до головного меню")
      scala.io.StdIn.readInt()
    }
  }

  private def processAllRecordsSelection(recordType: Int): ZIO[DepartmentDao with PositionDao with EmployeeDao with TimesheetDao with PayrollDao with TaxDeductionDao, ServiceError,  Unit] = recordType match {
    case 1 =>
      for {
        departments <- DepartmentDao.getAll()
        _ <- ZIO.foreachDiscard(departments)(department => ZIO.succeed(println(department)))
      } yield ()
    case 2 =>
      for {
        positions <- PositionDao.getAll()
        _ <- ZIO.foreachDiscard(positions)(position => ZIO.succeed(println(position)))
      } yield ()
    case 3 =>
      for {
        employees <- EmployeeDao.getAll()
        _ <- ZIO.foreachDiscard(employees)(employee => ZIO.succeed(println(employee)))
      } yield ()
    case 4 =>
      for {
        timesheets <- TimesheetDao.getAll()
        _ <- ZIO.foreachDiscard(timesheets)(timesheet => ZIO.succeed(println(timesheet)))
      } yield ()
    case 5 =>
      for {
        payrolls <- PayrollDao.getAll()
        _ <- ZIO.foreachDiscard(payrolls)(payroll => ZIO.succeed(println(payroll)))
      } yield ()
    case 6 =>
      for {
        taxDeductions <- TaxDeductionDao.getAll()
        _ <- ZIO.foreachDiscard(taxDeductions)(taxDeduction => ZIO.succeed(println(taxDeduction)))
      } yield ()
    case 7 => ZIO.unit // Повернення до головного меню
    case _ => ZIO.succeed(println("Невірний вибір. Спробуйте ще раз."))
  }

  private def getChoice: UIO[Int] =
    ZIO.attempt(scala.io.StdIn.readInt()).orElse(ZIO.succeed(-1))

  def displayAllIds[T](dao: CommonDao[T]): ZIO[Any, ServiceError, Unit] =
    for {
      records <- dao.getAll()
      _ <- ZIO.foreachDiscard(records) {
        record =>
          ZIO.succeed(println(s"ID: ${record.toString}"))
      }
    } yield ()

  private def processChoice(choice: Int): ZIO[DepartmentDao with PositionDao with EmployeeDao with TimesheetDao with PayrollDao with TaxDeductionDao, ServiceError, Unit] = choice match {
    case 1 => ??? // логіка додавання запису
    case 2 => for {
      recordType <- selectRecordType()
      _ <- processAllRecordsSelection(recordType)
    } yield ()
    case 3 => for {
      recordType <- selectRecordTypeForId()
      _ <- recordType match {
        case 1 =>
          for {
            departments <- DepartmentDao.getAll()
            _ <- ZIO.foreachDiscard(departments)(department => ZIO.succeed(println(department.id)))
          } yield ()
        //case 2 => displayAllIds(CommonDao)
       // case 3 => displayAllIds(CommonDao[EmployeeDao])
        // Інші випадки для різних типів записів
        case _ => ZIO.succeed(println("Невірний тип запису."))
      }
      id <- getIdFromUser()
      _ <- processRecordByIdSelection(recordType, id)
    } yield ()
    case 4 => ??? // логіка видалення запису
    case 0 => ZIO.succeed(println("Виходжу..."))
    case _ => ZIO.succeed(println("Невірний вибір. Спробуйте ще раз."))
  }
  def runMenu: ZIO[DepartmentDao with PositionDao with EmployeeDao with TimesheetDao with PayrollDao with TaxDeductionDao, Throwable, Unit] =
    (for {
      _ <- display()
      choice <- getChoice
      _ <- processChoice(choice)
      _ <- runMenu.unless(choice == 0)
    } yield ()).mapError(e => new Throwable(e.toString))

  private def selectRecordTypeForId(): UIO[Int] = {
    ZIO.succeed {
      println("Виберіть тип запису для отримання за ID:")
      println("1. Відділ")
      println("2. Посада")
      println("3. Працівник")
      println("4. Табель")
      println("5. Заробітна плата")
      println("6. Податкові відрахування")
      scala.io.StdIn.readInt()
    }
  }

  private def getIdFromUser(): UIO[Int] = {
    ZIO.succeed {
      println("Введіть ID:")
      scala.io.StdIn.readInt()
    }
  }

  private def processRecordByIdSelection(recordType: Int, id: Int) = recordType match {
    case 1 => for {
      departments <- DepartmentDao.getAll()
      _ <- ZIO.foreachDiscard(departments) {
        department =>
          ZIO.succeed(println(s"ID: ${department.id}, Назва: ${department.name}"))
      }
    } yield ()
    case 2 => ??? // логіка отримання запису посади за ID
    case 3 => ??? // логіка отримання запису працівника за ID
    case 4 => ??? // логіка отримання запису табелю за ID
    case 5 => ??? // логіка отримання запису заробітної плати за ID
    case 6 => ??? // логіка отримання запису податкових відрахувань за ID
    case _ => ZIO.succeed(println("Невірний вибір. Спробуйте ще раз."))
  }
}
object Menu:
  val live: ZLayer[Any, Nothing, Menu] =
    ZLayer.succeed(Menu())